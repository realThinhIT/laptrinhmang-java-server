package livestream.models;

import helpers.CalendarHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoomMessageDAO extends BaseDAO {

    UserDAO userDAO;

    public RoomMessageDAO() { this.userDAO = new UserDAO(); }

    public RoomMessage rsCreateRoomMessageFromRs(ResultSet rs) throws SQLException {
        return new RoomMessage(
                rs.getInt("id"),
                rs.getString("content"),
                userDAO.getUserById(rs.getInt("user_id")),
                rs.getTimestamp("created_at").toString(),
                rs.getInt("room_id")
        );
    }

    public ArrayList<RoomMessage> getAllMessagesByRoomId(int roomId) throws SQLException {
        ArrayList<RoomMessage> roomMessages = new ArrayList<>();

        PreparedStatement ps = this.preparedStatement(
                "SELECT * FROM `room_messages`" +
                        "WHERE `room_id` = ?"
        );
        ps.setInt(1, roomId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            roomMessages.add(
                    rsCreateRoomMessageFromRs(rs)
            );
        }

        return roomMessages;
    }

    public RoomMessage createNewMessageInRoom(int roomId, int userId, String content) throws SQLException {
        PreparedStatement ps = this.preparedStatement(
                "INSERT INTO `room_messages` (room_id, user_id, content, createdAt)" +
                        "VALUES (?, ?, ?, ?)"
        );
        ps.setInt(1, roomId);
        ps.setInt(2, userId);
        ps.setString(3, content);
        ps.setTimestamp(4, CalendarHelper.getTimestamp());

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();

        int insertedRowId = 0;
        while (rs.next()) {
            insertedRowId = rs.getInt(1);

            return new RoomMessage(
                    insertedRowId,
                    content,
                    userDAO.getUserById(userId),
                    CalendarHelper.getTimestamp().toString(),
                    roomId
            );
        }

        return null;
    }
}
