package com.example.p3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.lineargauge.pointers.Bar;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;

import java.util.ArrayList;
import java.util.List;

public class InsightsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insights,container,false);
       Pie pie = AnyChart.pie();
        Cartesian cartesian = AnyChart.column();
        List<DataEntry> data = new ArrayList<>();
        /*data.add(new ValueDataEntry("John", 10000));
        data.add(new ValueDataEntry("Jake", 12000));
        data.add(new ValueDataEntry("Peter", 18000));*/

        data.add(new ValueDataEntry("Test", 80540));
        data.add(new ValueDataEntry("Test1", 94190));
        data.add(new ValueDataEntry("Test2", 102610));
        data.add(new ValueDataEntry("Test3", 110430));
        data.add(new ValueDataEntry("Test4", 128000));
        data.add(new ValueDataEntry("Test5", 143760));
        data.add(new ValueDataEntry("Test6", 170670));
        data.add(new ValueDataEntry("Test7", 213210));
        data.add(new ValueDataEntry("Test8", 249980));

        Column column = cartesian.column(data);

        pie.data(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");
        cartesian.animation(true);
        cartesian.title("HeartRate overview");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Date");
        cartesian.yAxis(0).title("HeartRate");

        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        //anyChartView.setChart(pie);
        anyChartView.setChart(cartesian);

        return view;


    }
}
