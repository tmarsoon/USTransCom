package application;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
	 private static final String LOG_FILE_PATH = "simulation_log.txt";

	    static {
	        try (PrintWriter writer = new PrintWriter(LOG_FILE_PATH)) {
	            writer.print(""); // Clear the file content
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public static void log(String message) {
	        try (FileWriter writer = new FileWriter(LOG_FILE_PATH, true)) {
	            writer.write(message + System.lineSeparator());
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}