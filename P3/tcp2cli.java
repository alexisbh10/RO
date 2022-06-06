import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class tcp2cli {
	static String ip_address;
	static int port_numer;
	private static int timeout = 10;
	static int num = 0;
	
	// Crear socket sin conectar, conectamos y ponemos temporizador
	
	public static void main (String args[]) throws Exception {
		if (args.length==2) {
			ip_address=args[0];
			port_numer=Integer.parseInt(args[1]);	
			DataInputStream entrada;
			DataOutputStream salida;

			try {				
				System.out.println("El cliente se ha iniciado.");
				Scanner read = new Scanner (System.in);
				read.useDelimiter("\n");
				
				Socket sc = new Socket ();
				try {
					sc.connect(new InetSocketAddress(ip_address, port_numer), 15000);
					entrada = new DataInputStream (sc.getInputStream());
					salida = new DataOutputStream (sc.getOutputStream());
					
					boolean cero = false;
					while (!cero) {
							String cadena = "";
							System.out.println("Introduce n�meros (Deben estar en la misma l�nea): ");
							cadena = read.nextLine();
							String[] cadenas = cadena.trim().split(" "); 
						        if (cadenas[0].equals("0")) {
						            	cero = true;
						            	break;
							}
							salida.writeUTF(cadena);
							String msjEntrada = entrada.readUTF();
							System.out.println("Valor del acumulador: " + msjEntrada);
							}
					
					sc.close();
				} catch (SocketTimeoutException i) {
					System.out.println("Se han excedido los 15 segundos.");
				}
			} catch (SocketException f) {
				System.out.println("No se ha podido crear la conexi�n TCP."); 
			}
		} else {
			System.out.println("Sintaxis correcta: $ udpcli ip_address port_numer");
			System.exit(0);
		}
	}
}
