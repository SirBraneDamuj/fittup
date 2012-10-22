/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * Client.java
 * Represents a client connection to the server.
 * Responsible for formatting and sending requests as well as
 * receiving and fielding responses.
 */

package com.zpthacker.ftp.client;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;

import com.zpthacker.ftp.client.util.FileUtils;
import com.zpthacker.ftp.client.util.Logger;

public class Client {
	String hostname;
	int port;
	Socket clientSocket;
	PrintStream out; //the command output stream
	BufferedReader in; //the command input stream
	boolean passive = true; //default to passive mode
	DataConnection conn; //the data connection object
	int dataPort; //if we're in port mode, this is the port
	
	public Client(String hostname, int port) throws IOException {
		this.hostname = hostname;
		this.port = port;
		this.clientSocket = new Socket(hostname, port);
		this.out = new PrintStream(this.clientSocket.getOutputStream());
		this.in = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
	}
	
	public void type(boolean flag) {
		if(flag) {
			this.writeCommand("type I");
		} else {
			this.writeCommand("type A");
		}
	}
	
	public void setPassiveOff(int port) {
		this.passive = false;
		this.dataPort = port;
	}
	
	public void setPassiveOn() {
		this.passive = true;
	}
	
	public String user(String user) {
		String command = "user " + user;
		return this.writeCommand(command);
	}
	
	public String pass(String pass) {
		String command = "pass " + pass;
		return this.writeCommand(command);
	}
	
	public String pwd() {
		String response = this.writeCommand("pwd");
		if(response.indexOf("257") == -1) {
			return null;
		}
		int start = response.indexOf("\"");
		int end = response.indexOf("\"", start+1);
		return response.substring(start+1, end);
	}
	
	public String mkd(String directoryArg) {
		String command = "mkd " + directoryArg;
		return this.writeCommand(command);
	}
	
	public String cwd(String directoryArg) {
		String command = "cwd " + directoryArg;
		return this.writeCommand(command);
	}
	
	public String cdup() {
		return this.writeCommand("cdup");
	}
	
	private void createConnection() {
		if(this.conn != null) {
			this.conn.close();
		}
		if(this.passive) {
			this.pasv();
		} else {
			this.port();
		}
	}
	
	public boolean list(String argument) {
		this.createConnection();
		String command = "list";
		if(argument != null) {
			command += " " + argument;
		}
		//send data connection data into standard out
		boolean result = this.getData(command, System.out);
		return result;
	}
	
	public boolean retr(String path) {
		if(path == null) {
			return false;
		}
		this.createConnection();
		String command = "retr " + path;
		try {
			FileOutputStream out = FileUtils.getFileOutputStream(path);
			//send data connection data into a file
			println("Initiating data connection...please wait...");
			boolean result = this.getData(command, out);
			out.close();
			return result;
		} catch(IOException e) {
			return false;
		}
	}
	
	/*
	 * Opens the data connection socket and reads data from it
	 * Sends received data into an output stream
	 */
	private boolean getData(String command, OutputStream out) {
		Socket socket = null;
		//passive mode requires the socket to be created before sending the request
		if(this.passive) {
			socket = this.conn.getSocket();
		}
		String response = this.writeCommand(command);
		//port mode requires the socket to be created after sending the request
		if(!this.passive) {
			socket = this.conn.getSocket();
		}
		if(response.indexOf("150") != -1) { //received a mark from the server
			this.readDataSocket(socket, out);
			try {
				response = this.readLine();
				if(response.indexOf("226") == -1) {
					return false;
				} else { //transfer successful
					return true;
				}
			} catch(IOException e) {
				return false;
			}
		} else {
			return false;
		}
	}
	
	//PASV must be sent prior to all data connections when in passive mode
	public void pasv() {
		String response = this.writeCommand("pasv");
		if(response.indexOf("227") == -1) {
			println("PASV command failed");
		}
		this.conn = new PassiveConnection(response);
	}
	
	//PORT must be sent prior to all data connections when in port mode
	public void port() {
		String portString = "," + Integer.toString(this.dataPort/256) + ",";
		portString += this.dataPort % 256;
		String argString = this.clientSocket.getLocalAddress().toString().substring(1).replaceAll("\\.", ",") + portString;
		String response = this.writeCommand("port " + argString);
		if(response.indexOf("200") == -1) {
			println("PORT command failed");
		}
		this.conn = new PortConnection(this.dataPort);
	}
	
	/*
	 * implemented as requested in the assignment
	 * but not exposed to a CLI command
	 * because the user shouldn't know about FTP commands
	 */
	public String help() {
		return this.writeCommand("help");
	}
	
	/*
	 * Writes a command to the server,
	 * then reads lines until it encounters one that begins with ###<SP>
	 * Lines that don't begin with that indicate that more lines are available to read
	 */
	public String writeCommand(String command) {
		try {
			String response = null;
			this.printLine(command);
			String retval = "";
			do {
				response = this.readLine();
				retval += response + "\n";
			} while(response.indexOf(" ") != 3);
			retval = retval.substring(0, retval.length()-1);
			return retval;
		} catch(IOException e) {
			System.out.println("Error while executing command: " + command);
		}
		return null;
	}
	
	public void close() throws IOException {
		this.out.close();
		this.in.close();
		this.clientSocket.close();
		this.conn.close();
	}
	
	public void printLine(String line) {
		this.out.println(line);
		Logger.request(line);
	}
	
	public String readLine() throws IOException {
		String line = in.readLine();
		Logger.response(line);
		return line;
	}
	
	public boolean standby() throws IOException {
		return in.ready();
	}
	
	public void readDataSocket(Socket socket, OutputStream out) {
		int bufSize = 512;
		try {
			InputStream dataIn = socket.getInputStream();
			while(true) {
				byte[] buf = new byte[bufSize];
				int count = dataIn.read(buf);
				if(count == -1) {
					break;
				} else if(count < bufSize) {
					buf = Arrays.copyOfRange(buf, 0, count);
				}
				out.write(buf);
			}
			socket.close();
		} catch(IOException e) {
			return;
		}
	}
	
	public String getInfo() {
		return this.hostname + ":" + this.port;
	}
}
