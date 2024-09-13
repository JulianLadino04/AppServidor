package Servidor;

public class HexadecimalABinario {
    public static void main(String[] args) {
        System.out.println(convertirHexToBin("67a")); // 1010
        System.out.println(convertirHexToBin("3f")); // 11111111
        System.out.println(convertirHexToBin("a82")); // 10011010010
    }
    public static String convertirHexToBin(String hexadecimal) {
        int decimal = Integer.parseInt(hexadecimal, 16);
        return Integer.toBinaryString(decimal);
    }
}
