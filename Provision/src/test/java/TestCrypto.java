import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.net.util.Base64;
import org.junit.Test;

public class TestCrypto {

	@Test
	public void testCrypto() throws Exception {
		String secretID = "L0ck it up saf3";
		String url = "http://test.com/download/example/xml";

		// Need to pad key for AES
		// Do so by SHA-1 of key, copy first 16 bytes
		// Generate the secret key specs.
		byte[] key = (secretID).getBytes("UTF-8");
		MessageDigest sha = MessageDigest.getInstance("SHA-1");
		key = sha.digest(key);
		key = Arrays.copyOf(key, 16); // use only first 128 bit

		SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
		// Instantiate the cipher
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

		byte[] encrypted = cipher.doFinal(url.getBytes());
		System.out.println("encrypted string: " + Base64.encodeBase64String(encrypted));

		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
		byte[] original = cipher.doFinal(encrypted);
		String originalString = new String(original);
		System.out.println("Original string: " + originalString + "\nOriginal string (Hex): " + Base64.encodeBase64String(original));

	}
}
