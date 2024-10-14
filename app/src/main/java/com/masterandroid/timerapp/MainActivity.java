package com.masterandroid.timerapp;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.masterandroid.timerapp.databinding.ActivityMainBinding;
import com.masterandroid.timerapp.fragment.AlarmFragment;
import com.masterandroid.timerapp.fragment.CountDownFragment;
import com.masterandroid.timerapp.fragment.CurrentTimeFragment;
import com.masterandroid.timerapp.fragment.StopwatchFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private static final int REQUEST_CODE_NOTIFICATION_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        checkNotificationPermission();
        setContentView(binding.getRoot());

        replaceFragment(new CurrentTimeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.alarm)
                replaceFragment(new AlarmFragment());
            else if(item.getItemId() == R.id.stopwatch)
                replaceFragment(new StopwatchFragment());
            else if (item.getItemId() == R.id.timer)
                replaceFragment(new CountDownFragment());
            else
                replaceFragment(new CurrentTimeFragment());

            return true;
        });
    }

    private void replaceFragment (Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();


    }

    private void checkNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_CODE_NOTIFICATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_NOTIFICATION_PERMISSION) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
