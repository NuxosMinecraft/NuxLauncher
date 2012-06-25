package fr.nuxos.minecraft.NuxLauncher.console;

import java.io.Console;

public class ConsoleRender {
	
	private Console MainConsole;
	
    public ConsoleRender() { 
    	MainConsole = System.console();
    	MainConsole.printf("%s%n", "NuxLoader - Console mode");

    }
    
    public void Log(String message) {
    	MainConsole.printf("%s%n", message);
    }
    
    public String GetInput() {
    	return MainConsole.readLine().toString();
    }
    
    public String GetSecretInput() {
    	return MainConsole.readPassword().toString();
    }
}
