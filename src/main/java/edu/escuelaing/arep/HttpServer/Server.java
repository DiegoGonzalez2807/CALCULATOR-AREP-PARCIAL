package edu.escuelaing.arep.HttpServer;

import edu.escuelaing.arep.calculator.TrigCalculator;

import java.net.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    static String HTTP_OK_HEADER = "HTTP/1.1 200 OK\r\n"
            + "Content-Type: application/json\r\n"
            + "\r\n";

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        //CICLO DE ESCUCHA
        while(running){
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, resource;
            boolean flag = true;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                //REVISA LA PRIMER LINEA GET /resource HTTP/1.1
                if(flag){
                    flag = false;
                    resource = inputLine.split(" ")[1];
                    reviewFirstLine(resource,out);
                }
                if (!in.ready()) {break; }
            }
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }
    /**
     * Funcion encargada de revisar la primer linea y ver que recurso se esta pidiendo
     * @param resource
     */
    public static void reviewFirstLine(String resource,PrintWriter out) throws IOException {
        if(resource.contains("val")){
            String operation = checkOperation(resource);
            Double value = Double.valueOf(resource.split("=")[1]);
            if(operation.equals("qck")){
                System.out.println("QUICKSORT AUN NO");
            }
            else{
                String possibleJson = "{value:"+getResult(value,operation)+"}";
                out.println(HTTP_OK_HEADER+possibleJson);
            }
        }
        else{
            System.out.println("NO SE PUDO LEER DE MANERA CORRECTA EL RECURSO");
        }
    }

    public static Double getResult(Double value, String operation){
        TrigCalculator calculator = new TrigCalculator();

        if(operation.equals("sin")){
            return calculator.Sin_Operation(value);
        }
        else if(operation.equals("cos")){
            return calculator.Cos_Operation(value);
        }
        else if(operation.equals("tan")){
            return calculator.Tan_Operation(value);
        }
        return null;
    }

    public static String checkOperation(String resource){
        if(resource.contains("sin")){
            return "sin";
        }
        else if(resource.contains("cos")){
            return "cos";
        }
        else if(resource.contains("tan")){
            return "tan";
        }
        else if(resource.contains("qck")){
            return "qck";
        }
        return null;
    }

    }
