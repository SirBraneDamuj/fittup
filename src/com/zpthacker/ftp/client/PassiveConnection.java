/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * PassiveConnection.java
 * Represents a connection established in passive mode.
 * Parses the h1,h2,h3,h4,p1,p2 address response and creates an appropriate socket.
 */

package com.zpthacker.ftp.client;

import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PassiveConnection extends DataConnection {
	
	private String ip;
	private int port;
	private Socket socket;
	
	public PassiveConnection(String pasvResponse) {
		super();
		String ipInfo = this.extractIPAndPortInfoFromPasvResponse(pasvResponse);
		this.ip = this.extractIPFromPasvString(ipInfo);
		this.port = this.extractPortFromPasvString(ipInfo);
	}
	
	public PassiveConnection(String ip, int port) {
		super();
		this.ip = ip;
		this.port = port;
	}

	@Override
	public Socket getSocket() {
		if(this.socket == null) {
			this.socket = this.createSocket();
		}
		return this.socket;	
	}
	
	private Socket createSocket() {
		try {
			return new Socket(this.ip, this.port);
		} catch(IOException e) {
			printConnectionFailedMessage(e);
			return null;
		}
	}
	
	@Override
	public void close() {
		try {
			if(this.socket != null) {
				this.socket.close();
			}
		} catch(IOException e) {
			return;
		}
	}
	
	/*
	 * Uses a regex to extract the address string from the PASV response.
	 */
	private String extractIPAndPortInfoFromPasvResponse(String response) {
		Pattern p = Pattern.compile("(\\d{1,3},){5}\\d{1,3}");
		Matcher m = p.matcher(response);
		if(m.find()) {
			return m.group();
		} else {
			return null;
		}
	}
	
	/*
	 * From the PASV address string, extracts the IP address.
	 */
	private String extractIPFromPasvString(String pasvString) {
		Pattern p = Pattern.compile("\\d{1,3}");
		Matcher m = p.matcher(pasvString);
		String ip = "";
		int count = 0;
		while(m.find()) {
			count++;
			if(count == 5) {
				break;
			}
			ip += m.group() + ".";
		}
		ip = ip.substring(0, ip.length()-1);
		return ip;
	}
	
	/*
	 * From the PASV address string, extracts the port number.
	 * From a string h1,h2,h3,h4,p1,p2
	 * Where h1.h2.h3.h4 is the IP address
	 * the port number is 256 * p1 + p2
	 */
	private int extractPortFromPasvString(String pasvString) {
		Pattern p = Pattern.compile("\\d{1,3}");
		Matcher m = p.matcher(pasvString);
		int count = 0;
		int port = 0;
		while(m.find()) {
			count++;
			if(count == 5) {
				port += Integer.parseInt(m.group()) * 256;
			} else if(count == 6) {
				port += Integer.parseInt(m.group());
			}
		}
		return port;
	}

}
