package generativeLogo.zr_logo;

import java.util.Arrays;
import java.util.List;

public class CompoundElement extends Element {

    List<Element> elements;

    public CompoundElement(Element... elements) {
        super(elements[0].parent);
        this.elements = Arrays.asList(elements);
        /*
         * Apply inherited persistence to children.
         */
        persistent(persistent);
    }

    @Override
    public Element persistent(boolean persistent) {
        super.persistent(persistent);
        if (elements == null) {
            return this;
        }
        for (Element element : elements) {
            element.persistent(persistent);
        }
        return this;
    }

    @Override
    public void draw(float percentage) {
        for (Element element : elements) {
            element.draw(percentage);
        }
    }

    @Override
    protected void drawShape(float percentage) {
        // NOP;
    }
}