import java.util.Random;

public class HillCipherRuntimeTest {

    static Random rand = new Random();

    static String generatePlaintext(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('A' + rand.nextInt(26)));
        }
        return sb.toString();
    }

    static int[][] generateValidKey(int n) {
        int[][] key;
        do {
            key = new int[n][n];
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++)
                    key[i][j] = rand.nextInt(26);
        } while (!HillCipherDynamic.isValidKey(key, n));
        return key;
    }

    public static void main(String[] args) {

        int matrixSize = 5;     // Fixed matrix size
        int textLength = 11;  // Fixed plaintext size

        int[][] key = generateValidKey(matrixSize);
        String plaintext = generatePlaintext(textLength);

        long startEncrypt = System.nanoTime();
        String cipher = HillCipherDynamic.encrypt(plaintext, key, matrixSize);
        long endEncrypt = System.nanoTime();

        long startDecrypt = System.nanoTime();
        HillCipherDynamic.decrypt(cipher, key, matrixSize);
        long endDecrypt = System.nanoTime();

        System.out.println("=== Runtime Test ===");
        System.out.println("Matrix Size: " + matrixSize + "x" + matrixSize);
        System.out.println("Plaintext Length: " + textLength);
        System.out.println("Encryption Time: " + (endEncrypt - startEncrypt) + " ns");
        System.out.println("Decryption Time: " + (endDecrypt - startDecrypt) + " ns");
    }
}
