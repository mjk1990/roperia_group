package cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class HiloComprobarEstado extends Thread {

    int puerto_servidor;
    String ip_server;

    public HiloComprobarEstado(String ip, int puerto) {
        this.puerto_servidor = puerto;
        this.ip_server = ip;
    }

    @Override
    public void run() {
//        while (true) {
            udpCliente();
       // }
    }

    /**
     *Esta funcion recibe un mensaje udp del servidor y contesta ok 
     */
    public void udpCliente() {
        // socket
        DatagramSocket aSocket = null;
        try {
            // puerto
            int socket_no = 9900;
            // defino socket
            aSocket = new DatagramSocket(socket_no);
            // buffer almacenamiento
            byte[] buffer = new byte[1000];
            while (true) {
                // paquete requerimiento
                DatagramPacket requerimiento = new DatagramPacket(buffer, buffer.length);
                // recepcion requerimiento
                aSocket.receive(requerimiento);
                System.out.print("espero por una solicitud udp ...");
                String r = new String(requerimiento.getData(), 0, requerimiento.getLength());
                if (r.contains("activo")) {
                    // paquete respuesta
                    DatagramPacket respuesta = new DatagramPacket("ok".getBytes(),
                            "ok".getBytes().length, requerimiento.getAddress(),
                            requerimiento.getPort());
                    // envio
                    aSocket.send(respuesta); 
                    aSocket.close();
                }
            }
           
            // manejo de errores
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } 
    }
}
