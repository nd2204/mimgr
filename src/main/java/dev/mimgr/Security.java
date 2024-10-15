package dev.mimgr;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Security {
  public static String hash_string(String input) {
    try {
      // Create a MessageDigest instance for SHA-256
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      // Hash the input string
      byte[] hashBytes = digest.digest(input.getBytes());
      // Convert the byte array to a hexadecimal string
      StringBuilder hexString = new StringBuilder();
      for (byte b : hashBytes) {
        String hex = Integer.toHexString(0xff & b);
        if (hex.length() == 1) hexString.append('0');
        hexString.append(hex);
      }
      return hexString.toString();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  public static String generate_salt(int length) {
    SecureRandom sr = new SecureRandom();
    byte[] salt = new byte[length];

    sr.nextBytes(salt);
    return Base64.getEncoder().encodeToString(salt);
  }
}
