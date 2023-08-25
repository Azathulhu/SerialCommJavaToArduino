//Basic Serial communication for the arduino to receive data.
//#Chishiki Sakagami

#include <Servo.h>

Servo xAxisServo;
Servo yAxisServo;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);//Ready to begin connection
  xAxisServo.attach(3);
  yAxisServo.attach(5);
}

void loop() {
  // put your main code here, to run repeatedly:
  if(Serial.available() > 0){
    String data = Serial.readStringUntil('\n');
    int indexChar = data.indexOf(',');
    
    if (indexChar != -1){
      int x = data.substring(0, indexChar).toInt();
      int y = data.substring(indexChar + 1).toInt();

      //You may change the values of these according to your motor but it depends.
      int xAxisAngle = map(x, 0, 1000, 0, 180);
      int yAxisAngle = map(y, 0, 500, 0, 180);//piece of shit

      xAxisServo.write(xAxisAngle);
      yAxisServo.write(yAxisAngle);

      //That is the entire code of the backend.
    }
  }
}
