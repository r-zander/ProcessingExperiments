package generativeLogo.zr_logo;

public class ConstructionArc extends Element {

    public final Line constructedLine;

    private float     startAngle;

    private float     endAngle;

    public ConstructionArc(Line constructedLine, float startAngle) {
        super(constructedLine.parent);
        persistent(false);
        this.constructedLine = constructedLine;
        if (constructedLine.angle < startAngle) {
            this.startAngle = startAngle - ZR_Logo.TWO_PI;
            this.endAngle = constructedLine.angle;
        } else {
            this.startAngle = startAngle;
            this.endAngle = constructedLine.angle;
        }

        float length = constructedLine.length * (endAngle - startAngle);
        calculateFrameCount(length);
    }

    @Override
    public void drawShape(float percentage) {
        parent.arc(
                constructedLine.startX,
                constructedLine.startY,
                constructedLine.length * 2,
                constructedLine.length * 2,
                startAngle,
                startAngle + (endAngle - startAngle) * percentage);
    }
}