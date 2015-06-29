package servidor;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;


public class Servidor {

    public static List<HashMap> usuariosConectados = new ArrayList<>();
    public static ServerSocket serversock;
    public static Socket sock;
    public static ObjectOutputStream salidaObjecto;
    public static ObjectInputStream entrada;
    
    public static void main(String[] args) {
       try {
           //multicast_IP puerto_multicast puerto_TCP
            String multicast_IP = "";
            int puerto_multicast = 0, puerto_TCP = 0;
            //parametros ingresados por consola
            String[] parametro = new String[3];
            for (int i = 0; i < args.length; i++) {
                switch (i) {
                    case 0:
                        parametro[0] = args[i];
                        break;
                    case 1:
                        parametro[1] = args[i];
                        break;
                    case 2:
                        parametro[2] = args[i];
                        break;
                }
            }
            if (parametro != null) {
                //validacion de los parametros ingresados
                String IPV4_REGEX = "(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)"
                        + "|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))";//para saber si es una ip valida
                String PUERTO = "^[0-9]*";
                Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);
                Pattern PUERTO_PATTERN = Pattern.compile(PUERTO);
                if (IPV4_PATTERN.matcher(parametro[0]).matches()) {//valida la ip ingresada
                    multicast_IP = parametro[0];
                    if (PUERTO_PATTERN.matcher(parametro[1]).matches()) {//valida EL PUERTO 
                        puerto_multicast = Integer.parseInt(parametro[1].trim());
                        if (PUERTO_PATTERN.matcher(parametro[2]).matches()) {//valida EL PUERTO TCP
                            puerto_TCP = Integer.parseInt(parametro[2].trim());
                        } else {
                            System.out.println("Error de puerto tcp ... ");
                            System.exit(0);
                        }
                    } else {
                        System.out.println("Error de puerto multicast ... ");
                        System.exit(0);
                    }
                } else {
                    System.out.println("Error de ip multicast ... ");
                    System.exit(0);
                }
                System.out.println("Servidor iniciado ...");

                // defino socket
                serversock = new ServerSocket(puerto_TCP);
                while (true) {//esperar por un nuevo cliente
                    sock = serversock.accept();
                    sock.setSoLinger(true, 10);
                    //envio multicasip y puerto multicast                   
                    salidaObjecto = new ObjectOutputStream(sock.getOutputStream());
                    String ipenviar = multicast_IP + ":" + puerto_multicast;
                    salidaObjecto.writeObject(ipenviar);
                    salidaObjecto.close(); 
                    sock.close();
                    new Thread(new HiloProcesarComandos(puerto_TCP)).start();//hilo para ejecutar comandos                  
                    new Thread(new HiloEscucharConversa(multicast_IP,puerto_multicast)).start();//hilo para escuchar conversaciones
                    
                }
            } else {
                System.out.println("Error al ingresar los parámetros .. ");
                System.exit(0);
            }
        } catch (IOException ex) {
           System.out.println(ex.getMessage());
        }
    }
}
