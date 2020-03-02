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
        if (footer.getType().isSolid()) {
            return true;
        }
        return false;
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
