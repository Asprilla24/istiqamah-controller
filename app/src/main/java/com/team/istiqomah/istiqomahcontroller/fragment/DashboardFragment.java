package com.team.istiqomah.istiqomahcontroller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.team.istiqomah.istiqomahcontroller.R;
import com.team.istiqomah.istiqomahcontroller.adapter.presentationAdapter;
import com.team.istiqomah.istiqomahcontroller.core.AppData;
import com.team.istiqomah.istiqomahcontroller.helper.persentaseSholatRealmHelper;
import com.team.istiqomah.istiqomahcontroller.model.persentaseSholatModel;
import com.team.istiqomah.istiqomahcontroller.model.waktuSholatModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.realm.Realm;

public class DashboardFragment extends Fragment {

    @BindView(R.id.listView)
    RecyclerView listView;

    Unbinder unbinder;

    private Realm realm;
    private persentaseSholatRealmHelper persentaseSholatRealmHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        realm = Realm.getDefaultInstance();
        persentaseSholatRealmHelper = new persentaseSholatRealmHelper(realm);

        initView();

        return rootView;
    }

    void initView(){
        ArrayList<ArrayList<persentaseSholatModel>> getPersentaseSholat = new ArrayList<>();
        ArrayList<waktuSholatModel> getSholat = AppData.getKeySholat();


        for (int i = 0; i < 5; i++) {
            int idSholat = getSholat.get(i).getId_Sholat();
            ArrayList<persentaseSholatModel> data = persentaseSholatRealmHelper.getListPersentaseSholat(idSholat);
            if(data.size() > 0)
                getPersentaseSholat.add(data);
        }

        showLog(getPersentaseSholat.size()+"");
        presentationAdapter myArrayAdapter = new presentationAdapter(getPersentaseSholat);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(myArrayAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showLog(String message){
        Log.d("DashboardFragment", message);
    }
}
