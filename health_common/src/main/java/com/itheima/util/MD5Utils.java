package com.itheima.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
	/**
	 * 使用md5的算法进行加密
	 */
	public static String md5(String plainText) {
		byte[] secretBytes = null;
		try {
			secretBytes = MessageDigest.getInstance("md5").digest(
					plainText.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有md5这个算法！");
		}
		String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
		// 如果生成数字未满32位，需要前面补0
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}

	//测试md5 加密,不可逆,不可以解密,只能通过轮询的方式破解
    //bcrpt加密是可逆的,他和MD5使用方法注意区分
	public static void main(String[] args) {
		System.out.println(md5("1234"));
        System.out.println("---------------------------");
        /*
        使用bcrpt
        * 将salt随机并混入最终加密后的密码，验证时也无需单独提供之前的salt，
        * 从而无需单独处理salt问题加密后的格式一般为：
        * $2a$10$4pa4NmZoXgvKv0lwdiiGke.Ui1eMjZmfYrXa9F.CuK0EXTf9DFWaC
        */
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encode = encoder.encode("1234");//加密
        System.out.println(encode);


    }

}