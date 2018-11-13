package livestream.models;

import config.Const;
import exception.UserDAOException;
import helpers.CalendarHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class RoomDAO extends BaseDAO {

    UserDAO userDAO;

    public RoomDAO() {
        this.userDAO = new UserDAO();
    }

    public Room rsCreateRoomFromRs(ResultSet rs) throws SQLException {
        return new Room(
                rs.getInt("id"),
                rs.getString("name"),
                userDAO.getUserById(rs.getInt("owner_id")),
                rs.getTimestamp("created_at").toString(),
                rs.getInt("status"),
                null,
                null
        );
    }

    public ArrayList<Room> getAllRooms(int roomStatus) throws SQLException {
        ArrayList<Room> rooms = new ArrayList<>();

        PreparedStatement ps = preparedStatement(
                "SELECT * FROM `rooms`" +
                        "WHERE `status` = ?"
        );
        ps.setInt(1, roomStatus);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            rooms.add(
                    rsCreateRoomFromRs(rs)
            );
        }

        return rooms;
    }

    public Room getRoomById(int roomId) throws SQLException {
        PreparedStatement ps = this.preparedStatement(
                "SELECT * FROM `rooms` WHERE `id` = ?"
        );
        ps.setInt(1, roomId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rsCreateRoomFromRs(rs);
        }

        return null;
    }

    public int createNewRoom(String name, int ownerId) throws SQLException {
        PreparedStatement ps = this.preparedStatement(
                "INSERT INTO `rooms` (name, owner_id, created_at, status)" +
                        "VALUES (?, ?, ?, ?)"
        );
        ps.setString(1, name);
        ps.setInt(2, ownerId);
        ps.setTimestamp(3, CalendarHelper.getTimestamp());
        ps.setInt(4, Const.STATUS_ROOM_ACTIVE);

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();

        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;
    }

    public boolean updateRoomStatus(int roomId, int newStatus) throws SQLException {
        PreparedStatement ps = this.preparedStatement(
                "UPDATE `rooms`" +
                        "SET status = " +
                        "WHERE id = ?"
        );
        ps.setInt(1, newStatus);
        ps.setInt(2, roomId);

        return ps.executeUpdate() > 0;
    }
}
