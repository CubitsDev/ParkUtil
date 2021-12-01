package dev.tomr.parkutil.player.implementation;

import lombok.Getter;
import org.bukkit.Bukkit;

public class Player implements dev.tomr.parkutil.player.Player {
    private final org.bukkit.entity.Player bukkitPlayer;
    @Getter
    private int id;
    @Getter
    private String uuid;
    @Getter
    private String lastKnownIp;

    public Player(org.bukkit.entity.Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }

    public Player(int id, String uuid, String lastKnownIp) {
        this.id = id;
        this.uuid = uuid;
        this.lastKnownIp = lastKnownIp;
        this.bukkitPlayer = Bukkit.getPlayer(uuid);
    }

    @Override
    public org.bukkit.entity.Player getBukkitPlayer() {
        return bukkitPlayer;
    }
}
