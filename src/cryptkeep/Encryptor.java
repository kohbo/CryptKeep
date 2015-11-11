/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cryptkeep;

/**
 *
 * @author kohbo
 */
package Encryption;
import java.io.*;
import java.security.*;
import java.security.spec.*;

import javax.crypto.*;
import javax.crypto.spec.*;

public class Encryptor {
public static final int AES_Key_Size = 256;
	
	Cipher pkCipher, aesCipher;
	byte[] aesKey;
	SecretKeySpec aeskeySpec;
	public Encryptor() throws GeneralSecurityException {
		pkCipher = Cipher.getInstance("RSA");
		 aesCipher = Cipher.getInstance("AES");
	}
	public void makeKey() throws NoSuchAlgorithmException {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
	    kgen.init(AES_Key_Size);
	    SecretKey key = kgen.generateKey();
	    aesKey = key.getEncoded();
	    aeskeySpec = new SecretKeySpec(aesKey, "AES");
	}
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
	public void saveKey(File out, File publicKeyFile) throws IOException, GeneralSecurityException {
		byte[] encodedKey = new byte[(int)publicKeyFile.length()];
		new FileInputStream(publicKeyFile).read(encodedKey);
		X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedKey);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey pk = kf.generatePublic(publicKeySpec);
		pkCipher.init(Cipher.ENCRYPT_MODE, pk);
		CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out), pkCipher);
		os.write(aesKey);
		os.close();
	}
	public void encrypt(File in, File out) throws IOException, InvalidKeyException {
          aesCipher.init(Cipher.ENCRYPT_MODE, aeskeySpec);
		
		FileInputStream is = new FileInputStream(in);
		CipherOutputStream os = new CipherOutputStream(new FileOutputStream(out), aesCipher);
		
		copy( is, os);
		
		os.close();
	}
	public void decrypt(File in, File out) throws IOException, InvalidKeyException {
		aesCipher.init(Cipher.DECRYPT_MODE, aeskeySpec);
		
		CipherInputStream is = new CipherInputStream(new FileInputStream(in), aesCipher);
		FileOutputStream os = new FileOutputStream(out);
		
		copy(is, os);
		
		is.close();
		os.close();
	}
	private void copy(InputStream is, OutputStream os) throws IOException {
		int i;
		byte[] b = new byte[1024];
		while((i=is.read(b))!=-1) {
			os.write(b, 0, i);
		}
	}
	

}
