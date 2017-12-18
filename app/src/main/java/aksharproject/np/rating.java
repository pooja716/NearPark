package aksharproject.np;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;


public class rating extends Fragment {

    public rating() {
        // Required empty public constructor
    }

    RatingBar simpleRatingBar;
    Button submitButton;
    public static final String TAG = rating.class.getSimpleName();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v1 = inflater.inflate(R.layout.fragment_rating, container, false);
        getActivity().setTitle("RateUs");
        simpleRatingBar = (RatingBar) v1.findViewById(R.id.simpleRatingBar);
        submitButton = (Button) v1.findViewById(R.id.submitButton);// initiate a rating bar
        Float ratingNumber = simpleRatingBar.getRating(); // get rating number from a rating bar

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get values and then displayed in a toast
                String totalStars = "Total Stars:: " + simpleRatingBar.getNumStars();
                String rating = "Rating :: " + simpleRatingBar.getRating();
                Toast.makeText(getActivity(), totalStars + "\n" + rating, Toast.LENGTH_LONG).show();
            }
        });
        return v1;

    }
}
