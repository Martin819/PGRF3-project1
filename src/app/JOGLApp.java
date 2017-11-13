package app;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

public class JOGLApp {
	private static final int FPS = 60; // animator's target frames per second
	private static final String APP_TITLE ="PGRF3 - 1.uloha - Martin Polreich";
	private static JFrame frame;
	private static JMenu mappingMenu;
	private static Renderer ren;
	private static JCheckBoxMenuItem jchkUseTexture;
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
            menuNoMapping.addActionListener(e -> ren.setMappingType(Renderer.MAPPING_NONE));
            mappingMenu.add(menuNoMapping);
            btnMapping.add(menuNoMapping);
/* NORMAL */
            JMenuItem menuNormalMapping = new JCheckBoxMenuItem("Normal");
            menuNormalMapping.addActionListener(e -> ren.setMappingType(Renderer.MAPPING_NORMAL));
            mappingMenu.add(menuNormalMapping);
            btnMapping.add(menuNormalMapping);
/* PARALLAX */
            JMenuItem menuParallaxMapping = new JCheckBoxMenuItem("Parallax");
            menuParallaxMapping.addActionListener(e -> ren.setMappingType(Renderer.MAPPING_PARALLAX));
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
	}

}