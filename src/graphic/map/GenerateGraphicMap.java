package graphic.map;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import data.player.ColorPlayers;
import data.territory.Territory;
import data.territory.building.CapitalBuilding;
import engine.map.Map;

/**
 * The graphic representation of the map
 * @author @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class GenerateGraphicMap {

	private SpriteBatch batch;
	private ArrayList<Sprite> sprites;
	private HashMap<Coordinates, Territory> territories;
	private Texture capitalSelectionedFont;
	private Texture territorySelectionedFont;
	private ArrayList<Texture> capital;
	private ArrayList<Texture> playersColor;
	
	private boolean hasNewMap;
	private Map newMap;
	
	private final String key = new String();

	/**
	 * 
	 * @param map The map
	 * @param batch The screen for display
	 */
	public GenerateGraphicMap(Map map, SpriteBatch batch) {
		this.batch = batch;
		this.hasNewMap = false;
		this.newMap = null;
		
		capital = new ArrayList<Texture>();
		playersColor = new ArrayList<Texture>();
        for(int i = 1; i <= 8; i++){
        	capital.add(new Texture(Gdx.files.internal("resources"+File.separator+"images"+File.separator+"256"+File.separator+"capital"+File.separator+"capital" + i + ".png")));
        }
        for(int i = 1; i <=6; i++){
        	playersColor.add(new Texture(Gdx.files.internal("resources"+File.separator+"images"+File.separator+"256"+File.separator+"ground"+File.separator+"neigh" + i + ".png")));
        }
        
        initialize(map);
		
	}

	/**
	 * Display the map graphically
	 * @param selectedTroopNativeTerritory
	 * @param selectedTroop
	 */
	public void generate(Territory selectedTroopNativeTerritory, Integer selectedTroop) {
		synchronized (key) {
			if(hasNewMap){
				actualize(newMap);
				hasNewMap = false;
			}
		}
		batch.begin();
		for (Sprite s : sprites) {
			Territory spriteTerritory = territories.get(new Coordinates((int) s.getX(), (int) s.getY()));
			s.draw(batch);
			
			//If the territory contains troops
			if(spriteTerritory.getBuilding() != null && spriteTerritory.getBuilding().getClass() != CapitalBuilding.class){
				String buildingName = spriteTerritory.getBuilding().getClass().getSimpleName();
				Texture buildingTexture = new Texture(Gdx.files.internal("resources"+File.separator+"images"+File.separator+"256"+File.separator+"building"+File.separator+ buildingName + ".png"));
				batch.draw(buildingTexture, s.getX(), s.getY());
			}
			if(!spriteTerritory.getTroops().isEmpty()){
				//For all the troops on the territory
				if(selectedTroopNativeTerritory != null){
					if(!selectedTroopNativeTerritory.equals(spriteTerritory)){
						for(int index=0; index<spriteTerritory.getTroops().size() ; index++){
							String soldierName = spriteTerritory.getTroops().get(index).getSoldiers().get(0).getClass().getSimpleName();
							Texture soldierTexture = new Texture(Gdx.files.internal("resources"+File.separator+"images"+File.separator+"troops"+File.separator+ soldierName + ".png"));
							batch.draw(soldierTexture, (float) (s.getX() + s.getWidth()/2 + (Math.cos((index*2*Math.PI)/spriteTerritory.getTroops().size()) * (s.getWidth()/3))) - (soldierTexture.getWidth()/2), (float) (s.getY() + s.getHeight()/2 + (Math.sin((index*2*Math.PI)/spriteTerritory.getTroops().size()) * (s.getWidth()/3))) - (soldierTexture.getHeight()/2));
						}
					}
					else{
						for(int index=0; index<spriteTerritory.getTroops().size() ; index++){
							String soldierName = spriteTerritory.getTroops().get(index).getSoldiers().get(0).getClass().getSimpleName();
							Texture soldierTexture = null;
							if(index == selectedTroop){
								soldierTexture = new Texture(Gdx.files.internal("resources"+File.separator+"images"+File.separator+"troops"+File.separator+ soldierName + "Button.png"));
							}
							else{
								soldierTexture = new Texture(Gdx.files.internal("resources"+File.separator+"images"+File.separator+"troops"+File.separator+ soldierName + ".png"));
							}
							batch.draw(soldierTexture, (float) (s.getX() + s.getWidth()/2 + (Math.cos((index*2*Math.PI)/spriteTerritory.getTroops().size()) * (s.getWidth()/3))) - (soldierTexture.getWidth()/2), (float) (s.getY() + s.getHeight()/2 + (Math.sin((index*2*Math.PI)/spriteTerritory.getTroops().size()) * (s.getWidth()/3))) - (soldierTexture.getHeight()/2));
						}
					}
				}
				else{
					for(int index=0; index<spriteTerritory.getTroops().size() ; index++){
						String soldierName = spriteTerritory.getTroops().get(index).getSoldiers().get(0).getClass().getSimpleName();
						Texture soldierTexture = new Texture(Gdx.files.internal("resources"+File.separator+"images"+File.separator+"troops"+File.separator+ soldierName + ".png"));
						batch.draw(soldierTexture, (float) (s.getX() + s.getWidth()/2 + (Math.cos((index*2*Math.PI)/spriteTerritory.getTroops().size()) * (s.getWidth()/3))) - (soldierTexture.getWidth()/2), (float) (s.getY() + s.getHeight()/2 + (Math.sin((index*2*Math.PI)/spriteTerritory.getTroops().size()) * (s.getWidth()/3))) - (soldierTexture.getHeight()/2));
					}
				}
			}
			
			if(spriteTerritory.getOwner() != null){
				batch.setColor(ColorPlayers.getInstance().getColor(spriteTerritory.getOwner().getNumber()));
				for(int i = 1; i <=6; i++){
					Territory neigh = spriteTerritory.getNeigh(i);
					if((neigh == null) || !spriteTerritory.getOwner().equals(neigh.getOwner())){
						batch.draw(playersColor.get(i-1), s.getX(), s.getY());
					}
				}
				batch.setColor(Color.WHITE);
			}
			
			//If the territory is selected
			if (((SpriteTerritory) s).isSelected()) {
				//If it's a capital
				if (spriteTerritory.isCapital()) {
					batch.draw(capitalSelectionedFont, s.getX(), s.getY());
				} else {
					batch.draw(territorySelectionedFont, s.getX(), s.getY());
				}
			}
			
		}
		batch.end();
	}

	/**
	 * Initialize the graphic representation from the engine representation
	 * @param map The engine map
	 */
	public void initialize(Map map){
		Territory ancientTerritory = null;
		Texture currentTexture;
		Coordinates pos = new Coordinates(0, 0);
		sprites = new ArrayList<Sprite>();
		territories = new HashMap<Coordinates, Territory>();
        

		capitalSelectionedFont = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "256" + File.separator + "ground" + File.separator + "selected.png"));
		territorySelectionedFont = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "256" + File.separator + "ground" + File.separator + "selected.png"));

		for (Territory t : map) {
			if (t.isCapital()) {
				currentTexture = capital.get(t.getOwner().getNumber());
			} else {
				int i = (int) (Math.random() * 4);
				currentTexture = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "256" + File.separator + "ground" + File.separator + t.getClass().getSimpleName() +i+ ".png"));
			}

			if (ancientTerritory == null) {
				pos.setCoord(0, 0);
			} else {
				for (int i = 1; i <= 6; i++) {
					if (ancientTerritory.getNeigh(i) != null) {
						if (ancientTerritory.getNeigh(i).equals(t)) {
							switch (i) {

							case 3:
								pos.setCoord(pos.getX() + currentTexture.getWidth(), pos.getY());
								break;
							case 4:
								pos.setCoord((int) (pos.getX() + (currentTexture.getWidth() / 2)), (int) (pos.getY() - (currentTexture.getHeight() / 1.32)));
								break;
							case 5:
								pos.setCoord((int) (pos.getX() - (currentTexture.getWidth() / 2)), (int) (pos.getY() - (currentTexture.getHeight() / 1.32)));
								break;
							case 6:
								pos.setCoord(pos.getX() - currentTexture.getWidth(), pos.getY());
								break;
							}
							break;
						}
					}
				}
			}
			SpriteTerritory currentSprite = new SpriteTerritory(currentTexture);
			currentSprite.setPosition(pos.getX(), pos.getY());
			sprites.add(currentSprite);
			territories.put(new Coordinates(pos.getX(), pos.getY()), t);
			ancientTerritory = t;
		}
	}
	
	/**
	 * Actualize the graphic representation of the map from the engine representation
	 * @param map The engine representation of the map
	 */
	public void actualize(Map map){
		Territory ancientTerritory = null;
		Texture currentTexture;
		Coordinates pos = new Coordinates(0, 0);
		territories.clear();

		for (Territory t : map) {
			if (t.isCapital()) {
				currentTexture = capital.get(t.getOwner().getNumber());
			} else {
				int i = (int) (Math.random() * 4);
				currentTexture = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "256" + File.separator + "ground" + File.separator + t.getClass().getSimpleName() +i+ ".png"));
			}

			if (ancientTerritory == null) {
				pos.setCoord(0, 0);
			} else {
				for (int i = 1; i <= 6; i++) {
					if (ancientTerritory.getNeigh(i) != null) {
						if (ancientTerritory.getNeigh(i).equals(t)) {
							switch (i) {

							case 3:
								pos.setCoord(pos.getX() + currentTexture.getWidth(), pos.getY());
								break;
							case 4:
								pos.setCoord((int) (pos.getX() + (currentTexture.getWidth() / 2)), (int) (pos.getY() - (currentTexture.getHeight() / 1.32)));
								break;
							case 5:
								pos.setCoord((int) (pos.getX() - (currentTexture.getWidth() / 2)), (int) (pos.getY() - (currentTexture.getHeight() / 1.32)));
								break;
							case 6:
								pos.setCoord(pos.getX() - currentTexture.getWidth(), pos.getY());
								break;
							}
							break;
						}
					}
				}
			}
			territories.put(new Coordinates(pos.getX(), pos.getY()), t);
			ancientTerritory = t;
		}
	}
	
	/**
	 * Actualize only one territory of the map
	 * @param map The map
	 * @param x The position x
	 * @param y The position y
	 * @param type The class of the territory
	 */
	public void actualize(Map map ,float x,float y, String type){
		for(Sprite s: sprites){
			if (s.getBoundingRectangle().contains(x, y)) {
				s.getTexture().getTextureData().prepare();
				if (s.getTexture().getTextureData().consumePixmap().getPixel((int) (x - s.getX()), (int) (y - s.getY())) != -256) {	
					if(type.equals("Capital")){
						Texture texture = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "256" + File.separator + "ground" + File.separator + "capital.png"));
						s.setTexture(texture);
					}
					else{
						int i = (int) (Math.random() * 4);
						Texture texture = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "256" + File.separator + "ground" + File.separator + type +i+ ".png"));
						s.setTexture(texture);
					}
				}
				break;
			}
		}
		
	}
	public ArrayList<Sprite> getSprites() {
		return sprites;
	}

	public HashMap<Coordinates, Territory> getTerritoriesByCoordinates() {
		return territories;
	}
	
	/**
	 * Redefined the map
	 * @param map The new map
	 */
	public void setNewMap(Map map){
		synchronized (key) {
			newMap = map;
			hasNewMap = true;
		}
	}

}
