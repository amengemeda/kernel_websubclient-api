package io.mosip.kernel.websub.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.websub.spi.SubscriptionClient;
import io.mosip.kernel.core.websub.spi.SubscriptionExtendedClient;
import io.mosip.kernel.websub.api.config.publisher.RestTemplateHelper;
import io.mosip.kernel.websub.api.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@ComponentScan({"io.mosip.kernel.websub.api.client.PublisherClientImpl"})
@RestController
public class Subscriber {

    @MockBean
    private RestTemplateHelper restTemplateHelper;

    private RestTemplate restTemplate;

    private MockRestServiceServer mockServer;

    @Autowired
    private SubscriptionClient<SubscriptionChangeRequest, UnsubscriptionRequest, SubscriptionChangeResponse> subscriptionClient;

    @Autowired
    private SubscriptionExtendedClient<FailedContentResponse, FailedContentRequest> subscriptionExtendedClient;

    private SubscriptionChangeRequest subscriptionRequest;

    private UnsubscriptionRequest unsubscriptionRequest;

    private FailedContentRequest failedContentRequest;

    private String expectedFailedContentURL = "http://localhost:9191/hub?topic=demo&callback=aHR0cDovL2xvY2FsaG9zdDo4MDgwL2NhbGxiYWNr&timestamp=2021-08-30T12:39:23.511446100Z&pageindex=0&messageCount=10";

    @Autowired
    private ObjectMapper objectMapper;

    public void init() {


        restTemplate = new RestTemplate();

        subscriptionRequest = new SubscriptionChangeRequest();
        subscriptionRequest.setHubURL(Variables.subscriberHubUrl);
        subscriptionRequest.setTopic(Variables.topic);
        subscriptionRequest.setCallbackURL(Variables.callback);
        subscriptionRequest.setSecret(Variables.secret);

        unsubscriptionRequest = new UnsubscriptionRequest();
		unsubscriptionRequest.setHubURL(Variables.subscriberHubUrl);
		unsubscriptionRequest.setTopic(Variables.topic);
		unsubscriptionRequest.setCallbackURL(Variables.callback);

    }

    public void newHubInit() {


        restTemplate = new RestTemplate();

        subscriptionRequest = new SubscriptionChangeRequest();
        subscriptionRequest.setHubURL(Variables.newHubSubscriberHubUrl);
        subscriptionRequest.setTopic(Variables.newHubTopic);
        subscriptionRequest.setCallbackURL(Variables.newHubCallback);
        subscriptionRequest.setSecret(Variables.newHubSecret);
        
        unsubscriptionRequest = new UnsubscriptionRequest();
        unsubscriptionRequest.setHubURL(Variables.newHubSubscriberHubUrl);
        unsubscriptionRequest.setTopic(Variables.newHubTopic);
        unsubscriptionRequest.setCallbackURL(Variables.newHubCallback);

    }
    @RequestMapping("/subscribe")
    public String Subscribe() throws URISyntaxException {
        this.init();
        SubscriptionChangeResponse subscriptionChangeResponse=subscriptionClient.subscribe(subscriptionRequest);
        assertThat(subscriptionChangeResponse.getTopic(),is(Variables.topic));
        return "subscription request sent successfully";
    }
    @RequestMapping("/unsubscribe")
    public String unsubscribe() throws URISyntaxException {
        this.init();
        SubscriptionChangeResponse subscriptionChangeResponse=subscriptionClient.unSubscribe(unsubscriptionRequest);
        assertThat(subscriptionChangeResponse.getTopic(),is(Variables.topic));
        return "Unsubscribed Successfully";
	}


    /**
     * New Hub
     * Subscribe and unsubscribe
     **/
    @RequestMapping("newHub/subscribe")
    public String newHubSubscribe() throws URISyntaxException {
        this.newHubInit();
        SubscriptionChangeResponse subscriptionChangeResponse=subscriptionClient.subscribe(subscriptionRequest);
        assertThat(subscriptionChangeResponse.getTopic(),is(Variables.newHubTopic));
        return "subscription request sent successfully";
    }
    @RequestMapping("newHub/unsubscribe")
    public String newHubUnsubscribe() throws URISyntaxException {
        this.newHubInit();
        SubscriptionChangeResponse subscriptionChangeResponse=subscriptionClient.unSubscribe(unsubscriptionRequest);
        assertThat(subscriptionChangeResponse.getTopic(),is(Variables.newHubTopic));
        return "Unsubscribed Successfully";
    }
}
