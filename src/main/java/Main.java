
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.FileOutputStream;
import java.io.IOException;



public class Main {

    public static final String REMOTE_SERVICE_URL = "https://api.nasa.gov/planetary/apod?api_key=uCjHsW7XAzfI6CSxxfFai9B6BFn3Y7aqJup9gnOf";

    public static ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet request = new HttpGet(REMOTE_SERVICE_URL);
        CloseableHttpResponse response = httpClient.execute(request);

        Nasa nasa = mapper.readValue(response
                .getEntity()
                .getContent(), Nasa.class
        );
        System.out.println(nasa);
        response.close();
        HttpGet request2 = new HttpGet(nasa.getUrl());
        CloseableHttpResponse response2 = httpClient.execute(request2);
        FileOutputStream fos = new FileOutputStream("MarsFingers_Curiosity_960.jpg");
        byte[] bytes = response2
                .getEntity()
                .getContent()
                .readAllBytes();
        fos.write(bytes, 0, bytes.length);
        fos.close();
        response2.close();
        httpClient.close();
    }
}
