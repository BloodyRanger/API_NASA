import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {

    public static final String ADDRESS = "https://api.nasa.gov/planetary/apod?api_key=RGWvoda9koKIoNZmngB8az8IiWxGibfx4Gk2J4LR";
    public static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(5000)
                            .setSocketTimeout(30000)
                            .setRedirectsEnabled(false)
                            .build())
                    .build();

            HttpGet request = new HttpGet(ADDRESS);
            CloseableHttpResponse response = httpClient.execute(request);

            Nasa nasa = mapper.readValue(response.getEntity().getContent(), new TypeReference<Nasa>() {
            });

            String image = nasa.getUrl();
            String name = image.substring(image.lastIndexOf('/') + 1).trim();

            HttpGet requestImage = new HttpGet(image);
            CloseableHttpResponse responseImage = httpClient.execute(requestImage);

            FileOutputStream fos = new FileOutputStream(name);
            byte[] bytes = responseImage.getEntity().getContent().readAllBytes();
            fos.write(bytes, 0, bytes.length);
            fos.close();

        } catch (IOException err) {
            err.printStackTrace();
        }
    }
}
