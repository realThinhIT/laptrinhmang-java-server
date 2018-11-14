package models.dao;

import controllers.ConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class BaseDAO {
    protected Connection connection = ConnectionUtils.db;
    protected Statement stmt;
    protected PreparedStatement pstmt;

    public BaseDAO() {

    }

    public Statement getStatement() throws SQLException {
        stmt = this.connection.createStatement();
        return stmt;
    }

    public PreparedStatement preparedStatement(String statement) throws SQLException {
        pstmt = this.connection.prepareStatement(
                statement,
                Statement.RETURN_GENERATED_KEYS
        );
        return pstmt;
    }
}
