package com.uowee.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

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
        switch (id) {
            case R.id.btn_share:
                ARouter.getInstance().build("/share/test").navigation();
                break;
            case R.id.btn_reader:
                ARouter.getInstance().build("/reader/test").navigation();
                break;
            default:
                break;
        }

    }
}
