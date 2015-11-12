package org.elsysbg.ip.availability;

import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User implements IUser {
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
		this.times.add(DATE_FORMAT.format(new Date()));
	}
	
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public boolean isLogged() {
		return logged;
	}
	@Override
	public void setLogged(boolean logged) {
		this.logged = logged;
	}
	@Override
	public int getLoginCounter() {
		return loginCounter;
	}
	@Override
	public void setLoginCounter(int loginCounter) {
		this.loginCounter = loginCounter;
	}
	@Override
	public Socket getSocket() {
		return socket;
	}
	@Override
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	@Override
	public List<String> getTimes() {
		return times;
	}
	@Override
	public void setTimes(Date date) {
		this.times.add(DATE_FORMAT.format(date));
	}
}