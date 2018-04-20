package com.uowee.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Toast;

import com.alibaba.android.arouter.launcher.ARouter;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AppCompatButton mShareBtn;
    private AppCompatButton mReaderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mShareBtn = findViewById(R.id.btn_share);
        mReaderBtn = findViewById(R.id.btn_reader);
        mShareBtn.setOnClickListener(this);
        mReaderBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_share) {
            Toast.makeText(MainActivity.this, "AAAAA", Toast.LENGTH_SHORT).show();
            ARouter.getInstance().build("/share/center").navigation();
        } else if (id == R.id.btn_reader) {
            ARouter.getInstance().build("/reader/test").navigation();
        }
    }
}
