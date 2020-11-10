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
				String key = input.readLine();
				Integer algo = Integer.parseInt(input.readLine());
				System.out.println("[Server] El algoritmo usado es: " + algo);
				System.out.println("[Server] Mensaje: " + mensaje);
				System.out.println("[Server] Key: " + key);
				System.out.println("[Server] MAC: " + macdelMensajeEnviado);
				// a continuación habría que verificar el MAC
				String macdelMensajeCalculado = secureCore.calculateHMAC(mensaje, key, algo);
				System.err.println(mensaje);
				if (macdelMensajeEnviado.equals(macdelMensajeCalculado)) {
					output.println("Mensaje enviado integro ");
				} else {
					output.println("Mensaje enviado no integro.");
				}
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
