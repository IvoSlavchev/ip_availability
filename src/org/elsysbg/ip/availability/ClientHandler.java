package org.elsysbg.ip.availability;

import java.util.Scanner;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class ClientHandler implements Runnable, IClientHandler {	
	private final IAvailabilityServer availabilityServer;
	private final Socket socket;
	
	public ClientHandler(IAvailabilityServer availabilityServer, Socket socket) {
		this.availabilityServer = availabilityServer;
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			final PrintStream out = new PrintStream(socket.getOutputStream());
			final Scanner scanner = new Scanner(socket.getInputStream());
			final ICommandHandler commandHandler = new CommandHandler();
			out.println("Vuvedi komanda: ");
			while (scanner.hasNextLine()) {									
				final String line = scanner.nextLine();				
				out.println(commandHandler.execute(line, availabilityServer, socket));
				out.println("Vuvedi komanda: ");
			}
			scanner.close();
			out.close();
		} catch (IOException e) {
				e.printStackTrace();
		} finally {
			availabilityServer.onClientStopped(this);
		}
	}	

	@Override
	public void stopClient() throws IOException {
		socket.close();
	}
}