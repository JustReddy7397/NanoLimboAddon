package ga.justreddy.wiki.nanolimboaddon.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * @author JustReddy
 */
public class ChatUtil {

    public static BaseComponent format(String message) {
        return new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void sendConsole(String message) {
        ProxyServer.getInstance().getConsole()
                .sendMessage(format(message));
    }

}
