package Servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import static Servidor.BinarioAHexadecimal.convertirBinToHex;
import static Servidor.DecimalABinario.convertirDecToBin;
import static Servidor.DecimalAHexadecimal.convertirDecToHex;

public class EchoTCPServer {
    public static final int PORT = 3400;
    private ServerSocket listener;
    private Socket serverSideSocket;
    private PrintWriter toNetwork;
    private BufferedReader fromNetwork;

    public EchoTCPServer() {
        System.out.println("Echo TCP Server está corriendo en el puerto: " + PORT);
    }

    public void init() throws Exception {
        listener = new ServerSocket(PORT);
        while (true) {
            serverSideSocket = listener.accept();
            createStreams(serverSideSocket);
            protocol(serverSideSocket);
            serverSideSocket.close();  // Cerrar la conexión después de enviar el mensaje
        }
    }

    public void protocol(Socket socket) throws Exception {
        String message = fromNetwork.readLine();
        System.out.println("[Server] From client: " + message);

        String answer = processMessage(message);
        toNetwork.println(answer);
    }

    private String processMessage(String message) {
        String[] parts = message.split(" ");
        String command = parts[0];
        String answer;
        int longitudCadena = 0;

        //para verificar condicion: longitud de cadena multiplo de 16:
        if (!command.equals("UNI-CAD")){
            longitudCadena = Integer.valueOf(parts[1]);
        }


        try {
            switch (command) {
                case "CONV-DEC-BIN":
                    if (parts.length == 3) {
                        int decimalValue = Integer.parseInt(parts[1]);
                        int bitWidth = Integer.parseInt(parts[2]);
                        answer = "Resultado: " + convertirDecToBin(decimalValue, bitWidth);
                    } else {
                        answer = "Error: Formato incorrecto. Use: CONV-DEC-BIN <decimal> <ancho en bits>";
                    }
                    break;

                case "CONV-DEC-HEX":
                    if (parts.length == 3) {
                        int decimalValue = Integer.parseInt(parts[1]);
                        int bitWidth = Integer.parseInt(parts[2]);
                        answer = "Resultado: " + convertirDecToHex(decimalValue, bitWidth);
                    } else {
                        answer = "Error: Formato incorrecto. Use: CONV-DEC-HEX <decimal> <ancho en bits>";
                    }
                    break;

                case "CONV-BIN-HEXA":
                    if (parts.length == 2) {
                        String binaryValue = parts[1];
                        answer = "Resultado: " + convertirBinToHex(binaryValue);
                    } else {
                        answer = "Error: Formato incorrecto. Use: CONV-BIN-HEXA <binario>";
                    }
                    break;

                case "GEN-CAD":
                    if (parts.length == 2) {
                        int length = Integer.parseInt(parts[1]);
                        answer = "Resultado: " + generateAlphabeticalString(length);
                    } else if (parts.length == 3 && longitudCadena%16==0) {
                        int length = Integer.parseInt(parts[1]);
                        int segmentSize = Integer.parseInt(parts[2]);
                        answer = "Resultado: " + segmentString(generateAlphabeticalString(length), segmentSize);
                    } else {
                        answer = "Error: Formato incorrecto. Use: GEN-CAD <longitud> [segmento]";
                    }
                    break;

                case "GEN-CAD-PAR":
                    if (parts.length == 3 && longitudCadena%16==0) {
                        int length = Integer.parseInt(parts[1]);
                        int segmentSize = Integer.parseInt(parts[2]);
                        String generatedString = generateAlphabeticalString(length);
                        answer = segmentStringIndependently(generatedString, segmentSize);
                    } else {
                        answer = "Error: Formato incorrecto. Use: GEN-CAD-PAR <longitud> <segmento>";
                    }
                    break;

                case "CAD-SEG":
                    if (parts.length > 2 &&VerificarNumeroPartes(parts)) {
                        int length = Integer.parseInt(parts[1]);
                        int[] segmentSizes = new int[parts.length - 2];
                        for (int i = 2; i < parts.length; i++) {
                            segmentSizes[i - 2] = Integer.parseInt(parts[i]);
                        }
                        answer = "Resultado: " + segmentStringWithSizes(generateAlphabeticalString(length), segmentSizes);
                    } else {
                        answer = "Error: Formato incorrecto. Use: CAD-SEG <longitud> <tamaños de segmentos>";
                    }
                    break;

                case "CAD-SEG-PAR":
                    if (parts.length > 2 && longitudCadena%16==0 && VerificarNumeroPartes(parts)) {
                        int length = Integer.parseInt(parts[1]);
                        int[] segmentSizes = new int[parts.length - 2];
                        for (int i = 2; i < parts.length; i++) {
                            segmentSizes[i - 2] = Integer.parseInt(parts[i]);
                        }
                        String generatedString = generateAlphabeticalString(length);
                        answer = segmentStringWithSizesIndependently(generatedString, segmentSizes);
                    } else {
                        answer = "Error: Formato incorrecto. Use: CAD-SEG-PAR <longitud> <tamaños de segmentos>";
                    }
                    break;
                case "UNI-CAD":
                    if (parts.length > 1) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 1; i < parts.length; i++) {
                            sb.append(parts[i]);
                        }
                        answer = "Resultado: " + sb.toString();
                    } else {
                        answer = "Error: Formato incorrecto. Use: UNI-CAD <partes de la cadena>";
                    }
                    break;

                default:
                    answer = "Error: Comando no reconocido.";
            }
        } catch (NumberFormatException e) {
            answer = "Error: Parámetro no es un número válido.";
        }

        return answer;
    }

    // Genera una cadena alfabética en minúsculas
    private String generateAlphabeticalString(int length) {
        StringBuilder sb = new StringBuilder();
        String chars = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(i % chars.length()));
        }
        return sb.toString();
    }

    // Segmenta la cadena de forma independiente, formateando como "Parte N: segmento"
    private String segmentStringIndependently(String str, int segmentSize) {
        StringBuilder sb = new StringBuilder();
        int partNumber = 1;
        for (int i = 0; i < str.length(); i += segmentSize) {
            sb.append("Parte ").append(partNumber++).append(": ");
            sb.append(str, i, Math.min(str.length(), i + segmentSize)).append("\n");
        }
        return sb.toString();
    }

    // Segmenta la cadena en partes de diferentes tamaños, formateando como "Parte N: segmento"
    private String segmentStringWithSizesIndependently(String str, int[] segmentSizes) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        int partNumber = 1;
        for (int size : segmentSizes) {
            if (index + size > str.length()) break;
            sb.append("Parte ").append(partNumber++).append(": ");
            sb.append(str, index, index + size).append("\n");
            index += size;
        }
        return sb.toString();
    }

    private String segmentString(String str, int segmentSize) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i += segmentSize) {
            if (i > 0) sb.append(",");
            sb.append(str, i, Math.min(str.length(), i + segmentSize));
        }
        return sb.toString();
    }

    private String segmentStringWithSizes(String str, int[] segmentSizes) {
        StringBuilder sb = new StringBuilder();
        int index = 0;
        for (int size : segmentSizes) {
            if (index + size > str.length()) break;
            if (sb.length() > 0) sb.append(",");
            sb.append(str, index, index + size);
            index += size;
        }
        return sb.toString();
    }

    private void createStreams(Socket socket) throws Exception {
        toNetwork = new PrintWriter(socket.getOutputStream(), true);
        fromNetwork = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static void main(String args[]) throws Exception {
        EchoTCPServer es = new EchoTCPServer();
        es.init();
    }

    public boolean VerificarNumeroPartes(String[] partes){
        boolean respuesta = true;
        int longitud = Integer.valueOf(partes[1]);
        int suma=0;

        for(int i=2;i< partes.length;i++){
            suma+=Integer.valueOf(partes[i]);
        }
        if(suma>longitud){
            respuesta = false;
        }
        return respuesta;
    }
}
