package creativecode.generativeCity;

import static processing.core.PConstants.*;
import processing.core.PShape;
import util.Numbers;

public abstract class BuildingShape {

    float maxArea;

    abstract PShape createShape(float width, float height);

    /**
     * Complexity of the shape to determine the minimum reasonable size.
     * 
     * @return usually the number of vertices
     */
    abstract float getComplexity();

    int getProbability() {
        return 1;
    }

    boolean canBeRotated() {
        return true;
    }

    public PShape newShape(float width, float height) {
        PShape shape = createShape(width, height);

        if (canBeRotated()) {
            int random = Numbers.random(0, 3);
            switch (random) {
                case 1:
                    shape.translate(width, 0);
                    break;
                case 2:
                    shape.translate(width, height);
                    break;
                case 3:
                    shape.translate(0, height);
                    break;
                default:
                    break;
            }
            shape.rotate(random * HALF_PI);
        }

        return shape;
    }
}