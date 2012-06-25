package fr.nuxos.minecraft.NuxLauncher.exceptions;

public class InvalidLauncherModeException extends Exception {

	private static final long serialVersionUID = 16L;
	private final Throwable cause;
	private final String message;

	public InvalidLauncherModeException(String message) {
		this(null, message);
	}

	public InvalidLauncherModeException(Throwable throwable, String message) {
		this.cause = null;
		this.message = message;
	}

	public InvalidLauncherModeException() {
		this(null, "Launcher mode must be console or gui.");
	}

	public Throwable getCause() {
		return this.cause;
	}

	public String getMessage() {
		return this.message;
	}
	
}
