package com.tencent.qcloud.qcloudxml;

import android.util.Xml;

import com.tencent.qcloud.qcloudxml.annoation.XmlBean;
import com.tencent.qcloud.qcloudxml.annoation.XmlElement;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@XmlBean(name = "TestBeana")
public class TestBean {
    @XmlElement(name = "Aab")
    public String aaa;
    public int bbb;

//    public ITestBean2 iTestBean2;
    public ITestBean1 iTestBean1;

    public List<ITestBean2> iTestBean2List;

    @XmlBean
    public static class ITestBean1{
        public String iaaa;
    }

    @XmlBean
    public static class ITestBean2{
        public int ibbb;
    }

    public static void parse(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setInput(inputStream, "UTF-8");
        int eventType = xmlPullParser.getEventType();
        String tagName;
        TestBean result = new TestBean();
        result.iTestBean2List = new ArrayList<>();
        ITestBean1 itestBean11 = null;
        ITestBean2 itestBean2 = null;
        while (eventType != XmlPullParser.END_DOCUMENT){
            switch (eventType){
                case XmlPullParser.START_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("iTestBean2List")){
                        itestBean2 = new ITestBean2();
                    }else if(tagName.equalsIgnoreCase("ibbb")){
                        xmlPullParser.next();
                        itestBean2.ibbb = Integer.parseInt(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("Aab")){
                        xmlPullParser.next();
                        result.aaa = xmlPullParser.getText();
                    }else if(tagName.equalsIgnoreCase("bbb")){
                        xmlPullParser.next();
                        result.bbb = Integer.parseInt(xmlPullParser.getText());
                    }else if(tagName.equalsIgnoreCase("iTestBean1")){
                        xmlPullParser.next();
                        itestBean11 = new ITestBean1();
                    } else if(tagName.equalsIgnoreCase("iaaa")){
                        xmlPullParser.next();
                        itestBean11.iaaa = xmlPullParser.getText();
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = xmlPullParser.getName();
                    if(tagName.equalsIgnoreCase("iTestBean2List")){
                        result.iTestBean2List.add(itestBean2);
                        itestBean2 = null;
                    } else if(tagName.equalsIgnoreCase("iTestBean1")){
                        result.iTestBean1 = itestBean11;
                        itestBean2 = null;
                    }
                    break;
            }
            eventType = xmlPullParser.next();
        }

    }

    public static String build(TestBean testBean) throws XmlPullParserException, IOException {
        if(testBean == null)return null;
        StringWriter xmlContent = new StringWriter();
        XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
        XmlSerializer xmlSerializer = xmlPullParserFactory.newSerializer();
        xmlSerializer.setOutput(xmlContent);
        xmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);

        xmlSerializer.startDocument("UTF-8", null);
        xmlSerializer.startTag("", "TestBeana");

        if(testBean.aaa != null){
            addElement(xmlSerializer, "Aab", testBean.aaa);
        }
        addElement(xmlSerializer, "bbb", String.valueOf(testBean.bbb));

        if(testBean.iTestBean1 != null){
            xmlSerializer.startTag("", "ITestBean1");
            if(testBean.iTestBean1.iaaa != null)addElement(xmlSerializer, "iaaa", testBean.iTestBean1.iaaa);
            xmlSerializer.endTag("", "ITestBean1");
        }

        if(testBean.iTestBean2List != null && testBean.iTestBean2List.size() > 0){
            xmlSerializer.startTag("", "ITestBean2List");
            for(ITestBean2 iTestBean2 : testBean.iTestBean2List){
                xmlSerializer.startTag("", "ITestBean2");
                    if(iTestBean2.ibbb != -1) addElement(xmlSerializer, "ibbb", String.valueOf(iTestBean2.ibbb));
                xmlSerializer.endTag("", "ITestBean2");
            }
            xmlSerializer.endTag("", "ITestBean2List");
        }

        xmlSerializer.endTag("", "TestBeana");
        xmlSerializer.endDocument();
        return removeXMLHeader(xmlContent.toString());
    }

    /**
     * 增加XML节点
     */
    private static void addElement(XmlSerializer xmlSerializer, String tag, String value) throws IOException {
        if(value != null){
            xmlSerializer.startTag("", tag);
            xmlSerializer.text(value);
            xmlSerializer.endTag("", tag);
        }
    }

    /**
     * 删除XML头
     */
    private static String removeXMLHeader(String xmlContent){
        if(xmlContent != null){
            if(xmlContent.startsWith("<?xml")){
                int index = xmlContent.indexOf("?>");
                xmlContent = xmlContent.substring(index + 2);
            }
        }
        return xmlContent;
    }
}
