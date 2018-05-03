package com.uowee.share;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.muse.router.facade.annotation.Route;


@Route(path = "/share")
public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity_main);
    }
}
