package dev.tomr.parkutil.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DataDriver {

    ResultSet customSelectQuery(String query) throws SQLException;

    void createTable(String query) throws SQLException;

    void customInsertQuery(String query) throws SQLException;

    void customUpdateQuery(String query) throws SQLException;

    void customDeleteQuery(String query) throws SQLException;

    void closeConnection() throws SQLException;
}
