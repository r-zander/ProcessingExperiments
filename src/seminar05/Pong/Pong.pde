/*
 * Allgemeine Anmerkungen:
 *   - 'static final' Variablen in GROSSBUCHSTABEN sind Konstanten, die nie ihren Wert ändern.
 */

static final color  BACKGROUND_COLOR = #000000;

static final color  FOREGROUND_COLOR = #00FF00;

static final int  BASE_SPEED       = 20;

int               centerX;

int               centerY;

class Board {

	static final int MARGIN      = 100;

	/**
	 * Interessanter Parameter zu ändern während des Spiels
	 */
	static final int HEIGHT      = 200;

	static final int HALF_HEIGHT = HEIGHT / 2;

	static final int WIDTH       = 50;

	float           xPos;

	float           yPos;

	Board(float xPos, float yPos) {
		this.xPos = xPos;
		this.yPos = yPos;
	}

	float xPos() {
		return xPos;
	}

	float yPos() {
		return yPos;
	}

	float centerY() {
		return yPos + Board.HALF_HEIGHT;
	}

	void yPos(float yPos) {
		this.yPos = constrain(yPos, 0, height - HEIGHT);
	}

	void draw() {
		rect(xPos, yPos, WIDTH, HEIGHT);
	}

}

/**
 * The Board controlled by keyboard.
 */
class KeyBoard extends Board {

	static final float SPEED = BASE_SPEED * 1.5f;

	KeyBoard(float xPos, float yPos) {
		super(xPos, yPos);
	}

	void up() {
		yPos(yPos() - SPEED);
	}

	void down() {
		yPos(yPos() + SPEED);
	}

	/**
	 * Considers the special position of being left.
	 */
	float xPos() {
		return super.xPos() + WIDTH;
	}
}

Board    mouseBoard;

KeyBoard keyBoard;

class Ball {

	static final int    RADIUS               = 25;

	static final int    DIAMETER             = RADIUS * 2;

	static final float SPEED_LIMIT          = BASE_SPEED;

	static final float SPEED_ADDITION_LIMIT = BASE_SPEED * .25f;

	float              xPos;

	float              yPos;

	float              xSpeed;

	float              ySpeed;

	Ball() {
		reset();
	}

	void reset() {
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

	void draw() {
		ellipse(xPos, yPos, DIAMETER, DIAMETER);
	}

	float getAbsoluteSpeed() {
		return abs(xSpeed) + abs(ySpeed);
	}
}

Ball ball;

class Player {

	int   score = 0;

	float scoreX;

	float scoreY;

	Player(float scoreX, float scoreY) {
		this.scoreX = scoreX;
		this.scoreY = scoreY;
	}

	void increaseScore() {
		score++;
	}

	void drawScore() {
		textSize(40);
		text(score, scoreX, scoreY);
	}
}

Player  mousePlayer;

Player  keyboardPlayer;

boolean gamePaused = true;

int     aiLevel    = 0;

void setup() {
	/*
	 * Full screen application
	 */
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

	/*
	 * Hide the mouse cursor - because it's a game.
	 */
    noCursor();
}

/*
 * Full screen application
 */
boolean sketchFullScreen() {
    return true;
}

void keyPressed() {
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

void draw() {
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
}

void drawBallSpeed() {
	fill(color(0, 255, 128));
	textAlign(CENTER);
	text(ball.getAbsoluteSpeed(), centerX, height - 100);
	fill(FOREGROUND_COLOR);
}

void drawPauseInfo() {
	textAlign(CENTER);
	text("<Leertaste> um loszulegen!", centerX, centerY + 100);
}

void drawAiInfo() {
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

void drawBoardPlayerMouse() {
	mouseBoard.yPos(mouseY - Board.HEIGHT / 2);
	mouseBoard.draw();
}

void drawBoardPlayerKeyboard() {
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

boolean isAiEnabled() {
	return aiLevel > 0;
}

void drawBall() {
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
			/*
			 * Possible collision with right (mouse controlled) board.
			 */
			checkBoardCollision(mouseBoard);
		} else if (ball.xPos - Ball.RADIUS <= keyBoard.xPos()) {
			/*
			 * Possible collision with left (keyboard / ai controlled) board.
			 */
			checkBoardCollision(keyBoard);
		}
	}
	ball.draw();
}

void checkBoardCollision(Board board) {
	float distance = ball.yPos - board.yPos();

	if (distance >= -Ball.RADIUS && distance <= Board.HEIGHT + Ball.RADIUS) {
		deflectBall(abs(distance - Board.HALF_HEIGHT) / Board.HALF_HEIGHT);
	}
}

/**
 * @param percentage
 *            of x/y speed gain distribution. 0 means 100% x speed gain.
 */
void deflectBall(float percentage) {
	ball.xSpeed = -ball.xSpeed;

	boolean xPositive = ball.xSpeed > 0;
	boolean yPositive = ball.ySpeed > 0;
	float totalSpeed = ball.getAbsoluteSpeed() + random(Ball.SPEED_ADDITION_LIMIT);

	ball.xSpeed = xPositive ? totalSpeed * (1 - percentage) : -totalSpeed * (1 - percentage);
	ball.ySpeed = yPositive ? totalSpeed * percentage : -totalSpeed * percentage;
}

void mouseScored() {
	mousePlayer.increaseScore();
	afterScored();
}

void keyboardScored() {
	keyboardPlayer.increaseScore();
	afterScored();
}

void afterScored() {
	ball.reset();
	gamePaused = true;
}
