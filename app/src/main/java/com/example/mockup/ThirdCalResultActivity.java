package com.example.mockup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ThirdCalResultActivity extends AppCompatActivity {

    TextView recommendN, recommendP, recommendK, resultN, resultP, resultK;
    TextView noticeN, noticeP, noticeK;
    TextView tvCalResult1, tvCalResult2;
    Button btnBack, btnCapture, btnHome;
    int noticeInputN,noticeInputP,noticeInputK; // 값을 넘겨받기 위한 변수
    Double space, resultCalP, resultCalK, result; // 넘겨받은 값과 계산을 하기위한 변수
    String name;

    Double recommendPostN, recommendPostP, recommendPostK, recommendFertWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_cal_result);



        recommendN = findViewById(R.id.recommendN);
        recommendP = findViewById(R.id.recommendP);
        recommendK = findViewById(R.id.recommendK);

        resultN = findViewById(R.id.resultN);
        resultP = findViewById(R.id.resultP);
        resultK = findViewById(R.id.resultK);
        noticeN = findViewById(R.id.noticeN);
        noticeP = findViewById(R.id.noticeP);
        noticeK = findViewById(R.id.noticeK);
        btnBack = findViewById(R.id.btnBack);
        btnCapture = findViewById(R.id.btnCapture);
        btnHome = findViewById(R.id.btnHome);
        tvCalResult1 = findViewById(R.id.tvCalResult1);
        tvCalResult2 = findViewById(R.id.tvCalResult2);

        // 액션바 설정
        ActionBar bar = getSupportActionBar();
        bar.setTitle("웃거름 계산결과");
        bar.setDisplayHomeAsUpEnabled(true);

        Intent gIntent = getIntent();

        // ThirdActivity에서 면적을 넘겨 받음
        space = gIntent.getDoubleExtra("FertilizerArea", 0);

        // ThirdActivity에서 넘겨 받은 이름을 세팅
        name = gIntent.getStringExtra("cropsName");
        tvCalResult1.setText("[ " + name + "의 계산결과" + " ]");

        //  ThirdActivity에서 넘겨 받은 권장량을 계산하여 송출
        recommendPostN = gIntent.getDoubleExtra("recommendPostN", 1);  // 밑거름 질소
        double num1 = recommendPostN * space;  // 권장 질소 밑거름 양
        recommendN.setText(String.format("%.2f", num1) + " Kg");

        recommendPostP = gIntent.getDoubleExtra("recommendPostP", 0);   // 밑거름 인산
        double num2 = recommendPostP * space;
        recommendP.setText(String.format("%.2f", num2) + " Kg");

        recommendPostK = gIntent.getDoubleExtra("recommendPostK", 1);   // 밑거름 칼리
        double num3 = recommendPostK * space;
        recommendK.setText(String.format("%.2f", num3) + " Kg");

        // 비료 양 계산
        noticeInputN = gIntent.getIntExtra("InputN", 0); // 비율을 받음
        recommendFertWeight = num1*100/noticeInputN;
        tvCalResult2.setText(String.format("%.2f", recommendFertWeight)+"kg 비료를 나누어 살포하세요.");


        // 투입될 질소 양 계산하여 출력
        resultN.setText(String.format("%.2f", num1) + " Kg");

        // 투입될 인산 양 계산하여 출력
        noticeInputP = gIntent.getIntExtra("InputP", 0); // 비율을 받음
        resultCalP = recommendFertWeight *noticeInputP/100;
        resultP.setText(String.format("%.2f", resultCalP) + " Kg");

        // 투입될 칼리 양 계산하여 출력
        noticeInputK = gIntent.getIntExtra("InputK", 0); // 비율을 받음
        resultCalK = recommendFertWeight *noticeInputK/100;
        resultK.setText(String.format("%.2f", resultCalK) + " Kg");

        if (num2 > resultCalP) {
            result = num2 - resultCalP;
            noticeP.setText(" 인산이 " + String.format("%.2f", result) + "Kg 만큼 부족합니다.");
            noticeP.setTextColor(Color.BLUE);
        } else if (num2 < resultCalP) {
            result = resultCalP - num2;
            noticeP.setText(" 인산이 " + String.format("%.2f", result) + "Kg 만큼 초과했습니다.");
            noticeP.setTextColor(Color.RED);
        } else {
            noticeP.setText(" 정량을 주셨습니다.");
        }

        if (num3 > resultCalK) {
            result = num3 - resultCalK;
            noticeK.setText(" 칼리가 " + String.format("%.2f", result) + "Kg 만큼 부족합니다.");
            noticeK.setTextColor(Color.BLUE);
        } else if (num3 < resultCalK) {
            result = resultCalK - num3;
            noticeK.setText(" 칼리가 " + String.format("%.2f", result) + "Kg 만큼 초과했습니다.");
            noticeK.setTextColor(Color.RED);
        } else {
            noticeK.setText(" 정량을 주셨습니다.");
        }

        // 뒤로가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }); // 뒤로가기 버튼 끝

        // 캡처 버튼
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View rootView = getWindow().getDecorView();

                File screenShot = ScreenShot(rootView);
                if (screenShot != null) {
                    //갤러리에 추가
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(screenShot)));
                    showToast("갤러리에 저장되었습니다.");
                }
            }
        });  // 캡처 버튼 끝

        // 홈으로 버튼
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        }); // 홈으로 버튼 끝

    } // onCreate끝

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {  // 액션바 뒤로가기
        finish();
        return super.onOptionsItemSelected(item);
    }

    // 화면 캡쳐하기 메서드 작성
    public File ScreenShot(View view) {

        // 캡쳐가 저장될 외부 저장소 경로 설정
        final String CAPTURE_PATH = "/CROPS";

        // 사진이 담길 폴더 이름 설정
        String strFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + CAPTURE_PATH;
        File folder = new File(strFolderPath);
        if (!folder.exists()) { // 폴더가 없다면 생성
            folder.mkdirs();
        }

        // 파일 이름 설정(캡처 시간으로 설정)
        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = simpleDate.format(mDate);
        String mPath = strFolderPath + "/" + getTime + ".jpg";

        view.setDrawingCacheEnabled(true);  //화면에 뿌릴때 캐시를 사용하게 한다

        Bitmap screenBitmap = view.getDrawingCache();   //캐시를 비트맵으로 변환

        File file = new File(mPath);  //Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try {
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os);   //비트맵을 PNG파일로 변환
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        view.setDrawingCacheEnabled(false);
        return file;

    } // 화면 캡쳐하기 메서드 끝

    // 토스트 메서드 작성
    void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    } // 토스트 메서드 끝

}