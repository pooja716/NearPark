package aksharproject.np;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by acer on 10/18/2017.
 */
public class LotAdapter extends BaseAdapter {
    private Activity mactivity;
    private List<ParkingLot> mParkingList;
    private onBook mOnBook;
    private LayoutInflater inflater;

    public LotAdapter(Activity mactivity,List<ParkingLot> mParkingList, onBook mOnBook)
    {
        this.mactivity=mactivity;
        this.mParkingList = mParkingList;
        this.mOnBook = mOnBook;
        inflater = (LayoutInflater) mactivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mParkingList.size();
    }

    @Override
    public ParkingLot getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Viewholder v1;
        if(convertView == null)
        {
            convertView = inflater.inflate(R.layout.list_parking_slot,null);
            v1 = new Viewholder();
            v1.txtName = (TextView) convertView.findViewById(R.id.txtUser);
            v1.txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);
          //  v1.txtMobileno=(TextView)convertView.findViewById(R.id.txtMobile);
            v1.btnView = (Button) convertView.findViewById(R.id.btnBookParking);
            convertView.setTag(v1);
        }
        else
        {
            v1 = (Viewholder) convertView.getTag();
        }
        v1.txtName.setText(mParkingList.get(position).getUserName());
        v1.txtAddress.setText(mParkingList.get(position).getAddress());
       // v1.txtMobileno.setText(mParkingList.get(position).getMobileno());
        v1.btnView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                //Toast.makeText(mactivity, mParkingList.get(position).getArea(),Toast.LENGTH_LONG).show();
                mOnBook.onBookPressed(mParkingList.get(position).getId());
            }
        });

        return convertView;
    }

    static class Viewholder
    {
        TextView txtName;
        TextView txtAddress;
      //  TextView txtMobileno;
        Button btnView;
    }
}
