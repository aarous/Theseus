package theseus_Game;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.GL12;
import org.newdawn.slick.opengl.Texture;

/**
 * The class "Skybox" holds the coordinates for the vertices and textures for
 * the Skybox.
 * 
 * @author Ahmed Arous
 */

public class Skybox {
	private static int skyDisplayList = glGenLists(1);
	private static TextureManager texMan = TextureManager.getInstance();

	/** The back Texture of the Skybox. */
	private Texture back = texMan.getSkyboxBack();
	/** The top Texture of the Skybox. */
	private Texture top = texMan.getSkyboxTop();
	/** The front Texture of the Skybox. */
	private Texture front = texMan.getSkyboxFront();
	/** The left Texture of the Skybox. */
	private Texture left = texMan.getSkyboxLeft();
	/** The right Texture of the Skybox. */
	private Texture right = texMan.getSkyboxRight();
	/** The bottom Texture of the Skybox. */
	private Texture bottom = texMan.getSkyboxBottom();

	/**
	 * The space between the Terrain and the Skybox in all directions except the
	 * bottom.
	 */
	private float spacer = 100f;

	/** The space between the Terrain and the Skybox in bottom direction. */
	private float SKY_BOTTOM = Map.FLOOR_HEIGHT - spacer;

	/** The x coordinate. */
	private float x;
	/** The z coordinate. */
	private float z;

	/**
	 * Constructor.
	 */
	public Skybox() {
		this.x = Grid.GRID_WIDTH + spacer;
		this.z = Grid.GRID_HEIGHT + spacer;
		draw();
	}

	/**
	 * This Method clamps the texture to the edge to prevent gaps between them.
	 */
	private void clampToEdge() {
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
	}

	/**
	 * This Method draws the Skybox.
	 */
	public void draw() {
		glNewList(skyDisplayList, GL_COMPILE);

		glPushMatrix();
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_CULL_FACE);

		// Front side
		clampToEdge();
		front.bind();
		glBegin(GL_QUADS);
		glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-spacer, spacer, -spacer);
		glTexCoord2f(1.0f, 0.0f);
		glVertex3f(x, spacer, -spacer);
		glTexCoord2f(1.0f, 1.0f);
		glVertex3f(x, SKY_BOTTOM, -spacer);
		glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-spacer, SKY_BOTTOM, -spacer);
		glEnd();

		// Back side
		clampToEdge();
		back.bind();
		glBegin(GL_QUADS);
		glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-spacer, SKY_BOTTOM, z);
		glTexCoord2f(0.0f, 1.0f);
		glVertex3f(x, SKY_BOTTOM, z);
		glTexCoord2f(0.0f, 0.0f);
		glVertex3f(x, spacer, z);
		glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-spacer, spacer, z);
		glEnd();

		// Top
		clampToEdge();
		top.bind();
		glBegin(GL_QUADS);
		glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-spacer, spacer, -spacer);
		glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-spacer, spacer, z);
		glTexCoord2f(0.0f, 0.0f);
		glVertex3f(x, spacer, z);
		glTexCoord2f(1.0f, 0.0f);
		glVertex3f(x, spacer, -spacer);
		glEnd();

		// bottom
		clampToEdge();
		bottom.bind();
		glBegin(GL_QUADS);
		glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-spacer, SKY_BOTTOM, -spacer);
		glTexCoord2f(0.0f, 1.0f);
		glVertex3f(x, SKY_BOTTOM, -spacer);
		glTexCoord2f(0.0f, 0.0f);
		glVertex3f(x, SKY_BOTTOM, z);
		glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-spacer, SKY_BOTTOM, z);
		glEnd();

		// Right side
		clampToEdge();
		right.bind();
		glBegin(GL_QUADS);
		glTexCoord2f(0.0f, 0.0f);
		glVertex3f(x, spacer, -spacer);
		glTexCoord2f(1.0f, 0.0f);
		glVertex3f(x, spacer, z);
		glTexCoord2f(1.0f, 1.0f);
		glVertex3f(x, SKY_BOTTOM, z);
		glTexCoord2f(0.0f, 1.0f);
		glVertex3f(x, SKY_BOTTOM, -spacer);
		glEnd();

		// Left side
		clampToEdge();
		left.bind();
		glBegin(GL_QUADS);
		glTexCoord2f(1.0f, 1.0f);
		glVertex3f(-spacer, SKY_BOTTOM, -spacer);
		glTexCoord2f(0.0f, 1.0f);
		glVertex3f(-spacer, SKY_BOTTOM, z);
		glTexCoord2f(0.0f, 0.0f);
		glVertex3f(-spacer, spacer, z);
		glTexCoord2f(1.0f, 0.0f);
		glVertex3f(-spacer, spacer, -spacer);
		clampToEdge();
		glEnd();

		glCullFace(GL_FRONT);
		glDisable(GL_CULL_FACE);
		glDisable(GL_TEXTURE_2D);
		glPopMatrix();

		glEndList();

	}

	/**
	 * This Method calls the DisplayList containing the Skybox.
	 */
	public void render() {
		glCallList(skyDisplayList);
	}

}