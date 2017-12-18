package aksharproject.np;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;



public class parkingtimer extends Fragment {
    private static int timeHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private static int timeMinute = Calendar.getInstance().get(Calendar.MINUTE);
    TextView textView1;
    private static TextView textView2;
    Context mcontext;
    public static TextView getTextView2() {
        return textView2;
    }

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    public static final String TAG = parkingtimer.class.getSimpleName();

    public parkingtimer() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v1 = inflater.inflate(R.layout.fragment_parkingtimer, container, false);
        getActivity().setTitle("Parking Timer");
        textView1 = (TextView) v1.findViewById(R.id.msg1);
        textView1.setText(timeHour + ":" + timeMinute);
        textView2 = (TextView) v1.findViewById(R.id.msg2);
        Button btn1 = (Button) v1.findViewById(R.id.button1);
        Button btn2 = (Button) v1.findViewById(R.id.button2);
        alarmManager = (AlarmManager)getActivity().getSystemService(mcontext.ALARM_SERVICE);
        Intent myIntent = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, myIntent, 0);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView2.setText("");
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.HOUR, timeHour);
                bundle.putInt(Constant.MINUTE, timeMinute);

                MyDialogFragment fragment = new MyDialogFragment(new MyHandler());
                fragment.setArguments(bundle);
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(fragment, Constant.TIME_PICKER);
                transaction.commit();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView2.setText("");
                cancelAlarm();
            }
            private void cancelAlarm() {
                if (alarmManager != null) {
                    alarmManager.cancel(pendingIntent);
                }
            }
        });

        return v1;
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            timeHour = bundle.getInt(Constant.HOUR);
            timeMinute = bundle.getInt(Constant.MINUTE);
            textView1.setText(timeHour + ":" + timeMinute);
            setAlarm();
        }
        private void setAlarm() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, timeHour);
            calendar.set(Calendar.MINUTE, timeMinute);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }


    }
}
