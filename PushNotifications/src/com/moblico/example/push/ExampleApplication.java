package com.moblico.example.push;

import com.moblico.services.ServiceManager;
import com.moblico.services.ServiceManager.Environment;

import android.app.Application;

public class ExampleApplication extends Application {

	
	@Override
	public void onCreate() {
		super.onCreate();
		final String moblicoAPIKey = "1c1202b6-7a36-4451-ba32-fe57f9a6c8d4";
		ServiceManager.services().init(this, moblicoAPIKey, Environment.PRODUCTION, 4);
	}
}
