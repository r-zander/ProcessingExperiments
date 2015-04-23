import processing.opengl.*;
import peasy.*;

//VARIABLES
PImage bg;
PImage texmap;

int sDetail = 32;  //Sphere detail setting
float globeRadius = 300;

float[] cx,cz,sphereX,sphereY,sphereZ;
float sinLUT[];
float cosLUT[];
float SINCOS_PRECISION = 0.5f;
int SINCOS_LENGTH = int(360.0 / SINCOS_PRECISION);


//CAM
PeasyCam cam;


void setup() {
  size(800,800,OPENGL);
  background(255);

  //GLOBE
  texmap = loadImage("world.jpg");    
  initializeSphere(sDetail);
  
  cam = new PeasyCam(this, 800);
  cam.setMinimumDistance(400);
  cam.setMaximumDistance(1000);
  
  cam.rotateY(PI*0.5);
}


void draw() {
  background(0);
  renderGlobe();
}
