package dev.tomr.parkutil.data;

import dev.tomr.parkutil.exceptions.PlayerDriverException;
import dev.tomr.parkutil.player.implementation.Player;
import lombok.AllArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
public class PlayerDriver {
    private DataDriver dataDriver;

    public boolean checkPlayerExistence(String uuid) throws SQLException {
        String sql = "SELECT id,uuid FROM players WHERE uuid = ?;";
        Object[] objects = new Object[1];
        objects[0] = uuid;
        ResultSet results = dataDriver.customSelectQuery(sql, objects);
        if (results.next()) {
            return true;
        } else {
            return false;
        }
    }

    public Player registerPlayer(org.bukkit.entity.Player player) throws SQLException, PlayerDriverException {
        String sql = "INSERT INTO players (uuid, lastKnownIp) VALUES(?, ?);";
        Object[] objects = new Object[2];
        objects[0] = player.getUniqueId().toString();
        objects[1] = player.getAddress().getAddress().toString();
        dataDriver.customInsertQuery(sql, objects);
        String sqlQuery = "SELECT id,uuid FROM players WHERE uuid = ?;";
        Object[] objectsQuery = new Object[1];
        objectsQuery[0] = player.getUniqueId().toString();
        ResultSet results = dataDriver.customSelectQuery(sqlQuery, objectsQuery);
        if (results.next()) {
            return new Player(results.getInt(0), player.getUniqueId().toString(), player.getAddress().getAddress().toString());
        } else {
            throw new PlayerDriverException("User could not be identified after registering");
        }
    }

    public Player retrievePlayer(String uuid) throws SQLException, PlayerDriverException {
        String sql = "SELECT * FROM players WHERE uuid = ?";
        Object[] objects = new Object[1];
        objects[0] = uuid;
        ResultSet resultSet = dataDriver.customSelectQuery(sql, objects);
        if (resultSet.next()) {
            return new Player(resultSet.getInt(0), resultSet.getString(1), resultSet.getString(2));
        } else {
            throw new PlayerDriverException("User could not be found in the database");
        }
    }

    public void createTable() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS `players` (\n" +
                "\t`id` INT NOT NULL AUTO_INCREMENT,\n" +
                "\t`uuid` VARCHAR(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,\n" +
                "\t`lastKnownIp` TEXT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL\n" +
                ");";
        dataDriver.createTable(sql);
    }

}
