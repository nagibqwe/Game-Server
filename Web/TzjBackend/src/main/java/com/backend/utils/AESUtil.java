package com.backend.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.Key;
import java.util.Base64;

/**
 * @explain: desc
 * @time Created on 2019/9/10 15:29.
 * @author: tc
 */
public class AESUtil {
	public static final String CHARSET = "UTF-8";
	public static final String AES_ALGORITHM = "AES";
	public static final String ivParameter = "1234567890abcdef"; // 偏移量,可自行修改
	public static final String AES_CIPHER = "AES/CBC/PKCS5Padding";

	/**
	 * 加密，加密后返回编码
	 * @param context
	 * @param key
	 * @return
	 */
	public static String encrypt(String context, String key) {
		try {
			Cipher cipher = Cipher.getInstance(AES_CIPHER);
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, getKey(key), iv);
			byte[] result = cipher.doFinal(context.getBytes(CHARSET));
			return Base64.getEncoder().encodeToString(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 解密
	 * @param context
	 * @param key
	 * @return
	 */
	public static String decrypt(String context, String key) {
		Cipher cipher;
		try {
			// 这里有多种填充方式
			cipher = Cipher.getInstance(AES_CIPHER);
			// 初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
			IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
			cipher.init(Cipher.DECRYPT_MODE, getKey(key), iv);
			byte[] result = cipher.doFinal(Base64.getDecoder().decode(context));
			return new String(result, CHARSET);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	private static Key getKey(String key) {
		byte[] raw = key.getBytes();
		return new SecretKeySpec(raw, AES_ALGORITHM);
	}

	public static String decodeURIComponent(String s) {
		if (s == null) {
			return null;
		}
		String result;
		try {
			result = URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			result = s;
		}
		return result;
	}

	public static void main(String[] args) {
		String url = "0CyTqGAzJw2aoTfrKtDx3MAk%2BRnHmRUEnWz%2BcB46O82WdL6o99%2FO4wbImefvOzKIIja6yABxc8jIgKDl8kuOgs%2Fch8O1ZAZsrP%2BsOlAa4HrYDD%2B7U7iXfABPWmw%2Fl3jCDquGGCLjABKZUM8Lj3bvmw%3D%3D";
		String data = decrypt(decodeURIComponent(url), "u;8iI34%$#34df,/");
		System.out.println(data);
	}
}
