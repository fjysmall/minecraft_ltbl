package team.imcc.ltbl;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.HashMap;

public class LtblCommandExecutor implements CommandExecutor {

    private Plugin plugin;
    Map<String, BukkitRunnable> player2Task;

    public LtblCommandExecutor(Plugin plugin) {
        this.plugin = plugin;
        this.player2Task = new HashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (label.equalsIgnoreCase("ltbl")) {

            if ((sender instanceof Player)) {

                Player player = (Player) sender;

                if (this.player2Task.containsKey(player.getName())) {
                    this.player2Task.get(player.getName()).cancel();
                    this.player2Task.remove(player.getName());
                    sender.sendMessage("LTBL switch off");
                }
                else {
                    this.player2Task.put(
                        player.getName(),
                        new BukkitRunnable() {

                            @Override
                            public void run() {

                                if (!player.isOnline()) {
                                    player2Task.remove(player.getName());
                                    this.cancel();
                                    return;
                                }

                                int radius = 64;
                                for (int i = -radius; i <= radius; i++) {
                                    for (int j = -radius; j <= radius; j++) {
                                        for (int k = -radius; k <= radius; k++) {

                                            Location location = player.getLocation();
                                            location.setX(location.getX() + i);
                                            location.setY(location.getY() + j);
                                            location.setZ(location.getZ() + k);

                                            Block block = player.getWorld().getBlockAt(location);
                                            if (!BlockUtil.isSpawnableBlock(block)) {
                                               continue;
                                            }

                                            if (block.getLightFromBlocks() > 7) {
                                                continue;
                                            }

                                            Location displayLocation = block.getLocation();
                                            displayLocation.setX(displayLocation.getX() + 0.5);
                                            displayLocation.setY(displayLocation.getY() + 0.5);
                                            displayLocation.setZ(displayLocation.getZ() + 0.5);
                                            ParticleUtil.sendParticle(player, displayLocation, 35, 0, 0);
                                        }
                                    }
                                }
                            }
                        }
                    );

                    this.player2Task.get(player.getName()).runTaskTimerAsynchronously(this.plugin, 0, 64);

                    sender.sendMessage("LTBL switch on");
                }

                return true;
            }
            else {
                // not sent by player
                return false;
            }
        }

        return false;
    }
}
