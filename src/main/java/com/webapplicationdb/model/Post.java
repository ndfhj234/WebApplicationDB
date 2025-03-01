package com.webapplicationdb.model;

import java.util.ArrayList;
import java.util.List;

public class Post {
    private String userName;
    private String[] posts;
    private static final int MAX_POSTS = 5;

    public Post() {
        this.posts = new String[MAX_POSTS];
    }

    public Post(String userName) {
        this.userName = userName;
        this.posts = new String[MAX_POSTS];
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String[] getPosts() {
        return posts;
    }

    public void setPosts(String[] posts) {
        this.posts = posts;
    }

    public List<String> getNonNullPosts() {
        List<String> nonNullPosts = new ArrayList<>();
        for (String post : posts) {
            if (post != null && !post.trim().isEmpty()) {
                nonNullPosts.add(post);
            }
        }
        return nonNullPosts;
    }

    public void addPost(String newPost) {
        // Shift posts to make room for new post at index 0
        for (int i = MAX_POSTS - 1; i > 0; i--) {
            posts[i] = posts[i - 1];
        }
        posts[0] = newPost;
    }

    public void deletePost(int index) {
        if (index >= 0 && index < MAX_POSTS) {
            // Shift posts after deleted post up
            for (int i = index; i < MAX_POSTS - 1; i++) {
                posts[i] = posts[i + 1];
            }
            posts[MAX_POSTS - 1] = null;
        }
    }
}
