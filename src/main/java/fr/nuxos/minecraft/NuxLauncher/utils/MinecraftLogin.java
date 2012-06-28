package fr.nuxos.minecraft.NuxLauncher.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.PublicKey;
import java.util.Hashtable;

import javax.net.ssl.HttpsURLConnection;

import fr.nuxos.minecraft.NuxLauncher.NuxLauncher;
import fr.nuxos.minecraft.NuxLauncher.Performer;
import fr.nuxos.minecraft.NuxLauncher.exceptions.*;

public class MinecraftLogin implements Runnable {
	private NuxLauncher launcher;
	private Performer performer;
	private String username;
	private String password;
	public Hashtable<String, String> loggingInfo;

	public MinecraftLogin(NuxLauncher launcher, Performer performer, String username, String password) {
		this.launcher = launcher;
		this.performer = performer;
		this.username = username;
		this.password = password;
	}

	public void run() {
		if (username.isEmpty() || password.isEmpty()) {
			performer.authFinishedFail("Champs incomplets");
		} else {
			performer.changeProgress("Authentification ...", 0);
			login();
		}
	}

	private void login() {
		try {

			String parameters = "user=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=" + launcher.getMinecraftLauncherVersion();
			performer.changeProgress("Connexion à minecraft.net ...", 10);
			String result = executePostSSL("https://login.minecraft.net/", parameters, "minecraft");

			if (result == null) {
				performer.authFinishedFail("Can't connect");
				throw new MCNetworkException();
			}

			if (!result.contains(":")) {
				if (result.trim().equals("Bad login")) {
					performer.authFinishedFail("mauvais username/password");
					throw new BadLoginException();
				} else if (result.trim().equals("Old version")) {
					performer.authFinishedFail("version trop vieille");
					throw new OutdatedMCLauncherException();
				} else if (result.trim().contains("User not premium")) {
					performer.authFinishedFail("compte non payé");
					throw new MinecraftUserNotPremiumException();
				} else if (result.trim().contains("Account migrated, use e-mail as username.")) {
					performer.authFinishedFail("utilisez votre adresse e-mail");
					throw new AccountMigratedException();
				} else {
					performer.authFinishedFail("erreur inconnue");
					System.err.print("Unknown login result : \"" + result + "\"");
				}
			} else {
				performer.changeProgress("Lecture des données ...", 50);

				String[] values = result.split(":");

				loggingInfo = new Hashtable<String, String>();
				loggingInfo.put("username", values[2].trim());
				loggingInfo.put("latestversion", values[0].trim());
				loggingInfo.put("downloadticket", values[1].trim());
				loggingInfo.put("sessionid", values[3].trim());

				performer.authFinishedSuccess(loggingInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String executePostSSL(String targetURL, String urlParameters, String CertifName) {

		HttpsURLConnection connection = null;

		try {
			URL url = new URL(targetURL);

			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
			// connection.setRequestProperty("Content-Language", "en-US");
			// useless
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.connect();

			java.security.cert.Certificate[] certs = connection.getServerCertificates();

			byte[] bytes = new byte[294];
			DataInputStream minecraftCert = new DataInputStream(MinecraftLogin.class.getResourceAsStream("/" + CertifName + ".key"));
			minecraftCert.readFully(bytes);
			minecraftCert.close();

			java.security.cert.Certificate remoteCert = certs[0];
			PublicKey remoteCertPublicKey = remoteCert.getPublicKey();
			byte[] data = remoteCertPublicKey.getEncoded();

			for (int i = 0; i < data.length; i++) {
				if (data[i] == bytes[i])
					continue;
				throw new RuntimeException("Error : key mismatch !");
			}

			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			InputStream is = connection.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));

			StringBuffer response = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();

			String str1 = response.toString();
			return str1;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
