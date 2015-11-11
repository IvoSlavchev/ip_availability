package org.elsysbg.ip.availability;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class User {
	private final List<Date> times = new ArrayList<Date>();
	private String name;
	private boolean logged;
	private int loginCounter;
	private Socket socket;
	
	public User(String name, boolean logged, Socket socket) {
		this.name = name;
		this.logged = logged;
		this.loginCounter = 1;
		this.socket = socket;
		this.times.add(new Date());
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isLogged() {
		return logged;
	}
	public void setLogged(boolean logged) {
		this.logged = logged;
	}
	public int getLoginCounter() {
		return loginCounter;
	}
	public void setLoginCounter(int loginCounter) {
		this.loginCounter = loginCounter;
	}
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	public List<Date> getTimes() {
		return times;
	}
	public void setTimes(Date date) {
		this.times.add(date);
	}
}