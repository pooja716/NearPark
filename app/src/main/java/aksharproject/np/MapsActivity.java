package aksharproject.np;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    Calendar myCalendar = Calendar.getInstance();
    private GoogleMap mMap;
    EditText username, mEdDate, mEdTime, mEdHour;
    EditText name;
    EditText address;
    EditText area;
    EditText city;
    String u_name, _name, _address, _area, _city, _day, _tim, _hor;
    int _hour, _startTime;
    Button btnViewMap;
    List<android.location.Address> mAddressList;
    Geocoder mGeoCoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        address = (EditText) findViewById(R.id.address);
        area = (EditText) findViewById(R.id.area);
        city = (EditText) findViewById(R.id.city);
        mEdDate = (EditText) findViewById(R.id.day);
        mEdTime = (EditText) findViewById(R.id.tim);
        mEdHour = (EditText) findViewById(R.id.hour);
        btnViewMap = (Button) findViewById(R.id.p_btn);
        /*final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        mEdDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(getApplicationContext(),date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        mEdTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getApplicationContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mEdTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });*/
        btnViewMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int TotalTime = Integer.parseInt(mEdTime.getText().toString()) + Integer.parseInt(mEdHour.getText().toString());
                getParkingLot(area.getText().toString(),city.getText().toString(),mEdDate.getText().toString(),TotalTime);
            }
        });

        mGeoCoder = new Geocoder(this);
        mapFragment.getMapAsync(this);
    }
    void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEdDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void getParkingLot(String Area, String City, String date, int TotalTime) {
        final ProgressDialog pDialog = new ProgressDialog(MapsActivity.this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Area", Area);
            jsonObject.put("City", City);
            jsonObject.put("Day", date);
            jsonObject.put("TotalTime", TotalTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getActivity(),jsonObject.toString(),Toast.LENGTH_LONG).show();
        String regUrl = Constant.APP_BASE_URL + Constant.METHOD_GET_MAP;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                regUrl, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        try {
                            int status = response.getInt("status");
                            //Toast.makeText(getActivity(),String.valueOf(status),Toast.LENGTH_LONG).show();
                            if(status==1){
                                JSONArray ReportArray = response.getJSONArray("info");
                                List<ParkingLot> ParkingList = new ArrayList<>();
                                for(int i=0;i<ReportArray.length();i++){
                                    //mandalList.add(ReportArray.getJSONObject(i).getString("mandalName"));
                                    ParkingLot singleParkingLot = new ParkingLot();
                                    singleParkingLot.setArea(ReportArray.getJSONObject(i).getString("Area"));
                                    singleParkingLot.setAddress(ReportArray.getJSONObject(i).getString("Address"));
                                    singleParkingLot.setCity(ReportArray.getJSONObject(i).getString("City"));
                                    ParkingList.add(singleParkingLot);

                                    //Generate .xls
                                    //generateXLS(singleKaryakar);
                                }

                                if(!ParkingList.isEmpty()) {
                                    for(ParkingLot Single : ParkingList){
                                        String city = Single.getCity();
                                        String area = Single.getArea();
                                        String mLoc = area + "," + city;
                                        //String mLoc = "Akshardham, Gandhinagar";

                                        try {
                                            Toast.makeText(getApplicationContext(),"Plot Called" + mLoc,Toast.LENGTH_LONG).show();
                                            mAddressList = mGeoCoder.getFromLocationName(mLoc,1);
                                            android.location.Address mSingleAddress = mAddressList.get(0);
                                            LatLng mLatLng = new LatLng(mSingleAddress.getLatitude(),mSingleAddress.getLongitude());
                                            mMap.addMarker(new MarkerOptions().position(mLatLng).title(Single.getAddress()));
                                            mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Error.. Please try again..",Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Error Loading Data",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //Log.d(TAG, response.toString());
                        //Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d("BAPS", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                // hide the progress dialog
                pDialog.hide();
            }
        });

        String tag_json_obj = "MApsActivity";
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        try {
            mAddressList = mGeoCoder.getFromLocationName("Akshardham,Gandhinagar",1);
            android.location.Address mSingleAddress = mAddressList.get(0);
            LatLng mLatLng = new LatLng(mSingleAddress.getLatitude(),mSingleAddress.getLongitude());
            mMap.addMarker(new MarkerOptions().position(mLatLng).title("Pethapur"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mLatLng));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
