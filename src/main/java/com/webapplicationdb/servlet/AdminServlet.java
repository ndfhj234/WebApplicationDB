package com.webapplicationdb.servlet;
import com.webapplicationdb.dao.FollowDAO;
import com.webapplicationdb.dao.PostDAO;
import com.webapplicationdb.dao.UserDAO;
import com.webapplicationdb.model.User;
import com.webapplicationdb.util.SessionUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
    private UserDAO userDAO;
    private PostDAO postDAO;
    private FollowDAO followDAO;
    
    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
        postDAO = new PostDAO();
        followDAO = new FollowDAO();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userRole = SessionUtil.getUserRole(request);
        if (userRole == null || (!userRole.equals("admin") && !userRole.equals("super_admin"))) {
            response.sendRedirect("login.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        if ("create".equals(action)) {
            createUser(request, response, userRole);
        } else if ("update".equals(action)) {
            updateUser(request, response, userRole);
        } else if ("delete".equals(action)) {
            deleteUsers(request, response, userRole);
        } else if ("bulkUpdate".equals(action)) {
            bulkUpdateUsers(request, response, userRole);
        }
    }
    
    private void createUser(HttpServletRequest request, HttpServletResponse response, String adminRole)
            throws IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String role = request.getParameter("userRole");
        if (userName == null || password == null || role == null ||
            userName.trim().isEmpty() || password.trim().isEmpty() || role.trim().isEmpty()) {
            SessionUtil.setErrorMessage(request, "All fields are required");
            response.sendRedirect("create.jsp");
            return;
        }
        if (role.equals("admin") && !adminRole.equals("super_admin")) {
            SessionUtil.setErrorMessage(request, "You don't have permission to create admin users");
            response.sendRedirect("create.jsp");
            return;
        }
        if (!userDAO.isUserNameAvailable(userName)) {
            SessionUtil.setErrorMessage(request, "Username is already taken");
            response.sendRedirect("create.jsp");
            return;
        }
        User user = new User(userName, password, role);
        if (userDAO.createUser(user)) {
            postDAO.createUserPosts(userName);
            followDAO.createUserFollows(userName);
            
            SessionUtil.setSuccessMessage(request, "User created successfully");
            response.sendRedirect("admin.jsp");
        } else {
            SessionUtil.setErrorMessage(request, "Error creating user");
            response.sendRedirect("create.jsp");
        }
    }
    
    private void updateUser(HttpServletRequest request, HttpServletResponse response, String adminRole)
            throws IOException {
        String userName = request.getParameter("userName");
        String newPassword = request.getParameter("newPassword");
        String newRole = request.getParameter("newRole");
        if (userName == null || userName.trim().isEmpty()) {
            SessionUtil.setErrorMessage(request, "Username is required");
            response.sendRedirect("update.jsp");
            return;
        }
        User user = userDAO.getUser(userName);
        if (user == null) {
            SessionUtil.setErrorMessage(request, "User not found");
            response.sendRedirect("update.jsp");
            return;
        }
        if (user.getUserRole().equals("admin") && !adminRole.equals("super_admin")) {
            SessionUtil.setErrorMessage(request, "You don't have permission to modify admin users");
            response.sendRedirect("update.jsp");
            return;
        }
        if (newRole != null && newRole.equals("admin") && !adminRole.equals("super_admin")) {
            SessionUtil.setErrorMessage(request, "You don't have permission to set admin role");
            response.sendRedirect("update.jsp");
            return;
        }       
        if (userDAO.updateUser(userName, newPassword, newRole)) {
            SessionUtil.setSuccessMessage(request, "User updated successfully");
            response.sendRedirect("admin.jsp");
        } else {
            SessionUtil.setErrorMessage(request, "Error updating user");
            response.sendRedirect("update.jsp");
        }
    }
    
    private void deleteUsers(HttpServletRequest request, HttpServletResponse response, String adminRole)
            throws IOException {
        String[] usersToDelete = request.getParameterValues("usersToDelete");
        if (usersToDelete == null || usersToDelete.length == 0) {
            SessionUtil.setErrorMessage(request, "No users selected for deletion");
            response.sendRedirect("delete.jsp");
            return;
        }
        
        boolean overallSuccess = true;
        int successCount = 0;
        Map<String, String> userUpdates = new HashMap<>();
        
        for (String userName : usersToDelete) {
            User user = userDAO.getUser(userName);
            if (user == null) {
                userUpdates.put(userName, "Error: User not found");
                overallSuccess = false;
                continue;
            }
            
            // Only super_admin can delete admin users
            if (user.getUserRole().equals("admin") && !adminRole.equals("super_admin")) {
                userUpdates.put(userName, "Error: Cannot delete admin user");
                overallSuccess = false;
                continue;
            }
            
            if (userDAO.deleteUser(userName)) {
                userUpdates.put(userName, "Success: User deleted successfully");
                successCount++;
            } else {
                userUpdates.put(userName, "Error: Failed to delete user");
                overallSuccess = false;
            }
        }
        
        request.getSession().setAttribute("bulkUpdateResults", userUpdates);
        request.getSession().setAttribute("bulkUpdateSuccess", overallSuccess);
        request.getSession().setAttribute("bulkUpdateSuccessCount", successCount);
        
        if (successCount == 0) {
            SessionUtil.setErrorMessage(request, "Failed to delete any users.");
        } else if (overallSuccess) {
            SessionUtil.setSuccessMessage(request, "Successfully deleted " + successCount + " users.");
        } else {
            SessionUtil.setErrorMessage(request, "Deleted " + successCount + " users with some errors.");
        }
        
        response.sendRedirect("result.jsp?action=delete");
    }
    
    private void bulkUpdateUsers(HttpServletRequest request, HttpServletResponse response, String adminRole)
        throws ServletException, IOException {
        int userCount = Integer.parseInt(request.getParameter("userCount"));
        boolean overallSuccess = true;
        int successCount = 0;
        Map<String, String> userUpdates = new HashMap<>();
        
        for (int i = 0; i < userCount; i++) {
            String originalUsername = request.getParameter("originalUsername_" + i);
            String newUsername = request.getParameter("newUsername_" + i);
            String newPassword = request.getParameter("newPassword_" + i);
            String newRole = request.getParameter("newRole_" + i);
            
            if ((newUsername == null || newUsername.trim().isEmpty()) &&
                (newPassword == null || newPassword.trim().isEmpty()) &&
                (newRole == null || newRole.trim().isEmpty())) {
                continue;
            }
            
            User user = userDAO.getUser(originalUsername);
            if (user == null) {
                userUpdates.put(originalUsername, "Error: User not found");
                overallSuccess = false;
                continue;
            }
            
            if (user.getUserRole().equals("admin") && !adminRole.equals("super_admin")) {
                userUpdates.put(originalUsername, "Error: Cannot modify admin user");
                overallSuccess = false;
                continue;
            }
            
            if (newRole != null && !newRole.trim().isEmpty() && 
                newRole.equals("admin") && !adminRole.equals("super_admin")) {
                userUpdates.put(originalUsername, "Error: Cannot set admin role");
                overallSuccess = false;
                continue;
            }
            
            StringBuilder updateMessage = new StringBuilder("Success: ");
            boolean userUpdateSuccess = false;
            
            try {
                if (newUsername != null && !newUsername.trim().isEmpty() && !newUsername.equals(originalUsername)) {
    
                    if (!userDAO.isUserNameAvailable(newUsername)) {
                        userUpdates.put(originalUsername, "Error: Username '" + newUsername + "' is already taken");
                        overallSuccess = false;
                        continue;
                    }
                    
                    String currentPassword = user.getPassword();
                    String currentRole = user.getUserRole();
                    
                    String finalPassword = (newPassword != null && !newPassword.isEmpty()) ? newPassword : currentPassword;
                    String finalRole = (newRole != null && !newRole.isEmpty()) ? newRole : currentRole;
                    
                    User newUser = new User(newUsername, finalPassword, finalRole);
                    
                    if (userDAO.createUser(newUser)) {
                        updateMessage.append("Created new user '").append(newUsername).append("'. ");
                        
                        postDAO.createUserPosts(newUsername);
                        followDAO.createUserFollows(newUsername);
                        
                        followDAO.updateFollowReferences(originalUsername, newUsername);
                        
                        if (userDAO.deleteUser(originalUsername)) {
                            updateMessage.append("Deleted old user '").append(originalUsername).append("'. ");
                            userUpdateSuccess = true;
                            successCount++;
                        } else {
                            updateMessage = new StringBuilder("Partial success: Created new user but failed to delete old user");
                            overallSuccess = false;
                        }
                    } else {
                        userUpdates.put(originalUsername, "Error: Failed to create new user with username '" + newUsername + "'");
                        overallSuccess = false;
                        continue;
                    }
                } 
                else {

                    String passwordToUpdate = (newPassword != null && !newPassword.trim().isEmpty()) ? newPassword : null;
                    String roleToUpdate = (newRole != null && !newRole.trim().isEmpty()) ? newRole : null;

                    if (passwordToUpdate != null || roleToUpdate != null) {
                        if (userDAO.updateUser(originalUsername, passwordToUpdate, roleToUpdate)) {
                            if (passwordToUpdate != null) {
                                updateMessage.append("Updated password. ");
                            }
                            if (roleToUpdate != null) {
                                updateMessage.append("Updated role to '").append(roleToUpdate).append("'. ");
                            }
                            userUpdateSuccess = true;
                            successCount++;
                        } else {
                            updateMessage = new StringBuilder("Error: Failed to update user");
                            overallSuccess = false;
                        }
                    } else {
                        continue;
                    }
                }
                
                userUpdates.put(originalUsername, updateMessage.toString());
            } 
            catch (Exception e) {
                userUpdates.put(originalUsername, "Error: " + e.getMessage());
                overallSuccess = false;
                e.printStackTrace();
            }
        }
        
        request.getSession().setAttribute("bulkUpdateResults", userUpdates);
        request.getSession().setAttribute("bulkUpdateSuccess", overallSuccess);
        request.getSession().setAttribute("bulkUpdateSuccessCount", successCount);
        
        if (successCount == 0) {
            if (userUpdates.isEmpty()) {
                SessionUtil.setSuccessMessage(request, "No users were modified.");
            } else {
                SessionUtil.setErrorMessage(request, "Failed to update any users.");
            }
        } else if (overallSuccess) {
            SessionUtil.setSuccessMessage(request, "Successfully updated " + successCount + " users.");
        } else {
            SessionUtil.setErrorMessage(request, "Updated " + successCount + " users with some errors.");
        }
        
        response.sendRedirect("result.jsp?action=update");
    }
}