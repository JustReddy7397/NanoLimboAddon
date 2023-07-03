package ga.justreddy.wiki.nanolimboaddon.manager;

import ga.justreddy.wiki.nanolimboaddon.config.Config;
import ga.justreddy.wiki.nanolimboaddon.enums.LimboType;
import ga.justreddy.wiki.nanolimboaddon.util.ChatUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JustReddy
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class LimboManager {

    Map<String, LimboType> servers;
    Map<ProxiedPlayer, Long> queue;
    Map<ProxiedPlayer, Title> titles;
    int maxPlayers;

    public LimboManager(Config config) {
        servers = new HashMap<>();
        queue = new HashMap<>();
        titles = new HashMap<>();
        Configuration limboSection = config.getConfig().getSection("limbos");
        maxPlayers = limboSection.getInt("max-players");
        for (String server : limboSection.getKeys()) {
            LimboType type = LimboType.getType(limboSection.getString(server + ".type"));
            if (type == null) {
                ChatUtil.sendConsole("&7[&dNanoLimboAddon&7] &cInvalid type for server: " + server);
                ChatUtil.sendConsole("&7[&dNanoLimboAddon&7] &cExpected AFK or QUEUE but got: " +
                        limboSection.getString(server + ".type")
                );
                continue;
            }
            servers.put(server, type);
            ChatUtil.sendConsole("&7[&dNanoLimboAddon&7] &aLoaded server:" + server
                    + " with type: " + type);
        }
    }

    public boolean isFull() {
        return getOnline() >= maxPlayers;
    }

    public int getOnline() {
        int count = 0;
        for (ServerInfo serverInfo : ProxyServer.getInstance().getServersCopy().values()) {
            String name = serverInfo.getName();
            LimboType type = servers.getOrDefault(name, null);
            if (servers.containsKey(name) && type == LimboType.QUEUE) continue;
            count += serverInfo.getPlayers().size();
        }
        return count;
    }

    public void addToQueue(ProxiedPlayer player) {
        queue.putIfAbsent(player, System.currentTimeMillis());
    }

    public void removeFromQueue(ProxiedPlayer player) {
        queue.remove(player);
        titles.remove(player);
    }

    public Title getTitle(ProxiedPlayer player) {
        return titles.getOrDefault(player, ProxyServer.getInstance().createTitle());
    }

    public boolean isServer(String name, LimboType type) {
        return servers.containsKey(name) && servers.getOrDefault(name, null) == type;
    }

    public void sendToRandomQueueServer(ProxiedPlayer player) {
        Map<String, LimboType> queueServers = new HashMap<>();
        for (Map.Entry<String, LimboType> entry : servers.entrySet()) {
            if (entry.getValue() != LimboType.QUEUE) continue;
            queueServers.put(entry.getKey(), entry.getValue());
        }
        List<String> servers = new ArrayList<>(queueServers.keySet());
        servers = servers.stream().filter(server ->
                        ProxyServer.getInstance().getServerInfo(server) != null
                ).sorted(Comparator.comparing(s -> ProxyServer.getInstance().getServerInfo(s)
                        .getPlayers().size()))
                .collect(Collectors.toList());
        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(servers.get(0));
        if (serverInfo == null) {
            throw new IllegalStateException("Can't find a QUEUE server to send " + player.getName() + " to!");
        }
        player.connect(serverInfo);
        addToQueue(player);
    }

    public void sendToRandomAFKServer(ProxiedPlayer player) {
        Map<String, LimboType> queueServers = new HashMap<>();
        for (Map.Entry<String, LimboType> entry : servers.entrySet()) {
            if (entry.getValue() != LimboType.AFK) continue;
            queueServers.put(entry.getKey(), entry.getValue());
        }
        List<String> servers = new ArrayList<>(queueServers.keySet());
        servers = servers.stream().filter(server ->
                        ProxyServer.getInstance().getServerInfo(server) != null
                ).sorted(Comparator.comparing(s -> ProxyServer.getInstance().getServerInfo(s)
                        .getPlayers().size()))
                .collect(Collectors.toList());
        ServerInfo serverInfo = ProxyServer.getInstance().getServerInfo(servers.get(0));
        if (serverInfo == null) {
            throw new IllegalStateException("Can't find a AFK server to send " + player.getName() + " to!");
        }
        if (player.getServer() == null) return;
        if (player.getServer().getInfo() == null) return;
        if (player.getServer().getInfo().getName().equalsIgnoreCase(serverInfo.getName())) return;
        player.connect(serverInfo);
    }

    public ProxiedPlayer getFirstPlayerInQueue() {
        List<ProxiedPlayer> result = queue.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return result.get(0);
    }

    public int getCurrentPosition(ProxiedPlayer player ) {
        List<ProxiedPlayer> result = queue.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        int pos = 0;
        for (int i = 0; i < result.size(); i++) {
            ProxiedPlayer prox = result.get(i);
            if (prox.getUniqueId().equals(player.getUniqueId())) {
                pos = i + 1;
                break;
            }
        }
        return pos;
    }

}
