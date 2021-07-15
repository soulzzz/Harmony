package com.xmut.harmony.util.httputil.http;
import java.io.IOException;

import okhttp3.Response;

public interface OkHttpCallback {
    void onFailure(IOException e);
    void onSuccess(Response response);
}
