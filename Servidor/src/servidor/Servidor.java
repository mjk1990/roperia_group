package servidor;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Servidor {

    public static List<HashMap> usuariosConectados = new ArrayList<>();
    public static ServerSocket serversock;
    public static Socket sock;
    public static ObjectOutputStream salidaObjecto;
    public static ObjectInputStream entrada;
    
    public static void main(String[] args) {
        // TODO code application logic here
    }
}
