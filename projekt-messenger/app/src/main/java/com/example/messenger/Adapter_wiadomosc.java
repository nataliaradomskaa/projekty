package com.example.messenger;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class Adapter_wiadomosc extends RecyclerView.Adapter<Adapter_wiadomosc.ViewHolder> {

    ArrayList<String> wiadomosci;
    ArrayList<String> id_od_kogo;
    String id_uzytkownika;
    Context context;
    int maxWidth;

    public Adapter_wiadomosc(ArrayList<String> wiadomosci,ArrayList<String> id_od_kogo,String id_uzytkownika, int maxWidth){
        this.wiadomosci=wiadomosci;
        this.id_od_kogo=id_od_kogo;
        this.id_uzytkownika=id_uzytkownika;
        this.maxWidth=maxWidth;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private final CardView cardView;
        public ViewHolder(CardView v){
            super(v);
            cardView=v;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv= (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_message,parent,false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        context=holder.cardView.getContext();
        TextView tv= holder.cardView.findViewById(R.id.textViewMessage);
        LinearLayout ll= holder.cardView.findViewById(R.id.linearLayoutMessage);
        tv.setText(wiadomosci.get(position));

        if(id_uzytkownika==id_od_kogo.get(position)){
            tv.setBackground(context.getResources().getDrawable(R.drawable.wyslana_wiadomosc));
            tv.setMaxWidth(maxWidth);
            ll.setGravity(Gravity.RIGHT);
        }
        else {
            tv.setBackground(context.getResources().getDrawable(R.drawable.odebrana_wiadomosc));
            tv.setMaxWidth(maxWidth);
            ll.setGravity(Gravity.LEFT);
        }

    }

    @Override
    public int getItemCount() {
        return wiadomosci.size();
    }
}