package com.projects.kquicho.uwatm8;

import com.projects.kquicho.uw_api_client.Core.UWParser;

public interface UWClientResponseHandler {

    public void onSuccess(UWData data, Integer position);

    public void onError(String error);

}
