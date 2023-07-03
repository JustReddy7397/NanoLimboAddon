package ga.justreddy.wiki.nanolimboaddon.spigot.manager;

import ga.justreddy.wiki.nanolimboaddon.spigot.NanoLimboAddon;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author JustReddy
 */
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class LimboManager {

    private final List<String> limbos;
    private final Map<UUID, Long> afk;
    private final long afkTime;
    public LimboManager(List<String> limbos, int afkTime) {
        this.limbos = limbos;
        this.afk = new HashMap<>();
        this.afkTime = afkTime * 1000L;
    }

    public void sendToAfkServer(Player player) {
        String server = limbos.get(0);
        if (server == null) {
            throw new IllegalStateException("Can't find a AFK server to send " + player.getName() + " to!");
        }
        NanoLimboAddon.getInstance().getBungeeManager().connect(player, server);
    }

    public void addToAfkMap(Player player) {
        afk.put(player.getUniqueId(), System.currentTimeMillis());
    }


    public void removeFromAfkMap(Player player) {
        afk.remove(player.getUniqueId());
    }

    public void updateAfkMap(Player player) {
        if (!isAFK(player)) {
            afk.put(player.getUniqueId(), System.currentTimeMillis());
        } else {
            if (afk.getOrDefault(player.getUniqueId(), 0L) == -1L) return;
            afk.replace(player.getUniqueId(), System.currentTimeMillis());
        }
    }

    public boolean isAFK(Player player){

        if(afk.containsKey(player.getUniqueId())){
            if(afk.get(player.getUniqueId()) == -1L){
                return true;
            }else{
                long timeElapsed = System.currentTimeMillis() - afk.get(player.getUniqueId());
                return timeElapsed >= afkTime;
            }
        }else{
            afk.put(player.getUniqueId(), System.currentTimeMillis());
        }
        return false;
    }
}
