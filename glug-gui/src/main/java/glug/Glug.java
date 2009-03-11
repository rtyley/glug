package glug;

import static java.awt.EventQueue.invokeLater;
import glug.gui.MainFrame;

import javax.swing.UIManager;

public class Glug {

	public static void main(String args[]) {
//		new FileGCTrace(file, new GCLogFileReader()).getAllGCActivities().
		
		UIManager.put("Slider.paintValue", Boolean.FALSE);
		invokeLater(new Runnable() {
			public void run() {
				new MainFrame().setVisible(true);
			}
		});
	}
}
