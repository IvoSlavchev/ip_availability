package org.elsysbg.ip.availability;

import java.net.Socket;
import java.util.Date;
import java.util.List;

public interface IUser {
	String getName();
	void setName(String name);
	boolean isLogged();
	void setLogged(boolean logged);
	int getLoginCounter();
	void setLoginCounter(int loginCounter);
	Socket getSocket();
	void setSocket(Socket socket);
	List<String> getTimes();
	void setTimes(Date date);
}