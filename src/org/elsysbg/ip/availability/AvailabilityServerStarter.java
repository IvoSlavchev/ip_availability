package org.elsysbg.ip.availability;

import java.io.IOException;

public class AvailabilityServerStarter {
	private static final int SERVER_PORT = 31111;
	
	public static void main(String[] args) throws IOException {
		final IAvailabilityServer server = new AvailabilityServer(SERVER_PORT);
		server.startServer();
	}
}