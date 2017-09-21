package theseus_Game;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 * The class "TextureManager" loads all Textures that will be used and passes
 * them for the bindings.
 * 
 * @author Ahmed Arous
 */
public class TextureManager {

	/** The back Texture of the Skybox. */
	private Texture skyboxBack = null;
	/** The top Texture of the Skybox. */
	private Texture skyboxTop = null;
	/** The front Texture of the Skybox. */
	private Texture skyboxFront = null;
	/** The left Texture of the Skybox. */
	private Texture skyboxLeft = null;
	/** The right Texture of the Skybox. */
	private Texture skyboxRight = null;
	/** The bottom Texture of the Skybox. */
	private Texture skyboxBottom = null;
	/** The Floor Texture. */
	private Texture floorTexture = null;
	/** The Wall Texture. */
	private Texture wallTexture = null;
	/** The EndField Texture. */
	private Texture end = null;

	/** The TextureManager instance. */
	private static TextureManager instance;

	/**
	 * Static method to retrieve the one and only reference to the
	 * TextureManager.
	 * 
	 * @return the reference of the TextureManager
	 */
	public static TextureManager getInstance() {
		if (instance == null) {
			instance = new TextureManager();
		}
		return instance;
	}

	/**
	 * The hidden constructor of the singleton.
	 */
	private TextureManager() {
		try {
			this.skyboxBack = TextureLoader
					.getTexture("PNG", new FileInputStream(new File(
							"res/images/skybox/back.png")));
			this.skyboxTop = TextureLoader.getTexture("PNG",
					new FileInputStream(new File("res/images/skybox/top.png")));
			this.skyboxFront = TextureLoader
					.getTexture("PNG", new FileInputStream(new File(
							"res/images/skybox/front.png")));
			this.skyboxLeft = TextureLoader
					.getTexture("PNG", new FileInputStream(new File(
							"res/images/skybox/left.png")));
			this.skyboxRight = TextureLoader
					.getTexture("PNG", new FileInputStream(new File(
							"res/images/skybox/right.png")));
			this.skyboxBottom = TextureLoader.getTexture("PNG",
					new FileInputStream(
							new File("res/images/skybox/bottom.png")));
			this.floorTexture = TextureLoader.getTexture("PNG",
					new FileInputStream(new File("res/images/floor.png")));

			this.wallTexture = TextureLoader.getTexture("PNG",
					new FileInputStream(new File("res/images/wall.png")));

			this.end = TextureLoader.getTexture("PNG", new FileInputStream(
					new File("res/images/end.png")));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the back Texture of the Skybox.
	 * 
	 * @return The back Texture of the Skybox.
	 */
	public Texture getSkyboxBack() {
		return skyboxBack;
	}

	/**
	 * Gets the top Texture of the Skybox.
	 * 
	 * @return The top Texture of the Skybox.
	 */
	public Texture getSkyboxTop() {
		return skyboxTop;
	}

	/**
	 * Gets the front Texture of the Skybox.
	 * 
	 * @return The front Texture of the Skybox.
	 */
	public Texture getSkyboxFront() {
		return skyboxFront;
	}

	/**
	 * Gets the left Texture of the Skybox.
	 * 
	 * @return The left Texture of the Skybox.
	 */
	public Texture getSkyboxLeft() {
		return skyboxLeft;
	}

	/**
	 * Gets the right Texture of the Skybox.
	 * 
	 * @return The right Texture of the Skybox.
	 */
	public Texture getSkyboxRight() {
		return skyboxRight;
	}

	/**
	 * Gets the bottom Texture of the Skybox.
	 * 
	 * @return The bottom Texture of the Skybox.
	 */
	public Texture getSkyboxBottom() {
		return skyboxBottom;
	}

	/**
	 * Gets the floor Texture.
	 * 
	 * @return The floor Texture.
	 */
	public Texture getFloorTexture() {
		return floorTexture;
	}

	/**
	 * Gets the wall Texture.
	 * 
	 * @return The wall Texture.
	 */
	public Texture getWallTexture() {
		return wallTexture;
	}

	/**
	 * Gets the EndField Texture.
	 * 
	 * @return The end Texture.
	 */
	public Texture getEnd() {
		return end;
	}

}
