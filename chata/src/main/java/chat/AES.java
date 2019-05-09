package chat;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.*;

public class AES {
	private static SecretKeySpec skey;
	
	private static byte[] key;
	
	
	public static void setKey(String myKey) {
		MessageDigest sha = null;
		try {
			AES.key = myKey.getBytes("UTF-8");
			sha = MessageDigest.getInstance("SHA-1");
			key = sha.digest(key);
			key = Arrays.copyOf(key, 16);
			skey = new SecretKeySpec(key, "AES");
		} catch (NoSuchAlgorithmException nsae) {
			throw new RuntimeException(nsae);
		} catch (UnsupportedEncodingException uee) {
			throw new RuntimeException(uee);
		}
	}
	
	public static String encrypt(String plainTxt, String secret) {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			return Base64.getEncoder().encodeToString(cipher.doFinal(plainTxt.getBytes("UTF-8")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		} 
	}
	public static String decrypt(String cipherTxt, String secret) {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5padding");
			cipher.init(Cipher.DECRYPT_MODE, skey);
			return new String(cipher.doFinal(Base64.getDecoder().decode(cipherTxt)));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String txt = "hello world!";
		String key = "ssshhhhhhhhhhh!!";
		String cipher = null;
		System.out.println(txt);
		System.out.println(
				cipher = AES.encrypt(txt, key));
		System.out.println(AES.decrypt(cipher, key));

	}

}
