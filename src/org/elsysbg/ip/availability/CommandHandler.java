package org.elsysbg.ip.availability;

import java.io.IOException;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommandHandler implements ICommandHandler {
	private final Map<String, User> users = new HashMap<String, User>();
	private String currUser;
	
	private boolean isLoggedIn(String user) {
		if (users.containsKey(user)) {
			return users.get(user).isLogged();
		}
		return false;
	}
	
	@Override
	public String execute(String cmd, IAvailabilityServer availServer, Socket socket) throws IOException {
		final String[] cmds = cmd.split(":");
		switch (cmds[0]) {
			case "login": 
				return login(cmds, socket);
			case "logout":
				return logout();
			case "info":
				return info(cmds);
			case "listavailable":
				return listavailable();
			case "listabsent":
				return listabsent();
			case "shutdown":
				return shutdown(availServer);
			default:
				return "error:unknowncommand";
		}
	}
	
	private String login(String[] cmds, Socket socket) {
		logout();
		currUser = cmds[1];
		if (!users.containsKey(currUser)) {
			final User user = new User(currUser, true, socket);
			users.put(currUser, user);		
			return "ok";
		}
		users.get(currUser).setLogged(true);
		int counter = users.get(currUser).getLoginCounter();
		users.get(currUser).setLoginCounter(++counter);
		users.get(currUser).setTimes(new Date());			
		return "ok";
	}
	
	private String logout() {
		if (isLoggedIn(currUser)) {
			users.get(currUser).setLogged(false);
			users.get(currUser).setTimes(new Date());
			return "ok";
		}
		return "error:notlogged";
	}
	
	private String info(String[] cmds) {
		if (isLoggedIn(currUser)) {
			String userTimes = "";
			for (String next : users.get(cmds[1]).getTimes()) {
				userTimes += ":" + next;
			}
			return "ok:" + cmds[1] + ":" + isLoggedIn(cmds[1]) + ":" 
						 + users.get(cmds[1]).getLoginCounter() + userTimes;
		}
		return "error:notlogged";
	}
	
	private String listavailable() {
		if (isLoggedIn(currUser)) {
			String result = "ok";
			for (IUser next : users.values()) {
				if (next.isLogged()) {
					result += ":" + next.getName();
				}
			}
			return result;
		}
		return "error:notlogged";
	}
	
	private String listabsent() {
		if (isLoggedIn(currUser)) {
			String result = "ok";
			for (IUser next : users.values()) {
				if (!next.isLogged()) {
					result += ":" + next.getName();
				}
			}
			return result;
		}
		return "error:notlogged";
	}
	
	private String shutdown(IAvailabilityServer availabilityServer) throws IOException {
		if (isLoggedIn(currUser)) {
			logout();
			availabilityServer.stopServer();
			return "";
		}
		return "error:notlogged";
	}
}