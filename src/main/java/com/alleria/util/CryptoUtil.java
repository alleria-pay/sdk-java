package com.alleria.util;

import org.apache.commons.codec.Charsets;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created on 2017/6/6.
 */
public class CryptoUtil {

    public static final String MD5withRSA = "MD5withRSA";
    public static final String RSA = "RSA";
    public static final String AES = "AES";
    public static final int LENGTH_2048 = 2048;
    public static final String RSA_PADDING = "RSA/ECB/PKCS1Padding";
    public static final String AES_PADDING = "AES/ECB/PKCS5Padding";
    private static final Logger LOGGER = LoggerFactory.getLogger(CryptoUtil.class);

    /**
     * 获得公钥
     *
     * @return
     * @throws Exception
     */
    public static PublicKey getPublicKey(String pubKeyString) {
        if (StringUtils.isEmpty(pubKeyString)) {
            return null;
        }
        try {
            byte[] keyBytes = Base64.decodeBase64(pubKeyString);

            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return keyFactory.generatePublic(spec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("公钥格式错误", e);
        }
    }

    /**
     * 获得私钥
     *
     * @return
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String privateKey) {
        if (StringUtils.isEmpty(privateKey)) {
            return null;
        }
        try {
            byte[] keyBytes = Base64.decodeBase64(privateKey);

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("私钥格式错误", e);
        }
    }

    /**
     * 验证签名
     *
     * @param dataString
     * @param signatureString
     * @param publicKey
     * @return
     */
    public static boolean verifyData(String dataString, String signatureString, PublicKey publicKey, String algorithm) {
        if (StringUtils.isEmpty(dataString) || StringUtils.isEmpty(signatureString)) {
            throw new RuntimeException("验证签名参数缺失");
        }
        try {
            byte[] signatureBytes = Base64.decodeBase64(signatureString);
            Signature signature = Signature.getInstance(algorithm);
            signature.initVerify(publicKey);
            signature.update(dataString.getBytes(Charsets.UTF_8));

            return signature.verify(signatureBytes);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            LOGGER.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 签名
     *
     * @param privateKey
     * @param data
     * @param algorithm
     * @return
     */
    public static String sign(PrivateKey privateKey, String data, String algorithm) {
        //获取签名对象
        try {
            byte[] plainText = data.getBytes(Charsets.UTF_8);
            Signature signer = Signature.getInstance(algorithm);

            signer.initSign(privateKey);
            signer.update(plainText);
            byte[] signature = signer.sign();

            return Base64.encodeBase64String(signature);
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("签名失败", e);
        }

    }

    public static String RSAEncryptBase64(byte[] plainBytes, PublicKey publicKey) {
        return Base64.encodeBase64String(RSAEncrypt(plainBytes, publicKey, LENGTH_2048, RSA_PADDING));
    }

    /**
     * RSA加密
     *
     * @param plainBytes      明文字节数组
     * @param publicKey       公钥
     * @param keyLength       密钥bit长度
     * @param cipherAlgorithm 加解密算法，一般为RSA/ECB/PKCS1Padding
     * @return 加密后字节数组，不经base64编码
     */
    public static byte[] RSAEncrypt(byte[] plainBytes, PublicKey publicKey, int keyLength, String cipherAlgorithm) {
        int reserveSize = 11; //padding填充字节数，预留11字节
        int keyByteSize = keyLength / 8; // 密钥字节数
        int encryptBlockSize = keyByteSize - reserveSize; // 加密块大小=密钥字节数-padding填充字节数
        int nBlock = plainBytes.length / encryptBlockSize;// 计算分段加密的block数，向上取整
        if ((plainBytes.length % encryptBlockSize) != 0) { // 余数非0，block数再加1
            nBlock += 1;
        }

        try {
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // 输出buffer，大小为nBlock个keyByteSize
            ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * keyByteSize);
            // 分段加密
            for (int offset = 0; offset < plainBytes.length; offset += encryptBlockSize) {
                int inputLen = plainBytes.length - offset;
                if (inputLen > encryptBlockSize) {
                    inputLen = encryptBlockSize;
                }

                // 得到分段加密结果
                byte[] encryptedBlock = cipher.doFinal(plainBytes, offset, inputLen);
                // 追加结果到输出buffer中
                outbuf.write(encryptedBlock);
            }

            outbuf.flush();
            outbuf.close();
            return outbuf.toByteArray();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(String.format("没有[%s]此类加密算法", cipherAlgorithm), e);
        } catch (NoSuchPaddingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(String.format("没有[%s]此类填充模式", cipherAlgorithm), e);
        } catch (InvalidKeyException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("无效密钥", e);
        } catch (IllegalBlockSizeException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("加密块大小不合法", e);
        } catch (BadPaddingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("错误填充模式", e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("字节输出流异常", e);
        }
    }

    /**
     * RSA解密
     *
     * @param encryptedBytes  加密后字节数组
     * @param privateKey      私钥
     * @param keyLength       密钥bit长度
     * @param cipherAlgorithm 加解密算法，一般为RSA/ECB/PKCS1Padding
     * @return 解密后字节数组，不经base64编码
     * @throws RuntimeException
     */
    public static byte[] RSADecrypt(byte[] encryptedBytes, PrivateKey privateKey, int keyLength, String cipherAlgorithm)
            throws RuntimeException {
        int reserveSize = 11; //padding填充字节数，预留11字节
        int keyByteSize = keyLength / 8; // 密钥字节数
        int decryptBlockSize = keyByteSize - reserveSize; // 解密块大小=密钥字节数-padding填充字节数
        int nBlock = encryptedBytes.length / keyByteSize;// 计算分段解密的block数，理论上能整除

        try {
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // 输出buffer，大小为nBlock个decryptBlockSize
            ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * decryptBlockSize);
            // 分段解密
            for (int offset = 0; offset < encryptedBytes.length; offset += keyByteSize) {
                // block大小: decryptBlock 或 剩余字节数
                int inputLen = encryptedBytes.length - offset;
                if (inputLen > keyByteSize) {
                    inputLen = keyByteSize;
                }

                // 得到分段解密结果
                byte[] decryptedBlock = cipher.doFinal(encryptedBytes, offset, inputLen);
                // 追加结果到输出buffer中
                outbuf.write(decryptedBlock);
            }

            outbuf.flush();
            return outbuf.toByteArray();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(String.format("没有[%s]此类解密算法", cipherAlgorithm), e);
        } catch (NoSuchPaddingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(String.format("没有[%s]此类填充模式", cipherAlgorithm), e);
        } catch (InvalidKeyException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("无效密钥", e);
        } catch (IllegalBlockSizeException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("解密块大小不合法", e);
        } catch (BadPaddingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("错误填充模式", e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("字节输出流异常", e);
        }
    }

    public static String AESEncryptBase64(String plainText, byte[] keyBytes) {
        return Base64.encodeBase64String(AESEncrypt(plainText.getBytes(Charsets.UTF_8), keyBytes, AES, AES_PADDING, null));
    }

    /**
     * AES加密
     *
     * @param plainBytes      明文字节数组
     * @param keyBytes        密钥字节数组
     * @param keyAlgorithm    密钥算法
     * @param cipherAlgorithm 加解密算法
     * @param IV              随机向量
     * @return 加密后字节数组，不经base64编码
     * @throws RuntimeException
     */
    public static byte[] AESEncrypt(byte[] plainBytes, byte[] keyBytes, String keyAlgorithm, String cipherAlgorithm, String IV)
            throws RuntimeException {
        try {
            // AES密钥长度为128bit、192bit、256bit，默认为128bit
            if (keyBytes.length % 8 != 0 || keyBytes.length < 16 || keyBytes.length > 32) {
                throw new RuntimeException("AES密钥长度不合法");
            }

            Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            SecretKey secretKey = new SecretKeySpec(keyBytes, keyAlgorithm);
            if (!StringUtils.isEmpty(IV)) {
                IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            }

            return cipher.doFinal(plainBytes);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(String.format("没有[%s]此类加密算法", cipherAlgorithm), e);
        } catch (NoSuchPaddingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(String.format("没有[%s]此类填充模式", cipherAlgorithm), e);
        } catch (InvalidKeyException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("无效密钥", e);
        } catch (InvalidAlgorithmParameterException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("无效密钥参数", e);
        } catch (BadPaddingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("错误填充模式", e);
        } catch (IllegalBlockSizeException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("加密块大小不合法", e);
        }
    }

    /**
     * AES解密
     *
     * @param encryptedBytes  密文字节数组，不经base64编码
     * @param keyBytes        密钥字节数组
     * @param keyAlgorithm    密钥算法
     * @param cipherAlgorithm 加解密算法
     * @param IV              随机向量
     * @return 解密后字节数组
     * @throws RuntimeException
     */
    public static byte[] AESDecrypt(byte[] encryptedBytes, byte[] keyBytes, String keyAlgorithm, String cipherAlgorithm, String IV)
            throws RuntimeException {
        try {
            // AES密钥长度为128bit、192bit、256bit，默认为128bit
            if (keyBytes.length % 8 != 0 || keyBytes.length < 16 || keyBytes.length > 32) {
                throw new RuntimeException("AES密钥长度不合法");
            }

            Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            SecretKey secretKey = new SecretKeySpec(keyBytes, keyAlgorithm);
            if (!StringUtils.isEmpty(IV)) {
                IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes());
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
            }

            return cipher.doFinal(encryptedBytes);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(String.format("没有[%s]此类加密算法", cipherAlgorithm), e);
        } catch (NoSuchPaddingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(String.format("没有[%s]此类填充模式", cipherAlgorithm), e);
        } catch (InvalidKeyException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("无效密钥", e);
        } catch (InvalidAlgorithmParameterException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("无效密钥参数", e);
        } catch (BadPaddingException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("错误填充模式", e);
        } catch (IllegalBlockSizeException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException("解密块大小不合法", e);
        }
    }

    public static String[] generateKey(String algorithm, int length) {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException("不支持的算法名称：" + algorithm, ex);
        }

        SecureRandom secureRandom = new SecureRandom();
        keyPairGenerator.initialize(length, secureRandom);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();
        return new String[]{Base64.encodeBase64String(privateKey.getEncoded()), Base64.encodeBase64String(publicKey.getEncoded())};
    }

    public static byte[] initAESKey() {
        KeyGenerator kg;
        try {
            kg = KeyGenerator.getInstance(AES);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error(e.getMessage(), e);
            return new byte[0];
        }
        kg.init(128);
        SecretKey secretKey = kg.generateKey();
        return secretKey.getEncoded();
    }

}
