package br.udesc.mca.segment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class SegmenterClient {

    public static void main(String[] args) throws Exception {
        StringBuilder json = new StringBuilder();
        FileReader fr = new FileReader("trajectory.json");
        BufferedReader br = new BufferedReader(fr);
        String linha;
        while ((linha = br.readLine()) != null) {
            json.append(linha);
            json.append('\n');
        }
        br.close();
        fr.close();
        System.out.println(json);

        HttpClient hc = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://127.0.0.1:8080/segmenter/segmentByPoints/2");
        post.addHeader("accept", "application/json");

        StringEntity entity = new StringEntity(json.toString());
        entity.setContentType("application/json");
        post.setEntity(entity);

        HttpResponse response = hc.execute(post);
        System.out.println(response.getStatusLine());
        br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }
    }
}
