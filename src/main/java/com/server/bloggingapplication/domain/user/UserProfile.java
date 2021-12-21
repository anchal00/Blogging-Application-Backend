package com.server.bloggingapplication.domain.user;

public class UserProfile {

    private User userProfileInfo;
    private boolean isFollowedByYou;

    public UserProfile(User user, boolean isFollowedByYou) {
        this.userProfileInfo = user;
        this.isFollowedByYou = isFollowedByYou;
    }
    public User getUser() {
        return userProfileInfo;
    }
    public boolean isFollowed() {
        return this.isFollowedByYou;
    }

}
