package com.catcsyun.liantadog.adapter.dj.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class GridRec extends RecyclerView.ItemDecoration {
    private int space;

    public GridRec(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;

        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = 0;
        } else {
            outRect.top = 0;
        }
    }
}