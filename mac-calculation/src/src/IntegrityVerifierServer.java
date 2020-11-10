package src;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;

import javax.management.openmbean.InvalidKeyException;
import javax.net.ServerSocketFactory;

public class IntegrityVerifierServer {

	private ServerSocket serverSocket;

	// Constructor
	public IntegrityVerifierServer() throws Exception {
		// ServerSocketFactory para construir los ServerSockets
		ServerSocketFactory socketFactory = (ServerSocketFactory) ServerSocketFactory.getDefault();
		// creación de un objeto ServerSocket (se establece el puerto)
		serverSocket = (ServerSocket) socketFactory.createServerSocket(7070);
	}

	// ejecución del servidor para escuchar peticiones de los clientes
	private void runServer() throws InvalidKeyException, SignatureException, NoSuchAlgorithmException {
		while (true) {
			// espera las peticiones del cliente para comprobar mensaje/MAC
			try {
				System.err.print("Esperando conexiones de clientes...");
				Socket socket = (Socket) serverSocket.accept();
				// abre un BufferedReader para leer los datos del cliente
				BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				// abre un PrintWriter para enviar datos al cliente
				PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
				// se lee del cliente el mensaje y el macdelMensajeEnviado
				String mensaje = input.readLine();
				String macdelMensajeEnviado = input.readLine();
				String key = secureCore.importPass();
				Integer algo = Integer.parseInt(input.readLine());
				System.out.println("hasta aqui llego killo");
				System.out.println("[Server] El algoritmo usado es: " + algo);
				System.out.println("[Server] Mensaje: " + mensaje);
				System.out.println("[Server] Key: " + key);
				/* Parte del codigo para evitar el replay */
				String cadena;
				String macdelMensajeCalculado;
				long time = new Date().getTime();
				long newTime;
				int succeed = 0; // si 0, no ha salido bien
				for (int i = 0; i < 10; i++) {
					newTime = time - i;
					cadena = mensaje + newTime;

					macdelMensajeCalculado = secureCore.calculateHMAC(cadena, key, algo);
					System.out.println("[Cliente] MAC: " + macdelMensajeEnviado);
					System.out.println(newTime);
					System.out.println("[Server] MAC: " + macdelMensajeCalculado);
					// a continuación habría que verificar el MAC
					if (macdelMensajeEnviado.equals(macdelMensajeCalculado)) {
						output.println("Mensaje enviado integro ");
						System.err.println(mensaje);
						succeed = 1;
						break;
					}
				}
				if (succeed == 0) {
					output.println("Mensaje enviado no integro.");
				}

				/* Parte del codigo para evitar el replay */

				output.close();
				input.close();
				socket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}

	// ejecucion del servidor
	public static void main(String args[]) throws Exception {
		IntegrityVerifierServer server = new IntegrityVerifierServer();
		server.runServer();
	}
}
