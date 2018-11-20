package team.imcc.ltbl;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.material.Stairs;
import org.bukkit.material.Step;
import org.bukkit.material.WoodenStep;

public class BlockUtil {

    public static boolean isSpawnableBlock(Block block) {

        switch (block.getType()) {
            case AIR:
            case SIGN_POST:
            case DOUBLE_PLANT:
            case RED_ROSE:
            case YELLOW_FLOWER:
            case LONG_GRASS:
            case BROWN_MUSHROOM:
            case RED_MUSHROOM:
            case STANDING_BANNER:
            case SAPLING:
            case DEAD_BUSH:
                break;
            default:
                return false;
        }

        if (block.getRelative(BlockFace.DOWN).isLiquid()) {
            return false;
        }

        if (block.getRelative(BlockFace.DOWN).getType().isTransparent()) {
            return false;
        }

        Material material = block.getRelative(BlockFace.DOWN).getType();

        switch (material) {
            case AIR:
            case GLASS:
            case STAINED_GLASS:
            case STAINED_GLASS_PANE:
            case THIN_GLASS:
            case LEAVES:
            case LEAVES_2:
            case CHORUS_FLOWER:
            case CHORUS_PLANT:
                return false;
        }

        if (block.getRelative(BlockFace.DOWN).getType() == Material.STEP) {
            Step step = (Step) block.getRelative(BlockFace.DOWN).getState().getData();
            if (!step.isInverted()) {
                return false;
            }
        }

        if (block.getRelative(BlockFace.DOWN).getType() == Material.WOOD_STEP) {
            WoodenStep woodStep = (WoodenStep) block.getRelative(BlockFace.DOWN).getState().getData();
            if (!woodStep.isInverted()) {
                return false;
            }
        }

        switch (material) {
            case SANDSTONE_STAIRS:
            case SMOOTH_STAIRS:
            case ACACIA_STAIRS:
            case SPRUCE_WOOD_STAIRS:
            case BIRCH_WOOD_STAIRS:
            case BRICK_STAIRS:
            case COBBLESTONE_STAIRS:
            case DARK_OAK_STAIRS:
            case JUNGLE_WOOD_STAIRS:
            case NETHER_BRICK_STAIRS:
            case PURPUR_STAIRS:
            case QUARTZ_STAIRS:
            case RED_SANDSTONE_STAIRS:
            case WOOD_STAIRS: {
                Stairs stairs = (Stairs) block.getRelative(BlockFace.DOWN).getState().getData();
                if (!stairs.isInverted()) {
                    return false;
                }
            }
        }

        switch (material) {
            case FENCE:
            case ACACIA_FENCE:
            case BIRCH_FENCE:
            case DARK_OAK_FENCE:
            case IRON_FENCE:
            case JUNGLE_FENCE:
            case NETHER_FENCE:
            case SPRUCE_FENCE:
            case FENCE_GATE:
            case ACACIA_FENCE_GATE:
            case BIRCH_FENCE_GATE:
            case DARK_OAK_FENCE_GATE:
            // case IRON_FENCE_GATE:
            case JUNGLE_FENCE_GATE:
            // case NETHER_FENCE_GATE:
            case SPRUCE_FENCE_GATE:
            case ACACIA_DOOR:
            case BIRCH_DOOR:
            case DARK_OAK_DOOR:
            case IRON_DOOR:
            case JUNGLE_DOOR:
            case WOOD_DOOR:
            case WOODEN_DOOR:
            case SPRUCE_DOOR:
            case GOLD_PLATE:
            case STONE_PLATE:
            case IRON_PLATE:
            case WOOD_PLATE:
            case COBBLE_WALL:
                return false;
        }

        switch (material) {
            case ICE:
            case PACKED_ICE:
            case MYCEL:
            case GRASS_PATH:
                return false;
        }

        switch (material) {
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
            case SILVER_SHULKER_BOX:
            case WHITE_SHULKER_BOX:
            case YELLOW_SHULKER_BOX:
                return false;
        }

        switch (material) {
            case ANVIL:
            case BED_BLOCK:
            case CHEST:
            case TRAPPED_CHEST:
            case ENDER_CHEST:
            case ENCHANTMENT_TABLE:
            case MAGMA:
            case TNT:
            case CAKE:
            case WATER_LILY:
            case DIODE:
            case TRAP_DOOR:
            case PISTON_EXTENSION:
            case DAYLIGHT_DETECTOR:
            case DAYLIGHT_DETECTOR_INVERTED:
            case CARPET:
            case LADDER:
            case VINE:
            case FLOWER_POT:
            case WEB:
            case BEDROCK:
            case CACTUS:
            case ITEM_FRAME:
            case ENDER_PORTAL_FRAME:
            case WALL_SIGN:
            case WALL_BANNER:
            case SIGN_POST:
            case STANDING_BANNER:
            case CAULDRON:
            case BREWING_STAND:
            case BEACON:
                return false;
        }

        return true;
    }
}
