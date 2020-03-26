package com.xy.module_test;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xy.lib.utils.ScreenInfoUtils;
import com.xy.lib_common.router.RouterReDefine;
import com.xy.simplerouter.SimpleRouterClassRegister;
import com.xy.simplerouter.SimpleRouterObj;

@SimpleRouterClassRegister(key = RouterReDefine.TEST_ACTIVITY, type = SimpleRouterObj.ACTIVITY)
public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnOpenFloat;
    private TextView tvScreenInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        initView();
    }


    public void initView(){
        tvScreenInfo = findViewById(R.id.tv_screen_info);
        btnOpenFloat = findViewById(R.id.btn_open_float);
        btnOpenFloat.setOnClickListener(this);

        tvScreenInfo.setText(ScreenInfoUtils.getScreenInfo(this));
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_open_float){
            Intent intent = new Intent(this,FloatActivity.class);
            startActivity(intent);
        }
    }
}
