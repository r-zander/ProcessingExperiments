package _converter;

import lombok.Data;
import lombok.experimental.Accessors;
import processing.core.PApplet;

public class RecordingUtil {

    public static int FPS        = 30;

    public static int MAX_WIDTH  = 1276;

    public static int MAX_HEIGHT = Math.round(MAX_WIDTH / 16f * 9);

    public static void setupRecording(RecordingArguments arguments) {
        float xRel = arguments.originalWidth / (float) MAX_WIDTH;
        float yRel = arguments.originalHeight / (float) MAX_HEIGHT;

        float rel = Math.max(xRel, yRel);

        if (arguments.renderer != null) {
            arguments.applet.size(
                    Math.round(arguments.originalWidth / rel),
                    Math.round(arguments.originalHeight / rel),
                    arguments.renderer);
        } else {
            arguments.applet.size(
                    Math.round(arguments.originalWidth / rel),
                    Math.round(arguments.originalHeight / rel));
        }
        System.out.println(arguments.originalWidth + " x " + arguments.originalHeight
                + " scaled to " + Math.round(arguments.originalWidth / rel) + " x "
                + Math.round(arguments.originalHeight / rel) + " (max " + MAX_WIDTH
                + " x " + MAX_HEIGHT + ")");
        arguments.applet.registerMethod("draw", new FrameRecorder(
                arguments.applet,
                arguments.listener,
                arguments.startFrame,
                arguments.framesRecorded));
        arguments.applet.frameRate(FPS);

        if (arguments.listener != null) {
            arguments.listener.onSetupRecording(arguments.applet);
        }
    }

    @Data
    @Accessors(fluent = true)
    public static class RecordingArguments {

        private final PApplet     applet;

        private final int         originalWidth;

        private final int         originalHeight;

        private String            renderer;

        private RecordingListener listener;

        private int               startFrame;

        private int               framesRecorded;
    }

    public interface RecordingListener {

        public void onSetupRecording(PApplet $);

        public void onBeforeFrameRecording(PApplet $);
    }

    public static class FrameRecorder {

        private PApplet           applet;

        private RecordingListener listener;

        private int               startFrame;

        private int               framesRecorded;

        public FrameRecorder(PApplet applet, RecordingListener listener, int startFrame,
                int framesRecorded) {
            this.applet = applet;
            this.listener = listener;
            this.startFrame = startFrame;
            this.framesRecorded = framesRecorded;
        }

        public void draw() {
            if ((applet.frameCount - startFrame) > framesRecorded) {
                applet.exit();
            }
            if (applet.frameCount >= startFrame) {
                if (listener != null) {
                    listener.onBeforeFrameRecording(applet);
                }
                applet.saveFrame("recordings/" + applet.getClass().getSimpleName()
                        + "/frame-####.tga");
            }
        }
    }

}
