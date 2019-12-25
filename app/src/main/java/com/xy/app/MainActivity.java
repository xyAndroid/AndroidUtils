package com.xy.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xy.lib.utils.ScreenUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnOpenFloat;
    private TextView tvScreenInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    public void initView(){
        tvScreenInfo = findViewById(R.id.tv_screen_info);
        btnOpenFloat = findViewById(R.id.btn_open_float);
        btnOpenFloat.setOnClickListener(this);

        tvScreenInfo.setText(ScreenUtils.getScreenInfo(this));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_open_float:
                Intent intent = new Intent(this,FloatActivity.class);
                startActivity(intent);
                break;
        }
    }
}
