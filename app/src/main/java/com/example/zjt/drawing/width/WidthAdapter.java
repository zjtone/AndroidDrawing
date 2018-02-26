package com.example.zjt.drawing.width;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zjt.drawing.OnItemClickListener;
import com.example.zjt.drawing.R;

import java.util.List;

/**
 * Created by zjt on 18-2-26.
 */

public class WidthAdapter extends RecyclerView.Adapter implements View.OnClickListener {
    private Context mContext;
    private List<Integer> mWidthList;
    private OnItemClickListener onItemClickListener = null;

    public WidthAdapter(Context context, List<Integer> widthList) {
        this.mContext = context;
        this.mWidthList = widthList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.width_view, parent, false);
        view.setOnClickListener(this);
        return new WidthViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder != null && holder instanceof WidthViewHolder) {
            WidthViewHolder widthHolder = (WidthViewHolder) holder;
            widthHolder.widthView.setWidth(mWidthList.get(position));
            widthHolder.widthView.invalidate();
            widthHolder.view.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return mWidthList == null ? 0 : mWidthList.size();
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(view, (Integer) view.getTag());
    }

    private class WidthViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private WidthView widthView;

        public WidthViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            widthView = itemView.findViewById(R.id.widthView);
        }
    }
}
