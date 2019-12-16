package com.example.p3;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;

public class PopUp extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pop_up);

        DisplayMetrics  ws = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(ws);

        int width = ws.widthPixels;
        int height = ws.heightPixels;

        getWindow().setLayout((int)(width*.9),(int)(height *.4));



    }
}
