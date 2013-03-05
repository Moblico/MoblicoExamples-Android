package com.moblico.example.locations;

import java.util.ArrayList;

import com.moblico.entities.Location;
import com.moblico.http.HttpResponse;
import com.moblico.services.ServiceCallbacks;
import com.moblico.services.ServiceManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setProgressBarIndeterminate(false);
		
		if(savedInstanceState == null){
			LocationsFragment frag = new LocationsFragment();
			getFragmentManager().beginTransaction().add(android.R.id.content, frag).commit();
		}
		
	}

	public static class LocationsFragment extends ListFragment{
		
		ArrayList<Location> mLocations;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setRetainInstance(true);
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			if(mLocations == null){
				getActivity().setProgressBarIndeterminate(true);
				ServiceManager.services().locations().locations(null, null, null, null, null, new ServiceCallbacks() {
					
					@SuppressWarnings("unchecked")
					@Override
					public void onRequestReturned(Object locs, HttpResponse response) {
						if(getActivity() != null) getActivity().setProgressBarIndeterminate(false);
						if(response.getError() != null){
							Log.e(getClass().getName(), response.getError().getMessage());
						}
						else{
							mLocations = (ArrayList<Location>) locs;
							if(getActivity() != null){
								LocationsAdapter adapter = new LocationsAdapter(getActivity(), mLocations);
								setListAdapter(adapter);
							}
						}
					}
				});
			}
			else{
				LocationsAdapter adapter = new LocationsAdapter(getActivity(), mLocations);
				setListAdapter(adapter);
			}
		}
		
		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			Intent i = new Intent(getActivity(), DetailsActivity.class);
			i.putExtra(DetailsActivity.EXTRA_LOC, mLocations.get(position).toString());
			startActivity(i);
		}
	}
	
	public static class LocationsAdapter extends BaseAdapter{

		Context mContext;
		ArrayList<Location> mLocations;
		
		public LocationsAdapter(Context context, ArrayList<Location> locations){
			mContext = context;
			mLocations = locations;
		}
		
		@Override
		public int getCount() {
			if(mLocations == null) return 0;
			else return mLocations.size();
		}

		@Override
		public Object getItem(int position) {
			if(mLocations == null) return null;
			else return mLocations.get(position);
		}

		@Override
		public long getItemId(int position) {
			if(mLocations == null) return -1;
			else return Long.valueOf(mLocations.get(position).getId());
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = View.inflate(mContext, android.R.layout.simple_list_item_2, null);
			}
			final TextView t1 = (TextView)convertView.findViewById(android.R.id.text1);
			final TextView t2 = (TextView)convertView.findViewById(android.R.id.text2);
			final Location loc = mLocations.get(position);
			t1.setText(loc.getName());
			t2.setText(String.format("%s%n%s, %s", loc.getAddress_1(), loc.getCity(), loc.getStateOrProvince()));
			return convertView;
		}

		
	}

}
