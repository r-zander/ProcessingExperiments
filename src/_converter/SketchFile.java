package _converter;

import java.io.File;

public class SketchFile extends File {

    public static final String FILE_ENDING = "pde";

    private String             sketchName;

    private File               sketchDirectory;

    public SketchFile(File outputDirectory, File javaFile) {
        super(outputDirectory, getFileName(javaFile) + "." + FILE_ENDING);
        this.sketchDirectory = outputDirectory;
    }

    /**
     * @param file
     * @return the filename without ending
     */
    public static String getFileName(File file) {
        String fileName = file.getName();
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }

    public File getSketchDirectory() {
        return sketchDirectory;
    }

    public String getSketchName() {
        if (sketchName == null) {
            sketchName = getFileName(this);
        }
        return sketchName;
    }

    public static void main(String[] args) {}

}
