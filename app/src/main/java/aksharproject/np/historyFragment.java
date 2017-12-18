package aksharproject.np;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class historyFragment extends Fragment {

Button btn;
    public historyFragment() {
        // Required empty public constructor
    }

    public static final String TAG = historyFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v1 = inflater.inflate(R.layout.fragment_history, container, false);

        getActivity().setTitle("History");
        return v1;
    }
}
