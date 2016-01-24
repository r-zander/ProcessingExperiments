package informationArchitecture.globeVisualization;

import peasy.PeasyCam;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.Table;
import processing.data.TableRow;

public class GlobeVisualization extends PApplet {

    // VARIABLES
    PImage   bg;

    PImage   texmap;

    int      sDetail          = 32;                              // Sphere detail setting

    float    globeRadius      = 300;

    float[]  cx, cz, sphereX, sphereY, sphereZ;

    float    sinLUT[];

    float    cosLUT[];

    float    SINCOS_PRECISION = 0.5f;

    int      SINCOS_LENGTH    = (int) (360.0 / SINCOS_PRECISION);

    // CAM
    PeasyCam cam;

    City[]   cities           = new City[84];

    @Override
    public void setup() {
        size(800, 800, OPENGL);
        background(255);

        // GLOBE
        texmap = loadImage("globeVisualization/world_large.jpg");
        initializeSphere(sDetail);

        cam = new PeasyCam(this, 800);
        cam.setMinimumDistance(400);
        cam.setMaximumDistance(1000);

        cam.rotateY(PI * 0.5);

        initCities();
    }

    void initCities() {

        Table table = loadTable("globeVisualization/cities.tsv", "header");

        int i = 0;
        for (TableRow row : table.rows()) {
            City city = new City();
            city.setName(row.getString("Name"));
            city.setCountry(row.getString("Country"));
            city.setPopulation(row.getLong("Population"));
            city.setLat(row.getFloat("Lat"));
            city.setLng(row.getFloat("Lng"));

            computeCoordinates(city);
            cities[i] = city;
            i++;

            println(city);
        }

    }

    @Override
    public void draw() {
        background(0);
        renderGlobe();
        drawCities();
    }

    private void drawCities() {
        stroke(255, 255, 0);
        for (City city : cities) {
            pushMatrix();
            translate(city.getX(), city.getY(), city.getZ());
            sphere(city.getPopulation() / 1000000);
            popMatrix();
        }
    }

    private void computeCoordinates(City city) {
        float angleNorthSouth = radians(city.getLat());
        float angleEastWest = radians(city.getLng());

        city.setX(globeRadius * cos(angleNorthSouth) * cos(angleEastWest));
        city.setY(-globeRadius * sin(angleNorthSouth));
        city.setZ(-globeRadius * cos(angleNorthSouth) * sin(angleEastWest));
    }

    void renderGlobe() {
        noStroke();
        textureMode(IMAGE);
        texturedSphere(globeRadius, texmap);
    }

    void initializeSphere(int res) {
        sinLUT = new float[SINCOS_LENGTH];
        cosLUT = new float[SINCOS_LENGTH];

        for (int i = 0; i < SINCOS_LENGTH; i++) {
            sinLUT[i] = (float) Math.sin(i * DEG_TO_RAD * SINCOS_PRECISION);
            cosLUT[i] = (float) Math.cos(i * DEG_TO_RAD * SINCOS_PRECISION);
        }

        float delta = (float) SINCOS_LENGTH / res;
        float[] cx = new float[res];
        float[] cz = new float[res];

        // Calc unit circle in XZ plane
        for (int i = 0; i < res; i++) {
            cx[i] = -cosLUT[(int) (i * delta) % SINCOS_LENGTH];
            cz[i] = sinLUT[(int) (i * delta) % SINCOS_LENGTH];
        }

        // Computing vertexlist vertexlist starts at south pole
        int vertCount = res * (res - 1) + 2;
        int currVert = 0;

        // Re-init arrays to store vertices
        sphereX = new float[vertCount];
        sphereY = new float[vertCount];
        sphereZ = new float[vertCount];
        float angle_step = (SINCOS_LENGTH * 0.5f) / res;
        float angle = angle_step;

        // Step along Y axis
        for (int i = 1; i < res; i++) {
            float curradius = sinLUT[(int) angle % SINCOS_LENGTH];
            float currY = -cosLUT[(int) angle % SINCOS_LENGTH];
            for (int j = 0; j < res; j++) {
                sphereX[currVert] = cx[j] * curradius;
                sphereY[currVert] = currY;
                sphereZ[currVert++] = cz[j] * curradius;
            }
            angle += angle_step;
        }
        sDetail = res;
    }

    // Generic routine to draw textured sphere
    void texturedSphere(float r, PImage t) {
        int v1, v11, v2;

        beginShape(TRIANGLE_STRIP);
        texture(t);
        float iu = (float) (t.width - 1) / (sDetail);
        float iv = (float) (t.height - 1) / (sDetail);
        float u = 0, v = iv;
        for (int i = 0; i < sDetail; i++) {
            vertex(0, -r, 0, u, 0);
            vertex(sphereX[i] * r, sphereY[i] * r, sphereZ[i] * r, u, v);
            u += iu;
        }
        vertex(0, -r, 0, u, 0);
        vertex(sphereX[0] * r, sphereY[0] * r, sphereZ[0] * r, u, v);
        endShape();

        // Middle rings
        int voff = 0;
        for (int i = 2; i < sDetail; i++) {
            v1 = v11 = voff;
            voff += sDetail;
            v2 = voff;
            u = 0;
            beginShape(TRIANGLE_STRIP);
            texture(t);
            for (int j = 0; j < sDetail; j++) {
                vertex(sphereX[v1] * r, sphereY[v1] * r, sphereZ[v1++] * r, u, v);
                vertex(sphereX[v2] * r, sphereY[v2] * r, sphereZ[v2++] * r, u, v + iv);
                u += iu;
            }

            // Close each ring
            v1 = v11;
            v2 = voff;
            vertex(sphereX[v1] * r, sphereY[v1] * r, sphereZ[v1] * r, u, v);
            vertex(sphereX[v2] * r, sphereY[v2] * r, sphereZ[v2] * r, u, v + iv);
            endShape();
            v += iv;
        }
        u = 0;

        // Add the southern cap
        beginShape(TRIANGLE_STRIP);
        texture(t);
        v2 = voff;
        for (int i = 0; i < sDetail; i++) {
            v2 = voff + i;
            vertex(0, r, 0, u, v + iv);
            vertex(sphereX[v2] * r, sphereY[v2] * r, sphereZ[v2] * r, u, v);
            u += iu;
        }
        vertex(0, r, 0, u, v + iv);
        vertex(sphereX[voff] * r, sphereY[voff] * r, sphereZ[voff] * r, u, v);
        endShape();
    }

    public static void main(String args[]) {
        PApplet.main(new String[] { "--present", GlobeVisualization.class.getName() });
    }
}