package com.example.root.adhkar.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.radaee.pdf.Document;
import com.radaee.pdf.Global;
import com.radaee.pdf.Page;
import com.radaee.reader.PDFLayoutView;
import com.radaee.reader.PDFViewController;
import com.radaee.util.PDFAssetStream;
import com.radaee.util.PDFHttpStream;
import com.radaee.view.VPage;
import com.radaee.viewlib.R;

public class LastReadFragment extends Fragment implements PDFLayoutView.PDFLayoutListener {
    static protected Document ms_tran_doc;
    static private int m_tmp_index = 0;
    private PDFAssetStream m_asset_stream = null;
    private PDFHttpStream m_http_stream = null;
    private Document m_doc = null;
    private RelativeLayout m_layout = null;
    private PDFLayoutView m_view = null;
    private PDFViewController m_controller = null;
    private boolean m_modified = false;
    private boolean need_save_doc = false;

    private void onFail(String msg)//treat open failed.
    {
        m_doc.Close();
        m_doc = null;
        // Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    private final void ProcessOpenResult(int ret) {
        switch (ret) {
            case -1://need input password
                onFail("Open Failed: Invalid Password");
                break;
            case -2://unknown encryption
                onFail("Open Failed: Unknown Encryption");
                break;
            case -3://damaged or invalid format
                onFail("Open Failed: Damaged or Invalid PDF file");
                break;
            case -10://access denied or invalid file path
                onFail("Open Failed: Access denied or Invalid path");
                break;
            case 0://succeeded, and continue
                m_view.PDFOpen(m_doc, this);
                m_controller = new PDFViewController(m_layout, m_view);
                break;
            default://unknown error
                onFail("Open Failed: Unknown Error");
                break;
        }
    }

    class MyPDFFontDel implements Document.PDFFontDelegate {
        @Override
        public String GetExtFont(String collection, String fname, int flag, int[] ret_flags) {
            Log.i("ExtFont", fname);
            return null;
        }
    }

    private MyPDFFontDel m_font_del = new MyPDFFontDel();

    @SuppressLint("InlinedApi")
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //plz set this line to Activity in AndroidManifes.xml:
        //    android:configChanges="orientation|keyboardHidden|screenSize"
        //otherwise, APP shall destroy this Activity and re-create a new Activity when rotate.
        Global.Init(getActivity());
        m_layout = (RelativeLayout) LayoutInflater.from(getActivity()).inflate(com.example.root.adhkar.R.layout.fragment_lastread, null);
        m_view = (PDFLayoutView) m_layout.findViewById(R.id.pdf_view);
        Intent intent = getActivity().getIntent();
        String bmp_format = intent.getStringExtra("BMPFormat");
        if (bmp_format != null) {
            if (bmp_format.compareTo("RGB_565") == 0)
                m_view.PDFSetBmpFormat(Bitmap.Config.RGB_565);
            else if (bmp_format.compareTo("ARGB_4444") == 0)
                m_view.PDFSetBmpFormat(Bitmap.Config.ARGB_4444);
        }
        if (ms_tran_doc != null) {
            m_doc = ms_tran_doc;
            ms_tran_doc = null;
            m_doc.SetCache(String.format("%s/temp%08x.dat", Global.tmp_path, m_tmp_index));//set temporary cache for editing.
            m_tmp_index++;
            m_view.PDFOpen(m_doc, this);
            m_controller = new PDFViewController(m_layout, m_view);
            need_save_doc = true;
        } else {
            String pdf_asset = getArguments().getString("PDFAsset");
            String pdf_path = intent.getStringExtra("PDFPath");
            String pdf_pswd = intent.getStringExtra("PDFPswd");
            String pdf_http = intent.getStringExtra("PDFHttp");
            if (pdf_http != null && pdf_http != "") {
                m_http_stream = new PDFHttpStream();
                m_http_stream.open(pdf_http);
                m_doc = new Document();
                int ret = m_doc.OpenStream(m_http_stream, pdf_pswd);
                ProcessOpenResult(ret);
            } else if (pdf_asset != null && pdf_asset != "") {
                m_asset_stream = new PDFAssetStream();
                m_asset_stream.open(getActivity().getAssets(), pdf_asset);
                m_doc = new Document();
                int ret = m_doc.OpenStream(m_asset_stream, pdf_pswd);
                ProcessOpenResult(ret);
            } else if (pdf_path != null && pdf_path != "") {
                m_doc = new Document();
                int ret = m_doc.Open(pdf_path, pdf_pswd);
                m_doc.SetCache(String.format("%s/temp%08x.dat", Global.tmp_path, m_tmp_index));//set temporary cache for editing.
                m_tmp_index++;
                m_doc.SetFontDel(m_font_del);
                ProcessOpenResult(ret);
            }
        }


            loadSavedPreferences();

      /*  SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int style = sharedPreferences.getInt("view", 0);
        m_view.PDFSetView(style);
        */return m_layout;
    }

    private void loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        int pos = sharedPreferences.getInt("pagecur", 0);
        m_view.PDFGotoPage(pos);
        int style = sharedPreferences.getInt("view", 0);
        m_view.PDFSetView(style);
        //m_menu_view.MenuDismiss();

    }

    private void savePreferences(String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }


    @Override
        public void onPause() {
        savePreferences("pagecur", m_view.PDFGetCurrPage());
            super.onPause();
        }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (m_doc == null)
            m_doc = m_view.PDFGetDoc();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        m_view.BundleSavePos(savedInstanceState);
        if (need_save_doc && m_doc != null) {
            Document.BundleSave(savedInstanceState, m_doc);//save Document object
            m_doc = null;
        }
    }


    public void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);
        if (m_doc == null) {
            m_doc = Document.BundleRestore(savedInstanceState);//restore Document object
            m_view.PDFOpen(m_doc, this);
            m_controller = new PDFViewController(m_layout, m_view);
            need_save_doc = true;
        }
        m_view.BundleRestorePos(savedInstanceState);
    }

    public void onBackPressed() {
        if (m_controller == null || m_controller.OnBackPressed()) {
            if (m_modified) {
                TextView txtView = new TextView(getActivity());
                txtView.setText("Document modified\r\nDo you want save it?");
                new AlertDialog.Builder(getActivity()).setTitle("Exiting").setView(
                        txtView).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_doc.Save();
                        onBackPressed();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                    }
                }).show();
            } else onBackPressed();
        }
    }

    /*  @SuppressLint("InlinedApi")
      @Override
      protected void onDestroy() {
          if (m_doc != null) {
              m_view.PDFClose();
              m_doc.Close();
              m_doc = null;
          }
          if (m_asset_stream != null) {
              m_asset_stream.close();
              m_asset_stream = null;
          }
          if (m_http_stream != null) {
              m_http_stream.close();
              m_http_stream = null;
          }
          super.onDestroy();
      }
  */
    @Override
    public void OnPDFPageModified(int pageno) {
        m_modified = true;
    }

    @Override
    public void OnPDFPageChanged(int pageno) {
        if (m_controller != null)
            m_controller.OnPageChanged(pageno);
    }

    @Override
    public void OnPDFAnnotTapped(VPage vpage, Page.Annotation annot) {
        if (m_controller != null)
            m_controller.OnAnnotTapped(annot);
    }

    @Override
    public void OnPDFBlankTapped() {
        if (m_controller != null)
            m_controller.OnBlankTapped();
    }

    @Override
    public void OnPDFSelectEnd(String text) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity()).inflate(com.radaee.viewlib.R.layout.dlg_text, null);
        final RadioGroup rad_group = (RadioGroup) layout.findViewById(com.radaee.viewlib.R.id.rad_group);
        final String sel_text = text;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi")
            public void onClick(DialogInterface dialog, int which) {
                if (rad_group.getCheckedRadioButtonId() == com.radaee.viewlib.R.id.rad_copy) {
                    Toast.makeText(getActivity(), "todo copy text:" + sel_text, Toast.LENGTH_SHORT).show();
                } else if (m_doc.CanSave()) {
                    boolean ret = false;
                    if (rad_group.getCheckedRadioButtonId() == com.radaee.viewlib.R.id.rad_copy) {
                        Toast.makeText(getActivity(), "todo copy text:" + sel_text, Toast.LENGTH_SHORT).show();
                        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        android.content.ClipData clip = android.content.ClipData.newPlainText("Radaee", sel_text);
                        clipboard.setPrimaryClip(clip);
                    } else if (rad_group.getCheckedRadioButtonId() == com.radaee.viewlib.R.id.rad_highlight)
                        ret = m_view.PDFSetSelMarkup(0);
                    else if (rad_group.getCheckedRadioButtonId() == com.radaee.viewlib.R.id.rad_underline)
                        ret = m_view.PDFSetSelMarkup(1);
                    else if (rad_group.getCheckedRadioButtonId() == com.radaee.viewlib.R.id.rad_strikeout)
                        ret = m_view.PDFSetSelMarkup(2);
                    else if (rad_group.getCheckedRadioButtonId() == com.radaee.viewlib.R.id.rad_squiggly)
                        ret = m_view.PDFSetSelMarkup(4);
                    if (!ret)
                        Toast.makeText(getActivity(), "add annotation failed!", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getActivity(), "can't write or encrypted!", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                if (m_controller != null)
                    m_controller.OnSelectEnd();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setTitle("Process selected text");
        builder.setCancelable(false);
        builder.setView(layout);
        AlertDialog dlg = builder.create();
        dlg.show();
    }

    @Override
    public void OnPDFOpenURI(String uri) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(uri);
            intent.setData(content_url);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(getActivity(), "todo: open url:" + uri, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void OnPDFOpenJS(String js) {
        Toast.makeText(getActivity(), "todo: execute java script", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnPDFOpenMovie(String path) {
        Toast.makeText(getActivity(), "todo: play movie", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnPDFOpenSound(int[] paras, String path) {
        Toast.makeText(getActivity(), "todo: play sound", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnPDFOpenAttachment(String path) {
        Toast.makeText(getActivity(), "todo: treat attachment", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnPDFOpen3D(String path) {
        Toast.makeText(getActivity(), "todo: play 3D module", Toast.LENGTH_SHORT).show();
    }
}
