import gab.opencv.*;
import processing.video.*;
import java.awt.*;

Capture video;
OpenCV opencv;

void setup() {
  size(640, 480);
  video = new Capture(this, 640/2, 480/2);
  opencv = new OpenCV(this, 640/2, 480/2);
  opencv.loadCascade(OpenCV.CASCADE_FRONTALFACE);  

  video.start();
}

void draw() {
  scale(2);
  opencv.loadImage(video);

//  image(video, 0, 0 );

  noFill();
  stroke(0, 255, 0);
  strokeWeight(3);
  Rectangle[] faces = opencv.detect();

  for (int i = 0; i < faces.length; i++) {
    video.copy(faces[i].x, faces[i].y, faces[i].width, faces[i].height, 0, 0, width, height);
//    rect(faces[i].x, faces[i].y, faces[i].width, faces[i].height);
  image(video, 0, 0);
  }
}

void captureEvent(Capture c) {
  c.read();
}

