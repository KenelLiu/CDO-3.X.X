package com.cdo.util.codec;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.util.encoders.Hex;
public class SHA {
	/**
	 * 
	 * @param arg 待加密字符
	 * @return
	 */
	public static String encryptSHA1(String arg){		
		return encryptSHA1(arg.getBytes());
	}
	/**
	 * 
	 * @param arg 待加密数组
	 * @return
	 */
	public static String encryptSHA1(byte[] arg){
		Digest digest = new SHA1Digest();
		digest.update(arg, 0, arg.length);
		  // 创建保存摘要的字节数组
        byte[] cipherBytes = new byte[digest.getDigestSize()];
        digest.doFinal(cipherBytes, 0);
        String cipherText =Hex.toHexString(cipherBytes);
        return cipherText;
	}
}
