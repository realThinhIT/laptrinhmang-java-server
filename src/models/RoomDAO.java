package models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
                        "WHERE `status` = *"
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

}
