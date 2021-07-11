package ru.snchz29.models;

public class FriendPair {
    private String userName;
    private String friendName;

    public FriendPair(String userName, String friendName) {
        this.userName = userName;
        this.friendName = friendName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }
}
