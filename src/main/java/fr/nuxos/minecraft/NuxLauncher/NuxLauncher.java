package fr.nuxos.minecraft.NuxLauncher;

import fr.nuxos.minecraft.NuxLauncher.minecraft.MinecraftLogin;
import fr.nuxos.minecraft.NuxLauncher.console.Performer;
import fr.nuxos.minecraft.NuxLauncher.exceptions.InvalidLauncherModeException;
import java.io.Console;

public class NuxLauncher {

	static String NuxLauncherVersion = "indev";
	static Integer MinecraftLauncherVersion = 13;
	
	// temp
	static String Username;
	static String Password;
	static MinecraftLogin MC_Logger;
	// end temp.
	
	private Performer ConsolePerformer;
	
	public NuxLauncher(String Mode) {
		
        try {
          if(Mode.equals("console")) {
        	  
        	  // simple behavior, no bullshit :
        	  // login,  if ok launch minecraft, else retry login.
        	  // nothing more.
        	  
        	  ConsolePerformer = new Performer(this);
        	  
    	  }
    	  else if(Mode.equals("gui")) {
    		  // not yet implemented
    		 
    	  }
    	  else {
    	  throw new InvalidLauncherModeException();
    	  }            
        } catch (Exception e) {
          e.printStackTrace();
          System.err.print(e.toString());
        }		
	}
	
	
    public Integer getMinecraftLauncherVersion() {
        return MinecraftLauncherVersion;
    }
	
	
}
