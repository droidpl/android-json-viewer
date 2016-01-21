package com.github.droidpl.android.jsonviewer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Activity that can be requested to display some json content.
 */
public class JSONViewerActivity extends AppCompatActivity implements RVJsonAdapter.JsonListener {

    /**
     * The json key to pass the state.
     */
    private static final String JSON_OBJECT_STATE = "bundle_json_object";
    /**
     * The json array key to send the state.
     */
    private static final String JSON_ARRAY_STATE = "bundle_json_array";

    /**
     * The list to display the keys and content.
     */
    private RecyclerView mRecyclerView;

    /**
     * Starts the activity with a json object.
     *
     * @param context    The context to start the activity.
     * @param jsonObject The json object.
     */
    public static void startActivity(@NonNull Context context, @Nullable JSONObject jsonObject) {
        Intent intent = new Intent(context, JSONViewerActivity.class);
        Bundle bundle = new Bundle();
        if (jsonObject != null) {
            bundle.putString(JSON_OBJECT_STATE, jsonObject.toString());
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    /**
     * Starts an activity with a json array.
     *
     * @param context   The context to start the activity.
     * @param jsonArray The json array.
     */
    public static void startActivity(@NonNull Context context, @Nullable JSONArray jsonArray) {
        Intent intent = new Intent(context, JSONViewerActivity.class);
        Bundle bundle = new Bundle();
        if (jsonArray != null) {
            bundle.putString(JSON_ARRAY_STATE, jsonArray.toString());
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_viewer_activity);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_json);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Bundle intentExtras = getIntent().getExtras();
        if (intentExtras == null) {
            finish();
        } else {
            String jsonObjectString = intentExtras.getString(JSON_OBJECT_STATE);
            String jsonArrayString = intentExtras.getString(JSON_ARRAY_STATE);
            try {
                RVJsonAdapter adapter;
                if (jsonObjectString != null) {
                    adapter = new RVJsonAdapter(this, new JSONObject(jsonObjectString), this);
                } else if (jsonArrayString != null) {
                    adapter = new RVJsonAdapter(this, new JSONArray(jsonArrayString), this);
                } else {
                    adapter = new RVJsonAdapter(this);
                }
                mRecyclerView.setAdapter(adapter);
            } catch (JSONException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    @Override
    public void openJsonObject(@NonNull JSONObject object) {
        JSONViewerActivity.startActivity(this, object);
    }

    @Override
    public void openJsonArray(@NonNull JSONArray array) {
        JSONViewerActivity.startActivity(this, array);
    }
}
