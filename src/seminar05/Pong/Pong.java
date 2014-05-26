package seminar05.Pong;

import processing.core.PApplet;

public class Pong extends PApplet {

    private static final long serialVersionUID = -56589606646834162L;

    private static final int  BACKGROUND_COLOR = 0xFF000000;

    private static final int  FOREGROUND_COLOR = 0xFF00FF00;

    private static final int  BASE_SPEED       = 20;

    private int               centerX;

    private int               centerY;

    private class Board {

        public static final int MARGIN      = 100;

        /**
         * Interessanter Parameter zu ändern während des Spiels
         */
        public static final int HEIGHT      = 200;

        public static final int HALF_HEIGHT = HEIGHT / 2;

        public static final int WIDTH       = 50;

        private float           xPos;

        private float           yPos;

        public Board(float xPos, float yPos) {
            this.xPos = xPos;
            this.yPos = yPos;
        }

        public float xPos() {
            return xPos;
        }

        public float yPos() {
            return yPos;
        }

        public float centerY() {
            return yPos + Board.HALF_HEIGHT;
        }

        public void yPos(float yPos) {
            this.yPos = constrain(yPos, 0, height - HEIGHT);
        }

        public void draw() {
            rect(xPos, yPos, WIDTH, HEIGHT);
        }

    }

    /**
     * The Board controlled by keyboard.
     */
    private class KeyBoard extends Board {

        public static final float SPEED = BASE_SPEED * 1.5f;

        public KeyBoard(float xPos, float yPos) {
            super(xPos, yPos);
        }

        public void up() {
            yPos(yPos() - SPEED);
        }

        public void down() {
            yPos(yPos() + SPEED);
        }

        /**
         * Considers the special position of being left.
         */
        @Override
        public float xPos() {
            return super.xPos() + WIDTH;
        }
    }

    private Board    mouseBoard;

    private KeyBoard keyBoard;

    private class Ball {

        public static final int    RADIUS               = 25;

        public static final int    DIAMETER             = RADIUS * 2;

        private static final float SPEED_LIMIT          = BASE_SPEED;

        private static final float SPEED_ADDITION_LIMIT = BASE_SPEED * .25f;

        private float              xPos;

        private float              yPos;

        private float              xSpeed;

        private float              ySpeed;

        public Ball() {
            reset();
        }

        public void reset() {
            xPos = centerX;
            yPos = centerY;
            ySpeed = random(SPEED_LIMIT / 6, SPEED_LIMIT / 3);
            if (random(1) >= .5) {
                ySpeed = -ySpeed;
            }
            xSpeed = SPEED_LIMIT - ySpeed;
            if (random(1) >= .5) {
                xSpeed = -xSpeed;
            }
            if (random(1) >= .5) {
                ySpeed = -ySpeed;
            }
        }

        public void draw() {
            ellipse(xPos, yPos, DIAMETER, DIAMETER);
        }

        public float getAbsoluteSpeed() {
            return abs(xSpeed) + abs(ySpeed);
        }
    }

    private Ball ball;

    private class Player {

        private int   score = 0;

        private float scoreX;

        private float scoreY;

        public Player(float scoreX, float scoreY) {
            this.scoreX = scoreX;
            this.scoreY = scoreY;
        }

        public void increaseScore() {
            score++;
        }

        public void drawScore() {
            textSize(40);
            text(score, scoreX, scoreY);
        }
    }

    private Player  mousePlayer;

    private Player  keyboardPlayer;

    private boolean gamePaused = true;

    private int     aiLevel    = 0;

    @Override
    public void setup() {
//        frameRate(20);
        size(displayWidth, displayHeight);
        /*
         * Everything will be in foreground color.
         */
        fill(FOREGROUND_COLOR);
        noStroke();

        centerX = width / 2;
        centerY = height / 2;

        mouseBoard = new Board(width - Board.MARGIN, centerY);
        keyBoard = new KeyBoard(Board.MARGIN - Board.WIDTH, centerY);

        ball = new Ball();

        mousePlayer = new Player(width * .75f, 200);
        keyboardPlayer = new Player(width * .25f, 200);

//        noCursor();
    }

    @Override
    public void keyPressed() {
        switch (key) {
            case ' ':
                gamePaused = !gamePaused;
                break;
            case 'a':
                if (isAiEnabled()) {
                    aiLevel = 0;
                } else {
                    aiLevel = 5;
                }
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                aiLevel = Character.getNumericValue(key);
                break;
            default:
                break;
        }
    }

    @Override
    public void draw() {
        fill(BACKGROUND_COLOR, 20);
        rect(0, 0, width, height);
        fill(FOREGROUND_COLOR);

        drawBoardPlayerMouse();
        drawBoardPlayerKeyboard();

        drawBall();
        drawBallSpeed();

        mousePlayer.drawScore();
        keyboardPlayer.drawScore();

        if (gamePaused) {
            drawPauseInfo();
        }

        drawAiInfo();

//        stroke(255);
//        line(keyBoard.xPos(), 0, keyBoard.xPos(), height);
//        line(mouseBoard.xPos(), 0, mouseBoard.xPos(), height);
//        noStroke();
    }

    private void drawBallSpeed() {
        fill(color(0, 255, 128));
        textAlign(CENTER);
        text(ball.getAbsoluteSpeed(), centerX, height - 100);
        fill(FOREGROUND_COLOR);
    }

    private void drawPauseInfo() {
        textAlign(CENTER);
        text("<Leertaste> um loszulegen!", centerX, centerY + 100);
    }

    private void drawAiInfo() {
        fill(color(0, 255, 128));
        String aiStatus;
        if (aiLevel > 0) {
            aiStatus = "<A>I Level <" + aiLevel + ">";
        } else {
            aiStatus = "<A>I deaktviert";
        }
        textAlign(LEFT);
        text(aiStatus, keyBoard.xPos(), height - 100);
        fill(FOREGROUND_COLOR);
    }

    private void drawBoardPlayerMouse() {
        mouseBoard.yPos(mouseY - Board.HEIGHT / 2);
        mouseBoard.draw();
    }

    private void drawBoardPlayerKeyboard() {
        /*
         * AI can control the keyboard, if enabled.
         */
        if (isAiEnabled()) {
            /*
             * Only follow the ball if its on our site of the board.
             */
            if (ball.xPos < width / 10f * (aiLevel + 1)) {
                if (ball.yPos > keyBoard.centerY()) {
                    keyBoard.down();
                } else if (ball.yPos < keyBoard.centerY()) {
                    keyBoard.up();
                }
            }
        } else {
            if (keyPressed) {
                if (key == CODED) {
                    if (keyCode == UP) {
                        keyBoard.up();
                    } else if (keyCode == DOWN) {
                        keyBoard.down();
                    }
                } else if (key == 'w') {
                    keyBoard.up();
                } else if (key == 's') {
                    keyBoard.down();
                }
            }
        }
        keyBoard.draw();
    }

    private boolean isAiEnabled() {
        return aiLevel > 0;
    }

    private void drawBall() {
        if (!gamePaused) {
            ball.xPos += ball.xSpeed;
            ball.yPos += ball.ySpeed;

            if (ball.xPos - Ball.RADIUS < 0) {
                /*
                 * Collision with left wall.
                 */
                mouseScored();
            } else if (ball.xPos + Ball.RADIUS > width) {
                /*
                 * Collision with right wall.
                 */
                keyboardScored();
            } else if (ball.yPos - Ball.RADIUS < 0 || ball.yPos + Ball.RADIUS > height) {
                /*
                 * Collision with top/bottom wall.
                 */
                ball.ySpeed = -ball.ySpeed;
            } else if (ball.xPos + Ball.RADIUS >= mouseBoard.xPos()) {
                checkBoardCollision(mouseBoard);
            } else if (ball.xPos - Ball.RADIUS <= keyBoard.xPos()) {
                checkBoardCollision(keyBoard);
            }
        }
        ball.draw();
    }

    private void checkBoardCollision(Board board) {
        float distance = ball.yPos - board.yPos();

        if (distance >= -Ball.RADIUS && distance <= Board.HEIGHT + Ball.RADIUS) {
            deflectBall(abs(distance - Board.HALF_HEIGHT) / Board.HALF_HEIGHT);
        }
    }

    /**
     * @param percentage
     *            of x/y speed gain distribution. 0 means 100% x speed gain.
     */
    private void deflectBall(float percentage) {
//        info("Deflect: " + 100 * percentage + "%");
        ball.xSpeed = -ball.xSpeed;

        boolean xPositive = ball.xSpeed > 0;
        boolean yPositive = ball.ySpeed > 0;
        float totalSpeed = ball.getAbsoluteSpeed() + random(Ball.SPEED_ADDITION_LIMIT);

        ball.xSpeed = xPositive ? totalSpeed * (1 - percentage) : -totalSpeed * (1 - percentage);
        ball.ySpeed = yPositive ? totalSpeed * percentage : -totalSpeed * percentage;

//        float additionalSpeed = random(Ball.SPEED_ADDITION_LIMIT);
//        float additionalSpeedX = (1 - percentage) * additionalSpeed;
//        float additionalSpeedY = additionalSpeed - additionalSpeedX;
//
//        if (ball.xSpeed > 0) {
//            ball.xSpeed += additionalSpeedX;
//        } else {
//            ball.xSpeed -= additionalSpeedX;
//        }
//        if (ball.ySpeed > 0) {
//            ball.ySpeed += additionalSpeedY;
//        } else {
//            ball.ySpeed -= additionalSpeedY;
//        }
    }

    private void mouseScored() {
        mousePlayer.increaseScore();
        afterScored();
    }

    private void keyboardScored() {
        keyboardPlayer.increaseScore();
        afterScored();
    }

    private void afterScored() {
        ball.reset();
        gamePaused = true;
    }

    @SuppressWarnings("unused")
    private void info(String msg) {
        fill(color(255, 192, 0));
        msg(msg);
    }

    @SuppressWarnings("unused")
    private void warn(String msg) {
        fill(color(0, 255, 0));
        msg(msg);
    }

    private void msg(String msg) {
        text(msg, centerX, 200);
        fill(FOREGROUND_COLOR);
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", Pong.class.getName() });
    }
}