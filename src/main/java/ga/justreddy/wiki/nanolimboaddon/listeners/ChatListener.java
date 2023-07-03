package ga.justreddy.wiki.nanolimboaddon.listeners;

import ga.justreddy.wiki.nanolimboaddon.NanoLimboAddon;
import ga.justreddy.wiki.nanolimboaddon.enums.LimboType;
import ga.justreddy.wiki.nanolimboaddon.manager.LimboManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

/**
 * @author JustReddy
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatListener implements Listener {

    List<String> allowed_commands = NanoLimboAddon
            .getInstance()
            .getConfig()
            .getConfig()
            .getStringList("command-whitelist");

    LimboManager manager = NanoLimboAddon.getInstance().getManager();

    @EventHandler
    public void onChat(ChatEvent event) {
        if (!(event.getSender() instanceof ProxiedPlayer)) return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        Server server = player.getServer();
        if (server == null) return;
        ServerInfo serverInfo = server.getInfo();
        if (serverInfo == null) return;
        String name = serverInfo.getName();
        if (name == null) return;
        if (manager.isServer(name, LimboType.AFK)) {
            String message = event.getMessage();
            if (!message.startsWith("/")) return;
            message = message.replace("/", "");
            String finalMessage = message; // Lambda's are stupid
            boolean isCommand = allowed_commands.stream()
                            .anyMatch(cmd -> cmd.equalsIgnoreCase(finalMessage));
            if (isCommand) return;
            event.setCancelled(true);
            return;
        }

        if (manager.isServer(name, LimboType.QUEUE)) {
            event.setCancelled(true);
        }

    }

}
