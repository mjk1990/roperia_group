package servidor;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
        try{
            String mensaje = "activo";
            byte[] m = mensaje.getBytes();
            InetAddress host = InetAddress.getByName(ip_cliente);
            int puerto_cli = 9900;
            DatagramPacket msj = new DatagramPacket(m, m.length, host, puerto_cli);
            try (DatagramSocket aSocket = new DatagramSocket()) {
                aSocket.send(msj);
                byte[] buffer = new byte[1000];
                DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(respuesta);
                String respcli=new String(respuesta.getData());//respuesta del cliente
                if (!respcli.equals("ok")) {
                    eliminar_clienteList(ip_cliente);
                } else {
                    mostrar_clienteList(ip_cliente);
                }
            } catch (SocketException e) {
               System.out.println(e.getMessage());
            }
        } catch (SocketException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
        for (Iterator<HashMap> it = Servidor.usuariosConectados.iterator(); it.hasNext();) {
            HashMap mapa = it.next();
            Iterator<Map.Entry<String, String>> ite = mapa.entrySet().iterator();
            while (ite.hasNext()) {
                Map.Entry<String, String> e = ite.next();
                if (e.getKey().equals("ip")) {
                   if(e.getValue().contains(ip)){
                     System.out.println(e.getValue());
                     break;
                   } 
                }
            }
        } 
    }
    
}
