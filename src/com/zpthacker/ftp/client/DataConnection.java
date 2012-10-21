package com.zpthacker.ftp.client;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;
import java.net.Socket;

public abstract class DataConnection {
	private String type;
	
	public DataConnection() {}
	
	public abstract Socket getSocket();
	
	public String getType() {
		return this.type;
	}
	
	public void printConnectionFailedMessage(Exception e) {
		println("Failed to create data connection");
		println(e.getMessage());
	}
	
	public abstract void close();
}
