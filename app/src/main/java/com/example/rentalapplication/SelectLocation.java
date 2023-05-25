package com.example.rentalapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SelectLocation extends AppCompatActivity {

    MapView map;
    EditText location;
    TextView getLocation;
    String locationName;
    String longitude="";
    String latitude="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        location = findViewById(R.id.location);
        getLocation = findViewById(R.id.getLocation);

        locationName = location.getText().toString().trim();



        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(longitude.isEmpty() && latitude.isEmpty()){
                    Toast.makeText(getApplicationContext(),longitude + " Please select the place",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent= new Intent(SelectLocation.this,AddRooms.class);
                    intent.putExtra("longitude",longitude);
                    intent.putExtra("latitude",latitude);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }


            }
        });

        location.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId != EditorInfo.IME_ACTION_SEARCH) {
                    location.clearFocus();
                    String query = location.getText().toString();
                    if (query != "") {
                        getLocationName(query);
                    } else {
                        Toast.makeText(SelectLocation.this, "Please search something", Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(SelectLocation.this, "hi there", Toast.LENGTH_LONG).show();

                }
                return true;
            }
        });

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string


        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);
        map.setTilesScaledToDpi(true);


        Overlay touchOverlay = new Overlay(this){
            ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay = null;
            @Override
            public void draw(Canvas arg0, MapView arg1, boolean arg2) {

            }


            @Override
            public boolean onSingleTapConfirmed(final MotionEvent e, final MapView mapView) {

                final Drawable marker = getApplicationContext().getResources().getDrawable(R.drawable.markericon);
                Projection proj = mapView.getProjection();
                GeoPoint loc = (GeoPoint) proj.fromPixels((int)e.getX(), (int)e.getY());
                longitude = Double.toString(((double)loc.getLongitudeE6())/1000000);
                latitude = Double.toString(((double)loc.getLatitudeE6())/1000000);
//                System.out.println("- Latitude = " + latitude + ", Longitude = " + longitude );

                Toast.makeText(getApplicationContext(),longitude + " longitude",Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(),latitude + " latitude",Toast.LENGTH_LONG).show();

                ArrayList<OverlayItem> overlayArray = new ArrayList<OverlayItem>();
                OverlayItem mapItem = new OverlayItem("", "", new GeoPoint((((double)loc.getLatitudeE6())/1000000), (((double)loc.getLongitudeE6())/1000000)));
                mapItem.setMarker(marker);
                overlayArray.add(mapItem);
                if(anotherItemizedIconOverlay==null){
                    anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray,null);
                    mapView.getOverlays().add(anotherItemizedIconOverlay);
                    mapView.invalidate();
                }else{
                    mapView.getOverlays().remove(anotherItemizedIconOverlay);
                    mapView.invalidate();
                    anotherItemizedIconOverlay = new ItemizedIconOverlay<OverlayItem>(getApplicationContext(), overlayArray,null);
                    mapView.getOverlays().add(anotherItemizedIconOverlay);
                }
                return true;
            }
        };
        map.getOverlays().add(touchOverlay);


    }

    private void getLocationName(String query) {

        try {
            Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> geoResults = geocoder.getFromLocationName(query, 1);
            if (!geoResults.isEmpty()) {
                Address addr = geoResults.get(0);
                Double latitude = addr.getLatitude();
                Double longitude = addr.getLongitude();

                Toast.makeText(SelectLocation.this, latitude.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(SelectLocation.this, longitude.toString(), Toast.LENGTH_LONG).show();
                location.setText("");
                GeoPoint location = new GeoPoint(addr.getLatitude(), addr.getLongitude());
                moveCameraMap(location);

            } else {
                Toast.makeText(getBaseContext(), "Location Not Found", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void moveCameraMap(GeoPoint location) {

        IMapController mapController = map.getController();
        mapController.setZoom(13.5);
        GeoPoint startPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        mapController.setCenter(startPoint);



    }




    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, Displayrooms.class);
        startActivity(intent);

    }
}

