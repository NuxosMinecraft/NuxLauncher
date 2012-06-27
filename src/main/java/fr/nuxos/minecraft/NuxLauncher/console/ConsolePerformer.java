package fr.nuxos.minecraft.NuxLauncher.console;

import java.io.IOException;

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
	MinecraftLogin logger;

	public ConsolePerformer(NuxLauncher MainLauncher) {
		// Loading main classes
		mainConsole = new ConsoleRender();
		launcher = MainLauncher;
		logger = new MinecraftLogin(launcher);
	}

	public void doLogin() {
		mainConsole.Log("Please login ( minecraft.net, SSL secured ) :");
		mainConsole.Log("Username :");
		String username = mainConsole.GetInput();
		mainConsole.Log("Password :");
		String password = mainConsole.GetInput();
		// MainConsole.Log(username + ":" + password);
		if (logger.login(username, password)) {
			mainConsole.Log("Successfully logged in player " + logger.getPseudo());
			mainConsole.Log(" - Session ID : " + logger.getSessionId());
			mainConsole.Log(" - Download ticket : " + logger.getDownloadTicket());
			mainConsole.Log(" - Latest version : " + logger.getLatestVersion());
		} else {
			mainConsole.Log("Error, please look logs.");
		}
	}

	public void doUpdate() {
		try {
			new Updater();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doLaunchMinecraft() {
		GameLauncher.main(logger);
	}
}
