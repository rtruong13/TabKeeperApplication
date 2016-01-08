package com.i.should.what.whatshouldi.DoPackage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.i.should.what.whatshouldi.DoPackage.Models.DoModel;
import com.i.should.what.whatshouldi.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ryan on 7/29/2015.
 */
public class DoListenRecyclerAdapter extends RecyclerView.Adapter<DoListenRecyclerAdapter.DoListenViewHolder> {

    private final Context context;
    List<DoModel> dayModels;

    public DoListenRecyclerAdapter(List<DoModel> dayModels, Context c) {
        context = c;
        this.dayModels = dayModels;
    }

    @Override
    public DoListenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.do_listen_item, parent, false);
        return new DoListenViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DoListenViewHolder holder, int position) {
        holder.bind(dayModels.get(position));
    }

    @Override
    public int getItemCount() {
        return dayModels.size();
    }

    public class DoListenViewHolder extends RecyclerView.ViewHolder {

        ImageView imageBand, imageAlbum;
        View mainLayout;

        public DoListenViewHolder(View itemView) {
            super(itemView);
            imageAlbum = (ImageView) itemView.findViewById(R.id.listenItemImage);
            imageBand = (ImageView) itemView.findViewById(R.id.listenItemArtistImage);
            mainLayout = itemView.findViewById(R.id.doListenItemMainLayout);
        }

        public void bind(DoModel doModel) {
            if (doModel.aID == -1) {
                imageBand.setVisibility(View.VISIBLE);
                imageAlbum.setVisibility(View.INVISIBLE);
                Picasso.with(context).load(doModel.doImage)
                        .placeholder(R.drawable.ic_group).into(imageBand);
            } else {
                imageBand.setVisibility(View.INVISIBLE);
                imageAlbum.setVisibility(View.VISIBLE);
                Picasso.with(context).load(doModel.doImage)
                        .placeholder(R.drawable.ic_group).into(imageAlbum);
            }
            mainLayout.setOnClickListener(null);
            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //todo open band!
                }
            });
        }
    }
}