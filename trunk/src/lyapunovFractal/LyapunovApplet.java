package lyapunovFractal;

/*

This defines an applet that computes Lyapunov exponents for an iterated sinus
function switching between different values according to a sequence sheme.
The graph shows wether certain points of the AB-plane of possible values has
positive (chaotic) or negative (stable) Lyapunov exponent. It runs thus:

First, we have a function f(z) = b * sin^2(z + r)
Now, we iterate this, i.e. f^n+1(z) = f(f^n(z))
Be (x,y) a point of the AB-plane.
Be s a string like "abaab".
Now the value of r switches between x and y, according to
the sequence s.
Be i = n modulo length(s).
That means,

  if the i-th letter of s is a, then r = x
  if the i-th letter of s is b, then r = y

The Lyapunov exponent is the sum of the logarithms of
the absolute of the first derivation of the iterations of f.
That means, we regard the value of

  Sum(i = 0 to infinity) { log(abs(f'(f^i(z)))}
  
(The value "infinity" is, for obvious reason, replaced by a finite
value as we compute our approximation)
  
The initial value of z (called "seed") is, in most cases, not very
important (0 would be a good choice). More important is the value of
b and the sequence s. All of this can be set using a control parameter
frame.

The applet shows only a part of the infinite AB-plane. Initially, it
shows the square [0, pi]x[0, pi]. Since sin^2 repeats after pi, the whole
plane is a tiling of this initial square. The user can zoom this square,
either by entering manually the [x1, y1]x[x2, y2]-coordinates or by
drawing a box using the mouse pointer.

Technically, we need three classes: first, an applet class. Next, a class
with the control parameter frame. And third, a class doing the computation.
The computation class is called by the control frame and starts a new
computation thread. It draws its result within an offscreen image belonging
to the applet. This offscreen image is used by the applet to draw on screen.

Two palettes for the negative (stable) and two palettes for the positive 
(chaotic) results are hardcoded. The palettes can be set via the applet's
parameters.

Best results are obtained if the applet is embeded as a square within
the html-page.

29.-30.5.2001

Jan Thor
jan@janthor.de
www.janthor.de

*/

import java.applet.*;
import java.awt.*;
import java.awt.image.*;
import java.lang.Math;
import java.lang.Boolean;
import java.awt.event.*;
import java.io.*;

public final class LyapunovApplet extends java.applet.Applet implements MouseListener, MouseMotionListener {
  // Since we are nice guys, we provide some informations about our applet:
  final String myAppletInfo = "Jan Thor\n29-31 may 2001\njan@janthor.de\nwww.janthor.de";
  // Some more Variables
  Image bufferImage; // the buffered image we need to avoid flickering...
  Graphics bg; // ...and its graphical context
  Image endBuffer; // another image to draw a box in it...
  Graphics ebg; // ...and its graphical context
  // control panel
  LyapunovFrame master;
  // Now, the special Lyapunov parameter.
  double x1;
  double x2;
  double y1;
  double y2;
  String sequenz;
  double seed;
  double b;
  int unusedIterations;
  int maxIterations;
  double maxResult;
  double minResult;
  // Reprise as default values for initialisation
  // ***You may change this if you like***
  private final static double d_x1 = 0.0;
  private final static double d_x2 = Math.PI;
  private final static double d_y1 = 0.0;
  private final static double d_y2 = Math.PI;
  private final static String d_sequenz = "ab";
  private final static double d_seed = 0.0;
  private final static double d_b = 2.5;
  private final static int d_unusedIterations = 20;
  private final static int d_maxIterations = 80;
  private final static double d_maxResult = (Math.sqrt(5.0) - 1.0) / 2.0;
  private final static double d_minResult = (Math.sqrt(5.0) + 1.0) / 2.0;
  // ***End of default values***
  // Mouse things
  boolean boxIs = false; // are we currently drawing a box?
  int mstartx; // box=mouse position
  int mstarty;
  int mcurrx;
  int mcurry;
  // palette
  final static int MAXPALETTELENGTH = 1024; // I need only 639 entries until now
  Color[] colpalminus = new Color[MAXPALETTELENGTH]; // palette for order
  int colpalminuslength;
  Color[] colpalplus = new Color[MAXPALETTELENGTH]; // palette for chaos
  int colpalpluslength;
  int paletteParam; // which palette we are using
  
  // first things first:
  // The inevailable init method
	public void init() {
	  // initialize the parameters with the default values
	  x1 = d_x1;
	  x2 = d_x2;
	  y1 = d_y1;
	  y2 = d_y2;
	  sequenz = d_sequenz;
	  seed = d_seed;
	  b = d_b;
    unusedIterations = d_unusedIterations;
    maxIterations = d_maxIterations;
    maxResult = d_maxResult;
    minResult = d_minResult;
    // prepare the palettes
    String parameterString; // String to convert parameters to integers etc.
    if((parameterString = getParameter("palette")) == null)
      paletteParam = 0;
    else
      paletteParam = (new Integer(parameterString)).intValue();
    makepalplus(); // change methods for different palettes
    makepalminus();
    // prepare the output image
    bufferImage = createImage(getSize().width, getSize().height);
    bg = bufferImage.getGraphics();
    endBuffer = createImage(getSize().width, getSize().height);
    ebg = endBuffer.getGraphics();
    bg.drawString("Waiting for your commands...", 0, bufferImage.getHeight(this) - 20);
	  // add the controls
    master = new LyapunovFrame(this);
    if(master != null) master.show();
    // add the mouse listener
    addMouseListener(this);
    addMouseMotionListener(this);
  }
  
  // I considered loading a palette (indeed I had not even just
  // written the code for loading one, but also built two palette
  // files), but file loading can be a problem within different
  // applet contexts (local viewing etc.), and since my palettes
  // are in any case computer generated, I generate them on the fly.
  // Change these to methods to add different palettes.
  // You can select a palette method using the "palette"-parameter.
  // Default is my usual green palette, 1 is the orthodox yellow/blue palette
  void makepalplus() {
    switch(paletteParam) {
      // the special palette No. 1
      case 1:
        for(int i = 0; i < 256; i++) {
          colpalplus[i] = new Color(0, 0, 255 - i);
        }
        colpalpluslength = 256;
        break;
      // the default palette
      default:
        for(int i = 0; i < 256; i++) {
          colpalplus[i] = new Color(0, i, 0);
        }
        colpalpluslength = 256;
        break;
    }
  }
  
  // This is the same for negative values.
  // Negative values denote regions of order;
  void makepalminus() {
    switch(paletteParam) {
      // the special palette No. 1
      case 1:
        for(int i = 0; i < 256; i++) {
          colpalminus[i] = new Color(i, i, 0);
        }
        for(int i = 256; i < 511; i++) {
          colpalminus[i] = new Color(255, 255, i - 255);
        }
        colpalminuslength = 511;
        break;
      // the default palette
      default:
        for(int i = 0; i < 64; i++) {
          colpalminus[i] = new Color(0, i, 0);
        }
        for(int i = 0; i < 127; i++) {
          colpalminus[i+64] = new Color(0, 64 + i / 2, 0);
        }
        for(int i = 0; i < 511; i++) {
          colpalminus[i+191] = new Color(0, 128 + i / 4, 0);
        }
        for(int i = 0; i < 128; i++) {
          colpalminus[i+702] = new Color(i * 2, 255, 0);
        }
        for(int i = 0; i < 32; i++) {
          colpalminus[i+830] = new Color(255, 255, i * 8);
        }
        colpalminuslength = 862;
        break;
    }
  }
  
  // this is overwritten to avoid flickering
  public void update(Graphics g) {
    paint(g);
  }
  
  // The usual paint-method
  public void paint(Graphics g) { 
    // we do everything within bufferImage, so nothing exciting happens
    // get the result of the computation stored in bufferImage and draw it
    ebg.drawImage(bufferImage, 0, 0, this);
    // draw a box
    if(boxIs) { 
      int diff = Math.max(Math.abs(mstartx - mcurrx), Math.abs(mstarty - mcurry));
      ebg.setColor(Color.red);
      ebg.drawRect(mstartx - diff, mstarty - diff, 2 * diff, 2 * diff);
    }
    // draw on screen
    g.drawImage(endBuffer, 0, 0, this);
  }
  
  // Leave a clean browser
  public void destroy() {
    // before closing time, we
    // have to get rid of our frame
    if(master != null) {
      master.dispose();
      master = null;
    }
  }
  
  // Mouse things (used to draw a box)
  
  // three dummy methods, unused
  public void mouseClicked(MouseEvent e) {}
  public void mouseEntered(MouseEvent e) {}
  public void mouseExited(MouseEvent e) {}
  
  // start a box if the mouse is pressed
  public void mousePressed(MouseEvent e) {
    boxIs = true;
    mstartx = e.getX();
    mstarty = e.getY();
    mcurrx = mstartx;
    mcurry = mstarty;
  }
  
  // stop the box and compute new location if the mouse is released
  public void mouseReleased(MouseEvent e) {
    // transform box coordinates in AB-plane-coordinates
    int diff = Math.max(Math.abs(mstartx - mcurrx), Math.abs(mstarty - mcurry));
    double tx1 = x1 + (x2 - x1) * (double)(mstartx - diff) / getSize().width;
    double tx2 = x1 + (x2 - x1) * (double)(mstartx + diff) / getSize().width;
    double ty1 = y1 + (y2 - y1) * (double)(mstarty - diff) / getSize().height;
    double ty2 = y1 + (y2 - y1) * (double)(mstarty + diff) / getSize().height;
    // perhaps the user just clicked to get rid of the box
    if(mstartx == mcurrx && mstarty == mcurry) {
      boxIs = false;
      tx1 = x1;
      tx2 = x2;
      ty1 = y1;
      ty2 = y2;
      repaint();
    }
    // inform the master
    master.inform(tx1, tx2, ty1, ty2);
  }

  // dummy, unused
  public void mouseMoved(MouseEvent e) {}
  
  // if the mouse is moved, update the box
  public void mouseDragged(MouseEvent e) {
      mcurrx = e.getX();
      mcurry = e.getY();
      repaint();
  }
  
  // Okay, that's it. He's dead, Jim.
  
  public String getAppletInfo() {
    return myAppletInfo;
  }
  
}

// We define a frame containing the controls for the applet
//
// To do this, we have to extend the Frame class
final class LyapunovFrame extends Frame implements ActionListener {
  // The Applet we want to control
  LyapunovApplet slave;
  
  // A Computation.
  LyapunovComputation rechnung;
    
  // Some controls
  TextField tf_x1;
  TextField tf_x2;
  TextField tf_y1;
  TextField tf_y2;
  TextField tf_sequenz;
  TextField tf_seed;
  TextField tf_b;
  TextField tf_unusedIterations;
  TextField tf_maxIterations;
  TextField tf_maxResult;
  TextField tf_minResult;
  Button tfPi;
  Button tfStart;
  
  // The Constructor
  LyapunovFrame(LyapunovApplet slave) {
    // Most important: Declare who we want to controle    
    this.slave = slave;
    
    // Add buttons, labels, layout etc.
    // we use two panels and a button outside both panels
    
    // first: main properties
    setTitle("lyapunov main control center");
    greenify(this);
    setLayout(new GridBagLayout());
    // Call our Manager
    Panel mainPan = new Panel(new GridBagLayout());
    Panel resultPan = new Panel(new GridBagLayout());
    GridBagConstraints griddy = new GridBagConstraints();
    griddy.weightx = 1.0;
    griddy.fill = GridBagConstraints.HORIZONTAL;
    griddy.anchor = GridBagConstraints.WEST;
    
    // Add some controls for mainPan
    // x1
    griddy.insets = new Insets(0, 0, 0, 0);
    griddy.gridwidth = GridBagConstraints.RELATIVE;
    tf_x1 = new TextField((new String()).valueOf(slave.x1), 16);
    greenify(tf_x1);
    mainPan.add(tf_x1, griddy);
    griddy.insets = new Insets(0, 5, 0, 10);
    griddy.gridwidth = GridBagConstraints.REMAINDER;
    mainPan.add(new Label("x1"), griddy);
    // x2
    griddy.insets = new Insets(5, 0, 0, 0);
    griddy.gridwidth = GridBagConstraints.RELATIVE;
    tf_x2 = new TextField((new String()).valueOf(slave.x2), 16);
    greenify(tf_x2);
    mainPan.add(tf_x2, griddy);
    griddy.insets = new Insets(5, 5, 0, 10);
    griddy.gridwidth = GridBagConstraints.REMAINDER;
    mainPan.add(new Label("x2"), griddy);
    // y1
    griddy.insets = new Insets(5, 0, 0, 0);
    griddy.gridwidth = GridBagConstraints.RELATIVE;
    tf_y1 = new TextField((new String()).valueOf(slave.y1), 16);
    greenify(tf_y1);
    mainPan.add(tf_y1, griddy);
    griddy.insets = new Insets(5, 5, 0, 0);
    griddy.gridwidth = GridBagConstraints.REMAINDER;
    mainPan.add(new Label("y1"), griddy);
    // y2
    griddy.insets = new Insets(5, 0, 0, 0);
    griddy.gridwidth = GridBagConstraints.RELATIVE;
    tf_y2 = new TextField((new String()).valueOf(slave.y2), 16);
    greenify(tf_y2);
    mainPan.add(tf_y2, griddy);
    griddy.insets = new Insets(5, 5, 0, 0);
    griddy.gridwidth = GridBagConstraints.REMAINDER;
    mainPan.add(new Label("y2"), griddy);
    // sequenz
    griddy.insets = new Insets(5, 0, 0, 0);
    griddy.gridwidth = GridBagConstraints.RELATIVE;
    tf_sequenz = new TextField(slave.sequenz, 16);
    greenify(tf_sequenz);
    mainPan.add(tf_sequenz, griddy);
    griddy.insets = new Insets(5, 5, 0, 0);
    griddy.gridwidth = GridBagConstraints.REMAINDER;
    mainPan.add(new Label("sequenz"), griddy);
    // seed
    griddy.insets = new Insets(5, 0, 0, 0);
    griddy.gridwidth = GridBagConstraints.RELATIVE;
    tf_seed = new TextField((new String()).valueOf(slave.seed), 16);
    greenify(tf_seed);
    mainPan.add(tf_seed, griddy);
    griddy.insets = new Insets(5, 5, 0, 0);
    griddy.gridwidth = GridBagConstraints.REMAINDER;
    mainPan.add(new Label("seed"), griddy);
    // b
    griddy.insets = new Insets(5, 0, 0, 0);
    griddy.gridwidth = GridBagConstraints.RELATIVE;
    tf_b = new TextField((new String()).valueOf(slave.b), 16);
    greenify(tf_b);
    mainPan.add(tf_b, griddy);
    griddy.insets = new Insets(5, 5, 0, 0);
    griddy.gridwidth = GridBagConstraints.REMAINDER;
    mainPan.add(new Label("b"), griddy);
    
    // Add some result controls for resultPan
    // unusedIterations
    griddy.insets = new Insets(0, 0, 0, 0);
    griddy.gridwidth = GridBagConstraints.RELATIVE;
    tf_unusedIterations = new TextField((new String()).valueOf(slave.unusedIterations), 8);
    greenify(tf_unusedIterations);
    resultPan.add(tf_unusedIterations, griddy);
    griddy.insets = new Insets(0, 5, 0, 0);
    griddy.gridwidth = GridBagConstraints.REMAINDER;
    resultPan.add(new Label("unused iterations"), griddy);
    // maxIterations
    griddy.insets = new Insets(5, 0, 0, 0);
    griddy.gridwidth = GridBagConstraints.RELATIVE;
    tf_maxIterations = new TextField((new String()).valueOf(slave.maxIterations), 8);
    greenify(tf_maxIterations);
    resultPan.add(tf_maxIterations, griddy);
    griddy.insets = new Insets(5, 5, 0, 0);
    griddy.gridwidth = GridBagConstraints.REMAINDER;
    resultPan.add(new Label("used iterations"), griddy);
    // maxResult
    griddy.insets = new Insets(5, 0, 0, 0);
    griddy.gridwidth = GridBagConstraints.RELATIVE;
    tf_maxResult = new TextField((new String()).valueOf(slave.maxResult), 8);
    greenify(tf_maxResult);
    resultPan.add(tf_maxResult, griddy);
    griddy.insets = new Insets(5, 5, 0, 0);
    griddy.gridwidth = GridBagConstraints.REMAINDER;
    resultPan.add(new Label("max. exp. result"), griddy);
    // minResult
    griddy.insets = new Insets(5, 0, 0, 0);
    griddy.gridwidth = GridBagConstraints.RELATIVE;
    tf_minResult = new TextField((new String()).valueOf(slave.minResult), 8);
    greenify(tf_minResult);
    resultPan.add(tf_minResult, griddy);
    griddy.insets = new Insets(5, 5, 0, 0);
    griddy.gridwidth = GridBagConstraints.REMAINDER;
    resultPan.add(new Label("min. exp. result"), griddy);
    // Add a Pi Button
    griddy.insets = new Insets(5, 0, 0, 0);
    griddy.gridwidth = GridBagConstraints.REMAINDER;
    tfPi = new Button("return to pi");
    greenify(tfPi);
    tfPi.setCursor(new Cursor(Cursor.HAND_CURSOR));
    resultPan.add(tfPi, griddy);
    // Lend the button an ear
    tfPi.addActionListener(this);
    tfPi.setActionCommand("piplease");
    // Add an explanation
    resultPan.add(new Label("z := b * sin�(z + r)"), griddy);
    resultPan.add(new Label("a-> r = x, b-> r = y"), griddy);
    
    // Add the two panels
    griddy.anchor = GridBagConstraints.NORTH;
    griddy.insets = new Insets(10, 10, 0, 0);
    griddy.gridwidth = GridBagConstraints.RELATIVE;
    add(mainPan, griddy);
    
    griddy.insets = new Insets(10, 10, 0, 10);
    griddy.gridwidth = GridBagConstraints.REMAINDER;
    add(resultPan, griddy);
    
    // Add an update trigger
    griddy.insets = new Insets(20, 10, 10, 10);
    griddy.gridwidth = GridBagConstraints.REMAINDER;
    tfStart = new Button("do it!");
    greenify(tfStart);
    tfStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
    add(tfStart, griddy);
    // Lend the button an ear
    tfStart.addActionListener(this);
    tfStart.setActionCommand("updateplease");
    
    // Done!
    pack();
    setResizable(false);
  }

  // what happens if something happens?
  public void actionPerformed(ActionEvent evt) {
    // We have two buttons we need listen to.
    // First, the update button
    if("updateplease".equals(evt.getActionCommand())) {
      // Is there a previous instance of the computation running,
      // consuming CPU power? Then kill!
      if(rechnung != null) { 
        rechnung.stop();
        rechnung = null;
      }
      // Update the parameters
      slave.boxIs = false;
      slave.x1 = (new Double(tf_x1.getText())).doubleValue();
      slave.x2 = (new Double(tf_x2.getText())).doubleValue();
      slave.y1 = (new Double(tf_y1.getText())).doubleValue();
      slave.y2 = (new Double(tf_y2.getText())).doubleValue();
      slave.sequenz = sequenzer(tf_sequenz.getText());
      slave.seed = (new Double(tf_seed.getText())).doubleValue();
      slave.b = (new Double(tf_b.getText())).doubleValue();
      slave.unusedIterations = (new Integer(tf_unusedIterations.getText())).intValue();
      slave.maxIterations = (new Integer(tf_maxIterations.getText())).intValue();
      slave.maxResult = (new Double(tf_maxResult.getText())).doubleValue();
      slave.minResult = (new Double(tf_minResult.getText())).doubleValue();
      // Start a computation
      rechnung = new LyapunovComputation(slave);
      rechnung.start();
    }
    // Second, the Pi button
    if("piplease".equals(evt.getActionCommand())) {
      slave.boxIs = false;
      slave.repaint();
      tf_x1.setText((new String()).valueOf(0.0));
      tf_x2.setText((new String()).valueOf(Math.PI));
      tf_y1.setText((new String()).valueOf(0.0));
      tf_y2.setText((new String()).valueOf(Math.PI));
    }
  }
  
  // define our spacy colors
  protected void greenify(Component comp) {
    comp.setForeground(new Color(204, 255, 153));
    comp.setBackground(Color.black);
  }
    
  // Leave a clean browser
  public void destroy() {
    // before closing time, we
    // stop any running computation
    if(rechnung != null) {
      rechnung.stop();
      rechnung = null;
    }
  }
  
  // get informed by the applet about mouse movements
  public void inform(double tx1, double tx2, double ty1, double ty2) { 
    tf_x1.setText((new String()).valueOf(tx1));
    tf_x2.setText((new String()).valueOf(tx2));
    tf_y1.setText((new String()).valueOf(ty1));
    tf_y2.setText((new String()).valueOf(ty2));
  }
  
  // Use some abbreviations:
  // 5a5b instead of aaaaabbbbb
  // 3(ab)b instead of abababb
  private String sequenzer(String asequence) {
    // some definitions
    String mysequence = asequence.toLowerCase(); // ignore case
    String wabbel = new String(""); // target string
    String stupfel; // string inside brakets
    int zahl; // a literal number
    char singlec; // a single character
    // walk along the string
    for(int i = 0; i < mysequence.length(); i++) {
      singlec = mysequence.charAt(i);
      if(singlec == 'a' || singlec == 'b') wabbel += String.valueOf(singlec);
      // in case we find a digit
      if(Character.isDigit(singlec)) {
        // find the whole number (perhaps several digits)
        zahl = 0;
        while(i < mysequence.length() && Character.isDigit(mysequence.charAt(i))) {
          zahl = 10 * zahl + Character.getNumericValue(mysequence.charAt(i));
          i++;
        }
        // maybe someone typed something like "aabb5"
        if(i < mysequence.length()) {
          singlec = mysequence.charAt(i);
          // the complicated case: a braket opens
          if(singlec == '(') {
            // scan to the braket's end
            stupfel = "";
            while(i < mysequence.length() & singlec != ')') {
              singlec = mysequence.charAt(i);
              if(singlec == 'a' || singlec == 'b') stupfel += String.valueOf(singlec);
              i++;
            }
            for(int j = 0; j < zahl; j++) wabbel += stupfel;
            i--;
          }
          // the simple case: just one char to be repeated
          else { 
            for(int j = 0; j < zahl; j++) wabbel += String.valueOf(singlec);
          }
        }
      }
    }
    return wabbel;
  }
  
}

// The computation itself gets its own class.
// This class implements Runnable to run its own thread.
// We extend it from Component to get some graphical methods for
// free (this is a bit of abuse, since this is not realy a
// component).
final class LyapunovComputation extends Component implements Runnable {
  LyapunovApplet slave; // the applet we do the work for
  Thread t; // someone doing the work
  int[] res = {16, 4, 1}; // preview resolutions; last value should always be "1"

  // Constructor
  public LyapunovComputation(LyapunovApplet slave) {
    this.slave = slave;
  }

  public void start() { 
    if(t == null) {        // On a beautiful new morning:
      t = new Thread(this); // Start a new Thread
      t.start();
    }
  }
  
  public void run() {
    // some variables needed for computation
    double summe;
    String seq;
    int sc;
    double x;
    double y;
    double p;
    float c;
    int cindex;
    Thread me = Thread.currentThread();
    // clear the stage, put a message on it
    slave.bg.setColor(Color.white);
    slave.bg.fillRect(0, 0, slave.bufferImage.getWidth(this), slave.bufferImage.getHeight(this));
    slave.bg.setColor(Color.black);
    slave.bg.drawString("Computation running...", 0, slave.bufferImage.getHeight(this) -20);
    slave.repaint();
    // The main computation
    // this uses several preview loops
    for(int k = 0; k < res.length; k++) {
      // loop through the AB-plane
      for(int j = 0; j < slave.bufferImage.getHeight(this); j += res[k]) { 
        for(int i = 0; i < slave.bufferImage.getWidth(this); i += res[k]) {
          // Get initial values
          x = slave.x1 + (slave.x2 - slave.x1) * ((double)i / slave.bufferImage.getWidth(this));
          y = slave.y1 + (slave.y2 - slave.y1) * ((double)j / slave.bufferImage.getHeight(this));
          summe = 0.0;
          p = slave.seed;
          sc = 0;
          // Do some unused iterations
          for(int n = 0; n < slave.unusedIterations; n++) {
            seq = slave.sequenz.substring(sc % slave.sequenz.length(), sc % slave.sequenz.length()+1);
            sc++;
            if(seq.equals("a")) {
              p = slave.b * Math.sin(p + x) * Math.sin(p + x);
            }
            else { 
              p = slave.b * Math.sin(p + y) * Math.sin(p + y);
            }
          }
          // Do some used iterations
          for(int n = 0; n < slave.maxIterations; n++) {
            seq = slave.sequenz.substring(sc % slave.sequenz.length(), sc % slave.sequenz.length()+1);
            sc++;
            if(seq.equals("a")) {
              summe += Math.log(Math.abs(2 * slave.b * Math.sin(p + x) * Math.cos(p + x)));
              p = slave.b * Math.sin(p + x) * Math.sin(p + x);
            }
            else { 
              summe += Math.log(Math.abs(2 * slave.b * Math.sin(p + y) * Math.cos(p + y)));
              p = slave.b * Math.sin(p + y) * Math.sin(p + y);
            }
          }
          // evaluate the color
          if(summe > 0) {
            c = (float)(summe / (slave.maxResult * slave.maxIterations));
            cindex = (int)(c * (float)slave.colpalpluslength);
            if(cindex >  slave.colpalpluslength - 1) cindex = slave.colpalpluslength - 1;
            if(cindex < 0) cindex = 0;
            slave.bg.setColor(slave.colpalplus[cindex]);
          }
          else {
            c = (float)(-summe / (slave.minResult * slave.maxIterations));
            cindex = (int)(c * (float)slave.colpalminuslength);
            if(cindex > slave.colpalminuslength - 1) cindex = slave.colpalminuslength - 1;
            if(cindex < 0) cindex = 0;
            slave.bg.setColor(slave.colpalminus[cindex]);
          }
          // output
          slave.bg.fillRect(i, j, res[k], res[k]);  
          // Does our Thread still exist? Otherwise stop
          if(me != t) j = slave.bufferImage.getHeight(this);
        }
        // Update Image every row
        slave.repaint();
        // Give a chance for stopping
        Thread.currentThread().yield();
      }
    }
    // If the Thread is still valid, show results
    // (this is not strictly necessairy, since we repaint evrey row,
    // but perhaps one day we want to change this code and set an
    // option not to repaint until the computation is finished, to
    // speed up things)
    if(me == t) {
      slave.bg.setColor(Color.black);
      slave.repaint();
    }
  }
  
  public void stop() { 
    t = null; // Argh! I'm killed!
  }
  
}