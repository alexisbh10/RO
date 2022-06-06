import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

public class udpcli {
	
	static String ip_address;
	static int port_numer;
	private static int timeout = 10;
	static int num = 0;
	
	public static void main (String args[]) throws Exception {
		if (args.length==2) {
			ip_address=args[0];
			port_numer=Integer.parseInt(args[1]);	

			DatagramSocket socketUDP = null;
			Scanner read = new Scanner (System.in);
	
			try {
				String cadena = "";
				socketUDP = new DatagramSocket();
				InetAddress ip_server = InetAddress.getByName(ip_address);
				System.out.println("Introduce números (Deben estar en la misma línea y aunque haya un 0 de por medio, el programa sólo sumará hasta ese cero): ");
				cadena = read.nextLine();
			    byte[] numsByte = cadena.getBytes();
				DatagramPacket sendPack = new DatagramPacket (numsByte, numsByte.length, ip_server, port_numer);
				socketUDP.send(sendPack);
				socketUDP.setSoTimeout(timeout*1000);
				try {
					byte[] recvBytes = new byte[69];
					DatagramPacket recvPack = new DatagramPacket(recvBytes, recvBytes.length);
					socketUDP.receive(recvPack);
					recvBytes = recvPack.getData();
					int recvNum = ByteBuffer.wrap(recvBytes).getInt();
					System.out.println("Valor recibido: " + recvNum);
				} catch (SocketTimeoutException e) {
					System.out.println("No hay respuesta del servidor " + ip_address + ":" + port_numer + " tras " + timeout + " segundos.");
					System.exit(0);
				}
				socketUDP.close();
			} catch (SocketException f) {
				System.out.println("No se ha podido crear la conexión UDP.");
				f.printStackTrace();
			}
		} else {
			System.out.println("Sintaxis correcta: $ udpcli ip_address port_numer");
			System.exit(0);
		}
	}
}
