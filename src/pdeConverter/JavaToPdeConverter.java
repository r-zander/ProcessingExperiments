package pdeConverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class JavaToPdeConverter {

    /**
     * Set to <code>null</code> to output the converted files in the folder of the java file.
     */
    private File       outputFolder   = new File("D:/xampp/htdocs/processingjs");

    private int        foundJavaFiles;

    private List<File> convertedFiles = new ArrayList<File>();

    public JavaToPdeConverter(File sourceDirectory) throws IOException {
        if (sourceDirectory.exists() == false) {
            throw new RuntimeException("src directory not found");
        }
        handleDirectory(sourceDirectory);
    }

    private void handleDirectory(File directory) throws IOException {
        if (directory.isDirectory() == false) {
            throw new RuntimeException(directory.getAbsolutePath() + " is not a folder");
        }
        String[] fileNames = directory.list();
        for (String fileName : fileNames) {
            File file = new File(directory, fileName);
            if (file.isDirectory()) {
                handleDirectory(file);
            } else if (fileName.endsWith(".java")) {
                handleJavaFile(file);
            }
        }
    }

    private static final Pattern IGNORE_SKETCH_PATTERN     = Pattern.compile("\\@IgnoreSketch");

    private static final Pattern CLASS_PATTERN             = Pattern.compile("public class .+ extends PApplet \\{");

    private static final Pattern END_CLASS_PATTERN         = Pattern.compile("^\\}");

    private static final Pattern OVERRIDE_PATTERN          = Pattern.compile("\\@Override");

    private static final Pattern SIZE_PATTERN              = Pattern.compile("size\\(.+\\);");

    private static final Pattern DISPLAY_DIMENSION_PATTERN = Pattern.compile("display(Width|Height)");

    private static final Pattern MAIN_METHOD_PATTERN       =
                                                                   Pattern.compile("public static void main\\(String\\[\\] args\\)");

    private static final Pattern END_MAIN_METHOD_PATTERN   = Pattern.compile("(    |\t)\\}");

    private static enum ParsingState {
        BEFORE_CLASS,
        INSIDE_CLASS,
        MAIN_METHOD,
        END_CLASS;
    }

    private void handleJavaFile(File javaFile) {
        foundJavaFiles++;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(javaFile));

            /*
             * The complete parsing (including the patterns) assume formatted code.
             */
            ParsingState state = ParsingState.BEFORE_CLASS;
            BufferedWriter writer = null;
            String line;
            while ((line = br.readLine()) != null) {
                switch (state) {
                    case BEFORE_CLASS:
                        /*
                         * 1st: make sure it's actually a processing sketch
                         */

                        if (matchesLine(IGNORE_SKETCH_PATTERN, line)) {
                            /*
                             * Ingore the complete file
                             */
                            return;
                        }

                        if (matchesLine(CLASS_PATTERN, line)) {
                            writer = newPdeFileWriter(javaFile);
                            convertedFiles.add(javaFile);
                            writeHeadComment(writer);
                            state = ParsingState.INSIDE_CLASS;
                        }
                        break;
                    case INSIDE_CLASS:
                        if (matchesLine(MAIN_METHOD_PATTERN, line)) {
                            state = ParsingState.MAIN_METHOD;
                            break;
                        }

                        if (matchesLine(END_CLASS_PATTERN, line)) {
                            state = ParsingState.END_CLASS;
                            break;
                        }

                        if (matchesLine(OVERRIDE_PATTERN, line)) {
                            /*
                             * skip line
                             */
                            break;
                        }

                        line = DISPLAY_DIMENSION_PATTERN.matcher(line).replaceAll("window.inner$1");

                        writeln(writer, line);

                        /*
                         * Insert smooth(); directly after size.
                         * This can always be done, because any smoothing instruction within the sketch should be after
                         * size and thus override the default smooth inserted here.
                         */
                        if (matchesLine(SIZE_PATTERN, line)) {
                            writeln(writer, "        smooth();");
                        }
                        break;
                    case MAIN_METHOD:
                        /*
                         * All lines get skipped. Just check for end of method.
                         */
                        if (matchesLine(END_MAIN_METHOD_PATTERN, line)) {
                            state = ParsingState.INSIDE_CLASS;
                        }
                        break;
                    default:
                        break;
                }

            }
            if (writer != null) {
                writer.close();
                createHtmlFile(javaFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void writeln(BufferedWriter writer, String line) throws IOException {
        writer.write(line);
        writer.newLine();
    }

    private static final Format DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    private static void writeHeadComment(BufferedWriter writer) throws IOException {
        writeln(writer, "/*");
        writeln(writer, " * Generated at " + DATE_FORMAT.format(new Date()));
        writeln(writer, " */");
    }

    private BufferedWriter newFileWriter(File javaFile, String fileEnding) throws IOException {
        String absolutePath = javaFile.getAbsolutePath();

        if (outputFolder != null) {
            absolutePath = outputFolder.getAbsolutePath() + File.separator + javaFile.getName();
        }

        absolutePath = absolutePath.substring(0, absolutePath.length() - 5) + "." + fileEnding;
        BufferedWriter writer = new BufferedWriter(new FileWriter(absolutePath));
        return writer;
    }

    private BufferedWriter newPdeFileWriter(File javaFile) throws IOException {
        return newFileWriter(javaFile, "pde");
    }

    private void createHtmlFile(File javaFile) throws IOException {
        BufferedWriter writer = newFileWriter(javaFile, "html");

        BufferedReader htmlTemplate =
                new BufferedReader(new InputStreamReader(
                        JavaToPdeConverter.class.getResourceAsStream("ProcessingJsTemplate.html"),
                        "UTF-8"));
        try {
            String line;
            while ((line = htmlTemplate.readLine()) != null) {
                String name = javaFile.getName();
                line = line.replace("${sketchFile}", name.substring(0, name.length() - 5) + ".pde");
                writeln(writer, line);
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (htmlTemplate != null) {
                try {
                    htmlTemplate.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static boolean matchesLine(Pattern patternToUse, String line) {
        return patternToUse.matcher(line).find();
    }

    private void outputStatistic() {
        System.out.println("Found " + foundJavaFiles + " JAVA files, the following " + convertedFiles.size()
                + " files were converted to PDE:");
        for (File file : convertedFiles) {
            System.out.println(file);
        }
    }

    public static void main(String[] args) throws IOException {
//        File sourceDirectory = new File("src");
        File sourceDirectory = new File("src/timevisualization/orbitclock");
        if (sourceDirectory.exists() == false) {
            throw new RuntimeException("src directory not found");
        }
        JavaToPdeConverter converter = new JavaToPdeConverter(sourceDirectory);
        converter.outputStatistic();
    }
}
