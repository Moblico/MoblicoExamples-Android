package com.moblico.example.push;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.moblico.entities.User;
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

	public static class PushFragment extends Fragment {

		private String username;
		private String password;

		private Button registerButton;
		private TextView userNameTextView;
		private TextView regResultTextView;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setRetainInstance(true);
			username = Settings.Secure.getString(getActivity()
					.getContentResolver(), Settings.Secure.ANDROID_ID);
			password = username.substring(1);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View v = inflater.inflate(R.layout.main, null);
			registerButton = (Button) v.findViewById(R.id.btnRegister);
			registerButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ServiceManager.services().push().register(
						getActivity(), SENDER_ID, new ServiceCallbacks() {
							@Override
							public void onRequestReturned(Object obj, HttpResponse res) {
								if (res.getError() != null) {
									regResultTextView.setText(
											"Error registering: "
												+ res.getError().getMessage());
								} else {
									regResultTextView.setText("Registered!");
								}
							}
						});
				}
			});
			userNameTextView = (TextView) v.findViewById(R.id.txtUsername);
			regResultTextView = (TextView) v.findViewById(R.id.txtRegResult);
			ServiceManager.services().setUser(username, password);
			ServiceManager.services().user().getUser(username, checkUserCallback);
			return v;
		}

		private final ServiceCallbacks checkUserCallback = new ServiceCallbacks() {
			@Override
			public void onRequestReturned(Object obj, HttpResponse response) {
				if (obj instanceof User) {
					userNameTextView.setText("User Name: \n" + username + "\n");
					registerButton.setEnabled(true);
				} else {
					User user = new User();
					user.setUsername(username);
					user.setPassword(password);
					ServiceManager.services().setUser(null, null);
					ServiceManager.services().user().registerUser(user, createUserCallbacks);
				}
			}
		};

		private final ServiceCallbacks createUserCallbacks = new ServiceCallbacks() {
			@Override
			public void onRequestReturned(Object obj, HttpResponse response) {
				if (response.getError() != null) {
					userNameTextView.setText("Error registering user: \n"
							+ response.getError().getMessage());
				} else {
					ServiceManager.services().setUser(username, password);
					userNameTextView.setText("User Name: \n" + username + "\n");
					registerButton.setEnabled(true);
				}
			}
		};
	}
}
