package com.example.blackmail;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;

public class GoalMapActivity extends FragmentActivity implements LocationListener, ConnectionCallbacks, OnConnectionFailedListener {

	private GoogleMap map;
	private LocationClient lc;
	private int numMarkers;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.goal_map);
		lc = new LocationClient(this,this,this);
		numMarkers = 0; //number of markers on the map
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
	
	public void onMapClick(LatLng point) {
		//Currently I'm writing this as if they can only designate one location.
		//In the future, if we allow multiple locations, this has to be changed.
		//It currently doesn't work, but I'll figure it out soon.
		if(numMarkers == 0){
			map.addMarker(new MarkerOptions().position(point).title("Goal Location")
					.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
			//Update the map
			CameraPosition pos = map.getCameraPosition();
			numMarkers += 1;
			Toast toast = Toast.makeText(getApplicationContext(), (CharSequence)"Tried placing marker", Toast.LENGTH_SHORT);
			toast.show();
		}
		//Tell user only one waypoint can be added or ignored
	}

}
