package com.example.blc;

///////////////////////////////////////////////////////////////////////////
// Gradient                                                              //
//                                                                       //
// This class allows the user to pick a gradient color for the LED strip //
///////////////////////////////////////////////////////////////////////////

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Gradient extends AppCompatActivity {
    ColorHelper CH = new ColorHelper();
    BluetoothManager BM = new BluetoothManager();

    Button gradientButton;
    Button CPButton1;
    Button CPButton2;
    Button CPButton3;
    Button CPButton4;
    Button grad1Button;
    Button grad2Button;
    Button grad3Button;
    Button grad4Button;
    Button gradDelayButton;

    EditText gradDelayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initialize buttons and set their click actions
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gradient);

        gradientButton = findViewById(R.id.gradButton);
        CPButton1 = findViewById(R.id.CPButton1);
        CPButton2 = findViewById(R.id.CPButton2);
        CPButton3 = findViewById(R.id.CPButton3);
        CPButton4 = findViewById(R.id.CPButton4);
        grad1Button = findViewById(R.id.grad1Button);
        grad2Button = findViewById(R.id.grad2Button);
        grad3Button = findViewById(R.id.grad3Button);
        grad4Button = findViewById(R.id.grad4Button);
        gradDelayButton = findViewById(R.id.gradDelayButton);

        gradDelayText = findViewById(R.id.gradDelayText);

        sendGradient();
        updateAllCPButtonBackground();

        // Once the user is done selecting colors
        gradientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Changes one of the gradient colors
        CPButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker(0);
            }
        });
        CPButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker(1);
            }
        });
        CPButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker(2);
            }
        });
        CPButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker(3);
            }
        });

        // Changes the number of colors in the gradient
        grad1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CH.gradNum = 1;
                sendGradient();
            }
        });
        grad2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CH.gradNum = 2;
                sendGradient();
            }
        });
        grad3Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CH.gradNum = 3;
                sendGradient();
            }
        });
        grad4Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CH.gradNum = 4;
                sendGradient();
            }
        });
        gradDelayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CH.gradDelay = Integer.parseInt(gradDelayText.getText().toString());
            }
        });
    }

    // After a color was changed it will update the strip to have the proper gradient
    @Override
    public void onResume(){
        super.onResume();
        sendGradient();
        updateAllCPButtonBackground();
    }

    // Used to change one of the gradient colors
    public void openColorPicker(int i) {
        CH.meta = i;
        Intent intent = new Intent(this, ColorPicker.class);
        startActivity(intent);
    }

    // Will update the color boxes that are displayed to the user to be the correct colors.
    private void updateAllCPButtonBackground() {
        CPButton1.setBackgroundColor(android.graphics.Color.argb(255, CH.rgbArray[1][0][0], CH.rgbArray[1][0][1], CH.rgbArray[1][0][2]));
        CPButton2.setBackgroundColor(android.graphics.Color.argb(255, CH.rgbArray[1][1][0], CH.rgbArray[1][1][1], CH.rgbArray[1][1][2]));
        CPButton3.setBackgroundColor(android.graphics.Color.argb(255, CH.rgbArray[1][2][0], CH.rgbArray[1][2][1], CH.rgbArray[1][2][2]));
        CPButton4.setBackgroundColor(android.graphics.Color.argb(255, CH.rgbArray[1][3][0], CH.rgbArray[1][3][1], CH.rgbArray[1][3][2]));
    }

    // Constructs and sends a message containing the entire gradient to the arduino.
    private void sendGradient() {
        String temp = "";
        if (CH.gradNum > 0)
            temp += CH.gradNum + "," + CH.gradDelay + "," + CH.rgbArray[1][0][0] + "," + CH.rgbArray[1][0][1] + "," + CH.rgbArray[1][0][2];
        if (CH.gradNum > 1)
            temp += "," + CH.rgbArray[1][1][0] + "," + CH.rgbArray[1][1][1] + "," + CH.rgbArray[1][1][2];
        if (CH.gradNum > 2)
            temp += "," + CH.rgbArray[1][2][0] + "," + CH.rgbArray[1][2][1] + "," + CH.rgbArray[1][2][2];
        if (CH.gradNum > 3)
            temp += "," + CH.rgbArray[1][3][0] + "," + CH.rgbArray[1][3][1] + "," + CH.rgbArray[1][3][2];
        temp += ";";
        BM.write(temp);
    }
}