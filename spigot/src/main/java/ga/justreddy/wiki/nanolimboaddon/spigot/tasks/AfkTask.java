package ga.justreddy.wiki.nanolimboaddon.spigot.tasks;

import ga.justreddy.wiki.nanolimboaddon.spigot.NanoLimboAddon;
import ga.justreddy.wiki.nanolimboaddon.spigot.manager.LimboManager;
import ga.justreddy.wiki.nanolimboaddon.spigot.util.ChatUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author JustReddy
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AfkTask implements Runnable {

    LimboManager manager;

    @Override
    public void run() {
        for (UUID uuid : manager.getAfk().keySet()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) return;
            int remainingTime = manager.getRemainingAfkTime(player);
            if (remainingTime == 1L) return;
            if (remainingTime == 10) {
                player.sendMessage(
                        ChatUtil.format(
                                NanoLimboAddon.getInstance().getConfig().getString("afk-message")
                        )
                );
            }
            if (remainingTime == manager.getAfkTime() / 1000) {
                manager.sendToAfkServer(player);
            }
        }
    }
}
