import beads.*;

AudioContext ac;

Glide gainGlide;
Glide frequencyGlide;


void setup() {
  size(400, 300);
  
  ac = new AudioContext();
  
  frequencyGlide = new Glide(ac, 20, 50);

  WavePlayer wp = new WavePlayer(ac, frequencyGlide, Buffer.SQUARE);
  
  WavePlayer wp2 = new WavePlayer(ac, 440, Buffer.SINE);
  
  gainGlide = new Glide(ac, 0.0, 50);
  
  Gain g = new Gain(ac, 1, gainGlide);
  
  g.addInput(wp);
  g.addInput(wp2);
  
  ac.out.addInput(g);

  ac.start();
}


void draw() {
  gainGlide.setValue(mouseX / (float)width);
  frequencyGlide.setValue(mouseY);
}



