package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HiloEscucharConversa extends Thread{
    private String ip_multicast;
    private int puerto_multi;
    
    
    public HiloEscucharConversa(String ip_multi,int puerto) {
        this.ip_multicast=ip_multi;
        this.puerto_multi=puerto;
    }
   
    @Override
    public void run() {
        while (true) {
            try {            
                recibir_mensajes();
            } catch (UnknownHostException ex) {System.out.println(ex.getMessage());} catch (IOException ex) {System.out.println(ex.getMessage());} 
        }
    }
    
    
    private void recibir_mensajes() throws UnknownHostException, IOException { 
        try {
          while (true) {  
            InetAddress grupo = InetAddress.getByName(ip_multicast);
            MulticastSocket s = new MulticastSocket(puerto_multi);
            s.joinGroup(grupo);
            // buffer
            byte[] buffer = new byte[1000];
            DatagramPacket recepcion = new DatagramPacket(buffer, buffer.length);
            s.receive(recepcion);
            String msj = new String(recepcion.getData(), 0, recepcion.getLength());
//            s.leaveGroup(grupo); 
            if (msj.contains("@")) {
                String usuario = msj.substring(0, msj.indexOf("@"));
                agregarUsuarioList(usuario, recepcion.getAddress().getHostAddress());
            } 
          }
        } catch (Throwable ex) {
            System.out.println("eroroor de hilo"+ex.getMessage());
        }
    }

    private void agregarUsuarioList(String usuario, String ip) {
        HashMap<String, String> usua = new HashMap();
        usua.put("ip", usuario + "-" + ip);
        boolean existe=false;
        for (HashMap mapa : Servidor.usuariosConectados) {
            Iterator<Map.Entry<String, String>> ite = mapa.entrySet().iterator();
            while (ite.hasNext()) {
                Map.Entry<String, String> e = ite.next();
                if (e.getKey().equals("ip")) {
                   if(e.getValue().toString().equals(usuario+"-"+ip)){
                     existe=true;  
                     break;
                   } 
                }
            }
        }
        if(existe==false && ip!=null){
          Servidor.usuariosConectados.add(usua);  
        }        
    }
}
