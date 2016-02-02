package interactiveProjects.facedetection;

import gab.opencv.OpenCV;

import java.awt.Rectangle;

import processing.core.PApplet;

public class FaceDetection extends PApplet {

    OpenCV      opencv;

    Rectangle[] faces;

    public void setup() {
        opencv = new OpenCV(this, "facedetection/test.jpg");
        size(opencv.width, opencv.height);

        opencv.loadCascade(OpenCV.CASCADE_FRONTALFACE);
        faces = opencv.detect();
    }

    public void draw() {
        image(opencv.getInput(), 0, 0);

        noFill();
        stroke(0, 255, 0);
        strokeWeight(3);
        for (int i = 0; i < faces.length; i++) {
            ellipse(faces[i].x + faces[i].width / 2, faces[i].y + faces[i].height / 2, faces[i].width, faces[i].height);
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", FaceDetection.class.getName() });
    }

}
