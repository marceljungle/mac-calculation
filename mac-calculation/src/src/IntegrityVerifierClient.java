package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.management.openmbean.InvalidKeyException;
import javax.net.SocketFactory;
import javax.swing.JOptionPane;

public class IntegrityVerifierClient {

	static String[] options = { "HMAC SHA MD5", "HMAC SHA 1", "HMAC SHA 256", "HMAC SHA 384", "HMAC SHA 512" };

	public IntegrityVerifierClient() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		// Constructor que abre una conexión Socket para enviar mensaje/MAC al
		// servidor
		try {
			SocketFactory socketFactory = (SocketFactory) SocketFactory.getDefault();
			Socket socket = (Socket) socketFactory.createSocket("localhost", 7070);
			// crea un PrintWriter para enviar mensaje/MAC al servidor
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			String mensaje = JOptionPane.showInputDialog(null, "Introduzca su mensaje:");
			/*
			 * Devuelve el indice de la opción elegida, y es tratada en la funcion
			 * calculateHMAC
			 */
			int algoritmo = JOptionPane.showOptionDialog(null,
					"Seleccione el algoritmo a emplear: (Por defecto HMAC SHA 512)", "Click a button",
					JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
			output.println(mensaje); // envio del mensaje al servidor
			// habría que calcular el correspondiente MAC con la clave
			// compartida por servidor/cliente
			String key = RandomString.getAlphaNumericString(100);
			String macdelMensaje = secureCore.calculateHMAC(mensaje, key, algoritmo);
			System.out.println("[Cliente] El algoritmo usado es: " + algoritmo);
			System.out.println("[Cliente] Mensaje: " + mensaje);
			System.out.println("[Cliente] Key: " + key);
			System.out.println("[Cliente] MAC: " + macdelMensaje);
			output.println(macdelMensaje);
			output.println(key);
			output.println(algoritmo);
			output.flush();
			// crea un objeto BufferedReader para leer la respuesta del servidor
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String respuesta = input.readLine(); // lee la respuesta del servidor
			System.out.println(respuesta); // muestra la respuesta al cliente
			output.close();
			input.close();
			socket.close();
		} // end try
		catch (IOException ioException) {
			ioException.printStackTrace();
		}
		// Salida de la aplicacion
		finally {
			System.exit(0);
		}
	}

	// ejecución del cliente de verificación de la integridad
	public static void main(String args[]) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		new IntegrityVerifierClient();
	}
}
