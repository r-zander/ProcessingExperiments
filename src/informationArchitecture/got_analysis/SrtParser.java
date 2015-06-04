package informationArchitecture.got_analysis;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import processing.core.PApplet;

class SrtParser {

    static final String nl         = "\\\n";

    static final String sp         = "[ \\t]*";

    final Pattern       srtPattern = Pattern.compile("(?s)(\\d+)" + sp + nl
                                           + "(\\d{1,2}):(\\d\\d):(\\d\\d),(\\d\\d\\d)" + sp + "-->" + sp
                                           + "(\\d\\d):(\\d\\d):(\\d\\d),(\\d\\d\\d)" + sp + "(X1:\\d.*?)??" + nl
                                           + "(.*?)" + nl + nl);

    CharSequence fromFile(File filename) {
        String[] lines = PApplet.loadStrings(filename);
        if (lines == null) {
            return null;
        }

        final StringBuilder joinedLines = new StringBuilder(lines.length * 16);
        boolean firstLine = true;
        for (String line : lines) {
            if (!firstLine) {
                joinedLines.append("\n");
            } else {
                firstLine = false;
            }
            if (line != null) {
                joinedLines.append(line);
            }
        }
        return joinedLines;
    }

    List<Subtitle> readSrtFile(File file) {
        final CharSequence joinedLines = fromFile(file);

        if (joinedLines == null) {
            PApplet.println("WARNING: joinedLines == null for '" + file + "'");
            return Collections.emptyList();
        }

        ArrayList<Subtitle> subtitles = new ArrayList<Subtitle>();

        Matcher matcher = srtPattern.matcher(joinedLines);

        while (matcher.find()) {
            // group 2, 3, 4, and 5 is start time
            // group 6, 7, 8, and 9 is finish time
            // group 11 is subtitle text

            Subtitle subtitle = new Subtitle();
            subtitle.index = Integer.parseInt(matcher.group(1));

            subtitle.start.hour = Integer.parseInt(matcher.group(2));
            subtitle.start.minute = Integer.parseInt(matcher.group(3));
            subtitle.start.second = Integer.parseInt(matcher.group(4));
            subtitle.start.milli = Integer.parseInt(matcher.group(5));

            subtitle.end.hour = Integer.parseInt(matcher.group(6));
            subtitle.end.minute = Integer.parseInt(matcher.group(7));
            subtitle.end.second = Integer.parseInt(matcher.group(8));
            subtitle.end.milli = Integer.parseInt(matcher.group(9));

            subtitle.text = matcher.group(11);

            subtitles.add(subtitle);
        }

        return subtitles;
    }

}
