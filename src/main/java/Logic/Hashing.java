package Logic;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * <h1>Hashing Logic Class</h1>
 * Contains all of the logic that deals with SHA256 hashes and related functions
 *
 * @author Ethan O'Donnell : ejpo2@kent.ac.uk
 * @author Lia Wilkinson : lw517@kent.ac.uk
 * @version 0.1
 * @since 27/01/2021
 */
public class Hashing {
    //Constant Array to help convert bytes to hex
    private static final byte[] HEX_ARRAY = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
    private static final String STRING_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!$%^abcdefghijklmnopqrstuvwxyz";
    private static final Random rand = new Random();

    /**
     * Converts a Raw Byte Array into a base16 string representation
     * From SO Post: https://stackoverflow.com/a/9855338
     * @param bytes An array of bytes from MessageDigest
     * @return String of bytes in base16 format
     */
    public static String bytesToHex(byte[] bytes) {
        byte[] hexChars = new byte[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return (new String(hexChars, StandardCharsets.UTF_8)).toLowerCase();
    }

    /**
     * Given a string, compute the sha256 hash as a base16 string
     * @param input String to compute the hash of
     * @return Base 16 String representation of inputs sha256 hash
     */
    public static String sha256SumString (String input) {
        MessageDigest md;
        byte[] hashBytes = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            hashBytes = md.digest(input.getBytes());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            Error.showGenericErrorInGUI(e);
        }
        return bytesToHex(hashBytes);
    }

    /**
     * Given a number representing the size of the string you want to return generate a pseudo
     * random string. This function is NOT cryptographically secure
     * @param length Length of th string to return (Must be positive!)
     * @return A pseudo random string
     */
    public static String generatePseudoRandomString(int length){
        if (length < 0) {
            throw new IllegalArgumentException("Input is less than 0");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length ; i++) {
            int index = rand.nextInt(STRING_ALPHABET.length());
            sb.append(STRING_ALPHABET.charAt(index));
        }
        return  sb.toString();
    }
}
