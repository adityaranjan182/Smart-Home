#include "FirebaseESP8266.h"
#include <ESP8266WiFi.h>

#define FIREBASE_HOST "your_firebase_project.firebaseio.com"
#define FIREBASE_AUTH "database_secret"
#define WIFI_SSID "wifi_name"
#define WIFI_PASSWORD "wifi_password"

FirebaseData data_1;
FirebaseData data_2;

void setup()
{

    Serial.begin(9600);
    pinMode(D1, OUTPUT);
    pinMode(D2, OUTPUT);
    pinMode(D3, OUTPUT);
    pinMode(D4, OUTPUT);
    pinMode(D5, OUTPUT);

    digitalWrite(D1, HIGH); //glows red while connecting

    digitalWrite(D3, HIGH);
    digitalWrite(D4, HIGH);
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
    Serial.print("Connecting to Wi-Fi");
    while (WiFi.status() != WL_CONNECTED)
    {
        Serial.print(".");
        delay(200);
    }
    Serial.println();
    Serial.print("Connected with IP: ");
    Serial.println(WiFi.localIP());
    digitalWrite(D1, LOW);
    delay(500);
    digitalWrite(D2, HIGH); //glows green when successfully connected to wifi
    Serial.println();

    Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

    //auto reconnect the WiFi when connection lost
    Firebase.reconnectWiFi(true);

    //adding nodes to firebase realtime database
    if (Firebase.setInt(data_1, "/bulb_status", 0))
    {
        //Success
        Serial.println("Set int data success");
    }
    if (Firebase.setInt(data_2, "/fan_status", 0))
    {
        //Success
        Serial.println("Set int data success");
    }
}

void loop()
{
    int bulb = data_1.intData();
    int fan = data_2.intData();
    if (!(Firebase.getInt(data_1, "bulb_status")) || !(Firebase.getInt(data_2, "fan_status")))
    {
        Serial.println("error while communicating with firebase");
        digitalWrite(D5, HIGH);
        delay(500);
        digitalWrite(D5, LOW); //blue light starts blinking when there is an
        delay(500);            //error while communicating with firebase
        return;
    }

    if (Firebase.getInt(data_1, "bulb_status"))
    {
        if (bulb == 1)
        {
            Serial.println("bulb get turned on");
            digitalWrite(D3, LOW);
        }
        else
        {
            Serial.println("bulb off");
            digitalWrite(D3, HIGH);
        }
    }
    if (Firebase.getInt(data_2, "fan_status"))
    {
        if (fan == 1)
        {
            Serial.println("fan get turned on");
            digitalWrite(D4, LOW);
        }
        else
        {
            Serial.println("fan off");
            digitalWrite(D4, HIGH);
        }
    }
}