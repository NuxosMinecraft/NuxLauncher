package fr.nuxos.minecraft.NuxLauncher.console;

import java.util.Hashtable;

import fr.nuxos.minecraft.NuxLauncher.NuxLauncher;
import fr.nuxos.minecraft.NuxLauncher.Performer;
import fr.nuxos.minecraft.NuxLauncher.launch.GameLauncher;
import fr.nuxos.minecraft.NuxLauncher.utils.MinecraftLogin;
import fr.nuxos.minecraft.NuxLauncher.utils.Updater;

public class ConsolePerformer implements Performer {

	// Main
	ConsoleRender mainConsole;
	NuxLauncher launcher;
	// Minecraft
	Hashtable<String, String> loggingInfo;

	public ConsolePerformer(NuxLauncher MainLauncher) {
		// Loading main classes
		mainConsole = new ConsoleRender();
		launcher = MainLauncher;
		//logger = new MinecraftLogin(launcher);
	}

	public void doLogin() {
		mainConsole.Log("Please login ( minecraft.net, SSL secured ) :");
		mainConsole.Log("Username :");
		String username = mainConsole.GetInput();
		mainConsole.Log("Password :");
		String password = mainConsole.GetInput();
		// MainConsole.Log(username + ":" + password);
		//if (logger.login(username, password)) {
			mainConsole.Log("Successfully logged in player " + loggingInfo.get("username"));
			mainConsole.Log(" - Session ID : " + loggingInfo.get("sessionid"));
			mainConsole.Log(" - Download ticket : " + loggingInfo.get("downloadticket"));
			mainConsole.Log(" - Latest version : " + loggingInfo.get("latestversion"));
		//} else {
			mainConsole.Log("Error, please look logs.");
		//}
	}

	public void doUpdate() {
		new Updater(this);
	}

	public void doLaunchMinecraft() {
		GameLauncher.main(loggingInfo);
	}

	public void changeProgress(String status, int progress) {
	}

	public void downloadsFinished() {
	}

	public void authFinishedSuccess(Hashtable<String, String> loggingInfo) {
		this.loggingInfo = loggingInfo;
	}

	public void authFinishedFail(String reason) {
		// TODO Auto-generated method stub
		
	}
}
