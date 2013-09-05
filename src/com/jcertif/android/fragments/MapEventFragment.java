package com.jcertif.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jcertif.android.R;

public class MapEventFragment extends RESTResponderFragment {

	MapView mapv;
	GoogleMap gmap;
	Marker marker;
	LatLng initPosition;
	
	public MapEventFragment(){
		super();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setRetainInstance(true);
		View rootView=inflater.inflate(R.layout.fragment_map, container,false);
		
		getActivity().setTitle(R.string.app_name);
		setHasOptionsMenu(true);
		
		mapv=(MapView)rootView.findViewById(R.id.mapview);
		mapv.onCreate(savedInstanceState);
		
		onMapReady();
		
		return rootView;
	}

	
	public void onMapReady() {
		
		initPosition=new LatLng(-4.257399,15.283935);
		
		gmap=mapv.getMap();
		gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		gmap.getUiSettings().setCompassEnabled(true);
		try{
			MapsInitializer.initialize(this.getSherlockActivity());
		}catch(GooglePlayServicesNotAvailableException e){
			e.printStackTrace();
		}
		//Move the camera instantly to initial position with a zoom of 17.
		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(initPosition, 17));
		// Zoom in, animating the camera.
		gmap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null); 
		//Initialize the marker
		marker=gmap
				.addMarker(new MarkerOptions()
				.title("Brazzaville")
				.visible(true)
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker)));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapv.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapv.onLowMemory();
	}

	@Override
	public void onResume() {
		mapv.onResume();
		super.onResume();
	}

	@Override
	public void onRESTResult(int code, Bundle result) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
}
