package com.example.p3;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FactsFragment extends Fragment {
    TextView readFacts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_facts,container,false);
        readFacts = view.findViewById(R.id.FactsTxt);

        readFacts.setMovementMethod(new ScrollingMovementMethod());
        return view;
    }

}
