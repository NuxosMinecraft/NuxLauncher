package fr.nuxos.minecraft.NuxLauncher.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.PublicKey;

import javax.net.ssl.HttpsURLConnection;

import fr.nuxos.minecraft.NuxLauncher.NuxLauncher;
import fr.nuxos.minecraft.NuxLauncher.exceptions.*;

public class MinecraftLogin {

	// Logged infos
	private static String latestVersion;
	private static String downloadTicket;
	private static String sessionId;
	private static String pseudo;

	// State
	private static Boolean isLogged;

	// Statement
	private NuxLauncher launcher;

	public MinecraftLogin(NuxLauncher NL) {
		this.launcher = NL;
	}

	public boolean login(String username, String password) {
		try {

			String parameters = "user=" + URLEncoder.encode(username, "UTF-8")
					+ "&password=" + URLEncoder.encode(password, "UTF-8")
					+ "&version=" + launcher.getMinecraftLauncherVersion();
			String result = executePostSSL(
					"https://login.minecraft.net/", parameters, "minecraft");

			if (result == null) {
				throw new MCNetworkException();
			}

			if (!result.contains(":")) {
				if (result.trim().equals("Bad login")) {
					throw new BadLoginException();
				} else if (result.trim().equals("Old version")) {
					throw new OutdatedMCLauncherException();
				} else if (result.trim().contains("User not premium")) {
					throw new MinecraftUserNotPremiumException();
				} else if (result.trim().contains(
						"Account migrated, use e-mail as username.")) {
					throw new AccountMigratedException();
				} else {
					System.err.print("Unknown login result : \"" + result
							+ "\"");
				}

			}
			String[] values = result.split(":");

			pseudo = values[2].trim();
			latestVersion = values[0].trim();
			downloadTicket = values[1].trim();
			sessionId = values[3].trim();

			isLogged = true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static String executePostSSL(String targetURL, String urlParameters,
			String CertifName) {

		HttpsURLConnection connection = null;

		try {
			URL url = new URL(targetURL);

			connection = (HttpsURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("Content-Length",
					Integer.toString(urlParameters.getBytes().length));
			// connection.setRequestProperty("Content-Language", "en-US");
			// useless
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.connect();

			java.security.cert.Certificate[] certs = connection
					.getServerCertificates();

			byte[] bytes = new byte[294];
			DataInputStream minecraftCert = new DataInputStream(
					MinecraftLogin.class.getResourceAsStream(CertifName + ".key"));
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

			DataOutputStream wr = new DataOutputStream(
					connection.getOutputStream());
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

	// GetSet
	public boolean isLogged() {
		return isLogged;
	}

	public String getLatestVersion() {
		return latestVersion;
	}

	public String getDownloadTicket() {
		return downloadTicket;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getPseudo() {
		return pseudo;
	}

}
