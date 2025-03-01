package com.webapplicationdb.dao;
import com.webapplicationdb.model.User;
import com.webapplicationdb.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class UserDAO {
    private static final String INSERT_USER = "INSERT INTO account (user_name, password, user_role) VALUES (?, ?, ?)";
    private static final String SELECT_USER = "SELECT * FROM account WHERE user_name = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM account WHERE user_role = 'user'";
    private static final String SELECT_ALL_ADMINS = "SELECT * FROM account WHERE user_role = 'admin'";
    private static final String UPDATE_USER = "UPDATE account SET password = ?, user_role = ? WHERE user_name = ?";
    private static final String DELETE_USER = "DELETE FROM account WHERE user_name = ?";
    
    public boolean createUser(User user) {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_USER)) {
            
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword()); 
            stmt.setString(3, user.getUserRole());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public User getUser(String userName) {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_USER)) {
            
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User();
                user.setUserName(rs.getString("user_name"));
                user.setPassword(rs.getString("password"));
                user.setUserRole(rs.getString("user_role"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_USERS)) {
            
            while (rs.next()) {
                User user = new User();
                user.setUserName(rs.getString("user_name"));
                user.setPassword(rs.getString("password"));
                user.setUserRole(rs.getString("user_role"));
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    
    public List<User> getAllAdmins() {
        List<User> admins = new ArrayList<>();
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_ADMINS)) {
            
            while (rs.next()) {
                User admin = new User();
                admin.setUserName(rs.getString("user_name"));
                admin.setPassword(rs.getString("password"));
                admin.setUserRole(rs.getString("user_role"));
                admins.add(admin);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admins;
    }
    
    public boolean updateUser(String userName, String newPassword, String newRole) {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_USER)) {
            
            User currentUser = getUser(userName);
            if (currentUser == null) {
                return false;
            }
            
            stmt.setString(1, newPassword != null ? newPassword : currentUser.getPassword());
            stmt.setString(2, newRole != null ? newRole : currentUser.getUserRole());
            stmt.setString(3, userName);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteUser(String userName) {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_USER)) {
            
            stmt.setString(1, userName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean validateLogin(String userName, String password) {
        User user = getUser(userName);
        return user != null && password.equals(user.getPassword()); 
    }
    
    public boolean isUserNameAvailable(String userName) {
        return getUser(userName) == null;
    }
    
    public boolean renameUser(String oldUsername, String newUsername) {
        try {
            FollowDAO followDAO = new FollowDAO();
            followDAO.updateFollowReferences(oldUsername, newUsername);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}