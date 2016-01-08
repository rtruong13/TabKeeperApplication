package com.i.should.what.whatshouldi.DoPackage;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.i.should.what.whatshouldi.DoPackage.Models.DoDayModel;
import com.i.should.what.whatshouldi.R;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by ryan on 8/29/2015.
 */
public class DoRecyclerAdapter extends RecyclerView.Adapter<DoRecyclerAdapter.DoViewHolder> {

    private final Context context;
    List<DoDayModel> dayModels;

    public DoRecyclerAdapter(List<DoDayModel> dayModels, Context c)
    {
        context = c;
        this.dayModels = dayModels;
    }

    @Override
    public DoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.do_item_layout, parent, false);
        return new DoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DoViewHolder holder, int position) {
        holder.bind(dayModels.get(position));
    }

    @Override
    public int getItemCount() {
        return dayModels.size();
    }

    public class DoViewHolder extends RecyclerView.ViewHolder{

        TextView dayView, monthView;
        View watchLayout, listenLayout;
        RecyclerView watchRView, listenRView;
        DoDayModel model;

        public DoViewHolder(View itemView) {
            super(itemView);

            dayView = (TextView) itemView.findViewById(R.id.dayDate);
            monthView = (TextView) itemView.findViewById(R.id.mDate);

            watchLayout = itemView.findViewById(R.id.watchLayout);
            listenLayout = itemView.findViewById(R.id.listenLayout);

            watchRView = (RecyclerView) itemView.findViewById(R.id.watchList);
            listenRView = (RecyclerView) itemView.findViewById(R.id.listenList);
        }

        public void bind(DoDayModel doDayModel) {
            model = doDayModel;

            Calendar calendar = new GregorianCalendar();
            calendar.setTime(model.date);

            dayView.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
            String myDate =
                String.valueOf(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)) + " '"
                + String.valueOf(calendar.get(Calendar.YEAR));
            monthView.setText(myDate);

            if(model.listenList == null || model.listenList.size()<=0)
            {
                listenLayout.setVisibility(View.GONE);
            }else{
                listenLayout.setVisibility(View.VISIBLE);
                DoListenRecyclerAdapter listenAdapter = new DoListenRecyclerAdapter(model.listenList, context);
                listenRView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                listenRView.setAdapter(listenAdapter);
            }

            if(model.watchList== null || model.watchList.size()<=0)
            {
                watchLayout.setVisibility(View.GONE);
            }else{
                watchLayout.setVisibility(View.VISIBLE);
                DoWatchRecyclerAdapter watchAdapter = new DoWatchRecyclerAdapter(model.watchList, context);
                watchRView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                watchRView.setAdapter(watchAdapter);
            }
        }
    }
}