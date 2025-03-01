package com.webapplicationdb.model;

import java.util.ArrayList;
import java.util.List;

public class Follow {
    private String userName;
    private String[] follows;
    private static final int MAX_FOLLOWS = 3;

    public Follow() {
        this.follows = new String[MAX_FOLLOWS];
    }

    public Follow(String userName) {
        this.userName = userName;
        this.follows = new String[MAX_FOLLOWS];
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String[] getFollows() {
        return follows;
    }

    public void setFollows(String[] follows) {
        this.follows = follows;
    }

    public List<String> getNonNullFollows() {
        List<String> nonNullFollows = new ArrayList<>();
        for (String follow : follows) {
            if (follow != null && !follow.trim().isEmpty()) {
                nonNullFollows.add(follow);
            }
        }
        return nonNullFollows;
    }

    public boolean addFollow(String newFollow) {
        // Check if already following
        for (String follow : follows) {
            if (newFollow.equals(follow)) {
                return false;
            }
        }
        
        // Find first empty slot
        for (int i = 0; i < MAX_FOLLOWS; i++) {
            if (follows[i] == null || follows[i].trim().isEmpty()) {
                follows[i] = newFollow;
                return true;
            }
        }
        return false;
    }

    public boolean unfollow(String followToRemove) {
        for (int i = 0; i < MAX_FOLLOWS; i++) {
            if (followToRemove.equals(follows[i])) {
                follows[i] = null;
                return true;
            }
        }
        return false;
    }

    public boolean isFollowing(String userName) {
        for (String follow : follows) {
            if (userName.equals(follow)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasSpace() {
        for (String follow : follows) {
            if (follow == null || follow.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
