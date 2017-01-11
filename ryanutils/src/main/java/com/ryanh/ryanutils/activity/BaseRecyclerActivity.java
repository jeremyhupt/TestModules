package com.ryanh.ryanutils.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ryanh.ryanutils.ItemDecoration.SimpleItemDecoration;
import com.ryanh.ryanutils.R;


/**
 * Author:胡仲俊
 * Date: 2017 - 01 - 03
 * Des:
 * FIXME
 * Todo
 */

public abstract class BaseRecyclerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private boolean isLinear = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        BaseAdapter baseAdapter = new BaseAdapter(this, setDatas());

        SimpleItemDecoration simpleItemDecoration = new SimpleItemDecoration();
        simpleItemDecoration.initDecorationType(this, simpleItemDecoration.DECORATION_EXPANDABLE, new SimpleItemDecoration.DecorationCallback() {
            @Override
            public long getGroupId(int position) {
                return Character.toUpperCase(setDatas()[position].charAt(0));
            }

            @Override
            public String getGroupFirstLine(int position) {
                return setDatas()[position].substring(0, 1).toUpperCase();
            }
        });

        recyclerView.addItemDecoration(simpleItemDecoration);
        recyclerView.setAdapter(baseAdapter);

        setContentView(recyclerView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.base_change) {
            if (isLinear) {
                recyclerView.setLayoutManager(new GridLayoutManager(BaseRecyclerActivity.this, 3));
                isLinear = false;
            } else {
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                isLinear = true;
            }
        }
        return true;
    }

    protected abstract String[] setDatas();

    protected abstract View.OnClickListener setOnItemClickListener(int position);

    protected class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.ItemViewHolder> {

        private String[] mStr;
        private Context mContext;

        public BaseAdapter(Context context, String[] str) {
            mContext = context;
            this.mStr = str;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_base_text, null);
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemViewHolder holder, final int position) {
            TextView textView = (TextView) holder.itemView.findViewById(R.id.base_text);
            textView.setText(mStr[position]);
            holder.itemView.setOnClickListener(BaseRecyclerActivity.this.setOnItemClickListener(position));
        }

        @Override
        public int getItemCount() {
            return mStr.length;
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {

            public ItemViewHolder(View itemView) {
                super(itemView);

            }
        }
    }
}
