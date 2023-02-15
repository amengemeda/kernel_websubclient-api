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
}
