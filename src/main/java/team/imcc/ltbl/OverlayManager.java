package team.imcc.ltbl;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Drives every active overlay on the server thread.
 *
 * <p>The scan volume is walked one 16x16x16 chunk section at a time, a few sections per tick, so a
 * full pass costs a fraction of a millisecond per tick instead of a multi millisecond spike. Each
 * section that is scanned immediately emits its particles, which makes the overlay ripple outwards
 * from the player instead of appearing all at once.
 */
public final class OverlayManager {
	/** Re-centre the scan when the player moved further than this from the last anchor. */
	private static final int REBUILD_DISTANCE = 12;

	private static final Map<UUID, Session> SESSIONS = new HashMap<>();

	private OverlayManager() {
	}

	public static boolean isEnabled(UUID playerId) {
		return SESSIONS.containsKey(playerId);
	}

	public static void enable(ServerPlayer player) {
		Session session = new Session();
		session.radius = LtblConfig.get().radius;
		session.rebuild(player);
		SESSIONS.put(player.getUUID(), session);
	}

	public static void disable(UUID playerId) {
		SESSIONS.remove(playerId);
	}

	public static Session session(UUID playerId) {
		return SESSIONS.get(playerId);
	}

	public static void clear() {
		SESSIONS.clear();
	}

	public static void onServerTick(MinecraftServer server) {
		if (SESSIONS.isEmpty()) {
			return;
		}

		LtblConfig config = LtblConfig.get();
		ParticleOptions defaultParticle = ParticleStyle.parse(config.particle).particle();
		Iterator<Map.Entry<UUID, Session>> iterator = SESSIONS.entrySet().iterator();

		while (iterator.hasNext()) {
			Map.Entry<UUID, Session> entry = iterator.next();
			ServerPlayer player = server.getPlayerList().getPlayer(entry.getKey());

			if (player == null || player.isRemoved()) {
				iterator.remove();
				continue;
			}

			try {
				entry.getValue().tick(player, config, defaultParticle);
			} catch (RuntimeException e) {
				// Never let one broken overlay take the server tick loop down with it.
				iterator.remove();
				LtblMod.LOGGER.error("LTBL overlay for {} failed and was switched off", player.getGameProfile().name(), e);
			}
		}
	}

	static final class Session {
		private ResourceKey<Level> dimension;
		private int radius = 32;
		/** -1 means "use the dimension's own spawn light limit". */
		private int lightLimit = -1;
		/** null means "use the particle from the config file". */
		private ParticleStyle style;
		private int anchorX;
		private int anchorY;
		private int anchorZ;

		private int minSectionX;
		private int minSectionY;
		private int minSectionZ;
		private int countX;
		private int countY;
		private int countZ;
		private int totalSections;
		private int cursor;

		private int rotation;
		private final List<BlockPos> hits = new ArrayList<>();

		int radius() {
			return radius;
		}

		int lightLimit() {
			return lightLimit;
		}

		ParticleStyle style() {
			return style;
		}

		void setStyle(ParticleStyle newStyle) {
			style = newStyle;
		}

		void setRadius(ServerPlayer player, int newRadius) {
			radius = Math.max(LtblConfig.MIN_RADIUS, Math.min(LtblConfig.MAX_RADIUS, newRadius));
			rebuild(player);
		}

		void setLightLimit(int limit) {
			lightLimit = limit;
		}

		String cycleSeconds() {
			int perTick = Math.max(1, LtblConfig.get().sectionsPerTick);
			double seconds = totalSections / (double) perTick / 20.0;
			return String.format(java.util.Locale.ROOT, "%.1f", seconds);
		}

		void rebuild(ServerPlayer player) {
			ServerLevel level = player.level();
			int px = player.getBlockX();
			int py = player.getBlockY();
			int pz = player.getBlockZ();

			dimension = level.dimension();
			anchorX = px;
			anchorY = py;
			anchorZ = pz;

			minSectionX = (px - radius) >> 4;
			minSectionZ = (pz - radius) >> 4;
			minSectionY = Math.max(level.getMinSectionY(), (py - radius) >> 4);
			int maxSectionX = (px + radius) >> 4;
			int maxSectionZ = (pz + radius) >> 4;
			int maxSectionY = Math.min(level.getMaxSectionY(), (py + radius) >> 4);

			countX = maxSectionX - minSectionX + 1;
			countZ = maxSectionZ - minSectionZ + 1;
			countY = maxSectionY - minSectionY + 1;
			totalSections = Math.max(0, countX * countZ * countY);
			cursor = 0;
		}

		void tick(ServerPlayer player, LtblConfig config, ParticleOptions defaultParticle) {
			ParticleOptions particle = style != null ? style.particle() : defaultParticle;
			ServerLevel level = player.level();
			int dx = player.getBlockX() - anchorX;
			int dz = player.getBlockZ() - anchorZ;
			int dy = player.getBlockY() - anchorY;

			if (cursor >= totalSections
					|| level.dimension() != dimension
					|| dx * dx + dz * dz + dy * dy > REBUILD_DISTANCE * REBUILD_DISTANCE) {
				rebuild(player);
			}

			int lightLimit = this.lightLimit >= 0 ? this.lightLimit : SpawnRules.defaultLightLimit(level);
			int budget = config.maxParticlesPerTick;

			for (int i = 0; i < config.sectionsPerTick && cursor < totalSections; i++, cursor++) {
				int index = cursor;
				int sectionX = minSectionX + index % countX;
				int rest = index / countX;
				int sectionZ = minSectionZ + rest % countZ;
				int sectionY = minSectionY + rest / countZ;

				budget = scanSection(player, level, sectionX, sectionY, sectionZ, lightLimit, budget, config, particle);
			}
		}

		private int scanSection(ServerPlayer player, ServerLevel level, int sectionX, int sectionY, int sectionZ,
				int lightLimit, int budget, LtblConfig config, ParticleOptions particle) {
			hits.clear();
			collectSection(level, sectionX, sectionY, sectionZ, lightLimit, hits);
			return emit(player, level, hits, budget, config, particle);
		}

		private int emit(ServerPlayer player, ServerLevel level, List<BlockPos> positions, int budget,
				LtblConfig config, ParticleOptions particle) {
			int size = positions.size();
			if (size == 0 || budget <= 0) {
				return budget;
			}

			// Rotate the starting point so a tight particle budget still covers the whole area over time.
			int start = Math.floorMod(rotation++ * 17, size);
			double viewDistanceSq = config.particleViewDistance * config.particleViewDistance;
			double px = player.getX();
			double py = player.getEyeY();
			double pz = player.getZ();
			int sent = 0;

			for (int i = 0; i < size && sent < budget; i++) {
				BlockPos position = positions.get((start + i) % size);
				double x = position.getX() + 0.5;
				double y = position.getY() + 0.15;
				double z = position.getZ() + 0.5;
				double ddx = x - px;
				double ddy = y - py;
				double ddz = z - pz;

				if (ddx * ddx + ddy * ddy + ddz * ddz > viewDistanceSq) {
					continue;
				}

				level.sendParticles(player, particle, false, false, x, y, z, 1, 0.0, 0.0, 0.0, 0.0);
				sent++;
			}

			return budget - sent;
		}
	}

	/** Collects every spawnable block of one chunk section. Runs on the server thread only. */
	static void collectSection(ServerLevel level, int sectionX, int sectionY, int sectionZ, int lightLimit,
			List<BlockPos> out) {
		int baseX = sectionX << 4;
		int baseY = sectionY << 4;
		int baseZ = sectionZ << 4;

		if (baseY > level.getMaxY() || baseY + 15 < level.getMinY()) {
			return;
		}

		LevelChunk chunk = level.getChunkSource().getChunkNow(sectionX, sectionZ);
		if (chunk == null) {
			return;
		}

		LevelChunkSection section = chunk.getSection(level.getSectionIndexFromSectionY(sectionY));
		if (section == null || section.hasOnlyAir()) {
			return;
		}

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		int minY = level.getMinY();
		int maxY = level.getMaxY();

		for (int y = 0; y < 16; y++) {
			int worldY = baseY + y;
			if (worldY <= minY || worldY >= maxY) {
				continue;
			}
			for (int z = 0; z < 16; z++) {
				for (int x = 0; x < 16; x++) {
					pos.set(baseX + x, worldY, baseZ + z);
					BlockState state = section.getBlockState(x, y, z);
					if (!SpawnRules.isPassable(level, state, pos)) {
						continue;
					}
					// Reuse the section palette for the floor when it is not in the row below the section.
					BlockState belowState = y > 0 ? section.getBlockState(x, y - 1, z) : level.getBlockState(pos.below());
					if (SpawnRules.isSpawnable(level, pos, belowState, lightLimit)) {
						out.add(pos.immutable());
					}
				}
			}
		}
	}
}
