import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class tcp3cli {

	static String ip_address;
	static int port_numer;
	private static int timeout = 10;
	static int num = 0;
	
	public static void main (String args[]) throws Exception {
		
	if (args.length==2) {
		try {
			ip_address = args[0];
			port_numer = Integer.parseInt(args[1]);
			boolean cero = false;
			SocketChannel cliente = SocketChannel.open(new InetSocketAddress(ip_address,port_numer));
	
			while(!cero) {
				Scanner read = new Scanner (System.in);
				String cadena="";
				System.out.println("Introduce números (Deben estar en la misma línea y aunque haya un 0 de por medio, el programa sólo sumará hasta ese cero): ");
				cadena = read.nextLine();
				ByteBuffer buffer = ByteBuffer.wrap(cadena.getBytes());
				cliente.write(buffer);
				String send = new String(buffer.array()).trim();				        	
	            String[] sendStrings = send.trim().split(" ");
	            for (int i = 0; i<sendStrings.length; i++){
			            int num = Integer.parseInt(sendStrings[i].trim());
			            if (num == 0) {
			            	System.out.println ("Uno de los números introducidos ha sido un 0 y por tanto, el programa ha finalizado.");
			            	System.exit(0);
			        }
			    }
				buffer.clear();
				ByteBuffer res = ByteBuffer.allocate(1024);
		        cliente.read(res);
		        String response = new String(res.array()).trim();
		        System.out.println("El valor del acumulador del cliente TCP es " + response + ".");
		        buffer.clear();
			}
			cliente.close();
		} catch (Exception e) {
			System.out.println ("Ha ocurrido un error con el cliente.");
		}
		} else if (args[2].equals("-u") && args.length==3) {
			try {
			ip_address = args[0];
			port_numer = Integer.parseInt(args[1]);
			DatagramChannel cliente = DatagramChannel.open();
			cliente.bind(null);
			boolean cero = false;
			
			while(!cero) {
				Scanner read = new Scanner (System.in);
				String cadena="";
				System.out.println("Introduce números (Deben estar en la misma línea y aunque haya un 0 de por medio, el programa sólo sumará hasta ese cero): ");
				cadena = read.nextLine();
				ByteBuffer buffer = ByteBuffer.wrap(cadena.getBytes());
				InetSocketAddress serverAddress = new InetSocketAddress(ip_address,port_numer);
				cliente.send(buffer, serverAddress);
				String send = new String(buffer.array()).trim();				        	
	            String[] sendStrings = send.trim().split(" ");
	            for (int i = 0; i<sendStrings.length; i++){
			            int num = Integer.parseInt(sendStrings[i].trim());
			            if (num == 0) {
			            	System.out.println ("Uno de los números introducidos ha sido un 0 y por tanto, el programa ha finalizado.");
			            	System.exit(0);
			        }
			    }
				buffer.clear();
				ByteBuffer res = ByteBuffer.allocate(1024);
		        cliente.receive(res);
		        String response = new String(res.array()).trim();
		        System.out.println("El valor del acumulador del cliente UDP es " + response + ".");
		        buffer.clear();
			}
			cliente.close();
		} catch (Exception e) {
			System.out.println ("Ha ocurrido un error con el cliente.");
		}
		
	} else {
		System.out.println ("Sintaxis correcta: $ tcp3cli ip_address port_numer [-u]");
		System.exit(0);
	}
   }
}
