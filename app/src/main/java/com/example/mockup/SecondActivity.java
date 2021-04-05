package com.example.mockup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    Spinner spCategory, spName;
    EditText edtFertArea, edtInputN, edtInputP, edtInputK;
    RadioButton rdoSpaceUnit1, rdoSpaceUnit2;
    RadioGroup rdoGroup;
    TextView spaceUnit, recommendFert1, recommendFert2, recommendFert3;
    Button btnCal;
    Double changeSquareMeter;
    Double  recommendPreN,  recommendPreP,  recommendPreK;
    String string1, string2, string3;

    // 데이터베이스 변수
    SQLiteDatabase sqlDB;
    ArrayAdapter<String> adapterC, adapterN;
    ArrayList<String> categoryData, nameData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        spCategory =findViewById(R.id.spCategory);
        spName =findViewById(R.id.spName);
        edtFertArea = findViewById(R.id.edtFertArea);
        edtInputN  = findViewById(R.id.edtInputN);
        edtInputP  = findViewById(R.id.edtInputP);
        edtInputK  = findViewById(R.id.edtInputK);
        rdoGroup = findViewById(R.id.rdoGroup);
        rdoSpaceUnit1 = findViewById(R.id.rdoSpaceUnit1);
        rdoSpaceUnit2 = findViewById(R.id.rdoSpaceUnit2);
        spaceUnit = findViewById(R.id.spaceUnit);
        recommendFert1 = findViewById(R.id.recommendFert1);
        recommendFert2 = findViewById(R.id.recommendFert2);
        recommendFert3 = findViewById(R.id.recommendFert3);
        btnCal = findViewById(R.id.btnCal);

        // 액션바 설정
        ActionBar bar = getSupportActionBar();
        bar.setTitle("밑거름 계산기");
        bar.setDisplayHomeAsUpEnabled(true);

        // 스피너(database) 설정 시작
        categoryData = new ArrayList<String>();
        nameData = new ArrayList<String>();

        try {
            boolean check = isCheckDB(this);

            if (!check) {
                copyDB(this);
            }

            sqlDB = SQLiteDatabase.openDatabase("/data/data/com.example.mockup/databases/cropsDB.db", null, SQLiteDatabase.OPEN_READONLY);

            Cursor cursor;
            cursor = sqlDB.rawQuery("SELECT distinct(category) FROM cropsTBL;", null);
            while (cursor.moveToNext()) {
                categoryData.add(cursor.getString(0));
            }

            adapterC = new ArrayAdapter<String>(SecondActivity.this, android.R.layout.simple_spinner_dropdown_item, categoryData);
            spCategory.setAdapter(adapterC);
            cursor.close();

            // 작물유형 선택
            spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    nameData.clear();  // 중요: 기존 드롭다운 메뉴에 추가되지 않도록
                    Cursor cursor1;
                    cursor1=sqlDB.rawQuery("SELECT name FROM cropsTBL WHERE category='"+spCategory.getSelectedItem().toString()+"';",null);
                    while (cursor1.moveToNext()){
                        nameData.add(cursor1.getString(0));
                    }
                    adapterN = new ArrayAdapter<String>(SecondActivity.this, android.R.layout.simple_spinner_dropdown_item,nameData);
                    spName.setAdapter(adapterN);
                    cursor1.close();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // 작물이름 선택
            spName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Cursor cursor2;
                    cursor2 = sqlDB.rawQuery("SELECT * FROM cropsTBL WHERE name='" + spName.getSelectedItem().toString() + "' AND category='" + spCategory.getSelectedItem().toString() + "';", null);
                    cursor2.moveToFirst();
                    recommendPreN = cursor2.getDouble(2); // 밑거름 질소값
                    recommendPreP = cursor2.getDouble(3); // 밑거름 인산값
                    recommendPreK = cursor2.getDouble(4); // 밑거름 칼리값

                    string1 = cursor2.getString(8); // 추천 밑거름1
                    string2 = cursor2.getString(9); // 추천 밑거름2
                    string3 = cursor2.getString(10); // 추천 밑거름3

                    recommendFert1.setText(string1);
                    recommendFert2.setText(string2);
                    recommendFert3.setText(string3);
                    cursor2.close();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }catch (Exception e) {
            showToast("복사 중 에러가 발생하였습니다.");
        } // 스피너(database) 설정 끝


        // 평수를 선택한 경우
        rdoSpaceUnit1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spaceUnit.setText(" 평");
            }
        });

        // 제곱미터를 선택한 경우
        rdoSpaceUnit2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spaceUnit.setText(" 제곱미터");
            }
        });


        // <계산> 버튼 시작
        btnCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), SecondCalResultActivity.class);

                // intent.putExtra("cropChoice",spName.getText().toString()); // 선택한 작물의 이름

                try {
                    switch (rdoGroup.getCheckedRadioButtonId()) {
                        case R.id.rdoSpaceUnit1: // 평수 -> (평수=3.3*제곱미터)
                            changeSquareMeter = Double.parseDouble(edtFertArea.getText().toString())*3.3; // 변환
                            intent.putExtra("FertilizerArea", changeSquareMeter); // 변환한 값을 전달
                            break;
                        case R.id.rdoSpaceUnit2: // 제곱미터
                            changeSquareMeter = Double.parseDouble(edtFertArea.getText().toString());
                            intent.putExtra("FertilizerArea", changeSquareMeter); // 변환 없이 전달
                            break;
                    }

                }catch (NumberFormatException e){
                    showToast("값을 입력해주세요");
                }

                try {

                    try {
                        if (edtFertArea.getText().toString().equals("")) {
                            showToast("면적 입력값이 비었습니다.");
                        }else if(edtInputN.getText().toString().equals("") || edtInputP.getText().toString().equals("") || edtInputK.getText().toString().equals("")){
                            showToast("비율은 0이라도 입력해주세요");
                        }else{
                            intent.putExtra("spaceUnit",spaceUnit.getText().toString()); // 면적 단위
                            intent.putExtra("InputN", Integer.parseInt(edtInputN.getText().toString())); // 질소 비율
                            intent.putExtra("InputP", Integer.parseInt(edtInputP.getText().toString())); // 인산 비율
                            intent.putExtra("InputK", Integer.parseInt(edtInputK.getText().toString())); // 칼리 비율
                            intent.putExtra("cropsName",spName.getSelectedItem().toString()); // 작물의 이름
                            intent.putExtra("recommendPreN",recommendPreN);// 권장 밑거름 질소값
                            intent.putExtra("recommendPreP",recommendPreP);// 권장 밑거름 인산값
                            intent.putExtra("recommendPreK",recommendPreK);// 권장 밑거름 칼리값

                            switch (rdoGroup.getCheckedRadioButtonId()) {
                                case R.id.rdoSpaceUnit1: // 평수 -> (평수=3.3*제곱미터)
                                    changeSquareMeter = Double.parseDouble(edtFertArea.getText().toString())*3.3; // 변환
                                    intent.putExtra("FertilizerArea", changeSquareMeter); // 변환한 값을 전달
                                    break;
                                case R.id.rdoSpaceUnit2: // 제곱미터
                                    changeSquareMeter = Double.parseDouble(edtFertArea.getText().toString());
                                    intent.putExtra("FertilizerArea", changeSquareMeter); // 변환 없이 전달
                                    break;
                            }
                            startActivity(intent);
                        }
                    }catch (java.lang.ArithmeticException e){
                        showToast("0을 초과하는 값을 입력해주세요");
                    }

                }catch (Exception e){
                    showToast("값을 입력해주세요");
                }
            }
        }); // <계산> 버튼 끝


    } // onCreate 끝

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    // 토스트 메서드 작성
    void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    // copyDB
    private void copyDB(Context context) {
        AssetManager manager = context.getAssets();
        String dbFolderPath = "/data/data/com.example.mockup/databases";
        String filePath = "/data/data/com.example.mockup/databases/cropsDB.db";
        File folder = new File(dbFolderPath);
        File file = new File(filePath);
        FileOutputStream fileOs = null;
        BufferedOutputStream bufferOs = null;
        try {
            InputStream inputS = manager.open("cropsDB.db");
            BufferedInputStream bufferIS = new BufferedInputStream(inputS);
            if (!folder.exists()) {
                //폴더 생성
                folder.mkdir();
            }
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }
            fileOs = new FileOutputStream(file);
            bufferOs = new BufferedOutputStream(fileOs);
            int read = -1;
            byte buffer[] = new byte[bufferIS.available()];
            while ((read = bufferIS.read(buffer, 0, bufferIS.available())) != -1) {
                bufferOs.write(buffer, 0, read);
            }

            bufferOs.flush();
            bufferOs.close();
            bufferIS.close();
            fileOs.close();
            inputS.close();

        } catch (IOException e) {
            //showToast("파일 복사 실패");
            showToast(e.getMessage());
        }
    }

    //DB 체크 메소드
    private boolean isCheckDB(Context context) {
        String filePath = "/data/data/com.example.mockup/databases/cropsDB.db";
        File file = new File(filePath);
        long newDBSize = 0;
        long oldDBSize = file.length(); // 이전 DB의 파일 크기기
        AssetManager manager = context.getAssets(); // Assets폴더에 접근
        try {
            InputStream inputS = manager.open("cropsDB.db");
            newDBSize = inputS.available();
        } catch (IOException e) {
            showToast("체크 실패");
        }

        if (file.exists()) {
            if (newDBSize != oldDBSize){
                return false;
            }else{
                return true;
            }
        } else {
            return false;
        }
    }


}