package com.train.entity;

import com.train.util.Cookie;

import java.io.Serializable;
import java.util.Map;
/**
 * 初始化文件加载
 * @author lijintao
 *
 */
public class TicketData implements Serializable {
	private static final long serialVersionUID = -2001688171145623261L;
	private String username;
	private String password;
	private Map<String,Cookie> map;
	
	public TicketData(String username, String password, Map<String, Cookie> map) {
		super();
		this.username = username;
		this.password = password;
		this.map = map;
	}
	@Override
	public String toString() {
		return "TicketData [username=" + username + ", password=" + password + ", map=" + map + "]";
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Map<String, Cookie> getMap() {
		return map;
	}
	public void setMap(Map<String, Cookie> map) {
		this.map = map;
	}
}
