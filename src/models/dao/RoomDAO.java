package models.dao;

import config.Const;
import helpers.CalendarHelper;
import livestream.models.Room;
import livestream.models.RoomMessage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoomDAO extends BaseDAO {

    UserDAO userDAO;
    RoomUserDAO roomUserDAO;

    public RoomDAO() {
        this.userDAO = new UserDAO();
        this.roomUserDAO = new RoomUserDAO();
    }

    public Room rsCreateRoomFromRs(ResultSet rs) throws SQLException {
        return new Room(
                rs.getInt("id"),
                rs.getString("name"),
                userDAO.getUserById(rs.getInt("owner_id")),
                rs.getTimestamp("created_at").toString(),
                rs.getInt("status"),
                null,
                roomUserDAO.getRoomUsersByRoomId(rs.getInt("id"), 1),
                roomUserDAO.getUsersByRoomId(rs.getInt("id"), 1)
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

    public Room createNewRoom(String name, int ownerId) throws SQLException {
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

        int insertedRowId = 0;
        if (rs.next()) {
            insertedRowId = rs.getInt(1);

            System.out.println("Created " + insertedRowId);


            return new Room(
                    insertedRowId,
                    name,
                    userDAO.getUserById(ownerId),
                    CalendarHelper.getTimestamp().toString(),
                    Const.STATUS_ROOM_ACTIVE,
                    null,
                    roomUserDAO.getRoomUsersByRoomId(rs.getInt("id"), 1),
                    roomUserDAO.getUsersByRoomId(insertedRowId, 1)
            );
        }

        return null;
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
