#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>

#define wifiSSID "wifi_name"
#define wifiPASS "wifi_password"
#define firebaseHost "your_firebase_project.firebaseio.com"
#define firebaseAuth "database_secret"

int dataReceive;
FirebaseData Home;
FirebaseJson receivedState;
FirebaseJsonData currState;
char query[10];

bool IoTmode = true;

void InitializePins(){
    pinMode(D1, OUTPUT);
    pinMode(D2, OUTPUT);
    pinMode(D3, OUTPUT);
    pinMode(D4, OUTPUT);
    pinMode(D5, OUTPUT);
    pinMode(D6, OUTPUT);
    pinMode(D7, OUTPUT);

    digitalWrite(D1, HIGH);
    digitalWrite(D2, HIGH);
    digitalWrite(D3, HIGH);
    digitalWrite(D4, HIGH);
}

void connectToWiFi(){
    digitalWrite(D5,HIGH);                  // red led glows while connecting to wifi
    WiFi.begin(wifiSSID, wifiPASS);
    Serial.print("Connecting to Wi-Fi");
    while (WiFi.status() != WL_CONNECTED)
    {
        Serial.print(".");
        delay(500);
    }
    digitalWrite(D5,LOW);                  // red led turns off and green led turns on after successfully
    digitalWrite(D6,HIGH);                 // connecting to the wifi network
    Serial.println();
    Serial.print("Connected with IP: ");
    Serial.println(WiFi.localIP());
}

bool addAppliances(FirebaseData &fbdo, const String &path, FirebaseJson &json){
    if(Firebase.setJSON(fbdo, "/home-1", json)){
        Serial.println("data inserted successfully");
        return true;
    }else{
        Serial.println("Failed to insert data");
        return false;
    }
}

bool getStatus(FirebaseData &fbdo, const String &path){
    if(Firebase.getJSON(fbdo, path)){
        return true;
    }
    digitalWrite(D7,HIGH);                //blue light blinks when there is an error while communicating with database and
    delay(1000);                          //user need to switch to bluetooth mode
    digitalWrite(D7,LOW);
    return false;
}

void setup(){
  
      Serial.begin(9600);
      
      InitializePins();
      connectToWiFi();

      FirebaseJson json;
      json.add("appliance-1",0);
      json.add("appliance-2",0);
      json.add("appliance-3",0);
      json.add("appliance-4",0);

      Firebase.begin(firebaseHost, firebaseAuth);

      //auto reconnect the WiFi when connection lost
      Firebase.reconnectWiFi(true);
      
      //setting initial values to the database
      Firebase.setJSON(Home, "/home-1", json);
}

void IoT(){
      getStatus(Home,"/home-1");
      receivedState = Home.jsonObject(); 
      int count = receivedState.iteratorBegin();
      for (int i = 1; i <= count; i++){
          snprintf(query, 12, "appliance-%d", i);
          receivedState.get(currState, query);
          int flag = currState.intValue;
          switch(i){
              case 1 : if(flag == 1) digitalWrite(D1, LOW);
                       else digitalWrite(D1, HIGH);
                       break;
              case 2 : if(flag == 1) digitalWrite(D2, LOW);
                       else digitalWrite(D2, HIGH);
                       break;
              case 3 : if(flag == 1) digitalWrite(D3, LOW);
                       else digitalWrite(D3, HIGH);
                       break;
              case 4 : if(flag == 1) digitalWrite(D4, LOW);
                       else digitalWrite(D4, HIGH);
                       break;
          }
      }
      receivedState.clear();
}

void BTmode(){
      if(Serial.available()>0){
          dataReceive = Serial.read();
          switch (dataReceive){
              case 1:  IoTmode = false;
                       break;
              case 2:  IoTmode = true;
                       break;
              case 10: digitalWrite(D1,LOW);
                       break;
              case 11: digitalWrite(D1,HIGH);
                       break;
              case 20: digitalWrite(D2,LOW);
                       break;
              case 21: digitalWrite(D2,HIGH);
                       break;
              case 30: digitalWrite(D3,LOW);
                       break;
              case 31: digitalWrite(D3,HIGH);
                       break;
              case 40: digitalWrite(D4,LOW);
                       break;
              case 41: digitalWrite(D4,HIGH);
                       break;
           }
     }
}

void loop(){
    BTmode();
    if(IoTmode){
        IoT();
    }
    delay(1000);
}
