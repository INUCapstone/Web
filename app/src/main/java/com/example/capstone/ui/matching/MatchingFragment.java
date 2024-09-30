package com.example.capstone.ui.matching;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.capstone.R;
import com.example.capstone.api.RepositoryCallback;
import com.example.capstone.api.service.NaverService;
import com.example.capstone.common.ExceptionCode;
import com.example.capstone.databinding.FragmentMatchingBinding;
import com.example.capstone.dto.Matching.DetailInfo;
import com.example.capstone.dto.Matching.TaxiRoomRes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchingFragment extends Fragment {

    private FragmentMatchingBinding binding;
    private SocketService socketService;
    private List<TaxiRoomRes> roomList;
    private List<DetailInfo> locationInfoList;
    private RoomAdapter adapter;
    private LocationAdapter locationAdapter;
    private Button startMatchingButton, stopMatchingButton, findLocationButton;
    private EditText targetLocation;
    private String data;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMatchingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        roomList = new ArrayList<>();
        adapter = new RoomAdapter(roomList);
        locationInfoList = new ArrayList<>();
        locationAdapter = new LocationAdapter(locationInfoList);

        RecyclerView recyclerView = root.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        RecyclerView locationRecyclerView = root.findViewById(R.id.recycler_view_locate);
        locationRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        locationRecyclerView.setAdapter(locationAdapter);

        startMatchingButton = root.findViewById(R.id.startMatchingButton);
        stopMatchingButton = root.findViewById(R.id.stopMatchingButton);
        findLocationButton = root.findViewById(R.id.findLocationButton);

        socketService = new SocketService(roomList,adapter,getContext());

        // 매칭 시작 버튼 클릭 시
        startMatchingButton.setOnClickListener(v -> {
            socketService.startSocketConnection();
            startMatchingButton.setVisibility(View.GONE);
            stopMatchingButton.setVisibility(View.VISIBLE);
            findLocationButton.setVisibility(View.GONE);
        });

        // 매칭 종료 버튼 클릭 시
        stopMatchingButton.setOnClickListener(v -> {
            socketService.stopSocketConnection();
            startMatchingButton.setVisibility(View.VISIBLE);
            stopMatchingButton.setVisibility(View.GONE);
            findLocationButton.setVisibility(View.VISIBLE);
        });

        // 도착지 검색 버튼 클릭 시
        findLocationButton.setOnClickListener(v -> {
            targetLocation = root.findViewById(R.id.targetLocation);
            NaverService naverService = new NaverService(getActivity(), locationInfoList,locationAdapter);
            naverService.getLocation(String.valueOf(targetLocation.getText()), new RepositoryCallback() {
                @Override
                public void onSuccess(String message) {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(ExceptionCode exceptionCode, Map<String, String> errorMessages) {
                }
            });
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
