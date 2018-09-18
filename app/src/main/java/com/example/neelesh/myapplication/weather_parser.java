package com.example.neelesh.myapplication;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Neelesh on 10/21/2017.
 */

public class weather_parser {
    private static final String ns = null;

    public String[] parse(InputStream in,String zipcodelist) throws XmlPullParserException, IOException {
        String[] result = zipcodelist.split("\\+");
        String[] temp = new String[result.length];
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            String name = parser.getName();
            Log.i("tag",name);
            XmlPullParser  parser1 = readparser(parser,"dwml","data");
            XmlPullParser  parser2 = readparser(parser1,"data","parameters");
            XmlPullParser parser3 =null;
            XmlPullParser parser4 =null;
            for (int x=0; x<result.length; x++) {
                  parser3 = readparser(parser2, "parameters", "temperature");
                  parser4 = readparser(parser3, "temperature", "value");
                name = parser4.getName();
                if (parser4.getName().equals("value")) {
                    temp[x] = readtext(parser4);
                    Log.i("temp",parser3.getName());

                }
                 while(!parser3.getName().equals("parameters"))
                {
                    parser3.nextTag();
                }
                parser3.nextTag();
                //parser2.nextTag();
                parser2 = parser3;
                Log.i("check",parser3.getName());
            }
        } finally {
            in.close();
        }
        return temp;
    }
    private String readtext(XmlPullParser parser)throws XmlPullParserException, IOException
    {
        String temp = null;
        if(parser.next() == XmlPullParser.TEXT) {
            temp = parser.getText();
            parser.nextTag();
        }
        Log.i("gotit",temp);

        return temp;
    }

    private XmlPullParser readparser(XmlPullParser parser,String start,String required) throws XmlPullParserException, IOException{
        Log.i("XmlPullparser",parser.getName());
        Log.i("XmlPullparser",start);
        parser.require(XmlPullParser.START_TAG, ns, start);
        Log.i("XmlPullparser",start);
        Log.i("XmlPullparser",required);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String x = parser.getName();
            if (x.equals(required)) {
                return parser;
            } else {
                skip(parser);
            }
        }
        return parser;
    }
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
