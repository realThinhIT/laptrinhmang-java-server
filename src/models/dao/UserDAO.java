package models.dao;

import exception.UserDAOException;
import helpers.CalendarHelper;
import livestream.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.security.MessageDigest;
import java.util.Locale;


public class UserDAO extends BaseDAO {
    public UserDAO() {}

    public static String byteArrayToString(byte[] bytes) {
        StringBuilder buffer = new StringBuilder();
        for (byte b : bytes) {
            buffer.append(String.format(Locale.getDefault(), "%02x", b));
        }
        return buffer.toString();
    }

    public static String sha1Encrypt(String clearString)
    {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.update(clearString.getBytes("UTF-8"));
            return byteArrayToString(messageDigest.digest());
        } catch (Exception ignored) {
            ignored.printStackTrace();
            return null;
        }
    }

    public User rsCreateUserFromRs(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("name"),
                rs.getTimestamp("created_at").toString()
        );
    }

    public ArrayList<User> getAllUsers() throws SQLException {
        ArrayList<User> users = new ArrayList<>();

        ResultSet rs = this.getStatement().executeQuery(
                "SELECT * FROM `users`"
        );

        while (((ResultSet) rs).next()) {
            users.add(
                    rsCreateUserFromRs(rs)
            );
        }

        return users;
    }

    public User getUserById(int userId) throws SQLException {
        PreparedStatement ps = this.preparedStatement(
                "SELECT * FROM `users` WHERE `id` = ? LIMIT 1"
        );
        ps.setInt(1, userId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
           return rsCreateUserFromRs(rs);
        }

        return null;
    }

    public int createNewUser(String username, String password, String name) throws SQLException, UserDAOException {
        if (this.getUserByUsername(username) != null) {
            throw new UserDAOException("An user with this username already exists.");
        }

        PreparedStatement ps = this.preparedStatement(
                "INSERT INTO `users` (username, password, name, created_at)" +
                        "VALUES (?, ?, ?, ?)"
        );
        ps.setString(1, username);
        ps.setString(2, UserDAO.sha1Encrypt(password));
        ps.setString(3, name);
        ps.setTimestamp(4, CalendarHelper.getTimestamp());

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();

        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;
    }

    public User getUserByUsernameAndPassword(String username, String password) throws SQLException {
        PreparedStatement ps = this.preparedStatement(
                "SELECT * FROM `users`" +
                        "WHERE `username` = ? AND `password` = ? LIMIT 1"
        );
        ps.setString(1, username);
        ps.setString(2, UserDAO.sha1Encrypt(password));

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rsCreateUserFromRs(rs);
        }

        return null;
    }

    public User getUserByUsername(String username) throws SQLException {
        PreparedStatement ps = this.preparedStatement(
                "SELECT * FROM `users`" +
                        "WHERE `username` = ? LIMIT 1"
        );
        ps.setString(1, username);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rsCreateUserFromRs(rs);
        }

        return null;
    }

    public User editUserById(int userId, String newName, String newPassword) throws SQLException {
        PreparedStatement ps = this.preparedStatement(
                "UPDATE `users`" +
                        "SET name = IF(CHAR_LENGTH(?), ?, name), password = IF(CHAR_LENGTH(?), ?, password)" +
                        "WHERE id = ?"
        );
        ps.setString(1, newName);
        ps.setString(2, newName);
        ps.setString(3, newPassword);
        ps.setString(4, UserDAO.sha1Encrypt(newPassword));
        ps.setInt(5, userId);

        if (ps.executeUpdate() > 0) {
            return getUserById(userId);
        }

        return null;
    }
}
