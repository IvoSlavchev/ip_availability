package org.elsysbg.ip.availability;

import java.io.IOException;
import java.net.Socket;

public interface ICommandHandler {
	String execute(String cmd, IAvailabilityServer availServer, Socket socket) throws IOException;
}