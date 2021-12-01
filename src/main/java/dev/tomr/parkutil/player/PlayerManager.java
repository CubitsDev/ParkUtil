package dev.tomr.parkutil.player;

import dev.tomr.parkutil.Parkutil;
import dev.tomr.parkutil.data.DataDriver;
import dev.tomr.parkutil.data.PlayerDriver;
import dev.tomr.parkutil.exceptions.PlayerDriverException;
import lombok.Getter;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PlayerManager {
    private DataDriver dataDriver = Parkutil.getParkutil().getDataDriver();
    private PlayerDriver playerDriver;
    @Getter
    private List<Player> players;

    public PlayerManager() {
        playerDriver = new PlayerDriver(dataDriver);
        try {
            playerDriver.createTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public Player registerPlayer(org.bukkit.entity.Player player) {
        try {
            boolean exists = playerDriver.checkPlayerExistence(player.getUniqueId().toString());
            if (!exists) {
                Player generatedPlayer = playerDriver.registerPlayer(player);
                players.add(generatedPlayer);
                return generatedPlayer;
            } else {
                Player foundPlayer = playerDriver.retrievePlayer(player.getUniqueId().toString());
                players.add(foundPlayer);
                return foundPlayer;
            }
        } catch (SQLException | PlayerDriverException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public Optional<Player> getPlayer(String uuid) {
        return players.stream().filter(Player -> Player.getUuid().equals(uuid)).findFirst();
    }
}
