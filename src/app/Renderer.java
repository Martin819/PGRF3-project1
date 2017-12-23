package app;

import com.jogamp.opengl.GL2GL3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import oglutils.*;
import transforms.Camera;
import transforms.Mat4;
import transforms.Mat4PerspRH;
import transforms.Vec3D;
import utils.MeshGenerator;

import java.awt.event.*;

/**
 * PGRF3 project #1 <br/>
 * Requires JOGL 2.3.0 or newer
 *
 * @author Martin Polreich
 * @version 1.3
 * @since 2017-11-13
 */

public class Renderer implements GLEventListener, MouseListener,
		MouseMotionListener, KeyListener {
    static final int FUNCTION_SADDLE = 0, FUNCTION_SNAKE = 1, FUNCTION_CONE = 2, FUNCTION_SOMBRERO = 3, FUNCTION_MUSHROOM = 4, FUNCTION_WINE_GLASS = 5,
            FUNCTION_SPHERE = 6, FUNCTION_ELHEAD = 7, FUNCTION_ALIEN = 8, FUNCTION_ROSE = 9, FUNCTION_RAINDROP = 10, FUNCTION_FOUNTAIN = 11;
    static final int MAPPING_NONE = 0, MAPPING_NORMAL = 1, MAPPING_PARALLAX = 2;

	private int width, height, ox, oy, polygonMode = GL2GL3.GL_FILL;
    private int renderedFunction=0, mappingType=0;
    private boolean lightPerVertex, useTexture, useAnimation;

    private OGLBuffers grid;
	private OGLTextRenderer textRenderer;

	private int locLightPos, locEyePos, locMatGrid, shaderProgram, locRenderFunction, locLightPerVertex, locTexture, locAnimation, locMapType, locParallaxCoef, locTime;
	private float parallaxCoef, time;

    private Vec3D lightPos = new Vec3D(0,0,150);

	private Camera cam = new Camera();
	private Mat4 proj;
    private OGLTexture2D texture, textureNormal, textureHeight;

	@Override
	public void init(GLAutoDrawable glDrawable) {
		// check whether shaders are supported
		GL2GL3 gl = glDrawable.getGL().getGL2GL3();
		OGLUtils.shaderCheck(gl);

		// get and set debug version of GL class
		gl = OGLUtils.getDebugGL(gl);
		glDrawable.setGL(gl);

		OGLUtils.printOGLparameters(gl);

		textRenderer = new OGLTextRenderer(gl, glDrawable.getSurfaceWidth(), glDrawable.getSurfaceHeight());
        shaderProgram = ShaderUtils.loadProgram(gl, "/grid");
        locMatGrid = gl.glGetUniformLocation(shaderProgram, "mat");
        locLightPos = gl.glGetUniformLocation(shaderProgram, "lightPos");
        locEyePos = gl.glGetUniformLocation(shaderProgram,"eyePos");
        locTexture = gl.glGetUniformLocation(shaderProgram, "useTexture");
        locAnimation = gl.glGetUniformLocation(shaderProgram, "useAnimation");
        locMapType = gl.glGetUniformLocation(shaderProgram, "mappingType");
        locParallaxCoef = gl.glGetUniformLocation(shaderProgram, "parallaxCoef");
        locRenderFunction = gl.glGetUniformLocation(shaderProgram, "renderFunction");
        locLightPerVertex = gl.glGetUniformLocation(shaderProgram, "lightPerVertex");
        locTime = gl.glGetUniformLocation(shaderProgram, "time");


		cam = cam.withPosition(new Vec3D(5, 5, 2.5))
				.withAzimuth(Math.PI * 1.25)
				.withZenith(Math.PI * -0.125);
        texture = new OGLTexture2D(gl, "/textures/bricks.jpg");
        textureNormal = new OGLTexture2D(gl, "/textures/bricksn.png");
        textureHeight = new OGLTexture2D(gl, "/textures/bricksh.png");
        grid = MeshGenerator.generateGrid(gl, 200, 200, "inPos");
		gl.glEnable(GL2GL3.GL_DEPTH_TEST);
	}


	@Override
	public void display(GLAutoDrawable glDrawable) {
        GL2GL3 gl = glDrawable.getGL().getGL2GL3();
        gl.glPolygonMode(GL2GL3.GL_FRONT_AND_BACK, polygonMode);
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        gl.glClear(GL2GL3.GL_COLOR_BUFFER_BIT | GL2GL3.GL_DEPTH_BUFFER_BIT);
        time +=0.1;
        Mat4 mat = cam.getViewMatrix().mul(proj);

        gl.glUseProgram(shaderProgram);
        gl.glUniformMatrix4fv(locMatGrid, 1, false, ToFloatArray.convert(mat), 0);
        gl.glUniform3fv(locLightPos, 1, ToFloatArray.convert(lightPos), 0);
        gl.glUniform3fv(locEyePos, 1, ToFloatArray.convert(cam.getEye()), 0);

        gl.glUniform1f(locRenderFunction, (float) renderedFunction);
        gl.glUniform1f(locLightPerVertex, (lightPerVertex)?1f:0f);
        gl.glUniform1f(locTexture, (useTexture)?1f:0f);
		gl.glUniform1f(locAnimation, (useAnimation)?1f:0f);

		gl.glUniform1f(locMapType, (float) mappingType);

        gl.glUniform1f(locParallaxCoef, parallaxCoef);
        gl.glUniform1f(locTime, time);


        texture.bind(shaderProgram, "texture", 0);
        textureNormal.bind(shaderProgram, "textureNormal", 1);
        textureHeight.bind(shaderProgram, "textureHeight", 2);

        grid.draw(GL2GL3.GL_TRIANGLES, shaderProgram);

        textRenderer.drawStr2D(width-220, 3, " (c) Martin Polreich - PGRF3 - FIM UHK");
    }

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {
		this.width = width;
		this.height = height;
		proj = new Mat4PerspRH(Math.PI / 4, height / (double) width, 0.01, 1000.0);
		textRenderer.updateSize(width, height);
	}

    public void setFunction(int function) {
        this.renderedFunction = function;
    }
    public void setLightPerVertex(boolean lightPerVertex){
        this.lightPerVertex=lightPerVertex;
    }
    public void setUseTexture(boolean useTexture){
        this.useTexture=useTexture;
    }
    public void setUseAnimation(boolean useAnimation) {
        this.useAnimation = useAnimation;
    }
    public void setMappingType(int mappingType){
        this.mappingType=mappingType;
    }
    public void setParallaxCoef(float parallaxCoef) {
        this.parallaxCoef = parallaxCoef;
    }

    @Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		ox = e.getX();
		oy = e.getY();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		cam = cam.addAzimuth(Math.PI * (ox - e.getX()) / width)
				.addZenith(Math.PI * (e.getY() - oy) / width);
		ox = e.getX();
		oy = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_W:
			cam = cam.forward(1);
			break;
		case KeyEvent.VK_D:
			cam = cam.right(1);
			break;
		case KeyEvent.VK_S:
			cam = cam.backward(1);
			break;
		case KeyEvent.VK_A:
			cam = cam.left(1);
			break;
		case KeyEvent.VK_CONTROL:
			cam = cam.down(1);
			break;
		case KeyEvent.VK_SHIFT:
			cam = cam.up(1);
			break;
		case KeyEvent.VK_SPACE:
			cam = cam.withFirstPerson(!cam.getFirstPerson());
			break;
		case KeyEvent.VK_R:
			cam = cam.mulRadius(0.9f);
			break;
		case KeyEvent.VK_F:
			cam = cam.mulRadius(1.1f);
			break;
		case KeyEvent.VK_L:
            polygonMode = GL2GL3.GL_LINE;
            break;
        case KeyEvent.VK_P:
            polygonMode = GL2GL3.GL_FILL;
            break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void dispose(GLAutoDrawable glDrawable) {
		glDrawable.getGL().getGL2GL3().glDeleteProgram(shaderProgram);
	}

}