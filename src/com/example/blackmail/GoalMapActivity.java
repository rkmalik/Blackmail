package com.example.blackmail;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;

public class GoalMapActivity extends FragmentActivity implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener, OnClickListener {

	private GoogleMap map;
	private LocationClient lc;
	private int numMarkers;
	private Marker goalMarker;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goal_map);
		lc = new LocationClient(this,this,this);
		numMarkers = 0; //number of markers on the map
		
		View deleteButton = findViewById(R.id.deleteMarkerButton);
		View continueButton = findViewById(R.id.continuePlanningButton);
		deleteButton.setOnClickListener(this);
		continueButton.setOnClickListener(this);
	}
	
	protected void onResume(){
		super.onResume();
		checkMapAndLC();
		
	}
	
	//If the map or location client was destroyed, recreate them.
	//This is a safety measure more than anything, it shouldn't be important
	//unless someone leaves their phone on this screen for too long.
	protected void checkMapAndLC(){
		if(map == null){
			//Get map from support fragment
			SupportMapFragment mapFrag = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
			map = mapFrag.getMap();
			if(map != null) {
				map.setMyLocationEnabled(true); //Allow location button on map
				map.setMapType(GoogleMap.MAP_TYPE_HYBRID); //Changes how map looks
				map.setOnMapClickListener(new OnMapClickListener() {

			        @Override
			        public void onMapClick(LatLng point) {
			        	//Currently I'm writing this as if they can only designate one location.
			    		//In the future, if we allow multiple locations, this has to be changed.
			    		//It currently doesn't work, but I'll figure it out soon.
			        	if(numMarkers == 0){
			    			goalMarker = map.addMarker(new MarkerOptions().position(point).title("Goal Location")
			    					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			    			//Update the map
			    			numMarkers += 1;
			    			Toast toast = Toast.makeText(getApplicationContext(), (CharSequence)"Tried placing marker", Toast.LENGTH_SHORT);
			    			toast.show();
			    		}
			        	//Tell the user only one marker can be added at a time.
			        	else {
			        		Toast onlyOne = Toast.makeText(getApplicationContext(), (CharSequence)"Only one marker can be added at a time.", Toast.LENGTH_SHORT);
			    			onlyOne.show();
			        	}
			        }
			    });
			}
		}
		if(lc == null){
			lc = new LocationClient(this, this, this);
			lc.connect();
		}
	}
	
	
	protected void onStop(){
		lc.disconnect();
		super.onStop();
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		//Don't care
		
	}
	@Override
	public void onConnected(Bundle connectionHint) {
		Location userLoc = lc.getLastLocation();
		CameraUpdate beginCam = CameraUpdateFactory.newLatLngZoom(new LatLng(userLoc.getLatitude(), userLoc.getLongitude()), 16f);
		map.moveCamera(beginCam);
	}
	@Override
	public void onDisconnected() {
		//Don't care
		
	}

	@Override
	public void onLocationChanged(Location location) {
		//Don't care, only polling once.
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){	
			case R.id.deleteMarkerButton:
				if(numMarkers > 0){
					if(goalMarker != null){
						goalMarker.remove();
						numMarkers=0;
					}
				    Toast deleteToast = Toast.makeText(getApplicationContext(), (CharSequence)"Deleted marker.", Toast.LENGTH_SHORT);
				    deleteToast.show();
				}
				else {
					Toast noneToDelete = Toast.makeText(getApplicationContext(), (CharSequence)"No marker to delete.", Toast.LENGTH_SHORT);
				    noneToDelete.show();
				}
				break;
			case R.id.continuePlanningButton:
					Intent choices = new Intent(this, GoalChoicesActivity.class);
					startActivity(choices);
					break;
			default:
				break;
		}
	}
	
}