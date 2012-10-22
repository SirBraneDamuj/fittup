/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * PortConnection.java
 * Represents a PORT data connection.
 * Uses a ServerSocket to listen on a port number
 * Returns a Socket once a connection has been established.
 */

package com.zpthacker.ftp.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class PortConnection extends DataConnection {
	
	private ServerSocket ssock; //PORT mode listens on a port for a connection from the server
	private Socket socket;
	private int port;
	
	public PortConnection(int port) {
		this.port = port;
		this.ssock = this.createServerSocket();
	}

	@Override
	public Socket getSocket() {
		if(this.ssock != null) {
			try {
				this.closeClientSocket();
				this.socket = this.ssock.accept();
				return this.socket;
			} catch (IOException e) {
				return null;
			}
		}
		return null;
	}
	
	private void closeClientSocket() {
		if(this.socket != null) {
			try {
				this.socket.close();
			} catch (IOException e) {
				return;
			}
		}
	}
	
	@Override
	public void close() {
		try {
			if(this.ssock != null) {
				this.ssock.close();
			}
			if(this.socket != null) {
				this.closeClientSocket();
			}
		} catch(IOException e) {
			return;
		}
	}
	
	private ServerSocket createServerSocket() {
		try {
			return new ServerSocket(this.port);
		} catch(IOException e) {
			return null;
		}
	}

}
