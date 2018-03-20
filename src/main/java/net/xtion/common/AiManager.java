package net.xtion.common;

import com.google.common.collect.Maps;
import net.xtion.util.DateUtil;
import net.xtion.util.SecurityUtil;
import net.xtion.util.StringUtil;

import java.util.HashMap;

public class AiManager {

    private static AiManager instance = new AiManager();

    //常量数据
    private static final String[] commonFields = new String[]{
            "1.0", "GET", "POST", "2", "HmacSHA256",
            };
    private static final String INPUTTASK_URI = "/api/ai/task/tasks";
    private static final String GETBUSINESS_URI = "/api/ai/business/imageInfo/base_result";

    public static AiManager get(){
        return instance;
    }

    public static AiManager post(){
        return instance;
    }

    public static AiManager put(){
        return instance;
    }

    public static AiManager delete(){
        return instance;
    }

    private AiManager(){

    }

    /**
     * 插入任务，版本为1.0
     * @param auth
     * @param requestParams
     * @return
     */
    public String inputTask(Auth auth,String account, Object requestParams){

        //权限数据
        String host = auth.getHost();
        String accessKey = auth.getAccessKey();
        String secretKey = auth.getSecretKey();

        //基本数据
        String apiVersion = commonFields[0];
        String method = commonFields[2];
        String uri = INPUTTASK_URI;
        String timestamp = DateUtil.gmtNow();
        String signatureVersion = commonFields[3];
        String signatureMethod = commonFields[4];

        //混合数据
        String signature = SecurityUtil.createSignature(accessKey,secretKey,method,host,uri,timestamp,
                signatureVersion,signatureMethod,Maps.newHashMap());
        String url = new StringBuffer(host).append(uri).toString();

        //数据请求
        String response = null;
        try {
            response = HttpUtilManager.getInstance().requestHttpPost(url,apiVersion,account,accessKey,timestamp,
                    signature,signatureVersion,signatureMethod,requestParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * 获取分析数据，版本为1.0
     * @param auth
     * @param requestParams
     * @return
     */
    public String getBusiness(Auth auth,String account,Object requestParams){

        //权限数据
        String host = auth.getHost();
        String accessKey = auth.getAccessKey();
        String secretKey = auth.getSecretKey();

        //基本数据
        String apiVersion = commonFields[0];
        String method = commonFields[1];
        String uri = GETBUSINESS_URI;
        String timestamp = DateUtil.gmtNow();
        String signatureVersion = commonFields[3];
        String signatureMethod = commonFields[4];

        //混合数据
        HashMap<String, String> params = (HashMap<String, String>)requestParams;
        String url = StringUtil.changeGetUrlWithParam(host,uri,params);
        String signature = SecurityUtil.createSignature(accessKey,secretKey,method,host,uri,timestamp,signatureVersion,signatureMethod,params);

        //数据请求
        String response = null;
        try {
            response = HttpUtilManager.getInstance().requestHttpGet(url,apiVersion,account,accessKey,timestamp,
                    signature,signatureVersion,signatureMethod);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

}
