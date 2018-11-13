package controllers;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {

    public static Connection db;

    public static Connection getMyConnection() throws SQLException,
            ClassNotFoundException {
        return MySQLConnUtils.getMySQLConnection();
    }

    //
    // Test Connection ...
    //
    public static void main(String[] args) throws SQLException,
            ClassNotFoundException {

        System.out.println("Get connection ... ");

        // Lấy ra đối tượng Connection kết nối vào database.
        db = ConnectionUtils.getMyConnection();
    }

}
