package com.example.mockup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener{

    Button btnMain1, btnMain2, btnMain3, btnMain4, btnMainClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoPermissions.Companion.loadAllPermissions(this,100);

        btnMain1 = findViewById(R.id.btnMain1);
        btnMain2 = findViewById(R.id.btnMain2);
        btnMain3 = findViewById(R.id.btnMain3);
        btnMain4 = findViewById(R.id.btnMain4);
        btnMainClose = findViewById(R.id.btnMainClose);

        // 액션바 설정
        ActionBar bar = getSupportActionBar();
        bar.setTitle("CROPS");


        // 안드로이드 버전이 8.0(oreo) 이상인 경우 외부저장소 접근 허용 유무 묻기(checkSelfPermission)
        if (Build.VERSION.SDK_INT >= 26) {
            int pCheck = ActivityCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            // 보안체크가 되지 않은 경우
            if (pCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);
            } else {  // 체크를 한 경우
                showToast("어서오세요");
            }
        } else {  // 안드로이드 버전이 8.0(oreo) 이하인 경우 sdcardProcess() 실행
            showToast("어서오세요");
        }




        // <이미 비료를 주신 경우> 버튼 시작
        btnMain1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FirstActivity.class);
                startActivity(intent);
            }
        }); // <이미 비료를 주신 경우> 버튼 끝


        // <밑거름 계산기> 버튼 시작
        btnMain2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                startActivity(intent);
            }
        }); // <밑거름 계산기> 버튼 끝


        // <웃거름 계산기> 버튼 시작
        btnMain3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ThirdActivity.class);
                startActivity(intent);
            }
        }); // <웃거름 계산기> 버튼 끝


        // <농약계산기> 버튼 시작
        btnMain4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FourthActivity.class);
                startActivity(intent);
            }
        }); // <농약계산기> 버튼 끝


        // <앱 종료> 버튼 시작
        btnMainClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 메인 액티비티에 대화상자를 만들겠다는 명령
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("종료");                        // 대화상자 제목
                builder.setMessage("정말로 종료하시겠습니까?");  // 대화상자 내용

                // PositiveButton: 대화상자 오른쪽에 위치하는 버튼(누르면 종료)
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();   // 종료
                    }
                });

                // NegativeButton: 대화상자 왼쪽에 위치하는 버튼(누르면 취소 메시지)
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "취소를 누르셨습니다.", Toast.LENGTH_LONG).show();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }); // <앱 종료> 버튼 끝

    }

    // Toast 메서드
    void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDenied(int i, String[] strings) {

    }

    @Override
    public void onGranted(int i, String[] strings) {

    }
}