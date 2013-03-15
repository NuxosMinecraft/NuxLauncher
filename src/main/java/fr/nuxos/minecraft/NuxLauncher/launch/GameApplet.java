package fr.nuxos.minecraft.NuxLauncher.launch;

import java.applet.Applet;
import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class GameApplet extends Applet implements AppletStub {
	private static final long serialVersionUID = -956822488563597767L;
	private Map<String, String> parameters;
	private Applet applet;
	private int context = 0;
	private boolean active = false;

	GameApplet(Map<String, String> parameters, Applet applet) {
		this.parameters = parameters;
		this.applet = applet;
		setLayout(new BorderLayout());
	}

	@Override
	public void start() {
		applet.setStub(this);
		applet.setSize(getWidth(), getHeight());
		add(applet, BorderLayout.CENTER);
		applet.init();
		this.active = true;
		applet.start();
		validate();
	}

	@Override
	public boolean isActive() {
		if (this.context == 0) {
			this.context = -1;
			try {
				if (getAppletContext() != null)
					this.context = 1;
			} catch (Exception localException) {
			}
		}
		if (this.context == -1)
			return this.active;
		return super.isActive();
	}

	@Override
	public URL getDocumentBase() {
		try {
			return new URL("http://www.minecraft.net/game/");
		} catch (MalformedURLException e) {
			return null;
		}
	}

	@Override
	public String getParameter(String name) {
		String custom = (String) parameters.get(name);
		if (custom != null)
			return custom;
		try {
			return super.getParameter(name);
		} catch (Exception e) {
			parameters.put(name, null);
		}
		return null;
	}

	public void appletResize(int width, int height) {

	}
	
	public void replace(Applet applet) {
        this.applet = applet;
        applet.setStub(this);
        applet.setSize(getWidth(), getHeight());
        
        setLayout(new BorderLayout());
        add(applet, "Center");
        
        applet.init();
        active = true;
        applet.start();
        validate();
    }

}
