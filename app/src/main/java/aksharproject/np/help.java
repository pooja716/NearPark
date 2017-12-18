package aksharproject.np;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class help extends Fragment {
    public static final String TAG = help.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v1 = inflater.inflate(R.layout.fragment_help, container, false);

        getActivity().setTitle("Help");
        return v1;
    }

    public help() {
        // Required empty public constructor
    }
}
