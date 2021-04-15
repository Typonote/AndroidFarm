package com.example.mockup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FirstCalResultActivity extends AppCompatActivity  {

    TextView recommendN, recommendP, recommendK, recommend2N, recommend2P, recommend2K, resultN, resultP, resultK;
    TextView noticeN, noticeP, noticeK;
    TextView tvCalResult;
    Button btnBack, btnCapture, btnHome;
    int noticeInputN, noticeInputP, noticeInputK; // 값을 넘겨받기 위한 변수
    Double space, kilogram, noticeCalN, noticeCalP, noticeCalK, result; // 넘겨받은 값과 계산을 하기위한 변수
    RadioButton rdoPost, rdoPre;
    String name;

    Double recommendPreN, recommendPreP, recommendPreK, recommendPostN, recommendPostP, recommendPostK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_cal_result);

        // 상태창
        if (Build.VERSION.SDK_INT >= 21) {

            getWindow().setStatusBarColor(Color.rgb(28,181,152)); }

        recommendN = findViewById(R.id.recommendN);
        recommendP = findViewById(R.id.recommendP);
        recommendK = findViewById(R.id.recommendK);
        recommend2N = findViewById(R.id.recommend2N);
        recommend2P = findViewById(R.id.recommend2P);
        recommend2K = findViewById(R.id.recommend2K);

        resultN = findViewById(R.id.resultN);
        resultP = findViewById(R.id.resultP);
        resultK = findViewById(R.id.resultK);
        noticeN = findViewById(R.id.noticeN);
        noticeP = findViewById(R.id.noticeP);
        noticeK = findViewById(R.id.noticeK);
        btnBack = findViewById(R.id.btnBack);
        btnCapture = findViewById(R.id.btnCapture);
        btnHome = findViewById(R.id.btnHome);
        tvCalResult = findViewById(R.id.tvCalResult);
        rdoPost = findViewById(R.id.rdoPost);
        rdoPre = findViewById(R.id.rdoPre);

        // 액션바 설정
        ActionBar bar = getSupportActionBar();
        bar.setTitle("계산결과");
        bar.setDisplayHomeAsUpEnabled(true);

        Intent gIntent = getIntent();

        // FirstActivity 면적을 넘겨 받음
        space = gIntent.getDoubleExtra("FertilizerArea", 0);
        Log.i("test","입력한 면적(제곱미터로 환산): "+space);


        // FirstActivity넘겨 받은 이름을 세팅
        name = gIntent.getStringExtra("cropsName");
        tvCalResult.setText("[ " + name + "의 계산결과" + " ]");
        Log.i("test","선택한 작물의 이름: "+name);

        //  FirstActivity넘겨 받은 권장량을 넘겨받고 계산하여 송출
        recommendPreN = gIntent.getDoubleExtra("recommendPreN", 1);  // 밑거름 질소
        double num1 = recommendPreN * space;
        recommend2N.setText(String.format("%.2f", num1) + " Kg");
        Log.i("test","밑거름 질소 권장량: "+num1);

        recommendPreP = gIntent.getDoubleExtra("recommendPreP", 0);   // 밑거름 인산
        double num2 = recommendPreP * space;
        recommend2P.setText(String.format("%.2f", num2) + " Kg");
        Log.i("test","밑거름 인산 권장량: "+recommendPreP);

        recommendPreK = gIntent.getDoubleExtra("recommendPreK", 1);   // 밑거름 칼리
        double num3 = recommendPreK * space;
        recommend2K.setText(String.format("%.2f", num3) + " Kg");
        Log.i("test","밑거름 칼리 권장량: "+recommendPreK);

        recommendPostN = gIntent.getDoubleExtra("recommendPostN", 1);  // 웃거름 질소
        double num4 = recommendPostN * space;
        recommendN.setText(String.format("%.2f", num4) + " Kg");
        Log.i("test","웃거름 질소 권장량: "+recommendPostN);

        recommendPostP = gIntent.getDoubleExtra("recommendPostP", 1);  // 웃거름 인산
        double num5 = recommendPostP * space;
        recommendP.setText(String.format("%.2f", num5) + " Kg");
        Log.i("test","웃거름 인산 권장량:"+recommendPostP);

        recommendPostK = gIntent.getDoubleExtra("recommendPostK", 1);  // 웃거름 칼리
        double num6 = recommendPostK * space;
        recommendK.setText(String.format("%.2f", num6) + " Kg");
        Log.i("test","웃거름 칼리 권장량: "+recommendPostK);


        // FirstActivity넘겨 받은 무게를 세팅
        kilogram = gIntent.getDoubleExtra("FertilizerWeight", 0);

        // 투입된 질소의 양 계산해서 출력
        noticeInputN = gIntent.getIntExtra("InputN", 0); // 비율을 받음
        noticeCalN = kilogram * noticeInputN / 100;
        resultN.setText(String.format("%.2f", noticeCalN) + " Kg");
        Log.i("test","입력한 질소 비율: "+noticeInputN);


        // 투입된 인산의 양 계산해서 출력
        noticeInputP = gIntent.getIntExtra("InputP", 0); // 비율을 받음
        noticeCalP = kilogram * noticeInputP / 100;
        resultP.setText(String.format("%.2f", noticeCalP) + " Kg");
        Log.i("test","입력한 인산 비율: "+noticeInputP);


        // 투입된 인산의 양 계산해서 출력
        noticeInputK = gIntent.getIntExtra("InputK", 0); // 비율을 받음
        noticeCalK = kilogram * noticeInputK / 100;
        resultK.setText(String.format("%.2f", noticeCalK) + " Kg");
        Log.i("test","입력한 칼리 비율: "+noticeInputK);

        // 웃거름 계산 출력

        if (num4 > noticeCalN) {
            result = num4 - noticeCalN;
            noticeN.setText(" 질소가 " + String.format("%.2f", result) + "Kg 만큼 부족합니다.");
            noticeN.setTextColor(Color.BLUE);
        } else if (num4 < noticeCalN) {
            result = noticeCalN - num4;
            noticeN.setText(" 질소가 " + String.format("%.2f", result) + "Kg 만큼 초과했습니다.");
            noticeN.setTextColor(Color.RED);
        } else {
            noticeN.setText(" 정량을 주셨습니다.");
        }

        if (num5 > noticeCalP) {
            result = num2 - noticeCalP;
            noticeP.setText(" 인산이 " + String.format("%.2f", result) + "Kg 만큼 부족합니다.");
            noticeP.setTextColor(Color.BLUE);
        } else if (num5 < noticeCalP) {
            result = noticeCalP - num5;
            noticeP.setText(" 인산이 " + String.format("%.2f", result) + "Kg 만큼 초과했습니다.");
            noticeP.setTextColor(Color.RED);
        } else {
            noticeP.setText(" 정량을 주셨습니다.");
        }

        if (num6 > noticeCalK) {
            result = num6 - noticeCalK;
            noticeK.setText(" 칼리가 " + String.format("%.2f", result) + "Kg 만큼 부족합니다.");
            noticeK.setTextColor(Color.BLUE);
        } else if (num6 < noticeCalK) {
            result = noticeCalK - num6;
            noticeK.setText(" 칼리가 " + String.format("%.2f", result) + "Kg 만큼 초과했습니다.");
            noticeK.setTextColor(Color.RED);
        } else {
            noticeK.setText(" 정량을 주셨습니다.");
        }


        rdoPost.setOnClickListener(new View.OnClickListener() {  // 웃거름
            @Override
            public void onClick(View v) {
                if (num4 > noticeCalN) {
                    result = num4 - noticeCalN;
                    noticeN.setText(" 질소가 " + String.format("%.2f", result) + "Kg 만큼 부족합니다.");
                    noticeN.setTextColor(Color.BLUE);
                } else if (num4 < noticeCalN) {
                    result = noticeCalN - num4;
                    noticeN.setText(" 질소가 " + String.format("%.2f", result) + "Kg 만큼 초과했습니다.");
                    noticeN.setTextColor(Color.RED);
                } else {
                    noticeN.setText(" 정량을 주셨습니다.");
                }

                if (num5 > noticeCalP) {
                    result = num2 - noticeCalP;
                    noticeP.setText(" 인산이 " + String.format("%.2f", result) + "Kg 만큼 부족합니다.");
                    noticeP.setTextColor(Color.BLUE);
                } else if (num5 < noticeCalP) {
                    result = noticeCalP - num5;
                    noticeP.setText(" 인산이 " + String.format("%.2f", result) + "Kg 만큼 초과했습니다.");
                    noticeP.setTextColor(Color.RED);
                } else {
                    noticeP.setText(" 정량을 주셨습니다.");
                }

                if (num6 > noticeCalK) {
                    result = num6 - noticeCalK;
                    noticeK.setText(" 칼리가 " + String.format("%.2f", result) + "Kg 만큼 부족합니다.");
                    noticeK.setTextColor(Color.BLUE);
                } else if (num6 < noticeCalK) {
                    result = noticeCalK - num6;
                    noticeK.setText(" 칼리가 " + String.format("%.2f", result) + "Kg 만큼 초과했습니다.");
                    noticeK.setTextColor(Color.RED);
                } else {
                    noticeK.setText(" 정량을 주셨습니다.");
                }
            }
        });


        rdoPre.setOnClickListener(new View.OnClickListener() {  // 밑거름
            @Override
            public void onClick(View v) {

                if (num1 > noticeCalN) {
                    result = num1 - noticeCalN;
                    noticeN.setText(" 질소가 " + String.format("%.2f", result) + "Kg 만큼 부족합니다.");
                    noticeN.setTextColor(Color.BLUE);
                } else if (num1 < noticeCalN) {
                    result = noticeCalN - num1;
                    noticeN.setText(" 질소가 " + String.format("%.2f", result) + "Kg 만큼 초과했습니다.");
                    noticeN.setTextColor(Color.RED);
                } else {
                    noticeN.setText(" 정량을 주셨습니다.");
                }

                if (num2 > noticeCalP) {
                    result = num2 - noticeCalP;
                    noticeP.setText(" 인산이 " + String.format("%.2f", result) + "Kg 만큼 부족합니다.");
                    noticeP.setTextColor(Color.BLUE);
                } else if (num2 < noticeCalP) {
                    result = noticeCalP - num2;
                    noticeP.setText(" 인산이 " + String.format("%.2f", result) + "Kg 만큼 초과했습니다.");
                    noticeP.setTextColor(Color.RED);
                } else {
                    noticeP.setText(" 정량을 주셨습니다.");
                }

                if (num3 > noticeCalK) {
                    result = num3 - noticeCalK;
                    noticeK.setText(" 칼리가 " + String.format("%.2f", result) + "Kg 만큼 부족합니다.");
                    noticeK.setTextColor(Color.BLUE);
                } else if (num3 < noticeCalK) {
                    result = noticeCalK - num3;
                    noticeK.setText(" 칼리가 " + String.format("%.2f", result) + "Kg 만큼 초과했습니다.");
                    noticeK.setTextColor(Color.RED);
                } else {
                    noticeK.setText(" 정량을 주셨습니다.");
                }
            }
        });

        // 뒤로가기 버튼
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Log.i("test","뒤로 이동");
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
                Log.i("test","홈으로 이동");
            }
        }); // 홈으로 버튼 끝

    }

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