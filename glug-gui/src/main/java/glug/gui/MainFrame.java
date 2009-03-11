/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GlugMainJFrame.java
 *
 * Created on 26-Feb-2009, 03:18:40
 */

package glug.gui;

import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.SwingUtilities.invokeLater;
import gchisto.gctrace.GCTrace;
import gchisto.gctracegenerator.NopGCTraceGeneratorListener;
import gchisto.gctracegenerator.file.FileGCTrace;
import gchisto.gctracegenerator.file.hotspot.GCLogFileReader;
import glug.gui.model.LogarithmicBoundedRange;
import glug.model.ThreadedSystem;

import java.io.File;
import java.util.List;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;

/**
 * Mostly auto-generated by... NETBEANS! Arrr!
 */
public class MainFrame extends javax.swing.JFrame {
	final ThreadedSystem threadedSystem = new ThreadedSystem();
	UITimeScale uiTimeScale = new UITimeScale();

	final ThreadedSystemViewComponent threadedSystemViewPanel;

	/** Creates new form GlugMainJFrame */
	@SuppressWarnings("serial")
	public MainFrame() {
		initComponents();
		threadedSystemViewPanel = new ThreadedSystemViewComponent(uiTimeScale, threadedSystem, new TimelineCursor());

		// threadedSystemViewPanel.setSize(threadedSystemViewPanel.getPreferredSize());
		timelineScrollPane.getViewport().add(threadedSystemViewPanel);

		uiTimeScale.setFullInterval(new Interval(new Instant(), new Duration(1000000)));
		timelineDateTimeComponent = new TimelineDateTimeComponent(uiTimeScale);
		timelineScrollPane.setColumnHeaderView(timelineDateTimeComponent);

		timelineScrollPane.validate();

		logarithmicBoundedRange = new LogarithmicBoundedRange(timeMagnificationSlider.getModel());
		zoomFactorSlideUpdater = new ZoomFactorSlideUpdater(uiTimeScale, logarithmicBoundedRange, timelineScrollPane
				.getViewport());

		uiTimeScale.setMillisecondsPerPixel(1000);

		setTransferHandler(new FileImportDragAndDropTransferHandler() {
			@Override
			public void load(List<File> files) {
				for (File file : files) {
					loadFile(file);
				}
			}
		});
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		jFileChooser1 = new javax.swing.JFileChooser();
		timeMagnificationSlider = new javax.swing.JSlider();
		timelineScrollPane = new javax.swing.JScrollPane();
		menuBar = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		openFileMenuItem = new javax.swing.JMenuItem();
		exitMenuItem = new javax.swing.JMenuItem();
		helpMenu = new javax.swing.JMenu();
		aboutBoxMenuItem = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Glug");

		timeMagnificationSlider.setPaintTicks(true);
		timeMagnificationSlider.addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				timeMagnificationSliderStateChanged(evt);
			}
		});

		fileMenu.setMnemonic('F');
		fileMenu.setText("File");

		openFileMenuItem.setMnemonic('O');
		openFileMenuItem.setText("Open File...");
		openFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openFileMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(openFileMenuItem);

		exitMenuItem.setMnemonic('X');
		exitMenuItem.setText("Exit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

		helpMenu.setMnemonic('H');
		helpMenu.setText("Help");

		aboutBoxMenuItem.setMnemonic('A');
		aboutBoxMenuItem.setText("About Glug");
		aboutBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aboutBoxMenuItemActionPerformed(evt);
			}
		});
		helpMenu.add(aboutBoxMenuItem);

		menuBar.add(helpMenu);

		setJMenuBar(menuBar);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout
				.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
						javax.swing.GroupLayout.Alignment.TRAILING,
						layout.createSequentialGroup().addContainerGap().addComponent(timeMagnificationSlider,
								javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(timelineScrollPane,
						javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 552,
						Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addComponent(timeMagnificationSlider,
						javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(
						javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(timelineScrollPane,
						javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void timeMagnificationSliderStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_timeMagnificationSliderStateChanged
		uiTimeScale.setMillisecondsPerPixel(logarithmicBoundedRange.getCurrentMillisecondsPerPixel());
	}// GEN-LAST:event_timeMagnificationSliderStateChanged

	private void openFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_openFileMenuItemActionPerformed
		int returnVal = jFileChooser1.showOpenDialog(this);
		if (returnVal == APPROVE_OPTION) {
			loadFile(jFileChooser1.getSelectedFile());
		}
	}// GEN-LAST:event_openFileMenuItemActionPerformed

	private void loadFile(File file) {
		System.out.println("You chose to open this file: " + file.getName());
		if (file.getName().toLowerCase().contains("jvm")) {
			loadGarbageCollectionLogFile(file);
		} else {
			loadJavaProcessLogFile(file);
		}
	}

	private void loadJavaProcessLogFile(File file) {
		System.out.println("Loading Java process log...");
		
		new LogLoadingTask(file, threadedSystem, uiTimeScale, zoomFactorSlideUpdater).execute();
	}

	private void loadGarbageCollectionLogFile(File file) {
		System.out.println("Loading Garbage Collection file...");
		new FileGCTrace(file, new GCLogFileReader()).init(new NopGCTraceGeneratorListener() {
			@Override
			public void finished(final GCTrace gcTrace) {
				System.out.println("Finished loading Garbage Collection file");
				invokeLater(new Runnable() {
					@Override
					public void run() {
						timelineScrollPane.getViewport().add(new GCTraceView(gcTrace, uiTimeScale, threadedSystem));
					}
				});
			}
		});
	}

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_exitMenuItemActionPerformed
		System.exit(0);
	}// GEN-LAST:event_exitMenuItemActionPerformed

	private void aboutBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_aboutBoxMenuItemActionPerformed
		new AboutBox().setVisible(true);
	}// GEN-LAST:event_aboutBoxMenuItemActionPerformed

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JMenuItem aboutBoxMenuItem;
	private javax.swing.JMenuItem exitMenuItem;
	private javax.swing.JMenu fileMenu;
	private javax.swing.JMenu helpMenu;
	private javax.swing.JFileChooser jFileChooser1;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenuItem openFileMenuItem;
	private javax.swing.JSlider timeMagnificationSlider;
	private javax.swing.JScrollPane timelineScrollPane;
	// End of variables declaration//GEN-END:variables

	private LogarithmicBoundedRange logarithmicBoundedRange;

	private ZoomFactorSlideUpdater zoomFactorSlideUpdater;
	private TimelineDateTimeComponent timelineDateTimeComponent;

}
