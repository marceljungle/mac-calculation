package src;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;
import java.util.Properties;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.management.openmbean.InvalidKeyException;

public class secureCore {
	private static final String HMAC_SHA512 = "HmacSHA512";
	private static final String HMAC_SHA384 = "HmacSHA384";
	private static final String HMAC_SHA256 = "HmacSHA256";
	private static final String HMAC_SHA1 = "HmacSHA1";
	private static final String HMAC_MD5 = "HmacMD5";

	public String configFile(String property) {
		Properties prop = new Properties();
		try {
			FileInputStream ip = new FileInputStream(
					"C:\\Users\\Marcel\\OneDrive - UNIVERSIDAD DE SEVILLA\\GitHub\\mac-calculation\\mac-calculation\\src\\src\\config.properties");
			try {
				prop.load(ip);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return prop.getProperty(property);
	}

	public static String importPass() {
		Properties prop = new Properties();
		try {
			FileInputStream ip = new FileInputStream(
					"C:\\Users\\Marcel\\OneDrive - UNIVERSIDAD DE SEVILLA\\GitHub\\mac-calculation\\mac-calculation\\src\\src\\password.properties");
			try {
				prop.load(ip);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
		return prop.getProperty("pass");
	}

	public String sendKey(Socket socket) {
		String clave = RandomString.getAlphaNumericString(100);
		try {
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			output.println(clave); // envio del mensaje al servidor

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}

		return "";
	}

	public static String calculateHMAC(String data, String key, int algo)
			throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {

		// POR DEFECTO PONEMOS HMAC SHA 512

		SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA512);
		Mac mac = Mac.getInstance(HMAC_SHA512);
		if (algo == 0) {
			secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_MD5);
			mac = Mac.getInstance(HMAC_MD5);
		} else if (algo == 1) {
			secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA1);
			mac = Mac.getInstance(HMAC_SHA1);
		} else if (algo == 2) {
			secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA256);
			mac = Mac.getInstance(HMAC_SHA256);
		} else if (algo == 3) {
			secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA384);
			mac = Mac.getInstance(HMAC_SHA384);
		} else if (algo == 4) {
			secretKeySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA512);
			mac = Mac.getInstance(HMAC_SHA512);
		}
		try {
			mac.init(secretKeySpec);
		} catch (java.security.InvalidKeyException e) {
			System.out.println(e.getMessage());
		}
		return toHexString(mac.doFinal(data.getBytes()));
	}

	private static String toHexString(byte[] bytes) {
		@SuppressWarnings("resource")
		Formatter formatter = new Formatter();
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}
		return formatter.toString();
	}

}
