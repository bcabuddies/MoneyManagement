package com.bcabuddies.moneymanagement.Accounts.CashFrag;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.moneymanagement.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CashFragment extends Fragment {

    @BindView(R.id.cash_fragment_recyclerView)
    RecyclerView cashFragmentRecyclerView;
    private View view;

    public CashFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cash, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

}
