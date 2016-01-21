package com.github.droidpl.android.jsonviewer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Adapter that is able to visualise the json object or json array in a recycler view.
 */
public class RVJsonAdapter extends RecyclerView.Adapter<RVJsonAdapter.ViewHolder> {

    /**
     * The context for the adapter inflating.
     */
    private Context mContext;
    /**
     * Map with the properties of the contained json object. Null if it is an array.
     */
    private HashMap<String, Object> mMap;
    /**
     * The json array to display. Null if there is a map.
     */
    private JSONArray mJsonArray;
    /**
     * The listener to send a clicked object.
     */
    private JsonListener mListener;

    /**
     * Listener to return the object clicked if it is a complex one.
     */
    public interface JsonListener {
        /**
         * Provides the clicked json object.
         *
         * @param object The object.
         */
        void openJsonObject(@NonNull JSONObject object);

        /**
         * Provides the clicked array item.
         *
         * @param array The array to open.
         */
        void openJsonArray(@NonNull JSONArray array);
    }

    /**
     * Constructor to create the recycler adapter with an object.
     *
     * @param context  The context.
     * @param json     The json object to display.
     * @param listener The listener.
     */
    public RVJsonAdapter(@NonNull Context context, @Nullable JSONObject json, @Nullable JsonListener listener) {
        mContext = context;
        if (json != null) {
            mMap = new LinkedHashMap<>(json.length());
            Iterator<String> keys = json.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                mMap.put(key, json.opt(key));
            }
        }
        mListener = listener;
    }

    /**
     * Constructor to create the recycler adapter with an array.
     *
     * @param context  The context.
     * @param array    The json array.
     * @param listener The listener.
     */
    public RVJsonAdapter(@NonNull Context context, @Nullable JSONArray array, @Nullable JsonListener listener) {
        mContext = context;
        mJsonArray = array;
        mListener = listener;
    }

    /**
     * Without any object.
     *
     * @param context opens it with any object.
     */
    public RVJsonAdapter(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public RVJsonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.json_item_adapter, parent, false));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    Object obj = getItem(viewHolder.getAdapterPosition());
                    if (obj != null) {
                        if (obj instanceof JSONObject) {
                            mListener.openJsonObject((JSONObject) obj);
                        } else if (obj instanceof JSONArray) {
                            mListener.openJsonArray((JSONArray) obj);
                        }
                    }
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RVJsonAdapter.ViewHolder holder, int position) {
        Object obj = getItem(position);
        String key = getItemKey(position);
        if (obj != null) {
            if (obj instanceof JSONObject) {
                holder.setObject(key, false);
            } else if (obj instanceof JSONArray) {
                holder.setObject(key, true);
            } else {
                holder.setText(key, obj.toString());
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mMap != null) {
            return mMap.size();
        } else if (mJsonArray != null) {
            return mJsonArray.length();
        }
        return 0;
    }

    /**
     * Provides the item key name. Number for array, property for object.
     *
     * @param position The position.
     * @return The key as string or null.
     */
    @Nullable
    private String getItemKey(int position) {
        if (mMap != null) {
            return new ArrayList<>(mMap.entrySet()).get(position).getKey();
        } else if (mJsonArray != null) {
            return String.valueOf(position);
        }
        return null;
    }

    /**
     * Provides the item in a given position.
     *
     * @param position The position.
     * @return The item in this position or null if no item.
     */
    @Nullable
    private Object getItem(int position) {
        if (mMap != null) {
            return new ArrayList<>(mMap.entrySet()).get(position).getValue();
        } else if (mJsonArray != null) {
            return mJsonArray.opt(position);
        }
        return null;
    }

    /**
     * The view holder for the adapter.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /**
         * A text view for the property.
         */
        private TextView mProperty;
        /**
         * A text view for the value.
         */
        private TextView mValue;
        /**
         * Image displayed in case of complex object.
         */
        private ImageView mImage;

        /**
         * Constructor for the view holder.
         *
         * @param itemView The view.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mProperty = (TextView) itemView.findViewById(R.id.tv_property);
            mValue = (TextView) itemView.findViewById(R.id.tv_value);
            mImage = (ImageView) itemView.findViewById(R.id.iv_next);
        }

        /**
         * Sets the text in the property.
         *
         * @param property The property.
         * @param text     The text.
         */
        public void setText(@Nullable String property, @Nullable String text) {
            mImage.setVisibility(View.GONE);
            mProperty.setText(property);
            mValue.setText(text);
        }

        /**
         * Sets an object in the adapter.
         *
         * @param property The property to display.
         * @param isArray  True if array, false otherwise.
         */
        public void setObject(@Nullable String property, boolean isArray) {
            mImage.setVisibility(View.VISIBLE);
            mProperty.setText(property);
            if (isArray) {
                mValue.setText("[...]");
            } else {
                mValue.setText("{...}");
            }
        }
    }
}
