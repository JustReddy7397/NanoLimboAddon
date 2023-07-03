package ga.justreddy.wiki.nanolimboaddon.spigot.listener;

import ga.justreddy.wiki.nanolimboaddon.spigot.manager.LimboManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * @author JustReddy
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AFKListener implements Listener {

    LimboManager manager;

    public AFKListener(LimboManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        manager.addToAfkMap(e.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e){
        manager.removeFromAfkMap(e.getPlayer());
    }

    @EventHandler
    public void onPlayerMovement(PlayerMoveEvent e){
        manager.updateAfkMap(e.getPlayer());
    }

}
