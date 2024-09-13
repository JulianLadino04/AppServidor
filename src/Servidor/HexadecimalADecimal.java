package Servidor;

public class HexadecimalADecimal {
    public static void main(String[] args) {
        System.out.println(convertirHexToDec("51f")); // 10
        System.out.println(convertirHexToDec("72")); // 255
        System.out.println(convertirHexToDec("ac3")); // 1234
    }
    public static int convertirHexToDec(String hexadecimal) {
        return Integer.parseInt(hexadecimal, 16);
    }
}
