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

import glug.groovy.ParserDefLoader;
import glug.gui.model.LogarithmicBoundedRange;
import glug.gui.timebar.TimelineDateTimeComponent;
import glug.gui.timelinecursor.TimelineCursor;
import glug.gui.zoom.TimelineViewport;
import glug.gui.zoom.ViewPreservingZoomer;
import glug.gui.zoom.ZoomFactorSlideUpdater;
import glug.gui.zoom.ZoomFocusFinder;
import glug.model.ThreadedSystem;
import glug.model.time.LogInterval;
import glug.parser.LogLoader;
import glug.parser.LogLoaderFactory;
import glug.parser.logmessages.LogMessageParserRegistry;
import groovy.lang.GroovyCodeSource;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.format.PeriodFormat;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import static glug.parser.logmessages.CompletedDatabaseQueryParser.DATABASE_QUERY;
import static glug.parser.logmessages.CompletedPageRequestParser.PAGE_REQUEST;
import static javax.swing.JFileChooser.APPROVE_OPTION;

/**
 * Mostly auto-generated by... NETBEANS! Arrr!
 */
public class MainFrame extends javax.swing.JFrame {

    private static final long serialVersionUID = 1L;

    final ThreadedSystem threadedSystem = new ThreadedSystem();
    UITimeScale uiTimeScale = new UITimeScale();

    final ThreadedSystemViewComponent threadedSystemViewPanel;

    private ViewPreservingZoomer viewPreservingZoomer;

    /**
     * Creates new form GlugMainJFrame
     */
    @SuppressWarnings("serial")
    public MainFrame() {
        initComponents();
        KeystrokeSupplementor keystrokeSupplementor = new KeystrokeSupplementor();
        keystrokeSupplementor.hackCtrlPlus(zoomInMenuItem);
        keystrokeSupplementor.hackCtrlMinus(zoomOutMenuItem);

        timelineCursor = new TimelineCursor();
        threadScale = new UIThreadScale();

        threadedSystemViewPanel = new ThreadedSystemViewComponent(uiTimeScale, threadScale, threadedSystem, timelineCursor);

        timelineCursor.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                LogInterval selectedInterval = timelineCursor.getSelectedInterval();
                String text = null;
                if (selectedInterval != null) {
                    text = selectedInterval.toJodaInterval().toPeriod().toString(PeriodFormat.getDefault());
                    Map<Object, Integer> occurences = threadedSystem.countOccurencesDuring(selectedInterval, "DB Query", "Page Request");
                    if (occurences.containsKey(DATABASE_QUERY) && occurences.containsKey(PAGE_REQUEST)) {
                        float dbQueries = occurences.get(DATABASE_QUERY);
                        int pageRequests = occurences.get(PAGE_REQUEST);
                        float ratio = dbQueries / pageRequests;
                        NumberFormat instance = NumberFormat.getInstance();
                        instance.setMinimumFractionDigits(2);
                        instance.setMaximumFractionDigits(2);
                        text = text + " " + instance.format(ratio) + " DB calls/Page req";
                    }
                }
                selectedRegionLabel.setText(text);
            }
        });

        innerPanel = new JPanel();
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        // innerPanel.add(threadedSystemViewPanel);
        // uiTimeScale.addChangeListener(new PropertyChangeListener() {
        // @Override
        // public void propertyChange(PropertyChangeEvent evt) {
        // innerPanel.setSize(uiTimeScale.fullModelToViewLength(),400);
        // }
        // });
        timelineScrollPane.getViewport().add(threadedSystemViewPanel);

        uiTimeScale.setFullInterval(new Interval(new Instant(), new Duration(1000000)));
        timelineDateTimeComponent = new TimelineDateTimeComponent(uiTimeScale);
        timelineDateTimeComponent.setTimeZone(DateTimeZone.forID("Europe/London"));
        timelineScrollPane.setColumnHeaderView(timelineDateTimeComponent);

        timelineScrollPane.validate();

        logarithmicBoundedRange = new LogarithmicBoundedRange(timeMagnificationSlider.getModel());
        timelineViewport = new TimelineViewport(uiTimeScale, timelineScrollPane.getViewport());
        viewPreservingZoomer = new ViewPreservingZoomer(timelineViewport,
                new ZoomFocusFinder(timelineCursor, timelineScrollPane.getViewport(), uiTimeScale), logarithmicBoundedRange);
        zoomFactorSlideUpdater = new ZoomFactorSlideUpdater(uiTimeScale, logarithmicBoundedRange, timelineScrollPane.getViewport());

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
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        timeMagnificationSlider = new javax.swing.JSlider();
        timelineScrollPane = new javax.swing.JScrollPane();
        selectedRegionLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openFileMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        zoomToSelectionMenuItem = new javax.swing.JMenuItem();
        zoomInMenuItem = new javax.swing.JMenuItem();
        zoomOutMenuItem = new javax.swing.JMenuItem();
        fitInWindowMenuItem = new javax.swing.JMenuItem();
        selectVisibleMenuItem = new javax.swing.JMenuItem();
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

        viewMenu.setMnemonic('V');
        viewMenu.setText("View");

        zoomToSelectionMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        zoomToSelectionMenuItem.setMnemonic('Z');
        zoomToSelectionMenuItem.setText("Zoom to selection");
        zoomToSelectionMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomToSelectionMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(zoomToSelectionMenuItem);

        zoomInMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_PLUS, java.awt.event.InputEvent.CTRL_MASK));
        zoomInMenuItem.setMnemonic('I');
        zoomInMenuItem.setText("Zoom In");
        zoomInMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomInMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(zoomInMenuItem);

        zoomOutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_MINUS, java.awt.event.InputEvent.CTRL_MASK));
        zoomOutMenuItem.setMnemonic('O');
        zoomOutMenuItem.setText("Zoom Out");
        zoomOutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomOutMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(zoomOutMenuItem);

        fitInWindowMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        fitInWindowMenuItem.setMnemonic('F');
        fitInWindowMenuItem.setText("Fit in Window");
        fitInWindowMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fitInWindowMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(fitInWindowMenuItem);

        selectVisibleMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        selectVisibleMenuItem.setText("Select Visible");
        selectVisibleMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectVisibleMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(selectVisibleMenuItem);

        menuBar.add(viewMenu);

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
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(selectedRegionLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 340, Short.MAX_VALUE)
                                .addComponent(timeMagnificationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(timelineScrollPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(selectedRegionLabel)
                                        .addComponent(timeMagnificationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(timelineScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void fitInWindowMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fitInWindowMenuItemActionPerformed
        fitWindowTo(uiTimeScale.getFullInterval());
    }//GEN-LAST:event_fitInWindowMenuItemActionPerformed

    private void zoomOutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomOutMenuItemActionPerformed
        viewPreservingZoomer.zoomPreservingViewLocation(uiTimeScale.getMillisecondsPerPixel() * 2);
    }//GEN-LAST:event_zoomOutMenuItemActionPerformed

    private void selectVisibleMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectVisibleMenuItemActionPerformed
        Interval visibleInterval = timelineViewport.getVisibleInterval();
        timelineCursor.setSelectedInterval(new LogInterval(visibleInterval));
    }//GEN-LAST:event_selectVisibleMenuItemActionPerformed

    private void zoomToSelectionMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_zoomToSelectionMenuItemActionPerformed
        LogInterval selectedInterval = timelineCursor.getSelectedInterval();
        if (selectedInterval != null) {
            fitWindowTo(selectedInterval.toJodaInterval());
        }
    }// GEN-LAST:event_zoomToSelectionMenuItemActionPerformed

    private void fitWindowTo(Interval interval) {
        uiTimeScale.setMillisecondsPerPixelToFit(interval, timelineScrollPane.getViewport().getExtentSize().width);
        timelineViewport.setViewPosition(interval.getStart().toInstant(), 0);
    }

    private void zoomInMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jMenuItem1ActionPerformed
        viewPreservingZoomer.zoomPreservingViewLocation(uiTimeScale.getMillisecondsPerPixel() / 2);
    }// GEN-LAST:event_jMenuItem1ActionPerformed

    private void timeMagnificationSliderStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_timeMagnificationSliderStateChanged
        viewPreservingZoomer.zoomPreservingViewLocation(logarithmicBoundedRange.getCurrentMillisecondsPerPixel());
    }// GEN-LAST:event_timeMagnificationSliderStateChanged

    private void openFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_openFileMenuItemActionPerformed
        int returnVal = jFileChooser1.showOpenDialog(this);
        if (returnVal == APPROVE_OPTION) {
            loadFile(jFileChooser1.getSelectedFile());
        }
    }// GEN-LAST:event_openFileMenuItemActionPerformed

    private void loadFile(File file) {
        System.out.println("You chose to open this file: " + file.getName());
        loadJavaProcessLogFile(file);
    }

    private void loadJavaProcessLogFile(File file) {
        System.out.println("Loading Java process log...");
        LogLoaderFactory logLoaderFactory = new LogLoaderFactory();
        try {
            InputStream stream = ParserDefLoader.class.getResourceAsStream("DefaultParsers.groovy");
            //System.out.println("Groovy resource at "+resource);
            LogMessageParserRegistry registry = new ParserDefLoader().load(new GroovyCodeSource(new InputStreamReader(stream), "foo", "bar"));

            LogLoader logLoader = logLoaderFactory.createLoaderFor(file, threadedSystem, registry);
            new LogLoadingTask(logLoader, new DataLoadedUIUpdater(threadedSystem, uiTimeScale, threadScale, zoomFactorSlideUpdater), 50000).execute();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
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
    private javax.swing.JMenuItem fitInWindowMenuItem;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openFileMenuItem;
    private javax.swing.JMenuItem selectVisibleMenuItem;
    private javax.swing.JLabel selectedRegionLabel;
    private javax.swing.JSlider timeMagnificationSlider;
    private javax.swing.JScrollPane timelineScrollPane;
    private javax.swing.JMenu viewMenu;
    private javax.swing.JMenuItem zoomInMenuItem;
    private javax.swing.JMenuItem zoomOutMenuItem;
    private javax.swing.JMenuItem zoomToSelectionMenuItem;
    // End of variables declaration//GEN-END:variables

    private LogarithmicBoundedRange logarithmicBoundedRange;

    private ZoomFactorSlideUpdater zoomFactorSlideUpdater;
    private TimelineDateTimeComponent timelineDateTimeComponent;
    private TimelineCursor timelineCursor;
    private JPanel innerPanel;

    private UIThreadScale threadScale;

    private TimelineViewport timelineViewport;

}
