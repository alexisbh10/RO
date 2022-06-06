
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

public class tcp2ser {
	
	static int port_numer;
	static ServerSocket server = null;
	static Socket sc = null;
	static DataInputStream entrada;
	static DataOutputStream salida;
	static tcp2serHijo tcp2serHijo;
    static Thread thread;
	
	
	public static void main (String args[]) throws Exception {
		
		if (args.length==1) {
			port_numer = Integer.parseInt(args[0]);
			try {
				server = new ServerSocket (port_numer);	
				int numCliente = 0;
				System.out.println("El servidor TCP Multithread se ha iniciado.\n");
	
				while (true) {
					sc = server.accept();
					numCliente++;
					tcp2serHijo sct = new tcp2serHijo(sc, numCliente);
		            new Thread (sct).start();
		        }
			} catch (SocketException e) {
				System.out.println("No se ha podido crear el servidor TCP Multithread.\n");
			}
			
		} else {
			System.out.println("Sintaxis servidor: $ udpser port_numer");
			System.exit(0);
		}
	}
}


class tcp2serHijo extends Thread {
	
	private Socket sc;
	private int numCliente;
	
	public tcp2serHijo (Socket sc, int numCliente) {
		this.sc = sc;
		this.numCliente = numCliente;
	}

	public void run() {
		while (true) {
			try {
				int acul = 0;
			    DataInputStream entradaNum = new DataInputStream (sc.getInputStream());
				DataOutputStream salidaAcul = new DataOutputStream (sc.getOutputStream());
				boolean interrupcion = false;
				while (!interrupcion) {
					try {
						String msjEntrada = entradaNum.readUTF();
						if (msjEntrada==null) {
							interrupcion = true;
						}
				        String[] recvStrings = msjEntrada.trim().split(" ");
				        for (int i = 0; i<recvStrings.length; i++){
				            int num = Integer.parseInt(recvStrings[i].trim());
				            acul +=num;
							System.out.println("Valor del acumulador del cliente " + numCliente+ ": " + acul);
				        }
				        String acumulador = String.valueOf(acul);
				        salidaAcul.writeUTF(acumulador);
				 	} catch (EOFException i) {
						interrupcion = true;
				 	} catch (SocketException e) {
						interrupcion = true;
					} 
				} 
		
			} catch (IOException e) {
				System.out.println("Error.");
			}
	
		}
	}
}
