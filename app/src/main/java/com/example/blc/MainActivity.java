package com.example.blc;

///////////////////////////////////////////////////////////////////////////
// MainActivity                                                          //
//                                                                       //
// This class contains the opening screen where the user will connect to //
// the Arduino as well as select what mode they want the LEDs to be in.  //
///////////////////////////////////////////////////////////////////////////

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ColorHelper CH = new ColorHelper();
    BluetoothManager BM = new BluetoothManager();

    private Button solidColorButton;
    private Button gradientColorButton;
    private Button bluetoothButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // When created, identify all the buttons and set them to variables as well as set their click actions
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solidColorButton = findViewById(R.id.solidColorButton);
        gradientColorButton = findViewById(R.id.gradientColorButton);
        bluetoothButton = findViewById(R.id.bluetoothButton);

        bluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openBluetoothScreen();
            }
        });
        solidColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSolidColor();
            }
        });
        gradientColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGradient();
            }
        });
    }

    public void openBluetoothScreen() {
        if (BM.getBluetoothAdapter() != null) // Bluetooth is supported on this device
        {
            // Enable bluetooth if available
            if (!BM.getBluetoothAdapter().isEnabled()) {
                int REQUEST_ENABLE_BT = 0;
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            Set<BluetoothDevice> pairedDevices = BM.getBluetoothAdapter().getBondedDevices();
            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                    String temp = device.getName();
                    if (temp.equals("HC-05")) {
                        BM.bluetoothName = temp;
                        BM.bluetoothAddress = device.getAddress();
                        BM.bluetoothDevice = device;
                    }
                }
            }
            if (BM.bluetoothName != "") {
                BM.startThreads();
                Toast.makeText(getApplicationContext(),"Connected to: " + BM.bluetoothName,Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(),"Not Connected",Toast.LENGTH_SHORT).show();
            }
        }
    }

    // When the solid color button is pressed
    public void openSolidColor() {
        CH.mode = 0;
        CH.meta = 0;
        Intent intent = new Intent(this, SolidColor.class);
        startActivity(intent);
    }

    // When the gradient button is pressed
    public void openGradient() {
        CH.mode = 1;
        CH.meta = 0;
        Intent intent = new Intent(this, Gradient.class);
        startActivity(intent);
    }
}
