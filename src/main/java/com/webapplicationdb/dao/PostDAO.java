package com.webapplicationdb.dao;

import com.webapplicationdb.model.Post;
import com.webapplicationdb.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostDAO {
    private static final String INSERT_POST = "INSERT INTO posts (user_name, post1, post2, post3, post4, post5) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_POSTS = "SELECT * FROM posts WHERE user_name = ?";
    private static final String UPDATE_POSTS = "UPDATE posts SET post1 = ?, post2 = ?, post3 = ?, post4 = ?, post5 = ? WHERE user_name = ?";
    private static final String DELETE_POSTS = "DELETE FROM posts WHERE user_name = ?";

    public boolean createUserPosts(String userName) {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_POST)) {
            
            stmt.setString(1, userName);
            for (int i = 2; i <= 6; i++) {
                stmt.setNull(i, Types.VARCHAR);
            }
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Post getUserPosts(String userName) {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_POSTS)) {
            
            stmt.setString(1, userName);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Post post = new Post(userName);
                String[] posts = new String[5];
                for (int i = 0; i < 5; i++) {
                    posts[i] = rs.getString("post" + (i + 1));
                }
                post.setPosts(posts);
                return post;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updatePosts(Post post) {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_POSTS)) {
            
            String[] posts = post.getPosts();
            for (int i = 0; i < 5; i++) {
                if (posts[i] != null && !posts[i].trim().isEmpty()) {
                    stmt.setString(i + 1, posts[i]);
                } else {
                    stmt.setNull(i + 1, Types.VARCHAR);
                }
            }
            stmt.setString(6, post.getUserName());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePosts(String userName) {
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_POSTS)) {
            
            stmt.setString(1, userName);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Post> getFollowedUsersPosts(List<String> followedUsers) {
        List<Post> posts = new ArrayList<>();
        if (followedUsers.isEmpty()) {
            return posts;
        }

        StringBuilder query = new StringBuilder("SELECT * FROM posts WHERE user_name IN (");
        for (int i = 0; i < followedUsers.size(); i++) {
            query.append(i == 0 ? "?" : ", ?");
        }
        query.append(")");

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {
            
            for (int i = 0; i < followedUsers.size(); i++) {
                stmt.setString(i + 1, followedUsers.get(i));
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Post post = new Post(rs.getString("user_name"));
                String[] userPosts = new String[5];
                for (int i = 0; i < 5; i++) {
                    userPosts[i] = rs.getString("post" + (i + 1));
                }
                post.setPosts(userPosts);
                posts.add(post);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }
}
