package com.example.blc;

////////////////////////////////////////////////////////////////////////////
// ColorPicker                                                            //
//                                                                        //
// This class runs the menu that allows the user to select one RGB color. //
// Once the user selects a RGB color it will display the selected color.  //
////////////////////////////////////////////////////////////////////////////

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

public class ColorPicker extends AppCompatActivity {
    ColorHelper CH = new ColorHelper();
    BluetoothManager BM = new BluetoothManager();

    Button colorDisplay;
    Button redButton;
    Button greenButton;
    Button blueButton;

    EditText redText;
    EditText greenText;
    EditText blueText;

    SeekBar redBar;
    SeekBar greenBar;
    SeekBar blueBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // When the screen is first opened it will assign all of the buttons and bars to their respective variable
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_picker);

        colorDisplay = findViewById(R.id.colorDisplay);

        redButton = findViewById(R.id.redButton);
        greenButton = findViewById(R.id.greenButton);
        blueButton = findViewById(R.id.blueButton);

        redText = findViewById(R.id.redText);
        greenText = findViewById(R.id.greenText);
        blueText = findViewById(R.id.blueText);

        redBar = findViewById(R.id.redBar);
        greenBar = findViewById(R.id.greenBar);
        blueBar = findViewById(R.id.blueBar);

        redText.setText(Integer.toString(CH.rgbArray[CH.mode][CH.meta][0]));
        greenText.setText(Integer.toString(CH.rgbArray[CH.mode][CH.meta][1]));
        blueText.setText(Integer.toString(CH.rgbArray[CH.mode][CH.meta][2]));
        redBar.setProgress(CH.rgbArray[CH.mode][CH.meta][0]);
        greenBar.setProgress(CH.rgbArray[CH.mode][CH.meta][1]);
        blueBar.setProgress(CH.rgbArray[CH.mode][CH.meta][2]);
        updateColorDisplay();

        colorDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Here for the red, green, and blue buttons it will read the text box and change the color accordingly as well as check the max and min values.
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp;
                if (redText.getText().toString().length() > 3) {
                    redText.setText("255");
                    temp = 255;
                } else {
                    temp = Integer.parseInt(redText.getText().toString());
                }
                if (temp > 255){
                    temp = 255;
                    redText.setText("255");
                }
                else if (temp < 0) {
                    temp = 0;
                    redText.setText("0");
                }
                CH.rgbArray[CH.mode][CH.meta][0] = temp;
                redBar.setProgress(temp);
                updateColorDisplay();
                updateLED();
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });
        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp;
                if (greenText.getText().toString().length() > 3) {
                    greenText.setText("255");
                    temp = 255;
                } else {
                    temp = Integer.parseInt(greenText.getText().toString());
                }
                if (temp > 255){
                    temp = 255;
                    greenText.setText("255");
                }
                else if (temp < 0) {
                    temp = 0;
                    greenText.setText("0");
                }
                CH.rgbArray[CH.mode][CH.meta][1] = temp;
                greenBar.setProgress(temp);
                updateColorDisplay();
                updateLED();
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });
        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int temp;
                if (blueText.getText().toString().length() > 3) {
                    blueText.setText("255");
                    temp = 255;
                } else {
                    temp = Integer.parseInt(blueText.getText().toString());
                }
                if (temp > 255){
                    temp = 255;
                    blueText.setText("255");
                }
                else if (temp < 0) {
                    temp = 0;
                    blueText.setText("0");
                }
                CH.rgbArray[CH.mode][CH.meta][2] = temp;
                blueBar.setProgress(temp);
                updateColorDisplay();
                updateLED();
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {

                }
            }
        });

        // These functions will run when the bar is moved to change the color and will change the color of the leds once the user releases the bar.
        redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                CH.rgbArray[CH.mode][CH.meta][0] = redBar.getProgress();
                redText.setText(Integer.toString(redBar.getProgress()));
                updateColorDisplay();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateLED();
            }
        });
        greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                CH.rgbArray[CH.mode][CH.meta][1] = greenBar.getProgress();
                greenText.setText(Integer.toString(greenBar.getProgress()));
                updateColorDisplay();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateLED();
            }
        });
        blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                CH.rgbArray[CH.mode][CH.meta][2] = blueBar.getProgress();
                blueText.setText(Integer.toString(blueBar.getProgress()));
                updateColorDisplay();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updateLED();
            }
        });
    }

    // Change the color that is shown in a box to the user
    public void updateColorDisplay() {
        colorDisplay.setBackgroundColor(android.graphics.Color.argb(255, CH.rgbArray[CH.mode][CH.meta][0], CH.rgbArray[CH.mode][CH.meta][1], CH.rgbArray[CH.mode][CH.meta][2]));
    }

    // Update the LED strip to have the proper colors
    public void updateLED() {
        BM.write("1," + CH.rgbArray[CH.mode][CH.meta][0] + "," + CH.rgbArray[CH.mode][CH.meta][1] + "," + CH.rgbArray[CH.mode][CH.meta][2] + ";");
    }
}
