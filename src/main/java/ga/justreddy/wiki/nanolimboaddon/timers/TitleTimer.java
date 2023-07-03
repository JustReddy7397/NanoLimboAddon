package ga.justreddy.wiki.nanolimboaddon.timers;

import ga.justreddy.wiki.nanolimboaddon.manager.LimboManager;
import ga.justreddy.wiki.nanolimboaddon.util.ChatUtil;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.TimerTask;

/**
 * @author JustReddy
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class TitleTimer extends TimerTask {

    LimboManager manager;
    Configuration configuration;

    @Override
    public void run() {
        for (ProxiedPlayer player : manager.getQueue().keySet()) {
            Title title = manager.getTitle(player);
            int position = manager.getCurrentPosition(player);
            title.title(ChatUtil.format(configuration.getString("titles.queue.title")
                    .replaceAll("<number>", String.valueOf(position))
            ));
            title.subTitle(ChatUtil.format(configuration.getString("titles.queue.subtitle")
                    .replaceAll("<number>", String.valueOf(position))
            ));
            title.stay(160);
            player.sendTitle(title);
            if (configuration.getString("messages.queue").isEmpty()) return;
            player.sendMessage(ChatUtil.format(
                    configuration.getString("messages.queue")
                            .replaceAll("<number>", String.valueOf(position))
            ));
        }

        for (ProxiedPlayer player : manager.getAfk()) {
            Title title = manager.getTitle(player);
            title.title(ChatUtil.format(configuration.getString("titles.afk.title")));
            title.subTitle(ChatUtil.format(configuration.getString("titles.afk.subtitle")));
            title.stay(160);
            player.sendTitle(title);
            if (configuration.getString("messages.afk").isEmpty()) return;
            player.sendMessage(ChatUtil.format(
                    configuration.getString("messages.afk")
            ));
        }


    }
}
