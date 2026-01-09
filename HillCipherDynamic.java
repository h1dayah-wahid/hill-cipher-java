//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Scanner;

public class HillCipherDynamic {

    static final int MOD = 26;

    /* ===============================
       Utility Functions
       =============================== */

    static int charToInt(char c) {
        return c - 'A';
    }

    static char intToChar(int n) {
        return (char) (n + 'A');
    }

    static int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    static int modInverse(int a) {
        a = (a % MOD + MOD) % MOD;
        for (int x = 1; x < MOD; x++) {
            if ((a * x) % MOD == 1)
                return x;
        }
        return -1;
    }

    /* ===============================
       Matrix Operations
       =============================== */

    static int determinant(int[][] matrix, int n) {
        if (n == 1)
            return matrix[0][0];

        int det = 0;
        int sign = 1;

        for (int f = 0; f < n; f++) {
            det += sign * matrix[0][f] * determinant(getCofactor(matrix, 0, f, n), n - 1);
            sign = -sign;
        }
        return det;
    }

    static int[][] getCofactor(int[][] matrix, int p, int q, int n) {
        int[][] temp = new int[n - 1][n - 1];
        int i = 0, j = 0;

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (row != p && col != q) {
                    temp[i][j++] = matrix[row][col];
                    if (j == n - 1) {
                        j = 0;
                        i++;
                    }
                }
            }
        }
        return temp;
    }

    static boolean isValidKey(int[][] key, int n) {
        int det = determinant(key, n);
        det = (det % MOD + MOD) % MOD;
        return gcd(det, MOD) == 1;
    }

    static int[][] adjoint(int[][] matrix, int n) {
        int[][] adj = new int[n][n];

        if (n == 1) {
            adj[0][0] = 1;
            return adj;
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int sign = ((i + j) % 2 == 0) ? 1 : -1;
                adj[j][i] = sign * determinant(getCofactor(matrix, i, j, n), n - 1);
            }
        }
        return adj;
    }

    static int[][] inverseMatrix(int[][] matrix, int n) {
        int det = determinant(matrix, n);
        det = (det % MOD + MOD) % MOD;

        int detInv = modInverse(det);
        if (detInv == -1)
            throw new RuntimeException("Matrix is not invertible modulo 26.");

        int[][] adj = adjoint(matrix, n);
        int[][] inv = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inv[i][j] = (adj[i][j] * detInv) % MOD;
                inv[i][j] = (inv[i][j] + MOD) % MOD;
            }
        }
        return inv;
    }

    /* ===============================
       Encryption
       =============================== */

    static String encrypt(String plaintext, int[][] key, int n) {
        plaintext = plaintext.toUpperCase().replaceAll("[^A-Z]", "");

        while (plaintext.length() % n != 0)
            plaintext += "X";

        StringBuilder cipher = new StringBuilder();

        for (int i = 0; i < plaintext.length(); i += n) {
            for (int row = 0; row < n; row++) {
                int sum = 0;
                for (int col = 0; col < n; col++) {
                    sum += key[row][col] * charToInt(plaintext.charAt(i + col));
                }
                cipher.append(intToChar(sum % MOD));
            }
        }
        return cipher.toString();
    }

    /* ===============================
       Decryption
       =============================== */

    static String decrypt(String cipherText, int[][] key, int n) {
        int[][] invKey = inverseMatrix(key, n);
        StringBuilder plain = new StringBuilder();

        for (int i = 0; i < cipherText.length(); i += n) {
            for (int row = 0; row < n; row++) {
                int sum = 0;
                for (int col = 0; col < n; col++) {
                    sum += invKey[row][col] * charToInt(cipherText.charAt(i + col));
                }
                plain.append(intToChar(sum % MOD));
            }
        }
        return plain.toString();
    }

    /* ===============================
       Main
       =============================== */

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter key matrix dimension (N): ");
        int n = sc.nextInt();

        if (n < 2) {
            System.out.println("Matrix size must be at least 2x2.");
            return;
        }

        int[][] key = new int[n][n];

        System.out.println("Enter key matrix values (0–25):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                key[i][j] = sc.nextInt();
            }
        }
        if (!isValidKey(key, n)) {
            System.out.println("Error: This matrix cannot be used for Hill Cipher because it is not invertible modulo 26.");
            return; // Stop the program before it crashes during decryption
        }

        sc.nextLine();

        System.out.print("Enter plaintext: ");
        String plaintext = sc.nextLine();

        String cipher = encrypt(plaintext, key, n);
        System.out.println("Encrypted Text: " + cipher);

        String decrypted = decrypt(cipher, key, n);
        System.out.println("Decrypted Text: " + decrypted);

        sc.close();
    }
}
