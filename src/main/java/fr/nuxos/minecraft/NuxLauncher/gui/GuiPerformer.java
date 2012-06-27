package fr.nuxos.minecraft.NuxLauncher.gui;

import java.io.File;
import java.io.IOException;

import fr.nuxos.minecraft.NuxLauncher.NuxLauncher;
import fr.nuxos.minecraft.NuxLauncher.Performer;
import fr.nuxos.minecraft.NuxLauncher.console.ConsoleRender;
import fr.nuxos.minecraft.NuxLauncher.launch.GameLauncher;
import fr.nuxos.minecraft.NuxLauncher.utils.Downloader;
import fr.nuxos.minecraft.NuxLauncher.utils.MinecraftLogin;
import fr.nuxos.minecraft.NuxLauncher.utils.Updater;
import fr.nuxos.minecraft.NuxLauncher.utils.Utils;
import fr.nuxos.minecraft.NuxLauncher.yml.YAMLFormat;
import fr.nuxos.minecraft.NuxLauncher.yml.YAMLProcessor;

public class GuiPerformer implements Performer {
	
	// Main
	MainFrame mainWindow;
	NuxLauncher launcher;
	// Minecraft
	MinecraftLogin logger;

	public GuiPerformer(NuxLauncher MainLauncher) {
		// Loading main classes
		launcher = MainLauncher;
		mainWindow = new MainFrame(launcher, this);
		logger = new MinecraftLogin(launcher);
	}

	public void doLogin() {
		
		// need to be threaded to be efficient ...
		mainWindow.setStatus("Logging in ... ");
		mainWindow.setProgression(0);
		mainWindow.setProgressBarView(true);
		mainWindow.setButtonEnabled(false);
		
		if(logger.login(mainWindow.getUsername(), mainWindow.getPassword())) {
			mainWindow.setProgression(100);
			mainWindow.setStatus("Bienvenue, " + logger.getPseudo() + ".");
			mainWindow.setLogged(true);
			//system.sleep(2000);
			doUpdate();
		}
		else {
			mainWindow.setStatus("Erreur lors de la connexion, merci de réessayer.");
			mainWindow.setProgression(0);
		}
	}

	public void doUpdate() {
		try {
			Downloader.download("http://launcher.nuxos-minecraft.fr/repo.yml", Utils.getWorkingDir().toString() + "/repo.yml");

			File repoFile = new File(Utils.getWorkingDir(), "repo.yml");
			YAMLProcessor repo = new YAMLProcessor(repoFile, false, YAMLFormat.EXTENDED);
			repo.load();

			YAMLProcessor config = new YAMLProcessor(launcher.getConfig(), false, YAMLFormat.EXTENDED);
			config.load();

			if (repo.getInt("repository.version") > config.getInt("repository.version", 0)) {
				mainWindow.setStatus("Mise à jour du jeu en cours ...");
				mainWindow.setProgression(0);
				Updater.processFiles(repo.getNodes("repository.highest"));
				Updater.processFiles(repo.getNodes("repository.high"));
				Updater.processFiles(repo.getNodes("repository.normal"));
				Updater.processFiles(repo.getNodes("repository.optional"));

				config.setProperty("repository.version", repo.getInt("repository.version"));
				config.save();
				mainWindow.setButtonText("Jouer");
				mainWindow.setStatus("Jeu à jour, prêt à lancer.");
				mainWindow.setButtonEnabled(true);
				mainWindow.setProgression(100);
			}
			else {
				mainWindow.setButtonText("Jouer");
				mainWindow.setStatus("Jeu à jour, prêt à lancer.");
				mainWindow.setButtonEnabled(true);
				mainWindow.setProgression(100);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doLaunchMinecraft() {
		GameLauncher.main(logger);
	}

}
