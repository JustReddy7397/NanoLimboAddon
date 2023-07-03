package ga.justreddy.wiki.nanolimboaddon.spigot.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @author JustReddy
 */
public class ChatUtil {

    public static String format(String message){
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(format(message));
    }

}
