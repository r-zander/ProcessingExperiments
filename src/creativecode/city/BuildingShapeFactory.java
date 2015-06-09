package creativecode.city;

import static creativecode.city.GenerativeCity.*;
import static processing.core.PConstants.*;

import java.util.ArrayList;

import processing.core.PShape;
import util.Numbers;
import creativecode.city.BuildingShapeFactory.BuildingShape;

public class BuildingShapeFactory extends ArrayList<BuildingShape> {

    abstract class BuildingShape {

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
    }

    private static final BuildingShapeFactory INSTANCE = new BuildingShapeFactory();

    private int                               totalSum;

    private BuildingShapeFactory() {
        /*
         * Einfaches Rechteck
         * +-+
         * | |
         * +-+
         */
        add(new BuildingShape() {

            @Override
            PShape createShape(float width, float height) {
                return $.createShape(RECT, 0, 0, width, height);
            }

            @Override
            boolean canBeRotated() {
                return false;
            }

            @Override
            float getComplexity() {
                return 4;
            }
        });

        /*
         * Rechteck mit einbuchteter Ecke
         * +--+
         * + ++
         * +-+
         */
        add(new BuildingShape() {

            @Override
            PShape createShape(float width, float height) {
                PShape shape = $.createShape();
                shape.beginShape();
                shape.vertex(0, 0);
                shape.vertex(width, 0);
                shape.vertex(width, height);
                float notchWidth = width * $.random(0.1f, 0.7f);
                shape.vertex(notchWidth, height);
                float notchHeight = height * $.random(0.3f, 0.9f);
                shape.vertex(notchWidth, notchHeight);
                shape.vertex(0, notchHeight);
                shape.endShape(CLOSE);
                return shape;
            }

            @Override
            float getComplexity() {
                return 6;
            }
        });

        /*
         * Viereck mit schräger Kante
         * +--+
         * + /
         * +/
         */
        add(new BuildingShape() {

            @Override
            PShape createShape(float width, float height) {
                PShape shape = $.createShape();
                shape.beginShape();
                shape.vertex(0, 0);
                shape.vertex(width, 0);
                float mainWidth = width * $.random(0.5f, 0.9f);
                shape.vertex(mainWidth, height);
                shape.vertex(0, height);
                shape.endShape(CLOSE);
                return shape;
            }

            @Override
            float getComplexity() {
                return 4;
            }
        });

        /*
         * Viereck mit Spitze am Rand
         * +--/
         * + +
         * +-+
         */
        add(new BuildingShape() {

            @Override
            PShape createShape(float width, float height) {
                PShape shape = $.createShape();
                shape.beginShape();
                shape.vertex(0, 0);
                shape.vertex(width, 0);
                float mainWidth = width * $.random(0.5f, 0.9f);
                float mainHeight = height * $.random(0.5f, 0.8f);
                shape.vertex(mainWidth, height - mainHeight);
                shape.vertex(mainWidth, height);
                shape.vertex(0, height);
                shape.endShape(CLOSE);
                return shape;
            }

            @Override
            float getComplexity() {
                return 5;
            }
        });

        /*
         * Viereck mit Spitze auf Kante
         * +-+
         * + >
         * +-+
         */
        add(new BuildingShape() {

            @Override
            PShape createShape(float width, float height) {
                PShape shape = $.createShape();
                shape.beginShape();
                shape.vertex(0, 0);
                float mainWidth = width * $.random(0.5f, 0.9f);
                shape.vertex(mainWidth, 0);

                float peakOffset = height * $.random(0.2f, 0.3f);
                shape.vertex(mainWidth, peakOffset);

                float peakHeight = height * $.random(0.2f, 0.5f);
                shape.vertex(width, peakOffset + peakHeight / 2);
                shape.vertex(mainWidth, peakOffset + peakHeight);

                shape.vertex(mainWidth, height);
                shape.vertex(0, height);
                shape.endShape(CLOSE);
                return shape;
            }

            @Override
            float getComplexity() {
                return 7;
            }
        });

        /*
         * Zwei Vierecke
         */
        add(new BuildingShape() {

            @Override
            PShape createShape(float width, float height) {
                PShape shape = $.createShape(GROUP);
                float firstHeight = height * $.random(0.3f, 0.7f);
                float distance = height * $.random(0.1f, 0.3f);
                shape.addChild($.createShape(RECT, 0, 0, width, firstHeight - distance));
                shape.addChild($.createShape(RECT, 0, firstHeight, width, height - firstHeight));
                return shape;
            }

            @Override
            float getComplexity() {
                return 8;
            }
        });

        for (BuildingShape shape : this) {
            totalSum += shape.getProbability();
        }
    }

    public static PShape newShape(float width, float height) {
        BuildingShape buildingShape = INSTANCE.getRandomBuildingShape();
        PShape shape = buildingShape.createShape(width, height);

        if (buildingShape.canBeRotated()) {
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

    private BuildingShape getRandomBuildingShape() {
        // http://stackoverflow.com/a/9330493
        int index = Numbers.random(totalSum) + 1;
        int sum = 0;
        int i = 0;
        while (sum < index) {
            BuildingShape item = get(i++);
            sum += item.getProbability();
        }
        return get(i - 1);
    }
}