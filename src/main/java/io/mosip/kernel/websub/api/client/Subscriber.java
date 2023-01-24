package io.mosip.kernel.websub.api.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.mosip.kernel.core.websub.spi.SubscriptionClient;
import io.mosip.kernel.core.websub.spi.SubscriptionExtendedClient;
import io.mosip.kernel.websub.api.config.publisher.RestTemplateHelper;
import io.mosip.kernel.websub.api.exception.WebSubClientException;
import io.mosip.kernel.websub.api.model.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;

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

        String topic="AmanTopic";
        String hubUrl = "https://dev.fayda.et/websub/hub";
        String callback = "http://10.30.28.27:8080/callback";
        String secret = "OIUAShdasoi";

        restTemplate = new RestTemplate();

        subscriptionRequest = new SubscriptionChangeRequest();
        subscriptionRequest.setHubURL(hubUrl);
        subscriptionRequest.setTopic(topic);
        subscriptionRequest.setCallbackURL(callback);
        subscriptionRequest.setSecret(secret);

        unsubscriptionRequest = new UnsubscriptionRequest();
		unsubscriptionRequest.setHubURL(hubUrl);
		unsubscriptionRequest.setTopic(topic);
		unsubscriptionRequest.setCallbackURL(callback);

    }
    @RequestMapping("/subscribe")
    public String Subscribe() throws URISyntaxException {
        this.init();
        String topic="AmanTopic";
        SubscriptionChangeResponse subscriptionChangeResponse=subscriptionClient.subscribe(subscriptionRequest);
        assertThat(subscriptionChangeResponse.getTopic(),is(topic));
        return "Subscribed Successfully";
    }
    @RequestMapping("/unsubscribe")
    public String unsubscribe() throws URISyntaxException {
        this.init();
        String topic="AmanTopic";
        SubscriptionChangeResponse subscriptionChangeResponse=subscriptionClient.unSubscribe(unsubscriptionRequest);
        assertThat(subscriptionChangeResponse.getTopic(),is(topic));
        return "Unsubscribed Successfully";
	}

}
