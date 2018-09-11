package fr.hironic.moodtracker.tools;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DecimalFormat;

public class ChartValueFormatter implements ValueFormatter {

    private DecimalFormat mFormat;

    public ChartValueFormatter() {
        mFormat = new DecimalFormat("###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if(value > 0) {
            return mFormat.format(value) + " %";
        } else {
            return "";
        }
    }

}
