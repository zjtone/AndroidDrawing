package com.example.zjt.drawing;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zjt.drawing.width.WidthAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private MyDrawing card;
    private RecyclerView mWidthRecycler;

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
        initWidthRecycler();
    }

    private void initWidthRecycler() {
        mWidthRecycler = findViewById(R.id.widthRecyclerView);
        final List<Integer> widthList = new ArrayList<>();
        initWidthData(widthList);
        WidthAdapter widthAdapter = new WidthAdapter(this,widthList);
        widthAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                card.setWidth(widthList.get(position));
                Toast.makeText(MainActivity.this, "" + widthList.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mWidthRecycler.setLayoutManager(layoutManager);
        mWidthRecycler.setAdapter(widthAdapter);
    }

    private void initWidthData(List<Integer> widthList) {
        for(int i = 0 ; i < 5 ; i++){
            widthList.add(10 + i * 5);
        }
    }
}
