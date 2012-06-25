package fr.nuxos.minecraft.NuxLauncher.minecraft;

import java.net.URLEncoder;

import fr.nuxos.minecraft.NuxLauncher.NuxLauncher;
import fr.nuxos.minecraft.NuxLauncher.exceptions.*;
import fr.nuxos.minecraft.NuxLauncher.utils.*;

public class MinecraftLogin {

	// Logged infos
    private static String latestVersion;
    private static String downloadTicket;
    private static String sessionId;
    private static String pseudo;
    
    // State
    private static Boolean isLogged;
    
    // Statement
    private NuxLauncher _NuxosLauncher;
    
    public MinecraftLogin(NuxLauncher NL) {
        this._NuxosLauncher = NL;
    }
    
    public boolean login(String username, String password) {
        try {
        	
          String parameters = "user=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8") + "&version=" + _NuxosLauncher.getMinecraftLauncherVersion();
          String result = Network.executePostSSL("https://login.minecraft.net/", parameters, "minecraft");
          
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
			} else if (result.trim().contains("Account migrated, use e-mail as username.")) {
				throw new AccountMigratedException();				
			} else {
            	System.err.print("Unknown login result : \"" + result + "\"");
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
          System.err.print(e.toString());
          return false;
        }
        return true;
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

