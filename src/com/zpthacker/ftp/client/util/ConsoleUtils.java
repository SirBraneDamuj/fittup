package com.zpthacker.ftp.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleUtils {
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	
	public static void print(String str) {
		System.out.print(str);
	}
	
	public static void println(String str) {
		System.out.println(str);
	}
	
	public static String readLine() {
		String retval = null;
		try {
			retval = in.readLine();
		} catch(IOException e) {
			println("Error reading console input");
			System.exit(1);
		}
		return retval;
	}
}