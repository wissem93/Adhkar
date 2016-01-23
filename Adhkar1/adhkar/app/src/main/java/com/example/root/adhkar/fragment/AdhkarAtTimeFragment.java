package com.example.root.adhkar.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.root.adhkar.R;
import com.example.root.adhkar.activity.SharingActivity;
import com.facebook.CallbackManager;
import com.facebook.share.widget.ShareDialog;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by root on 01/07/15.
 */
public class AdhkarAtTimeFragment extends Fragment {

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        View view = inflater.inflate(
                R.layout.fragment_athkar_at_time, container, false);


        ListView listView = (ListView) view.findViewById(R.id.adhkar);
        final ArrayList<String> listAdhkar = new ArrayList<>();
        int dhekr;
        Calendar c = Calendar.getInstance();


        int heur = new Time(System.currentTimeMillis()).getHours();


        if ((heur <= 12) && (heur >= 3))
            dhekr = R.array.listadhkarasabeh;
        else if ((heur > 12) && (heur < 20))
            dhekr = R.array.listadhkarmasaa;
        else
            dhekr = R.array.listadhkarnawm;

        String[] tabadhkar = getResources().getStringArray(dhekr);
        for (int i = 0; i < tabadhkar.length; i++) {
            listAdhkar.add(tabadhkar[i]);
        }
        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listAdhkar);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dilogshare);
                dialog.setTitle("share");

                Button dialogButton = (Button) dialog.findViewById(R.id.concel);
                // if button is clicked, close the custom dialog
                ImageButton sharefb = (ImageButton) dialog.findViewById(R.id.sharefb1);


                sharefb.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SharingActivity.class);
                        intent.putExtra("testd", listAdhkar.get(position));

                        startActivity(intent);
                    }
                });
                ImageButton share = (ImageButton) dialog.findViewById(R.id.share1);
                share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent shareingIntent = new Intent(Intent.ACTION_SEND);
                        String shareBody = listAdhkar.get(position);
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, getActivity().getResources().getString(R.string.share)));


                        //           initShareIntent("",tabDoaa[i]);
                    }
                });
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });


                dialog.show();
            }
        });


        return view;
    }
}
