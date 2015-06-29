package cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public class HiloMulticastRecibir extends Thread{
    String ip_multicast;
    int puerto_multi;
    
    public HiloMulticastRecibir(String ip,int puerto) {
        this.ip_multicast=ip;
        this.puerto_multi=puerto;
    }
   
    @Override
    public void run() {
      while(true){
        recibir_mensajes(); //espero por mensaje del grupo   
      }  
    }
    
   private void recibir_mensajes() {      
        try {
            InetAddress direccion = InetAddress.getLocalHost();
            String IP_local = direccion.getHostAddress();//ip como String 
            InetAddress grupo = InetAddress.getByName(ip_multicast);
            MulticastSocket s = new MulticastSocket(puerto_multi);
            s.joinGroup(grupo);
            // buffer
            byte[] buffer = new byte[1000];
            DatagramPacket recepcion = new DatagramPacket(buffer, buffer.length);
            s.receive(recepcion);
            String msj = new String(recepcion.getData(), 0, recepcion.getLength());
            if(msj.contains("@")){
              String usuario=msj.substring(0,msj.indexOf("@"));  
              String mensaje=msj.substring(msj.indexOf("@")+1,msj.length());
              System.out.println(usuario+" ---> "+mensaje);
            }
           // s.leaveGroup(grupo);
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
   }  
    
}
