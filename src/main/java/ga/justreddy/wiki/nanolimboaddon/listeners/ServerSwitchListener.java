package ga.justreddy.wiki.nanolimboaddon.listeners;

import ga.justreddy.wiki.nanolimboaddon.NanoLimboAddon;
import ga.justreddy.wiki.nanolimboaddon.enums.LimboType;
import ga.justreddy.wiki.nanolimboaddon.manager.LimboManager;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

/**
 * @author JustReddy
 */
public class ServerSwitchListener implements Listener {

    LimboManager manager = NanoLimboAddon.getInstance().getManager();

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Server toServer = player.getServer();
        if (toServer == null) return;
        ServerInfo to = toServer.getInfo();
        if (to == null) return;
        ServerInfo from = event.getFrom();
        if (from == null) return;
        String toName = to.getName();
        if (toName == null) return;
        String fromName = from.getName();
        if (fromName == null) return;
        if (manager.isServer(fromName, LimboType.QUEUE)
                && manager.isServer(toName, LimboType.AFK)) return;
        if (manager.isServer(toName, LimboType.AFK)
                && manager.isServer(fromName, LimboType.QUEUE)) return;

        if (manager.isServer(fromName, LimboType.AFK)
                && manager.isServer(toName, LimboType.AFK)) return;

        if (manager.isServer(fromName, LimboType.QUEUE)
                && manager.isServer(toName, LimboType.QUEUE)) return;

        if (manager.isServer(toName, LimboType.AFK)) {
            manager.sendToRandomAFKServer(player);
            return;
        }

        if (manager.isServer(toName, LimboType.QUEUE)) {
            manager.addToQueue(player);
            return;
        }

        if (manager.isServer(fromName, LimboType.QUEUE)) {
            manager.removeFromQueue(player);
            return;
        }

        if (manager.isServer(fromName, LimboType.AFK)) {
            manager.removeFromAfk(player);
            return;
        }

    }

}
