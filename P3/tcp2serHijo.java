import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

public class tcp2serHijo implements Runnable {
	
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
				System.out.println("AAAAAAA");
			}
	
		}
	}
}
