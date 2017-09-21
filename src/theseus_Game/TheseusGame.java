package theseus_Game;

import theseus.Main;
import theseus.Window;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ARBDepthTexture;
import org.lwjgl.opengl.ARBShadow;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Frame;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 * The class "TheseusGame" represents the gaming part of Theseus. It Builds the
 * OpenGL game context, loads the Camera and contains the render loop.
 * 
 * @author Ahmed Arous
 */
public class TheseusGame {
	/** The running. */
	public static boolean running = true;

	/** The exploreMode. */
	public static boolean exploreMode = false;

	/** The fullscreen. */
	public static boolean fullscreen = false;

	/** The printFPS. */
	private static boolean printFPS = true;

	/** The resizable. */
	private static boolean resizable = true;

	/** The fps. */
	private static int fps;

	/** The lastFPS. */
	private static long lastFPS;

	/** The Grid . */
	private static Grid grid;

	/** The Skybox. */
	private static Skybox skybox;

	/** The Canvas. */
	private static Canvas canvas;

	/** The game Thread. */
	private static Thread gameThread;

	/** The JFrame window. */
	private static JFrame w;

	/** The Camera */
	private static Camera cam;

	/** The TheseusGame instance. */
	private static TheseusGame instance;

	/** The shadowMapTexture instance. */
	private static int shadowMapTexture;

	/** The size of the shadowMapTexture. */
	private static int shadowMapSize = 512;

	/** The cameraViewMatrix. */
	private static FloatBuffer cameraViewMatrix;

	/** The lightProjectionMatrix. */
	private static FloatBuffer lightProjectionMatrix;

	/** The cameraProjectionMatrix. */
	private static FloatBuffer cameraProjectionMatrix;

	/** The lightViewMatrix. */
	private static FloatBuffer lightViewMatrix;

	/** The AmbientLight. */
	private static FloatBuffer lModelAmbient = BufferUtils.createFloatBuffer(4);

	/** The whiteLight. */
	private static FloatBuffer whiteLight = BufferUtils.createFloatBuffer(4);

	/** The biasMatrix. */
	private static FloatBuffer bM = BufferUtils.createFloatBuffer(16).put(
			new float[] { 0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.0f,
					0.0f, 0.5f, 0.0f, 0.5f, 0.5f, 0.5f, 1.0f });

	/** The lightPosition. */
	private static FloatBuffer lightPosition = BufferUtils
			.createFloatBuffer(16).put(new float[] { 0f, 1.6f, 1.0f, 1 });

	/** The temporal FloatBuffer. */
	private static FloatBuffer tempFloatBuffer = ByteBuffer.allocateDirect(16)
			.order(ByteOrder.nativeOrder()).asFloatBuffer();

	/** The textureMatrix. */
	private static Matrix4f textureMatrix = new Matrix4f();

	/**
	 * Static method to retrieve the one and only reference to the TheseusGame.
	 * 
	 * @return the reference of the TheseusGame
	 */
	public static TheseusGame getInstance() {
		if (instance == null) {
			instance = new TheseusGame();
		}
		return instance;
	}

	/**
	 * The hidden constructor of the singleton.
	 */
	private TheseusGame() {
		// The Game Thread.
		gameThread = new Thread() {

			public void run() {
				initDisplay();
				checkOpenGL();

				grid = Grid.getInstance();
				grid.drawDatenstruktur();
				grid.getPositions();
				skybox = new Skybox();
				cam = new Camera((float) w.getWidth() / (float) w.getHeight(),
						(float) grid.getStartX(), 0.75f,
						(float) grid.getStartZ());
				cam.applyProjectionMatrix();

				initGL();

				lastFPS = getTime();

				while (running) {
					glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
					cam.applyModelviewMatrix();

					grid.render();
					skybox.render();

					// renderShadows();
					// renderCamera();
					// renderLight();
					// renderEnd();

					cam.mouseInput();
					cam.keyboardInput();

					if (Mouse.isButtonDown(0))
						Mouse.setGrabbed(true);
					else if (Mouse.isButtonDown(1))
						Mouse.setGrabbed(false);

					if (resizable) {
						if (Display.wasResized()) {
							glViewport(0, 0, Display.getWidth(),
									Display.getHeight());
							glMatrixMode(GL_PROJECTION);
							glLoadIdentity();
							gluPerspective(Camera.getFov(), w.getWidth()
									/ (float) w.getHeight(), Camera.getzNear(),
									Camera.getzFar());
							glMatrixMode(GL_MODELVIEW);
							glLoadIdentity();
						}
					}

					if (printFPS) {
						updateFPS();
					}
					Display.update();

					if (Display.isCloseRequested()) {
						running = false;
					}
				}
				Display.destroy();
				w.dispose();
				
				JFrame frame = new JFrame("WIN!!!!");
				JLabel label = new JLabel();
				BufferedImage endI;
				ImageIcon imageIcon = new ImageIcon();
				try
				{
					endI = ImageIO.read(new File("res/win.png"));
					imageIcon.setImage(endI);
					label.setIcon(imageIcon);
					Container contentPane = frame.getContentPane();
					//contentPane.add(label);
					label.setVisible(true);
					frame.add(label);
					frame.setSize(512, 512);
					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
				}
				catch (IOException ex)
				{
					
				}
				
			}

		};
		gameThread.start();
	}

	/**
	 * unfinished.
	 */
	public static void renderShadows() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		// render first pass
		glMatrixMode(GL_PROJECTION);
		glLoadMatrix(lightProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
		glLoadMatrix(lightViewMatrix);

		glViewport(0, 0, shadowMapSize, shadowMapSize);
		glDisable(GL_LIGHTING);
		glColorMask(false, false, false, false);
		glEnable(GL_POLYGON_OFFSET_FILL);

		// drawScene
		grid.render();
		// !drawScene

		glBindTexture(GL_TEXTURE_2D, shadowMapTexture);
		glCopyTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, 0, 0, shadowMapSize,
				shadowMapSize);
		glEnable(GL_LIGHTING);
		glColorMask(true, true, true, true);
		glViewport(0, 0, w.getWidth(), w.getHeight());
	}

	/**
	 * unfinished.
	 */
	public static void renderCamera() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

		glMatrixMode(GL_PROJECTION);
		glLoadMatrix(cameraProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
		glLoadMatrix(cameraViewMatrix);

		glTexGeni(GL_S, GL_TEXTURE_GEN_MODE, GL_EYE_LINEAR);

		tempFloatBuffer.put(0, textureMatrix.m00);
		tempFloatBuffer.put(1, textureMatrix.m10);
		tempFloatBuffer.put(2, textureMatrix.m20);
		tempFloatBuffer.put(3, textureMatrix.m30);

		glTexGen(GL_S, GL_EYE_PLANE, tempFloatBuffer);
		glEnable(GL_TEXTURE_GEN_S);

		glTexGeni(GL_T, GL_TEXTURE_GEN_MODE, GL_EYE_LINEAR);

		tempFloatBuffer.put(0, textureMatrix.m01);
		tempFloatBuffer.put(1, textureMatrix.m11);
		tempFloatBuffer.put(2, textureMatrix.m21);
		tempFloatBuffer.put(3, textureMatrix.m31);

		glTexGen(GL_T, GL_EYE_PLANE, tempFloatBuffer);
		glEnable(GL_TEXTURE_GEN_T);

		glTexGeni(GL_R, GL_TEXTURE_GEN_MODE, GL_EYE_LINEAR);

		tempFloatBuffer.put(0, textureMatrix.m02);
		tempFloatBuffer.put(1, textureMatrix.m12);
		tempFloatBuffer.put(2, textureMatrix.m22);
		tempFloatBuffer.put(3, textureMatrix.m32);

		glTexGen(GL_R, GL_EYE_PLANE, tempFloatBuffer);
		glEnable(GL_TEXTURE_GEN_R);

		glTexGeni(GL_Q, GL_TEXTURE_GEN_MODE, GL_EYE_LINEAR);

		tempFloatBuffer.put(0, textureMatrix.m03);
		tempFloatBuffer.put(1, textureMatrix.m13);
		tempFloatBuffer.put(2, textureMatrix.m23);
		tempFloatBuffer.put(3, textureMatrix.m33);

		glTexGen(GL_Q, GL_EYE_PLANE, tempFloatBuffer);
		glEnable(GL_TEXTURE_GEN_Q);

		glBindTexture(GL_TEXTURE_2D, shadowMapTexture);
		glEnable(GL_TEXTURE_2D);

		glTexParameteri(GL11.GL_TEXTURE_2D,
				ARBShadow.GL_TEXTURE_COMPARE_MODE_ARB,
				GL14.GL_COMPARE_R_TO_TEXTURE);
		glTexParameteri(GL11.GL_TEXTURE_2D,
				ARBShadow.GL_TEXTURE_COMPARE_FUNC_ARB, GL11.GL_LEQUAL);
		glTexParameteri(GL11.GL_TEXTURE_2D,
				ARBDepthTexture.GL_DEPTH_TEXTURE_MODE_ARB, GL11.GL_INTENSITY);

		// drwaScene
		grid.render();
		// !drawScene

	}

	/**
	 * unfinished.
	 */
	public static void renderLight() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glMatrixMode(GL_PROJECTION);
		glLoadMatrix(lightProjectionMatrix);
		glMatrixMode(GL_MODELVIEW);
		glLoadMatrix(lightViewMatrix);

		// drawScene
		grid.render();
		// !drawScene

	}

	/**
	 * unfinished.
	 */
	public static void renderEnd() {
		glDisable(GL_TEXTURE_2D);
		glMatrixMode(GL_TEXTURE);
		glLoadIdentity();
		glDisable(GL_TEXTURE_2D);
		glDisable(GL_TEXTURE_GEN_S);
		glDisable(GL_TEXTURE_GEN_T);
		glDisable(GL_TEXTURE_GEN_R);
		glDisable(GL_TEXTURE_GEN_Q);
	}

	/**
	 * This method initializes all OpenGL components needed.
	 */
	public static void initGL() {
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		glShadeModel(GL_SMOOTH);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
		glDepthFunc(GL_LEQUAL);
		glEnable(GL_CULL_FACE);
		glEnable(GL_NORMALIZE);

		whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
		lModelAmbient.put(0.5f).put(0.5f).put(0.5f).put(1.0f).flip();

		// Create the shadow map texture
		IntBuffer imageBuffer = BufferUtils.createIntBuffer(1);
		glGenTextures(imageBuffer);
		shadowMapTexture = imageBuffer.get(0);
		ByteBuffer b = ByteBuffer.allocateDirect(shadowMapSize * shadowMapSize)
				.order(ByteOrder.nativeOrder());
		glBindTexture(GL_TEXTURE_2D, shadowMapTexture);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, shadowMapSize,
				shadowMapSize, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_BYTE, b);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);

		glPushMatrix();
		glLoadIdentity();
		GLU.gluPerspective(45.0f, (float) w.getWidth() / (float) w.getHeight(),
				1.0f, 100.0f);
		cameraProjectionMatrix = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_MODELVIEW_MATRIX, cameraProjectionMatrix);

		glLoadIdentity();
		GLU.gluLookAt(cam.getX(), cam.getY(), cam.getZ(), 0.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f);
		cameraViewMatrix = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_MODELVIEW_MATRIX, cameraViewMatrix);

		glLoadIdentity();
		// lower the fov the higher quality the spot light
		GLU.gluPerspective(90f, 1.0f, 0.01f, 100.0f);
		// USE ORTHO for day light
		glOrtho(-1.25, 1.25, -1.25, 1.25, 0.01, 100);
		lightProjectionMatrix = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_MODELVIEW_MATRIX, lightProjectionMatrix);

		glLoadIdentity();
		GLU.gluLookAt(lightPosition.get(0), lightPosition.get(1),
				lightPosition.get(2), 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f);
		lightViewMatrix = BufferUtils.createFloatBuffer(16);
		glGetFloat(GL_MODELVIEW_MATRIX, lightViewMatrix);

		glPopMatrix();
		glEnable(GL_DEPTH_TEST);
	}

	/**
	 * This method initializes the lwjgl Display.
	 */
	public static void initDisplay() {
		canvas = new Canvas();
		w = Window.getWindow();
		w.add(canvas);
		w.setVisible(true);

		try {
			Display.setDisplayMode(new DisplayMode(w.getWidth(), w.getHeight()));
			Display.setParent(canvas);
			Display.setVSyncEnabled(true);
			Display.create();
		} catch (LWJGLException ex) {
			ex.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
	}

	/**
	 * This method checks if the System supports the needed OpenGL version.
	 */
	public static void checkOpenGL() {
		if (!GLContext.getCapabilities().OpenGL11) {
			System.err
					.println("Your OpenGL version doesn't support the required functionality.");
			Display.destroy();
			System.exit(1);
		}
	}

	/**
	 * Updates the Frames per Second Text label.
	 */
	public static void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			if (printFPS) {
				if (!exploreMode) {
					Main.window.setFPS("Theseus fps: " + fps
							+ "    Normal Mode");
				} else
					Main.window.setFPS("Theseus fps: " + fps
							+ "   Explore Mode");
			}
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}

	/**
	 * Checks if the Game is running.
	 * 
	 * @return {@code true} if the Game is running {@code false} else
	 */
	public static boolean isRunning() {
		return running;
	}

	/**
	 * Gets the Time.
	 * 
	 * @return The Time.
	 */
	private static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	/**
	 * Checks if the Display is resizable.
	 * 
	 * @return {@code true} if the Display is resizable {@code false} else
	 */
	public static boolean isResizable() {
		return resizable;
	}

	/**
	 * Sets the running.
	 * 
	 * @param running
	 *            the new running.
	 */
	public static void setRunning(boolean running) {
		TheseusGame.running = running;
	}

	/**
	 * Sets the exploreMode.
	 * 
	 * @param exploreMode
	 *            the new exploreMode.
	 */
	public static void setExploreMode(boolean exploreMode) {
		TheseusGame.exploreMode = exploreMode;
	}

}