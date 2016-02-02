package creativecode.sounds.analyzer;

import java.io.File;

import processing.core.PApplet;
import beads.AudioContext;
import beads.FFT;
import beads.Gain;
import beads.PowerSpectrum;
import beads.Sample;
import beads.SampleManager;
import beads.SamplePlayer;
import beads.ShortFrameSegmenter;

public class AudioAnalyzer extends PApplet {

    private AudioContext ac;

    PowerSpectrum        ps;

    @Override
    public void setup() {
        size(400, 400);
        ac = new AudioContext();

        selectInput("Select Audio File", "handleFile");
    }

    public void handleFile(File file) {
        Sample sample = SampleManager.sample(file.getAbsolutePath());
        SamplePlayer player = new SamplePlayer(ac, sample);
        Gain gain = new Gain(ac, 2, 0.2f);
        gain.addInput(player);
        ac.out.addInput(gain);

        ShortFrameSegmenter sfs = new ShortFrameSegmenter(ac);
        sfs.setChunkSize(512);
        sfs.addInput(ac.out);

        FFT fft = new FFT();
        ps = new PowerSpectrum();
        sfs.addListener(fft);
        fft.addListener(ps);
        ac.out.addDependent(sfs);

        ac.start();
    }

    float heighestValue = 0;

    @Override
    public void draw() {
        if (ps == null) {
            return;
        }

        background(0);
        stroke(255);

        float[] features = ps.getFeatures();
        if (features != null) {
            for (int i = 0; i < features.length; i++) {
                float log = log(features[i]);

                if (log > heighestValue) {
                    heighestValue = log;
                    println(heighestValue);
                }

                line(i, height, i, height - map(log, 0, 7, 0, height));
            }
        }

    }

    public static void main(String[] args) {
        PApplet.main(new String[] { AudioAnalyzer.class.getName() });
    }
}