package servidor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class HiloProcesarComandos extends Thread {

    private int puerto_servidor;

    public HiloProcesarComandos(int puerto_servidor) {
        this.puerto_servidor = puerto_servidor;
    }

    @Override
    public void run() {
        try {
            System.out.print("Comando>");
            StringBuilder str = new StringBuilder();
            char c;
            String consulta;
            while (true) {
                Reader entrad = new InputStreamReader(System.in);
                while ((c = (char) entrad.read()) != '\n') {
                    str.append(c);
                }
                System.out.print("Comando>");
                consulta = str.toString().trim();
                if (consulta.contains("exit")) {
                    System.out.println("Servidor cerrando conexiones ...");
                    System.exit(0);
                }
                if (consulta.contains("list")) {
                    listarClientes();
                }
            }
            
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void listarClientes() {
        System.out.println("valor"+Servidor.usuariosConectados.size());
        for(HashMap m:Servidor.usuariosConectados){
            Iterator<Map.Entry<String, String>> ite = m.entrySet().iterator();
            String valor = null;
            while (ite.hasNext()) {
                Map.Entry<String, String> e = (Entry<String, String>) ite.next();
                if (e.getKey().equals("ip")) {
                    if (e.getValue() != null) {
                        valor = e.getValue().toString();
                    }
                }
            }            
            if(valor!=null){
               new Thread(new HiloComprobarEstadoCliente(puerto_servidor, valor)).start();     
            }
           
        }
    }

}

