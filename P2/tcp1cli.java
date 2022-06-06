import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.util.Scanner;

public class tcp1cli {
	static String ip_address;
	static int port_numer;
	private static int timeout = 10;
	static int num = 0;
	
	public static void main (String args[]) throws Exception {
		if (args.length==2) {
			ip_address=args[0];
			port_numer=Integer.parseInt(args[1]);	
			DataInputStream entrada;
			DataOutputStream salida;

	
			try {
				Socket sc = new Socket (ip_address, port_numer);
				Scanner read = new Scanner (System.in);
				read.useDelimiter("\n");

				entrada = new DataInputStream (sc.getInputStream());
				salida = new DataOutputStream (sc.getOutputStream());
				
				boolean cero = false;
				while (!cero) {
						String cadena = "";

						System.out.println("Introduce números (Deben estar en la misma línea): ");
						cadena = read.nextLine();
						salida.writeUTF(cadena);
						String msjEntrada = entrada.readUTF();
						System.out.println (msjEntrada);
						System.out.println("Valor del acumulador: " + msjEntrada);
					    String[] cadenas = cadena.trim().split(" ");
				        for (int i = 0; i<cadenas.length; i++){
				            if (cadenas[0].equals("0")) {
				            	cero = true;
				            }
				        } 
				}
				sc.close();
			} catch (SocketException f) {
				System.out.println("No se ha podido crear la conexión TCP."); 
			}
		} else {
			System.out.println("Sintaxis correcta: $ udpcli ip_address port_numer");
			System.exit(0);
		}
	}
}
