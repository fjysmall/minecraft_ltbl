package team.imcc.ltbl;

import org.bukkit.Bukkit;
import org.bukkit.Location;
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
                }
                else {

                    this.player2Task.put(
                        player.getName(),
                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                Location loc = player.getLocation();
                                loc.setY(loc.getY() + 2);

                                // default FLAME
                                ParticleUtil.sendParticle(player, loc, 26, 0, 0);
                            }
                        }
                    );

                    this.player2Task.get(player.getName()).runTaskTimerAsynchronously(this.plugin, 0, 10);
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
