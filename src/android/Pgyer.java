package com.yang.eto1.CordovaPlugin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.pgyersdk.update.PgyUpdateManager;

public class Pgyer extends CordovaPlugin {

    @Override
    public void initialize(final CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        cordova.getThreadPool().execute(new Runnable() {
            public void run() {
                PgyUpdateManager.register(cordova.getActivity());
            }
        });
    }

    @Override
    public void onResume(boolean multitasking) {
        super.onResume(multitasking);

        // 自定义摇一摇的灵敏度，默认为1000，数值越小灵敏度越高。
        PgyFeedbackShakeManager.setShakingThreshold(1000);

        // 以Activity的形式打开，这种情况下必须在AndroidManifest.xml配置FeedbackActivity
        PgyFeedbackShakeManager.register(cordova.getActivity(), false);

    }

    @Override
    public void onPause(boolean multitasking) {
        super.onPause(multitasking);
        PgyFeedbackShakeManager.unregister();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
