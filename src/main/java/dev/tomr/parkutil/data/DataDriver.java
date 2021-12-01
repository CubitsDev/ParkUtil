package dev.tomr.parkutil.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface DataDriver {

    ResultSet customSelectQuery(String query, Object[] args) throws SQLException;

    void createTable(String query) throws SQLException;

    void customInsertQuery(String query, Object[] args) throws SQLException;

    void customUpdateQuery(String query, Object[] args) throws SQLException;

    void customDeleteQuery(String query, Object[] args) throws SQLException;

    void closeConnection() throws SQLException;
}
