package Servidor;

public class DecimalABinario {
    public static void main(String[] args) {
        System.out.println(convertirDecToBin(48, 7));
        System.out.println(convertirDecToBin(12, 5));
        System.out.println(convertirDecToBin(22, 5));
    }

    public static String convertirDecToBin(int decimal, int ancho) {
        String binario = Integer.toBinaryString(decimal);
        while (binario.length() < ancho) {
            binario = "0" + binario;
        }
        return binario;
    }
}
