package team.imcc.ltbl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Plain JSON config in {@code config/ltbl.json}. Loaded once at startup; a missing or broken file
 * is replaced by the defaults so the server never fails to boot because of this mod.
 */
public final class LtblConfig {
	public static final int MIN_RADIUS = 8;
	public static final int MAX_RADIUS = 64;

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private static LtblConfig instance;

	/** Horizontal/vertical scan radius in blocks. */
	public int radius = 32;
	/** Chunk sections (16x16x16) scanned per player per server tick; higher = faster refresh, more load. */
	public int sectionsPerTick = 2;
	/** Hard cap on particle packets sent for one player in one tick. */
	public int maxParticlesPerTick = 400;
	/** Only mark blocks within this distance from the player's eyes. */
	public double particleViewDistance = 24.0;
	/** barrier, end_rod, glow or flame. */
	public String particle = "barrier";

	public static LtblConfig get() {
		return instance == null ? (instance = new LtblConfig()) : instance;
	}

	public static void load(Logger logger) {
		Path path = FabricLoader.getInstance().getConfigDir().resolve("ltbl.json");
		LtblConfig config = new LtblConfig();

		if (Files.exists(path)) {
			try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
				LtblConfig parsed = GSON.fromJson(reader, LtblConfig.class);
				if (parsed != null) {
					config = parsed;
				}
			} catch (IOException | JsonSyntaxException e) {
				logger.warn("Could not read {} ({}), using defaults", path, e.getMessage());
			}
		}

		config.sanitize();
		instance = config;
		config.save(logger);
	}

	public void sanitize() {
		radius = clamp(radius, MIN_RADIUS, MAX_RADIUS);
		sectionsPerTick = clamp(sectionsPerTick, 1, 16);
		maxParticlesPerTick = clamp(maxParticlesPerTick, 0, 4000);
		particleViewDistance = Math.max(2.0, Math.min(particleViewDistance, 64.0));
		particle = ParticleStyle.parse(particle).id();
	}

	private static int clamp(int value, int min, int max) {
		return Math.max(min, Math.min(max, value));
	}

	public void save(Logger logger) {
		Path path = FabricLoader.getInstance().getConfigDir().resolve("ltbl.json");
		try {
			Files.createDirectories(path.getParent());
			try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
				GSON.toJson(this, writer);
			}
		} catch (IOException e) {
			logger.warn("Could not write {}", path, e);
		}
	}
}
