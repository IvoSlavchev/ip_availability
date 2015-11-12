package org.elsysbg.ip.availability;

import java.io.IOException;

public interface IClientHandler {
	void run();
	void stopClient() throws IOException;
}