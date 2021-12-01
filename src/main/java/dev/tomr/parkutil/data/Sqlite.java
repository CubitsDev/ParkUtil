package dev.tomr.parkutil.data;


import dev.tomr.parkutil.Parkutil;

import java.sql.*;

public class Sqlite implements DataDriver {
    private Connection conn;
    private DatabaseMetaData meta;

    public Sqlite(String fileName, String directory) {
        String url = "jdbc:sqlite:" + directory + "/" + fileName;

        try (Connection connection = DriverManager.getConnection(url)) {
            this.conn = connection;
            if (conn != null) {
                meta = conn.getMetaData();
                Parkutil.getParkutil().getLogger().info("Connected to SQLite Database");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public ResultSet customSelectQuery(String query, Object[] args) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(query);
        for (int i = 0; i < args.length; i++) {
            statement.setObject(i, args[i]);
        }
        return statement.executeQuery();
    }

    @Override
    public void createTable(String query) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(query);
        statement.execute();
        statement.close();
    }

    @Override
    public void customInsertQuery(String query, Object[] args) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(query);
        for (int i = 0; i < args.length; i++) {
            statement.setObject(i, args[i]);
        }
        statement.execute();
        statement.close();
    }

    @Override
    public void customUpdateQuery(String query, Object[] args) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(query);
        for (int i = 0; i < args.length; i++) {
            statement.setObject(i, args[i]);
        }
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public void customDeleteQuery(String query, Object[] args) throws SQLException {
        PreparedStatement statement = conn.prepareStatement(query);
        for (int i = 0; i < args.length; i++) {
            statement.setObject(i, args[i]);
        }
        statement.executeUpdate();
        statement.close();
    }

    @Override
    public void closeConnection() throws SQLException {
        conn.close();
    }
}
