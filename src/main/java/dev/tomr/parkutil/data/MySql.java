package dev.tomr.parkutil.data;

import dev.tomr.parkutil.Parkutil;

import java.sql.*;

public class MySql implements DataDriver {
    private Connection conn;
    private DatabaseMetaData meta;

    public MySql(String host, String user, String pass, String database) {
        String url = "jdbc:mysql://" + host + "/"
                + database + "?user=" + user + "&password=" + pass;

        try (Connection connection = DriverManager.getConnection(url)) {
            this.conn = connection;
            if (conn != null) {
                meta = conn.getMetaData();
                Parkutil.getParkutil().getLogger().info("Connected to MySQL database @ " + host);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public ResultSet customSelectQuery(String query) throws SQLException {
        Statement statement = conn.createStatement();
        return statement.executeQuery(query);
    }

    @Override
    public void createTable(String query) throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate(query);
        statement.close();
        conn.commit();
    }

    @Override
    public void customInsertQuery(String query) throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate(query);
        statement.close();
        conn.commit();
    }

    @Override
    public void customUpdateQuery(String query) throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate(query);
        statement.close();
        conn.commit();
    }

    @Override
    public void customDeleteQuery(String query) throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate(query);
        statement.close();
        conn.commit();
    }

    @Override
    public void closeConnection() throws SQLException {
        conn.close();
    }
}
