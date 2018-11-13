package controllers;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionUtils {

    public static Connection db;

    public static Connection getMyConnection() throws SQLException,
            ClassNotFoundException {
        return MySQLConnUtils.getMySQLConnection();
    }

    public static void main(String[] args) {

        System.out.println("Get connection ... ");

        // Lấy ra đối tượng Connection kết nối vào database.
        try {

            db = ConnectionUtils.getMyConnection();

            System.out.println("Get connection " + db);

            System.out.println("Done!");

            System.out.println("Waiting client connect...");

            startServer();
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("\n------- Loi server -------\n" + e.getMessage() + "\n --------------------- \n");
        }
    }

    private static void startServer() {
        Server server = new Server();
        server.openServer();
        server.listening();
    }
}
