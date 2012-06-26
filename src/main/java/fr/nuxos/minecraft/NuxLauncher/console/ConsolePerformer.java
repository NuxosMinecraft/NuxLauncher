package fr.nuxos.minecraft.NuxLauncher.console;

import java.io.File;
import java.io.IOException;

import fr.nuxos.minecraft.NuxLauncher.NuxLauncher;
import fr.nuxos.minecraft.NuxLauncher.Performer;
import fr.nuxos.minecraft.NuxLauncher.utils.Downloader;
import fr.nuxos.minecraft.NuxLauncher.utils.MinecraftLogin;
import fr.nuxos.minecraft.NuxLauncher.utils.Updater;
import fr.nuxos.minecraft.NuxLauncher.utils.Utils;
import fr.nuxos.minecraft.NuxLauncher.yml.YAMLFormat;
import fr.nuxos.minecraft.NuxLauncher.yml.YAMLProcessor;

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
		try {
			Downloader.download("http://launcher.nuxos-minecraft.fr/repo.yml",
					Utils.getWorkingDir().toString() + "/repo.yml");

			File repoFile = new File(Utils.getWorkingDir(), "repo.yml");
			YAMLProcessor repo = new YAMLProcessor(repoFile, false,
					YAMLFormat.EXTENDED);
			repo.load();
			Updater.processFiles(repo.getNodes("repository.highest"));
			Updater.processFiles(repo.getNodes("repository.high"));
			Updater.processFiles(repo.getNodes("repository.normal"));
			Updater.processFiles(repo.getNodes("repository.optional"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doLaunchMinecraft() {
		// not yet implemented : launches minecraft client
	}
}
