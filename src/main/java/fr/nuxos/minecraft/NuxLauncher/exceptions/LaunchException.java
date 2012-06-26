package fr.nuxos.minecraft.NuxLauncher.exceptions;

public class LaunchException extends Exception {

    private static final long serialVersionUID = 3880112062413252523L;

    public LaunchException() {
        super();
    }

    public LaunchException(String message, Throwable cause) {
        super(message, cause);
    }

    public LaunchException(String message) {
        super(message);
    }

    public LaunchException(Throwable cause) {
        super(cause);
    }

}