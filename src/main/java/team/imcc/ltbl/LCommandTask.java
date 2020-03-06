package team.imcc.ltbl;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LCommandTask extends BukkitRunnable {

    public static final int DETECTION_RADIUS = 32;

    private Player player;

    public LCommandTask(Player player) {
        this.player = player;
    }

    public static boolean isTheWorld(Player player) {
        // ONLY on NORMAL world
        return player.getWorld().getEnvironment().equals(World.Environment.NORMAL);
    }

    public static boolean isTheBiome(Block block) {
        Biome biome = block.getBiome();
        switch (biome) {
            case THE_VOID:
            case MUSHROOM_FIELDS:
            case MUSHROOM_FIELD_SHORE:
                return false;
        }
        return true;
    }

    public static boolean isOnSpawnBlock(Block block) {
        switch (block.getType()) {
            // air
            case AIR:
            case CAVE_AIR:
            case VOID_AIR:

            // plant
            case GRASS:
            case TALL_GRASS:
            case DANDELION:
            case CHORUS_PLANT:
            case CHORUS_FLOWER:
            case KELP_PLANT:
            case ROSE_BUSH:
            case WITHER_ROSE:
            case SWEET_BERRY_BUSH:
            case BROWN_MUSHROOM:
            case RED_MUSHROOM:
            case ACACIA_SAPLING:
            case BAMBOO_SAPLING:
            case SPRUCE_SAPLING:
            case BIRCH_SAPLING:
            case DARK_OAK_SAPLING:
            case JUNGLE_SAPLING:
            case OAK_SAPLING:
            case DEAD_BUSH:
            case CORNFLOWER:
            case SUNFLOWER:

            // sign
            case SPRUCE_SIGN:
            case ACACIA_SIGN:
            case BIRCH_SIGN:
            case DARK_OAK_SIGN:
            case JUNGLE_SIGN:
            case OAK_SIGN:

            // banner
            case BLACK_BANNER:
            case BLUE_BANNER:
            case BROWN_BANNER:
            case CYAN_BANNER:
            case GRAY_BANNER:
            case GREEN_BANNER:
            case LIGHT_BLUE_BANNER:
            case LIME_BANNER:
            case LIGHT_GRAY_BANNER:
            case MAGENTA_BANNER:
            case ORANGE_BANNER:
            case PINK_BANNER:
            case PURPLE_BANNER:
            case RED_BANNER:
            case WHITE_BANNER:
            case YELLOW_BANNER:
                return true;
        }
        return false;
    }

    public static boolean isSolidBlockBelow(Block block) {
        Block footer = block.getRelative(BlockFace.DOWN);
        if (footer.isLiquid()) {
            return false;
        }
        if (isOnSpawnBlock(footer)) {
            return false;
        }
        if (!footer.getType().isSolid()) {
            return false;
        }
        switch (footer.getType()) {
            case CHEST:
            case CHEST_MINECART:
            case ACACIA_FENCE:
            case BIRCH_FENCE:
            case DARK_OAK_FENCE:
            case JUNGLE_FENCE:
            case NETHER_BRICK_FENCE:
            case OAK_FENCE:
            case SPRUCE_FENCE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            case OAK_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
                return false;
        }
        switch (footer.getType()) {
            case SANDSTONE_SLAB:
            case SMOOTH_QUARTZ_SLAB:
            case SMOOTH_RED_SANDSTONE_SLAB:
            case SMOOTH_SANDSTONE_SLAB:
            case ACACIA_SLAB:
            case ANDESITE_SLAB:
            case SMOOTH_STONE_SLAB:
            case SPRUCE_SLAB:
            case BIRCH_SLAB:
            case STONE_BRICK_SLAB:
            case STONE_SLAB:
            case BRICK_SLAB:
            case COBBLESTONE_SLAB:
            case CUT_RED_SANDSTONE_SLAB:
            case CUT_SANDSTONE_SLAB:
            case DARK_OAK_SLAB:
            case DARK_PRISMARINE_SLAB:
            case DIORITE_SLAB:
            case END_STONE_BRICK_SLAB:
            case GRANITE_SLAB:
            case JUNGLE_SLAB:
            case MOSSY_COBBLESTONE_SLAB:
            case MOSSY_STONE_BRICK_SLAB:
            case NETHER_BRICK_SLAB:
            case OAK_SLAB:
            case PETRIFIED_OAK_SLAB:
            case POLISHED_ANDESITE_SLAB:
            case POLISHED_DIORITE_SLAB:
            case POLISHED_GRANITE_SLAB:
            case PRISMARINE_BRICK_SLAB:
            case PRISMARINE_SLAB:
            case PURPUR_SLAB:
            case QUARTZ_SLAB:
            case RED_NETHER_BRICK_SLAB:
            case RED_SANDSTONE_SLAB:
                if (footer.getBlockData().getAsString().contains("type=bottom")) {
                    return false;
                }
        }
        switch (footer.getType()) {
            case SANDSTONE_STAIRS:
            case ACACIA_STAIRS:
            case SMOOTH_QUARTZ_STAIRS:
            case SMOOTH_RED_SANDSTONE_STAIRS:
            case SMOOTH_SANDSTONE_STAIRS:
            case SPRUCE_STAIRS:
            case STONE_BRICK_STAIRS:
            case STONE_STAIRS:
            case ANDESITE_STAIRS:
            case BIRCH_STAIRS:
            case BRICK_STAIRS:
            case COBBLESTONE_STAIRS:
            case DARK_OAK_STAIRS:
            case DARK_PRISMARINE_STAIRS:
            case DIORITE_STAIRS:
            case END_STONE_BRICK_STAIRS:
            case GRANITE_STAIRS:
            case JUNGLE_STAIRS:
            case MOSSY_COBBLESTONE_STAIRS:
            case MOSSY_STONE_BRICK_STAIRS:
            case NETHER_BRICK_STAIRS:
            case OAK_STAIRS:
            case POLISHED_ANDESITE_STAIRS:
            case POLISHED_DIORITE_STAIRS:
            case POLISHED_GRANITE_STAIRS:
            case PRISMARINE_BRICK_STAIRS:
            case PRISMARINE_STAIRS:
            case PURPUR_STAIRS:
            case QUARTZ_STAIRS:
            case RED_NETHER_BRICK_STAIRS:
            case RED_SANDSTONE_STAIRS:
                if (footer.getBlockData().getAsString().contains("half=bottom")) {
                    return false;
                }
        }
        return true;
    }

    public static boolean isTheBlock(Block block) {
        // check the block under
        if (isOnSpawnBlock(block) && isSolidBlockBelow(block)) {
           return true;
        }
        return false;
    }

    public static boolean isTheLight(Block block) {
        return block.getLightFromBlocks() <= 7;
    }

    protected boolean chainChecker(Block block) {
        if (!isTheBiome(block)) return false;
        if (!isTheLight(block)) return false;
        if (!isTheBlock(block)) return false;
        return true;
    }

    protected void doLTBL(Block block) {
        Location displayLocation = block.getLocation();
        displayLocation.setX(displayLocation.getX() + 0.5);
        displayLocation.setY(displayLocation.getY() + 0.5);
        displayLocation.setZ(displayLocation.getZ() + 0.5);
        ParticleUtil.sendParticle(player, displayLocation);
    }

    @Override
    public void run() {
        if (!player.isOnline()) {
            // stop self for play is offline
            this.cancel();
            return;
        }

        if (!isTheWorld(player)) {
            return;
        }

        for (int i = -DETECTION_RADIUS; i <= DETECTION_RADIUS; i++) {
            for (int j = -DETECTION_RADIUS; j <= DETECTION_RADIUS; j++) {
                for (int k = -DETECTION_RADIUS; k <= DETECTION_RADIUS; k++) {
                    Location location = player.getLocation();
                    location.setX(location.getX() + i);
                    location.setY(location.getY() + j);
                    location.setZ(location.getZ() + k);
                    Block block = player.getWorld().getBlockAt(location);
                    if (chainChecker(block)) {
                        doLTBL(block);
                    }
                }
            }
        }
    }
}
