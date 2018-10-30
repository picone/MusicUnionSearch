package chien.com.musicunionsearch.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    public static String hash(String src) {
        StringBuilder ret = new StringBuilder();
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] buffer = digest.digest(src.getBytes());
            for (byte b: buffer) {
                int n = b & 0xff;
                String s = Integer.toHexString(n);
                if (s.length() == 1) {
                    ret.append("0");
                }
                ret.append(s);
            }
        } catch (NoSuchAlgorithmException ignored) {}
        return ret.toString();
    }
}
