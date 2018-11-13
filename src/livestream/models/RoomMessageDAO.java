package livestream.models;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RoomMessageDAO extends BaseDAO {
    public RoomMessageDAO() {}

    public RoomMessage rsCreateRoomMessageFromRs(ResultSet rs) throws SQLException {
//        return new RoomMessage(
//                rs.getInt("id"),
//                rs.getString("content"),
//
//        );
        return null;
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
}
