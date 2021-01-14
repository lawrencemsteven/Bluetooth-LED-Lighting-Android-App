package com.example.blc;

//////////////////////////////////////////////////////////////////////////////
// ColorHelper                                                              //
//                                                                          //
// This class store the various colors and the current state of the program //
// This is mostly used to send over information about the current state     //
//////////////////////////////////////////////////////////////////////////////

public class ColorHelper {
    // 0 = Solid
    // 1-3 = Gradient
    // 4 = Random
    static int mode = 0;
    static int meta = 0;

    static int modeMax = 3;
    static int metaMax = 4;

    static int[][][] rgbArray = new int[modeMax][metaMax][3];

    static int gradDelay = -1;
    static int gradNum = 2;
}
