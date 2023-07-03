package ga.justreddy.wiki.nanolimboaddon;

import ga.justreddy.wiki.nanolimboaddon.config.Config;
import ga.justreddy.wiki.nanolimboaddon.listeners.ChatListener;
import ga.justreddy.wiki.nanolimboaddon.listeners.PlayerDisconnectListener;
import ga.justreddy.wiki.nanolimboaddon.listeners.PlayerJoinListener;
import ga.justreddy.wiki.nanolimboaddon.listeners.ServerSwitchListener;
import ga.justreddy.wiki.nanolimboaddon.manager.LimboManager;
import ga.justreddy.wiki.nanolimboaddon.timers.QueueTimer;
import ga.justreddy.wiki.nanolimboaddon.timers.TitleTimer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.Timer;
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

    Timer queueTimer;
    Timer titleTimer;

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

        queueTimer = new Timer();
        queueTimer.schedule(new QueueTimer(manager), 0, 1000L);
        titleTimer = new Timer();
        titleTimer.schedule(new TitleTimer(manager, config.getConfig()), 0, 5 * 1000L);

    }

    @Override
    public void onDisable() {
        queueTimer.cancel();
        titleTimer.cancel();
    }
}
