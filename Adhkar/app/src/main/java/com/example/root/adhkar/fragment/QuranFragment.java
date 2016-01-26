package com.example.root.adhkar.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.root.adhkar.util.BaseQuranData;
import com.example.root.adhkar.classobject.Quran;
import com.example.root.adhkar.R;
import com.example.root.adhkar.adapter.myAdapterListSura;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by root on 30/06/15.
 */
public class QuranFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, final Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quran, container, false);
        String[] namesurah = getResources().getStringArray(R.array.sura_names);


        ArrayList<Quran> qurans = new ArrayList<>();
        int[] suraPageStarteNum = BaseQuranData.SURA_PAGE_START;
        int[] nbreayah = BaseQuranData.SURA_NUM_AYAHS;
        boolean[] makkiya = BaseQuranData.SURA_IS_MAKKI;
        String ismakki = getString(R.string.makki);
        for (int i = 0; i < 114; i++) {
            if (makkiya[i]) {
                ismakki = getString(R.string.makki);
            } else {
                ismakki = getString(R.string.madani);
            }

            qurans.add(new Quran(i, namesurah[i],
                    suraPageStarteNum[i], nbreayah[i], ismakki));
        }


        ListView list = (ListView) view.findViewById(R.id.listSurah);
        myAdapterListSura adpt = new myAdapterListSura(getActivity(), qurans);

        list.setAdapter(adpt);

        final String[] numSura = getResources().getStringArray(R.array.listnum_surah);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

              //  savePreferences("idsura",numSura[position]);

                Intent intent = new Intent(getActivity(), com.radaee.reader.PDFViewAct.class);
                intent.putExtra("PDFAsset", numSura[position] + ".pdf");
                //      intent.putExtra( "PDFPswd", "" );//password

                startActivity(intent);


            }
        });


        return view;
    }

    public void CreateFileFromInputStream(InputStream inStream, String path) throws IOException {
        // write the inputStream to a FileOutputStream
        OutputStream out = new FileOutputStream(new File(path));

        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = inStream.read(bytes)) != -1) {
            out.write(bytes, 0, read);
        }

        inStream.close();
        out.flush();
        out.close();

    }



    private void savePreferences(String key, boolean value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }




    /*
    private void render() {
        try {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
            int REQ_WIDTH = 1;
            int REQ_HEIGHT = 1;
            REQ_WIDTH = imageView.getWidth();
            REQ_HEIGHT = imageView.getHeigh();

            Bitmap bitmap = Bitmap.createBitmap(REQ_WIDTH, REQ_HEIGHT, Bitmap.Oonfig.ARGB_4444);
            File file = new File("/sdcard/Download/test.pdf");
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));

            if (currentPage < 0) {
                currentPage = 0;
            } else if (currentPage > renderer.getPageCount()) {
                currentPage = renderer.getPageCount() - 1;
            }

            Matrix m = imageView.getImageMatrix();
            Rect rect = new Rect(0, 0, REQ_WIDTH, REQ_HEIGHT);
            renderer.openPage(currentPage).render(bitmap, rect, m, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            imageView.setImageMatrix(m);
            imageView.setImageBitmap(bitmap);
            imageView.invalidate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}












































