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
                  new Updater();
                  mainWindow.setButtonText("Jouer");
		  mainWindow.setStatus("Jeu à jour, prêt à lancer.");
	          mainWindow.setButtonEnabled(true);
                  mainWindow.setProgression(100);
                } catch (IOException e) {
                  e.printStackTrace();
                }
	}

	public void doLaunchMinecraft() {
		GameLauncher.main(logger);
	}

}
