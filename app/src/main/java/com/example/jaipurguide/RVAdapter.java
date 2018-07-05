package com.example.jaipurguide;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>   //it is customAdapter that follows a viewholder hich reduces the cost of findviewbyid()
{

   private ArrayList<com.example.jaipurguide.Place> places;

    Context mContext;

   public RVAdapter(ArrayList<com.example.jaipurguide.Place> places, Context c){
        this.mContext=c;
        this.places = places;
    }

    public  class PersonViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView placeEtitle;
        TextView placeHtitle;
        ImageView placeImage;

        PersonViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            placeEtitle = (TextView)itemView.findViewById(R.id.eTitle);
            placeHtitle = (TextView)itemView.findViewById(R.id.hTitle);
            placeImage = (ImageView)itemView.findViewById(R.id.image);
        }
    }

    // As its name suggests, this method is called when the custom ViewHolder needs to be initialized.
    // We specify the layout that each item of the RecyclerView should use.
    // This is done by inflating the layout using LayoutInflater,
    // passing the output to the constructor of the custom ViewHolder.
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row, parent, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }



    // specify the contents of each item of the RecyclerView.
    // This method is very similar to the getView method of a ListView's adapter
    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.placeEtitle.setText(places.get(i).eTitle);
        personViewHolder.placeHtitle.setText(places.get(i).hTitle);
        personViewHolder.placeImage.setImageResource(places.get(i).image);
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    //Finally, you need to override the onAttachedToRecyclerView method.
    // For now, we can simply use the superclass's implementation of this method
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public  void filterList(ArrayList<com.example.jaipurguide.Place> filterdNames) {
        places = filterdNames;
        notifyDataSetChanged();
    }

}
