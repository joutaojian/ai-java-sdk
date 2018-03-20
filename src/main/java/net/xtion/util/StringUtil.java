package net.xtion.util;

import com.google.common.base.Strings;

import java.util.Map;

/**
 * Created by Administrator on 2018/3/20.
 */
public class StringUtil {

    public static String changeGetUrlWithParam(String host,String uri, Map<String, String> params){

        StringBuffer url = new StringBuffer(host).append(uri).append("?");

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            url.append(key).append('=').append(SecurityUtil.urlEncode(Strings.nullToEmpty(value))).append('&');//参数需要URI 编码
        }
        url.deleteCharAt(url.length() - 1);// remove last '&'

        return url.toString();
    }

}
