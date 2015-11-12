package org.elsysbg.ip.availability;

import java.io.IOException;

public interface IAvailabilityServer {
	void startServer() throws IOException;
	boolean isRunning();
	void stopServer() throws IOException;
	void onClientStopped(IClientHandler clientHandler);
}