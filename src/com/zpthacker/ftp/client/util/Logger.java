package com.zpthacker.ftp.client.util;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	private static Logger instance;
	
	public static boolean initWithFilename(String filename) {
		try {
			instance = new Logger(filename);
			return true;
		} catch(IOException e) {
			return false;
		}
	}
	
	public static void close() {
		instance.closeStream();
	}
	
	public static void request(String request) {
		instance.writeLine(formRequestLogLine(request));
		instance.flush();
	}
	
	private static String formRequestLogLine(String request) {
		return formLogLine("[REQUEST] " + request);
	}
	
	public static void response(String response) {
		instance.writeLine(formResponseLogLine(response));
		instance.flush();
	}
	
	private static String formResponseLogLine(String response) {
		return formLogLine("[RESPONSE] " + response);
	}
	
	private static String formLogLine(String message) {
		return "[" + getCurrentTimeStamp() + "]" + message;
	}
	
	private static String getCurrentTimeStamp() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
	
	private BufferedWriter out;
	
	private Logger(String filename) throws IOException {
		this.out = new BufferedWriter(new FileWriter(filename, true));
	}
	
	private void writeLine(String line) {
		try {
		this.out.write(line);
		this.out.newLine();
		} catch(IOException e) {
			println("Error writing to file");
		}
	}
	
	private void flush() {
		try {
			this.out.flush();
		} catch(IOException e) {
			println("Flush error");
		}
	}
	
	private void closeStream() {
		try {
			this.out.close();
		} catch(IOException e) {
			println("Failed to close log file");
		}
	}
}
