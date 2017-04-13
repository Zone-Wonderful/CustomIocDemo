package com.zone.customiocdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zone.ioc.CheckNet;
import com.zone.ioc.OnClick;
import com.zone.ioc.ViewById;
import com.zone.ioc.ViewUtils;

/**
 * Xutils
 * 属性注入：利用反射区获取Annotation ---> value  --->findViewById  ---->反射注入属性
 * 事件注入：利用反射区获取Annotation ---> value  --->findViewById  ----> setOnclickListener---> 反射执行方法
 */
public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.tv)
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //x.view().inject(this);

        //初始化注解
        ViewUtils.inject(this);
    }
    @OnClick({R.id.tv,R.id.iv})
    @CheckNet //没网不执行该方法
    private void onClick(View view){

        Toast.makeText(this,"text点击",Toast.LENGTH_SHORT).show();
    }

}
