package livestream.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomUserDAO extends BaseDAO {
    public RoomUserDAO() {}

    public int checkUserAlreadyInRoom(int roomId, int userId) throws SQLException {
        PreparedStatement ps = this.preparedStatement(
                "SELECT * FROM `room_users`" +
                        "WHERE room_id = ? AND user_id = ?" +
                        "LIMIT 1"
        );
        ps.setInt(1, roomId);
        ps.setInt(2, userId);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("id");
        }

        return 0;
    }

    public int addUserToRoom(int roomId, int userId, int invitor_id, int request_status, int status) throws SQLException {
        PreparedStatement ps = this.preparedStatement(
                "INSERT INTO `room_users` (room_id, user_id, invitor_id, request_status, status)" +
                        "VALUES (?, ?, ?, ?)"
        );
        ps.setInt(1, roomId);
        ps.setInt(2, userId);
        ps.setInt(3, invitor_id);
        ps.setInt(4, request_status);
        ps.setInt(5, status);

        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();

        if (rs.next()) {
            return rs.getInt(1);
        }

        return 0;
    }

    public boolean updateRoomUserStatus(int rowId, int newStatus) throws SQLException {
        PreparedStatement ps = this.preparedStatement(
                "UPDATE `room_users`" +
                        "SET status = " +
                        "WHERE id = ?"
        );
        ps.setInt(1, newStatus);
        ps.setInt(2, rowId);

        return ps.executeUpdate() > 0;
    }

    public boolean updateRoomUserRequestStatus(int rowId, int newReqStatus) throws SQLException {
        PreparedStatement ps = this.preparedStatement(
                "UPDATE `room_users`" +
                        "SET request_status = " +
                        "WHERE id = ?"
        );
        ps.setInt(1, newReqStatus);
        ps.setInt(2, rowId);

        return ps.executeUpdate() > 0;
    }
}
