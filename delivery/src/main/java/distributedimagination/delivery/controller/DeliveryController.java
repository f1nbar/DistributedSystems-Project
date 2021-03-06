package distributedimagination.delivery.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import distributedimagination.delivery.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @Autowired
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @RequestMapping(value = "/delivery-providers")
    public Map<String, String> returnMap() {
        return deliveryService.getDelivery();
    }

    @PostMapping(value = "/request-delivery", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getDeliveryList(@RequestBody String order) {
        JsonObject jsonOrder = JsonParser.parseString(order).getAsJsonObject();
        String serviceID = jsonOrder.get("serviceID").toString().replace("\"", "");
        return deliveryService.GenerateDelivery(serviceID, jsonOrder);
    }
}