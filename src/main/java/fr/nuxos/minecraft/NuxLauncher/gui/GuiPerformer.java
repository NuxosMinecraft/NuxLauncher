package fr.nuxos.minecraft.NuxLauncher.gui;

import java.util.Hashtable;

import javax.swing.SwingUtilities;

import fr.nuxos.minecraft.NuxLauncher.NuxLauncher;
import fr.nuxos.minecraft.NuxLauncher.Performer;
import fr.nuxos.minecraft.NuxLauncher.launch.GameLauncher;
import fr.nuxos.minecraft.NuxLauncher.utils.MinecraftLogin;
import fr.nuxos.minecraft.NuxLauncher.utils.Updater;

public class GuiPerformer implements Performer {

	// Main
	MainFrame mainWindow;
	NuxLauncher launcher;
	// Minecraft
    Hashtable<String, String> loggingInfo;

	public GuiPerformer(NuxLauncher MainLauncher) {
		// Loading main classes
		launcher = MainLauncher;
		mainWindow = new MainFrame(this);
		//logger = new MinecraftLogin(launcher, this);
	}

	public void doLogin() {
		mainWindow.setProgressBarView(true);
		mainWindow.setButtonEnabled(false);
		Thread t = new Thread(new MinecraftLogin(launcher, this, mainWindow.getUsername(), mainWindow.getPassword())); //TODO: handle download errors
		t.start();
	}

	public void doUpdate() {
		mainWindow.setButtonText("Mise à jour ...");
		mainWindow.setButtonEnabled(false);
		Thread t = new Thread(new Updater(this)); //TODO: handle download errors
		t.start();
	}

	public void doLaunchMinecraft() {
		mainWindow.dispose();
		GameLauncher.main(loggingInfo);
	}

	public void changeProgress(final String status, final int progress) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				mainWindow.setStatus(status);
				mainWindow.setProgression(progress);
			}
		});
	}
	
	public void downloadsFinished() {
		mainWindow.setButtonText("Jouer");
		mainWindow.setStatus("Jeu à jour, prêt à lancer.");
		mainWindow.setButtonEnabled(true);
		mainWindow.setProgression(100);
	}
	
	public void authFinishedSuccess(Hashtable<String, String> loggingInfo) {
		mainWindow.writeRemember();
		this.loggingInfo = loggingInfo;
		mainWindow.setStatus("Bienvenue, " + loggingInfo.get("username") + "."); //TODO: can't see. Add button update or remove that
		mainWindow.setLogged(true);
		doUpdate();
	}
	
	public void authFinishedFail(String reason) {
		mainWindow.setButtonEnabled(true);
		mainWindow.setStatus("Erreur lors de la connexion : " + reason + ". Réessayez.");
		mainWindow.setProgression(0);
	}
}
