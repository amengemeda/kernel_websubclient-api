package io.mosip.kernel.websub.api.client;

public class Variables {

    // common variables for subscriber and publisher
    public static final String topic="mpartner-default-print/CREDENTIAL_ISSUED";
    public static final String secret = "OIUAShdasoi";

    // subscriber variables
    public static final String subscriberHubUrl = "https://api-internal.dev.fayda.et/hub";
    public static final String callback = "http://10.30.28.27:8080/ID_CREDENTIAL/callback";

    //publisher variables
    public static final String publisherHubUrl = "https://api-internal.dev.fayda.et/hub";
    public static final String moeTopic="ID_CREDENTIAL";

    /**
     * New hub variables
     */


    // common variables for subscriber and publisher
    public static final String newHubTopic="TestTopic";
    public static final String newHubSecret = "TestSecret";

    // subscriber variables
    public static final String newHubSubscriberHubUrl = "http://172.19.7.90:9191/hub";
    public static final String newHubCallback = "http://10.50.50.65:8080/newHub/callback";

    //publisher variables
    public static final String newHubPublisherHubUrl = "http://172.19.7.90:9191/hub";


}
