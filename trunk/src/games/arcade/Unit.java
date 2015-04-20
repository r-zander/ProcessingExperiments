package games.arcade;

import static games.arcade.ArcadeConstants.*;
import static java.lang.Math.*;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import processing.core.PApplet;

@Data
@Accessors(chain = true)
public class Unit extends Block {

    @Setter(AccessLevel.NONE)
    private State state;

    /*
     * Number of frames the state is kept.
     */
    private int   stateFrames;

    public Unit(PApplet app) {
        super(app, Shape.ELLIPSE);

        width(50);
        height(50);
        bottom(app.height - 50);
        left(150);
    }

    public void jump(Direction gravityDirection) {
        updateState(State.JUMPING);
    }

    public State update(Direction gravityDirection, List<Block> blocks) {
        stateFrames++;
        /*
         * Check if the unit is still inside game boundaries
         */
        if (right() < 0 || left() > app().width || bottom() < 0 || top() > app().height) {
            return updateState(State.DEAD);
        }

        /*
         * Calculate collisions.
         */
        for (Block block : blocks) {
//            Direction collisionDirection = null;
//            if (right() > block.left() && left() < block.right() ){
//            if (top() > block.bottom()) {
//                collisionDirection = Direction.BOTTOM;
//            } else if (bottom() < block.top()) {
//                collisionDirection = Direction.TOP;
//            }
//            } if (bottom() > block.top() && top() <){
//            else if (right() < block.left()) {
//                collisionDirection = Direction.LEFT;
//            } else if (left() > block.right()) {
//                collisionDirection = Direction.RIGHT;
//            }
            if (isCollidingCircleRectangle(
                    centerX(),
                    centerY(),
                    width(),
                    block.x(),
                    block.y(),
                    block.width(),
                    block.height())) {
                draw();
                return updateState(State.WALKING);
            }
        }

        /*
         * No collisions - falling
         */
        offset(gravityDirection, GRAVITY /* * stateFrames * stateFrames */);

        draw();

        return updateState(State.FALLING);
    }

    boolean isCollidingCircleRectangle(float circleX, float circleY, float radius, float rectangleX, float rectangleY,
            float rectangleWidth, float rectangleHeight) {
        float circleDistanceX = abs(circleX - rectangleX - rectangleWidth / 2);
        float circleDistanceY = abs(circleY - rectangleY - rectangleHeight / 2);

        if (circleDistanceX > (rectangleWidth / 2 + radius)) {
            return false;
        }
        if (circleDistanceY > (rectangleHeight / 2 + radius)) {
            return false;
        }

        if (circleDistanceX <= (rectangleWidth / 2)) {
            return true;
        }
        if (circleDistanceY <= (rectangleHeight / 2)) {
            return true;
        }

        double cornerDistance_sq =
                pow(circleDistanceX - rectangleWidth / 2, 2) + pow(circleDistanceY - rectangleHeight / 2, 2);

        return (cornerDistance_sq <= pow(radius, 2));
    }

    private State updateState(State state) {
        if (this.state != state) {
            this.state = state;
            stateFrames = 0;
        }
        return state;
    }

    private static enum State {
        JUMPING,
        FALLING,
        WALKING,
        DEAD;
    }

}
