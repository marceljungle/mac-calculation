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

public class atacante {

	public atacante() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		try {
			SocketFactory socketFactory = (SocketFactory) SocketFactory.getDefault();
			Socket socket = (Socket) socketFactory.createSocket("localhost", 7070);
			// crea un PrintWriter para enviar mensaje/MAC al servidor
			PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			int algoritmo = 4;
			String mensajeTime = "ES12 123123 123123 123123 123123 200€ ES13 123123 123123 123123 123123";
			output.println(mensajeTime); // envio del mensaje al servidor
			String macdelMensaje = "10bd779cc577933ee2f34412dab37204f98a1a976702cbb4c210ae468a270a00ec4cb8c021376cb42124040417fc3026cae83feb58dc8d898f980fb24cd5b585";
			output.println(macdelMensaje);
			output.println(algoritmo);
			output.flush();
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
		new atacante();
	}
}
