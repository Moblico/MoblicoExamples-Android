package com.moblico.example.locations;

import com.google.gson.Gson;
import com.moblico.entities.Location;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailsActivity extends Activity {

	public static final String EXTRA_LOC = "extra.loc";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details);
		
		final Gson g = new Gson();
		final Location loc = g.fromJson(getIntent().getExtras().getString(EXTRA_LOC), Location.class);
		
		final TextView name = (TextView)findViewById(R.id.name);
		final TextView address = (TextView)findViewById(R.id.address);
		final TextView phone = (TextView)findViewById(R.id.phone);
		
		name.setText(loc.getName());
		address.setText(String.format("%s%n%s, %s %s", loc.getAddress_1(), loc.getCity(), loc.getStateOrProvince(), loc.getPostalCode()));
		phone.setText(loc.getPhone());
	}
}
