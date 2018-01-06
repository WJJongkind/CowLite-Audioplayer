/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cap.gui;

import cap.core.audio.AudioController;
import cap.util.IO;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Wessel
 */
public class SavePanel extends DefaultPanel
{
    private final AudioController controller;
    private final Map<String, Object> GRAPHICS;
    /**
     * Creates new form YTImportScreen
     */
    public SavePanel(Map<String, Object> graphics, AudioController controller) {
        initComponents();
        importButton.setBackground((Color) graphics.get("background"));
        importButton.setForeground((Color) graphics.get("listbg"));
        super.setBackground((Color) graphics.get("listbg"));
        this.controller = controller;
        error.setVisible(false);
        this.GRAPHICS = graphics;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        importButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        error = new javax.swing.JLabel();

        importButton.setText("Save");
        importButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Name");

        error.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        error.setForeground(new java.awt.Color(255, 0, 0));
        error.setText("Playlist could not be imported. Please chose a unique name.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(nameField)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(importButton)
                            .addComponent(jLabel1)
                            .addComponent(error))
                        .addGap(0, 12, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(114, Short.MAX_VALUE)
                .addComponent(error)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(importButton)
                .addContainerGap(126, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private Exception ex;
    private void importButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importButtonActionPerformed
        // TODO add your handling code here:
        String name = nameField.getText();

        if(controller.getPlaylists().containsKey(name))
        {
            CountDownLatch latch = new CountDownLatch(1);
            OverwritePanel pane = new OverwritePanel(GRAPHICS, latch);
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try{
                        DefaultWindow window = new DefaultWindow(GRAPHICS);
                        window.setContentPanel(pane);
                        window.setSize(400, 200);
                        window.setVisible(true);
                    }catch(Exception e){
                        latch.countDown();
                        ex = e;
                    }
                }
            }).start();

            JPanel src = this;
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try{
                        latch.await();

                        if(ex != null)
                            throw ex;
                        if(pane.getAccepted())
                        {
                            IO.saveFile(controller, name);
                            exit();
                            return;
                        }
                    }catch(Exception e){
                        error.setVisible(true);
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        else
        {
            try{
                IO.saveFile(controller, name);
                exit();
            }catch(Exception e){
                e.printStackTrace();
                error.setVisible(true);
            }
        }
    }//GEN-LAST:event_importButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel error;
    private javax.swing.JButton importButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField nameField;
    // End of variables declaration//GEN-END:variables
}
