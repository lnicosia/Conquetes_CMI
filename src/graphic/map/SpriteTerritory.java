package graphic.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Redefinition of sprites for territory adaptation
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class SpriteTerritory extends Sprite {

	private boolean isSelected;

	/**
	 * 
	 * @param texture The texture of the territory
	 */
	public SpriteTerritory(Texture texture) {
		super(texture);
		this.isSelected = false;
	}

	public void changeSelectionState() {
		isSelected = !isSelected;
	}

	public boolean isSelected() {
		return isSelected;
	}
}
