package com.example.lvo.alertnotifications;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.CertificateException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


import com.example.lvo.alertnotifications.ServerApi;

public class App extends Application {
    private static ServerApi userByToken;
    private Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://alert.elem.ru") //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create()) //Конвертер, необходимый для преобразования JSON'а в объекты
                .client(getUnsafeOkHttpClient())
                .build();
        userByToken = retrofit.create(ServerApi.class); //Создаем объект, при помощи которого будем выполнять запросы
    }

    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                        String authType) {}

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType) {}

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[0];
                }
            } };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext
                    .getSocketFactory();

            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient = okHttpClient.newBuilder()
                    .sslSocketFactory(sslSocketFactory)
                    .hostnameVerifier(org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER).build();

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static ServerApi getApi() {
        return userByToken;
    }
}