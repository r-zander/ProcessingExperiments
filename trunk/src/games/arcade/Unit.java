package games.arcade;

import static games.arcade.ArcadeConstants.*;
import static processing.core.PApplet.*;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import processing.core.PApplet;
import processing.core.PConstants;

@Data
@Accessors(chain = true)
public class Unit extends Block {

    @Setter(AccessLevel.NONE)
    private State            state;

    /*
     * Number of frames the state is kept.
     */
    private int              stateFrames;

    private static final int JUMP_FRAMES       = 20;

    private int              jumpStrength;

    private static final int MAX_JUMP_STRENGTH = 15;

    public Unit(PApplet app) {
        super(app, Shape.ELLIPSE);

        width(50);
        height(50);
    }

    public void resetPosition() {
        state = State.AFTER_JUMP_FALLING;
        bottom(app().height - 300);
        left(150);
    }

    public void jump() {
        System.out.println("jump");
        if (state == State.JUMPING) {
            if (jumpStrength < MAX_JUMP_STRENGTH) {
                jumpStrength++;
            }
        } else if (state == State.AFTER_JUMP_FALLING) {
            // NOP
        } else {
            updateState(State.JUMPING);
            jumpStrength = 1;
        }
    }

    public State update(Direction gravityDirection, List<Block> blocks) {
        stateFrames++;
        /*
         * Check if the unit is still inside game boundaries
         */
        if (right() < 0 || left() > app().width || bottom() < 0 || top() > app().height) {
            return updateState(State.DEAD);
        }

        if (state == State.JUMPING) {
            if (stateFrames > JUMP_FRAMES) {
                return updateState(State.AFTER_JUMP_FALLING);
            }
            float mappedStrength = map(jumpStrength, 4, MAX_JUMP_STRENGTH, 2, 6);
            System.out.println("Frame: " + stateFrames);
            System.out.println(jumpStrength + " : " + mappedStrength);
            offset(gravityDirection.invert(), mappedStrength * (-0.692f * stateFrames + 11.765f));
            draw();
            return state;
        }

        /*
         * Calculate collisions.
         */
        for (Block block : blocks) {
            Vector testCircleAABB = getCollisionVector(this, block);
            if (testCircleAABB != null) {
                Direction collisionSide = vectorToDirection(testCircleAABB);
                highlightCollisionSide(block, collisionSide);

                offset(collisionSide, abs(testCircleAABB.getLength() - width() / 2));
                draw();
                return updateState(State.WALKING);
            }
        }

        /*
         * No collisions - falling
         */
        if (state != State.AFTER_JUMP_FALLING) {
            updateState(State.FALLING);
        }
        offset(gravityDirection, GRAVITY * stateFrames /* * stateFrames * stateFrames */);

        draw();

        return state;
    }

    private void highlightCollisionSide(Block block, Direction collisionSide) {
        app().stroke(0xffffffff);
        switch (collisionSide) {
            case TOP:
                app().line(block.left(), block.top(), block.right(), block.top());
                break;
            case RIGHT:
                app().line(block.right(), block.top(), block.right(), block.bottom());
                break;
            case BOTTOM:
                app().line(block.left(), block.bottom(), block.right(), block.bottom());
                break;
            case LEFT:
                app().line(block.left(), block.top(), block.left(), block.bottom());
                break;
        }
    }

    public static Direction vectorToDirection(Vector vector) {
        if (abs(vector.getX()) > abs(vector.getY())) {
            if (vector.getX() > 0) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (vector.getY() > 0) {
                return Direction.BOTTOM;
            } else {
                return Direction.TOP;
            }
        }
    }

    public static Direction angleToDirection(float radians) {
        radians %= PConstants.TWO_PI;
        if (radians >= PConstants.QUARTER_PI) {
            if (radians < PConstants.HALF_PI + PConstants.QUARTER_PI) {
                return Direction.BOTTOM;
            } else if (radians < PConstants.PI + PConstants.QUARTER_PI) {
                return Direction.LEFT;
            } else if (radians < PConstants.PI + PConstants.HALF_PI + PConstants.QUARTER_PI) {
                return Direction.TOP;
            } else {
                return Direction.RIGHT;
            }
        } else {
            return Direction.RIGHT;
        }
    }

    public static Vector vectorDistPointAABB(final Vector p, final Block aabb) {
        Vector vector = new Vector();
        float v;
        float minX, minY, maxX, maxY;

        // get the minX, maxX, minY and maxY points of the AABB
        minX = aabb.centerX() - aabb.width() / 2;
        maxX = aabb.centerX() + aabb.width() / 2;

        minY = aabb.centerY() - aabb.height() / 2;
        maxY = aabb.centerY() + aabb.height() / 2;

        // test the bounds against the points X axis
        v = p.getX();

        if (v < minX)
            vector.setX(v - minX);
        if (v > maxX)
            vector.setX(v - maxX);

        // test the bounds against the points Y axis
        v = p.getY();

        if (v < minY)
            vector.setY(v - minY);
        if (v > maxY)
            vector.setY(v - maxY);

        return vector;
    }

    public static Vector getCollisionVector(final Block circle, final Block box) {
        // get the squared distance between circle center and the AABB
        Vector v = vectorDistPointAABB(new Vector(circle.centerX(), circle.centerY()), box);
        float r = circle.width() / 2;

        if (v.getSqLength() <= r * r) {
            return v;
        }
        return null;
    }

    private State updateState(State state) {
        if (this.state != state) {
            this.state = state;
            stateFrames = 0;
        }
        return state;
    }

    public static enum State {
        JUMPING,
        AFTER_JUMP_FALLING,
        FALLING,
        WALKING,
        DEAD;
    }

}
