package distributedimagination.quotation.controller;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@SpringBootApplication
@RestController
public class QuotationController {
    @Autowired
    private EurekaClient eurekaClient;

    @RequestMapping(value = "/service-instances/list")
    public ArrayList<String> getApplications() {
        ArrayList<String> serviceInstances = new ArrayList<String>();
        List<Application> applications = eurekaClient.getApplications().getRegisteredApplications();
        for (Application application : applications) {
            for (InstanceInfo instance : application.getInstances()) {
                if (instance.getAppName().contains("POSTAL-SERVICE-"))
                    serviceInstances.add(instance.getHomePageUrl());
            }
        }
        return serviceInstances;
    }

    @RequestMapping(value = "/service-instances/quotations")
    public ArrayList<String> getQuotationsList() throws URISyntaxException {
        ArrayList<String> quotations = new ArrayList<String>();
        quotations.add("Test Quote");
        ArrayList<String> serviceInstances = getApplications();
        Iterator<String> iterator = serviceInstances.iterator();
        while (iterator.hasNext()) {
            String appURL = iterator.next();
            final String baseUrl = appURL + "/quote";
            URI uri = new URI(baseUrl);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> requestEntity = new HttpEntity<>(null);
            ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
            quotations.add(result.toString());
        }
        return quotations;
    }
}