import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;


public class CommandHandler {
	public final static Map<String, Integer> usersToLoginCount = new HashMap<String, Integer>();
	public final static LinkedList<String> currentlyLoggedUsers = new LinkedList<String>(); 
	
	public static String execute(String command) {
		final String[] split = command.split(":");
		if(split.length > 1) {
			switch (split[1]) {
			case "login": 
				return login(split);
			case "logout":
				return logout(split);
			case "info":
				return info(split);
			case "listavailable":
				return listavailable(split);
			case "shutdown":
				return shutdown(split);
			default:
				return "error:unknowncommand";
			}
		} else {
			return "error:notenougharguments";
		}
		
	}

	private static String login(String[] split) {
		if (!currentlyLoggedUsers.contains(split[0])) {
			currentlyLoggedUsers.push(split[0]);
			if (!usersToLoginCount.containsKey(split[0])) {
				usersToLoginCount.put(split[0], 1);
			} else {
				int logCounter = usersToLoginCount.get(split[0]);
				usersToLoginCount.put(split[0], ++logCounter);
			}
			return "ok";		
		}
		return "already logged in";
	}
	
	private static String logout(String[] split) {
		if (currentlyLoggedUsers.contains(split[0])) {
			currentlyLoggedUsers.remove(split[0]);
			return "ok";
		}
		return "error:notlogged";
	}
	
	private static String info(String[] split) {
		if (currentlyLoggedUsers.contains(split[0])) {
			return "ok:" + split[2] + ":" + currentlyLoggedUsers.contains(split[2]) 
						 + ":" + usersToLoginCount.get(split[2]);
		} 
		return "error:notlogged";
	}
	
	private static String listavailable(String[] split) {
		if (currentlyLoggedUsers.contains(split[0])) {
			ListIterator<String> it = currentlyLoggedUsers.listIterator();
			String available = "ok";
			while (it.hasNext()) {
				available += ":" + it.next();
			}
			return available;			
		} 
		return "error:notlogged";
	}
	
	private static String shutdown(String[] split) {
		if (currentlyLoggedUsers.contains(split[0])) {
			AppMain.shutdown = true;
			return "ok ";			
		}
		return "error:notlogged";
	}
}