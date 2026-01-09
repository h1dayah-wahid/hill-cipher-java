import java.util.Random;

public class HillCipherAvalancheTest {

    static Random rand = new Random();

    // Generate random plaintext (A–Z only)
    static String generatePlaintext(int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((char) ('A' + rand.nextInt(26)));
        }
        return sb.toString();
    }

    // Generate a valid Hill Cipher key
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

    // Change one character in plaintext
    static String modifyOneCharacter(String text) {
        char[] chars = text.toCharArray();
        int index = rand.nextInt(chars.length);

        char original = chars[index];
        char modified;

        do {
            modified = (char) ('A' + rand.nextInt(26));
        } while (modified == original);

        chars[index] = modified;
        return new String(chars);
    }

    // Count different characters between two strings
    static int countDifferences(String a, String b) {
        int diff = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i))
                diff++;
        }
        return diff;
    }

    public static void main(String[] args) {

        int n = 5;              // Matrix size
        int textLength = 11;   // Plaintext length

        int[][] key = generateValidKey(n);

        String plaintext1 = generatePlaintext(textLength);
        String plaintext2 = modifyOneCharacter(plaintext1);

        String cipher1 = HillCipherDynamic.encrypt(plaintext1, key, n);
        String cipher2 = HillCipherDynamic.encrypt(plaintext2, key, n);

        int changed = countDifferences(cipher1, cipher2);
        double percentage = (changed * 100.0) / cipher1.length();

        System.out.println("=== Avalanche Effect (Diffusion Test) ===");
        System.out.println("Matrix Size: " + n + "x" + n);
        System.out.println("Plaintext Length: " + textLength);
        System.out.println("Changed Ciphertext Characters: " + changed);
        System.out.printf("Diffusion Percentage: %.2f%%\n", percentage);
    }
}

