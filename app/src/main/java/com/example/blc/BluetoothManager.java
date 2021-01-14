package com.example.blc;

////////////////////////////////////////////////////////////////////////////
// BluetoothManager                                                       //
//                                                                        //
// This class will manage the connection over bluetooth with the arduino. //
////////////////////////////////////////////////////////////////////////////

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothManager {
    static BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    static BluetoothDevice bluetoothDevice;
    static ConnectThread connectThread;
    static ConnectedThread connectedThread;
    static BluetoothSocket bluetoothSocket;

    public static String bluetoothName = "";
    public static String bluetoothAddress = "";

    public static BluetoothAdapter getBluetoothAdapter() {
        return bluetoothAdapter;
    }

    public static void startThreads() {
        connectThread = new ConnectThread(bluetoothDevice);
        connectedThread = new ConnectedThread(bluetoothSocket);
    }

    public static void write(String s) {
        connectedThread.write(s.getBytes());
    }

    private static class ConnectThread extends Thread {
        private final BluetoothDevice mmDevice;
        private final BluetoothSocket mmSocket;
        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket
            // because mmSocket is final.
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                // Get a BluetoothSocket to connect with the given BluetoothDevice.
                // MY_UUID is the app's UUID string, also used in the server code.
                tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805f9b34fb")); // UUID from bluetooth module on arduino
            } catch (IOException e) {
                //Toast.makeText(getApplicationContext(),"Socket's Create() Method Failed",Toast.LENGTH_SHORT).show();
            }
            mmSocket = tmp;
            bluetoothSocket = tmp;
            run();
        }

        public void run() {
            // Cancel discovery because it otherwise slows down the connection.
            bluetoothAdapter.cancelDiscovery();

            try {
                // Connect to the remote device through the socket. This call blocks
                // until it succeeds or throws an exception.
                mmSocket.connect();
                //Toast.makeText(getApplicationContext(),"Connected to: " + bluetoothName,Toast.LENGTH_SHORT).show();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and return.
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    //Toast.makeText(getApplicationContext(),"Could Not Close The Client Socket",Toast.LENGTH_SHORT).show();
                }
            }

            // The connection attempt succeeded. Perform work associated with
            // the connection in a separate thread.
            // manageMyConnectedSocket(mmSocket);
        }
    }

    private static class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private byte[] mmBuffer; // mmBuffer store for the stream

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams; using temp objects because
            // member streams are final.
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                //Toast.makeText(getApplicationContext(),"Error occured while creating input stream",Toast.LENGTH_SHORT).show();
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                //Toast.makeText(getApplicationContext(),"Error occured while creating output stream",Toast.LENGTH_SHORT).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                try {
                    // Read from the InputStream.
                    numBytes = mmInStream.read(mmBuffer);
                    // Send the obtained bytes to the UI activity.
                    //Message readMsg = handler.obtainMessage(MessageConstants.MESSAGE_READ, numBytes, -1, mmBuffer);
                    //readMsg.sendToTarget();
                } catch (IOException e) {
                    //Toast.makeText(getApplicationContext(),"Input stream was disconnected",Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device.
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);

                // Share the sent message with the UI activity.
                //Message writtenMsg = handler.obtainMessage(MessageConstants.MESSAGE_WRITE, -1, -1, mmBuffer);
                //writtenMsg.sendToTarget();
            } catch (IOException e) {
                //Toast.makeText(getApplicationContext(),"Error occured while sending data",Toast.LENGTH_SHORT).show();

                // Send a failure message back to the activity.
                //Message writeErrorMsg = handler.obtainMessage(MessageConstants.MESSAGE_TOAST);
                Bundle bundle = new Bundle();
                bundle.putString("toast",
                        "Couldn't send data to the other device");
                //writeErrorMsg.setData(bundle);
                //handler.sendMessage(writeErrorMsg);
            }
        }
    }
}