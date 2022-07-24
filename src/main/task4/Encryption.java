package main.task4;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static main.task4.EditFile.*;

public class Encryption {
    private static final String way = "src\\main\\task4\\auth.json";
    private static final byte[] salt = "12345678".getBytes();
    private static final int iterationCount = 40000;
    private static final int keyLength = 128;


    public static void main(String[] args) throws GeneralSecurityException {
        JSONObject js = readJSON(way);
        String login = (String) js.get("login");
        String password = (String) js.get("password");

        String keyWord = RandomStringUtils.randomAlphanumeric(6);
        SecretKeySpec key = createSecretKey(keyWord.toCharArray(),
                salt, iterationCount, keyLength);


        System.out.println("password: " + password);
        /*a. Шифрует пароль с использованием ключа, который также вшифровывается в результат шифрования;**/
        String encryptedPassword = encrypt(password, key);
        System.out.println("Encrypted password: " + encryptedPassword);

        /*d. Без внешне передаваемого ключа шифрования.
        Ключ шифрования должен генерироваться самой программой и становится частью шифра, но только так, чтобы его нельзя было легко вычленить.*/
        String encryptedPassKey = base64Encode((encryptedPassword + " " + keyWord).getBytes());
        System.out.println("Encrypted Pass+key: " + encryptedPassKey);

        /*b. Создаёт новый файл с шифрованным паролем и удаляет старый файл с открытым паролем;*/
        reFile(login, encryptedPassKey, way);

        /*c. Другой метод того же класса должен уметь расшифровать таким образом зашифрованный пароль из файла с шифрованным паролем и
        печатать расшифрованный пароль в консоль, исходный файл с расшифрованным паролем создавать не надо;*/
        //decryptPassKey(password);
    }

    private static SecretKeySpec createSecretKey(char[] password, byte[] salt, int iterationCount, int keyLength) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec keySpec = new PBEKeySpec(password, salt, iterationCount, keyLength);
        SecretKey keyTmp = keyFactory.generateSecret(keySpec);
        return new SecretKeySpec(keyTmp.getEncoded(), "AES");
    }

    private static String encrypt(String property, SecretKeySpec key) throws GeneralSecurityException {
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key);
        AlgorithmParameters parameters = pbeCipher.getParameters();
        IvParameterSpec ivParameterSpec = parameters.getParameterSpec(IvParameterSpec.class);
        byte[] cryptoText = pbeCipher.doFinal(property.getBytes(StandardCharsets.UTF_8));
        byte[] iv = ivParameterSpec.getIV();
        return base64Encode(iv) + ":" + base64Encode(cryptoText);
    }

    private static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    private static String decrypt(String string, SecretKeySpec key) throws GeneralSecurityException {
        String iv = string.split(":")[0];
        String property = string.split(":")[1];
        Cipher pbeCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(base64Decode(iv)));
        return new String(pbeCipher.doFinal(base64Decode(property)), StandardCharsets.UTF_8);
    }

    private static void decryptPassKey(String encryptedPassKey) throws GeneralSecurityException {
        String[] enc = new String(base64Decode(encryptedPassKey), StandardCharsets.UTF_8).split(" ");
        String decryptedPassword = decrypt(enc[0], createSecretKey(enc[1].toCharArray(), salt, iterationCount, keyLength));
        System.out.println("Decrypted password: " + decryptedPassword);
    }

    private static byte[] base64Decode(String property) {
        return Base64.getDecoder().decode(property);
    }
}
