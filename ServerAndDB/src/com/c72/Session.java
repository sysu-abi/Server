package com.c72;

public class Session {
	private int cookie;
	private int uid;
	public Session() {}
	public void setCookie(int cookie) {
		this.cookie= cookie;
	}
	public int getCookie() {
		return cookie;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getUid() {
		return uid;
	}
}
