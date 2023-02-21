package io.mosip.kernel.websub.api.client;

import io.mosip.kernel.websub.api.config.publisher.RestTemplateHelper;
import io.mosip.kernel.websub.api.model.SubscriptionChangeRequest;
import net.minidev.json.JSONValue;
import org.bouncycastle.asn1.cmp.Challenge;
import org.json.*;
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

    public void init() {
        restTemplate = new RestTemplate();
    }

    @RequestMapping("/registerTopic")
    public String registerTopic() throws URISyntaxException {
        this.init();
		publisherClientImpl.registerTopic(Variables.moeTopic, Variables.publisherHubUrl);
        return "Topic registered";
    }

    @RequestMapping("/unregisterTopic")
    public String unregisterTopic() throws URISyntaxException {
        this.init();
        publisherClientImpl.unregisterTopic(Variables.moeTopic, Variables.publisherHubUrl);
        return "Topic unregistered";
    }

    @RequestMapping("/publish")
    public String publish() throws URISyntaxException {
        this.init();
        String payload = "{\n" +
                "    \"regid\": \"String\"\n" +
                "    \"photo\": \"base64 string\"\n" +
                "    \"FIN\": \"String\"\n" +
                "}\n";
        publisherClientImpl.publishUpdate(Variables.topic, payload, MediaType.APPLICATION_JSON_UTF8_VALUE, null, Variables.publisherHubUrl);
        return "Content Published";
    }

    @RequestMapping("/notifyUpdate")
    public String notifyUpdate() throws URISyntaxException {
        this.init();
        publisherClientImpl.notifyUpdate(Variables.topic, null, Variables.publisherHubUrl);
        return "Update notified";
    }

    @RequestMapping(path = "/callback", method = RequestMethod.POST)
    public String testResponse(@RequestBody String publishedContent) throws IOException {
        System.out.println("publishedContent: "+publishedContent);
        return "publishedContent: "+publishedContent;
    }

    @RequestMapping(path = "/callback", method = RequestMethod.GET)
    public ResponseEntity testGetResponse(@RequestParam("hub.challenge") String challenge, @RequestParam("hub.topic") String res_topic) throws IOException {
        if(res_topic==Variables.topic){
            return ResponseEntity.status(HttpStatus.OK)
                    .body(challenge);
        }else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("");
        }
    }


    /**
     * MOE => Ministry of Education
     *
     * MOE callback end Points
     */

    @RequestMapping(path = "ID_CREDENTIAL/callback", method = RequestMethod.POST)
    public String moePostCallbackResponse(@RequestBody String publishedContent) throws IOException, JSONException {
        System.out.println("publishedContent: "+publishedContent);
        JSONObject publishedContentObject = new JSONObject(publishedContent);
        JSONObject payloadObject=new JSONObject();

        //Elroe function


        payloadObject.put("regid",publishedContentObject.get("regid"));
        payloadObject.put("photo",publishedContentObject.get("photo"));
        payloadObject.put("FIN",publishedContentObject.get("FIN"));
        String payloadJSON = JSONValue.toJSONString(payloadObject);
        publisherClientImpl.publishUpdate(Variables.moeTopic, payloadJSON, MediaType.APPLICATION_JSON_UTF8_VALUE, null, Variables.publisherHubUrl);
        return "publishedContent: "+publishedContent;
    }

    @RequestMapping(path = "ID_CREDENTIAL/callback", method = RequestMethod.GET)
    public ResponseEntity moeGetCallbackResponse(@RequestParam("hub.challenge") String challenge, @RequestParam("hub.topic") String res_topic) throws IOException {
        System.out.println("challenge: "+challenge);
        return ResponseEntity.status(HttpStatus.OK)
                .body(challenge);
    }


    /**
     * NH => New Hub
     *
     * New Hub register/unregister topic
     * New Hub publish topic
     * New Hub callback end Points
     */

    @RequestMapping("newHub/registerTopic")
    public String NHRegisterTopic() throws URISyntaxException {
        this.init();
        publisherClientImpl.registerTopic(Variables.newHubTopic, Variables.newHubPublisherHubUrl);
        return "Topic registered";
    }

    @RequestMapping("newHub/unregisterTopic")
    public String NHUnregisterTopic() throws URISyntaxException {
        this.init();
        publisherClientImpl.unregisterTopic(Variables.newHubTopic, Variables.newHubPublisherHubUrl);
        return "Topic unregistered";
    }

    @RequestMapping("newHub/publish")
    public String NHPublish() throws URISyntaxException {
        this.init();
        String payload = "{\n" +
                "    \"regId\": \"1234567890\"\n"+
                "}\n";
        publisherClientImpl.publishUpdate(Variables.newHubTopic, payload, MediaType.APPLICATION_JSON_UTF8_VALUE, null, Variables.newHubPublisherHubUrl);
        return "Content Published";
    }
    @RequestMapping(path = "newHub/callback", method = RequestMethod.POST)
    public String NHPostCallbackResponse(@RequestBody String publishedContent) throws IOException, JSONException {
        System.out.println("publishedContent: "+publishedContent);
        JSONObject publishedContentObject = new JSONObject(publishedContent);
        int regId = 0;
        if(!publishedContentObject.isNull("regId")){
            regId = publishedContentObject.getInt("regId");
            int firstFiveDigit = Integer.parseInt(Integer.toString(regId).substring(0, 5));
            System.out.println("The first Five digits: "+firstFiveDigit);
        }

        return "publishedContent: "+publishedContent;
    }

    @RequestMapping(path = "newHub/callback", method = RequestMethod.GET)
    public ResponseEntity NHGetCallbackResponse(@RequestParam("hub.challenge") String challenge, @RequestParam("hub.topic") String res_topic) throws IOException {
        System.out.println("challenge: "+challenge);
        return ResponseEntity.status(HttpStatus.OK)
                .body(challenge);
    }
}
