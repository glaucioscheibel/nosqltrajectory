package br.udesc.mca.importabusriodejaneiro;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ImportJSON {
    private static final String URL = "http://dadosabertos.rio.rj.gov.br/apiTransporte/apresentacao/rest/index.cfm/onibus";

    public static void main(String[] args) throws Exception {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(URL);
        request.addHeader("Accept", "text/html,application/xhtml+xml,application/xml,application/json");
        HttpResponse result = httpClient.execute(request);
        HttpEntity entity = result.getEntity();
        String json = EntityUtils.toString(entity, "UTF-8");
        httpClient.close();
        System.out.println(json.getBytes().length);
        ObjectMapper om = new ObjectMapper();
        BusData bd = om.readValue(json, BusData.class);
        System.out.println(bd);
    }
}
