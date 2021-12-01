package dev.tomr.parkutil.player;

public interface Player {

    org.bukkit.entity.Player getBukkitPlayer();

    int getId();

    String getUuid();

    String getLastKnownIp();
}
