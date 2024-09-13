package Servidor;

public class BinarioAHexadecimal {
    public static void main(String[] args) {
        System.out.println(convertirBinToHex("11100101"));
        System.out.println(convertirBinToHex("011010001"));
        System.out.println(convertirBinToHex("110001001111"));
    }
    public static String convertirBinToHex(String binario) {
        int decimal = Integer.parseInt(binario, 2);
        return Integer.toHexString(decimal);
    }
}
