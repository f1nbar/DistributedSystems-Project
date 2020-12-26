package distributedimagination.quotation.controller;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@SpringBootApplication
@RestController
public class QuotationController {
    @Autowired
    private DiscoveryClient discoveryClient;
    //public ArrayList<String> applicationsInstances = new ArrayList<>();

    @RequestMapping(value = "/service-instances/list")
    public List getApplications() {
        List<ServiceInstance> services = discoveryClient.getInstances("DEMO-SERVICE");
        return services;
    }
}

    //    @RequestMapping("/service-instances/quotations")
//    public ArrayList<String> getQuotationsList(@PathVariable String applicationName) {
//        ArrayList<String> quotations = new ArrayList<String>();
//        for (int i = 0; i < applicationsInstances.size(); i++) {
//            applicationName = applicationsInstances.get(i).getAppName();
//            quotations.add("/{applicationName}/quote");
//        }
//        return quotations;
//    }
