package com.jack.demopagefacebook.objects;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jack on 3/14/17.
 */

public class Page {

    public static Page pareJson(String data) {
        if (!TextUtils.isEmpty(data)) {
            try {
                JSONObject json = new JSONObject(data);
                Page page = new Page();
                page.id = json.optString("id");
                page.name = json.optString("name");
                JSONObject jsonPicture = json.getJSONObject("picture").getJSONObject("data");
                if (jsonPicture != null) {
                    page.url = jsonPicture.optString("url");
                }
                return page;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String id;
    public String name;
    public String url;

}
