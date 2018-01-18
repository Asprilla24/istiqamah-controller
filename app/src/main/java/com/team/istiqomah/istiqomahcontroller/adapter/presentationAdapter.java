package com.team.istiqomah.istiqomahcontroller.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.team.istiqomah.istiqomahcontroller.R;
import com.team.istiqomah.istiqomahcontroller.core.AppData;
import com.team.istiqomah.istiqomahcontroller.model.persentaseSholatModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by alde.asprilla on 04/02/2017.
 */

public class presentationAdapter extends RecyclerView.Adapter<presentationAdapter.MyViewHolder> {
    private ArrayList<ArrayList<persentaseSholatModel>> mListPresentation;

    public presentationAdapter(ArrayList<ArrayList<persentaseSholatModel>> listPersentase){
        super();
        this.mListPresentation = listPersentase;
    }

    @Override
    public presentationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.presentation_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ArrayList<persentaseSholatModel> obj = mListPresentation.get(position);

        if(obj.size() > 0){
            setChart(holder.chart, obj);
            holder.tvSholat.setText(getSholatName(obj.get(0)));
        }
    }

    @Override
    public int getItemCount() {
        return mListPresentation.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvSholat)
        TextView tvSholat;
        @BindView(R.id.chart)
        LineChart chart;

        public MyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void setChart(LineChart lineChart, ArrayList<persentaseSholatModel> data){
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        //set data
        ArrayList<Entry> entries = new ArrayList<>();
        for(int i = 0; i < data.size(); i++){
            persentaseSholatModel obj = data.get(i);
            entries.add(new Entry(pecahWaktu("Day",obj.getTanggal()), obj.getPersentase().floatValue()));
        }
        LineDataSet dataset = new LineDataSet(entries, "");

        LineData lineData = new LineData(dataset);
        dataset.setDrawFilled(true);
        dataset.setFillAlpha(100);

        //setting possision of Axis
        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxis = lineChart.getAxisRight();
        YAxis yAxisLeft = lineChart.getAxisLeft();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        yAxis.setEnabled(false);
        yAxisLeft.setDrawGridLines(false);

        lineChart.setAutoScaleMinMaxEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setScaleEnabled(false);
        lineChart.setHighlightPerTapEnabled(false);
        lineChart.setHighlightPerDragEnabled(false);
        lineChart.setData(lineData);
        lineChart.animateY(1000);
    }

    private String getSholatName(persentaseSholatModel obj){
        switch (obj.getId_Sholat()){
            case AppData.KEY_ISYA : return "Isya";
            case AppData.KEY_SUBUH: return "Subuh";
            case AppData.KEY_DHUHUR: return "Dhuhur";
            case AppData.KEY_ASHAR: return "Ashar";
            case AppData.KEY_MAGHRIB: return "Maghrib";
            default:return "";
        }
    }

    private int pecahWaktu(String satuan, String waktu){
        switch (satuan){
            case "Day" : return Integer.parseInt(waktu.substring(8, 10));
            case "Hour" : return Integer.parseInt(waktu.substring(0,2));
            case "Minute" : return Integer.parseInt(waktu.substring(3, 5));
            case "Second" : {
                if(waktu.length() == 8)
                    return Integer.parseInt(waktu.substring(6, 8));
                else
                    return 0;
            }
            default : return 0;
        }
    }

    private void showLog(String message){
        Log.d("presentationAdapter", message);
    }
}
