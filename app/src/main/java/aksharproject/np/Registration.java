package aksharproject.np;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;


public class Registration extends AppCompatActivity {

    public static final String TAG = Registration.class.getSimpleName();
    EditText username;
    EditText password;
    EditText name;
    EditText address;
    EditText area;
    EditText city;
    EditText pincode;
    EditText mobileno;
    EditText email;
    TextView textView;
    Button button;
    String u_name, pwd, _name, _address, _area, _city, pin_code, mobile_no, _email;
    JSONObject jsonObject = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.address);
        area = (EditText) findViewById(R.id.area);
        city = (EditText) findViewById(R.id.city);
        pincode = (EditText) findViewById(R.id.pincode);
        mobileno = (EditText) findViewById(R.id.mobileno);
        email = (EditText) findViewById(R.id.email);
        //textView = (TextView) findViewById(R.id.link_registration);
        button = (Button) findViewById(R.id.regbtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    void doregistration(String username, String password, String name, String address, String area, String city, String pincode, String mobileno, String email) {
        //final ProgressDialog pDialog = new ProgressDialog(getApplicationContext());
        //pDialog.setMessage("Loading...");
        //pDialog.show();

        JSONObject jsonObject = new JSONObject();
        try {
            //jsonObject.put("dbFieldName", valueInAndroid);
            jsonObject.put("UserName", username);
            jsonObject.put("Password", password);
            jsonObject.put("Name", name);
            jsonObject.put("Address", address);
            jsonObject.put("Area", area);
            jsonObject.put("City", city);
            jsonObject.put("PinCode", pincode);
            jsonObject.put("MobileNo", mobileno);
            jsonObject.put("Email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String regUrl = Constant.APP_BASE_URL + Constant.METHOD_REGISTER;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                regUrl, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int status = response.getInt("status");
                            if (status == 2) {
                                Toast.makeText(getApplicationContext(), "Already Registered", Toast.LENGTH_LONG).show();
                            } else if (status == 1) {
                                //Toast.makeText(getActivity(), "Your Work is : " + String.valueOf(Percentage) + "%", Toast.LENGTH_LONG).show();
                                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
                                mySharedPreference obj = new mySharedPreference();
                                obj.addSharedPreferenceString(getApplicationContext(),"UserName",u_name);
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
                Toast.makeText(getApplicationContext(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                // hide the progress dialog
               // pDialog.hide();
            }
        });

        String tag_json_obj = "Registration";
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void register() {
        u_name = username.getText().toString();
        pwd = password.getText().toString();
        _name = name.getText().toString();
        _address = address.getText().toString();
        _area = area.getText().toString();
        _city = city.getText().toString();
        pin_code = pincode.getText().toString();
        mobile_no = mobileno.getText().toString();
        _email = email.getText().toString();

        if (!validate()) {
            Toast.makeText(getApplicationContext(), "Please give proper input", Toast.LENGTH_SHORT).show();
        } else {
            OnSignupSuccess();
        }
    }

    public void OnSignupSuccess() {
        //Intent intent = new Intent(Registration.this, Login.class);
        //startActivity(intent);
        Toast.makeText(getApplicationContext(),"Validated...",Toast.LENGTH_LONG).show();
        doregistration(u_name,pwd,_name,_address, _area,_city,pin_code,mobile_no,_email);
        Intent intent=new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
    }

    public boolean validate() {
        boolean valid = true;

        if (u_name.isEmpty() || u_name.length() > 20) {
            username.setError("enter valid username");
            Toast.makeText(getApplicationContext(),"Uname",Toast.LENGTH_LONG).show();
            return valid = false;
        } else {
            username.setError(null);
        }
        if (pwd.isEmpty() || pwd.length() < 5 || pwd.length() > 8) {
            password.setError("password must be inbetween 3 and 8");
            Toast.makeText(getApplicationContext(),"PWD",Toast.LENGTH_LONG).show();
            return valid = false;
        } else {
            password.setError(null);
        }
        if (_name.isEmpty() || _name.length() > 20) {
            name.setError("enter valid name");
            return valid = false;
        } else {
            name.setError(null);
        }
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
        if (pin_code.isEmpty() || pin_code.length() > 6) {
            pincode.setError("enter valid pincode");
            return valid = false;
        } else {
            pincode.setError(null);
        }
        if (mobile_no.isEmpty() || mobile_no.length() != 10) {
            mobileno.setError("enter valid mobileno");
            return valid = false;
        } else {
            mobileno.setError(null);
        }
        if (_email.isEmpty() || _email.length() > 20 || !android.util.Patterns.EMAIL_ADDRESS.matcher(_email).matches()) {
            email.setError("enter valid email");
            return valid = false;
        } else {
            email.setError(null);
        }

        return valid;
    }
}