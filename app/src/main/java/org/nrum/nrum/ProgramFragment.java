package org.nrum.nrum;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ProgramFragment extends Fragment {
    public ProgramFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_program, container, false);
        String[] progItems= {"Program1","Program2","Program3","Program4"};
        ListView mainProgListView = (ListView) view.findViewById(R.id.mainProgList);
        ArrayAdapter<String> mainProgListViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                progItems
        );
        mainProgListView.setAdapter(mainProgListViewAdapter);
        return view;
    }
}
