package com.i.should.what.whatshouldi.DoPackage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.i.should.what.whatshouldi.DoPackage.Models.DoDayModel;
import com.i.should.what.whatshouldi.DoPackage.Models.DoModel;
import com.i.should.what.whatshouldi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ryan on 7/29/2015.
 */
public class DoWatchRecyclerAdapter extends RecyclerView.Adapter<DoWatchRecyclerAdapter.DoWatchViewHolder> {

    private final Context context;
    List<DoModel> dayModels;

    public DoWatchRecyclerAdapter(List<DoModel> dayModels, Context c)
    {
        context = c;
        this.dayModels = dayModels;
    }

    @Override
    public DoWatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.do_watch_item, parent, false);
        return new DoWatchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DoWatchViewHolder holder, int position) {
        holder.bind(dayModels.get(position));
    }

    @Override
    public int getItemCount() {
        return dayModels.size();
    }

    public class DoWatchViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        DoModel model;

        public DoWatchViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.watchItemImage);
        }

        public void bind(DoModel doModel) {
            model = doModel;
            Picasso.with(context).load(model.doImage)
                    .placeholder(R.drawable.poster_place_holder).into(image);
            image.setOnClickListener(null);
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo open movie!
                }
            });
        }
    }
}