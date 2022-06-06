import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.spi.RegisterableService;

public class tcp3ser {

	public static void main (String args[]) throws Exception {
			
		final int port_numer;
		
		if (args.length==1) {
			System.out.println ("El servidor se ha iniciado.");
			port_numer = Integer.parseInt(args[0]);
			Selector selector = Selector.open();
			ServerSocketChannel server = ServerSocketChannel.open();
	        server.bind(new InetSocketAddress(port_numer));
			server.configureBlocking(false);
			int acul = 0;
			int acul2 = 0;
			DatagramChannel channel = DatagramChannel.open();
   	        channel.socket().bind(new InetSocketAddress(port_numer));
   	        channel.configureBlocking(false);
 		    channel.register(selector,SelectionKey.OP_READ);
			int ops = server.validOps();
			server.register(selector, ops);		
			while(true){

				selector.selectNow();
				Set<SelectionKey> selectedKeys = selector.selectedKeys();    
				Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
				boolean cero = false;
			    while(keyIterator.hasNext()) {
				    SelectionKey keyIt = keyIterator.next();
				    boolean interrupcion = false; 
				    if(keyIt.isAcceptable()) {
				    	   try {
					    	   SocketChannel cliente = server.accept();
						       cliente.configureBlocking(false);
						       acul = 0;
					           cliente.register(selector, SelectionKey.OP_READ, acul);			
			                   System.out.println("Conexión aceptada del cliente TCP: " + cliente); 
				    	   } catch (Exception e) {
				    		   interrupcion = true;
				    		   keyIt.cancel();
				    	   }
		                   
				    } else if (keyIt.isReadable()) {

				    	  if (keyIt.channel() instanceof DatagramChannel) {
				    		  System.out.println ("Estamos ante un cliente UDP.");
				    		  DatagramChannel cliente2 = (DatagramChannel) keyIt.channel();
				    		  ByteBuffer buffer = ByteBuffer.allocate(256);
						      SocketAddress remote_address = cliente2.receive(buffer);
						      buffer.flip();
						      buffer.clear();
					        	 String res = new String(buffer.array()).trim();				        	
					             String[] recvStrings = res.trim().split(" ");
					             for (int i = 0; i<recvStrings.length; i++){
							            int num = Integer.parseInt(recvStrings[i].trim());
							            if (num == 0) {
							            	 cero = true;
							            	 break;
							        }
							      }
						         String send = new String(buffer.array()).trim();				        	
						         String[] sendStrings = send.trim().split(" ");
								     for (int j = 0; j<sendStrings.length; j++){
								         int num = Integer.parseInt(sendStrings[j].trim());
							           	 acul2 +=num;
								         keyIt.attach(acul2);
								         Object act = keyIt.attachment();
								         int c = (Integer) act;
								         System.out.println("El valor del acumulador UDP es " + c + ".");  
								        }
								        String acumulador = String.valueOf(acul2);
							        	buffer = ByteBuffer.wrap(acumulador.getBytes());
							            cliente2.send(buffer, remote_address);
							            buffer.clear();
				    	 } else {
				    		 try {
					    		 System.out.println ("Estamos ante un cliente TCP.");
						    	 SocketChannel cliente = (SocketChannel) keyIt.channel();	
					             ByteBuffer buffer = ByteBuffer.allocate(256);
						    	 cliente.read(buffer);
					             buffer.clear();
					        	 String res = new String(buffer.array()).trim();				        	
					             String[] recvStrings = res.trim().split(" ");
					             for (int l = 0; l<recvStrings.length; l++){
							            int num = Integer.parseInt(recvStrings[l].trim());
							            if (num == 0) {
							            	 cero = true;
							            	 break;
							        }
							      }
					             if (cero) {
					            	 cliente.close();
						             System.out.println("El valor introducido por el cliente TCP es 0 por lo que no es aceptado.");
					             } else {
						        	 String send = new String(buffer.array()).trim();				        	
						        	 String[] sendStrings = send.trim().split(" ");
							         int ac = (Integer) keyIt.attachment();
								     for (int k = 0; k<sendStrings.length; k++){
								         int num = Integer.parseInt(sendStrings[k].trim());
								         ac +=num;
								         keyIt.attach(ac);
								         System.out.println("El valor del acumulador del cliente TCP es  " + ac + ".");
								     }
								     String acumulador = String.valueOf(ac);
								     buffer = ByteBuffer.wrap(acumulador.getBytes());
								     cliente.write(buffer);
								     buffer.clear();
						         } 

				    	 } catch (Exception e) {
				    		 interrupcion = true;
				    		 keyIt.cancel();
				    		 break;
				    	 }
				    } 
				    }

				    keyIterator.remove();
				}
			}		
				} else {
					System.out.println("Sintaxis correcta: $ tcp3ser port_numer");
					System.exit(0);
		}
	}
}
