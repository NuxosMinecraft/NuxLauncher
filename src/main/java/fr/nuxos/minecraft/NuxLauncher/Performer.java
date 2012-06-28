package fr.nuxos.minecraft.NuxLauncher;

public interface Performer {
	public void doLogin();

	public void doUpdate();

	public void doLaunchMinecraft();

	public void changeProgress(String status, int progress);

	public void downloadsFinished();
	
	public void authFinishedSuccess(String[] logged_infos);
	
	public void authFinishedFail(String reason);
}
