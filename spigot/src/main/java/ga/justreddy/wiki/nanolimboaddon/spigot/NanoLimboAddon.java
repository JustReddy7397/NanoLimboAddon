package ga.justreddy.wiki.nanolimboaddon.spigot;

import ga.justreddy.wiki.nanolimboaddon.spigot.listener.AFKListener;
import ga.justreddy.wiki.nanolimboaddon.spigot.manager.BungeeManager;
import ga.justreddy.wiki.nanolimboaddon.spigot.manager.LimboManager;
import ga.justreddy.wiki.nanolimboaddon.spigot.util.ChatUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class NanoLimboAddon extends JavaPlugin {

    @Getter static NanoLimboAddon instance;

    BungeeManager bungeeManager;
    LimboManager limboManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        saveDefaultConfig();
        if (getConfig().getStringList("limbos").isEmpty()) {
            ChatUtil.sendConsole("&7[&dNanoLimboAddon&7]&c No AFK limbo servers set in config.yml");
            ChatUtil.sendConsole("&7[&dNanoLimboAddon&7]&c Disabling plugin...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        bungeeManager = BungeeManager.of(this);
        limboManager = new LimboManager(getConfig().getStringList("limbos"), getConfig().getInt("afk-time"));
        getServer().getPluginManager().registerEvents(new AFKListener(limboManager), this);



    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
