package io.mosip.kernel.websub.api.client;

import io.mosip.kernel.websub.api.config.publisher.RestTemplateHelper;
import io.mosip.kernel.websub.api.model.SubscriptionChangeRequest;
import org.bouncycastle.asn1.cmp.Challenge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.*;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

@ComponentScan({"io.mosip.kernel.websub.api.client.PublisherClientImpl"})
@RestController

public class Publisher {

    @MockBean
    private RestTemplateHelper restTemplateHelper;

    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Autowired
    private PublisherClientImpl<String> publisherClientImpl;

    private String hubUrl;
    private String topic;

    public void init() {
        restTemplate = new RestTemplate();
        hubUrl = "https://dev.fayda.et/websub/publish";
        topic = "AmanTopic";
    }

    @RequestMapping("/registerTopic")
    public String registerTopic() throws URISyntaxException {
        this.init();
		publisherClientImpl.registerTopic(topic, hubUrl);
        return "Topic registered";
    }

    @RequestMapping("/unregisterTopic")
    public String unregisterTopic() throws URISyntaxException {
        this.init();
        publisherClientImpl.unregisterTopic(topic, hubUrl);
        return "Topic unregistered";
    }

    @RequestMapping("/publish")
    public String publish() throws URISyntaxException {
        this.init();
        String payload = "Test Data";
        publisherClientImpl.publishUpdate(topic, payload, MediaType.APPLICATION_JSON_UTF8_VALUE, null, hubUrl);
        return "Content Published";
    }

    @RequestMapping("/notifyUpdate")
    public String notifyUpdate() throws URISyntaxException {
        this.init();
        publisherClientImpl.notifyUpdate(topic, null, hubUrl);
        return "Update notified";
    }

    @RequestMapping(path = "/callback", method = RequestMethod.POST)
    public String testResponse(@RequestBody String publishedContent) throws IOException {
        System.out.println("publishedContent: "+publishedContent);
        return "publishedContent: "+publishedContent;
    }

    @RequestMapping(path = "/callback", method = RequestMethod.GET)
    public ResponseEntity testGetResponse(@RequestParam("hub.challenge") String challenge, @RequestParam("hub.topic") String res_topic) throws IOException {
        if(res_topic==topic){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(challenge);
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("");
        }
    }

}
