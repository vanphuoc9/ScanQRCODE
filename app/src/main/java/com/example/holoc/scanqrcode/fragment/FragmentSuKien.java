package com.example.holoc.scanqrcode.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.holoc.scanqrcode.R;

/**
 * Created by holoc on 6/24/2018.
 */

public class FragmentSuKien extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmen_sukien_user,container, false);
        return view;
    }
}
