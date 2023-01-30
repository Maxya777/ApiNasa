
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;


public class Main {

    private static final String REMOTE_SERVICE_URL = "https://api.nasa.gov/planetary/apod?api_key=uCjHsW7XAzfI6CSxxfFai9B6BFn3Y7aqJup9gnOf";

    private static final ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
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
    }
}
