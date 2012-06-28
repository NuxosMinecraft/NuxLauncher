package fr.nuxos.minecraft.NuxLauncher.exceptions;

import java.io.File;

public class BadMd5Exception extends Exception {
	private static final long serialVersionUID = -3356110926795571673L;
	private final Throwable cause;
	private final String message;

	public BadMd5Exception(String message) {
		this(null, message);
	}

	public BadMd5Exception(Throwable throwable, String message) {
		this.cause = null;
		this.message = message;
	}

	public BadMd5Exception(File file, String excepted, String got) {
		this(null, "Bad md5 for " + file.getName() + " Excepted " + excepted + ", got " + got);
	}

	public Throwable getCause() {
		return this.cause;
	}

	public String getMessage() {
		return this.message;
	}
}
