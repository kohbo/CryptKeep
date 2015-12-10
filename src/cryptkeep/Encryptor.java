package cryptkeep;

import java.io.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.swing.JFileChooser;

public class Encryptor extends javax.swing.JFrame {
    private static final int AES_Key_Size = 128;
    Cipher pkCipher, aesCipher;
    byte[] aesKey;
    SecretKeySpec aeskeySpec;

    /**
     * Creates new form Excryptor
     */
    public Encryptor() throws GeneralSecurityException {
        initComponents();
        setVisible(true);
        setLocationRelativeTo(null);
        pack();
        pkCipher = Cipher.getInstance("RSA");
	aesCipher = Cipher.getInstance("AES");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnEncrypt = new javax.swing.JButton();
        btnDecrypt = new javax.swing.JButton();
        txtStatus = new javax.swing.JTextArea();

        setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        btnEncrypt.setText("Encrypt File");
        btnEncrypt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEncryptActionPerformed(evt);
            }
        });
        add(btnEncrypt);

        btnDecrypt.setText("Decrypt File");
        btnDecrypt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDecryptActionPerformed(evt);
            }
        });
        add(btnDecrypt);

        txtStatus.setEditable(false);
        txtStatus.setBackground(new java.awt.Color(0, 0, 0));
        txtStatus.setColumns(20);
        txtStatus.setForeground(new java.awt.Color(255, 255, 255));
        txtStatus.setLineWrap(true);
        txtStatus.setRows(5);
        txtStatus.setMinimumSize(new java.awt.Dimension(100, 100));
        txtStatus.setName("textStatus"); // NOI18N
        txtStatus.setPreferredSize(new java.awt.Dimension(449, 94));
        add(txtStatus);
    }// </editor-fold>//GEN-END:initComponents

    private void btnEncryptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEncryptActionPerformed
        try {
            makeKey();
            JFileChooser jf = new JFileChooser();
            File publicKeyFile = null;
            File encryptedKeyFile = null;
            File sourceFile =  null;
            File targetFile = null;
                    
            if(jf.showDialog(btnEncrypt, "Open Pub Key") == JFileChooser.APPROVE_OPTION){
                publicKeyFile = jf.getSelectedFile();
                encryptedKeyFile = new File(jf.getSelectedFile().getAbsolutePath() + ".enc");
            } else {
                throw new IOException("A public key must be selected.");
            }
            saveKey(encryptedKeyFile, publicKeyFile);
            if(jf.showDialog(btnEncrypt, "Open Source File") == JFileChooser.APPROVE_OPTION){
                sourceFile = jf.getSelectedFile();
                targetFile = new File(jf.getSelectedFile().getAbsolutePath() + ".enc");
            } else {
                throw new IOException("A source file must be selected.");
            }
            encrypt(sourceFile, targetFile);
        } catch (Exception e) {
            this.addToLog("Exception Occured: " + e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnEncryptActionPerformed

    private void btnDecryptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDecryptActionPerformed
        try {
            JFileChooser jf = new JFileChooser();
            File privateKeyFile = null;
            File encryptedKeyFile = null;
            File sourceFile =  null;
            File targetFile = null;
                    
            if(jf.showDialog(btnEncrypt, "Open Enc Key") == JFileChooser.APPROVE_OPTION){
                encryptedKeyFile = jf.getSelectedFile();
            } else {
                throw new IOException("A private key must be selected.");
            }
            if(jf.showDialog(btnEncrypt, "Open Priv Key") == JFileChooser.APPROVE_OPTION){
                privateKeyFile = jf.getSelectedFile();
            } else {
                throw new IOException("A private key must be selected.");
            }
            loadKey(encryptedKeyFile, privateKeyFile);
            if(jf.showDialog(btnEncrypt, "Open Source File") == JFileChooser.APPROVE_OPTION){
                sourceFile = jf.getSelectedFile();
                targetFile = new File(jf.getSelectedFile().getAbsolutePath().substring(0, jf.getSelectedFile().getAbsolutePath().length() - 4));
            } else {
                throw new IOException("A source file must be selected.");
            }
            decrypt(sourceFile, targetFile);
        } catch (Exception e) {
            this.addToLog("Exception Occured: " + e.getMessage() + "\n" + e.getStackTrace());
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDecryptActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDecrypt;
    private javax.swing.JButton btnEncrypt;
    private javax.swing.JTextArea txtStatus;
    // End of variables declaration//GEN-END:variables
    
    
    //This function generates a new public and private key
    public void makeKey() throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        kgen.init(AES_Key_Size);
        SecretKey key = kgen.generateKey();
        aesKey = key.getEncoded();
        aeskeySpec = new SecretKeySpec(aesKey, "AES");
    }
    
    //Accepts an encrypted public key and decrypts it using an encrypted private key
    public void loadKey(File in, File privateKeyFile) throws GeneralSecurityException, IOException {
        byte[] encodedKey = new byte[(int)privateKeyFile.length()];
        new FileInputStream(privateKeyFile).read(encodedKey);
        PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PrivateKey pk = kf.generatePrivate(privateKeySpec);
        pkCipher.init(Cipher.DECRYPT_MODE, pk);
        aesKey = new byte[AES_Key_Size/8];
        CipherInputStream is = new CipherInputStream(new FileInputStream(in), pkCipher);
        is.read(aesKey);
        aeskeySpec = new SecretKeySpec(aesKey, "AES");
    }
    
    //Accepts a public key and outputs an encrypted public key
    public void saveKey(File out, File publicKeyFile) throws IOException, GeneralSecurityException {
        byte[] encodedKey = new byte[(int)publicKeyFile.length()];
        new FileInputStream(publicKeyFile).read(encodedKey);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pk = kf.generatePublic(publicKeySpec);
        pkCipher.init(Cipher.ENCRYPT_MODE, pk);
        try (CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out), pkCipher)) {
            os.write(aesKey);
        }
    }
    
    //Accepts encrypted file and outputs decrypted file
    public void encrypt(File in, File out) throws IOException, InvalidKeyException {
        aesCipher.init(Cipher.ENCRYPT_MODE, aeskeySpec);	
        FileInputStream is = new FileInputStream(in);
        try (CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out), aesCipher)) {
            copy( is, os);
        }
        is.close();
    }
    
    //Accepts an encrypted file and outputs encrypted file
    public void decrypt(File in, File out) throws IOException, InvalidKeyException {
         aesCipher.init(Cipher.DECRYPT_MODE, aeskeySpec);
        FileOutputStream os;
        try (CipherInputStream is = new CipherInputStream(new FileInputStream(in), aesCipher)) {
            os = new FileOutputStream(out);
            copy(is, os);
        }
        os.close();
    }
    
    //copies contents of one stream to another
    private void copy(InputStream is, OutputStream os) throws IOException {
            int i;
            byte[] b = new byte[1024];
            while((i=is.read(b))!=-1) {
                    os.write(b, 0, i);
            }
    }
    
    private void addToLog(String text){
        System.out.println(text);
        txtStatus.setText(txtStatus.getText() + text + "\n");
    }
}
