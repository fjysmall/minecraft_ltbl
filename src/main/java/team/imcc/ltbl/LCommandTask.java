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

        // special objects
        switch (footer.getType()) {
            case ANVIL:
            case CHIPPED_ANVIL:
            case DAMAGED_ANVIL:
            case ARMOR_STAND:
            case BEDROCK:
            case CHEST:
            case CHEST_MINECART:
            case TRAPPED_CHEST:
            case ENDER_CHEST:
            case WRITABLE_BOOK:
            case MAGMA_BLOCK:
            case MAGMA_CREAM:
            case MAGMA_CUBE_SPAWN_EGG:
            case TNT:
            case CAKE:
            case COBWEB:
            case CACTUS:
            case ITEM_FRAME:
            case LADDER:
            case VINE:
            case FLOWER_POT:
            case CARVED_PUMPKIN:
            case CAULDRON:
            case BREWING_STAND:
            case BEACON:
            case PAINTING:
            case SCAFFOLDING:
                return false;
        }

        // new blocks
        switch (footer.getType()) {
            case COMPOSTER:
            case LECTERN:
            case CARTOGRAPHY_TABLE:
            case CRAFTING_TABLE:
            case ENCHANTING_TABLE:
            case FLETCHING_TABLE:
            case SMITHING_TABLE:
            case LANTERN:
            case CAMPFIRE:
            case BELL:
            case STONECUTTER:
            case GRINDSTONE:
                return false;
        }

        // red stone
        switch (footer.getType()) {
            case PISTON:
            case PISTON_HEAD:
            case MOVING_PISTON:
            case STICKY_PISTON:
            case DAYLIGHT_DETECTOR:
            case RAIL:
            case DETECTOR_RAIL:
            case ACTIVATOR_RAIL:
            case POWERED_RAIL:
            case ACACIA_PRESSURE_PLATE:
            case BIRCH_PRESSURE_PLATE:
            case DARK_OAK_PRESSURE_PLATE:
            case HEAVY_WEIGHTED_PRESSURE_PLATE:
            case JUNGLE_PRESSURE_PLATE:
            case LIGHT_WEIGHTED_PRESSURE_PLATE:
            case OAK_PRESSURE_PLATE:
            case SPRUCE_PRESSURE_PLATE:
            case STONE_PRESSURE_PLATE:
                return false;
        }

        // special road
        switch (footer.getType()) {
            case ICE:
            case BLUE_ICE:
            case FROSTED_ICE:
            case PACKED_ICE:
            case GRASS_PATH:
            case MYCELIUM:
                return false;
        }

        // wall banner
        switch (footer.getType()) {
            case WHITE_WALL_BANNER:
            case BLACK_WALL_BANNER:
            case BLUE_WALL_BANNER:
            case BROWN_WALL_BANNER:
            case CYAN_WALL_BANNER:
            case GRAY_WALL_BANNER:
            case GREEN_WALL_BANNER:
            case LIGHT_BLUE_WALL_BANNER:
            case LIGHT_GRAY_WALL_BANNER:
            case LIME_WALL_BANNER:
            case MAGENTA_WALL_BANNER:
            case ORANGE_WALL_BANNER:
            case PINK_WALL_BANNER:
            case PURPLE_WALL_BANNER:
            case RED_WALL_BANNER:
            case YELLOW_WALL_BANNER:
                return false;
        }

        // plant
        switch (footer.getType()) {
            case LILY_PAD:
            case LILY_OF_THE_VALLEY:
            case POTTED_LILY_OF_THE_VALLEY:
            case POTTED_ACACIA_SAPLING:
            case POTTED_ALLIUM:
            case POTTED_AZURE_BLUET:
            case POTTED_BAMBOO:
            case POTTED_BIRCH_SAPLING:
            case POTTED_BLUE_ORCHID:
            case POTTED_BROWN_MUSHROOM:
            case POTTED_CACTUS:
            case POTTED_CORNFLOWER:
            case POTTED_DANDELION:
            case POTTED_DARK_OAK_SAPLING:
            case POTTED_DEAD_BUSH:
            case POTTED_FERN:
            case POTTED_JUNGLE_SAPLING:
            case POTTED_OAK_SAPLING:
            case POTTED_ORANGE_TULIP:
            case POTTED_OXEYE_DAISY:
            case POTTED_PINK_TULIP:
            case POTTED_POPPY:
            case POTTED_RED_MUSHROOM:
            case POTTED_RED_TULIP:
            case POTTED_SPRUCE_SAPLING:
            case POTTED_WHITE_TULIP:
            case POTTED_WITHER_ROSE:
                return false;
        }

        // ender
        switch (footer.getType()) {
            case END_PORTAL_FRAME:
            case END_GATEWAY:
            case END_CRYSTAL:
            case END_PORTAL:
                return false;
        }

        // carpet
        switch (footer.getType()) {
            case CYAN_CARPET:
            case BLACK_CARPET:
            case BLUE_CARPET:
            case BROWN_CARPET:
            case GRAY_CARPET:
            case GREEN_CARPET:
            case LIGHT_BLUE_CARPET:
            case LIGHT_GRAY_CARPET:
            case LIME_CARPET:
            case MAGENTA_CARPET:
            case ORANGE_CARPET:
            case PINK_CARPET:
            case PURPLE_CARPET:
            case RED_CARPET:
            case WHITE_CARPET:
            case YELLOW_CARPET:
                return false;
        }

        // bed
        switch (footer.getType()) {
            case BLACK_BED:
            case BLUE_BED:
            case BROWN_BED:
            case CYAN_BED:
            case GRAY_BED:
            case GREEN_BED:
            case LIGHT_BLUE_BED:
            case LIGHT_GRAY_BED:
            case LIME_BED:
            case MAGENTA_BED:
            case ORANGE_BED:
            case PINK_BED:
            case PURPLE_BED:
            case RED_BED:
            case WHITE_BED:
            case YELLOW_BED:
                return false;
        }

        // boxes
        switch (footer.getType()) {
            case SHULKER_BOX:
            case BLACK_SHULKER_BOX:
            case BLUE_SHULKER_BOX:
            case BROWN_SHULKER_BOX:
            case CYAN_SHULKER_BOX:
            case GRAY_SHULKER_BOX:
            case GREEN_SHULKER_BOX:
            case LIGHT_BLUE_SHULKER_BOX:
            case LIME_SHULKER_BOX:
            case MAGENTA_SHULKER_BOX:
            case ORANGE_SHULKER_BOX:
            case PINK_SHULKER_BOX:
            case PURPLE_SHULKER_BOX:
            case RED_SHULKER_BOX:
            case WHITE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
            case LIGHT_GRAY_SHULKER_BOX:
                return false;
        }

        // glass
        switch (footer.getType()) {
            case GLASS:
            case GLASS_BOTTLE:
            case GRAY_STAINED_GLASS:
            case BLACK_STAINED_GLASS:
            case GREEN_STAINED_GLASS:
            case BLUE_STAINED_GLASS:
            case BROWN_STAINED_GLASS:
            case CYAN_STAINED_GLASS:
            case LIGHT_BLUE_STAINED_GLASS:
            case LIGHT_GRAY_STAINED_GLASS:
            case LIME_STAINED_GLASS:
            case MAGENTA_STAINED_GLASS:
            case ORANGE_STAINED_GLASS:
            case PINK_STAINED_GLASS:
            case PURPLE_STAINED_GLASS:
            case RED_STAINED_GLASS:
            case WHITE_STAINED_GLASS:
            case YELLOW_STAINED_GLASS:
            case GLASS_PANE:
            case GRAY_STAINED_GLASS_PANE:
            case BLACK_STAINED_GLASS_PANE:
            case GREEN_STAINED_GLASS_PANE:
            case BLUE_STAINED_GLASS_PANE:
            case BROWN_STAINED_GLASS_PANE:
            case CYAN_STAINED_GLASS_PANE:
            case LIGHT_BLUE_STAINED_GLASS_PANE:
            case LIGHT_GRAY_STAINED_GLASS_PANE:
            case LIME_STAINED_GLASS_PANE:
            case MAGENTA_STAINED_GLASS_PANE:
            case ORANGE_STAINED_GLASS_PANE:
            case PINK_STAINED_GLASS_PANE:
            case PURPLE_STAINED_GLASS_PANE:
            case RED_STAINED_GLASS_PANE:
            case WHITE_STAINED_GLASS_PANE:
            case YELLOW_STAINED_GLASS_PANE:
                return false;
        }

        // fence
        switch (footer.getType()) {
            case IRON_BARS:
            case IRON_TRAPDOOR:
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

        // wall
        switch (footer.getType()) {
            case WALL_TORCH:
            case COBBLESTONE_WALL:
            case ANDESITE_WALL:
            case BRICK_WALL:
            case DIORITE_WALL:
            case END_STONE_BRICK_WALL:
            case GRANITE_WALL:
            case MOSSY_COBBLESTONE_WALL:
            case MOSSY_STONE_BRICK_WALL:
            case NETHER_BRICK_WALL:
            case PRISMARINE_WALL:
            case RED_NETHER_BRICK_WALL:
            case RED_SANDSTONE_WALL:
            case SANDSTONE_WALL:
            case STONE_BRICK_WALL:
                return false;
        }

        // door
        switch (footer.getType()) {
            case DARK_OAK_DOOR:
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case IRON_DOOR:
            case JUNGLE_DOOR:
            case OAK_DOOR:
            case SPRUCE_DOOR:
            case ACACIA_TRAPDOOR:
            case BIRCH_TRAPDOOR:
            case DARK_OAK_TRAPDOOR:
            case IRON_TRAPDOOR:
            case JUNGLE_TRAPDOOR:
            case OAK_TRAPDOOR:
            case SPRUCE_TRAPDOOR:
                return false;
        }

        // wall sign
        switch (footer.getType()) {
            case ACACIA_WALL_SIGN:
            case BIRCH_WALL_SIGN:
            case DARK_OAK_WALL_SIGN:
            case JUNGLE_WALL_SIGN:
            case OAK_WALL_SIGN:
            case SPRUCE_WALL_SIGN:
                return false;
        }

        // bottom slab
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

        // bottom stairs
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
