import java.io.*;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioFileFormat.Type;

import beads.*;

AudioContext ac;

RecordToSample rts;
Sample outputSample;


void setup() {

  size(400, 300);
  ac = new AudioContext();

  Clock clock = new Clock(ac, 500);
  clock.addMessageListener(
  new Bead() {
    int pitch;

    public void messageReceived(Bead message) {
      Clock c = (Clock)message;

      if (c.isBeat()) {
        //println("BEAT");

        WavePlayer wp1 = new WavePlayer(ac, 90, Buffer.SINE);
        Glide gainGlide = new Glide(ac, 1.0, 100);
        gainGlide.setValue(0.0);
        Gain g1 = new Gain(ac, 1, gainGlide);
        g1.addInput(wp1);

        WavePlayer wp2 = new WavePlayer(ac, 440, Buffer.NOISE);
        Glide gain2Glide = new Glide(ac, 0.5, 120);
        gain2Glide.setValue(0.0);
        Gain g2 = new Gain(ac, 1, gain2Glide);
        g2.addInput(wp2);

        ac.out.addInput(g1);
        ac.out.addInput(g2);
      }

      if (c.getCount() % 16 == 8) {
        //println("COUNT 2");

        Envelope env = new Envelope(ac, 0);
        env.addSegment(1, 10);
        env.addSegment(0.5, 20);
        env.addSegment(0.0, 500);

        WavePlayer wp1 = new WavePlayer(ac, 440, Buffer.SAW);
        Gain g1 = new Gain(ac, 1, env);
        g1.addInput(wp1);

        TapIn delayIn = new TapIn(ac, 400);
        delayIn.addInput(g1);
        TapOut delayOut = new TapOut(ac, delayIn, 200);

        OnePoleFilter filter = new OnePoleFilter(ac, 280);
        filter.addInput(delayOut);

        /*
        WavePlayer wp2 = new WavePlayer(ac, 440, Buffer.NOISE);
         Glide gain2Glide = new Glide(ac, 0.8, 400);
         gain2Glide.setValue(0.0);
         Gain g2 = new Gain(ac, 1, gain2Glide);
         g2.addInput(wp2);
         */
        ac.out.addInput(g1);
        ac.out.addInput(filter);
        //ac.out.addInput(g2);
      }
    }
  }
  );
  
  
  try {
    AudioFormat af = new AudioFormat(44100, 16, 1, true, true);
    outputSample = new Sample(5000);
    rts = new RecordToSample(ac, outputSample, RecordToSample.Mode.INFINITE);
  } catch(Exception e) {
    e.printStackTrace();
  }
  
  rts.addInput(ac.out);
  
  ac.out.addDependent(rts);
  ac.out.addDependent(clock);
  ac.start();
}

void draw() {}


void keyPressed() {
  println("SAVE");
  if(key == 's') {
    rts.clip();
    Sample sample = rts.getSample();
    try {
      sample.write(sketchPath("") + "test" + day() + "-" + hour() + "-" + minute() + "-" + second() + ".wav", AudioFileType.WAV);
    } catch(Exception e) {
      e.printStackTrace();
    }
    ac.stop();
    exit();
  }
}

