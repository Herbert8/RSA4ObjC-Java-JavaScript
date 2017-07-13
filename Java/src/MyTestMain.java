

import java.io.File;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;


/**
 * Created by bahb on 2017/7/12.
 */
public class MyTestMain {

    /**
     * String to hold the name of the private key file.
     */
    public static final String PRIVATE_KEY_FILE = "../Certification/rsa_1024_priv.pem";

    /**
     * String to hold name of the public key file.
     */
    public static final String PUBLIC_KEY_FILE = "../Certification/rsa_1024_pub.pem";

    public static String encodeB64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] decodeB64(String base64Str) {
        return Base64.getDecoder().decode(base64Str);
    }

    public static void main(String[] args) {
        try {

//            // Check if the pair of keys are present else generate those.
//            if (!EncryptionUtil.areKeysPresent()) {
//                // Method generates a pair of keys using the RSA algorithm and stores it
//                // in their respective files
//                EncryptionUtil.generateKey();
//            }

            // 公钥
            File publicKeyFile = new File(PUBLIC_KEY_FILE);
            PublicKey publicKey = EncryptionUtil.loadPublicKeyFromPEMFile(publicKeyFile);

            // 私钥
            File privateKeyFile = new File(PRIVATE_KEY_FILE);
            final PrivateKey privateKey = EncryptionUtil.loadPrivateKeyFromPkcs1OrPkcs8EncodedPEMFile(privateKeyFile);

            byte[] cipherText = EncryptionUtil.encrypt("测试中文1", publicKey);
            String plainText = EncryptionUtil.decrypt(cipherText, privateKey);
            System.out.println("Encrypted: " + encodeB64(cipherText));
            System.out.println("Decrypted: [" + plainText + "]");

            cipherText = EncryptionUtil.encrypt("测试中文2", privateKey);
            plainText = EncryptionUtil.decrypt(cipherText, publicKey);
            System.out.println("Encrypted: " + encodeB64(cipherText));
            System.out.println("Decrypted: [" + plainText + "]");

            // 测试解密，输入其他语言加密的 Base64 字符串，然后进行解密
            final String encryptedDataWithB64Encoding = "atrcDtLqoCRU5N1wM7NyIy7tTnSqLRnv2YK7pIMY4M4N/1BBMg/LS4nMD06ngZ1+hcHfv4MGllGZPV7GyNf5RMEcaPa1uT2/X4c1+wmsPV8XmB7iiHGNE39dZG0fWD3rx6G/RRVjWVPFeZ25klwSANo5a9USEey+O6zpBaw3ygc=";
            System.out.println("Encrypted: " + encryptedDataWithB64Encoding);
            cipherText = decodeB64(encryptedDataWithB64Encoding);
            plainText = EncryptionUtil.decrypt(cipherText, privateKey);
            System.out.println("Decrypted: [" + plainText + "]");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
