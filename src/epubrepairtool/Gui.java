/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epubrepairtool;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Marco
 */
public class Gui extends javax.swing.JFrame {

    private File parentDirectory=null;
    
    private DefaultListModel epubListModel=new DefaultListModel();
    private ArrayList<Epub> epubList=new ArrayList<Epub>();
    
    private EpubTableModel epubTableModel=new EpubTableModel();
    
    /**
     * Creates new form Gui
     */
    public Gui() {
        initComponents();
        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screenSize.width/2-this.getSize().width/2, screenSize.height/2-this.getSize().height/2);
        
        fileList.addListSelectionListener(new ListSelectionListener (){
            @Override
            public void valueChanged(ListSelectionEvent e) {
                refreshEpubModelTable();
            }
        });
    }
    
    private void scanDirectory(File selectedDirectory){
        epubListModel.clear();
        File[] files=selectedDirectory.listFiles(new FileFilter(){
            public boolean accept(File file){
                if(file.isFile() &&
                        (file.getName().toLowerCase().endsWith(".epub") ||
                         file.getName().toLowerCase().endsWith(".zip") ) ){
                    return true;
                }else{
                    return false;
                }
            }
        });
        for(int k=0;k<files.length;k++){
            Epub epub=new Epub(files[k]);
            try{
                epub.analyze();
                if(epub.mustToBeFixed()){
                    epubList.add(epub);
                    epubListModel.addElement(files[k].getName());
                }
            }catch(IOException ioe){
                JOptionPane.showMessageDialog(null, "Error while analyzing the epub "+epub.getFile().getName(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }
    
    private void refreshEpubModelTable(){
        int sel=fileList.getSelectedIndex();
        if(sel==-1){
            epubTableModel.unsetData();
        }else{
            epubTableModel.setData(epubList.get(sel), filenamesTable);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        browseButton = new javax.swing.JButton();
        directoryTextField = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        filenamesTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        fileList = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ePub repair tool");

        browseButton.setText("Browse...");
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        directoryTextField.setEditable(false);

        filenamesTable.setModel(epubTableModel);
        jScrollPane2.setViewportView(filenamesTable);

        jLabel2.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        jLabel2.setText("Invalid entries");

        fileList.setModel(epubListModel);
        jScrollPane1.setViewportView(fileList);

        jLabel1.setFont(new java.awt.Font("Dialog", 3, 12)); // NOI18N
        jLabel1.setText("List of ePub with invalid entries");

        jButton2.setText("Fix files");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 457, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(browseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(directoryTextField))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(browseButton)
                    .addComponent(directoryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser chooser;
        
        if(parentDirectory!=null){
            chooser=new JFileChooser(parentDirectory);
        }else{
            chooser=new JFileChooser();
        }
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File selectedDirectory = chooser.getSelectedFile();
            parentDirectory=selectedDirectory.getParentFile();
            directoryTextField.setText(selectedDirectory.getAbsolutePath());
            scanDirectory(selectedDirectory);
        }
    }//GEN-LAST:event_browseButtonActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        
        for(int k=0;k<epubList.size();k++){
            Epub epub=epubList.get(k);
            try{
                epub.fix();
            }catch(IOException ioe){
                JOptionPane.showMessageDialog(null, "Error while fixing the epub "+epub.getFile().getName(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        String nativeLF= UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(nativeLF);
        }   catch (InstantiationException e) {
            System.out.println("err1"+nativeLF);
        } catch (ClassNotFoundException e) {
            System.out.println("err1"+nativeLF);
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("err1"+nativeLF);
        } catch (IllegalAccessException e) {
            System.out.println("err1"+nativeLF);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Gui().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JTextField directoryTextField;
    private javax.swing.JList<String> fileList;
    private javax.swing.JTable filenamesTable;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}