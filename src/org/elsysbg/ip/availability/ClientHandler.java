package org.elsysbg.ip.availability;

import java.util.Map;
import java.util.Scanner;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;

public class ClientHandler implements Runnable {
	private final Map<String, User> users = new HashMap<String, User>();
	private final AvailabilityServer availabilityServer;
	private final Socket socket;
	private boolean closed;
	
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
			out.println("Enter command: ");
			while (scanner.hasNextLine()) {									
				final String line = scanner.nextLine();				
				out.println(execute(line));
				out.println("Enter command: ");
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
		if(cmds.length > 1) {
			switch (cmds[1]) {
			case "login": 
				return login(cmds, socket);
			case "logout":
				return logout(cmds);
			case "info":
				return info(cmds);
			case "listavailable":
				return listavailable(cmds);
			case "listabsent":
				return listabsent(cmds);
			case "shutdown":
				return shutdown(cmds);
			default:
				return "error:unknowncommand";
			}
		} else {
			return "error:notenougharguments";
		}	
	}

	private boolean isLoggedIn(String user) {
		if (users.containsKey(user)) {
			return users.get(user).isLogged();
		}
		return false;
	}
	
	private String login(String[] cmds, Socket socket) {
		if (!users.containsKey(cmds[0])) {
			final User user = new User(cmds[0], true, socket);
			users.put(cmds[0], user);
			return "ok";
		}
		if (!isLoggedIn(cmds[0])) {
			users.get(cmds[0]).setLogged(true);
			int counter = users.get(cmds[0]).getLoginCounter();
			users.get(cmds[0]).setLoginCounter(++counter);
			return "ok";
		}
		return "already logged in";
	}
	
	private String logout(String[] cmds) {
		if (isLoggedIn(cmds[0])) {
			users.get(cmds[0]).setLogged(false);
			return "ok";
		}
		return "error:notlogged";
	}
	
	private String info(String[] cmds) {
		if (isLoggedIn(cmds[0])) {
			return "ok:" + cmds[2] + ":" + isLoggedIn(cmds[2]) + ":" 
						 + users.get(cmds[2]).getLoginCounter();
		}
		return "error:notlogged";
	}
	
	private String listavailable(String[] cmds) {
		if (isLoggedIn(cmds[0])) {
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
	
	private String listabsent(String[] cmds) {
		if (isLoggedIn(cmds[0])) {
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
	
	private String shutdown(String[] cmds) throws IOException {
		if (isLoggedIn(cmds[0])) {
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