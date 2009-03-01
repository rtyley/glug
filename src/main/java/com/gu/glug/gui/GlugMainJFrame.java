/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GlugMainJFrame.java
 *
 * Created on 26-Feb-2009, 03:18:40
 */

package com.gu.glug.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFileChooser;
import javax.swing.TransferHandler;
import javax.swing.UIManager;

import com.gu.glug.ThreadedSystem;

/**
 *
 * @author roberto
 */
public class GlugMainJFrame extends javax.swing.JFrame {
	final ThreadedSystem threadedSystem = new ThreadedSystem();
	
	final ThreadedSystemViewPanel threadedSystemViewPanel;
    /** Creates new form GlugMainJFrame */
    public GlugMainJFrame() {
        initComponents();
        threadedSystemViewPanel = new ThreadedSystemViewPanel(threadedSystem);
        //threadedSystemViewPanel.setSize(threadedSystemViewPanel.getPreferredSize());
        jScrollPane1.getViewport().add(threadedSystemViewPanel);
        jScrollPane1.validate();
        TransferHandler newHandler = new TransferHandler() {
        	@Override
        	public boolean canImport(TransferSupport support) {
        		return true;
        	}
        	
        	@Override
        	public boolean importData(TransferSupport support) {
        		Object transferData =null;
        		try {
        			DataFlavor[] dataFlavors = support.getDataFlavors();
        			for (DataFlavor dataFlavor : dataFlavors) {
        				if (dataFlavor.isFlavorJavaFileListType()) {
        					System.out.println("Hurrah!");
        				}
        				
        			}
					transferData = support.getTransferable().getTransferData(DataFlavor.getTextPlainUnicodeFlavor());
					InputStream is = (InputStream) transferData;
				} catch (UnsupportedFlavorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		return false;
        	}
        };
		setTransferHandler(newHandler);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        timeMagnificationSlider = new javax.swing.JSlider();
        jScrollPane1 = new javax.swing.JScrollPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openFileMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Glug");

        timeMagnificationSlider.setPaintTicks(true);
        timeMagnificationSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                timeMagnificationSliderStateChanged(evt);
            }
        });

        fileMenu.setText("File");

        openFileMenuItem.setText("Open File...");
        openFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(openFileMenuItem);

        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(timeMagnificationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(timeMagnificationSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void timeMagnificationSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_timeMagnificationSliderStateChanged

        threadedSystemViewPanel.setMagnification(Math.pow(2, ((timeMagnificationSlider.getValue()/4f)-80)/16 ));
}//GEN-LAST:event_timeMagnificationSliderStateChanged

    private void openFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileMenuItemActionPerformed
        
        int returnVal = jFileChooser1.showOpenDialog(this);
          if(returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooser1.getSelectedFile();
			System.out.println("You chose to open this file: " +
            		 selectedFile.getName());
            new LogLoadingTask(selectedFile, threadedSystem, threadedSystemViewPanel).execute();
            
          }
}//GEN-LAST:event_openFileMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
    	UIManager.put("Slider.paintValue", Boolean.FALSE);
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GlugMainJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openFileMenuItem;
    private javax.swing.JSlider timeMagnificationSlider;
    // End of variables declaration//GEN-END:variables

}
