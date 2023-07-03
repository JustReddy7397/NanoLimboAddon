package ga.justreddy.wiki.nanolimboaddon.timers;

import ga.justreddy.wiki.nanolimboaddon.enums.LimboType;
import ga.justreddy.wiki.nanolimboaddon.manager.LimboManager;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

import java.util.TimerTask;

/**
 * @author JustReddy
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class QueueTimer extends TimerTask {

    LimboManager manager;

    @Override
    public void run() {
        if (!manager.isFull()) {
            ProxiedPlayer player = manager.getFirstPlayerInQueue();
            if (player == null) return;
            Server currentServer = player.getServer();
            if (currentServer == null) return;
            ServerInfo currentInfo = currentServer.getInfo();
            if (!manager.isServer(currentInfo.getName(), LimboType.QUEUE)) return;
            ServerInfo server = ProxyServer.getInstance().getServerInfo(manager.getLobbyServer());
            if (server == null) {
                throw new IllegalStateException("Can't find a lobby server to send " + player.getName() + " to!");
            }
            player.connect(server);
            manager.removeFromQueue(player);
        }
    }
}
