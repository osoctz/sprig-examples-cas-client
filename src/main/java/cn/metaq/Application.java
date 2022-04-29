package cn.metaq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

//    static {
//        //for localhost testing only
//        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
//            new javax.net.ssl.HostnameVerifier(){
//
//                public boolean verify(String hostname,
//                    javax.net.ssl.SSLSession sslSession) {
//                    if (hostname.equals("localhost")) {
//                        return true;
//                    }
//                    return false;
//                }
//            });
//    }

    public static void main(String[] args) {

        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
            (hostname, sslSession) -> {
                if (hostname.equals("localhost")) {
                    return true;
                }
                return false;
            });
        SpringApplication.run(Application.class, args);
    }
}
