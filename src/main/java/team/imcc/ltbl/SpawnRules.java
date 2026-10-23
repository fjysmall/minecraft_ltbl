package team.imcc.ltbl;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;

/**
 * Vanilla spawn rules, reduced to the two questions a light overlay cares about: is this block
 * empty enough for a mob, and is it dark enough for one.
 */
public final class SpawnRules {
	private SpawnRules() {
	}

	/**
	 * Highest block light level at which a monster can still spawn in this dimension.
	 * Overworld: 0, Nether: 7, End: 0. Derived from the dimension type data pack values, so a
	 * datapack that changes spawn rules changes the overlay too.
	 */
	public static int defaultLightLimit(ServerLevel level) {
		DimensionType type = level.dimensionType();
		int blockLimit = type.monsterSpawnBlockLightLimit();
		int lightTestMax = type.monsterSpawnLightTest().maxInclusive();
		return Math.min(blockLimit, lightTestMax);
	}

	/** Can a mob's body occupy this block: no collision shape and no fluid. */
	public static boolean isPassable(ServerLevel level, BlockState state, BlockPos pos) {
		if (state.isAir()) {
			return true;
		}
		if (state.isSolidRender()) {
			// Full opaque cube: the cheap answer for most of the world.
			return false;
		}
		if (!state.getFluidState().isEmpty()) {
			return false;
		}
		return state.getCollisionShape(level, pos).isEmpty();
	}

	/**
	 * Dark enough, a solid floor and room for a two block tall mob. The caller already knows the
	 * block is passable and passes in the state below it: the cheapest filters run first.
	 */
	public static boolean isSpawnable(ServerLevel level, BlockPos pos, BlockState belowState, int lightLimit) {
		BlockPos below = pos.below();
		if (!belowState.isFaceSturdy(level, below, Direction.UP)) {
			return false;
		}
		if (level.getBrightness(LightLayer.BLOCK, pos) > lightLimit) {
			return false;
		}
		if (level.getBiome(pos).is(Biomes.MUSHROOM_FIELDS)) {
			return false;
		}
		BlockPos above = pos.above();
		return level.getBlockState(above).getCollisionShape(level, above).isEmpty();
	}
}
