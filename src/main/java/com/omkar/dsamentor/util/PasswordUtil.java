package com.omkar.dsamentor.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordUtil {
    public static String hashPassword(String password)
    {

        Argon2 argon2=Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

        char[] passwordArray=password.toCharArray();

        try {
            return argon2.hash(3, 65536, 1, passwordArray);
        } finally {
            argon2.wipeArray(passwordArray);
        }
    }

    @SuppressWarnings("deprecation")
    public static boolean verifyPassword(String password, String storedHash)
    {
        Argon2 argon2=Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

        char[] passwordArray=password.toCharArray();

        try {
            return argon2.verify(storedHash, password);
        } finally {
            argon2.wipeArray(passwordArray);
        }
    }
}
