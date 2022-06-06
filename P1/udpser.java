import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class udpser {

	static int port_numer;
	
	public static void main (String args[]) throws Exception {
		
		if (args.length==1) {
			port_numer = Integer.parseInt(args[0]);
			
			try {
				int acul = 0;
				InetAddress ip_client = null;
				int portClient = 0;
				DatagramPacket recvPack = null;
				DatagramSocket socketUDP = null;
				boolean vacio = false;
				
				while (true) {
					socketUDP = new DatagramSocket (port_numer);
					byte[] recvBytes = new byte[69];
					recvPack = new DatagramPacket (recvBytes, recvBytes.length);
					socketUDP.receive(recvPack);
					ip_client = recvPack.getAddress();
					portClient = recvPack.getPort();
					String recvStr = new String(recvBytes, StandardCharsets.UTF_8);
			        String[] recvStrings = recvStr.trim().split(" ");
			        for (int i = 0; i<recvStrings.length; i++){
			            int num = Integer.parseInt(recvStrings[i].trim());
			            if (recvStrings[i].equals("0")) {
			            	break;
			            }
			            acul +=num;
						System.out.println("Valor del acumulador: " + acul);
			        }
			        byte[] sendByte = ByteBuffer.allocate(69).putInt(acul).array();
					DatagramPacket sendPack = new DatagramPacket (sendByte, sendByte.length, ip_client, portClient);
					socketUDP.send(sendPack);
					socketUDP.close();
				}
			} catch (SocketException e) {
				System.out.println("No se ha establecido la conexión entre cliente y servidor.");
				e.printStackTrace();
			}
		} else {
			System.out.println("Sintaxis servidor: $ udpser port_numer");
			System.exit(0);
		}
	}
}
