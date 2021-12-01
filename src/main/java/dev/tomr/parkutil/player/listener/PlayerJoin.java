package dev.tomr.parkutil.player.listener;

import dev.tomr.parkutil.Parkutil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Parkutil.getParkutil().getPlayerManager().registerPlayer(event.getPlayer());
    }

}
