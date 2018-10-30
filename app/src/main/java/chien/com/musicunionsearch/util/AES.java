package chien.com.musicunionsearch.util;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AES {

    private SecretKeySpec secretKey;

    public AES(String key) {
        secretKey = new SecretKeySpec(key.getBytes(), "AES");
    }

    public String encrypt(String src) {

        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] buffer = cipher.doFinal(src.getBytes());

        } catch (NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException
                | InvalidKeyException | NoSuchAlgorithmException ignored) {
        }
        return null;
    }
}
