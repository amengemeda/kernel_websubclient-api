package io.mosip.kernel.websub.api.client;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class IDTokenServiceImpl implements IDTokenService{
    @Override
    public String getIDToken(String id, String type, String name) throws JSONException {
        //id - numeric value for ID
        //type - VID or UIN
        //name - full name separated by space

        JSONObject response = callAuthenticationApi(id,type,name);
        String token = getTokenFromResponse(response);
        return token;
    }

    @Override
    public JSONObject callAuthenticationApi(String individualId, String individualType, String name) throws JSONException {
        String uri=null, thumbprint=null, requestSessionKey=null, requestHMAC=null;

        //get uri

        //get thumbprint

        //get requestsessionkey

        //get requesthmac

        JSONObject response = postApi(uri,individualId,individualType,name,thumbprint,requestSessionKey,requestHMAC);
        return response;
    }

    public String getTokenFromResponse(JSONObject resp) throws JSONException {
        JSONObject value = (JSONObject) resp.get("response");
        String token = value.getString("authToken");
        return token;
    }

    public JSONObject postApi(String uri, String individualId, String individualType, String name, String thumbprint, String requestSessionKey, String requestHMAC) throws JSONException {
        //how to handle request headers
        JSONObject request=null, response = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String transactionId = null;
        //handle transactionId

        RestTemplate restTemplate = new RestTemplate();
        String url ="https://dev.fayda.et/idauthentication/v1/auth/{:FISP-LicenseKey}/{:Auth-Partner-ID}/{:Partner-Api-Key}";
        request = new JSONObject(
                "{ \"id\": \"fayda.identity.auth\",\"version\": \"v1\",\"requestTime\": " +
                        sdf.format(timestamp) +
                        ",\"env\": " + "Developer" +
                        ",\"domainUri\": " + "uri" +
                        ",\"transactionID\": " + transactionId +
                        ",\"requestedAuth\": {\"otp\": false,\"demo\": true,\"bio\": false}," +
                        "\"consentObtained\": true," +
                        "\"individualId\": " + individualId +
                        ",\"individualIdType\": " + individualType +
                        ",\"thumbprint\": " + thumbprint +
                        ",\"requestSessionKey\": " + requestSessionKey +
                        ",\"requestHMAC\": " + requestHMAC +
                        ",\"request\": {" +
                        "\"timestamp\": " + sdf.format(timestamp) +
                        "\"demographics\": {" +
                        "\"name\": [ {\"language\": \"eng\"," +
                        "\"value\": " + name + "}]}}"
        );
        response = restTemplate.postForObject(url, request, JSONObject.class);
        return response;
    }

}
