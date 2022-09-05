package com.project.quanlyorder.OtherLibrary;

import androidx.annotation.NonNull;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import com.project.quanlyorder.R;

public class DisplayBillInfoNoteActivity extends Dialog {

    public Context context;
    public String txtNote;
    TextView tvNote;

    public DisplayBillInfoNoteActivity(@NonNull Context context, String txtNote) {
        super(context);
        this.txtNote = txtNote;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_display_bill_info_note);

        tvNote =(TextView) findViewById(R.id.tv_displaynote_note);
        this.tvNote.setText(txtNote);
    }

}