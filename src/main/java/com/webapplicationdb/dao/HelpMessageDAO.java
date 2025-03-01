package com.webapplicationdb.dao;

import com.webapplicationdb.model.HelpMessage;
import com.webapplicationdb.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HelpMessageDAO {
    private static final String INSERT_MESSAGE = "INSERT INTO help_messages (user_name, message_text) VALUES (?, ?)";
    private static final String SELECT_LATEST_MESSAGES = "SELECT * FROM help_messages ORDER BY created_at DESC LIMIT 5";
    private static final String DELETE_MESSAGE = "DELETE FROM help_messages WHERE message_id = ?";

    public boolean createMessage(HelpMessage message) {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_MESSAGE, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, message.getUserName());
            stmt.setString(2, message.getMessage());
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    message.setMessageId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<HelpMessage> getLatestMessages() {
        List<HelpMessage> messages = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_LATEST_MESSAGES)) {
            
            while (rs.next()) {
                HelpMessage message = new HelpMessage();
                message.setMessageId(rs.getInt("message_id"));
                message.setUserName(rs.getString("user_name"));
                message.setMessage(rs.getString("message_text"));
                message.setCreatedAt(rs.getTimestamp("created_at"));
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public boolean deleteMessage(int messageId) {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_MESSAGE)) {
            
            stmt.setInt(1, messageId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
