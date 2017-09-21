package theseus_Game;

import static org.lwjgl.opengl.GL11.*;

/**
 * The class "Map" holds the coordinates for the vertices and textures for the
 * terrain and walls.
 * 
 * @author Ahmed Arous
 */

public class Map {

	/** The repetition of Terrain texture. */
	private int repeats = Grid.GRID_WIDTH + Grid.GRID_HEIGHT / 20;

	/** The height of the floor. */
	public static final float FLOOR_HEIGHT = -0.5f;

	/** The height of the walls. */
	public static final float WALL_HEIGHT = 2f;

	/**
	 * This Method draws the Terrain.
	 */
	public void drawTerrain() {
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex3f(Grid.GRID_WIDTH - 1.5f, FLOOR_HEIGHT,
				Grid.GRID_HEIGHT - 0.5f);
		glTexCoord2f(repeats, 0);
		glVertex3f(-0.5f, FLOOR_HEIGHT, Grid.GRID_HEIGHT - 0.5f);
		glTexCoord2f(repeats, repeats);
		glVertex3f(-0.5f, FLOOR_HEIGHT, -0.5f);
		glTexCoord2f(0, repeats);
		glVertex3f(Grid.GRID_WIDTH - 1.5f, FLOOR_HEIGHT, -0.5f);
		glEnd();
	}

	/**
	 * The Class "MapSquare" represents a grid square.
	 * 
	 * @author Ahmed Arous
	 */
	static public class MapSquare {
		private float x;
		private float z;

		/**
		 * Constructor.
		 */
		public MapSquare(float x, float z) {
			this.x = x;
			this.z = z;
		}

		/**
		 * This Method draws a single Tile if needed.
		 */
		public void drawTile() {
			glBegin(GL_QUADS);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(-Grid.TILE_SIZE, FLOOR_HEIGHT, -Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(-Grid.TILE_SIZE, FLOOR_HEIGHT, Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(Grid.TILE_SIZE, FLOOR_HEIGHT, Grid.TILE_SIZE);
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(Grid.TILE_SIZE, FLOOR_HEIGHT, -Grid.TILE_SIZE);
			glEnd();
		}

		/**
		 * This Method draws a plate.
		 */
		public void drawPlate() {
			glBegin(GL_QUADS);

			// Front side
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(-Grid.TILE_SIZE, -0.48f, -Grid.TILE_SIZE);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(Grid.TILE_SIZE, -0.48f, -Grid.TILE_SIZE);
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(Grid.TILE_SIZE, FLOOR_HEIGHT, -Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(-Grid.TILE_SIZE, FLOOR_HEIGHT, -Grid.TILE_SIZE);

			// Back side
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(-Grid.TILE_SIZE, FLOOR_HEIGHT, Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(Grid.TILE_SIZE, FLOOR_HEIGHT, Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(Grid.TILE_SIZE, -0.48f, Grid.TILE_SIZE);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(-Grid.TILE_SIZE, -0.48f, Grid.TILE_SIZE);

			// Top
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(-Grid.TILE_SIZE, -0.48f, -Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(-Grid.TILE_SIZE, -0.48f, Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(Grid.TILE_SIZE, -0.48f, Grid.TILE_SIZE);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(Grid.TILE_SIZE, -0.48f, -Grid.TILE_SIZE);

			// Right side
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(Grid.TILE_SIZE, -0.48f, -Grid.TILE_SIZE);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(Grid.TILE_SIZE, -0.48f, Grid.TILE_SIZE);
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(Grid.TILE_SIZE, FLOOR_HEIGHT, Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(Grid.TILE_SIZE, FLOOR_HEIGHT, -Grid.TILE_SIZE);

			// Left side
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(-Grid.TILE_SIZE, FLOOR_HEIGHT, -Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(-Grid.TILE_SIZE, FLOOR_HEIGHT, Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(-Grid.TILE_SIZE, -0.48f, Grid.TILE_SIZE);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(-Grid.TILE_SIZE, -0.48f, -Grid.TILE_SIZE);

			glEnd();

		}

		/**
		 * This Method draws a Wall.
		 */
		public void drawWall() {
			glBegin(GL_QUADS);

			// Front side
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(-Grid.TILE_SIZE, WALL_HEIGHT, -Grid.TILE_SIZE);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(Grid.TILE_SIZE, WALL_HEIGHT, -Grid.TILE_SIZE);
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(Grid.TILE_SIZE, FLOOR_HEIGHT, -Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(-Grid.TILE_SIZE, FLOOR_HEIGHT, -Grid.TILE_SIZE);

			// Back side
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(-Grid.TILE_SIZE, FLOOR_HEIGHT, Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(Grid.TILE_SIZE, FLOOR_HEIGHT, Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(Grid.TILE_SIZE, WALL_HEIGHT, Grid.TILE_SIZE);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(-Grid.TILE_SIZE, WALL_HEIGHT, Grid.TILE_SIZE);

			// Top
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(-Grid.TILE_SIZE, WALL_HEIGHT, -Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(-Grid.TILE_SIZE, WALL_HEIGHT, Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(Grid.TILE_SIZE, WALL_HEIGHT, Grid.TILE_SIZE);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(Grid.TILE_SIZE, WALL_HEIGHT, -Grid.TILE_SIZE);

			// Right side
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(Grid.TILE_SIZE, WALL_HEIGHT, -Grid.TILE_SIZE);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(Grid.TILE_SIZE, WALL_HEIGHT, Grid.TILE_SIZE);
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(Grid.TILE_SIZE, FLOOR_HEIGHT, Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(Grid.TILE_SIZE, FLOOR_HEIGHT, -Grid.TILE_SIZE);

			// Left side
			glTexCoord2f(1.0f, 1.0f);
			glVertex3f(-Grid.TILE_SIZE, FLOOR_HEIGHT, -Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 1.0f);
			glVertex3f(-Grid.TILE_SIZE, FLOOR_HEIGHT, Grid.TILE_SIZE);
			glTexCoord2f(0.0f, 0.0f);
			glVertex3f(-Grid.TILE_SIZE, WALL_HEIGHT, Grid.TILE_SIZE);
			glTexCoord2f(1.0f, 0.0f);
			glVertex3f(-Grid.TILE_SIZE, WALL_HEIGHT, -Grid.TILE_SIZE);

			glEnd();

		}

		/**
		 * Gets the x coordinate of the map square.
		 * 
		 * @return the x coordinate
		 */
		public float getX() {
			return x;
		}

		/**
		 * Gets the z coordinate of the map square.
		 * 
		 * @return the z coordinate
		 */
		public float getZ() {
			return z;
		}

	}

}
