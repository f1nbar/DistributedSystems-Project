package distributedimagination.eureka.controller;

import com.netflix.appinfo.InstanceInfo;
import distributedimagination.eureka.service.RegistryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


@RestController
public class RegistryController {

    private final RegistryService registryService;

    @Autowired
    public RegistryController(RegistryService registryService) {
        this.registryService = registryService;
    }

    @RequestMapping(value = "/postal-services", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<InstanceInfo> getAllParcels() {
        return registryService.getPostalServiceInstances();

    }

    @RequestMapping(value = "/postal-services/urls", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getPostalServiceURLs() {
        return registryService.getPostalURLs();
    }


    @RequestMapping(value = "/postal-services/id", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> getPostalServiceIDs() {
        return registryService.getPostalIDs();
    }
}
