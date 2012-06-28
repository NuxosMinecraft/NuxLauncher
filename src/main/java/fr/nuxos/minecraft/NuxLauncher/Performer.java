package fr.nuxos.minecraft.NuxLauncher;

public interface Performer {
	public void doLogin();

	public void doUpdate();

	public void doLaunchMinecraft();

	public void changeProgress(String status, int progress);

	public void downloadsFinished();
}
