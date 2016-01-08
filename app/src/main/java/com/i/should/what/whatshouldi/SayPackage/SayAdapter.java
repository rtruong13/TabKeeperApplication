package com.i.should.what.whatshouldi.SayPackage;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.i.should.what.whatshouldi.R;

import java.util.List;

/**
 * Created by ryan on 7.7.2015.
 */
public class SayAdapter extends ArrayAdapter<SayModel> {

    ListView listView;

    public SayAdapter(Context context, int resource, List<SayModel> objects, ListView list) {
        super(context, resource, objects);
        listView = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if(row == null)
        {
            row = LayoutInflater.from(getContext()).inflate(R.layout.say_row_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(row, position);
            row.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) row.getTag();
        viewHolder.addClick(position);
        viewHolder.SetModel(getItem(position));
        return row;
    }

    public class ViewHolder{
        public TextView subSection;
        public View rippleView;

        public ViewHolder(View v, int pos)
        {
            subSection = (TextView) v.findViewById(R.id.nameOfSubSec);
            rippleView = v.findViewById(R.id.rowRippleView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                subSection.setTransitionName("transtext" + pos);
            }
        }

        public void SetModel(SayModel sayModel)
        {
            subSection.setText(sayModel.name);
        }

        public void addClick(final int position) {
            rippleView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = MotionEventCompat.getActionMasked(event);
                    String DEBUG_TAG ="debug";
                    switch(action) {
                        case (MotionEvent.ACTION_DOWN) :
                            Log.d(DEBUG_TAG, "Action was DOWN");
                            return false;
                        case (MotionEvent.ACTION_UP) :
                            Log.d(DEBUG_TAG,"Action was UP");
                            return false;
                        case (MotionEvent.ACTION_CANCEL) :
                            Log.d(DEBUG_TAG,"Action was CANCEL");
                            return false;
                        case (MotionEvent.ACTION_OUTSIDE) :
                            Log.d(DEBUG_TAG,"Movement occurred outside bounds " +
                                    "of current screen element");
                            return false;
                    }
                    return true;
                }
            });
            rippleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run(){
                            listView.performItemClick(getView(position, null, null),
                                    position,
                                    getItemId(position));
                        }
                    }, 200);
                }
            });
        }
    }
}