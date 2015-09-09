package com.example.root.adhkar.fragment;

import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.root.adhkar.service.MyServiceNotification;
import com.example.root.adhkar.R;

/**
 * Created by root on 30/06/15.
 */
public class SettingFragment extends Fragment {
    Spinner langue;
    Context contex;
    private PendingIntent pendingIntent;
    int period;
    Intent i;
    CheckBox note;
    TimePicker timePicker;
    int index = 0;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.fragment_setting, container, false);
        // Inflate the layout for this fragment
        contex = getActivity();
        i = new Intent(getActivity(), MyServiceNotification.class);
        note = (CheckBox) view.findViewById(R.id.notification);
        langue = (Spinner) view.findViewById(R.id.langue);
        timePicker = (TimePicker) view.findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

                index++;
            }
        });
        langue.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                savePreferences("langue", position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (note.isChecked()) {
// potentially add data to the intent
                    period = timePicker.getCurrentHour() * 60 * 60 * 1000 + timePicker.getCurrentMinute() * 60 * 1000;
                    //     getActivity().stopService(i);

                    i.putExtra("key", period);

                    getActivity().startService(i);

                } else {
                    i.putExtra("key1", "kill");
                    getActivity().stopService(i);
                }
                savePreferences("notif", note.isChecked());
            }
        });

        loadSavedPreferences();
        return view;
    }

    @Override
    public void onPause() {
        savePreferences("heur", timePicker.getCurrentHour());
        savePreferences("minute", timePicker.getCurrentMinute());
        if (index > 2) {
            if (note.isChecked()) {
                period = timePicker.getCurrentHour() * 60 * 60 * 1000 + timePicker.getCurrentMinute() * 60 * 1000;
                i.putExtra("key", period);
                getActivity().startService(i);
            }
        }
        super.onPause();
    }

    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        boolean checkBoxValue = sharedPreferences.getBoolean("notif", false);
        int pos = sharedPreferences.getInt("langue", 2);
        if (checkBoxValue) {
            note.setChecked(true);
        } else {
            note.setChecked(false);
        }

        langue.setSelection(pos);
        int heur = sharedPreferences.getInt("heur", 1);
        int min = sharedPreferences.getInt("minute", 0);
        timePicker.setCurrentHour(heur);
        timePicker.setCurrentMinute(min);

    }

    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private void savePreferences(String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
