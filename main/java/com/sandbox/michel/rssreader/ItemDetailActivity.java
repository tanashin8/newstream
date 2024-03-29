package com.sandbox.michel.rssreader;

import android.os.Bundle;
import android.app.Activity;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.content.Intent;

public class ItemDetailActivity extends Activity {
    private TextView mTitle;
    private TextView mDescr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Intent intent = getIntent();

        String title = intent.getStringExtra("TITLE");
        mTitle = (TextView) findViewById(R.id.item_title);
        mTitle.setText(title);
        String descr = intent.getStringExtra("DESCRIPTION");
        mDescr = (TextView) findViewById(R.id.item_descr);
        mDescr.setText(descr);
    }
}