package de.uniwuerzburg.physiologie;

import java.awt.Toolkit;

import javax.swing.JOptionPane;

public class PluginUtils {

	private DstormPluginGui gui;
	public PluginUtils(DstormPluginGui gui){
		this.gui=gui;
}

	public void errorDialog(String message){
		final Runnable runnable =
			     (Runnable) Toolkit.getDefaultToolkit().getDesktopProperty("win.sound.exclamation");
			if (runnable != null) runnable.run();
		
			JOptionPane.showMessageDialog(gui, message , "Dialog",JOptionPane.ERROR_MESSAGE);
	}
	
	
	
	public void waitTenSeconds(String string) throws InterruptedException {
		WaitDialog waitDialog = new WaitDialog(string);
		waitDialog.setLocation((int)gui.getLocationOnScreen().getX()+600,(int)gui.getLocationOnScreen().getY()+400);
		waitDialog.setVisible(true);
		int i=0;
		for (i=0; i<10;i++){	
		waitDialog.setWaitTimeLabel(10-i,string);
		Thread.sleep(1000);
		}
		waitDialog.setVisible(false);
		waitDialog.dispose();
		waitDialog = null;
	}

}