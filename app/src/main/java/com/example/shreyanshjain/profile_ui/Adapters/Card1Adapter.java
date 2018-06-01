package com.example.shreyanshjain.profile_ui.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shreyanshjain.profile_ui.Card1Data;
import com.example.shreyanshjain.profile_ui.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class Card1Adapter extends RecyclerView.Adapter<Card1Adapter.ViewHolder> {

    private List<Card1Data> card1Data;
    private Context context;
    private int position;

    public Card1Adapter(List<Card1Data> card1Data, Context context,int position) {
        this.card1Data = card1Data;
        this.context = context;
        this.position = position;
    }

    @Override
    public void onBindViewHolder(@NonNull Card1Adapter.ViewHolder holder, int position) {

        final Card1Data data = card1Data.get(position);

        holder.head.setText(data.getHeading());
        holder.sub_head.setText(data.getSub());
        holder.desc.setText(data.getDesc());

        /*Picasso.with(context)
                .load(data.getImage())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.relativeLayout.setBackground(new BitmapDrawable(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });*/
    }

    @Override
    public int getItemCount() {
        return card1Data.size();
    }

    @NonNull
    @Override
    public Card1Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout=R.layout.card_1;
        switch (position)
        {
            case 1: layout = R.layout.card_2; // Simple Text
                    break;
            case 2: layout = R.layout.card_1; //
                    break;
            case 3: layout = R.layout.card_3;
                    break;
            case 4: layout = R.layout.card_4;
                break;

        }
        View v = LayoutInflater.from(parent.getContext())
                .inflate(layout,parent,false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView head,sub_head,desc;
        public RelativeLayout relativeLayout;

        public ViewHolder(View itemView)
        {
            super(itemView);

            head = (TextView)itemView.findViewById(R.id.tv_recycler_item_1);
            sub_head = (TextView)itemView.findViewById(R.id.tv_recycler_item_2);
            desc = (TextView)itemView.findViewById(R.id.tv_recycler_item_3);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.rela_round);
        }
    }

}

