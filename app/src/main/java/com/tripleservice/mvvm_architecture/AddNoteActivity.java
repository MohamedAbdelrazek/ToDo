package com.tripleservice.mvvm_architecture;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNoteActivity extends AppCompatActivity {

    public static String EXTRA_TITLE = "com.tripleservice.mvvm_architecture.EXTRA_TITLE";
    public static String EXTRA_DESC = "com.tripleservice.mvvm_architecture.EXTRA_DESC";
    public static String EXTRA_PRIORITY = "com.tripleservice.mvvm_architecture.EXTRA_PRIORITY";
    public static String EXTRA_ID = "com.tripleservice.mvvm_architecture.EXTRA_ID";

    @BindView(R.id.activity_add_note_title_edit_text)
    EditText activityAddNoteTitleEditText;
    @BindView(R.id.activity_add_note_desc_edit_text)
    EditText activityAddNoteDescEditText;
    @BindView(R.id.activity_add_note_priority_number_picker)
    NumberPicker activityAddNotePriorityNumberPicker;
    @BindView(R.id.activity_add_note_priority_edit_text)
    TextView activityAddNotePriorityEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        ButterKnife.bind(this);
        activityAddNotePriorityNumberPicker.setMinValue(1);
        activityAddNotePriorityNumberPicker.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");

            activityAddNoteTitleEditText.setText(intent.getStringExtra(EXTRA_TITLE));
            activityAddNoteDescEditText.setText(intent.getStringExtra(EXTRA_DESC));
            activityAddNotePriorityNumberPicker.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));

        } else {
            setTitle("Add Note");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note: {
                saveNote();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void saveNote() {
        String title = activityAddNoteTitleEditText.getText().toString();
        String desc = activityAddNoteDescEditText.getText().toString();
        int priority = activityAddNotePriorityNumberPicker.getValue();

        if (title.trim().isEmpty() | desc.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title and description !", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_DESC, desc);
        intent.putExtra(EXTRA_PRIORITY, priority);


        int id = getIntent().getIntExtra(EXTRA_ID, -1);

        if (id != -1) {
            intent.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, intent);
        finish();

    }
}
