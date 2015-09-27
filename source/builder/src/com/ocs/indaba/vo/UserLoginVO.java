package com.ocs.indaba.vo;

public class UserLoginVO {
	private String username = null;
	private String password = null;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "UserLoginVO [username=" + username + ", password=" + password
				+ "]";
	}

}
