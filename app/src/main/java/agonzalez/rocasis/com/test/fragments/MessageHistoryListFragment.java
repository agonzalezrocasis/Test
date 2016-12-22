package agonzalez.rocasis.com.test.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import agonzalez.rocasis.com.test.R;
import agonzalez.rocasis.com.test.adapters.MessageAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageHistoryListFragment extends Fragment implements MessageHistoryListFragmentListener {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private MessageAdapter adapter;

    public MessageHistoryListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_history_list, container, false);
        ButterKnife.bind(this, view);
        initAdapter();
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    private void initAdapter() {
        if (adapter == null) {
            adapter = new MessageAdapter(getActivity().getApplicationContext(), new ArrayList<String>());
        }
    }

    @Override
    public void addToList(String record) {
        adapter.add(record);
    }

    @Override
    public void clearList() {
        adapter.clear();
    }
}
