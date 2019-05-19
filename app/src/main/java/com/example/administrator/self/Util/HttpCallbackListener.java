package com.example.administrator.self.Util;

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
