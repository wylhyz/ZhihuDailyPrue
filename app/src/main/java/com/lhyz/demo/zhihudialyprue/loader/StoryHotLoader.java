package com.lhyz.demo.zhihudialyprue.loader;

import android.content.Context;

import com.lhyz.demo.zhihudialyprue.bean.StorySimple;

import java.util.List;

public class StoryHotLoader extends DataLoader<List<StorySimple>>{

    public StoryHotLoader(Context context) {
        super(context);
    }

    @Override
    protected List<StorySimple> loadData() {
        return null;
    }

    @Override
    public List<StorySimple> loadInBackground() {
        return loadData();
    }
}