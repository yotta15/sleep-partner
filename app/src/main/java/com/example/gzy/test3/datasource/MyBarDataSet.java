package com.example.gzy.test3.datasource;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

/**
 * created by gzy on 2019/4/2.
 * Describle;
 */
public class MyBarDataSet extends BarDataSet {

    public MyBarDataSet(List<BarEntry> yVals, String label) {
        super(yVals, label);
    }

    @Override
    public int getColor(int index) {
       ;


        //根据getEntryForXIndex(index).getVal()获得Y值,然后去对比,判断
        //我这1000 4000是根据自己的需求写的,可以随便设,判断条件if根据自己需求
        if( getEntryForIndex(index).getY() < 10)
            return mColors.get(0);
        else if( getEntryForIndex(index).getY()< 20)
            return mColors.get(1);
        else // greater or equal than 100 red
            return mColors.get(2);
    }
}

