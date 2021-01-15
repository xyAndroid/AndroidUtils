package com.xy.lib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author XieYan
 * @date 2020/7/27 14:14
 */
public class XEncryptUtils {

    /**
     * Return the hex string of MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of capital MD5 encryption
     */
    public static String getMD5(final String data) {
        if (data == null || data.length() == 0) return "";
        return getMD5(data.getBytes());
    }

    /**
     * Return the hex string of lowercase MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of lowercase MD5 encryption
     */
    public static String getMd5(final String data) {
        return getMD5(data).toLowerCase();
    }

    /**
     * Return the hex string of capital MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of capital MD5 encryption
     */
    public static String getMD5(final byte[] data) {
        return XConvertUtils.bytes2HexString(encryptMD5(data));
    }

    /**
     * Return the hex string of lowercase MD5 encryption.
     *
     * @param data The data.
     * @return the hex string of lowercase MD5 encryption
     */
    public static String getMd5(final byte[] data) {
        return getMD5(data).toLowerCase();
    }

    /**
     * Return the bytes of MD5 encryption.
     *
     * @param data The data.
     * @return the bytes of MD5 encryption
     */
    public static byte[] encryptMD5(final byte[] data) {
        return hashTemplate(data, "MD5");
    }

    /**
     * Return the hex string of file's capital MD5 encryption.
     *
     * @param filePath The path of file.
     * @return the hex string of file's capital MD5 encryption
     */
    public static String getMD5ByFile(final String filePath) {
        File file = XStringUtils.isSpace(filePath) ? null : new File(filePath);
        return getMD5ByFile(file);
    }

    /**
     * Return the hex string of file's lowercase MD5 encryption.
     *
     * @param filePath The path of file.
     * @return the hex string of file's lowercase MD5 encryption
     */
    public static String getMd5ByFile(final String filePath){
        return getMD5ByFile(filePath).toLowerCase();
    }

    /**
     * Return the hex string of file's capital MD5 encryption.
     *
     * @param file The file.
     * @return the hex string of file's capital MD5 encryption
     */
    public static String getMD5ByFile(final File file) {
        return XConvertUtils.bytes2HexString(encryptMD5File(file));
    }


    /**
     * Return the hex string of file's lowercase MD5 encryption.
     *
     * @param file The file.
     * @return the hex string of file's lowercase MD5 encryption
     */
    public static String getMd5ByFile(final File file) {
        return getMD5ByFile(file).toLowerCase();
    }

    /**
     * Return the bytes of file's MD5 encryption.
     *
     * @param file The file.
     * @return the bytes of file's MD5 encryption
     */
    public static byte[] encryptMD5File(final File file) {
        if (file == null) return null;
        FileInputStream fis = null;
        DigestInputStream digestInputStream;
        try {
            fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            digestInputStream = new DigestInputStream(fis, md);
            byte[] buffer = new byte[256 * 1024];
            while (true) {
                if (!(digestInputStream.read(buffer) > 0)) break;
            }
            md = digestInputStream.getMessageDigest();
            return md.digest();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Return the bytes of hash encryption.
     *
     * @param data      The data.
     * @param algorithm The name of hash encryption.
     * @return the bytes of hash encryption
     */
    static byte[] hashTemplate(final byte[] data, final String algorithm) {
        if (data == null || data.length <= 0) return null;
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO 增加常见加解密方法
}
