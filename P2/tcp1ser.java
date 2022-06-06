import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class tcp1ser {
	
	static int port_numer;
	static ServerSocket server = null;
	static Socket sc = null;
	static DataInputStream entrada;
	static DataOutputStream salida;
	
	
	public static void main (String args[]) throws Exception {
		
		if (args.length==1) {
			port_numer = Integer.parseInt(args[0]);
			
				server = new ServerSocket (port_numer);	
				System.out.println("El servidor se ha iniciado.");
				while (true) {
					int acul = 0;
					sc = server.accept();
				    entrada = new DataInputStream (sc.getInputStream());
					salida = new DataOutputStream (sc.getOutputStream());
					boolean interrupcion = false;
					while (!interrupcion) {
						try {
							String msjEntrada = entrada.readUTF();
							if (msjEntrada==null) {
								interrupcion = true;
							}
					        String[] recvStrings = msjEntrada.trim().split(" ");
					        for (int i = 0; i<recvStrings.length; i++){
					            int num = Integer.parseInt(recvStrings[i].trim());
					            acul +=num;
								System.out.println("Valor del acumulador: " + acul);
					        }
					        String acumulador = String.valueOf(acul);
					        salida.writeUTF(acumulador);
					 	} catch (EOFException i) {
							interrupcion = true;
					 	} catch (SocketException e) {
							interrupcion = true;
						} 
					}
		}} else {
			System.out.println("Sintaxis servidor: $ udpser port_numer");
			System.exit(0);
		}
	}
}
