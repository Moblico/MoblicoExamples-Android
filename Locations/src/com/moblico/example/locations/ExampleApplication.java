package com.moblico.example.locations;

import com.moblico.services.ServiceManager;
import com.moblico.services.ServiceManager.Environment;

import android.app.Application;

public class ExampleApplication extends Application {

	
	@Override
	public void onCreate() {
		super.onCreate();
		final String moblicoAPIKey = "648c35dd-886b-4733-b8db-6d062c9b3fc4";
		ServiceManager.services().init(this, moblicoAPIKey, Environment.TESTING, 4);
	}
}
