package ga.justreddy.wiki.nanolimboaddon.listeners;

import ga.justreddy.wiki.nanolimboaddon.NanoLimboAddon;
import ga.justreddy.wiki.nanolimboaddon.manager.LimboManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author JustReddy
 */
public class PlayerJoinListener implements Listener {

    private final LimboManager manager = NanoLimboAddon.getInstance().getManager();

    @EventHandler
    public void onPlayerJoin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        // When someone has this permission they won't get sent to the limbo :)
        if (player.hasPermission("limbo.bypass")) return;
        ProxyServer.getInstance()
                .getScheduler()
                .schedule(NanoLimboAddon.getInstance(), () -> {
                    if (manager.isFull()) {
                        manager.sendToRandomQueueServer(player);
                    } else {
                        manager.sendToLobby(player);
                    }
                }, 100, TimeUnit.MILLISECONDS);
    }

}
