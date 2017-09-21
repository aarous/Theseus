package theseus_Game;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.glu.GLU;
import theseus_Game.Map.MapSquare;
import theseus_Game.TheseusGame;
import static org.lwjgl.opengl.GL11.*;

/**
 * The class "Camera" handles the Keyboard and Mouse Input by the User and the
 * 3D navigation in Game.
 * 
 * @author Ahmed Arous
 */
public class Camera {

	/** The x Position of the Camera. */
	private float xPosition = 0;
	/** The y Position of the Camera. */
	private float yPosition = 0;
	/** The z Position of the Camera. */
	private float zPosition = 0;
	/** The x Position of the Camera. */
	private float rotationX = 0;
	/** The x Position of the Camera. */
	private float rotationY = 0;
	/** The x Position of the Camera. */
	private float rotationZ = 0;
	/** The previous x and z Position of the Camera. */
	private float xPrevious, zPrevious;
	/** The Grid Instance */
	private static Grid grid = Grid.getInstance();
	/** The Field of View */
	private static float fov = 60;
	/** The Aspect Ratio */
	private static float aspectRatio;
	/** The near clipping plane */
	private static float zNear = 0.1f;
	/** The far clipping plane */
	private static float zFar = 1000f;
	/** the maximum angle at which you can look up. */
	private static float maxLookUp = 90;
	/** the maximum angle at which you can look down. */
	private static float maxLookDown = -90;
	/** the mouse sensibility. */
	private static float mouseSpeed = 2;
	/** the speed of the Camera movement. */
	private static float walkingSpeed = 20;
	/** the elapsed time since the last frame. */
	private static int delta = 20;

	/**
	 * Constructor.
	 */
	public Camera(float aspectRatio, float x, float y, float z) {
		this.aspectRatio = aspectRatio;
		this.xPosition = x;
		this.yPosition = y;
		this.zPosition = z;
	}

	/**
	 * This method handles the Mouse Input and Camera rotation.
	 */
	public void mouseInput() {
		if (!Mouse.isGrabbed())
			return;
		float mouseDX = Mouse.getDX() * mouseSpeed * 0.16f;
		float mouseDY = Mouse.getDY() * mouseSpeed * 0.16f;
		if (rotationY + mouseDX >= 360) {
			rotationY = rotationY + mouseDX - 360;
		} else if (rotationY + mouseDX < 0) {
			rotationY = 360 - rotationY + mouseDX;
		} else {
			rotationY += mouseDX;
		}
		if (rotationX - mouseDY >= maxLookDown
				&& rotationX - mouseDY <= maxLookUp) {
			rotationX += -mouseDY;
		} else if (rotationX - mouseDY < maxLookDown) {
			rotationX = maxLookDown;
		} else if (rotationX - mouseDY > maxLookUp) {
			rotationX = maxLookUp;
		}
	}

	/**
	 * This method handles the Keyboard Input, Camera movement and the collision
	 * events.
	 */
	public void keyboardInput() {
		xPrevious = xPosition;
		zPrevious = zPosition;

		boolean keyUp = Keyboard.isKeyDown(Keyboard.KEY_UP)
				|| Keyboard.isKeyDown(Keyboard.KEY_W);
		boolean keyDown = Keyboard.isKeyDown(Keyboard.KEY_DOWN)
				|| Keyboard.isKeyDown(Keyboard.KEY_S);
		boolean keyLeft = Keyboard.isKeyDown(Keyboard.KEY_LEFT)
				|| Keyboard.isKeyDown(Keyboard.KEY_A);
		boolean keyRight = Keyboard.isKeyDown(Keyboard.KEY_RIGHT)
				|| Keyboard.isKeyDown(Keyboard.KEY_D);
		boolean flyUp = Keyboard.isKeyDown(Keyboard.KEY_SPACE);
		boolean flyDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);

		if (keyUp && keyRight && !keyLeft && !keyDown) {
			moveFromLook((walkingSpeed * 0.0002f) * delta, 0,
					(-walkingSpeed * 0.0002f) * delta);
		}
		if (keyUp && keyLeft && !keyRight && !keyDown) {
			moveFromLook((-walkingSpeed * 0.0002f) * delta, 0,
					(-walkingSpeed * 0.0002f) * delta);
		}
		if (keyDown && keyLeft && !keyRight && !keyUp) {
			moveFromLook((-walkingSpeed * 0.0002f) * delta, 0,
					(walkingSpeed * 0.0002f) * delta);
		}
		if (keyDown && keyRight && !keyLeft && !keyUp) {
			moveFromLook((walkingSpeed * 0.0002f) * delta, 0,
					(walkingSpeed * 0.0002f) * delta);
		}
		if (keyUp && !keyLeft && !keyRight && !keyDown) {
			moveFromLook(0, 0, (-walkingSpeed * 0.0002f) * delta);
		}
		if (keyDown && !keyUp && !keyLeft && !keyRight) {
			moveFromLook(0, 0, (walkingSpeed * 0.0002f) * delta);
		}
		if (keyLeft && !keyRight && !keyUp && !keyDown) {
			moveFromLook((-walkingSpeed * 0.0002f) * delta, 0, 0);
		}
		if (keyRight && !keyLeft && !keyUp && !keyDown) {
			moveFromLook((walkingSpeed * 0.0002f) * delta, 0, 0);
		}
		if (flyUp && !flyDown && TheseusGame.exploreMode) {
			yPosition += (walkingSpeed * 0.0002f) * delta;
		}
		if (flyDown && !flyUp) {
			if (getY() > 0.75) {
				yPosition -= (walkingSpeed * 0.0002f) * delta;
			}
		}

		if (yPosition < Map.WALL_HEIGHT) {
			collision();
		}

		if (Math.abs(grid.getEndX() - xPosition) < Grid.TILE_SIZE 
				&& Math.abs(grid.getEndZ() - zPosition) < Grid.TILE_SIZE) {
			TheseusGame.setRunning(false);
			System.out.println("Game Over!");
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_Q) {
				if (Keyboard.getEventKeyState()) {
					if (TheseusGame.exploreMode) {
						TheseusGame.setExploreMode(false);
						setWalkingSpeed(20);
					} else {
						TheseusGame.setExploreMode(true);
						setWalkingSpeed(100);
					}
				}
			}
		}
	}

	/**
	 * This method applies the Camera movement in the looking direction.
	 * 
	 * @param dx
	 *            the movement along the x-axis
	 * @param dy
	 *            the movement along the y-axis
	 * @param dz
	 *            the movement along the z-axis
	 */
	public void moveFromLook(float dx, float dy, float dz) {
		float nextX = this.xPosition;
		float nextY = this.yPosition;
		float nextZ = this.zPosition;

		float hypotenuseX = dx;
		float adjacentX = hypotenuseX
				* (float) Math.cos(Math.toRadians(rotationY - 90));
		float oppositeX = (float) Math.sin(Math.toRadians(rotationY - 90))
				* hypotenuseX;
		nextZ += adjacentX;
		nextX -= oppositeX;

		nextY += dy;

		float hypotenuseZ = dz;
		float adjacentZ = hypotenuseZ
				* (float) Math.cos(Math.toRadians(rotationY));
		float oppositeZ = (float) Math.sin(Math.toRadians(rotationY))
				* hypotenuseZ;
		nextZ += adjacentZ;
		nextX -= oppositeZ;

		this.xPosition = nextX;
		this.yPosition = nextY;
		this.zPosition = nextZ;
	}

	/**
	 * This method applies the perspective Projection Matrix.
	 */
	public void applyProjectionMatrix() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(fov, aspectRatio, zNear, zFar);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	/**
	 * This method translates the Camera position and resets the MODELVIEW
	 * matrix. basically what gluLookat does.
	 */
	public void applyModelviewMatrix() {
		glLoadIdentity();
		glRotatef(rotationX, 1, 0, 0);
		glRotatef(rotationY, 0, 1, 0);
		glRotatef(rotationZ, 0, 0, 1);
		glTranslatef(-xPosition, -yPosition, -zPosition);
	}

	/**
	 * This method implements a simple 2D collision.
	 */
	public void collision() {
		for (MapSquare wall : Grid.walls) {
			if (Math.abs(wall.getX() - xPosition) < Grid.TILE_SIZE + 0.2f
					&& Math.abs(wall.getZ() - zPosition) < Grid.TILE_SIZE + 0.2f) {
				xPosition = xPrevious;
				zPosition = zPrevious;
			}
		}
	}

	/**
	 * Gets the x coordinate of the Camera position.
	 * 
	 * @return The x coordinate.
	 */
	public float getX() {
		return xPosition;
	}

	/**
	 * Gets the y coordinate of the Camera position.
	 * 
	 * @return The y coordinate.
	 */
	public float getY() {
		return yPosition;
	}

	/**
	 * Gets the z coordinate of the Camera position.
	 * 
	 * @return The z coordinate.
	 */
	public float getZ() {
		return zPosition;
	}

	/**
	 * Gets the Field of View in degrees.
	 * 
	 * @return fov.
	 */
	public static float getFov() {
		return fov;
	}

	/**
	 * Gets the distance to the near clipping plane.
	 * 
	 * @return zNear.
	 */
	public static float getzNear() {
		return zNear;
	}

	/**
	 * Gets the distance to the far clipping plane.
	 * 
	 * @return zFar.
	 */
	public static float getzFar() {
		return zFar;
	}

	/**
	 * Sets the walking speed.
	 * 
	 * @param walkingSpeed
	 *            the new walkingSpeed.
	 */
	public static void setWalkingSpeed(float walkingSpeed) {
		Camera.walkingSpeed = walkingSpeed;
	}

}