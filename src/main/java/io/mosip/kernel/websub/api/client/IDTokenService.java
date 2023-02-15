package io.mosip.kernel.websub.api.client;

import org.json.JSONException;

public interface IDTokenService {
    public String getIDToken(String individualId, String individualType, String name, String lang) throws Exception;
    public Object callAuthenticationApi(String individualId, String individualType, String name, String lang) throws Exception;
}
