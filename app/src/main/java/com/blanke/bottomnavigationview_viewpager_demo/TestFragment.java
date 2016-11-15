package com.blanke.bottomnavigationview_viewpager_demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by blanke on 16-11-15.
 */

public class TestFragment extends Fragment {
    public static final String KEY_TITLE = "title";
    private TextView testText;
    private String title;

    public static TestFragment newInstance(String title) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        title = args.getString(KEY_TITLE, "test");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        testText = (TextView) view.findViewById(R.id.test_text);
        testText.setText(title);
    }
}
