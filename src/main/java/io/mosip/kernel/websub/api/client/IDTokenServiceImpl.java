package io.mosip.kernel.websub.api.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;


import javax.crypto.SecretKey;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class IDTokenServiceImpl implements IDTokenService{
    private final String url = "https://dev.fayda.et/idauthentication/v1/auth/{:FISP-LicenseKey}/{:Auth-Partner-ID}/{:Partner-Api-Key}";
    public CryptoUtil crypto = new CryptoUtil();
    @Override
    public String getIDToken(String id, String type, String name, String lang) throws Exception {
        //id - numeric value for ID
        //type - VID or UIN
        //name - full name separated by space
        //lang - amh or eng

        JSONObject response = callAuthenticationApi(id,type,name,lang);

        //todo check if token is valid
        String token = getTokenFromResponse(response);
        return token;
    }

    @Override
    public JSONObject callAuthenticationApi(String individualId, String individualType, String name, String lang) throws Exception {
        String uri=null, thumbprint=null, requestSessionKey=null, requestHMAC=null;

        //todo get thumbprint
        //thumbprint = crypto.generateThumbprint(); //add certificate

        //todo get requestsessionkey

        JSONObject response = postApi(individualId,individualType,name,lang,thumbprint,requestSessionKey);
        return response;
    }

    public String getTokenFromResponse(JSONObject resp) throws JSONException {
        JSONObject value = (JSONObject) resp.get("response");
        //todo get values
        String token = value.getString("authToken");
        return token;
    }

    public JSONObject postApi(String individualId, String individualType, String name, String lang, String thumbprint, String requestSessionKey) throws Exception {
        //todo how to handle request headers -signing + auth
        JSONObject JSONrequest=null, response = null;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String transactionId = null, requestHMAC = null;
        //todo handle transactionId

        JSONObject request = new JSONObject(), demolang = new JSONObject(), demovalue = new JSONObject();
        demolang.put("language", lang);
        demovalue.put("value",name);
        JSONArray demoname = new JSONArray();
        demoname.put(demolang);
        demoname.put(demovalue);
        request.put("timestamop",sdf.format(timestamp));
        request.put("demographics", demoname);

        //todo get requesthmac
        requestHMAC = crypto.generateHMAC(request.toString());

        //todo encrypt request
        SecretKey secretKey = crypto.generateSecretKey();



        RestTemplate restTemplate = new RestTemplate();

        //todo edit url
        //String url ="https://dev.fayda.et/idauthentication/v1/auth/{:FISP-LicenseKey}/{:Auth-Partner-ID}/{:Partner-Api-Key}";

        JSONrequest = new JSONObject(
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
                        ",\"request\": {" + //todo encrypt -session key
                        "\"timestamp\": " + sdf.format(timestamp) +
                        "\"demographics\": {" +
                        "\"name\": [ {\"language\": \"eng\"," +
                        "\"value\": " + name + "}]}}"
        );

        //todo handle error responses-generic exception
        try{
            response = restTemplate.postForObject(url, JSONrequest, JSONObject.class);
        }
        catch (Exception e){
            //handle exception
        }

        return response;
    }

}
