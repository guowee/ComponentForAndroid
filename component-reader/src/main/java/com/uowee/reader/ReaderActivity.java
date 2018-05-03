package com.uowee.reader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.muse.router.facade.annotation.Route;

@Route(path = "/reader")
public class ReaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reader_activity_main);
    }
}
