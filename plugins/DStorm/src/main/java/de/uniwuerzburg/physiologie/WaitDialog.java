package de.uniwuerzburg.physiologie;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Window.Type;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class WaitDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLabel lblPleaseWait;
	private int seconds =10;
	/**
	 * Create the dialog.
	 */
	public  WaitDialog(String string) {
		setResizable(false);
		setModal(false);
		setUndecorated(true);
		setType(Type.POPUP);
		setBounds(100, 100, 250, 52);
		getContentPane().setLayout(new BorderLayout());
		FlowLayout fl_contentPanel = new FlowLayout();
		contentPanel.setLayout(fl_contentPanel);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			lblPleaseWait = new JLabel("<html>Please wait...."+ seconds +"<br>"+ string + " is prepared </html>");
			lblPleaseWait.setIcon(new ImageIcon(WaitDialog.class.getResource("/de/uniwuerzburg/physiologie/resources/ajax-loader.gif")));
			contentPanel.add(lblPleaseWait);
		}
	}
	public void setWaitTimeLabel(int time, String string){
		lblPleaseWait.setText("<html>Please wait...."+ time +"<br>"+ string + " is prepared </html>");
	}
}
