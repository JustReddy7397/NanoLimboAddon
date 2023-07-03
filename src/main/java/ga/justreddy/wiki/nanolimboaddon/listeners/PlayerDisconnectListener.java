package ga.justreddy.wiki.nanolimboaddon.listeners;

import ga.justreddy.wiki.nanolimboaddon.NanoLimboAddon;
import ga.justreddy.wiki.nanolimboaddon.enums.LimboType;
import ga.justreddy.wiki.nanolimboaddon.manager.LimboManager;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * @author JustReddy
 */
public class PlayerDisconnectListener implements Listener {

    private final LimboManager manager = NanoLimboAddon.getInstance().getManager();

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        if (player == null) return;
        Server server = player.getServer();
        if (server == null) return;
        ServerInfo serverInfo = server.getInfo();
        if (serverInfo == null) return;
        String name = serverInfo.getName();
        if (name == null) return;
        if (!manager.isServer(name, LimboType.QUEUE)) return;
        manager.removeFromQueue(player);
    }

}
