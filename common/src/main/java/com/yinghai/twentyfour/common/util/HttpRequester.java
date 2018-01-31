package com.yinghai.twentyfour.common.util;

import com.alibaba.fastjson.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import com.yinghai.twentyfour.common.util.HttpRespons;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class HttpRequester {

    private String defaultContentEncoding;

    public HttpRequester() {
        this.defaultContentEncoding = Charset.defaultCharset().name();
    }

    /**
     * 发送HTTP请求
     *
     * @param urlString
     * @return 响映对象
     * @throws IOException
     */
    public HttpRespons send(String urlString, String method, Map<String, String> parameters)
            throws IOException {
        HttpURLConnection urlConnection = null;

        if (method.equalsIgnoreCase("GET") && parameters != null) {
            StringBuffer param = new StringBuffer();
            int i = 0;
            for (String key : parameters.keySet()) {
                if (i == 0)
                    param.append("?");
                else
                    param.append("&");
                param.append(key).append("=").append(parameters.get(key));
                i++;
            }
            urlString += param;
        }
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod(method);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);
        urlConnection.setRequestProperty("Accept-Charset", "utf-8");
        urlConnection.setRequestProperty("contentType", "utf-8");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("Charset", "utf-8");
        if (method.equalsIgnoreCase("POST") && parameters != null) {
            StringBuffer param = new StringBuffer();
            for (String key : parameters.keySet()) {
                param.append("&");
                param.append(key).append("=").append(parameters.get(key));
            }
            urlConnection.getOutputStream().write(param.toString().getBytes());
            urlConnection.getOutputStream().flush();
            urlConnection.getOutputStream().close();
        }

        return this.makeContent(urlString, urlConnection);
    }

    /**
     * 得到响应对象
     *
     * @param urlConnection
     * @return 响应对象
     * @throws IOException
     */
    private HttpRespons makeContent(String urlString,
                                    HttpURLConnection urlConnection) throws IOException {
        HttpRespons httpResponser = new HttpRespons();
        try {
            InputStream in = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(in,"utf-8"));
            httpResponser.contentCollection = new Vector<String>();
            StringBuffer temp = new StringBuffer();
            String line = bufferedReader.readLine();
            while (line != null) {
                httpResponser.contentCollection.add(line);
                temp.append(line).append("\r\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            String ecod = urlConnection.getContentEncoding();
            if (ecod == null)
                ecod = this.defaultContentEncoding;

            httpResponser.urlString = urlString;

            httpResponser.defaultPort = urlConnection.getURL().getDefaultPort();
            httpResponser.file = urlConnection.getURL().getFile();
            httpResponser.host = urlConnection.getURL().getHost();
            httpResponser.path = urlConnection.getURL().getPath();
            httpResponser.port = urlConnection.getURL().getPort();
            httpResponser.protocol = urlConnection.getURL().getProtocol();
            httpResponser.query = urlConnection.getURL().getQuery();
            httpResponser.ref = urlConnection.getURL().getRef();
            httpResponser.userInfo = urlConnection.getURL().getUserInfo();

            httpResponser.content = new String(temp.toString().getBytes(), ecod);
            httpResponser.contentEncoding = ecod;
            httpResponser.code = urlConnection.getResponseCode();
            httpResponser.message = urlConnection.getResponseMessage();
            httpResponser.contentType = urlConnection.getContentType();
            httpResponser.method = urlConnection.getRequestMethod();
            httpResponser.connectTimeout = urlConnection.getConnectTimeout();
            httpResponser.readTimeout = urlConnection.getReadTimeout();

            return httpResponser;
        } catch (IOException e) {
            throw e;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }


    /**
     * 默认的响应字符集
     */
    public String getDefaultContentEncoding() {
        return this.defaultContentEncoding;
    }

    /**
     * 设置默认的响应字符集
     */
    public void setDefaultContentEncoding(String defaultContentEncoding) {
        this.defaultContentEncoding = defaultContentEncoding;
    }

    public Map<String,String> athentication(String system,String loginName,String password,String orgnization,Integer encryptType,String encryptMethod){
        Map<String,String> returnMap=new HashMap<String ,String>();
        try {
            Map<String,String> map=new HashMap<String ,String>();
            map.put("system", system);
            map.put("loginName", loginName);
            map.put("password", password);
            map.put("orgnization", orgnization);
            map.put("encryptType", encryptType+"");
            map.put("encryptMethod", encryptMethod);
            HttpRespons hr =send("", "GET", map);
            String resultCode= StringJudge.parseResult(hr.getContent(), "resultCode");
            String message= StringJudge.parseResult(hr.getContent(), "message");
            returnMap.put("resultCode", resultCode);
            returnMap.put("message", message);
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return returnMap;
    }

    /**
     *
     * @param map
     * @return
     */
    public static String getIMURLString(Map<String,Object> map){
        StringBuffer param = new StringBuffer();
        int i = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (i == 0)
                param.append("?");
            else
                param.append("&");
            param.append(entry.getKey()).append("=").append(entry.getValue());
            i++;
        }
        return param.toString();
    }

    /**
     * 发送https请求
     *
     * @param requestUrl
     *            请求地址
     * @param requestMethod
     *            请求方式（GET、POST）
     * @param outputStr
     *            提交的数据
     * @return 返回微信服务器响应的信息
     */
    public static String httpsRequest(String requestUrl, String requestMethod,
                                      String outputStr) {
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new FsTrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type",
                    "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(
                    inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(
                    inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            //log.error("连接超时：{}", ce);
        } catch (Exception e) {
            //log.error("https请求异常：{}", e);
        }
        return null;
    }
    public static void main(String[] args) throws Exception {
    	System.out.println(requestGetByHttp("https://www.google.com.hk"));
    }
    /**
     * 調用其他URL進行數據處理
     * @param url 目的URL
     * @param params  參數
     * @return
     * @throws Exception
     * @throws Exception
     */
    public static String httpMutual(String url,List<NameValuePair> params) throws Exception, Exception{
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(params);
        httpPost.setEntity(urlEncodedFormEntity);
        CloseableHttpResponse response= client.execute(httpPost);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity, "UTF-8");
    }
    
    /**
     * 网络代理请求（翻墙使用get请求）
     * @param url
     * @return
     */
    public static String requestGetByHttp(String url){
    	String body = null;
		try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {		
			HttpGet get = new HttpGet(url);
			get.addHeader("Accept-Charset","utf-8");
			
			 //设置网络代理
            RequestConfig defaultRequestConfig = RequestConfig.custom()
                    .setSocketTimeout(5000)
                    .setConnectTimeout(5000)
                    .setConnectionRequestTimeout(5000)
                    .setStaleConnectionCheckEnabled(true)
                    .build();
            RequestConfig requestConfig = RequestConfig.copy(defaultRequestConfig).
                    setProxy(new HttpHost("127.0.0.1", 1080)) //代理地址  和 端口
                    .build();
            get.setConfig(requestConfig);
            
            HttpResponse response = sendRequest(httpClient, get);
            body = parseResponse(response);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return body;
    }
    
    private static HttpResponse sendRequest(CloseableHttpClient httpclient, HttpUriRequest httpost)
			throws ClientProtocolException, IOException {
		HttpResponse response = null;
		response = httpclient.execute(httpost);
		return response;
	}
    
    private static String parseResponse(HttpResponse response) {
		System.out.println("get response from http server..");
		HttpEntity entity = response.getEntity();

		System.out.println("response status: " + response.getStatusLine());
		Charset charset = ContentType.getOrDefault(entity).getCharset();
		if (charset != null) {
			System.out.println(charset.name());
		}

		String body = null;
		try {
			body = EntityUtils.toString(entity, "utf-8");
			System.out.println("body " + body);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}

		return body;
	}
}
