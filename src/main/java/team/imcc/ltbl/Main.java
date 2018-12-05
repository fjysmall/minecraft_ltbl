package team.imcc.ltbl;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onLoad() {
        getLogger().info("onLoad has been invoked!");
    }

    @Override
    public void onEnable() {
        getLogger().info("onEnable has been invoked!");

        // TODO (jyfeng)
        getCommand("ltbl").setExecutor(new LtblCommandExecutor(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable has been invoked!");
    }
}
