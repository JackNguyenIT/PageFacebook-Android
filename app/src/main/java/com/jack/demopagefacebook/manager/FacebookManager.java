package com.jack.demopagefacebook.manager;

import android.os.Bundle;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

/**
 * Created by Jack on 3/13/17.
 */

public class FacebookManager {
    private static final String ACCESS_TOKEN = "1797367403917212|9fbece39d452e94f5345d95d13e3928d";

//    private static final String URL_PAGE = "https://www.facebook.com/chuanmenvn";
    private static final String URL_PAGE ="https://www.facebook.com/tintucvtv24";

    public static void getPageWithUrl(Callback callback) {
        Bundle bundle = new Bundle();
        bundle.putString("fields", "picture,name");
        bundle.putString("access_token", ACCESS_TOKEN);
        GraphRequest graphRequest = new GraphRequest(null, URL_PAGE, bundle, HttpMethod.GET, callback);
        graphRequest.executeAsync();
    }

    public static void getDataFeedByPageId(String pageId, Callback callback) {
        Bundle bundle = new Bundle();
        bundle.putString("fields", "message,caption,description,full_picture,picture,name,link,type,created_time");
        bundle.putString("limit", "20");
        bundle.putString("access_token", ACCESS_TOKEN);
        GraphRequest graphRequest = new GraphRequest(null, String.format("%s/feed", pageId), bundle, HttpMethod.GET, callback);
        graphRequest.executeAsync();
    }

    public static class Callback implements GraphRequest.Callback {

        @Override
        public void onCompleted(GraphResponse response) {
            if (response.getError() != null) {
                onError(response.getError().getErrorMessage());
            } else {
                onSuccess(response.getRawResponse());
                onNextRequest(response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT));
            }
            onFinish();
        }

        protected void onSuccess(String data) {

        }

        protected void onError(String error) {

        }

        protected void onNextRequest(GraphRequest nextGraphRequest) {

        }

        protected void onFinish() {

        }
    }
}
