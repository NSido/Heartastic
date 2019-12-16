package com.example.p3;

import android.app.DatePickerDialog;
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



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insights, container, false);
        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        avgHeartRateTextView = view.findViewById(R.id.textview_avg_hr);
        minHeartRateTextView = view.findViewById(R.id.textview_min_hr);
        maxHeartRateTextView = view.findViewById(R.id.textview_max_hr);
        mListView = view.findViewById(R.id.listview_hr);
        changeDateTextView = view.findViewById(R.id.change_date_tv);




        changeDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            showDatePickerDialog();

            }
        });

        Pie pie = AnyChart.pie();
        Cartesian cartesian = AnyChart.column();

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

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");
        cartesian.animation(true);
        cartesian.title("Heartrate overview");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Date");
        cartesian.yAxis(0).title("Heartrate");

        anyChartView.setChart(cartesian);

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

    private void getHRData(String date) {
        mAuth = FirebaseAuth.getInstance();


        mDatabaseRefRoot = FirebaseDatabase.getInstance().getReference();
        mDatabaseRef = mDatabaseRefRoot.child("UserHeartRateData").child(mAuth.getUid()).child(date);


        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Integer> hrData = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String hRate = String.valueOf(ds.getValue());
                    hrData.add(Integer.valueOf(hRate));
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
