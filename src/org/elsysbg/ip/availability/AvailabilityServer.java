package org.elsysbg.ip.availability;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AvailabilityServer {
	private final List<ClientHandler> clients = Collections.synchronizedList(new LinkedList<ClientHandler>());
	
	private final int port;
	private boolean running;
	
	public AvailabilityServer(int port) {
		this.port = port;
	}
	
	public void startServer() throws IOException {
		setRunning();
		final ServerSocket serverSocket = new ServerSocket(port);	
		while(isRunning()) {
			final Socket socket = serverSocket.accept();
			final ClientHandler clientHandler = new ClientHandler(this, socket);
			clients.add(clientHandler);
			new Thread(clientHandler).start();
		}
		serverSocket.close();
	}
	
	private synchronized void setRunning() {
		if (running) {
			throw new IllegalStateException("Server already started.");
		}
		running = true;
	}
	
	public synchronized boolean isRunning() {
		return running;
	}

	public synchronized void stopServer() throws IOException {
		running = false;		
		for (ClientHandler next : clients) {
			next.stopClient();
		}
	}

	public void onClientStopped(ClientHandler clientHandler) {
		clients.remove(clientHandler);
	}
}