package theseus_Game;

import static org.lwjgl.opengl.GL11.*;
import java.util.ArrayList;
import org.newdawn.slick.opengl.Texture;
import theseus_Game.Map.MapSquare;
import theseus_data.Structure;
import theseus.Main;

/**
 * The class "Grid" handles the logical Implementation of the virtual world, by
 * either using the given structure extracted from an Image or by completely
 * generating it itself.
 * 
 * @author Ahmed Arous
 */

public class Grid {
	/** The Structure instance. */
	private static Structure structure = Main.structure;

	/** The TextureManager. */
	private static TextureManager texMan = TextureManager.getInstance();

	/** The Grid instance. */
	private static Grid instance;

	/** The Map or Terrain. */
	private Map map;

	/** The Start and End coordinates. */
	private int endX, endZ, startZ, startX;

	/** The Floor Texture. */
	private Texture floorTexture = texMan.getFloorTexture();

	/** The Wall Texture. */
	private Texture wallTexture = texMan.getWallTexture();

	/** The Wall Texture. */
	private Texture endTexture = texMan.getEnd();

	/** The MapSquares. */
	private MapSquare[][] grid = new MapSquare[GRID_WIDTH][GRID_HEIGHT];

	/** The DisplayLists containing the terrain. */
	private int displayList = glGenLists(1);

	/** The DisplayLists containing the walls. */
	private int wallDisplayList = glGenLists(1);

	/** Size of a Tile. */
	public static final float TILE_SIZE = 0.5f;

	/** The actual data structure given by Structure. */
	public int data[][] = structure.getDatenstruktur();

	/** The total height of the Grid. */
	public static int GRID_HEIGHT = structure.getDatenstruktur()[0].length;

	/** The total width of the Grid. */
	public static int GRID_WIDTH = structure.getDatenstruktur().length;

	/** ArrayList containing the walls. */
	public static ArrayList<MapSquare> walls = new ArrayList<MapSquare>();

	/**
	 * Static method to retrieve the one and only reference to the Grid.
	 * 
	 * @return the reference of the Grid
	 */
	public static Grid getInstance() {
		if (instance == null) {
			instance = new Grid();
		}
		return instance;
	}

	/**
	 * The hidden constructor of the singleton.
	 */
	public Grid() {
		map = new Map();
		for (int x = 0; x < GRID_WIDTH; x++) {
			for (int z = 0; z < GRID_HEIGHT; z++) {
				grid[x][z] = new MapSquare(x, z);
			}
		}
	}

	/**
	 * Method used for testing purposes.
	 */
	public void draw() {
		glNewList(displayList, GL_COMPILE);
		for (int x = 0; x < GRID_WIDTH; x++) {
			for (int z = 0; z < GRID_HEIGHT; z++) {
				glPushMatrix();
				glTranslatef(x, 0, z);
				glEnable(GL_TEXTURE_2D);
				floorTexture.bind();
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
				grid[x][z].drawTile();
				glDisable(GL_TEXTURE_2D);
				glPopMatrix();
			}
		}
		glEndList();

		glNewList(wallDisplayList, GL_COMPILE);
		int i = 0;
		for (int x = 0; x < GRID_WIDTH; x++) {
			for (int z = 0; z < GRID_HEIGHT; z++) {
				i++;
				if (i % 2.75f == 0) {
					glPushMatrix();
					glTranslatef(x, 0, z);
					glEnable(GL_TEXTURE_2D);
					wallTexture.bind();
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
							GL_LINEAR);
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
							GL_LINEAR);
					grid[x][z].drawWall();
					glDisable(GL_TEXTURE_2D);
					glPopMatrix();
				}
			}
		}
		glEndList();
	}

	/**
	 * This Method draws the terrain and a Wall for each "1" given by the
	 * Structure.
	 */
	public void drawDatenstruktur() {
		glNewList(displayList, GL_COMPILE);
		glPushMatrix();
		glEnable(GL_TEXTURE_2D);
		floorTexture.bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		map.drawTerrain();
		glDisable(GL_TEXTURE_2D);
		glPopMatrix();
		glEndList();

		glNewList(wallDisplayList, GL_COMPILE);
		for (int x = 0; x < data.length; x++) {
			for (int z = 0; z < data[0].length; z++) {
				if (data[x][z] == 1) {
					glPushMatrix();
					glTranslatef(x, 0, z);
					glEnable(GL_TEXTURE_2D);
					wallTexture.bind();
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
							GL_LINEAR);
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
							GL_LINEAR);
					walls.add(grid[x][z]);
					grid[x][z].drawWall();
					glDisable(GL_TEXTURE_2D);
					glPopMatrix();
				}
				if (data[x][z] == 3) {
					glPushMatrix();
					glTranslatef(x, 0.1f, z);
					glEnable(GL_TEXTURE_2D);
					endTexture.bind();
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER,
							GL_LINEAR);
					glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER,
							GL_LINEAR);
					grid[x][z].drawPlate();
					glDisable(GL_TEXTURE_2D);
					glPopMatrix();
				}
			}
		}
		glEndList();
	}

	/**
	 * This Method extracts the coordinates of the Start and End position out of
	 * the structure.
	 */
	public void getPositions() {
		int c = 0;
		for (int x = 0; x < data.length; x++) {
			for (int z = 0; z < data[0].length; z++) {
				if (data[x][z] == 3 && c == 0) {
					startX = x;
					startZ = z;
					c = 1;
				} else if (data[x][z] == 3 && c == 1) {
					endX = x;
					endZ = z;
					c = 0;
				}
			}
		}

		int rnd = (int) (Math.round(Math.random()));
		if (rnd == 1) {
			int ph;
			ph = startX;
			startX = endX;
			endX = ph;
			ph = startZ;
			startZ = endZ;
			endZ = ph;
		}

		System.out.println("rnd:" + rnd);
		System.out.println("Die Starposition: " + "(" + endX + " / " + endZ
				+ ")" + "	Die Zielposition: " + "(" + startX + " / " + startZ
				+ ")");
	}

	/**
	 * This Method calls the DisplayLists containing the terrain and walls.
	 */
	public void render() {
		glCallList(displayList);
		glCallList(wallDisplayList);
	}

	/**
	 * Gets the x coordinate of the End position.
	 * 
	 * @return The x coordinate.
	 */
	public int getEndX() {
		return endX;
	}

	/**
	 * Gets the z coordinate of the End position.
	 * 
	 * @return The z coordinate.
	 */
	public int getEndZ() {
		return endZ;
	}

	/**
	 * Gets the z coordinate of the Start position.
	 * 
	 * @return The z coordinate.
	 */
	public int getStartZ() {
		return startZ;
	}

	/**
	 * Gets the x coordinate of the Start position.
	 * 
	 * @return The x coordinate.
	 */
	public int getStartX() {
		return startX;
	}

}
