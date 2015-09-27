/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ocs.indaba.vo;

/**
 *
 * @author Jeff
 */
public class UserDisplay {
    private int userId;
    private String bio;
    private int permission;
    private String displayUsername;

    public UserDisplay() {
    }

    public UserDisplay(int userId, int permission, String displayUsername, String bio) {
        this.userId = userId;
        this.bio = bio;
        this.permission = permission;
        this.displayUsername = displayUsername;
    }


    public String getDisplayUsername() {
        return displayUsername;
    }

    public void setDisplayUsername(String displayUsername) {
        this.displayUsername = displayUsername;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String toString() {
        StringBuffer strBuf = new StringBuffer("UserDisplay[");

        strBuf.append("userId=").append(userId);
        strBuf.append("permission=").append(permission);
        strBuf.append("displayUsername=").append(displayUsername);
        strBuf.append("bio=").append(bio);
        strBuf.append(']');

        return strBuf.toString();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + userId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDisplay other = (UserDisplay) obj;
		if (userId != other.userId)
			return false;
		return true;
	}
}
