#include <SoftwareSerial.h>
#include <Adafruit_NeoPixel.h>

#ifdef __AVR__
 #include <avr/power.h> // Required for 16 MHz Adafruit Trinket
#endif
#define LED_PIN    6
#define LED_COUNT 600
Adafruit_NeoPixel strip(LED_COUNT, 6, NEO_GRB + NEO_KHZ800);

String state = "";
String prevState = "";
int numPix = LED_COUNT;
bool moveBool = false;
int moveDelay = 0;
int moveCounter = 0;

void setup() {
  #if defined(__AVR_ATtiny85__) && (F_CPU == 16000000)
    clock_prescale_set(clock_div_1);
  #endif
  strip.begin();           // INITIALIZE NeoPixel strip object (REQUIRED)
  strip.show();            // Turn OFF all pixels ASAP
  strip.setBrightness(255); // Set BRIGHTNESS to about 1/5 (max = 255)

  Serial.begin(115200);
//  Serial1.begin(115200);
  delay(100);
}

void loop() {
  /*if (Serial1.available() > 0) {
    state = Serial1.readStringUntil(';');
    Serial.print(state);
    moveBool = false;
    moveDelay = 0;
    moveCounter = 0;
  }
  else*/ if (Serial.available() > 0) {
    state = Serial.readStringUntil(';');
    moveBool = false;
    moveDelay = 0;
    moveCounter = 0;
  }
  if (!state.equals("")) {  
    if (state.substring(0,state.indexOf(',')).toInt() == 0) {
      String s = state.substring(state.indexOf(',')+1);
      settings(s);
    }
    else {
      colorSetting(state);
    }
  }
  setAll(0,255,0);
}

void settings(String s) {
  int option = s.substring(0,s.indexOf(',')).toInt();
  int value = s.substring(s.indexOf(',')+1,s.length()).toInt();
  if (option == 0) {
    strip.setBrightness(value);
  }
  else if (option == 1) {
    numPix = value;
    for(int i = strip.numPixels(); i >= numPix; i--) {
      strip.setPixelColor(i, strip.Color(0,0,0));
    }
  }
  if (prevState != "") {
    colorSetting(prevState);
  }
}

void colorSetting(String s) {
  prevState = s;
  int mode = 0;
  if (s.length() > 1) {
    mode = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',')+1);
  } else {
    mode = s.toInt();
  }
  if (mode == 1) {
    solidColor(s);
  }
  if (mode == 2) {
    gradient2(s);
  }
  if (mode == 3) {
    gradient3(s);
  }
  if (mode == 4) {
    gradient4(s);
  }
  if (mode == 5) {
    partyTime(s);
  }
}

void solidColor(String s) {
  int red = s.substring(0,s.indexOf(',')).toInt();
  int green = s.substring(s.indexOf(',') + 1, s.indexOf(',', s.indexOf(',') + 1)).toInt();
  int blue = s.substring(s.indexOf(',', s.indexOf(',') + 1) + 1, s.length()).toInt();
  setAll(red,green,blue);
  state = "";
}

void gradient2(String s) {
  moveDelay = s.substring(0,s.indexOf(',')).toInt();
  
  if (moveBool == false) {
    s = s.substring(s.indexOf(',') + 1, s.length());
    int r1 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int g1 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int b1 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int r2 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int g2 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int b2 = s.substring(0,s.indexOf(',')).toInt();
    
    setLed(numPix/4,r1,g1,b1);
    setLed(numPix*3/4,r2,g2,b2);
  
    float redOff = abs(r1-r2)*(1.0)/(numPix/2);
    float greenOff = abs(g1-g2)*(1.0)/(numPix/2);
    float blueOff = abs(b1-b2)*(1.0)/(numPix/2);
    
    for (int i=numPix/4; i < (numPix*3/4) - 1; i++) {
      setLed(i+1, abs(r1-redOff*(i-numPix/4)), abs(g1-greenOff*(i-numPix/4)), abs(b1-blueOff*(i-numPix/4)));
    }
    for (int i=numPix/4; i < numPix/2; i++) {
      setLed(i+1-(((i-numPix/4)+1)*2), abs(r1-redOff*(i-numPix/4)), abs(g1-greenOff*(i-numPix/4)), abs(b1-blueOff*(i-numPix/4)));
    }
    for (int i=numPix/2; i < numPix*3/4; i++) {
      setLed(i+1+numPix/2-(((i-numPix/2)+1)*2), abs(r1-redOff*(i-numPix/4)), abs(g1-greenOff*(i-numPix/4)), abs(b1-blueOff*(i-numPix/4)));
    }
    strip.show();
  
    if (moveDelay != -1) {
      moveBool = true;
    } else {
      state = "";
    }
  } else {
    uint32_t temp = strip.getPixelColor(numPix-2);
    for(int i=numPix-1; i>0; i--) {
      strip.setPixelColor(i, strip.getPixelColor(i-1));
    }
    strip.setPixelColor(0, temp);
    strip.show();
    delay(moveDelay);
  }
}

void gradient3(String s) {
  moveDelay = s.substring(0,s.indexOf(',')).toInt();
  
  if (moveBool == false) {
    s = s.substring(s.indexOf(',') + 1, s.length());
    int r1 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int g1 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int b1 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int r2 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int g2 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int b2 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int r3 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int g3 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int b3 = s.substring(0,s.indexOf(',')).toInt();
    
    setLed(numPix/6,r1,g1,b1);
    setLed(numPix/2,r2,g2,b2);
    setLed(numPix*5/6,r3,g3,b3);
  
    float redOff1 = abs(r1-r2)*(1.0)/(numPix/3);
    float greenOff1 = abs(g1-g2)*(1.0)/(numPix/3);
    float blueOff1 = abs(b1-b2)*(1.0)/(numPix/3);
    float redOff2 = abs(r2-r3)*(1.0)/(numPix/3);
    float greenOff2 = abs(g2-g3)*(1.0)/(numPix/3);
    float blueOff2 = abs(b2-b3)*(1.0)/(numPix/3);
    float redOff3 = abs(r3-r1)*(1.0)/(numPix/3);
    float greenOff3 = abs(g3-g1)*(1.0)/(numPix/3);
    float blueOff3 = abs(b3-b1)*(1.0)/(numPix/3);
    
    for(int i=0; i<numPix/3; i++) {
      setLed(i+1+numPix/6, abs(r1-redOff1*i), abs(g1-greenOff1*i), abs(b1-blueOff1*i));
      setLed(i+1+numPix/2, abs(r2-redOff2*i), abs(g2-greenOff2*i), abs(b2-blueOff2*i));
    }
    for (int i=0; i<numPix/6; i++) {
      setLed(i+1+numPix*5/6, abs(r3-redOff3*i), abs(g3-greenOff3*i), abs(b3-blueOff3*i));
    }
    for (int i=0; i<numPix/6; i++) {
      setLed(i, abs(r3-redOff3*(i+numPix/6)), abs(g3-greenOff3*(i+numPix/6)), abs(b3-blueOff3*(i+numPix/6)));
    }
    
    strip.show();
  
    if (moveDelay != -1) {
      moveBool = true;
    } else {
      state = "";
    }
  } else {
    uint32_t temp = strip.getPixelColor(numPix-2);
    for(int i=numPix-1; i>0; i--) {
      strip.setPixelColor(i, strip.getPixelColor(i-1));
    }
    strip.setPixelColor(0, temp);
    strip.show();
    delay(moveDelay);
  }
}

void gradient4(String s) {
  moveDelay = s.substring(0,s.indexOf(',')).toInt();
  
  if (moveBool == false) {
    s = s.substring(s.indexOf(',') + 1, s.length());
    int r1 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int g1 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int b1 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int r2 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int g2 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int b2 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int r3 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int g3 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int b3 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int r4 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int g4 = s.substring(0,s.indexOf(',')).toInt();
    s = s.substring(s.indexOf(',') + 1, s.length());
    int b4 = s.substring(0,s.indexOf(',')).toInt();
    
    setLed(numPix/8,r1,g1,b1);
    setLed(numPix*3/8,r2,g2,b2);
    setLed(numPix*5/8,r3,g3,b3);
    setLed(numPix*7/8,r4,g4,b4);
  
    float redOff1 = abs(r1-r2)*(1.0)/(numPix/4);
    float greenOff1 = abs(g1-g2)*(1.0)/(numPix/4);
    float blueOff1 = abs(b1-b2)*(1.0)/(numPix/4);
    float redOff2 = abs(r2-r3)*(1.0)/(numPix/4);
    float greenOff2 = abs(g2-g3)*(1.0)/(numPix/4);
    float blueOff2 = abs(b2-b3)*(1.0)/(numPix/4);
    float redOff3 = abs(r3-r4)*(1.0)/(numPix/4);
    float greenOff3 = abs(g3-g4)*(1.0)/(numPix/4);
    float blueOff3 = abs(b3-b4)*(1.0)/(numPix/4);
    float redOff4 = abs(r4-r1)*(1.0)/(numPix/4);
    float greenOff4 = abs(g4-g1)*(1.0)/(numPix/4);
    float blueOff4 = abs(b4-b1)*(1.0)/(numPix/4);
    
    //setLed(i+1, abs(r1-redOff*(i-numPix/4)), abs(g1-greenOff*(i-numPix/4)), abs(b1-blueOff*(i-numPix/4)));
    
    for(int i=0; i<numPix/4; i++) {
      setLed(i+1+numPix/8, abs(r1-redOff1*i), abs(g1-greenOff1*i), abs(b1-blueOff1*i));
      setLed(i+1+numPix*3/8, abs(r2-redOff2*i), abs(g2-greenOff2*i), abs(b2-blueOff2*i));
      setLed(i+1+numPix*5/8, abs(r3-redOff3*i), abs(g3-greenOff3*i), abs(b3-blueOff3*i));
    }
    for (int i=0; i<numPix/8; i++) {
      setLed(i+1+numPix*7/8, abs(r4-redOff4*i), abs(g4-greenOff4*i), abs(b4-blueOff4*i));
    }
    for (int i=0; i<numPix/8; i++) {
      setLed(i, abs(r4-redOff4*(i+numPix/8)), abs(g4-greenOff4*(i+numPix/8)), abs(b4-blueOff4*(i+numPix/8)));
    }
    
    strip.show();
  
    if (moveDelay != -1) {
      moveBool = true;
    } else {
      state = "";
    }
  } else {
    uint32_t temp = strip.getPixelColor(numPix-2);
    for(int i=numPix-1; i>0; i--) {
      strip.setPixelColor(i, strip.getPixelColor(i-1));
    }
    strip.setPixelColor(0, temp);
    strip.show();
    delay(moveDelay);
  }
}

void partyTime(String s) {
  if (!moveBool) {
    moveDelay = s.toInt();
    moveBool = true;
  }
  uint32_t c = strip.Color(random(256),random(256),random(256));
  for (int i = 0; i < numPix; i++) {
    strip.setPixelColor(i, c);
  }
  strip.show();
  delay(moveDelay);
}

void setLed(int i, int r, int g, int b) {
  uint32_t c = strip.Color(r,g,b);
  strip.setPixelColor(i, c);
}

void setAll(int r, int g, int b) {
  uint32_t c = strip.Color(r,g,b);
  for (int i = 0; i < numPix; i++) {
    strip.setPixelColor(i, c);
  }
  strip.show();
}
