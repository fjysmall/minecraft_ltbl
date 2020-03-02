package team.imcc.ltbl;

import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;


public class LCommandExecutor implements org.bukkit.command.CommandExecutor {

    private Plugin plugin;
    Map<Player, BukkitRunnable> player2Task;

    public LCommandExecutor(Plugin plugin) {
        this.plugin = plugin;
        this.player2Task = new ConcurrentHashMap<>();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!label.equalsIgnoreCase("ltbl")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            // not sent by player, ignore
            return false;
        }

        Player player = (Player) sender;

        if (this.player2Task.containsKey(player)) {
            this.player2Task.get(player).cancel();
            this.player2Task.remove(player);
            sender.sendMessage("LTBL switch off");
        }
        else {
            this.player2Task.put(player, new LCommandTask(player));
            this.player2Task.get(player)
                .runTaskTimerAsynchronously(this.plugin, 0, 64);
            sender.sendMessage("LTBL switch on");
        }

        return true;
    }
}
