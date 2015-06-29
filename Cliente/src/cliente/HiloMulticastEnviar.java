package cliente;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HiloMulticastEnviar extends Thread {

    String ipmulticas;
    int puerto_mul;
    String usuario;

    public HiloMulticastEnviar(String ip, int puerto, String usuario) {
        this.ipmulticas = ip;
        this.puerto_mul = puerto;
        this.usuario = usuario;
    }

    @Override
    public void run() {
        while (true) {
            System.out.print(usuario + ">");
            StringBuilder str = new StringBuilder();
            char c;
            try {
                Reader entrad = new InputStreamReader(System.in);
                while ((c = (char) entrad.read()) != '\n') {
                    str.append(c);
                }
                String mensaje =str.toString();//ingresado por el cliente
                if (mensaje.contains("exit")) {
                    System.out.println("Cliente ha terminado ...");
                    System.exit(0);
                } else {//enviamos al grupo
                    mensaje=usuario+"@"+str.toString();//ingresado por el cliente
                    enviar_mensajes(mensaje);
                }
            } catch (IOException ex) {
                System.out.print(ex.getMessage());
            }
        }
    }

    private void enviar_mensajes(String mensaje) {      
        try {
            InetAddress grupo = InetAddress.getByName(ipmulticas);
            MulticastSocket s = new MulticastSocket(puerto_mul);
            s.joinGroup(grupo); // opcional
            DatagramPacket paquete = new DatagramPacket(mensaje.getBytes(), mensaje.length(),grupo, puerto_mul);
            s.setTimeToLive(1); // máquinas locales 
            s.send(paquete);
//            s.leaveGroup(grupo);
        } catch (UnknownHostException ex) {
            Logger.getLogger(HiloMulticastEnviar.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HiloMulticastEnviar.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
