/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * DataConnection.java
 * Abstract data connection object.
 * Passive/PortConnection extends this
 */

package com.zpthacker.ftp.client;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;
import java.net.Socket;

public abstract class DataConnection {
	private String type;
	
	public DataConnection() {}
	
	//return the socket for this connection
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
