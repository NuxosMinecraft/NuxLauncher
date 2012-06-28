package fr.nuxos.minecraft.NuxLauncher.console;

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
	String[] logged_infos;

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
			mainConsole.Log("Successfully logged in player " + logged_infos[2]);
			mainConsole.Log(" - Session ID : " + logged_infos[3]);
			mainConsole.Log(" - Download ticket : " + logged_infos[1]);
			mainConsole.Log(" - Latest version : " + logged_infos[0]);
		//} else {
			mainConsole.Log("Error, please look logs.");
		//}
	}

	public void doUpdate() {
		new Updater(this);
	}

	public void doLaunchMinecraft() {
		GameLauncher.main(logged_infos);
	}

	public void changeProgress(String status, int progress) {
	}

	public void downloadsFinished() {
	}

	public void authFinishedSuccess(String[] logged_infos) {
		this.logged_infos = logged_infos;
	}

	public void authFinishedFail(String reason) {
		// TODO Auto-generated method stub
		
	}
}
