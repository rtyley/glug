package glug;

import static java.awt.EventQueue.invokeLater;
import glug.gui.MainFrame;

import javax.swing.UIManager;

public class Glug {

	public static void main(String args[]) {
		UIManager.put("Slider.paintValue", Boolean.FALSE);
		invokeLater(new Runnable() {
			public void run() {
				new MainFrame().setVisible(true);
			}
		});
	}
}
