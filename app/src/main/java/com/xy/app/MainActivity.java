package com.xy.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.xy.lib_common.router.RouterReDefine;
import com.xy.simplerouter.SimpleRouter;

public class MainActivity extends AppCompatActivity {
    Button btnOpenFloat;
    private TextView tvScreenInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        SimpleRouter.getInstance().startActivity(RouterReDefine.TEST_ACTIVITY);
    }
}
