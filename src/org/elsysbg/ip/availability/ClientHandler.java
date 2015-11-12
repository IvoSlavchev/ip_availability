package org.elsysbg.ip.availability;

import java.util.Scanner;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ClientHandler implements Runnable {	
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
			final CommandHandler commandHandler = new CommandHandler();
			out.println("Vuvedi komanda: ");
			while (scanner.hasNextLine()) {									
				final String line = scanner.nextLine();				
				out.println(commandHandler.execute(line, availabilityServer, socket));
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

	public void stopClient() throws IOException {
		socket.close();
		closed = true;
	}
}