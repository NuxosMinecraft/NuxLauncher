package fr.nuxos.minecraft.NuxLauncher.gui;

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
	MinecraftLogin logger;

	public GuiPerformer(NuxLauncher MainLauncher) {
		// Loading main classes
		launcher = MainLauncher;
		mainWindow = new MainFrame(this);
		logger = new MinecraftLogin(launcher);
	}

	public void doLogin() {

		// need to be threaded to be efficient ...
		mainWindow.setStatus("Logging in ... ");
		mainWindow.setProgression(0);
		mainWindow.setProgressBarView(true);
		mainWindow.setButtonEnabled(false);

		if (logger.login(mainWindow.getUsername(), mainWindow.getPassword())) {
			mainWindow.setProgression(100);
			mainWindow.setStatus("Bienvenue, " + logger.getPseudo() + "."); //TODO: can't see. Add button update or remove that
			mainWindow.setLogged(true);
			// system.sleep(2000);
			doUpdate();
		} else {
			mainWindow.setStatus("Erreur lors de la connexion, merci de réessayer."); //TODO: reask login
			mainWindow.setProgression(0);
		}
	}

	public void doUpdate() {
		mainWindow.setButtonText("Mise à jour ...");
		mainWindow.setButtonEnabled(false);
		Thread t = new Thread(new Updater(this)); //TODO: handle download errors
		t.start();
	}

	public void doLaunchMinecraft() {
		GameLauncher.main(logger); //TODO: close window when minecraft is launched
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
}
