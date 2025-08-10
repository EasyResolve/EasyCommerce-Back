package easycommerce.easycommerce.Autenticacion;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class TokenJWTConfig {
    private static final int ITERATIONS = 100000;
    private static final int KEY_LENGTH = 256;
    private static final byte[] SALT = generateSalt();

    // Clave generada una sola vez y almacenada en esta constante
    public static final SecretKey SECRET_KEY = generateSecretKey();

    private static SecretKey generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256 bits
        random.nextBytes(keyBytes);

        try {
            PBEKeySpec spec = new PBEKeySpec(Base64.getEncoder().encodeToString(keyBytes).toCharArray(), SALT, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] derivedKey = factory.generateSecret(spec).getEncoded();
            return new SecretKeySpec(derivedKey, "HmacSHA256");
        } catch (Exception e) {
            throw new RuntimeException("Error generando la clave secreta", e);
        }
    }

    private static byte[] generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    public final static String PREFIX_TOKEN = "Bearer ";
    public final static String HEADER_AUTHORIZATION = "Authorization";

}
