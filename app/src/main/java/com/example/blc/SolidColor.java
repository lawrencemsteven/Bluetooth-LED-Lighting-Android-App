package com.example.blc;

///////////////////////////////////////////////////////////////////////////
// SolidColor                                                            //
//                                                                       //
// This class controls the screen which lets the user pick a solid color //
///////////////////////////////////////////////////////////////////////////

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SolidColor extends AppCompatActivity {
    ColorHelper CH = new ColorHelper();

    private Button solidButton;
    private Button CPButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize buttons and set button actions
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solid_color);

        solidButton = findViewById(R.id.solidButton);
        CPButton = findViewById(R.id.CPButton);

        solidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        CPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

        setCPButtonBackground();
    }

    @Override
    public void onResume(){
        // When you return to this screen
        super.onResume();
        setCPButtonBackground();
    }

    // Opens the screen to pick a color
    public void openColorPicker() {
        Intent intent = new Intent(this, ColorPicker.class);
        startActivity(intent);
    }

    // Used to show thhe user what color they have selected
    public void setCPButtonBackground() {
        CPButton.setBackgroundColor(android.graphics.Color.argb(255, CH.rgbArray[0][0][0], CH.rgbArray[0][0][1], CH.rgbArray[0][0][2]));
    }
}
