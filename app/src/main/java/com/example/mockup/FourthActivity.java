package com.example.mockup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pedro.library.AutoPermissionsListener;

public class FourthActivity extends AppCompatActivity implements AutoPermissionsListener {

    Button btnCal1, btnCal2;
    // Button btnHome;
    EditText edt1, edt2, edt4, edt5;
    TextView tv3, tv6;
    String num1, num2;
    double result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);

        // 상태창
        if (Build.VERSION.SDK_INT >= 21) {

            getWindow().setStatusBarColor(Color.rgb(28,181,152)); }

        ActionBar bar = getSupportActionBar();
        bar.setTitle("농약 계산기");
        bar.setDisplayHomeAsUpEnabled(true);

        //btnHome =findViewById(R.id.btnHome);
        btnCal1 = findViewById(R.id.btnCal1);
        btnCal2 = findViewById(R.id.btnCal2);

        edt1 = findViewById(R.id.edt1);
        edt2 = findViewById(R.id.edt2);
        tv3 = findViewById(R.id.tv3);
        edt4 = findViewById(R.id.edt4);
        edt5 = findViewById(R.id.edt5);
        tv6 = findViewById(R.id.tv6);

        btnCal1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    num1 = edt2.getText().toString();   // 물 양(L)
                    num2 = edt1.getText().toString();   // 약제량(ml)
                    if (num1.equals("") || num2.equals("")) {
                        showToast("입력값이 비었습니다.");
                    } else {
                        result = Double.parseDouble(num1) / Double.parseDouble(num2);
                        result=(int)(result*10000)/10.0;
                        tv3.setText("" + result+"배");
                    }
                } catch (java.lang.ArithmeticException e) {
                    showToast("0으로 계산할 수 없습니다.");
                }

            }
        });

        btnCal2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    num1 = edt5.getText().toString();   // 물 양(L)
                    num2 = edt4.getText().toString();   // 희석배수(배)
                    if (num1.equals("") || num2.equals("")) {
                        showToast("입력값이 비었습니다.");
                    } else {
                        result = Double.parseDouble(num1) / Double.parseDouble(num2);
                        result=(int)(result*10000)/10.0;
                        tv6.setText("" + result+"ml");
                    }
                } catch (java.lang.ArithmeticException e) {
                    showToast("0으로 계산할 수 없습니다.");
                }
            }
        });

        /* btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });*/

    }

    // Toast 메서드
    void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();                     // 맨 위에 올라온 액티비티가 한 장 없어지는 것.(= 이전 화면으로 돌아감)
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDenied(int i, String[] strings) {

    }

    @Override
    public void onGranted(int i, String[] strings) {

    }
}