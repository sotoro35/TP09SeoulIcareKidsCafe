package com.hsr2024.tp09seoulicarekidscafe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ScrollingTabContainerView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {

    // 도대체 나는.... lodData(1);에 숫자를 왜 받게 했을까...? 무슨 의미가;;;

    TabLayout tab;


    RecyclerView recyclerView;

    RecyclerAdapter adapter1,adapter2;
    ArrayList<MyItem> items1 = new ArrayList<>();
    ArrayList<MyItem> items2 = new ArrayList<>();


    ProgressBar progressBar;

    MaterialToolbar toolbar; // 툴바 뒤로가기 버튼


    //콤보박스
    TextInputLayout inputlayout;
    ArrayList<String> comTexts = new ArrayList<>();

    ArrayAdapter arrayAdapter;

    AutoCompleteTextView autocomtv;


    // 서울공공형데이터에서 발급받은 인증키..
    String apikey = "545145716a736f7438394d43745866";


    String call= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        tab = findViewById(R.id.tab);
        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        adapter1 = new RecyclerAdapter(this,items1);
        adapter2 = new RecyclerAdapter(this,items2);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> finish());

        tab.addTab( tab.newTab().setText("무료시설"));
        tab.addTab( tab.newTab().setText("유료시설"));

        comTexts.add("전체보기");
        comTexts.add("강동구,강북구,강서구,광진구");
        comTexts.add("도봉구,동작구");
        comTexts.add("마포구,서초구,성동구");
        comTexts.add("양천구,영등포구,용산구");
        comTexts.add("종로구,중랑구");

        inputlayout = findViewById(R.id.inputlayout);
        arrayAdapter =new ArrayAdapter(this,R.layout.autocompleteitem,comTexts);

        autocomtv =(AutoCompleteTextView) inputlayout.getEditText();
        autocomtv.setAdapter(arrayAdapter);

        autocomtv.setText("전체보기", false);
        autocomtv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(SecondActivity.this, ""+position, Toast.LENGTH_SHORT).show();
                lodData();
            }
        });


        Intent intent= getIntent(); // 여기로 오는 택배아저씨들 집합!

        // 원하는 택배아저씨를 어떠케 가져오지..ㅠㅠ ...
        // 식별자(?)로 가져왔음..헐.. 왜 되는거지..? 묵시적인텐트...EX32참고함

        if (intent.getAction().toString().equals("tab1")){
            lodData();
            recyclerView.setAdapter(adapter1);
            tab.selectTab( tab.getTabAt(0) );

        } else if (intent.getAction().toString().equals("tab2")) {
            lodData();
            recyclerView.setAdapter(adapter2);
            // 메인에서 유료시설 눌렀을때 유료시설 탭이 나오도록 설정..
            tab.selectTab( tab.getTabAt(1) );
        }


        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().toString().equals("무료시설")) {
                    lodData();
                    recyclerView.setAdapter(adapter1);
                } else if (tab.getText().toString().equals("유료시설")) {
                    lodData();
                    recyclerView.setAdapter(adapter2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });//tabSelected..


    } //onCreate


    void lodData(){
        // 1. Manifest에서 퍼미션받기
        // 2. 네트워크 작업 할 스레드 만들기 (익명클래스)

        new Thread(){
            @Override
            public void run() {
                //기존 저장된 기차 내용 삭제하는 부분 추가하기(아직안함)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        items1.clear();
                        items2.clear();
                        adapter1.notifyDataSetChanged();
                        adapter2.notifyDataSetChanged();

                        progressBar.setVisibility(View.VISIBLE);

                    }
                });


                // 서버에서 가져 올 URL 정보 (1번 무료 , 2번 무료)
                String address= "";

                address = "http://openapi.seoul.go.kr:8088/"+apikey+"/xml/tnFcltySttusInfo1011/1/23/";

                try {
                    URL url = new URL(address);
                    InputStream is= url.openStream(); //바이트 스트림
                    InputStreamReader isr = new InputStreamReader(is); // 바이트 --> 문자로 변환

                    //스트림을 통해 xml파일을 읽어와서 분석해주는 분석가
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser pullParser = factory.newPullParser();
                    pullParser.setInput(isr); // 스트림으로 받은 문자를 분석설계!

                    int eventType =pullParser.getEventType(); //분석작업 시작!!

                    MyItem item= null; // 키즈카페 1곳 정보



                    while ( eventType != XmlPullParser.END_DOCUMENT ) {

                        switch (eventType){
                            case XmlPullParser.START_DOCUMENT:
                                //runOnUiThread(()-> Toast.makeText(SecondActivity.this, "파싱시작!", Toast.LENGTH_SHORT).show());
                                break;


                            case XmlPullParser.START_TAG:
                                String tagName = pullParser.getName();
                                if (tagName.equals("row")){
                                    item= new MyItem(); //빈 박스 만들기

                                }else if (tagName.equals("ATDRC_NM")){
                                    pullParser.next();
                                    item.atdrc_nm=pullParser.getText();

                                }else if (tagName.equals("RNTFEE_FREE_AT")){
                                    pullParser.next();
                                    item.rntfee_free_at=pullParser.getText();
                                } else if (tagName.equals("FCLTY_NM")) {
                                    pullParser.next();
                                    item.fclty_nm = pullParser.getText();

                                }else if (tagName.equals("CTTPC")){
                                    pullParser.next();
                                    item.cttpc = pullParser.getText();
                                    item.call_iv = pullParser. getText();

                                } else if (tagName.equals("BASS_ADRES")) {
                                    pullParser.next();
                                    item.bass_adres = pullParser.getText();

//                                } else if (tagName.equals("DETAIL_ADRES")) {
//                                    pullParser.next();
//                                    item.detail_adres = pullParser.getText();

                                } else if (tagName.equals("OPEN_WEEK")) {
                                    pullParser.next();
                                    item.open_week = pullParser.getText();

                                } else if (tagName.equals("CLOSE_WEEK")) {
                                    pullParser.next();
                                    item.close_week = pullParser.getText();

                                } else if (tagName.equals("POSBL_AGRDE")) {
                                    pullParser.next();
                                    item.posbl_agrde = pullParser.getText()+"이용가능합니다.";

                                }

                                break;

                            case XmlPullParser.END_TAG:
                                String tagName2 = pullParser.getName();
                                if (tagName2.equals("row") && item.rntfee_free_at.equals("Y")) {
                                    item.rntfee_free_at = "무료";
                                    if (autocomtv.getText().toString().equals("전체보기")) items1.add(item);
                                    else if (autocomtv.getText().toString().contains(item.atdrc_nm)) items1.add(item);


                                }else if (tagName2.equals("row") && item.rntfee_free_at.equals("N")){
                                    item.rntfee_free_at = "유료";
                                    if (autocomtv.getText().toString().equals("전체보기")) items2.add(item);
                                    else if (autocomtv.getText().toString().contains(item.atdrc_nm)) items2.add(item);

                                }
                                break;

                        }   eventType = pullParser.next();
                        Log.d("tab1","리스트의 개수: " + items1.size() );
                        Log.d("tab2","리스트의 개수: " + items2.size() );


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter1.notifyDataSetChanged();
                                adapter2.notifyDataSetChanged();

                                progressBar.setVisibility(View.GONE);
                            }
                        });

                    }//while...


                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (XmlPullParserException e) {
                    throw new RuntimeException(e);
                }

            } //run...
        }.start(); //new Thread..
    } //lodData...



}//MainActivity