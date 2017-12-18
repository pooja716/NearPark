package aksharproject.np;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class Book_a_lot extends Fragment implements onBook{

    Calendar myCalendar = Calendar.getInstance();
    EditText username, mEdDate, mEdTime, mEdHour;
    EditText name;
    EditText address;
    EditText area;
    EditText city;

    String u_name, _name, _address, _area, _city, _day, _tim, _hor;
    int _hour, _startTime;
    public static final String TAG = Book_a_lot.class.getSimpleName();
    public ListView mListParkingSlot;
    private LotAdapter mParkingListAdapter;
    Button btnViewReport;

    public Book_a_lot() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v1 = inflater.inflate(R.layout.fragment_book_a_lot, container, false);

        getActivity().setTitle("Book a Lot");

        address = (EditText) v1.findViewById(R.id.address);
        area = (EditText) v1.findViewById(R.id.area);
        city = (EditText) v1.findViewById(R.id.city);
        mEdDate = (EditText) v1.findViewById(R.id.day);
        mEdTime = (EditText) v1.findViewById(R.id.tim);
        mEdHour = (EditText) v1.findViewById(R.id.hour);
        mListParkingSlot = (ListView) v1.findViewById(R.id.ListParking);
        btnViewReport = (Button) v1.findViewById(R.id.p_btn);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

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
                new DatePickerDialog(getActivity(),date, myCalendar
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
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        mEdTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        btnViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int TotalTime = Integer.parseInt(mEdTime.getText().toString()) + Integer.parseInt(mEdHour.getText().toString());
                getParkingLot(area.getText().toString(),city.getText().toString(),mEdDate.getText().toString(),TotalTime);
            }
        });
        return v1;

    }
    void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEdDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void getParkingLot(String Area, String City, String date, int TotalTime) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
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
        String regUrl = Constant.APP_BASE_URL + Constant.METHOD_GET_PARKING_LOT;
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
                                    singleParkingLot.setUserName(ReportArray.getJSONObject(i).getString("UserName"));
                                    singleParkingLot.setAddress(ReportArray.getJSONObject(i).getString("Address"));
//                                    singleParkingLot.setMobileno(ReportArray.getJSONObject(i).getString("mobileNo"));
                                    singleParkingLot.setId(ReportArray.getJSONObject(i).getInt("id"));
                                    ParkingList.add(singleParkingLot);

                                    //Generate .xls
                                    //generateXLS(singleKaryakar);
                                }

                                if(!ParkingList.isEmpty()) {
                                    mParkingListAdapter = new LotAdapter(getActivity(),ParkingList,Book_a_lot.this);
                                    Toast.makeText(getActivity(),ParkingList.get(0).getUserName().toString(),Toast.LENGTH_LONG).show();
                                    mListParkingSlot.setAdapter(mParkingListAdapter);
                                    //generateXLS();
                                }
                                else{
                                    Toast.makeText(getActivity(),"Error.. Please try again..",Toast.LENGTH_LONG).show();
                                }
                            }
                            else{
                                Toast.makeText(getActivity(),"Error Loading Data",Toast.LENGTH_LONG).show();
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
                Toast.makeText(getActivity(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                // hide the progress dialog
                pDialog.hide();
            }
        });

        String tag_json_obj = "Book_a_lot";
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }



    @Override
    public void onBookPressed(int id) {
        mySharedPreference obj = new mySharedPreference();
        String UserName = obj.getSharedPreferenceString(getActivity(),"UserName");
        BookParkingSlot(id,UserName);
    }

    private void BookParkingSlot(int id, String UserName) {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("BookBy", UserName);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getActivity(),jsonObject.toString(),Toast.LENGTH_LONG).show();
        String regUrl = Constant.APP_BASE_URL + Constant.METHOD_BOOK_PARKING;
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
                                Toast.makeText(getActivity(),"Parking Slot Booked",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(getActivity(),"Error Loading Data",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //VolleyLog.d("BAPS", "Error: " + error.getMessage());
                Toast.makeText(getActivity(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                // hide the progress dialog
                pDialog.hide();
            }
        });

        String tag_json_obj = "Book_a_lot";
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
}
