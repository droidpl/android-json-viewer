package com.github.droidpl.android.jsonviewersample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.droidpl.android.jsonviewer.JSONViewerActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JSONObject json = null;
        try {
            json = new JSONObject("{'myProperty': 'myValue'}");
            JSONViewerActivity.startActivity(this, json);
        } catch (JSONException ignored) {
        }
        finish();
    }
}
