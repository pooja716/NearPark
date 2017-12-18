package aksharproject.np;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    EditText mEdUserName, mEdPassword;
    Button mBtnLogin, mBtnRegister;
    public static final String TAG = Login.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEdUserName = (EditText) findViewById(R.id.edUserName);
        mEdPassword = (EditText) findViewById(R.id.edPassword);

        mBtnLogin = (Button) findViewById(R.id.btnLogin);
        mBtnRegister = (Button) findViewById(R.id.btnRegistration);

        mySharedPreference obj = new mySharedPreference();
        String UserName = obj.getSharedPreferenceString(getApplicationContext(),"UserName");
        Toast.makeText(getApplicationContext(),UserName,Toast.LENGTH_LONG).show();
        if(UserName != null){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
        }
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUserName = mEdUserName.getText().toString();
                String txtPassword = mEdPassword.getText().toString();
                //String txtRole = spRole.getSelectedItem().toString();

                if(txtUserName.isEmpty() || txtPassword.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter All the Values", Toast.LENGTH_LONG).show();
                }
                else {
                    login(txtUserName, txtPassword);
                }

            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Registration.class);
                startActivity(i);
            }
        });
    }

    void login(final String userName, String password) {
        //View view = getActivity().getCurrentFocus();
        //Global.getInstance().hideKeyboard(view,getActivity());
        //Log.e("Aheval",name + mobile + email + desi);
        //final ProgressDialog pDialog = new ProgressDialog(getApplicationContext());
        //pDialog.setMessage("Loading...");
        //pDialog.show();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserName", userName);
            jsonObject.put("Password", password);
            //jsonObject.put("Role", role);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        String regUrl = Constant.APP_BASE_URL + Constant.METHOD_LOGIN;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                regUrl, jsonObject,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //pDialog.hide();
                        try {
                            int status = response.getInt("status");

                            if(status==1){
                                JSONObject  userInfo = response.getJSONObject("info");
                                String uName = userInfo.getString("UserName");
                                //String uType = userInfo.getString("UserType");
                                //int uId = userInfo.getInt("Id");
                                if(TextUtils.isEmpty(uName)){
                                    Toast.makeText(getApplicationContext(),"Invalid UserName or Password",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    mySharedPreference obj = new mySharedPreference();
                                    obj.addSharedPreferenceString(getApplicationContext(),obj.KEY_NAME,uName);
                                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                                    startActivity(i);
                                    //obj.addSharedPreferenceString(getApplicationContext(),obj.KEY_Role,uType);
                                    //obj.addSharedPreferenceInt(getActivity(),obj.KEY_Id, uId);
                                    //Toast.makeText(getActivity(),"Registration Done Successfully",Toast.LENGTH_LONG).show();
                                    //getParent().setDrawerEnabled(true);
                                    //getParent().openAddSanchalakLA();
                                    //getParent().startApp(uType);
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Invalid UserName or Password",Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, response.toString());
                        //Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                // hide the progress dialog
                //pDialog.hide();
            }
        });

        String tag_json_obj = "Login";
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    /*private MainActivity getParent(){
        return (MainActivity)getActivity();
    }*/

}
