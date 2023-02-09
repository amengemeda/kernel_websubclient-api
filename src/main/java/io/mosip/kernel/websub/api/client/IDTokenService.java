package io.mosip.kernel.websub.api.client;

import org.json.JSONException;

public interface IDTokenService {
    public String getIDToken(String individualId, String individualType, String name) throws JSONException;
    public Object callAuthenticationApi(String individualId, String individualType, String name) throws JSONException;
}
