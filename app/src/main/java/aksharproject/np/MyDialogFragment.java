package aksharproject.np;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TimePicker;

/**
 * Created by pooja on 20-11-2017.
 */

public class MyDialogFragment extends DialogFragment {
    private int timeHour;
    private int timeMinute;
    private Handler handler;

    public MyDialogFragment(Handler handler){this.handler=handler;}
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        timeHour = bundle.getInt(Constant.HOUR);
        timeMinute = bundle.getInt(Constant.MINUTE);
        TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeHour = hourOfDay;
                timeMinute = minute;
                Bundle b = new Bundle();
                b.putInt(Constant.HOUR, timeHour);
                b.putInt(Constant.MINUTE, timeMinute);
                Message msg = new Message();
                msg.setData(b);
                handler.sendMessage(msg);
            }
        };
        return new TimePickerDialog(getActivity(), listener, timeHour, timeMinute, false);
    }
}
