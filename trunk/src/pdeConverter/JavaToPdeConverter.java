package pdeConverter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaToPdeConverter {

    /**
     * Set to <code>null</code> to output the converted files in the folder of the java file.
     */
//    private File       outputFolder   = new File("D:/xampp/htdocs/processingjs");
    private File       outputFolder       = new File("D:/xampp/htdocs/portfolio-draft/pde/orbitclock");

//    private File       outputFolder   = new File("D:/xampp/htdocs/processingjs/circleFlower");

    private int        foundJavaFiles;

    private List<File> convertedFiles     = new ArrayList<File>();

    private boolean    skipHtmlGeneration = true;

    public JavaToPdeConverter(File sourceDirectory) throws IOException {
        if (outputFolder == null) {
            throw new UnsupportedOperationException("Irgendwie muss dann noch ein Unterordner angelegt werden.");
        }
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

    private static final Pattern IGNORE_CLASS_PATTERN     = Pattern.compile("\\@IgnoreClass");

    private static final Pattern SKETCH_CLASS_PATTERN     = Pattern.compile("public class .+ extends PApplet \\{");

    private static final Pattern ENUM_PATTERN             = Pattern.compile("(^| )enum ");

    private static final Pattern CLASS_PATTERN            = Pattern.compile("(^| )(class|interface) ");

    private static final Pattern END_CLASS_PATTERN        = Pattern.compile("^\\}");

    private static final Pattern SKIPPED_LINES_PATTERN    = Pattern.compile("(^//|" + "\\@Override|"
                                                                  + "\\@SuppressWarnings|" + "\\@formatter:off|"
                                                                  + "\\@formatter:on)");

    private static final Pattern IGNORED_LINES_PATTERN    = Pattern.compile("\\@Ignore( (\\d+))?");

    private static final Pattern SIZE_PATTERN             = Pattern.compile("size\\(.+\\);");

    private static final Pattern SYSTEM_MILLIS_PATTERN    = Pattern.compile(
                                                                  "System.currentTimeMillis()",
                                                                  Pattern.LITERAL);

    private static final Pattern SOP_PATTERN              = Pattern.compile("System.out.println(", Pattern.LITERAL);

    private static final Pattern STRING_FORMAT_PATTERN    = Pattern.compile("String.format(", Pattern.LITERAL);

    private static final Pattern NUMBERS_PATTERN          = Pattern.compile("( \\d*\\.?\\d+)(f|L)");

    private static final Pattern MAIN_METHOD_PATTERN      = Pattern.compile(
                                                                  "public static void main(String[] args)",
                                                                  Pattern.LITERAL);

    private static final Pattern END_MAIN_METHOD_PATTERN  = Pattern.compile("(    |\t)\\}");

    private static final Pattern END_METHOD_PATTERN       = Pattern.compile("^\\}");

    private static final Pattern IMPORT_PATTERN           = Pattern.compile("import ([^;]+);");

    private static final Pattern SETUP_METHOD_PATTERN     = Pattern.compile("void setup()", Pattern.LITERAL);

    private static final Pattern SMOOTHING_PATTERN        = Pattern.compile("(no)?[sS]mooth\\(\\);");

    private static final Pattern COMMENTS_TO_KEEP_PATTERN = Pattern.compile("^(\\/\\*\\*|" + " \\* |" + " \\*\\/)");

    private static enum ParsingState {
        BEFORE_CLASS,
        START_OF_SKETCH,
        INSIDE_CLASS,
        SETUP_METHOD,
        MAIN_METHOD,
        END_CLASS;
    }

    private void handleJavaFile(final File javaFile) {
        foundJavaFiles++;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(javaFile));

            /*
             * The complete parsing (including the patterns) assume formatted code.
             */
            ParsingState state = ParsingState.BEFORE_CLASS;
            BufferedWriter writer = null;
            Queue<String> imports = new LinkedList<String>();
            String line;
            int linesToIgnore = 0;
            boolean ignoreNextLineIfEmpty = false;
            while ((line = br.readLine()) != null) {

                /*
                 * Handle line ignoring.
                 */
                if (linesToIgnore > 0) {
                    if (!line.isEmpty()) {
                        linesToIgnore--;
                    }
                    continue;
                }

                if (ignoreNextLineIfEmpty) {
                    if (line.isEmpty()) {
                        continue;
                    }
                    ignoreNextLineIfEmpty = false;
                }

                Matcher matcher = IGNORED_LINES_PATTERN.matcher(line);
                if (matcher.find()) {
                    String group = matcher.group(2);
                    if (group == null) {
                        linesToIgnore = 1;
                    } else {
                        linesToIgnore = Integer.parseInt(group);
                    }
                    ignoreNextLineIfEmpty = true;
                    continue;
                }

                switch (state) {
                    case BEFORE_CLASS:
                        /*
                         * Remember imports. If the file is a sketch, the imports will be handled in the end.
                         */
                        matcher = IMPORT_PATTERN.matcher(line);
                        if (matcher.find()) {
                            imports.add(matcher.group(1));
                        }

                        /*
                         * Search for ignore annotations
                         */
                        if (matchesLine(IGNORE_CLASS_PATTERN, line)) {
                            /*
                             * Ingore the complete file
                             */
                            return;
                        }

                        /*
                         * 1st: make sure it's actually a processing sketch
                         */
                        if (matchesLine(SKETCH_CLASS_PATTERN, line)) {
                            writer = newPdeFileWriter(javaFile);
                            convertedFiles.add(javaFile);
                            writeHeadComment(writer, null);
                            state = ParsingState.START_OF_SKETCH;
                        }
                        break;
                    case START_OF_SKETCH:
                        if (line.isEmpty()) {
                            break;
                        } else {
                            state = ParsingState.INSIDE_CLASS;
                        }
                        //$FALL-THROUGH$
                    case INSIDE_CLASS:
                        if (matchesLine(SETUP_METHOD_PATTERN, line)) {
                            state = ParsingState.SETUP_METHOD;
                            writeln(writer, remove1LvlIndentation(line));
                            /*
                             * Inject call to preSetup() as first method of setup().
                             */
                            writeln(writer, "    if (typeof preSetup == 'function') { preSetup(); }");
                            break;
                        }

                        if (matchesLine(SMOOTHING_PATTERN, line)) {
                            /*
                             * Skip line.
                             */
                            break;
                        }

                        if (matchesLine(MAIN_METHOD_PATTERN, line)) {
                            state = ParsingState.MAIN_METHOD;
                            break;
                        }

                        if (matchesLine(END_CLASS_PATTERN, line)) {
                            state = ParsingState.END_CLASS;
                            break;
                        }

                        line = transformCode(line);

                        line = remove1LvlIndentation(line);

                        writeln(writer, line);
                        break;
                    case SETUP_METHOD:
                        line = transformCode(line);
                        line = remove1LvlIndentation(line);
                        writeln(writer, line);

                        if (matchesLine(SIZE_PATTERN, line)) {

                            /*
                             * Insert smooth(); directly after size(...).
                             * This can always be done, because any smoothing instruction within the sketch should be
                             * after
                             * size and thus override the default smooth inserted here.
                             */
                            writeln(writer, "    smooth();");
                        }

                        if (matchesLine(END_METHOD_PATTERN, line)) {
                            state = ParsingState.INSIDE_CLASS;
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

                /*
                 * Add all java files in the same directory as the sketch to the list of imports to handle.
                 */
                String[] javaFiles = javaFile.getParentFile().list(new FilenameFilter() {

                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".java") && !name.equals(javaFile.getName());
                    }
                });
                String sketchPath = javaFile.getPath();
                String sketchPackage =
                        sketchPath.substring("src/".length(), sketchPath.lastIndexOf(File.separatorChar) + 1);
                for (String fileName : javaFiles) {
                    imports.add(sketchPackage + fileName);
                }

                List<String> importedFiles = handleImports(imports);

                if (!skipHtmlGeneration) {
                    createHtmlFile(javaFile, importedFiles);
                }
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

    private String remove1LvlIndentation(String line) {
        if (line != null && !line.isEmpty()) {
            line = line.substring(4);
        }
        return line;
    }

    /**
     * @param line
     * @return <code>null</code> if the line should be skipped.
     */
    private String transformCode(String line) {
        if (matchesLine(SKIPPED_LINES_PATTERN, line)) {
            /*
             * skip line
             */
            return null;
        }

//        line = DISPLAY_DIMENSION_PATTERN.matcher(line).replaceAll("window.inner$1");
        line = SYSTEM_MILLIS_PATTERN.matcher(line).replaceAll("Date.now()");
        line = SOP_PATTERN.matcher(line).replaceAll("console.log(");
        line = STRING_FORMAT_PATTERN.matcher(line).replaceAll("sprintf(");
        line = NUMBERS_PATTERN.matcher(line).replaceAll("$1");

        return line;
    }

    private List<String> handleImports(Queue<String> imports) {
        Set<String> handledImports = new HashSet<String>();

        ArrayList<String> importedFiles = new ArrayList<String>();
        while (!imports.isEmpty()) {
            String importClass = imports.remove();
            handledImports.add(importClass);

            File importFile;
            if (importClass.endsWith(".java")) {
                importFile = new File("src/" + importClass);

            } else {
                importFile = new File("src/" + importClass.replace(".", "/") + ".java");
            }
            if (importFile.exists()) {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(importFile));

                    /*
                     * The complete parsing (including the patterns) assume formatted code.
                     */
                    ParsingState state = ParsingState.BEFORE_CLASS;
                    BufferedWriter writer = null;
                    String line;
                    int linesToIgnore = 0;
                    boolean ignoreNextLineIfEmpty = false;
                    List<String> beforeClassComments = null;

                    lineLoop:
                    while ((line = br.readLine()) != null) {

                        /*
                         * Handle line ignoring.
                         */
                        if (linesToIgnore > 0) {
                            if (!line.isEmpty()) {
                                linesToIgnore--;
                            }
                            continue;
                        }

                        if (ignoreNextLineIfEmpty) {
                            if (line.isEmpty()) {
                                continue;
                            }
                            ignoreNextLineIfEmpty = false;
                        }

                        Matcher matcher = IGNORED_LINES_PATTERN.matcher(line);
                        if (matcher.find()) {
                            String group = matcher.group(2);
                            if (group == null) {
                                linesToIgnore = 1;
                            } else {
                                linesToIgnore = Integer.parseInt(group);
                            }
                            ignoreNextLineIfEmpty = true;
                            continue;
                        }

                        switch (state) {
                            case BEFORE_CLASS:
                                /*
                                 * Remember imports. If the file is a sketch, the imports will be handled in the end.
                                 */
                                matcher = IMPORT_PATTERN.matcher(line);
                                if (matcher.find()) {

                                    String newImport = matcher.group(1);
                                    if (!imports.contains(newImport) && handledImports.contains(newImport)) {
                                        imports.add(newImport);
                                    }
                                }

                                if (matchesLine(COMMENTS_TO_KEEP_PATTERN, line)) {
                                    if (beforeClassComments == null) {
                                        beforeClassComments = new ArrayList<String>();
                                    }
                                    beforeClassComments.add(line);
                                    break;
                                }

                                /*
                                 * Search for ignore annotations
                                 */
                                if (matchesLine(IGNORE_CLASS_PATTERN, line)) {
                                    /*
                                     * Ingore the complete file
                                     */
                                    break lineLoop;
                                }

                                /*
                                 * Error: Found Enum - has to be converted by hand
                                 */
                                if (matchesLine(ENUM_PATTERN, line)) {
                                    System.err.println("Found enum: " + importFile);
                                    break lineLoop;
                                }

                                /*
                                 * It's a class
                                 */
                                if (matchesLine(CLASS_PATTERN, line)) {
                                    writer = newFileWriter(importFile, "java");
                                    writeHeadComment(writer, beforeClassComments);
                                    writeln(writer, line);
                                    state = ParsingState.INSIDE_CLASS;
                                }
                                break;
                            case INSIDE_CLASS:
                                if (matchesLine(MAIN_METHOD_PATTERN, line)) {
                                    state = ParsingState.MAIN_METHOD;
                                    break;
                                }

                                if (matchesLine(END_CLASS_PATTERN, line)) {
                                    writeln(writer, line);
                                    state = ParsingState.END_CLASS;
                                    break;
                                }

                                writeln(writer, transformCode(line));

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

                importedFiles.add(importFile.getName());
            }
        }

        return importedFiles;
    }

    private static void writeln(BufferedWriter writer, String line) throws IOException {
        if (line == null) {
            return;
        }
        writer.write(line);
        writer.newLine();
    }

//    private static final Format DATE_FORMAT = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);

    private static void writeHeadComment(BufferedWriter writer, List<String> beforeClassComments) throws IOException {
//        writeln(writer, "/*");
//        writeln(writer, " * Generated at " + DATE_FORMAT.format(new Date()));
//        writeln(writer, " */");
        if (beforeClassComments != null) {
            for (String comment : beforeClassComments) {
                writeln(writer, comment);
            }
        }
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

    private void createHtmlFile(File javaFile, List<String> importedFiles) throws IOException {
        BufferedWriter writer = newFileWriter(javaFile, "html");

        BufferedReader htmlTemplate =
                new BufferedReader(new InputStreamReader(
                        JavaToPdeConverter.class.getResourceAsStream("ProcessingJsTemplate.html"),
                        "UTF-8"));
        try {
            String line;
            while ((line = htmlTemplate.readLine()) != null) {
                String name = javaFile.getName();

                StringBuilder builder = new StringBuilder();
                builder.append(name.substring(0, name.length() - 5));
                builder.append(".pde");

                for (String importedFile : importedFiles) {
                    builder.append(" ");
                    builder.append(importedFile);
                }

                line = line.replace("${sketchFile}", builder.toString());
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
        if (line == null) {
            return false;
        }
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
//        File sourceDirectory = new File("src/animations/circleflower2");
        if (sourceDirectory.exists() == false) {
            throw new RuntimeException("src directory not found");
        }
        JavaToPdeConverter converter = new JavaToPdeConverter(sourceDirectory);
        converter.outputStatistic();
    }
}
