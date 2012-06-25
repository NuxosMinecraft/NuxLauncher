package fr.nuxos.minecraft.NuxLauncher.console;

import fr.nuxos.minecraft.NuxLauncher.NuxLauncher;
import fr.nuxos.minecraft.NuxLauncher.minecraft.MinecraftLogin;

public class Performer {
	
	// Main
	ConsoleRender MainConsole;
	NuxLauncher _NL;
	// Minecraft
	MinecraftLogin MC_Login;
	
	public Performer(NuxLauncher MainLauncher) {
		// Loading main classes
		MainConsole = new ConsoleRender();
		_NL = MainLauncher;
		MC_Login = new MinecraftLogin(_NL);
		
		if(init()) {
			doLogin();
		}
	}
	
	private boolean init() {
		// init code		
		
		return true;
	}
	
	private void doLogin() {
		MainConsole.Log("Please login ( minecraft.net, SSL secured ) :");
		MainConsole.Log("Username :");
		String username = MainConsole.GetInput();
		MainConsole.Log("Password :");
		String password = MainConsole.GetInput();
		//MainConsole.Log(username + ":" + password);
		if(MC_Login.login(username, password)){
			MainConsole.Log("Successfully logged in player " + MC_Login.getPseudo());
			MainConsole.Log(" - Session ID : " + MC_Login.getSessionId());
			MainConsole.Log(" - Download ticket : " + MC_Login.getDownloadTicket());
			MainConsole.Log(" - Latest version : " + MC_Login.getLatestVersion());
	    } else {
	    	MainConsole.Log("Error, please look logs.");
	    }
	}
	
	private void doUpdate() {
		// not yet implemented : update game files from a repo
	}
	
	private void doLaunchMinecraft() {
		// not yet implemented : launches minecraft client
	}
}
