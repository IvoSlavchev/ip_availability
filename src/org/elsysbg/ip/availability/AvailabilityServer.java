package org.elsysbg.ip.availability;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AvailabilityServer {
	private final int port;
	
	public AvailabilityServer(int port) {
		this.port = port;
	}
	
	public void startServer() throws IOException {
		final ServerSocket serverSocket = new ServerSocket(port);		
		final Socket socket = serverSocket.accept();
		final ClientHandler clientHandler = new ClientHandler();
		clientHandler.run(socket);
		serverSocket.close();
	}
}