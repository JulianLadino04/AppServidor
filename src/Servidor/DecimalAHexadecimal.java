package Servidor;

public class DecimalAHexadecimal {
    public static void main(String[] args) {
        System.out.println(convertirDecToHex(2742, 3)); // 000A
        System.out.println(convertirDecToHex(43, 2)); // FF
        System.out.println(convertirDecToHex(719, 3)); // 004D2
    }
    public static String convertirDecToHex(int decimal, int ancho) {
        String hexadecimal = Integer.toHexString(decimal);
        while (hexadecimal.length() < ancho) {
            hexadecimal = "0" + hexadecimal;
        }
        return hexadecimal;
    }
}
