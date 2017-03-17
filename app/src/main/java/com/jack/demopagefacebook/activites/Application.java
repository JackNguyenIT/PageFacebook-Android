package com.jack.demopagefacebook.activites;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Jack on 3/14/17.
 */

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);

    }
}
