package com.sandbox.michel.rssreader;

import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.util.Xml;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class RssParserTask extends AsyncTask<String, Integer, RssListAdapter> {
    private RssReader mActivity;
    private RssListAdapter mAdapter;
    private ProgressDialog mProgressDialog;

    // コンストラクタ
    public RssParserTask(RssReader activity, RssListAdapter adapter) {
        mActivity = activity;
        mAdapter = adapter;
    }

    // タスクを実行した直後にコールされる
    @Override
    protected void onPreExecute() {
        // プログレスバーを表示する
        mProgressDialog = new ProgressDialog(mActivity);
        mProgressDialog.setMessage("Now Loading...");
        mProgressDialog.show();
    }

    // バックグラウンドにおける処理を担う。タスク実行時に渡された値を引数とする
    @Override
    protected RssListAdapter doInBackground(String... params) {
        RssListAdapter result = null;
        try {
            // HTTP経由でアクセスし、InputStreamを取得する
            URL url = new URL(params[0]);
            InputStream is = url.openConnection().getInputStream();
            result = parseXml(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ここで返した値は、onPostExecuteメソッドの引数として渡される
        return result;
    }

    // メインスレッド上で実行される
    @Override
    protected void onPostExecute(RssListAdapter result) {
        mProgressDialog.dismiss();
        mActivity.setListAdapter(result);
    }

    // XMLをパースする
    public RssListAdapter parseXml(InputStream is)
            throws IOException, XmlPullParserException {
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(is, null);
            int eventType = parser.getEventType();
            Item currentItem = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tag = null;
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = parser.getName();
                        if (tag.equals("item")) {
                            currentItem = new Item();
                        } else if (currentItem != null) {
                            if (tag.equals("title")) {
                                currentItem.setTitle(parser.nextText());
                            } else if (tag.equals("description")) {
                                currentItem.setDescription(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tag = parser.getName();
                        if (tag.equals("item")) {
                            mAdapter.add(currentItem);
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mAdapter;
    }
}