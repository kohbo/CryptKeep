/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptkeep;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 *
 * @author kohbo
 */
public class PeerToPeer extends javax.swing.JFrame {
    private final int SOCKET_PORT = 5000;

    /**
     * Creates new form PeerToPeer
     * @param selectedFile
     */
    public PeerToPeer() {
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSend = new javax.swing.JButton();
        btnReceiveFile = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtStatus = new javax.swing.JTextPane();
        jLabel1 = new javax.swing.JLabel();
        chkEncrypt = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnSend.setText("Send File");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        btnReceiveFile.setText("Receive File");
        btnReceiveFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReceiveFileActionPerformed(evt);
            }
        });

        txtStatus.setBackground(new java.awt.Color(0, 0, 0));
        txtStatus.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(txtStatus);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Status");

        chkEncrypt.setText("Encrypt Before Sending");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnReceiveFile, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                    .addComponent(btnSend, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkEncrypt)
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnSend)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReceiveFile))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(chkEncrypt)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        addToLog("Starting file sending procedure...");
        try {
            sendFile();
        } catch (IOException e) {
            addToLog("Exception Occured: " + e.getMessage());
        }
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnReceiveFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReceiveFileActionPerformed
        addToLog("Starting file receiving procedure...");
        try {
            ReceiveFile();
        } catch (IOException e) {
            addToLog("Exception Occured: " + e.getMessage());
        }
    }//GEN-LAST:event_btnReceiveFileActionPerformed

    private void addToLog(String text){
        System.out.println(text);
        txtStatus.setText(txtStatus.getText() + text + "\n");
    }
    
    protected void sendFile() throws IOException{
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        OutputStream os = null;
        ServerSocket servsock = null;
        Socket sock = null;
        try {
            JFileChooser jfc = new JFileChooser();
            File selectedFile = null;
                    
            if(jfc.showDialog(btnSend, null) == JFileChooser.APPROVE_OPTION){
                selectedFile = jfc.getSelectedFile();
            } else {
                throw new IOException("A file must be chosen to send.");
            }
            
            servsock = new ServerSocket(SOCKET_PORT);
            if(servsock.isBound()){
                addToLog("Socked successfully bound to port.");
            }
            while (true) {
                addToLog("Waiting for connection...");
                try {
                  sock = servsock.accept();
                  addToLog("Connection establish with " + sock.getInetAddress());
                  // send file
                  byte [] mybytearray  = new byte [(int)selectedFile.length()];
                  fis = new FileInputStream(selectedFile);
                  bis = new BufferedInputStream(fis);
                  bis.read(mybytearray,0,mybytearray.length);
                  os = sock.getOutputStream();
                  addToLog("Sending " + selectedFile.getName() + "(" + mybytearray.length + " bytes)");
                  os.write(mybytearray,0,mybytearray.length);
                  os.flush();
                  addToLog("Done.");
                }
                catch(Exception e){
                    addToLog("Exception Occured: " + e.getMessage());
                }
                finally {
                  if (bis != null) bis.close();
                  if (os != null) os.close();
                  if (sock!=null) sock.close();
                }
            }
        }
        catch(IOException e){
            addToLog("Exception Occured: " + e.getMessage());
        }
        finally{
            if (servsock != null) servsock.close();
        }
    }
    
    private void ReceiveFile() throws IOException{
        String address = JOptionPane.showInputDialog("Enter server IP address");
        
        int bytesRead;
        int current = 0;
        JFileChooser jfc = new JFileChooser();
        File fileDestination;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        Socket sock = null;
        try {
            sock = new Socket(address, SOCKET_PORT);
            addToLog("Establishing connection to " + address);

            // receive file
            byte [] mybytearray  = new byte [60000000]; //up to 60 MB file
            InputStream is = sock.getInputStream();
            if(jfc.showSaveDialog(btnSend) == JFileChooser.APPROVE_OPTION){
                fileDestination = jfc.getSelectedFile();
            } else {
                throw new IOException("File destination must be chosen.");
            }
            fos = new FileOutputStream(fileDestination);
            bos = new BufferedOutputStream(fos);
            bytesRead = is.read(mybytearray,0,mybytearray.length);
            current = bytesRead;

            do {
               bytesRead =
                  is.read(mybytearray, current, (mybytearray.length-current));
               if(bytesRead >= 0) current += bytesRead;
            } while(bytesRead > -1);

            bos.write(mybytearray, 0 , current);
            bos.flush();
            System.out.println("File " + fileDestination.getName()
                + " downloaded (" + current + " bytes read)");
        }
        catch(Exception e){
            addToLog("Exception Occured: " + e.getMessage());
        }
        finally {
          if (fos != null) fos.close();
          if (bos != null) bos.close();
          if (sock != null) sock.close();
        }
    }
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(PeerToPeer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(PeerToPeer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(PeerToPeer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(PeerToPeer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new PeerToPeer().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnReceiveFile;
    private javax.swing.JButton btnSend;
    private javax.swing.JCheckBox chkEncrypt;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane txtStatus;
    // End of variables declaration//GEN-END:variables
}
