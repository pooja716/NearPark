package aksharproject.np;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class Provide_a_lot extends Fragment {
    Calendar myCalendar = Calendar.getInstance();
    EditText username, mEdDate, mEdTime, mEdHour;
    EditText name;
    EditText address;
    EditText area;
    EditText city;
    EditText mobile;
    Button button;
    String u_name, _name, _address, _area, _city, _day, _tim, _hor,_mob;
    int _hour, _startTime;

    public static final String TAG = Provide_a_lot.class.getSimpleName();


    public Provide_a_lot() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v1 = inflater.inflate(R.layout.fragment_provide_a_lot, container, false);
        getActivity().setTitle("Provide a Lot");

        address = (EditText) v1.findViewById(R.id.address);
        area = (EditText) v1.findViewById(R.id.area);
        city = (EditText) v1.findViewById(R.id.city);
        mobile=(EditText)v1.findViewById(R.id.mobileno);
        mEdDate = (EditText) v1.findViewById(R.id.day);
        mEdTime = (EditText) v1.findViewById(R.id.tim);
        mEdHour = (EditText) v1.findViewById(R.id.hour);
        button = (Button) v1.findViewById(R.id.p_btn);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
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
        return v1;
    }
    void updateLabel() {
        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mEdDate.setText(sdf.format(myCalendar.getTime()));
    }
    public void submit() {

        _address = address.getText().toString();
        _area = area.getText().toString();
        _city = city.getText().toString();
       // _mob = mobile.getText().toString();
        _day = mEdDate.getText().toString();
        _tim = mEdTime.getText().toString();
        _hor=mEdHour.getText().toString();


        if (!validate()) {
            Toast.makeText(getActivity().getApplication(), "invalid information", Toast.LENGTH_SHORT).show();
        } else {
            _hour = Integer.parseInt(_hor);
            _startTime = Integer.parseInt(_tim);
            if(_startTime < 10 || _hour > 18){
                mEdTime.setError("Please Enter Time Between 10 to 18");
            }
            if(_hour < 0 || _hour > 8){
                mEdHour.setError("Hours may be between 1 to 8");
            }
            //OnSubmit();
            mySharedPreference obj = new mySharedPreference();
            String u_name = obj.getSharedPreferenceString(getActivity(), obj.KEY_NAME);
            addLot(u_name, _address, _area, _city, _day, _tim, _startTime, _hour,_startTime + _hour);

        }
    }

    void addLot(String uname, String address, String area, String city, String Day, String Tim, int StartTime, int Hour, int TotalTime) {
        //final ProgressDialog pDialog = new ProgressDialog(getActivity());
        //pDialog.setMessage("Loading...");
        //pDialog.show();

        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("dbFieldName", valueInAndroid);
            jsonObject.put("UserName", uname);
            jsonObject.put("Address", address);
            jsonObject.put("Area", area);
            jsonObject.put("City", city);
            jsonObject.put("Day", _day);
            jsonObject.put("TotalTime", TotalTime);
            jsonObject.put("Tim", _startTime);
            jsonObject.put("Hour", _hour);
           // jsonObject.put("mobileNo", _mob);
            jsonObject.put("isActive", 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String regUrl = Constant.APP_BASE_URL + Constant.METHOD_PROVIDE_LOT;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                regUrl, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 2) {
                                Toast.makeText(getActivity(), "Parking lot Already Exist", Toast.LENGTH_LONG).show();
                            } else if (status == 1) {
                                //Toast.makeText(getActivity(), "Your Work is : " + String.valueOf(Percentage) + "%", Toast.LENGTH_LONG).show();
                                Toast.makeText(getActivity(), "Parking Slot added Successfully", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, response.toString());
                        //Toast.makeText(getActivity(), response.toString(), Toast.LENGTH_LONG).show();

                        //pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                // hide the progress dialog
                //pDialog.hide();
            }
        });

        String tag_json_obj = "Provide_a_lot";
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    public void OnSubmit() {
        Intent intent = new Intent(getActivity().getApplication(), Login.class);
        startActivity(intent);
    }

    public boolean validate() {
        boolean valid = true;

        if (_address.isEmpty() || _address.length() > 100) {
            address.setError("enter valid address");
            return valid = false;
        } else {
            address.setError(null);
        }
        if (_city.isEmpty() || _city.length() > 20) {
            city.setError("enter valid city");
        } else {
            city.setError(null);
        }
        if(_tim.isEmpty()){
            mEdTime.setError("Enter Time");
        }
        if(_hor.isEmpty()){
            mEdHour.setError("Enter Time");
        }
       // if(_mob.isEmpty() || _mob.length() < 10){
        //    mobile.setError("Enter 10 digit number");
       // }
        return valid;
    }
}
