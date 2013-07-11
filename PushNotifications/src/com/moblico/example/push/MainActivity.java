package com.moblico.example.push;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

		if (savedInstanceState == null) {
			PushFragment frag = new PushFragment();
			getFragmentManager().beginTransaction().add(android.R.id.content, frag).commit();
		}

	}

	public static class PushFragment extends Fragment implements OnClickListener, ServiceCallbacks {

		private Button registerButton;
		private TextView regResultTextView;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setRetainInstance(true);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.main, null);
			regResultTextView = (TextView) v.findViewById(R.id.txtRegResult);
			registerButton = (Button) v.findViewById(R.id.btnRegister);
			registerButton.setOnClickListener(this);
			return v;
		}

		@Override
		public void onClick(View v) {
			// We register when the button is clicked - this could also be done
			// when the app is launched.
			registerButton.setEnabled(false);
			ServiceManager.services().push()
					.registerAnonymous(getActivity(), SENDER_ID, this);
		}

		@Override
		public void onRequestReturned(Object obj, HttpResponse res) {
			// This is called when the registration is done
			registerButton.setEnabled(true);
			if (res.getError() != null) {
				regResultTextView.setText("Error registering: "
						+ res.getError().getMessage());
			} else {
				regResultTextView.setText("Registered!");
			}
		}

	}
}
