package fr.nuxos.minecraft.NuxLauncher.console;

import java.io.Console;

public class ConsoleRender {

	private Console mainConsole;

	public ConsoleRender() {
		mainConsole = System.console();
		mainConsole.printf("%s%n", "NuxLoader - Console mode");

	}

	public void Log(String message) {
		mainConsole.printf("%s%n", message);
	}

	public String GetInput() {
		return mainConsole.readLine().toString();
	}

	public String GetSecretInput() {
		return mainConsole.readPassword().toString();
	}
}
