package com.example.restaurant_management_20;

import android.app.Application;
import android.os.Bundle;


import com.parse.Parse;
import com.parse.ParseInstallation;

public class App extends Application {


    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                // if defined
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }

}
