package com.example.capstone.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.capstone.R;
import com.example.capstone.api.RepositoryCallback;
import com.example.capstone.api.service.MemberService;
import com.example.capstone.common.ExceptionCode;
import com.example.capstone.databinding.ActivityMainBinding;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private boolean hasRequestedMy = false;
    private boolean hasRequestedDriver = false;
    private boolean hasRequestedCharge = false;
    private boolean hasRequestedMatching = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
        // 기본 제목 제거
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination,
                                             @Nullable Bundle arguments) {
                handleNavigationChange(destination.getId());
            }
        });

    }

    private void handleNavigationChange(int destinationId) {

        if (destinationId == R.id.navigation_my && !hasRequestedMy) {
            sendMyPage();
        } else if (destinationId == R.id.navigation_driver && !hasRequestedDriver) {
            sendDriverPage();
        } else if (destinationId == R.id.navigation_matching && !hasRequestedMatching) {
            sendMatching();
        } else if (destinationId == R.id.navigation_charge && !hasRequestedCharge) {
            sendChargePage();
        }
    }

    private void sendDriverPage() {
        hasRequestedDriver = true; // 상태 유지
        hasRequestedMatching = false; // 상태 유지
        hasRequestedMy = false;
        hasRequestedCharge = false;

    }

    private void sendChargePage(){
        hasRequestedCharge = true;
        hasRequestedDriver = false;
        hasRequestedMatching = false;
        hasRequestedMy = false;

        MemberService memberService = new MemberService(MainActivity.this);
        memberService.getUserInfo(new RepositoryCallback(){

            @Override
            public void onSuccess(String message) {

                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(ExceptionCode exceptionCode, Map<String, String> errorMessages) {
                Toast.makeText(MainActivity.this, exceptionCode.getExceptionMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMyPage(){
        hasRequestedCharge = false;
        hasRequestedDriver = false;
        hasRequestedMatching = false;
        hasRequestedMy = true;

        MemberService memberService = new MemberService(MainActivity.this);
        memberService.getUserInfo(new RepositoryCallback(){

            @Override
            public void onSuccess(String message) {

                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(ExceptionCode exceptionCode, Map<String, String> errorMessages) {
                Toast.makeText(MainActivity.this, exceptionCode.getExceptionMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMatching(){
        hasRequestedDriver = false; // 상태 유지
        hasRequestedMatching = true; // 상태 유지
        hasRequestedMy = false;
        hasRequestedCharge = false;
    }

}