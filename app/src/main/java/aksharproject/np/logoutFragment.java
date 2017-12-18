package aksharproject.np;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static aksharproject.np.mySharedPreference.SHARED_PREFERENCE_FILE_NAME;
import static android.R.attr.data;


/**
 * A simple {@link Fragment} subclass.
 */
public class logoutFragment extends Fragment {
    EditText username;
    Button logout;
    public static final String TAG = logoutFragment.class.getSimpleName();

    public logoutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
      View v1= inflater.inflate(R.layout.fragment_logout, container, false);
        getActivity().setTitle("Logout");
        username=(EditText) v1.findViewById(R.id.edUserName);
        logout=(Button) v1.findViewById(R.id.btnLogout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              mySharedPreference obj = new mySharedPreference();
              String data=obj.removeSharedPreferenceString(getActivity(),obj.KEY_NAME);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(data,1);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(obj.KEY_NAME);
                editor.commit();
                Intent i = new Intent(getActivity(),Registration.class);
                startActivity(i);
            }
        });
        return v1;
    }

}
