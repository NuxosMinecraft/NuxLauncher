package fr.nuxos.minecraft.NuxLauncher;

import java.util.Hashtable;

public interface Performer {
	public void doLogin();

	public void doUpdate();

	public void doLaunchMinecraft();

	public void changeProgress(String status, int progress);

	public void downloadsFinished();
	
	public void authFinishedSuccess(Hashtable<String, String> loggingInfo);
	
	public void authFinishedFail(String reason);
}
