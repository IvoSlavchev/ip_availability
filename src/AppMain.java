import java.util.Scanner;

public class AppMain {
	public static boolean shutdown = false;
	
	public static void main(String[] args) {
		final Scanner in = new Scanner(System.in);
		
		while (!shutdown) {
			System.out.print("Enter command: ");
			final String command = in.next();
			final String result = CommandHandler.execute(command);
			System.out.println(result);
		}
		in.close();
	}
}