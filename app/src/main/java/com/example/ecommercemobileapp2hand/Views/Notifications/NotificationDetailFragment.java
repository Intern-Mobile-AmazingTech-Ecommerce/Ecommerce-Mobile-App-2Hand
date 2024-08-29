package com.example.ecommercemobileapp2hand.Views.Notifications;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecommercemobileapp2hand.Controllers.NotificationsHandler;
import com.example.ecommercemobileapp2hand.Models.Notifications;
import com.example.ecommercemobileapp2hand.R;
import com.example.ecommercemobileapp2hand.Views.Adapters.NotificationsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationDetailFragment extends Fragment {
    private RecyclerView recyclerViewNotifications;
    private NotificationsAdapter adapter;
    private List<Notifications> notificationsList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationDetailFragment newInstance(String param1, String param2) {
        NotificationDetailFragment fragment = new NotificationDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_detail, container, false);

        recyclerViewNotifications = view.findViewById(R.id.recycler_view_notifications);
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize adapter with empty list initially
        adapter = new NotificationsAdapter(new ArrayList<>());
        recyclerViewNotifications.setAdapter(adapter);
        fetchNotifications();

        return view;
    }
    private void fetchNotifications() {
        new Thread(() -> {
            notificationsList = NotificationsHandler.getNotifications();
            getActivity().runOnUiThread(() -> {
                adapter = new NotificationsAdapter(notificationsList);
                recyclerViewNotifications.setAdapter(adapter);
            });
        }).start();
    }
}