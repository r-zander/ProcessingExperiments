package generativeLogo;

import java.util.HashMap;
import java.util.Map;

import processing.core.PApplet;
import util.TwoDimensional;

public class LetterCoding extends PApplet {

    int                    bgColor    = 0xFFFFFFFF;

    int                    fgColor    = 0xFF000000;

    final int              gridHeight = 400;

    final int              gridWidth  = 400;

    int                    color;

    Map<Character, Letter> alphabet;

    class Letter {

        char  letter;

        float x;

        float y;

        public Letter(char letter) {
            this.letter = letter;
        }
    }

    @Override
    public void setup() {
        size(gridWidth, gridHeight + 100);
        frameRate(8);
        colorMode(HSB);
        color = color(random(255), 255, 255);

        alphabet = new HashMap<Character, Letter>(30);
        for (int i = 0; i <= 29; i++) {
            char c = getLetter(i);
            alphabet.put(c, new Letter(c));
        }

        for (int row = 1; row <= 6; row++) {
            for (int col = 1; col <= 5; col++) {
                int index = (row - 1) * 5 + col - 1;
                Letter letter = alphabet.get(getLetter(index));

                letter.x = gridWidth / 5f * (col - 0.5f);
                letter.y = gridHeight / 6f * (row - 0.5f);
            }
        }

        clean();
    }

    private void clean() {
        background(bgColor);
        drawGrid();
        stroke(fgColor);
        strokeWeight(1);
        line(0, gridHeight, width, gridHeight);
    }

    char getLetter(int index) {
        if (index <= 25) {
            return (char) ('A' + index);
        } else {
            switch (index) {
                case 26:
                    return 'ß';
                case 27:
                    return 'Ä';
                case 28:
                    return 'Ö';
                case 29:
                    return 'Ü';
            }
        }
        return (char) 0;
    }

    private void drawGrid() {
        fill(fgColor);
        for (Letter letter : alphabet.values()) {
            text(letter.letter, letter.x, letter.y);
        }
    }

    @Override
    public void draw() {

    }

    char  currentLetter;

    char  lastLetter;

    float cursorX = 20;

    @Override
    public void keyPressed() {
        if (key == currentLetter) {
            return;
        }
        if (key != CODED && alphabet.containsKey(Character.toUpperCase(key))) {
            currentLetter = key;
            drawLetter();
            fill(fgColor);
            text(key, cursorX, gridHeight + 50);
            cursorX += 10;
        } else {
            switch (key) {
                case ' ':
                    color = color(random(255), 255, 255);
                    cursorX = 20;
                    fill(bgColor);
                    noStroke();
                    rect(0, gridHeight + 1, width, height);
                    lastLetter = 0;
                    break;
//                case CODED:
//                    switch (keyCode) {
                case RETURN:
                case ENTER:
                    color = color(random(255), 255, 255);
                    cursorX = 20;
                    lastLetter = 0;
                    clean();
                    break;
//                    }
            }
        }
    }

    @Override
    public void keyReleased() {
        if (alphabet.containsKey(Character.toUpperCase(currentLetter))) {
            lastLetter = currentLetter;
        }
        currentLetter = 0;
    }

    float arcDirection = HALF_PI;

    private void drawLetter() {
        if (lastLetter == 0) {
            fill(color);
            noStroke();
            Letter letter = alphabet.get(Character.toUpperCase(currentLetter));
            ellipse(letter.x, letter.y, 30, 30);
        } else {

            Letter letter1 = alphabet.get(Character.toUpperCase(currentLetter));
            Letter letter2 = alphabet.get(Character.toUpperCase(lastLetter));

            stroke(color);
            noFill();
            strokeWeight(30);
//            line(letter1.x, letter1.y, letter2.x, letter2.y);

            float angle = TwoDimensional.angleBetween(letter1.x, letter1.y, letter2.x, letter2.y) + arcDirection;
            float distance = dist(letter1.x, letter1.y, letter2.x, letter2.y) / 2;
            float controllX = (letter1.x + letter2.x) / 2 + cos(angle) * distance;
            float controllY = (letter1.y + letter2.y) / 2 + sin(angle) * distance;
            bezier(letter1.x, letter1.y, controllX, controllY, controllX, controllY, letter2.x, letter2.y);

            arcDirection = -arcDirection;
        }
    }

    public static void main(String[] args) {
        PApplet.main(new String[] { "--present", LetterCoding.class.getName() });
    }
}