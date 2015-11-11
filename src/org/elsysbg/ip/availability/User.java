package org.elsysbg.ip.availability;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User {
	final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH'_'mm'_'ss.SSSZ");
	private final List<String> times = new ArrayList<String>();
	private String name;
	private boolean logged;
	private int loginCounter;
	private Socket socket;
	
	public User(String name, boolean logged, Socket socket) {
		this.name = name;
		this.logged = logged;
		this.loginCounter = 1;
		this.socket = socket;		
		Date date = new Date();
        String dateFormatted = DATE_FORMAT.format(date);
		this.times.add(dateFormatted);
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
	public List<String> getTimes() {
		return times;
	}
	public void setTimes(Date date) {
		String dateFormatted = DATE_FORMAT.format(date);
		this.times.add(dateFormatted);
	}
}