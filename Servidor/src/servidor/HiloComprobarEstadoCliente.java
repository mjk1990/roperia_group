package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloComprobarEstadoCliente extends Thread{
    int puerto;
    String ip_cliente;

    public HiloComprobarEstadoCliente(int puerto, String ip_cliente) {
        this.puerto = puerto;
        this.ip_cliente = ip_cliente;
    }
    
    @Override
    public void run() {
        //while (true) {           
            udpServidor();
        //}
    }

    public void udpServidor() {
  
            DatagramSocket aSocket = null; 
            try {
                aSocket = new DatagramSocket(); // cierrro el socket
            } catch (SocketException ex) {
                Logger.getLogger(HiloComprobarEstadoCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            String mensaje = "activo";
            // buffer
            byte[] m = mensaje.getBytes();
            // host y puerto a enviar requerimiento
            InetAddress host = null;
            try {
                host = InetAddress.getByName(ip_cliente);
            } catch (UnknownHostException ex) {
                Logger.getLogger(HiloComprobarEstadoCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            int puerto_cli = 9900;
            // mensaje a enviar
            DatagramPacket msj = new DatagramPacket(m, m.length, host, puerto_cli);
            try {
                // envio
                aSocket.send(msj);
            } catch (IOException ex) {
                Logger.getLogger(HiloComprobarEstadoCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            // paquete mensaje a recibir
            byte[] buffer = new byte[1000];
            DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
            try {
                aSocket.receive(respuesta);
            } catch (IOException ex) {
                Logger.getLogger(HiloComprobarEstadoCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
            String respcli=new String(respuesta.getData());//respuesta del cliente
            if (!respcli.equals("ok")) {
                eliminar_clienteList(ip_cliente);
            } else {
                mostrar_clienteList(ip_cliente);
            }
            // manejo de errores
        
    }
    
    private void eliminar_clienteList(String ip){
       for (HashMap mapa : Servidor.usuariosConectados) {
            Iterator<Map.Entry<String, String>> ite = mapa.entrySet().iterator();
            while (ite.hasNext()) {
                Map.Entry<String, String> e = ite.next();
                if (e.getKey().equals("ip")) {
                   if(e.getValue().contains(ip)){
                     Servidor.usuariosConectados.remove(mapa);
                     break;
                   } 
                }
            }
        } 
    }
    
    private void mostrar_clienteList(String ip){ 
       for (HashMap mapa : Servidor.usuariosConectados) {
            Iterator<Map.Entry<String, String>> ite = mapa.entrySet().iterator();
            while (ite.hasNext()) {
                Map.Entry<String, String> e = ite.next();
                if (e.getKey().equals("ip")) {
                   if(e.getValue().contains(ip)){
                     break;
                   } 
                }
            }
        } 
    }
    
}
