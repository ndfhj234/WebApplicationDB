package com.webapplicationdb.dao;

import com.webapplicationdb.model.Follow;
import com.webapplicationdb.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FollowDAO {
    private static final String INSERT_FOLLOW = "INSERT INTO follows (user_name, follow1, follow2, follow3) VALUES (?, ?, ?, ?)";
    private static final String SELECT_FOLLOWS = "SELECT * FROM follows WHERE user_name = ?";
    private static final String UPDATE_FOLLOWS = "UPDATE follows SET follow1 = ?, follow2 = ?, follow3 = ? WHERE user_name = ?";
    private static final String DELETE_FOLLOWS = "DELETE FROM follows WHERE user_name = ?";

    public boolean createUserFollows(String userName) {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_FOLLOW)) {
            
            stmt.setString(1, userName);
            for (int i = 2; i <= 4; i++) {
                stmt.setNull(i, Types.VARCHAR);
            }
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Follow getUserFollows(String userName) {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_FOLLOWS)) {
            
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Follow follow = new Follow(userName);
                String[] follows = new String[3];
                for (int i = 0; i < 3; i++) {
                    follows[i] = rs.getString("follow" + (i + 1));
                }
                follow.setFollows(follows);
                return follow;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateFollows(Follow follow) {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_FOLLOWS)) {
            
            String[] follows = follow.getFollows();
            for (int i = 0; i < 3; i++) {
                if (follows[i] != null && !follows[i].trim().isEmpty()) {
                    stmt.setString(i + 1, follows[i]);
                } else {
                    stmt.setNull(i + 1, Types.VARCHAR);
                }
            }
            stmt.setString(4, follow.getUserName());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFollows(String userName) {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_FOLLOWS)) {
            
            stmt.setString(1, userName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<String> getFollowedUsers(String userName) {
        List<String> followedUsers = new ArrayList<>();
        Follow follow = getUserFollows(userName);
        
        if (follow != null) {
            for (String followedUser : follow.getFollows()) {
                if (followedUser != null && !followedUser.trim().isEmpty()) {
                    followedUsers.add(followedUser);
                }
            }
        }
        
        return followedUsers;
    }

    public boolean updateFollowReferences(String oldUsername, String newUsername) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = true;
        
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false); 
            
            stmt = conn.prepareStatement("UPDATE follows SET user_name = ? WHERE user_name = ?");
            stmt.setString(1, newUsername);
            stmt.setString(2, oldUsername);
            stmt.executeUpdate();
            stmt.close();
            
            stmt = conn.prepareStatement("UPDATE follows SET follow1 = ? WHERE follow1 = ?");
            stmt.setString(1, newUsername);
            stmt.setString(2, oldUsername);
            stmt.executeUpdate();
            stmt.close();
            
            stmt = conn.prepareStatement("UPDATE follows SET follow2 = ? WHERE follow2 = ?");
            stmt.setString(1, newUsername);
            stmt.setString(2, oldUsername);
            stmt.executeUpdate();
            stmt.close();
            
            stmt = conn.prepareStatement("UPDATE follows SET follow3 = ? WHERE follow3 = ?");
            stmt.setString(1, newUsername);
            stmt.setString(2, oldUsername);
            stmt.executeUpdate();
            
            conn.commit(); 
            
        } catch (SQLException e) {
            e.printStackTrace();
            success = false;
            

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); 
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return success;
    }
}