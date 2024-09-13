package Servidor;

public class BinarioADecimal {
    public static void main(String[] args) {
        System.out.println(convertirBinToDec("100010")); // 10
        System.out.println(convertirBinToDec("10011")); // 100
        System.out.println(convertirBinToDec("1101")); // 6
    }

    public static int convertirBinToDec(String binario) {
        int decimal = 0;
        int potencia = 0;
        for (int i = binario.length() - 1; i >= 0; i--) {
            char bit = binario.charAt(i);
            if (bit == '1') {
                decimal += Math.pow(2, potencia);
            }
            potencia++;
        }
        return decimal;
    }
}
