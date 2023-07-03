package ga.justreddy.wiki.nanolimboaddon;

import ga.justreddy.wiki.nanolimboaddon.config.Config;
import ga.justreddy.wiki.nanolimboaddon.listeners.ChatListener;
import ga.justreddy.wiki.nanolimboaddon.listeners.PlayerDisconnectListener;
import ga.justreddy.wiki.nanolimboaddon.listeners.PlayerJoinListener;
import ga.justreddy.wiki.nanolimboaddon.listeners.ServerSwitchListener;
import ga.justreddy.wiki.nanolimboaddon.manager.LimboManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.logging.Logger;

/**
 * @author JustReddy
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class NanoLimboAddon extends Plugin {


    @Getter static NanoLimboAddon instance;

    LimboManager manager;
    Config config;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        try {
            config = new Config("config.yml");
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        manager = new LimboManager(config);
        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerListener(this, new ChatListener());
        pluginManager.registerListener(this, new PlayerDisconnectListener());
        pluginManager.registerListener(this, new PlayerJoinListener());
        pluginManager.registerListener(this, new ServerSwitchListener());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
