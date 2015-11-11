package org.elsysbg.ip.availability;

import java.util.Map;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

public class ClientHandler implements Runnable {
	private final Map<String, User> users = new HashMap<String, User>();
	
	private final AvailabilityServer availabilityServer;
	private final Socket socket;
	private boolean closed;
	private String currUser;
	
	public ClientHandler(AvailabilityServer availabilityServer, Socket socket) {
		this.availabilityServer = availabilityServer;
		this.socket = socket;
		this.closed = false;
	}
	
	@Override
	public void run() {
		try {
			final PrintStream out = new PrintStream(socket.getOutputStream());
			final Scanner scanner = new Scanner(socket.getInputStream());
			out.println("Vuvedi komanda: ");
			while (scanner.hasNextLine()) {									
				final String line = scanner.nextLine();				
				out.println(execute(line));
				out.println("Vuvedi komanda: ");
			}
			scanner.close();
			out.close();
		} catch (IOException e) {
			if (closed) {  
				e.printStackTrace();
			}
		} finally {
			availabilityServer.onClientStopped(this);
		}
	}
	
	public String execute(String command) throws IOException {
	final String[] cmds = command.split(":");
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
			return shutdown();
		default:
			return "error:unknowncommand";
		}
	}

	private boolean isLoggedIn(String user) {
		if (users.containsKey(user)) {
			return users.get(user).isLogged();
		}
		return false;
	}
	
	private String login(String[] cmds, Socket socket) {
		currUser = cmds[1];
		if (!users.containsKey(currUser)) {
			final User user = new User(currUser, true, socket);
			users.put(currUser, user);		
			return "ok";
		}
		if (!isLoggedIn(currUser)) {
			users.get(currUser).setLogged(true);
			int counter = users.get(currUser).getLoginCounter();
			users.get(currUser).setLoginCounter(++counter);
			users.get(currUser).setTimes(new Date());			
			return "ok";
		}
		return "error:alreadyloggedin";
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
			for (User next : users.values()) {
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
			for (User next : users.values()) {
				if (!next.isLogged()) {
					result += ":" + next.getName();
				}
			}
			return result;
		}
		return "error:notlogged";
	}
	
	private String shutdown() throws IOException {
		if (isLoggedIn(currUser)) {
			availabilityServer.stopServer();
			return "ok";
		}
		return "error:notlogged";
	}

	public void stopClient() throws IOException {
		socket.close();
		closed = true;
	}
}