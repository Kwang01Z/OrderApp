package com.project.quanlyorder.OtherLibrary;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.quanlyorder.R;

public class InputNoteDialog extends Dialog {
    public interface NoteListener {
        public void noteEntered(String note);
    }
    public Context context;
    private EditText edNote;
    private Button buttonOK;
    private Button buttonCancel;
    private InputNoteDialog.NoteListener listener;

    public InputNoteDialog(@NonNull Context context,InputNoteDialog.NoteListener listener) {
        super(context);
        this.listener = listener;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_note_dialog);
        edNote = (EditText) findViewById(R.id.ed_inputnote_txtNote);
        buttonOK = (Button) findViewById(R.id.mydatepicker_btnok);
        buttonCancel = (Button) findViewById(R.id.mydatepicker_btncancel);

        this.buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonOKClick();
            }
        });
        this.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonCancelClick();
            }
        });

    }
    // User click "OK" button.
    private void buttonOKClick()  {
        String note = this.edNote.getText().toString();

        if(note== null || note.isEmpty())  {
            Toast.makeText(this.context, "Please enter note", Toast.LENGTH_LONG).show();
            return;
        }
        this.dismiss(); // Close Dialog
        if(this.listener!= null)  {
            this.listener.noteEntered(note);
        }
    }

    // User click "Cancel" button.
    private void buttonCancelClick()  {
        this.dismiss();
    }
}