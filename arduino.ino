#include <ArduinoJson.h>
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>


#define FIREBASE_HOST "smart-home-e7748.firebaseio.com"
#define FIREBASE_AUTH "K6vgj05vG7KJQvVujNaCxacTz5ytyUKsFGjfP6XI"
#define WIFI_SSID "realme X"
#define WIFI_PASSWORD "01234567"

void setup() {
  Serial.begin(9600);
  pinMode(D2, OUTPUT);

  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
      Firebase.set("led_status", 0);
}

int n = 0;

void loop() {
  // set value
  // handle error
  if (Firebase.failed()) {
      Serial.print("setting /number failed:");
      Serial.println(Firebase.error());  
      return;
  }
  delay(500);
  
  // update value
  n = Firebase.getInt("led_status");
  // handle error
  if (n==1) {
      Serial.println("led get turned on");
      digitalWrite(D2, HIGH); 
      delay(500); 
      return;
  }else{
      Serial.println("led get turned off");
      digitalWrite(D2, LOW); 
      delay(500);
      return; 
  }
}
