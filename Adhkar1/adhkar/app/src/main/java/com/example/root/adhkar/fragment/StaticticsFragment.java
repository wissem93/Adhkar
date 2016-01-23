package com.example.root.adhkar.fragment;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.root.adhkar.util.BaseQuranData;
import com.example.root.adhkar.R;

public class StaticticsFragment extends Fragment {
    TextView numAyahRead;
    TextView numSuraRead;
    TextView numHezbRead;
    TextView numKhatmatxt;
    TextView poucentage;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_statistics, container, false);
        numAyahRead = (TextView) view.findViewById(R.id.numbreAyehRead);
        numSuraRead = (TextView) view.findViewById(R.id.numbreSurahRead);
        numHezbRead = (TextView) view.findViewById(R.id.numbreHezbRead);
        poucentage = (TextView) view.findViewById(R.id.poucentage);
        numKhatmatxt = (TextView) view.findViewById(R.id.NumbreKhatmahsQuran);
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        int page = sharedPreferences.getInt("pagecur", 1);
        int numbreAyeh = 0;
        for (int i = 0; i < BaseQuranData.PAGE_SURA_START[page] - 1; i++) {
            numbreAyeh = numbreAyeh + BaseQuranData.SURA_NUM_AYAHS[i];
        }
        numbreAyeh = numbreAyeh + BaseQuranData.PAGE_AYAH_START[page];
        float  pour = (page * 100) / 568;
        int nbreKhatma = loadSavedPreferences();
        if (page == 568) savePreferences("Khatma", (nbreKhatma + 1));
        numKhatmatxt.setText(numKhatmatxt.getText() + "  " + nbreKhatma);
        numAyahRead.setText(numAyahRead.getText() + "    " + numbreAyeh);
        numSuraRead.setText(numSuraRead.getText() + "    " + BaseQuranData.PAGE_SURA_START[page]);
        poucentage.setText(poucentage.getText() + "   " + pour + "%");
     //   view.setBackgroundResource(R.drawable.shadow);
        return view;
    }


    private void savePreferences(String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private int loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());

        int pos = sharedPreferences.getInt("Khatma", 0);


        return pos;
    }
}