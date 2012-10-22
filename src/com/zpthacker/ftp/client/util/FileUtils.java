/*
 * Zachary Thacker
 * CS472 Assignment 2a
 * 10/22/2012
 * 
 * FileUtils.java
 * A few static file utilities
 */

package com.zpthacker.ftp.client.util;

import static com.zpthacker.ftp.client.util.ConsoleUtils.println;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
	//write a string to a file that shares a name with the file represented by path
	public static void writeFile(String path, String content) {
		String filename = (new File(path)).getName();
		BufferedWriter out = null;
		try {
		out = new BufferedWriter(new FileWriter(filename));
		out.write(content, 0, content.length());
		out.close();
		} catch(IOException e) {
			println("Error writing file: " + filename);
		}
	}
	
	//returns a FileOutputStream for a file
	public static FileOutputStream getFileOutputStream(String path) throws IOException {
		String filename = (new File(path)).getName();
		return new FileOutputStream(filename);
	}
}
