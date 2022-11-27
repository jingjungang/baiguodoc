package com.ukang.baiyu.utils;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * 采用httpclient方式请求
 * @author SAN
 *
 */
public class HttpsUtil {
	private static final int CONNECTION_TIMEOUT = 10000;

    public static String doHttpGet(String serverURL) throws Exception {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, CONNECTION_TIMEOUT);
        HttpClient hc = new DefaultHttpClient();
        HttpGet get = new HttpGet(serverURL);
        get.addHeader("Content-Type", "application/x-www-form-urlencoded");
//        get.setHeader("Cookie","_br_uid_1=uid%3D6876780775167%3A; BIGipServerdscm_farm=1746184384.0.0000; STICKY=SEAWEB004P:044086BC26EB4FAE88BFBC531C091EDD:bdm5j355thye4445yvus2kis; drugstore%2Efish=UserID=728708E24FAA4229A58D61AA436133E3; s_vi=[CS]v1|25D1666085011D4F-60000109600D3FBA[CE]; foresee.repeatdays=90");
        get.setParams(httpParameters);
        HttpResponse response = null;
        try {
            response = hc.execute(get);
        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (SocketException e) {
            throw new Exception(e.getLocalizedMessage());
        }
        int sCode = response.getStatusLine().getStatusCode();
        if (sCode == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        } else
            throw new Exception("StatusCode is " + sCode);
    }

    public static String doHttpsGet(HttpClient hc, String serverURL) throws Exception {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, CONNECTION_TIMEOUT);
        hc = initHttpClient(httpParameters);
        HttpGet get = new HttpGet(serverURL);
        get.addHeader("Content-Type", "text/xml");
//        get.setHeader("Cookie","_br_uid_1=uid%3D6876780775167%3A; BIGipServerdscm_farm=1746184384.0.0000; STICKY=SEAWEB004P:044086BC26EB4FAE88BFBC531C091EDD:bdm5j355thye4445yvus2kis; drugstore%2Efish=UserID=728708E24FAA4229A58D61AA436133E3; s_vi=[CS]v1|25D1666085011D4F-60000109600D3FBA[CE]; foresee.repeatdays=90");
        get.setParams(httpParameters);
        HttpResponse response = null;
        try {
            response = hc.execute(get);
            Header[] hh = response.getHeaders("Set-Cookie");
            for(int i = 0; i < hh.length; i++){
            	Log.d("Set-Cookie", hh[i].getValue());
            }
        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (SocketException e) {
            throw new Exception(e.getLocalizedMessage());
        }
        int sCode = response.getStatusLine().getStatusCode();
        if (sCode == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        } else
            throw new Exception("StatusCode is " + sCode);
    }

    public static String doHttpPost(String serverURL, String xmlString) throws Exception {
        Log.d("doHttpPost", "serverURL="+serverURL);
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, CONNECTION_TIMEOUT);
        HttpProtocolParams.setVersion(httpParameters, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParameters, HTTP.UTF_8);
        HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost(serverURL);
        post.addHeader("Content-Type", "text/xml");
        post.setEntity(new StringEntity(xmlString, "UTF-8"));
        post.setParams(httpParameters);
        HttpResponse response = null;
        try {
            response = hc.execute(post);
        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (SocketException e) {
            throw new Exception(e.getLocalizedMessage());
        }
        int sCode = response.getStatusLine().getStatusCode();
        Log.d("response code ", "sCode="+sCode);
        if (sCode == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        } else
            throw new Exception("StatusCode is " + sCode);
    }

    public static String doHttpsPost2(HttpClient hc,String serverURL, List<NameValuePair> params) throws Exception {
        HttpParams httpParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpParameters, CONNECTION_TIMEOUT);
        hc = initHttpClient(httpParameters);
        HttpPost post = new HttpPost(serverURL);
        post.addHeader("Content-Type", "application/x-www-form-urlencoded");
        // 设置字符集
        HttpEntity httpEntity = new UrlEncodedFormEntity(params, "utf-8");
        post.setEntity(httpEntity);
        post.setParams(httpParameters);
        HttpResponse response = null;
        try {
            response = hc.execute(post);
            hc.getConnectionManager().shutdown();
        } catch (UnknownHostException e) {
            throw new Exception("Unable to access " + e.getLocalizedMessage());
        } catch (SocketException e) {
            throw new Exception(e.getLocalizedMessage());
        }
        int sCode = response.getStatusLine().getStatusCode();
        if (sCode == HttpStatus.SC_OK) {
            return EntityUtils.toString(response.getEntity());
        } else
            throw new Exception("StatusCode is " + sCode);
    }

    public static HttpClient initHttpClient(HttpParams params) {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new SSLSocketFactoryImp(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient(params);
        }
    }

    public static class SSLSocketFactoryImp extends SSLSocketFactory {
        final SSLContext sslContext = SSLContext.getInstance("TLS");

        public SSLSocketFactoryImp(KeyStore truststore) throws NoSuchAlgorithmException,
                KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                        String authType) throws java.security.cert.CertificateException {
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                        String authType) throws java.security.cert.CertificateException {
                }
            };
            sslContext.init(null, new TrustManager[] {
                tm
            }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
                throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
