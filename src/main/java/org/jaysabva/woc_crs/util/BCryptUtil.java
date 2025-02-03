package org.jaysabva.woc_crs.util;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptUtil {

    public static String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt(12);

        String hashedPassword = BCrypt.hashpw(plainPassword, salt);

        return hashedPassword;
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
