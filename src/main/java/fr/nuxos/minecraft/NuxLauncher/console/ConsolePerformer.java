package fr.nuxos.minecraft.NuxLauncher.console;

import fr.nuxos.minecraft.NuxLauncher.NuxLauncher;
import fr.nuxos.minecraft.NuxLauncher.Performer;
import fr.nuxos.minecraft.NuxLauncher.minecraft.MinecraftLogin;

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
			mainConsole.Log("Successfully logged in player "
					+ logger.getPseudo());
			mainConsole.Log(" - Session ID : " + logger.getSessionId());
			mainConsole.Log(" - Download ticket : "
					+ logger.getDownloadTicket());
			mainConsole.Log(" - Latest version : " + logger.getLatestVersion());
		} else {
			mainConsole.Log("Error, please look logs.");
		}
	}

	public void doUpdate() {
		// not yet implemented : update game files from a repo
	}

	public void doLaunchMinecraft() {
		// not yet implemented : launches minecraft client
	}
}
