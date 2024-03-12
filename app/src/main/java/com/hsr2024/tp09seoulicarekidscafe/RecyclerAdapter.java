package com.hsr2024.tp09seoulicarekidscafe;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.VH> {

    Context context;
    ArrayList<MyItem> items;

    public RecyclerAdapter(Context context, ArrayList<MyItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyclerview,parent,false);

        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        MyItem item = items.get(position);

        holder.atdrc_nm.setText(item.atdrc_nm);
        holder.rntfee_free_at.setText(item.rntfee_free_at);
        holder.fclty_nm.setText(item.fclty_nm);
        holder.cttpc.setText(item.cttpc);
        holder.bass_adres.setText(item.bass_adres);
        //holder.detail_adres.setText(item.detail_adres);
        holder.open_week.setText(item.open_week);
        holder.close_week.setText(item.close_week);
        holder.posbl_agrde.setText(item.posbl_agrde);

        holder.call_iv.setOnClickListener(v->{
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);

            String call = item.getCtppc().replace("-","");
            Uri uri = Uri.parse("tel:"+call);
            try{
                intent.setData(uri);
                context.startActivity(intent);

            }catch (Exception e){
                Toast.makeText(context, "전화기능 에러", Toast.LENGTH_SHORT).show();
            }

            Log.d("call",uri.toString());
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{
        TextView atdrc_nm,rntfee_free_at,fclty_nm,cttpc,bass_adres,detail_adres,open_week,close_week,posbl_agrde;
        ImageView call_iv;

        public VH(@NonNull View itemView) {
            super(itemView);
            atdrc_nm = itemView.findViewById(R.id.atdrc_nm);
            rntfee_free_at = itemView.findViewById(R.id.rntfee_free_at);
            fclty_nm= itemView.findViewById(R.id.fclty_nm);
            cttpc = itemView.findViewById(R.id.cttpc);
            bass_adres = itemView.findViewById(R.id.bass_adres);
            //detail_adres = itemView.findViewById(R.id.detail_adres);
            open_week = itemView.findViewById(R.id.open_week);
            close_week = itemView.findViewById(R.id.close_week);
            posbl_agrde = itemView.findViewById(R.id.posbl_agrde);
            call_iv = itemView.findViewById(R.id.call_iv);
        }
    }
}
