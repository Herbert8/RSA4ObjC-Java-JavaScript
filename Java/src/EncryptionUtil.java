
import java.io.*;

import java.security.*;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;


import sun.misc.BASE64Decoder;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateCrtKeySpec;




/**
 * @author JavaDigest
 *
 */
public class EncryptionUtil {

    /**
     * String to hold name of the encryption algorithm.
     */
    public static final String ALGORITHM = "RSA";


    /**
     * Generate key which contains a pair of private and public key using 1024
     * bytes. Store the set of keys in Prvate.key and Public.key files.
     *
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws FileNotFoundException
     */
//    public static void generateKey() {
//        try {
//            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
//            keyGen.initialize(1024);
//            final KeyPair key = keyGen.generateKeyPair();
//
//            File privateKeyFile = new File(PRIVATE_KEY_FILE);
//            File publicKeyFile = new File(PUBLIC_KEY_FILE);
//
//            // Create files to store public and private key
//            if (privateKeyFile.getParentFile() != null) {
//                privateKeyFile.getParentFile().mkdirs();
//            }
//            privateKeyFile.createNewFile();
//
//            if (publicKeyFile.getParentFile() != null) {
//                publicKeyFile.getParentFile().mkdirs();
//            }
//            publicKeyFile.createNewFile();
//
//            // Saving the Public key in a file
//            ObjectOutputStream publicKeyOS = new ObjectOutputStream(
//                    new FileOutputStream(publicKeyFile));
//            publicKeyOS.writeObject(key.getPublic());
//            publicKeyOS.close();
//
//            // Saving the Private key in a file
//            ObjectOutputStream privateKeyOS = new ObjectOutputStream(
//                    new FileOutputStream(privateKeyFile));
//            privateKeyOS.writeObject(key.getPrivate());
//            privateKeyOS.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    /**
     * The method checks if the pair of public and private key has been generated.
     *
     * @return flag indicating if the pair of keys were generated.
     */
//    public static boolean areKeysPresent() {
//
//        File privateKey = new File(PRIVATE_KEY_FILE);
//        File publicKey = new File(PUBLIC_KEY_FILE);
//
//        if (privateKey.exists() && publicKey.exists()) {
//            return true;
//        }
//        return false;
//    }

    /**
     * Encrypt the plain text using public key.
     *
     * @param text
     *          : original plain text
     * @param key
     *          :The public key
     * @return Encrypted text
     * @throws java.lang.Exception
     */
    public static byte[] encrypt(String text, Key key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    /**
     * Decrypt text using private key.
     *
     * @param text
     *          :encrypted text
     * @param key
     *          :The private key
     * @return plain text
     * @throws java.lang.Exception
     */
    public static String decrypt(byte[] text, Key key) {
        byte[] dectyptedText = null;
        try {
            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance(ALGORITHM);

            // decrypt the text using the private key
            cipher.init(Cipher.DECRYPT_MODE, key);

            dectyptedText = cipher.doFinal(text);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new String(dectyptedText);
    }



    public static PrivateKey loadPrivateKeyFromPkcs1OrPkcs8EncodedPEMString(String pemStr) throws GeneralSecurityException, IOException {
        // PKCS#8 format
        final String PEM_PRIVATE_START = "-----BEGIN PRIVATE KEY-----";
        final String PEM_PRIVATE_END = "-----END PRIVATE KEY-----";

        // PKCS#1 format
        final String PEM_RSA_PRIVATE_START = "-----BEGIN RSA PRIVATE KEY-----";
        final String PEM_RSA_PRIVATE_END = "-----END RSA PRIVATE KEY-----";

        String privateKeyPem = pemStr;

        if (privateKeyPem.contains(PEM_PRIVATE_START)) { // PKCS#8 format
            privateKeyPem = privateKeyPem.replace(PEM_PRIVATE_START, "").replace(PEM_PRIVATE_END, "");
            privateKeyPem = privateKeyPem.replaceAll("\\s", "");

            byte[] pkcs8EncodedKey = Base64.getDecoder().decode(privateKeyPem);

            KeyFactory factory = KeyFactory.getInstance("RSA");
            return factory.generatePrivate(new PKCS8EncodedKeySpec(pkcs8EncodedKey));

        } else if (privateKeyPem.contains(PEM_RSA_PRIVATE_START)) {  // PKCS#1 format

            privateKeyPem = privateKeyPem.replace(PEM_RSA_PRIVATE_START, "").replace(PEM_RSA_PRIVATE_END, "");
            privateKeyPem = privateKeyPem.replaceAll("\\s", "");

            DerInputStream derReader = new DerInputStream(Base64.getDecoder().decode(privateKeyPem));

            DerValue[] seq = derReader.getSequence(0);

            if (seq.length < 9) {
                throw new GeneralSecurityException("Could not parse a PKCS1 private key.");
            }

            // skip version seq[0];
            BigInteger modulus = seq[1].getBigInteger();
            BigInteger publicExp = seq[2].getBigInteger();
            BigInteger privateExp = seq[3].getBigInteger();
            BigInteger prime1 = seq[4].getBigInteger();
            BigInteger prime2 = seq[5].getBigInteger();
            BigInteger exp1 = seq[6].getBigInteger();
            BigInteger exp2 = seq[7].getBigInteger();
            BigInteger crtCoef = seq[8].getBigInteger();

            RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1, exp2, crtCoef);

            KeyFactory factory = KeyFactory.getInstance("RSA");

            return factory.generatePrivate(keySpec);
        }

        throw new GeneralSecurityException("Not supported format of a private key");
    }

    public static PrivateKey loadPrivateKeyFromPkcs1OrPkcs8EncodedPEMFile(File pemFileName) throws GeneralSecurityException, IOException {

        Path path = Paths.get(pemFileName.getAbsolutePath());

        String privateKeyPem = new String(Files.readAllBytes(path));

        return loadPrivateKeyFromPkcs1OrPkcs8EncodedPEMString(privateKeyPem);

    }


    public static PublicKey loadPublicKeyFromPEMString(String pemString) throws NoSuchAlgorithmException,
            InvalidKeySpecException, IOException {

        String temp = pemString;
        String publicKeyPEM = temp.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");

        BASE64Decoder b64=new BASE64Decoder();
        byte[] decoded = b64.decodeBuffer(publicKeyPEM);

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance(ALGORITHM);
        return kf.generatePublic(spec);
    }


    public static PublicKey loadPublicKeyFromPEMFile(File pemFileName) throws NoSuchAlgorithmException,
            InvalidKeySpecException, IOException {
        Path path = Paths.get(pemFileName.getAbsolutePath());
        String publicKeyPEM = new String(Files.readAllBytes(path));

        return loadPublicKeyFromPEMString(publicKeyPEM);
    }


}