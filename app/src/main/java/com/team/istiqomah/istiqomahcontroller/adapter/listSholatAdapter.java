package com.team.istiqomah.istiqomahcontroller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team.istiqomah.istiqomahcontroller.R;
import com.team.istiqomah.istiqomahcontroller.model.waktuSholatModel;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alde.asprilla on 31/01/2017.
 */

public class listSholatAdapter extends RecyclerView.Adapter<listSholatAdapter.MyViewHolder> {

    private ArrayList<waktuSholatModel> mListSholat;

    public listSholatAdapter(ArrayList<waktuSholatModel> listSholat){
        this.mListSholat = listSholat;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sholat_row, parent, false);

        return new listSholatAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        waktuSholatModel obj = mListSholat.get(position);
        String tanggal = obj.getTanggal().substring(8, 10) + " " + getMonthForInt(obj.getTanggal().substring(5,7));

        holder.tvSholat.setText(obj.getNama());
        holder.tvTime.setText(obj.getWaktu());
        holder.tvDate.setText(tanggal);
    }

    @Override
    public int getItemCount() {
        return mListSholat.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvSholat)
        TextView tvSholat;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvDate)
        TextView tvDate;

        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    String getMonthForInt(String number) {
        int num = Integer.parseInt(number) - 1;
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

}
