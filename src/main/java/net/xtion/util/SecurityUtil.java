package net.xtion.util;

import com.google.common.base.Strings;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SecurityUtil {

    public static String md5(String sourceStr,int bit){
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte[] b = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0){
                    i += 256;
                }
                if (i < 16){
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            if(bit == 32){
                return buf.toString();
            }else{
                return buf.toString().substring(8, 24);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * hmacSha256加密
     * @param signatureMethod
     * @param secretKey
     * @param source
     * @return
     */
    public static byte[] hmacSha256(String signatureMethod,String secretKey,String source){
        Mac hmacSha256 = null;
        try {
            hmacSha256 = Mac.getInstance(signatureMethod);
            SecretKeySpec secKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), signatureMethod);
            hmacSha256.init(secKey);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No such algorithm: " + e.getMessage());
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key: " + e.getMessage());
        }
        byte[] hash = hmacSha256.doFinal(source.getBytes(StandardCharsets.UTF_8));
        return hash;
    }

    /**
     * 使用标准URL Encode编码。注意和JDK默认的不同，空格被编码为%20而不是+。
     *
     * @param s String字符串
     * @return URL编码后的字符串
     */
    public static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("UTF-8 encoding not supported!");
        }
    }

    /**
     * 使用标准URL Encode解码。注意和JDK默认的不同，空格被编码为%20而不是+。
     *
     * @param s String字符串
     * @return URL编码后的字符串
     */
    public static String urlDecode(String s) {
        try {
            return URLDecoder.decode(s.replaceAll("%20", "\\+"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("UTF-8 encoding not supported!");
        }
    }

    /**
     * 加密运算
     * @param accessKey
     * @param secretKey
     * @param method
     * @param host
     * @param uri
     * @param timestamp
     * @param signatureVersion
     * @param signatureMethod
     * @param params
     * @return
     */
    public static String createSignature(String accessKey, String secretKey, String method, String host, String uri,String timestamp,
                                  String signatureVersion,String signatureMethod, Map<String, String> params){

        //拼接url数据
        StringBuffer sb = new StringBuffer();
        sb.append(method.toUpperCase()).append('\n') // GET
                .append(host.toLowerCase()).append('\n') // Host
                .append(uri).append('\n'); // /path
        params.remove("Signature");
        params.put("Access-Key", accessKey);
        params.put("Signature-Version", signatureVersion);
        params.put("Signature-Method", signatureMethod);
        params.put("Timestamp", timestamp);

        //拼接path参数
        SortedMap<String, String> map = new TreeMap<>(params);//利用SortedMap对参数按照ASCII码的顺序对参数名进行排序
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append('=').append(SecurityUtil.urlEncode(Strings.nullToEmpty(value))).append('&');//参数需要URI 编码
        }
        sb.deleteCharAt(sb.length() - 1);// remove last '&'

        //hmacSha256加密
        byte[] hash = SecurityUtil.hmacSha256(signatureMethod,secretKey,sb.toString());
        String actualSign = SecurityUtil.urlEncode(Base64.getEncoder().encodeToString(hash));

        return actualSign;
    }


}
