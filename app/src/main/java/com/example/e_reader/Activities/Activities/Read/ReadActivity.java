package com.example.e_reader.Activities.Activities.Read;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_reader.Activities.BookTypes.ParserPicker;
import com.example.e_reader.R;

public class ReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        String uri = getIntent().getStringExtra("uri");
        String fileType = ParserPicker.getFileType(uri, this);

        if ("application/epub+zip".equals(fileType)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_reader_container, new ReadEpubFragment())
                    .commit();
        }

        if ("application/pdf".equals(fileType)) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_reader_container, new ReadPdfFragment())
                    .commit();

        }
    }
}
