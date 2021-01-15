package com.xy.lib.utils;

import android.util.Base64;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author XieYan
 * @date 2020/6/19 10:02
 *
 * 1 AES是对称加密算法
 *      对称加密的概念，百度百科是这样解释的：在对称加密算法中，数据发信方将明文（原始数据）和加密密钥一起经过特殊加密算法处理后，
 *      使其变成复杂的加密密文发送出去。收信方收到密文后，若想解读原文，则需要使用加密用过的密钥及相同算法的逆算法对密文进行解密，
 *      才能使其恢复成可读明文。在对称加密算法中，使用的密钥只有一个，发收信双方都使用这个密钥对数据进行加密和解密，
 *      这就要求解密方事先必须知道加密密钥。关键点有两个：
 *      1、加密方、解密方使用同一个密钥。
 *      2、加密算法与解密算法互为逆算法。举例说明：123456-->234567的加密密钥就是1，加密算法是每位+；234567-->123456的解密密钥也是1，解密算法是每位-；
 *      其中加密算法（+）和解密算法（-）互为逆算法，这种加密算法就称作对称加密。
 *
 * 2、AES属于块加密
 *      AES在加密前需要把明文分为固定长度的若干块，然后对每块明文进行加密，最后再拼成一个完成的加密字符串。
 *      块加密要填充满最后一块，加密方、解密方要使用相同的padding填充方式。
 *
 * 3、AES密钥、初始向量、加密模式、填充方式
 *      我们常说的AES-128、AES-192、AES-256三种加密方式，数字表示的是密钥长度，比如AES-128说的是密钥是128位，也就是16个字节长度的字符串。
 *      JDK目前只支持AES-128加密，也就是传入的密钥必须是长度为16的字符串。
 *      初始向量Iv(Initialization Vector)，使用除ECB以外的其他加密模式均需要传入一个初始向量，其大小与块大小相等，AES块大小是128bit，
 *      所以Iv的长度是16字节，初始向量可以加强算法强度。
 *
 *      加密模式（Cipher Mode）有CBC、ECB、CTR、OFB、CFB五种。
 *
 *      填充方式（Padding）决定了最后的一个块需要填充的内容，填充方式有PKCS5Padding、PKCS7Padding、NOPADDING三种，但是JDK只提供了PKCS5Padding、NOPADDING两种，
 *      填充方式为PKCS5Padding时，最后一个块需要填充χ个字节，填充的值就是χ；填充方式为NOPADDING时，最后的一个块填充的内容由程序员自己决定，通常填充0。

 */
public class XAesUtils {

    private static final Charset UTF8 = StandardCharsets.UTF_8;
    private static final String AES = "AES";
    private static final String AES_CBC_PKCS5_PADDING = "AES/CBC/PKCS5Padding";
    private static final String AES_CBC_NO_PADDING = "AES/CBC/NoPadding";


    /**
     * JDK只支持AES-128加密，也就是密钥长度必须是128bit；
     * 参数为密钥key，key的长度小于16字符时用"0"补充，key长度大于16字符时截取前16位
     **/
    private static SecretKeySpec create128BitsKey(String key) {
        if (key == null) {
            key = "";
        }
        byte[] data = null;
        StringBuffer buffer = new StringBuffer(16);
        buffer.append(key);
        //小于16后面补0
        while (buffer.length() < 16) {
            buffer.append("\0");
        }
        //大于16，截取前16个字符
        if (buffer.length() > 16) {
            buffer.setLength(16);
        }
        data = buffer.toString().getBytes(UTF8);
        return new SecretKeySpec(data, AES);
    }

    /**
     * 创建128位的偏移量，iv的长度小于16时后面补0，大于16，截取前16个字符;
     *
     * @param iv
     * @return
     */
    private static IvParameterSpec create128BitsIV(String iv) {
        if (iv == null) {
            iv = "";
        }
        byte[] data = null;
        StringBuffer buffer = new StringBuffer(16);
        buffer.append(iv);
        while (buffer.length() < 16) {
            buffer.append("\0");
        }
        if (buffer.length() > 16) {
            buffer.setLength(16);
        }
        data = buffer.toString().getBytes(UTF8);
        return new IvParameterSpec(data);
    }

    private static String byte2String(byte[] data) {
        try {

            int length = data.length;
            for (int i = data.length - 1; i > 0; i--) {
                if (data[i] != 0) {
                    length = i + 1;
                    break;
                }
            }

            return new String(data, 0, length, UTF8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static class AesCbcPkcs5Padding {
        /**
         * 填充方式为Pkcs5Padding时，最后一个块需要填充χ个字节，填充的值就是χ，也就是填充内容由JDK确定
         *
         * @param srcContent
         * @param aesKey
         * @param aesIv
         * @return
         */
        public static String aesCbcPkcs5PaddingEncrypt(String srcContent, String aesKey, String aesIv) {
            SecretKeySpec key = create128BitsKey(aesKey);
            IvParameterSpec ivParameterSpec = create128BitsIV(aesIv);
            try {
                Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
                cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
                byte[] encryptedContent = cipher.doFinal(srcContent.getBytes(UTF8));
                return new String(Base64.encode(encryptedContent, Base64.NO_WRAP));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 填充方式为Pkcs5Padding时解密
         *
         * @param encryptedContent
         * @param aesKey
         * @param aesIv
         * @return
         */
        public static String aesCbcPkcs5PaddingDecrypt(String encryptedContent, String aesKey, String aesIv) {
            SecretKeySpec key = create128BitsKey(aesKey);
            IvParameterSpec ivParameterSpec = create128BitsIV(aesIv);
            try {
                Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
                cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
                byte[] decryptedContent = cipher.doFinal(Base64.decode(encryptedContent, Base64.NO_WRAP));
                return new String(decryptedContent, UTF8);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class AesCbcNoPadding {
        /**
         * 填充方式为NoPadding时，最后一个块的填充内容由程序员确定，通常为0.
         * AES/CBC/NoPadding加密的明文长度必须是16的整数倍，明文长度不满足16时，程序员要扩充到16的整数倍
         *
         * @param srcContent
         * @param aesKey
         * @param aesIV
         * @return
         */
        public static String aesCbcNoPaddingEncrypt(String srcContent, String aesKey, String aesIV) {
            try {
                //加密的数据长度不是16的整数倍时，原始数据后面补0，直到长度满足16的整数倍
                byte[] srcContentByte = srcContent.getBytes(UTF8);
                int len = srcContentByte.length;
                SecretKeySpec skeySpec = create128BitsKey(aesKey);
                //使用CBC模式，需要一个初始向量iv，可增加加密算法的强度
                IvParameterSpec iv = create128BitsIV(aesIV);
                Cipher cipher = null;
                //算法/模式/补码方式
                cipher = Cipher.getInstance(AES_CBC_NO_PADDING);

                //获取块的大小  16字节
                int blockSize = cipher.getBlockSize();
                //blockSize为2的幂次方，与运算可以代替取模提升效率
                int rem = len & (blockSize - 1);
                if (rem != 0) {
                    len = len + (blockSize - rem);
                }
                byte[] result = new byte[len];
                //扩充到16的整数倍
                System.arraycopy(srcContentByte, 0, result, 0, srcContentByte.length);
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
                byte[] encrypted = cipher.doFinal(result);
                return new String(Base64.encode(encrypted, Base64.NO_WRAP));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 填充方式为NoPadding 解密
         *
         * @param encryptedContent
         * @param aesKey
         * @param aesIV
         * @return
         */
        public static String aesCbcNoPaddingDecrypt(String encryptedContent, String aesKey, String aesIV) {
            SecretKeySpec skeySpec = create128BitsKey(aesKey);
            IvParameterSpec iv = create128BitsIV(aesIV);
            try {
                Cipher cipher = Cipher.getInstance(AES_CBC_NO_PADDING);
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
                byte[] decryptContent = cipher.doFinal(Base64.decode(encryptedContent, Base64.NO_WRAP));
                return byte2String(decryptContent);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
