package app;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

public class JOGLApp {
	private static final int FPS = 60; // animator's target frames per second
	private static final String APP_TITLE ="PGRF3 - 1.uloha - Martin Polreich";
	private static JFrame frame;
	private static JMenu mappingMenu;
	private static Renderer ren;
	private static JCheckBoxMenuItem jchkUseTexture;
	private static JSlider parallaxCoefSlider;
	private static JLabel sliderValue;
    private static final Dimension windowSize= new Dimension(1024, 768);
	private static Image info;
    public static void main(String[] args) {
		try {
			frame = new JFrame("TestFrame");
			frame.setSize(windowSize.width, windowSize.height);
			frame.setJMenuBar(getMenus());

			// setup OpenGL version
	    	GLProfile profile = GLProfile.getMaximum(true);
	    	GLCapabilities capabilities = new GLCapabilities(profile);

	    	// The canvas is the widget that's drawn in the JFrame
	    	GLCanvas canvas = new GLCanvas(capabilities);
	    	ren = new Renderer();
			canvas.addGLEventListener(ren);
			canvas.addMouseListener(ren);
			canvas.addMouseMotionListener(ren);
			canvas.addKeyListener(ren);
			canvas.setSize(windowSize.width,windowSize.height);
	    	frame.add(canvas);

	        //shutdown the program on windows close event

	    	//final Animator animator = new Animator(canvas);
	    	final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

	    	frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					new Thread() {
	                     @Override
	                     public void run() {
	                        if (animator.isStarted()) animator.stop();
	                        System.exit(0);
	                     }
	                  }.start();
				}
			});
            frame.setTitle(APP_TITLE);
            frame.pack();
            frame.setVisible(true);
            animator.start(); // start the animation loop


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static JMenuBar getMenus(){
		JMenuBar menuBar = new JMenuBar();
        menuBar.setBorder(BorderFactory.createMatteBorder(1, 1, 2, 1, Color.BLACK));
/* FUNCTIONS */
        JMenu objectsMenu = new JMenu("Functions");
		ButtonGroup btnFunctions = new ButtonGroup();
/* CARTESIAN */
            JMenu cartesianObjects = new JMenu("Cartesian");
/* CONE */
                JMenuItem menuObjectCone = new JCheckBoxMenuItem("Cone");
                menuObjectCone.addActionListener(e -> ren.setFunction(Renderer.FUNCTION_CONE));
                cartesianObjects.add(menuObjectCone);
                btnFunctions.add(menuObjectCone);
/* SADDLE */
                JMenuItem menuObjectSaddle = new JCheckBoxMenuItem("Saddle",true);
                menuObjectSaddle.addActionListener(e -> ren.setFunction(Renderer.FUNCTION_SADDLE));
                cartesianObjects.add(menuObjectSaddle);
                btnFunctions.add(menuObjectSaddle);
/* SNAKE */
                JMenuItem menuObjectSnake = new JCheckBoxMenuItem("Snake");
                menuObjectSnake.addActionListener(e-> ren.setFunction(Renderer.FUNCTION_SNAKE));
                cartesianObjects.add(menuObjectSnake);
                btnFunctions.add(menuObjectSnake);

            objectsMenu.add(cartesianObjects);

/* CYLINDRICAL */
		    JMenu cylindricalObjects = new JMenu("Cylindrical");
/* WINE_GLASS */
                JMenuItem menuObjectGlass = new JCheckBoxMenuItem("Glass");
                menuObjectGlass.addActionListener(e -> ren.setFunction(Renderer.FUNCTION_WINE_GLASS));
                cylindricalObjects.add(menuObjectGlass);
                btnFunctions.add(menuObjectGlass);
/* SOMBRERO */
                JMenuItem menuObjectSombrero = new JCheckBoxMenuItem("Sobrero");
                menuObjectSombrero.addActionListener(e -> ren.setFunction(Renderer.FUNCTION_SOMBRERO));
                cylindricalObjects.add(menuObjectSombrero);
                btnFunctions.add(menuObjectSombrero);
/* MUSHROOM */
                JMenuItem menuObjectMushroom = new JCheckBoxMenuItem("Mushroom");
                menuObjectMushroom.addActionListener(e -> ren.setFunction(Renderer.FUNCTION_MUSHROOM));
                cylindricalObjects.add(menuObjectMushroom);
                btnFunctions.add(menuObjectMushroom);

            objectsMenu.add(cylindricalObjects);

/* SPHERICAL */
		    JMenu sphericalObjects = new JMenu("Spherical");
/* SPHERE */
                JMenuItem menuObjectSphere = new JCheckBoxMenuItem("Sphere");
                menuObjectSphere.addActionListener(e -> ren.setFunction(Renderer.FUNCTION_SPHERE));
                sphericalObjects.add(menuObjectSphere);
                btnFunctions.add(menuObjectSphere);
/* ALIEN */
                JMenuItem menuObjectAlien = new JCheckBoxMenuItem("Alien");
                menuObjectAlien.addActionListener(e -> ren.setFunction(Renderer.FUNCTION_ALIEN));
                sphericalObjects.add(menuObjectAlien);
                btnFunctions.add(menuObjectAlien);
/* EL_HEAD */
                JMenuItem menuObjectElHead = new JCheckBoxMenuItem("Elephants head");
                menuObjectElHead.addActionListener(e -> ren.setFunction(Renderer.FUNCTION_ELHEAD));
                sphericalObjects.add(menuObjectElHead);
                btnFunctions.add(menuObjectElHead);
/* ROSE */
                JMenuItem menuObjectRose = new JCheckBoxMenuItem("Rose");
                menuObjectRose.addActionListener(e -> ren.setFunction(Renderer.FUNCTION_ROSE));
                sphericalObjects.add(menuObjectRose);
                btnFunctions.add(menuObjectRose);
/* RAINDROP */
                JMenuItem menuObjectRainDrop = new JCheckBoxMenuItem("Rain Drop");
                menuObjectRainDrop.addActionListener(e -> ren.setFunction(Renderer.FUNCTION_RAINDROP));
                sphericalObjects.add(menuObjectRainDrop);
                btnFunctions.add(menuObjectRainDrop);
/* FOUNTAIN */
                JMenuItem menuObjectFountain = new JCheckBoxMenuItem("Fountain");
                menuObjectFountain.addActionListener(e -> ren.setFunction(Renderer.FUNCTION_FOUNTAIN));
                sphericalObjects.add(menuObjectFountain);
                btnFunctions.add(menuObjectFountain);

            objectsMenu.add(sphericalObjects);

		menuBar.add(objectsMenu);

/* LIGHTNING */
		JMenu lightsMenu = new JMenu("Lightning");
        ButtonGroup btnLights = new ButtonGroup();
/* PER PIXEL */
            JMenuItem menuLightPerPixel = new JCheckBoxMenuItem("Per Pixel",true);
            menuLightPerPixel.addActionListener(e -> {
                    ren.setLightPerVertex(false);
                    toggleMappingMenu(true);
                });
            lightsMenu.add(menuLightPerPixel);
            btnLights.add(menuLightPerPixel);
/* PER VERTEX */
            JMenuItem menuLightPerVertex = new JCheckBoxMenuItem("Per Vertex");
            menuLightPerVertex.addActionListener(e -> {
                    ren.setLightPerVertex(true);
                    toggleMappingMenu(false);
                });
            lightsMenu.add(menuLightPerVertex);
            btnLights.add(menuLightPerVertex);

		menuBar.add(lightsMenu);

/* MAPPING */
		mappingMenu = new JMenu("Mapping");
		mappingMenu.setEnabled(false);
		ButtonGroup btnMapping = new ButtonGroup();
/* NONE */
            JMenuItem menuNoMapping = new JCheckBoxMenuItem("None",true);
            menuNoMapping.addActionListener(e -> {
                ren.setMappingType(Renderer.MAPPING_NONE);
                parallaxCoefSlider.setEnabled(false);
            });
            mappingMenu.add(menuNoMapping);
            btnMapping.add(menuNoMapping);
/* NORMAL */
            JMenuItem menuNormalMapping = new JCheckBoxMenuItem("Normal");
            menuNormalMapping.addActionListener(e -> {
                ren.setMappingType(Renderer.MAPPING_NORMAL);
                parallaxCoefSlider.setEnabled(false);
            });
            mappingMenu.add(menuNormalMapping);
            btnMapping.add(menuNormalMapping);
/* PARALLAX */
            JMenuItem menuParallaxMapping = new JCheckBoxMenuItem("Parallax");
            menuParallaxMapping.addActionListener(e -> {
                ren.setMappingType(Renderer.MAPPING_PARALLAX);
                parallaxCoefSlider.setEnabled(true);
            });
            mappingMenu.add(menuParallaxMapping);
            btnMapping.add(menuParallaxMapping);

		menuBar.add(mappingMenu);

/* TEXTURE */
		jchkUseTexture = new JCheckBoxMenuItem("Use texture");
		jchkUseTexture.addActionListener(e1 ->  {
				ren.setUseTexture(jchkUseTexture.getState());
				toggleMappingMenu(jchkUseTexture.getState());
		});
		menuBar.add(jchkUseTexture);

/* PARALLAX_SLIDER */
		parallaxCoefSlider = new JSlider(-10, 10);
		parallaxCoefSlider.setMajorTickSpacing(5);
		parallaxCoefSlider.setMinorTickSpacing(1);
		parallaxCoefSlider.setPaintTicks(true);
        Hashtable labelTable = new Hashtable();
        labelTable.put(parallaxCoefSlider.getMinimum(), new JLabel("MIN") );
        labelTable.put(0, new JLabel("0") );
        labelTable.put(parallaxCoefSlider.getMaximum(), new JLabel("MAX") );
        parallaxCoefSlider.setLabelTable( labelTable );
        parallaxCoefSlider.setPaintLabels(true);
        parallaxCoefSlider.setEnabled(false);
        JLabel sliderLabel = new JLabel("Parallax Mapping Coefficient:  ", JLabel.CENTER);
        sliderValue = new JLabel(String.valueOf(parallaxCoefSlider.getValue()));
        sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		parallaxCoefSlider.addChangeListener(e1 -> {
		    ren.setParallaxCoef((float) parallaxCoefSlider.getValue() / 100);
		    updateSliderValue();
        });
		menuBar.add(sliderLabel);
		menuBar.add(sliderValue);
		menuBar.add(Box.createRigidArea(new Dimension(15, 10)));
		menuBar.add(parallaxCoefSlider);
        menuBar.add(Box.createHorizontalGlue());

/* INFO */
		try
		{
			info = ImageIO.read(new File("res/img/info.png"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
        JButton btnAbout = new JButton(new ImageIcon(info));
		btnAbout.setFocusable(false);
		btnAbout.addActionListener(e ->  {
				JOptionPane.showMessageDialog(frame,
                        "Controls:\nMovement: WSAD, Ctrl, Shift \nRotation: mouse \nFirst pesron: Space \nPolygon mode: L(Line), P(Fill)",
                        "About",JOptionPane.INFORMATION_MESSAGE);
		});
		menuBar.add(btnAbout);
		return menuBar;
	}

	private static void toggleMappingMenu(boolean state){
		if(!state || jchkUseTexture.getState()){
			mappingMenu.setEnabled(state);
		}
		if(!jchkUseTexture.getState()){
            parallaxCoefSlider.setEnabled(false);
        }
	}

	private static void updateSliderValue(){
        sliderValue.setText(String.valueOf(parallaxCoefSlider.getValue()));
    }

}