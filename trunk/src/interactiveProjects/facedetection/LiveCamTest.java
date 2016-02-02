package interactiveProjects.facedetection;

import gab.opencv.OpenCV;

import java.awt.Rectangle;

import processing.core.PApplet;
import processing.video.Capture;

public class LiveCamTest extends PApplet {

    Capture video;

    OpenCV  opencv;

    public void setup() {
        size(640, 480);
        video = new Capture(this, 640 / 2, 480 / 2);
        opencv = new OpenCV(this, 640 / 2, 480 / 2);
        opencv.loadCascade(OpenCV.CASCADE_FRONTALFACE);

        video.start();
    }

    public void draw() {
        scale(2);
        opencv.loadImage(video);

        image(video, 0, 0);

        noFill();
        stroke(0, 255, 0);
        strokeWeight(3);
        Rectangle[] faces = opencv.detect();
        println(faces.length);

        for (int i = 0; i < faces.length; i++) {
            println(faces[i].x + "," + faces[i].y);
            rect(faces[i].x, faces[i].y, faces[i].width, faces[i].height);
        }
    }

    public void captureEvent(Capture c) {
        c.read();
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", LiveCamTest.class.getName() });
    }

}
