package com.moblico.example.push;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.moblico.http.HttpResponse;
import com.moblico.services.ServiceCallbacks;
import com.moblico.services.ServiceManager;

public class MainActivity extends Activity {
	/**
	 * The Sender ID we send to Google. See
	 * http://developer.android.com/google/gcm/gs.html. NOTE - This should be
	 * your Google-assigned sender ID.
	 */
	private static final String SENDER_ID = "579430396188";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
        setProgressBarIndeterminate(false);
        
		setContentView(R.layout.main);
		
		final Activity context = this;
		final Button registerButton = (Button) findViewById(R.id.btnRegister);
		final TextView resultsTextView = (TextView)findViewById(R.id.txtRegResult);
		
		final ServiceCallbacks registrationCallback = new ServiceCallbacks() {

			@Override
			public void onRequestReturned(Object obj, HttpResponse response) {
				// This is called when the registration is done
				registerButton.setEnabled(true);
				if (response.getError() != null) {
					resultsTextView.setText("Error registering: " + response.getError().getMessage());
				} else {
					resultsTextView.setText("Registered!");
				}
			}
		};
		
		OnClickListener registerButtonOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				registerButton.setEnabled(false);
				
    			// We register when the button is clicked - this could also be done
    			// when the app is launched.
				ServiceManager.services().push().registerAnonymous(context, SENDER_ID, registrationCallback);
			}
		};
		
		registerButton.setOnClickListener(registerButtonOnClickListener);
	}
}
