#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>

#define FIREBASE_HOST "smart-home-e7748.firebaseio.com"
#define FIREBASE_AUTH "K6vgj05vG7KJQvVujNaCxacTz5ytyUKsFGjfP6XI"
#define WIFI_SSID "realme X"
#define WIFI_PASSWORD "01234567"

FirebaseData firebaseData1;
FirebaseData firebaseData2;

void setup()
{

    Serial.begin(115200);
    pinMode(5, OUTPUT);
    pinMode(4, OUTPUT);
    pinMode(0, OUTPUT);
    pinMode(2, OUTPUT);

    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.print("Connecting to Wi-Fi");
    while (WiFi.status() != WL_CONNECTED)
    {
        Serial.print(".");
        delay(300);
    }
    Serial.println();
    Serial.print("Connected with IP: ");
    Serial.println(WiFi.localIP());
    Serial.println();

    Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

    //Enable auto reconnect the WiFi when connection lost
    Firebase.reconnectWiFi(true);
    if (Firebase.setInt(firebaseData1, "/bulb_status", 0))
    {
        //Success
        Serial.println("Set int data success");
    }
    else
    {
        //Failed?, get the error reason from firebaseData

        Serial.print("Error in setInt, ");
        Serial.println(firebaseData1.errorReason());
    }
    if (Firebase.setInt(firebaseData2, "/fan_status", 0))
    {
        //Success
        Serial.println("Set int data success");
    }
    else
    {
        //Failed?, get the error reason from firebaseData

        Serial.print("Error in setInt, ");
        Serial.println(firebaseData2.errorReason());
    }
}

void loop()
{
    int bulb = firebaseData1.intData();
    int fan = firebaseData2.intData();
    if (Firebase.getInt(firebaseData1, "bulb_status"))
    {
        if (bulb == 1)
        {
            Serial.println("bulb on");
        }
        else
        {
            Serial.println("bulb off");
        }
    }
    if (Firebase.getInt(firebaseData2, "fan_status"))
    {
        if (fan == 1)
        {
            Serial.println("fan on");
        }
        else
        {
            Serial.println("fan off");
        }
    }
    delay(500);
}
