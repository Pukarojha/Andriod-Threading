package com.example.threaddemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.threaddemo.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    // Define the handler here, outside of onCreate
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            Bundle bundle = msg.getData();
            String string = bundle.getString("myKey");
            binding.mytextView.setText(string);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void buttonClick(View view) {
        long endTime = System.currentTimeMillis() + 20 * 1000;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (System.currentTimeMillis() < endTime) {
                    synchronized (this) {
                        try {
                            wait(endTime - System.currentTimeMillis());
                        } catch (Exception ex) {
                            // Handle exception if needed
                        }
                    }

                }
                ;
                Message msg = handler.obtainMessage();
                Bundle bundle = new Bundle();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY.MM.dd G 'at' HH:mm: ss z");
                String currentDateandTime = simpleDateFormat.format(new Date());

                bundle.putString("myKey", currentDateandTime);
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
        };
        Thread myThread = new Thread(runnable);
        myThread.start();

    }

}
