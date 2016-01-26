package com.radaee.reader;

import com.radaee.pdf.Page.Annotation;
import com.radaee.viewlib.R;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class PDFViewController implements OnClickListener, SeekBar.OnSeekBarChangeListener {
    public interface PDFViewControllerListener {
        public void OnCtrlSelect(boolean set);
    }

    private int m_bar_status = 0;
    static public final int BAR_NONE = 0;
    static public final int BAR_CMD = 1;
    static public final int BAR_ANNOT = 2;
    static public final int BAR_FIND = 3;
    static public final int BAR_ACT = 4;
    private RelativeLayout m_parent;
    private PDFLayoutView m_view;
    private PDFTopBar m_bar_act;
    private PDFTopBar m_bar_cmd;
    private PDFTopBar m_bar_find;
    private PDFTopBar m_bar_annot;
    private PDFBotBar m_bar_seek;
    private PDFMenu m_menu_view;
    private ImageView btn_view;
    private ImageView btn_find;
    private ImageView btn_annot;
    private ImageView btn_select;
    private ImageView btn_find_back;
    private ImageView btn_find_prev;
    private ImageView btn_find_next;
    private ImageView btn_act_back;
    private ImageView btn_act_edit;
    private ImageView btn_act_perform;
    private ImageView btn_act_remove;
    private ImageView btn_annot_back;
    private ImageView btn_annot_ink;
    private ImageView btn_annot_line;
    private ImageView btn_annot_rect;
    private ImageView btn_annot_oval;
    private ImageView btn_annot_stamp;
    private ImageView btn_annot_note;
    private EditText edit_find;
    private SeekBar seek_page;
    private TextView lab_page;
    private View view_vert;
    private View view_single;
    private View view_dual;
    private boolean m_set = false;

    public PDFViewController(RelativeLayout parent, PDFLayoutView view) {
        m_parent = parent;
        m_view = view;
        m_bar_act = new PDFTopBar(m_parent, R.layout.bar_act);
        m_bar_cmd = new PDFTopBar(m_parent, R.layout.bar_cmd);
        m_bar_find = new PDFTopBar(m_parent, R.layout.bar_find);
        m_bar_annot = new PDFTopBar(m_parent, R.layout.bar_annot);
        m_bar_seek = new PDFBotBar(m_parent, R.layout.bar_seek);
        m_menu_view = new PDFMenu(m_parent, R.layout.pop_view);
        RelativeLayout layout = (RelativeLayout) m_bar_cmd.BarGetView();
        btn_view = (ImageView) layout.findViewById(R.id.btn_view);
        btn_find = (ImageView) layout.findViewById(R.id.btn_find);
        btn_annot = (ImageView) layout.findViewById(R.id.btn_annot);
        btn_select = (ImageView) layout.findViewById(R.id.btn_select);
        layout = (RelativeLayout) m_bar_find.BarGetView();
        btn_find_back = (ImageView) layout.findViewById(R.id.btn_back);
        btn_find_prev = (ImageView) layout.findViewById(R.id.btn_left);
        btn_find_next = (ImageView) layout.findViewById(R.id.btn_right);
        edit_find = (EditText) layout.findViewById(R.id.txt_find);
        layout = (RelativeLayout) m_bar_act.BarGetView();
        btn_act_back = (ImageView) layout.findViewById(R.id.btn_back);
        btn_act_edit = (ImageView) layout.findViewById(R.id.btn_edit);
        btn_act_perform = (ImageView) layout.findViewById(R.id.btn_perform);
        btn_act_remove = (ImageView) layout.findViewById(R.id.btn_remove);
        layout = (RelativeLayout) m_bar_annot.BarGetView();
        btn_annot_back = (ImageView) layout.findViewById(R.id.btn_back);
        btn_annot_ink = (ImageView) layout.findViewById(R.id.btn_annot_ink);
        btn_annot_line = (ImageView) layout.findViewById(R.id.btn_annot_line);
        btn_annot_rect = (ImageView) layout.findViewById(R.id.btn_annot_rect);
        btn_annot_oval = (ImageView) layout.findViewById(R.id.btn_annot_oval);
        btn_annot_stamp = (ImageView) layout.findViewById(R.id.btn_annot_stamp);
        btn_annot_note = (ImageView) layout.findViewById(R.id.btn_annot_note);
        layout = (RelativeLayout) m_bar_seek.BarGetView();
        seek_page = (SeekBar) layout.findViewById(R.id.seek_page);
        lab_page = (TextView) layout.findViewById(R.id.lab_page);
        lab_page.setTextColor(-1);
        LinearLayout layout1 = (LinearLayout) m_menu_view.MenuGetView();
        view_vert = layout1.findViewById(R.id.view_vert);
        view_single = layout1.findViewById(R.id.view_single);
        view_dual = layout1.findViewById(R.id.view_dual);

        btn_view.setOnClickListener(this);
        btn_find.setOnClickListener(this);
        btn_annot.setOnClickListener(this);
        btn_select.setOnClickListener(this);
        btn_find_back.setOnClickListener(this);
        btn_find_prev.setOnClickListener(this);
        btn_find_next.setOnClickListener(this);
        btn_act_back.setOnClickListener(this);
        btn_act_edit.setOnClickListener(this);
        btn_act_perform.setOnClickListener(this);
        btn_act_remove.setOnClickListener(this);
        btn_annot_back.setOnClickListener(this);
        btn_annot_ink.setOnClickListener(this);
        btn_annot_line.setOnClickListener(this);
        btn_annot_rect.setOnClickListener(this);
        btn_annot_oval.setOnClickListener(this);
        btn_annot_stamp.setOnClickListener(this);
        btn_annot_note.setOnClickListener(this);
        view_vert.setOnClickListener(this);
        view_single.setOnClickListener(this);
        view_dual.setOnClickListener(this);
        seek_page.setOnSeekBarChangeListener(this);
        seek_page.setMax(m_view.PDFGetDoc().GetPageCount() - 1);
        SetBtnEnabled(btn_annot, m_view.PDFCanSave());
    }

    private final void SetBtnEnabled(ImageView btn, boolean enable) {
        if (enable) {
            btn.setEnabled(true);
            btn.setBackgroundColor(0);
        } else {
            btn.setEnabled(false);
            btn.setBackgroundColor(0x80888888);
        }
    }

    private final void SetBtnChecked(ImageView btn, boolean check) {
        if (check) {
            btn.setBackgroundColor(0x80FF8000);
        } else {
            btn.setBackgroundColor(0);
        }
        m_set = check;
    }

    public void OnAnnotTapped(Annotation annot) {
        switch (m_bar_status) {
            case BAR_NONE:
                if (annot != null) {
                    m_bar_act.BarShow();
                    m_bar_status = BAR_ACT;
                }
                break;
            case BAR_CMD:
                if (annot != null) {
                    m_bar_cmd.BarSwitch(m_bar_act);
                    m_bar_seek.BarHide();
                    m_bar_status = BAR_ACT;
                }
                break;
            case BAR_ACT:
                if (annot == null) {
                    m_bar_act.BarHide();
                    m_bar_status = BAR_NONE;
                }
                break;
            case BAR_FIND:
                if (annot != null) {
                    m_bar_find.BarSwitch(m_bar_act);
                    m_bar_status = BAR_ACT;
                }
                break;
            case BAR_ANNOT:
                if (annot != null) {
                    m_bar_annot.BarSwitch(m_bar_act);
                    m_bar_status = BAR_ACT;
                }
                break;
        }
    }

    public void OnBlankTapped() {
        switch (m_bar_status) {
            case BAR_NONE:
                m_bar_cmd.BarShow();
                m_bar_seek.BarShow();
                m_bar_status = BAR_CMD;
                break;
            case BAR_ACT:
                m_bar_act.BarHide();
                m_bar_status = BAR_NONE;
                break;
            case BAR_CMD:
                m_menu_view.MenuDismiss();
                m_bar_cmd.BarHide();
                m_bar_seek.BarHide();
                m_bar_status = BAR_NONE;
                break;
            case BAR_FIND:
                m_bar_find.BarHide();
                m_bar_status = BAR_NONE;
                break;
            case BAR_ANNOT:
                m_bar_annot.BarHide();
                m_bar_status = BAR_NONE;
                break;
        }
    }

    public void OnSelectEnd() {
        m_view.PDFSetSelect();
        SetBtnChecked(btn_select, false);
        SetBtnEnabled(btn_view, true);
        SetBtnEnabled(btn_find, true);
        SetBtnEnabled(btn_annot, m_view.PDFCanSave());
    }

    @Override
    public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
        lab_page.setText(String.format("%d", arg0.getProgress() + 1));
    }

    @Override
    public void onStartTrackingTouch(SeekBar arg0) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar arg0) {
        m_view.PDFGotoPage(arg0.getProgress());
    }

    public void OnPageChanged(int pageno) {
        lab_page.setText(String.format("%d", pageno + 1));
        seek_page.setProgress(pageno);
    }

    public boolean OnBackPressed() {
        switch (m_bar_status) {
            case BAR_NONE:
                return true;
            case BAR_ACT:
                m_view.PDFCancelAnnot();
                m_bar_act.BarHide();
                m_bar_status = BAR_NONE;
                return false;
            case BAR_CMD:
                if (m_set) OnSelectEnd();
                m_menu_view.MenuDismiss();
                m_bar_cmd.BarHide();
                m_bar_seek.BarHide();
                m_bar_status = BAR_NONE;
                return false;
            case BAR_FIND:
                m_bar_find.BarHide();
                m_bar_status = BAR_NONE;
                return false;
            case BAR_ANNOT:
                if (m_set) {
                    m_view.PDFCancelAnnot();
                    m_set = false;
                    SetBtnEnabled(btn_annot_ink, m_view.PDFCanSave());
                    SetBtnEnabled(btn_annot_line, m_view.PDFCanSave());
                    SetBtnEnabled(btn_annot_rect, m_view.PDFCanSave());
                    SetBtnEnabled(btn_annot_oval, m_view.PDFCanSave());
                    SetBtnEnabled(btn_annot_stamp, m_view.PDFCanSave());
                    SetBtnEnabled(btn_annot_note, m_view.PDFCanSave());
                }
                m_bar_annot.BarHide();
                m_bar_status = BAR_NONE;
                return false;
            default:
                return false;
        }
    }

    private String m_find_str = null;

    @Override
    public void onClick(View arg0) {
        if (arg0 == btn_view)//popup view list
        {
            m_menu_view.MenuShow(0, m_bar_cmd.BarGetHeight());
        } else if (arg0 == btn_select) {
            if (m_set) OnSelectEnd();
            else {
                m_view.PDFSetSelect();
                SetBtnChecked(btn_select, true);
                SetBtnEnabled(btn_view, false);
                SetBtnEnabled(btn_find, false);
                SetBtnEnabled(btn_annot, false);
            }
        } else if (arg0 == btn_find) {
            m_bar_cmd.BarSwitch(m_bar_find);
            m_bar_seek.BarHide();
            m_bar_status = BAR_FIND;
        } else if (arg0 == btn_find_prev) {
            String val = edit_find.getText().toString();
            if (val != null && val != "") {
                if (m_find_str == val) {
                    m_view.PDFFind(-1);
                } else {
                    m_find_str = val;
                    m_view.PDFFindStart(val, false, false);
                    m_view.PDFFind(-1);
                }
            }
        } else if (arg0 == btn_find_next) {
            String val = edit_find.getText().toString();
            if (val != null && val != "") {
                if (m_find_str == val) {
                    m_view.PDFFind(1);
                } else {
                    m_find_str = val;
                    m_view.PDFFindStart(val, false, false);
                    m_view.PDFFind(1);
                }
            }
        } else if (arg0 == btn_annot) {
            m_bar_cmd.BarSwitch(m_bar_annot);
            m_bar_seek.BarHide();
            m_bar_status = BAR_ANNOT;
        } else if (arg0 == btn_annot_ink) {
            if (m_set) {
                m_view.PDFSetInk(1);
                SetBtnChecked(btn_annot_ink, false);
                SetBtnEnabled(btn_annot_line, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_rect, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_oval, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_stamp, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_note, m_view.PDFCanSave());
            } else {
                m_view.PDFSetInk(0);
                SetBtnChecked(btn_annot_ink, true);
                SetBtnEnabled(btn_annot_line, false);
                SetBtnEnabled(btn_annot_rect, false);
                SetBtnEnabled(btn_annot_oval, false);
                SetBtnEnabled(btn_annot_stamp, false);
                SetBtnEnabled(btn_annot_note, false);
            }
        } else if (arg0 == btn_annot_line) {
            if (m_set) {
                m_view.PDFSetLine(1);
                SetBtnEnabled(btn_annot_ink, m_view.PDFCanSave());
                SetBtnChecked(btn_annot_line, false);
                SetBtnEnabled(btn_annot_rect, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_oval, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_stamp, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_note, m_view.PDFCanSave());
            } else {
                m_view.PDFSetLine(0);
                SetBtnEnabled(btn_annot_ink, false);
                SetBtnChecked(btn_annot_line, true);
                SetBtnEnabled(btn_annot_rect, false);
                SetBtnEnabled(btn_annot_oval, false);
                SetBtnEnabled(btn_annot_stamp, false);
                SetBtnEnabled(btn_annot_note, false);
            }
        } else if (arg0 == btn_annot_rect) {
            if (m_set) {
                m_view.PDFSetRect(1);
                SetBtnEnabled(btn_annot_ink, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_line, m_view.PDFCanSave());
                SetBtnChecked(btn_annot_rect, false);
                SetBtnEnabled(btn_annot_oval, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_stamp, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_note, m_view.PDFCanSave());
            } else {
                m_view.PDFSetRect(0);
                SetBtnEnabled(btn_annot_ink, false);
                SetBtnEnabled(btn_annot_line, false);
                SetBtnChecked(btn_annot_rect, true);
                SetBtnEnabled(btn_annot_oval, false);
                SetBtnEnabled(btn_annot_stamp, false);
                SetBtnEnabled(btn_annot_note, false);
            }
        } else if (arg0 == btn_annot_oval) {
            if (m_set) {
                m_view.PDFSetEllipse(1);
                SetBtnEnabled(btn_annot_ink, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_line, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_rect, m_view.PDFCanSave());
                SetBtnChecked(btn_annot_oval, false);
                SetBtnEnabled(btn_annot_stamp, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_note, m_view.PDFCanSave());
            } else {
                m_view.PDFSetEllipse(0);
                SetBtnEnabled(btn_annot_ink, false);
                SetBtnEnabled(btn_annot_line, false);
                SetBtnEnabled(btn_annot_rect, false);
                SetBtnChecked(btn_annot_oval, true);
                SetBtnEnabled(btn_annot_stamp, false);
                SetBtnEnabled(btn_annot_note, false);
            }
        } else if (arg0 == btn_annot_stamp) {
            if (m_set) {
                m_view.PDFSetStamp(1);
                SetBtnEnabled(btn_annot_ink, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_line, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_rect, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_oval, m_view.PDFCanSave());
                SetBtnChecked(btn_annot_stamp, false);
                SetBtnEnabled(btn_annot_note, m_view.PDFCanSave());
            } else {
                m_view.PDFSetStamp(0);
                SetBtnEnabled(btn_annot_ink, false);
                SetBtnEnabled(btn_annot_line, false);
                SetBtnEnabled(btn_annot_rect, false);
                SetBtnEnabled(btn_annot_oval, false);
                SetBtnChecked(btn_annot_stamp, true);
                SetBtnEnabled(btn_annot_note, false);
            }
        } else if (arg0 == btn_annot_note) {
            if (m_set) {
                m_view.PDFSetNote(1);
                SetBtnEnabled(btn_annot_ink, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_line, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_rect, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_oval, m_view.PDFCanSave());
                SetBtnEnabled(btn_annot_stamp, m_view.PDFCanSave());
                SetBtnChecked(btn_annot_note, false);
            } else {
                m_view.PDFSetNote(0);
                SetBtnEnabled(btn_annot_ink, false);
                SetBtnEnabled(btn_annot_line, false);
                SetBtnEnabled(btn_annot_rect, false);
                SetBtnEnabled(btn_annot_oval, false);
                SetBtnEnabled(btn_annot_stamp, false);
                SetBtnChecked(btn_annot_note, true);
            }
        } else if (arg0 == btn_annot_back) {
            m_view.PDFCancelAnnot();
            m_set = false;
            SetBtnEnabled(btn_annot_ink, m_view.PDFCanSave());
            SetBtnEnabled(btn_annot_line, m_view.PDFCanSave());
            SetBtnEnabled(btn_annot_rect, m_view.PDFCanSave());
            SetBtnEnabled(btn_annot_oval, m_view.PDFCanSave());
            SetBtnEnabled(btn_annot_stamp, m_view.PDFCanSave());
            SetBtnEnabled(btn_annot_note, m_view.PDFCanSave());
            m_bar_annot.BarSwitch(m_bar_cmd);
            m_bar_seek.BarShow();
            m_bar_status = BAR_CMD;
        } else if (arg0 == btn_find_back) {
            m_bar_find.BarSwitch(m_bar_cmd);
            m_bar_seek.BarShow();
            m_bar_status = BAR_CMD;
        } else if (arg0 == btn_act_back) {
            m_view.PDFCancelAnnot();
            m_bar_act.BarSwitch(m_bar_cmd);
            m_bar_seek.BarShow();
            m_bar_status = BAR_CMD;
        } else if (arg0 == btn_act_edit) {
            m_view.PDFEditAnnot();
            m_bar_act.BarSwitch(m_bar_cmd);
            m_bar_seek.BarShow();
            m_bar_status = BAR_CMD;
        } else if (arg0 == btn_act_perform) {
            m_view.PDFPerformAnnot();
            m_bar_act.BarSwitch(m_bar_cmd);
            m_bar_seek.BarShow();
            m_bar_status = BAR_CMD;
        } else if (arg0 == btn_act_remove) {
            m_view.PDFRemoveAnnot();
            m_bar_act.BarSwitch(m_bar_cmd);
            m_bar_seek.BarShow();
            m_bar_status = BAR_CMD;
        } else if (arg0 == view_vert) {
            savePreferences("view", 0);
            m_view.PDFSetView(0);
            m_menu_view.MenuDismiss();
        } else if (arg0 == view_single) {
            savePreferences("view", 3);
            m_view.PDFSetView(3);
            m_menu_view.MenuDismiss();
        } else if (arg0 == view_dual) {
            savePreferences("view", 4);
            m_view.PDFSetView(4);
            m_menu_view.MenuDismiss();
        }
    }

    private void savePreferences(String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(m_view.getContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
