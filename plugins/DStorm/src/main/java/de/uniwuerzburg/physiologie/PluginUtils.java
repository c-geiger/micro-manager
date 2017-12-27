package de.uniwuerzburg.physiologie;

import java.awt.Toolkit;

import javax.swing.JOptionPane;

public class PluginUtils {

	private DstormPluginGui gui;
	private boolean running=false;
	
	
	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

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
		for (i=0; i<15;i++){	
		waitDialog.setWaitTimeLabel(15-i,string);
		Thread.sleep(1000);
		}
		waitDialog.setVisible(false);
		waitDialog.dispose();
		waitDialog = null;
	}
public void wait(int ms){
	try {
		Thread.sleep(ms);
	} catch (InterruptedException e) {
		System.out.println("error in wait");
		e.printStackTrace();
	}
}
}