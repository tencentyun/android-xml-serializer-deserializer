package com.tencent.qcloud.qcloudxml;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.qcloud.qcloudxml.core.QCloudXml;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String xml = "\n" +
                "<TestBeana>\n" +
                "  <Aab>aaa</Aab>\n" +
                "  <Bbb>3</Bbb>\n" +
                "  <ITestBean2>\n" +
                "    <Ibbb>1</Ibbb>\n" +
                "  </ITestBean2>\n" +
                "  <ITestBean1>\n" +
                "    <Iaaa>iaaaaaa</Iaaa>\n" +
                "  </ITestBean1>\n" +
                "  <ITestBean2List>\n" +
                "    <ITestBean2>\n" +
                "      <Ibbb>1</Ibbb>\n" +
                "    </ITestBean2>\n" +
                "    <ITestBean2>\n" +
                "      <Ibbb>2</Ibbb>\n" +
                "    </ITestBean2>\n" +
                "    <ITestBean2>\n" +
                "      <Ibbb>3</Ibbb>\n" +
                "    </ITestBean2>\n" +
                "  </ITestBean2List>\n" +
                "</TestBeana>";

//        @SuppressLint("ResourceType") InputStream inputStream = this.getResources().openRawResource(R.xml.test);

        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

        TestBean testBean = null;
        try {
            testBean = QCloudXml.fromXml(inputStream, TestBean.class);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView tv_from = findViewById(R.id.tv_from);
        tv_from.setText("TestBean.aaa = "+testBean.aaa);

        TestBean testBeanTo = new TestBean();
        testBeanTo.aaa = "aaa";
        testBeanTo.bbb = 3;
        testBeanTo.iTestBean1 = new TestBean.ITestBean1();
        testBeanTo.iTestBean1.iaaa = "iaaaaaa";
        testBeanTo.iTestBean2List = new ArrayList<>();
        TestBean.ITestBean2 iTestBean21 = new TestBean.ITestBean2();
        iTestBean21.ibbb = 1;
        TestBean.ITestBean2 iTestBean22 = new TestBean.ITestBean2();
        iTestBean22.ibbb = 2;
        TestBean.ITestBean2 iTestBean23 = new TestBean.ITestBean2();
        iTestBean23.ibbb = 3;
        testBeanTo.iTestBean2List.add(iTestBean21);
        testBeanTo.iTestBean2List.add(iTestBean22);
        testBeanTo.iTestBean2List.add(iTestBean23);

        String xmlTo = "";
        try {
            xmlTo = QCloudXml.toXml(testBeanTo);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView tv_to = findViewById(R.id.tv_to);
        tv_to.setText(xmlTo);
        Log.d("qjd", xmlTo);

        findViewById(R.id.btnTest).setVisibility(View.GONE);
//        findViewById(R.id.btnTest).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                test();
//            }
//        });
    }

    private void test(){
        String xml = "\n" +
                "<TestBeana>\n" +
                "  <Aab>aaa</Aab>\n" +
                "  <Bbb>3</Bbb>\n" +
                "  <ITestBean1>\n" +
                "    <Iaaa>iaaaaaa</Iaaa>\n" +
                "  </ITestBean1>\n" +
                "  <ITestBean2List>\n" +
                "    <ITestBean2>\n" +
                "      <Ibbb>1</Ibbb>\n" +
                "    </ITestBean2>\n" +
                "    <ITestBean2>\n" +
                "      <Ibbb>2</Ibbb>\n" +
                "    </ITestBean2>\n" +
                "    <ITestBean2>\n" +
                "      <Ibbb>3</Ibbb>\n" +
                "    </ITestBean2>\n" +
                "  </ITestBean2List>\n" +
                "</TestBeana>";

//        @SuppressLint("ResourceType") InputStream inputStream = this.getResources().openRawResource(R.xml.test);

        InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
        TestBean testBean = null;
        try {
            testBean = QCloudXml.fromXml(inputStream, TestBean.class);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        long now = System.currentTimeMillis();
        for (int i=0; i<10000; ++i) {
            try {
                QCloudXml.fromXml(inputStream, TestBean.class);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("time", "QCloudXml.fromXml use time="+(System.currentTimeMillis() - now));

        now = System.currentTimeMillis();
        for (int i=0; i<10000; ++i) {
            try {
                TestBean.parse(inputStream);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("time", "TestBean parse use time="+(System.currentTimeMillis() - now));


        now = System.currentTimeMillis();
        for (int i=0; i<10000; ++i) {
            try {
                QCloudXml.toXml(testBean);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("time", "QCloudXml.toXml use time="+(System.currentTimeMillis() - now));

        now = System.currentTimeMillis();
        for (int i=0; i<10000; ++i) {
            try {
                TestBean.build(testBean);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("time", "TestBean.build use time="+(System.currentTimeMillis() - now));
    }


}