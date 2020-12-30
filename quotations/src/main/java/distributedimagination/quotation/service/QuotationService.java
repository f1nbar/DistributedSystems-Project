package distributedimagination.quotation.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

@Service
public class QuotationService {

    public Map<String, String> getQuotes() {
        final String uri = "http://discovery:8761/postal-services/urls";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> map;
        map = restTemplate.getForObject(uri, Map.class);
        return map;
    }

    public ArrayList<String> getQuotationsList() {
        ArrayList<String> quotations = new ArrayList<>();
        Map<String, String> map = getQuotes();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String appURL = entry.getValue() + "/quote";
            String name = entry.getKey();
            RestTemplate restTemplates = new RestTemplate();
            quotations.add(restTemplates.getForObject(appURL, String.class));
        }
        return quotations;
    }
}