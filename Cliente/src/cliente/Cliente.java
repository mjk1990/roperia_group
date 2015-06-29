
package cliente;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.regex.Pattern;


public class Cliente {

   public static String nombre_usuario = null;
    public static DataInputStream entradaMensaje;
    public static String ip_multicast;
    public static int puerto_multicast;
    public static ObjectInputStream entrada;
    public static ObjectOutputStream salida;
    
    public static void main(String[] args) {
        //cliente nombre_usuario servidor_IP servidor_puerto
        String servidor_IP = null;
        int servidor_puerto = 0;

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
            String USUARIO = "^[A-Za-z0-9]*";
            Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);
            Pattern PUERTO_PATTERN = Pattern.compile(PUERTO);
            Pattern USUARIO_PATTERN = Pattern.compile(USUARIO);
            if (USUARIO_PATTERN.matcher(parametro[0]).matches()) {//valida el usuario
                nombre_usuario = parametro[0];
                if (IPV4_PATTERN.matcher(parametro[1]).matches()) {//valida la ip
                    servidor_IP = parametro[1];
                    if (PUERTO_PATTERN.matcher(parametro[2]).matches()) {//valida EL PUERTO TCP
                        servidor_puerto = Integer.parseInt(parametro[2].trim());
                    } else {
                        System.out.println("Error de puerto tcp ... ");
                        System.exit(0);
                    }
                } else {
                    System.out.println("Error de ip servidor ... ");
                    System.exit(0);
                }
            } else {
                System.out.println("Error de usuario ... ");
                System.exit(0);
            }
            try {
                try (Socket socket = new Socket(servidor_IP, servidor_puerto)) {
                    entrada = new ObjectInputStream(socket.getInputStream());
                    String ipingresa = (String) entrada.readObject();
                    entrada.close();
                    socket.close();
                    if (ipingresa.contains(":")) {//formato ip:puerto
                        ip_multicast = ipingresa.substring(0, ipingresa.indexOf(":"));
                        puerto_multicast = Integer.valueOf(ipingresa.substring(ipingresa.indexOf(":") + 1, ipingresa.length()));
                        //creo un hilo para enviar mensajes
                        new Thread(new HiloMulticastEnviar(ip_multicast, puerto_multicast, nombre_usuario)).start();
                        //creo un hilo para recibir mensajes
                        new Thread(new HiloMulticastRecibir(ip_multicast, puerto_multicast)).start();
                    }
                    //comprobar el estado del cliente
                    new Thread(new HiloComprobarEstado(servidor_IP,servidor_puerto)).start();


                } catch (ClassNotFoundException ex) {
                    System.out.println(ex.getMessage());
                }
            } catch (UnknownHostException ex) {
                System.out.println(ex.getMessage());
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            System.out.println("Error al ingresar los parámetros .. ");
            System.exit(0);
        }

    }
}
