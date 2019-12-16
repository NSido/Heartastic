package com.example.p3;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
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
import com.anychart.graphics.vector.Stroke;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static android.content.ContentValues.TAG;

public class InsightsFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

    private TextView minHeartRateTextView;
    private TextView avgHeartRateTextView;
    private TextView maxHeartRateTextView;
    private TextView changeDateTextView;

    private ListView mListView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseRefRoot;
    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;
    LineChart lineChart;
    LineData lineData;
    LineDataSet lineDataSet;
    ArrayList lineEntries;
    ArrayList<Integer> hrData;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insights, container, false);
        //AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        avgHeartRateTextView = view.findViewById(R.id.textview_avg_hr);
        minHeartRateTextView = view.findViewById(R.id.textview_min_hr);
        maxHeartRateTextView = view.findViewById(R.id.textview_max_hr);
        mListView = view.findViewById(R.id.listview_hr);
        changeDateTextView = view.findViewById(R.id.change_date_tv);

        hrData = new ArrayList<>();




        changeDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showDatePickerDialog();

            }
        });

        lineChart = view.findViewById(R.id.lineChart);
        getEntries();
        lineDataSet = new LineDataSet(lineEntries, "");
        lineData = new LineData(lineDataSet);
        lineChart.setData(lineData);
        lineDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setValueTextSize(18f);

       /* Pie pie = AnyChart.pie();
        Cartesian cartesian = AnyChart.line();
        cartesian.animation(true);
        cartesian.padding(10d, 20d, 5d, 20d);
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);



        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);


        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Your HeartRate throughout the day");

        cartesian.yAxis(0).title("BPM");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);


        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Date");
        cartesian.yAxis(0).title("Heartrate");

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("January", 120));
        data.add(new ValueDataEntry("February", 90));
        data.add(new ValueDataEntry("March", 80));
        data.add(new ValueDataEntry("Test3", 110));
        data.add(new ValueDataEntry("Test4", 68));
        data.add(new ValueDataEntry("Test5", 76));
        data.add(new ValueDataEntry("Test6", 67));
        data.add(new ValueDataEntry("Test7", 213));
        data.add(new ValueDataEntry("Test8", 98));


        Column column = cartesian.column(data);

        pie.data(data);
        anyChartView.setChart(cartesian);*/

        return view;


    }

    private int calculateAverage(List<Integer> hrData) {
        Integer sum = 0;
        if (!hrData.isEmpty()) {
            for (Integer heartRate : hrData) {
                sum += heartRate;
            }
            return sum / hrData.size();
        }
        return sum;
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void getEntries() {
        lineEntries = new ArrayList<>();
       lineEntries.add(new Entry(0, 120));
        lineEntries.add(new Entry(1, 40));
        lineEntries.add(new Entry(2, 67));
        lineEntries.add(new Entry(3, 87));
        lineEntries.add(new Entry(4, 97));
        lineEntries.add(new Entry(5, 59));
    }



    private void getHRData(String date) {
        mAuth = FirebaseAuth.getInstance();


        mDatabaseRefRoot = FirebaseDatabase.getInstance().getReference();
        mDatabaseRef = mDatabaseRefRoot.child("UserHeartRateData").child(mAuth.getUid()).child(date);


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String hRate = String.valueOf(ds.getValue());
                    //String hRate = String.valueOf(ds.getValue());
                    //String hRate2 = hRate.replaceAll("[^0-9]","");
                   // String hRate3 = hRate2.replaceAll(" ", "");
                    hrData.add(Integer.valueOf(hRate));

                  /*  lineEntries = new ArrayList<>();
                    lineEntries.add(new Entry(Integer.valueOf(hRate),i++));*/



                }
                Log.d("TAG", hrData.toString());
                int avgHeartRate = calculateAverage(hrData);
                int maxHeartRate = Collections.max(hrData);
                int minHeartRate = Collections.min(hrData);
                avgHeartRateTextView.setText(String.valueOf(avgHeartRate) + " " + "BPM");
                minHeartRateTextView.setText(String.valueOf(minHeartRate) + " " + "BPM");
                maxHeartRateTextView.setText(String.valueOf(maxHeartRate) + " " + "BPM");

                ArrayAdapter<Integer> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1
                        , hrData);

                mListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabaseRef.addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month +=1;
        String date = year + "/" + month + "/" + dayOfMonth;
        changeDateTextView.setText(date);
        getHRData(date);



    }

}

