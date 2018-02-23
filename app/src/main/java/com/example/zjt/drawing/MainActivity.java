package com.example.zjt.drawing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button button,next, previous;
    private MyDrawing card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        card = findViewById(R.id.card);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card.switchMode();
                if (button.getText().toString().equalsIgnoreCase("进入擦除"))
                    button.setText("进入画图");
                else
                    button.setText("进入擦除");
            }
        });

        next = findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card.next();
                Toast.makeText(MainActivity.this, "next", Toast.LENGTH_SHORT).show();
            }
        });

        previous = findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card.previous();
                Toast.makeText(MainActivity.this, "previous", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
