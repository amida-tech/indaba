package com.ocs.common.db;

import com.ocs.util.StringUtils;


/**
 * encrypt and decrypt, encode and decode
 * 
 * @author cuiweibing
 * @date 2012-1-3
 */
public class EncryptUtil {
	
//	private static BASE64Decoder encoder = new BASE64Decoder();
//	private static BASE64Encoder decoder = new BASE64Encoder();
//	/**
//	 * base64 decode
//	 * @param s
//	 * @return
//	 */
//	public static byte[] getFromBASE64(String s) {
//		if (s == null) {
//			return null;
//		}
//		try {
//			byte[] b = encoder.decodeBuffer(s);
//			return b;
//		} catch (Exception e) {
//			return null;
//		}
//	}
//	/**
//	 * base64 encode
//	 * @param s
//	 * @return
//	 */
//	public static String toBASE64(byte[] s) {
//		if (s == null) {
//			return null;
//		}
//
//		try {
//			String b = decoder.encode(s);
//			return b;
//		} catch (Exception e) {
//			return null;
//		}
//	}
	
	/**
	 * binary to hex
	 * @param b
	 * @return
	 */
	private static String binaryToHex(byte[] b){
		StringBuffer sb = new StringBuffer(64);
		for (int i = 0; i < b.length; i++) {
			int v = (int) b[i];
			v = v < 0 ? 0x100 + v : v;
			String cc = Integer.toHexString(v);
			if (cc.length() == 1)
				sb.append('0');
			sb.append(cc);
		}

		return sb.toString().toUpperCase();
	}
	
	/**
	 * hex to binary
	 * @param hex
	 * @return
	 */
	private static byte[] hexToBinary(String hex) {
		if (hex == null || hex.length() < 1)
			return new byte[0];

		int len = hex.length() / 2;
		byte[] result = new byte[len];
		len *= 2;

		for (int index = 0; index < len; index+=2) {
			String s = hex.substring(index, index + 2);
			int b = Integer.parseInt(s, 16);
			result[index / 2] = (byte) b;
//			index++;
		}

		return result;
	}
	
//	/**
//	 * encode str by charsetName
//	 * @param str
//	 * @param charsetName
//	 * @return
//	 */
//	private static byte[] getBytesForString(String str,String charsetName){
//		if(str==null || charsetName==null){
//			return null;
//		}
//		byte[] result=null;
//		try {
//			result=str.getBytes(charsetName);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		return result;
//	}
//	
//	/**
//	 * encode by UTF-8
//	 * @param str
//	 * @param charsetName
//	 * @return
//	 */
//	private static byte[] getBytesForStringWithUTF8(String str){
//		return getBytesForString(str,"UTF-8");
//	}
	
	/**
	 * rot 13
	 * @param str
	 * @return
	 */
	private static String rot13(String str){
		if(str == null || str.length() == 0){
			return str;
		}
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			int chr = str.charAt(i);
			// convert char if required
			if ((chr >= 'A') && (chr <= 'Z')) {
				chr += 13;
				if (chr > 'Z'){
					chr -= 26;
				}
			} else if ((chr >= 'a') && (chr <= 'z')) {
				chr += 13;
				if (chr > 'z'){
					chr -= 26;
				}
			} else if ((chr >= '0') && (chr <= '9')) {
				chr += 5;
				if (chr > '9'){
					chr -= 10;
				}
			}
			result.append((char) chr);
		}
		
		return result.toString();
	}
	
	/**
	 * encode with rot13
	 * @param str
	 * @return
	 */
	public static String encode(String str){
		if(StringUtils.isEmpty(str)){
			return null;
		}
		String middle = rot13(str);
		byte[] src = middle.getBytes();
		return binaryToHex(src);
	}
	
	/**
	 * decode with rot13
	 * @param str
	 * @return
	 */
	public static String decode(String str){
		if(StringUtils.isEmpty(str)){
			return null;
		}
		byte[] src = hexToBinary(str);
		String middle = new String(src);
		return rot13(middle);
	}
	
	public static void main(String[] v) throws Exception {
		String email = "cuiweibing.1234@sohu.com";
		String ret = EncryptUtil.encode(email);
		//System.out.println("EncryptUtil encode: "+ret);
		//System.out.println("EncryptUtil decode: "+EncryptUtil.decode(ret));
        
        System.out.println("End %s's assignment \"%s\" on \"%s\"".replaceAll("\"", "\\\\\""));
	}
}

