package graphic.screen;

import data.localisation.Localisation;
import data.player.Player;
import data.territory.Territory;
import data.territory.building.ResourceIncrease;
import data.territory.resource.Resource;
import engine.exception.GameNotSaveException;
import engine.game.GameStockage;
import engine.game.player.EntityEngine;
import engine.game.player.IAEngine;
import engine.game.player.PlayerEngine;
import engine.map.MapWater;
import graphic.map.Coordinates;
import graphic.map.GenerateGraphicMap;
import graphic.map.SpriteTerritory;
import graphic.screen.GuiGame.HelpListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.openal.AL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * 
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class GuiEditor implements Screen, GuiStream {

	private InputGame stage;
	
	// Un Batch = une partie de l'écran à charger
	private SpriteBatch mapBatch, backgroundBatch, menuBatch, typeSelectionBatch;

	// Une caméra par Batch
	private OrthographicCamera camera, backgroundCamera, menuCamera, typeSelectionCamera;
	
	private Texture background;
	private Sprite lateralBar, bottomBar;
	
	// Textures des drapeaux qui affichent le nom des joueurs
	private ArrayList<Texture> ribbons;
	
	// Textures des panneaux qui affichent les infos suivantes
	private Texture bTerritory, bProd, bStock;
		
	// Nom, Sprite, SpriteTerritory et Texture de chaque type de territoire
	private ArrayList<SpriteTerritory> typeSprites;
	private ArrayList<String> typeNames;
	private ArrayList<Texture> typeTextures;
	
	// Nom du type actuellement séléctionné
	private String selectedType;
	
	private MapWater map;
	private GenerateGraphicMap ggm;
	private BitmapFont bf;
	private ArrayList<Sprite> sprites;
	private int ancientSpriteSelectedPos, ancientTypeSelectedPos;
	private HashMap<Coordinates, Territory> territories;
	private Coordinates selectedTerritoryCoordinates, selectedTypeCoordinates;
	private Integer selectedTroop;
	private Territory selectedTroopNativeTerritory;
	private String errorMessage;
	
	// Gestion des joueurs
	private ArrayList<EntityEngine> players;
	private int capitalCount;
	private ArrayList<CheckBox[]> checkBoxes;
	private ArrayList<Coordinates> checkBoxesC;
	private Integer ancientIndex;
	
	// Boutons
	private Skin skin, uiskin;
	private TextButton changeProd, changeStock, autoProd, autoProdHelp, okProd, okStock;
	private String autoProdHelpText;
	
	private TextField changeProdField, changeStockField;
	private String changeProdValue, changeStockValue;

	public GuiEditor() {
		//Redirection des messages d'erreur
		try{
			System.setOut(new RedirectOutputStream(this));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void show() {
		map = new MapWater(10);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		errorMessage = "";

		changeProdValue = "0";
		changeStockValue = "0";
		
		ancientIndex=null;
		capitalCount = 0;
		players = new ArrayList<EntityEngine>();
		checkBoxes = new ArrayList<CheckBox[]>();
		checkBoxesC = new ArrayList<Coordinates>();
		
		autoProdHelpText=Localisation.getInstance().getMessage("AutoProdHelpText");
		
		selectedType = null;
		selectedTerritoryCoordinates = null;
		selectedTypeCoordinates = null;
		ancientSpriteSelectedPos = -1;
		ancientTypeSelectedPos = -1;

		selectedTroop = null;
		selectedTroopNativeTerritory = null;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.update();
		
		backgroundCamera = new OrthographicCamera();
		backgroundCamera.setToOrtho(false, w, h);
		backgroundCamera.update();
		
		menuCamera = new OrthographicCamera();
		menuCamera.setToOrtho(false, w, h);
		menuCamera.update();
		
		typeSelectionCamera = new OrthographicCamera();
		typeSelectionCamera.setToOrtho(false, w, h);
		typeSelectionCamera.update();
		/*Skin skin = new Skin(Gdx.files.internal("resources/menu.json"), new TextureAtlas(Gdx.files.internal("resources/images/buttons/menu.pack")));
		mapSize=new TextField("Map Size:",skin);
		stage.addActor(mapSize);*/
		bf = new BitmapFont(Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.fnt"), Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.png"), false);
		
		mapBatch = new SpriteBatch();
		backgroundBatch = new SpriteBatch();
		menuBatch = new SpriteBatch();
		typeSelectionBatch = new SpriteBatch();
		
		ggm = new GenerateGraphicMap(map, mapBatch);
		
		background = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "font.jpg"));
		lateralBar = new Sprite(new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "lateralbar.png")));
		bottomBar = new Sprite(new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "bottombar.png")));

		// ArrayList qui contient le nom de tous les types de territoire
		typeNames = new ArrayList<String>();
		typeNames.add("Water");
		typeNames.add("Plain");
		typeNames.add("Forest");
		typeNames.add("Desert");
		typeNames.add("Mountain");
		typeNames.add("Capital");

		// ArrayList qui contient les sprites correspondant à chaque type de territoire
		typeSprites = new ArrayList<SpriteTerritory>();
		typeTextures = new ArrayList<Texture>();
        for( int i = 0; i < typeNames.size(); i++){
        	// Cas où le type est "capital" : le fichier n'a pas de "0" à la fin de son nom
        	if(i==typeNames.size()-1){
        		Texture texture = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "64" + File.separator + "ground" + File.separator + typeNames.get(i) + ".png"));
        		typeTextures.add(texture);
        		typeSprites.add(new SpriteTerritory(texture));
        	}
        	// Sinon on charge le fichier correspondant au nom
        	else{
        		Texture texture = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "64" + File.separator + "ground" + File.separator + typeNames.get(i) + "0.png"));
        		typeTextures.add(texture);
        		typeSprites.add(new SpriteTerritory(texture));        	}
        	// Si c'est sur une ligne paire 
    		if(i%2==0){
                typeSprites.get(i).setPosition( lateralBar.getWidth()/2 - typeSprites.get(0).getWidth() - bf.getCapHeight()/2 , Gdx.graphics.getHeight() - typeSprites.get(0).getHeight() - typeSprites.get(0).getHeight()*(i+1) );
        	}
    		// Si c'est sur une ligne impaire
        	else{
                typeSprites.get(i).setPosition( lateralBar.getWidth()/2 + bf.getCapHeight()/2 , Gdx.graphics.getHeight() - typeSprites.get(0).getHeight() - typeSprites.get(0).getHeight()*i );
        	}
    		// Ainsi, on a deux sprites par ligne

        }                
        
		sprites = ggm.getSprites();
		territories = ggm.getTerritoriesByCoordinates();
	
		ribbons = new ArrayList<Texture>();
		for (int i = 0; i < 8; i++) {
			ribbons.add(new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "leather" + File.separator + "P" + (i + 1) + ".png")));
		}

		bTerritory = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "buttons" + File.separator + "bterritory.png"));
		bStock = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "buttons" + File.separator + "bstock.png"));
		bProd = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "buttons" + File.separator + "bprod.png"));

		camera.translate(00, -1000);
		camera.zoom = 3;
		//typeSelectionCamera.zoom = 0;
		buttons();

		Gdx.input.setInputProcessor(stage);

	}	
	
	public void buttons(){
		skin = new Skin(Gdx.files.internal("resources/menu.json"), new TextureAtlas(Gdx.files.internal("resources/images/buttons/menu.pack")));
		uiskin = new Skin(Gdx.files.internal("resources/uiskin.json"));
		stage = new InputGame();
		changeProd = new TextButton(Localisation.getInstance().getMessage("Change prod"), skin);
		changeStock = new TextButton(Localisation.getInstance().getMessage("Change stock"), skin);
		autoProd = new TextButton(Localisation.getInstance().getMessage("Auto"), skin);
		autoProdHelp = new TextButton("?", skin);
		okProd = new TextButton("Ok", uiskin);
		okStock = new TextButton("Ok", uiskin);
		
		changeProdField = new TextField("0", uiskin);
		changeStockField = new TextField("0", uiskin);
		
		
		changeProd.setHeight(40);
		changeStock.setHeight(40);
		autoProd.setHeight(40);
		//autoProdHelp.sizeBy(10,10);
		autoProdHelp.setHeight(40);
		autoProdHelp.setWidth(40);
		
		changeProd.setX(lateralBar.getWidth() + bTerritory.getWidth() + 20);
		changeProd.setY(bottomBar.getHeight() - 100);
		
		changeStock.setX(lateralBar.getWidth() + bTerritory.getWidth() + 20);
		changeStock.setY(bottomBar.getHeight() - 100 - changeProd.getHeight() );
		
		autoProd.setX(lateralBar.getWidth() + bTerritory.getWidth() + 20 + changeStock.getWidth() + 20 );
		autoProd.setY(bottomBar.getHeight() - 100);
		
		autoProdHelp.setX(lateralBar.getWidth() + bTerritory.getWidth() + 20 + changeStock.getWidth() + 20 + autoProd.getWidth());
		autoProdHelp.setY(bottomBar.getHeight() - 100);

		changeProdField.setX(changeStock.getX() );
		changeProdField.setY(changeStock.getY() - changeStock.getHeight());
		changeProdField.setVisible(false);
		
		changeStockField.setX(changeStock.getX() );
		changeStockField.setY(changeStock.getY() - changeStock.getHeight());
		changeStockField.setVisible(false);
		
		okProd.setX(changeStock.getX() + changeProdField.getWidth());
		okProd.setY(changeProdField.getY());
		okProd.setVisible(false);
		
		okStock.setX(changeStock.getX() + changeProdField.getWidth());
		okStock.setY(changeProdField.getY());
		okStock.setVisible(false);
		
		for(int index = 0; index<8; index++){
			CheckBox[] tripleCheck = new CheckBox[3];
			tripleCheck[0] = new CheckBox(Localisation.getInstance().getMessage("Player"), uiskin);
			tripleCheck[0].setVisible(false);
			tripleCheck[0].setX(autoProdHelp.getX()+autoProdHelp.getWidth()+20);
			tripleCheck[0].setY(autoProdHelp.getY());
			tripleCheck[0].setChecked(true);
			tripleCheck[1] = new CheckBox(Localisation.getInstance().getMessage("Easy"), uiskin);
			tripleCheck[1].setVisible(false);
			tripleCheck[1].setX(autoProdHelp.getX()+autoProdHelp.getWidth()+20);
			tripleCheck[1].setY(autoProdHelp.getY()-20);
			tripleCheck[2] = new CheckBox(Localisation.getInstance().getMessage("Normal"), uiskin);
			tripleCheck[2].setVisible(false);
			tripleCheck[2].setX(autoProdHelp.getX()+autoProdHelp.getWidth()+20);
			tripleCheck[2].setY(autoProdHelp.getY()-40);
			for(int checkIndex = 0; checkIndex<3; checkIndex++){
				stage.addActor(tripleCheck[checkIndex]);
			}
			checkBoxes.add(tripleCheck);
		}
		
		changeProd.addListener(new ChangeProdListener());
		changeStock.addListener(new ChangeStockListener());
		autoProd.addListener(new AutoProdListener());
		autoProdHelp.addListener(new AutoProdHelpListener());
		okProd.addListener(new OkProdListener());
		okStock.addListener(new OkStockListener());
		
		stage.addActor(changeProd);
		stage.addActor(changeStock);
		stage.addActor(autoProd);
		stage.addActor(autoProdHelp);
		stage.addActor(changeProdField);
		stage.addActor(changeStockField);
		stage.addActor(okProd);
		stage.addActor(okStock);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		backgroundBatch.begin();
		backgroundBatch.draw(background, 0, 0);
		backgroundBatch.end();

		camera.update();
		backgroundCamera.update();
		menuCamera.update();
		typeSelectionCamera.update();
		
		mapBatch.setProjectionMatrix(camera.combined);
		backgroundBatch.setProjectionMatrix(backgroundCamera.combined);
		menuBatch.setProjectionMatrix(menuCamera.combined);
		typeSelectionBatch.setProjectionMatrix(typeSelectionCamera.combined);

		ggm.generate(selectedTroopNativeTerritory, selectedTroop);

		writeText(menuBatch);
		for(int i = 0; i < typeSprites.size(); i++ ){
			typeSelectionBatch.begin();
			typeSprites.get(i).draw(typeSelectionBatch);
			typeSelectionBatch.end();
		}
		updateMenuSprites();
		actionOnTheMap();
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		stage.getViewport().update((int) w, (int) h);
		stage.act();
		stage.draw();

	}

	private void writeText(SpriteBatch menuBatch) {

		menuBatch.begin();
		menuBatch.draw(lateralBar, 0, 0);
		menuBatch.draw(bottomBar, lateralBar.getWidth(), 0);
		if (selectedTerritoryCoordinates != null) {
			int x = selectedTerritoryCoordinates.getX() / 256;
			int y = selectedTerritoryCoordinates.getY() / 128;
			menuBatch.draw(bTerritory, 260, 100);
			bf.setColor(118, 118, 118, 1);
			bf.draw(menuBatch, Localisation.getInstance().getMessage("Selected Territory") + " : [ " + Integer.toString(x) + "." + Integer.toString(-y) + " ]", lateralBar.getWidth() + 30, bottomBar.getHeight() - 70);
			Territory currentTerritory = territories.get(selectedTerritoryCoordinates);
			int bonusProduction = 0;
			if (currentTerritory.getBuilding() instanceof ResourceIncrease) {
				ResourceIncrease currentBuilding = (ResourceIncrease) currentTerritory.getBuilding();
				for (Integer i : currentTerritory.getResources().keySet()) {
					if (currentTerritory.getOwner() != null) {
						bonusProduction = currentBuilding.getResourceIncrease(i);
					}
				}
			}
			int currentTerritoryProduction = currentTerritory.getResources().get(Resource.WOOD).getProduction().getValue();
			menuBatch.draw(bProd, 260, 52);
			menuBatch.draw(bStock, 420, 52);
			bf.setColor(0, 0, 0, 1);
			bf.draw(menuBatch, Localisation.getInstance().getMessage("Production") + " : " + Integer.toString(currentTerritoryProduction + (currentTerritoryProduction * bonusProduction / 100)) + "     " + currentTerritory.getResources().get(Resource.WOOD).getStock().toString(), lateralBar.getWidth() + 30, bottomBar.getHeight() - 120);
			if (currentTerritory.getOwner() != null) {
				menuBatch.draw(ribbons.get(currentTerritory.getOwner().getNumber()), 220, 148);
				bf.setColor(1, 1, 1, 1);
				bf.draw(menuBatch, currentTerritory.getOwner().getName(), 260, 190);
			}

		} else {

		}
		bf.setColor(255, 255, 255, 1);
		bf.draw(menuBatch, errorMessage, lateralBar.getWidth() + 10, bf.getCapHeight() + 10);
		bf.setColor(255, 255, 255, 1);
		//bf.draw(menuBatch, game.getCurrentPlayer().getName(), 20, Gdx.graphics.getHeight() - 150);
		//bf.draw(menuBatch, Localisation.getInstance().getMessage("Your Resources") + " : ", 20, Gdx.graphics.getHeight() - 190);
		//bf.draw(menuBatch, game.getCurrentPlayer().getStock().get(Resource.WOOD).toString(), 20, Gdx.graphics.getHeight() - 230);
		bf.draw(menuBatch, Localisation.getInstance().getMessage("Editor") , lateralBar.getWidth()/2 - 35, Gdx.graphics.getHeight() - 10);
		
		// On écrit le nom des types de territoire à côté d'eux
		for(int index = 0 ; index < typeSprites.size() ; index++){
			if(index%2 == 0){
				bf.draw(menuBatch, Localisation.getInstance().getMessage(typeNames.get(index)) , typeSprites.get(index).getX() , typeSprites.get(index).getY() + typeSprites.get(0).getHeight() + bf.getCapHeight() +5 );
			}
			else{
				bf.draw(menuBatch, Localisation.getInstance().getMessage(typeNames.get(index)) , typeSprites.get(index).getX() , typeSprites.get(index).getY());
			}

		}
		if(selectedTerritoryCoordinates!=null){
			if(territories.get(selectedTerritoryCoordinates).isCapital()){
				if(ancientIndex!=null){
					for(int index=0; index<3; index ++){
						checkBoxes.get(ancientIndex)[index].setVisible(false);
					}
					ancientIndex=null;
				}
				ancientIndex = checkBoxesC.indexOf(selectedTerritoryCoordinates);
				System.out.println("Capitale num�ro : "+checkBoxesC.indexOf(selectedTerritoryCoordinates));
				for(int index=0; index<3; index ++){
					checkBoxes.get(checkBoxesC.indexOf(selectedTerritoryCoordinates))[index].setVisible(true);
				}
				for(int index=0; index<3; index ++){
					if((checkBoxes).get(checkBoxesC.indexOf(selectedTerritoryCoordinates))[index].isChecked()){
						switch(index){
						case 0:
							(checkBoxes).get(checkBoxesC.indexOf(selectedTerritoryCoordinates))[index+1].setChecked(false);
							(checkBoxes).get(checkBoxesC.indexOf(selectedTerritoryCoordinates))[index+2].setChecked(false);
							break;
						case 1:
							(checkBoxes).get(checkBoxesC.indexOf(selectedTerritoryCoordinates))[index+1].setChecked(false);
							(checkBoxes).get(checkBoxesC.indexOf(selectedTerritoryCoordinates))[index-1].setChecked(false);
							break;
						case 2:
							(checkBoxes).get(checkBoxesC.indexOf(selectedTerritoryCoordinates))[index-1].setChecked(false);
							(checkBoxes).get(checkBoxesC.indexOf(selectedTerritoryCoordinates))[index-2].setChecked(false);
							break;
						default:
							break;
								
						}
					}
				}
			}
			else{
				if(ancientIndex!=null){
					for(int index=0; index<3; index ++){
						checkBoxes.get(ancientIndex)[index].setVisible(false);
					}
					ancientIndex=null;
				}
			}
		}
		else{
			if(ancientIndex!=null){
				for(int index=0; index<3; index ++){				
					checkBoxes.get(ancientIndex)[index].setVisible(false);
				}	
				ancientIndex=null;
			}
		}
		menuBatch.end();
	}

	private void actionOnTheMap() {
		// To move the map
		if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT))
			camera.translate(16, 0); // Modify the first value to
										// reduce/accelerate the speed
		if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT))
			camera.translate(-16, 0);
		if (Gdx.input.isKeyPressed(Keys.DPAD_UP))
			camera.translate(0, -16);
		if (Gdx.input.isKeyPressed(Keys.DPAD_DOWN))
			camera.translate(0, 16);

		if (Gdx.input.isKeyPressed(Input.Keys.A) && camera.zoom < 5) {
			camera.zoom += 0.02;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q) && camera.zoom > 0.8) {
			camera.zoom -= 0.02;
		}
	}

	

	private void quit() {
		AL.destroy();
		Gdx.app.exit();
		System.exit(0);
	}
	
	@Override
	public void setErrorMessage(String message){
		this.errorMessage = message;
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	public class InputGame extends Stage {

		@Override
		public boolean keyUp(int keycode) {
			if (keycode == Input.Keys.ESCAPE) {
				quit();
			} else if (keycode == Input.Keys.SPACE) {
			} else if (keycode == Input.Keys.C) {
			} else if ((keycode >= Input.Keys.NUM_0) && keycode <= (Input.Keys.NUM_9)) {
				if (selectedTerritoryCoordinates != null) {
					selectedTroopNativeTerritory = territories.get(selectedTerritoryCoordinates);
					selectedTroop = keycode - Input.Keys.NUM_0;
				}

			} else if ((keycode >= Input.Keys.NUMPAD_0) && keycode <= (Input.Keys.NUMPAD_9)) {
				if (selectedTerritoryCoordinates != null) {
					selectedTroopNativeTerritory = territories.get(selectedTerritoryCoordinates);
					selectedTroop = keycode - Input.Keys.NUMPAD_0;
				}
			}
			else if (keycode == Input.Keys.S) {
				int indexC = 0;
				for(Coordinates c : checkBoxesC){
					System.err.println("Capitale actuelle : "+(checkBoxesC).indexOf(c));
					EntityEngine player;
					for(int index = 0; index<3;index++){
						if((checkBoxes).get(checkBoxesC.indexOf(c))[index].isChecked()){
							switch(index){
							case 0:
								System.err.println("La capitale "+(checkBoxesC).indexOf(c) +" va être le joueur " +((checkBoxesC).indexOf(c)+ 1));
								Player actualPlayer = new Player("Joueur " + ((checkBoxesC).indexOf(c)+ 1), (checkBoxesC).indexOf(c));
								player = new PlayerEngine(actualPlayer);
								territories.get(c).setOwner(actualPlayer);
								players.add(player);
								break;
							case 1:
								System.err.println("La capitale "+(checkBoxesC).indexOf(c) +" va être une easy");
								try {
									Player actualPlayer0 = new Player("IA " + (((checkBoxesC).indexOf(c) ) + 1 ), (checkBoxesC).indexOf(c));
									player = new IAEngine(actualPlayer0, "Easy", map, players);
									territories.get(c).setOwner(actualPlayer0);
									players.add(player);
								} catch (Exception e) {
									System.err.println("IA not created");
									player = null;
								}

								break;
							case 2:
								System.err.println("La capitale "+(checkBoxesC).indexOf(c) +" va être une normale");
								try {
									Player actualPlayer0 = new Player("IA " + (((checkBoxesC).indexOf(c) ) + 1 ), (checkBoxesC).indexOf(c));
									player = new IAEngine(actualPlayer0, "Normal", map, players);
									territories.get(c).setOwner(actualPlayer0);
									players.add(player);
								} catch (Exception e) {
									System.err.println("IA not created");
									player = null;
								}

								break;
							}
							
						}
						
					}				
					indexC ++;
					
				}
				if(players.size()<capitalCount){
					System.out.println(Localisation.getInstance().getMessage("Define players"));
				}
				else{
				save();
				}
			}
			return true;
		}

		@Override
		public boolean scrolled(int amount) {
			if (amount > 0 && camera.zoom < 5) {
				camera.zoom += 0.20;
			} else if (amount < 0 && camera.zoom > 0.8) {
				camera.zoom -= 0.20;
			}
			return true;
		}

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			super.touchDown(screenX, screenY, pointer, button);
			// Traduction des coordonnées de la souris en coordonnées de la
			// camera
			Vector3 vector = new Vector3(screenX, screenY, 0);
			Vector3 vectorMenu = new Vector3(screenX, screenY, 0);
			backgroundCamera.unproject(vectorMenu);
			// Si le curseur est partout sauf dans le menu
			if ((!lateralBar.getBoundingRectangle().contains(vectorMenu.x, vectorMenu.y)) && (!bottomBar.getBoundingRectangle().contains(vectorMenu.x, vectorMenu.y))) {
				camera.unproject(vector);
				// Parcour de tous les sprites
				for (int index = 0; index < sprites.size(); index++) {
					// Si le click se trouve dans le rectangle correspondant
					// à un territoire
					if (sprites.get(index).getBoundingRectangle().contains(vector.x, vector.y)) {
						sprites.get(index).getTexture().getTextureData().prepare();
						if (sprites.get(index).getTexture().getTextureData().consumePixmap().getPixel((int) (vector.x - sprites.get(index).getX()), (int) (vector.y - sprites.get(index).getY())) != -256) {
							if(button == Input.Buttons.LEFT){
								SpriteTerritory currentSprite = ((SpriteTerritory) (sprites.get(index)));
								if(!currentSprite.isSelected()){
									currentSprite.changeSelectionState();
									if(ancientSpriteSelectedPos == index){
										ancientSpriteSelectedPos = -1;
									}
									selection(index);
								}
								else{
									updateSelection(index);
								}
								int y=index/map.getNbColumn();
								int x=0;
								if(y%2 == 0){
									x=index%map.getNbColumn();
								}
								else{
									x = (map.getNbColumn()-1)-(index%map.getNbColumn());
								}
								if(selectedTypeCoordinates != null){
									if(selectedTerritoryCoordinates!=null){
										
										//Clic sur un territoire diff�rent du type s�lectionn�
										if(!(territories.get(selectedTerritoryCoordinates).getClass().getSimpleName().equals(selectedType))){
											
											// Type s�lectionn� = pas capitale
											if(!(selectedType.equals("Capital"))){
			
												//click sur une capitale
												if(territories.get(selectedTerritoryCoordinates).isCapital()==true){
													checkBoxesC.remove(selectedTerritoryCoordinates);
													capitalCount--;
													System.err.println(capitalCount);
												}
												Territory newTerritory = map.setType(x,y, selectedType);
												newTerritory.setOwner(null);
												newTerritory.undefineCapital();
												ggm.actualize(map, vector.x, vector.y, selectedType);
												territories.put(selectedTerritoryCoordinates, newTerritory);
												System.err.println(newTerritory.getClass());
												selectedTerritoryCoordinates = null;
											}
											// Type s�lectionn� = capitale 
											else if(selectedType.equals("Capital")){
													// Click sur une pas capitale
												if(!(territories.get(selectedTerritoryCoordinates).isCapital())){
													if(capitalCount<8){
														capitalCount++;
														System.err.println("Capital count: " +capitalCount);
														checkBoxesC.add(selectedTerritoryCoordinates);
														Territory newTerritory = map.setType(x,y, selectedType);
														newTerritory.defineCapital();
														//newTerritory.setOwner(new Player("Player"+(capitalCount-1),capitalCount-1));
														ggm.actualize(map, vector.x, vector.y, selectedType);
														territories.put(selectedTerritoryCoordinates, newTerritory);
														System.err.println(newTerritory.getClass());
														selectedTerritoryCoordinates = null;
													}
													else{
														System.out.println("Nombre de joueurs maximum atteint");
													}
												}
											}
											
							
										}
									}
										
								}
									
									
							}
							}
							return true;
						}
					}
				}
			// Si le curseur est dans le menu de gauche
			else if (lateralBar.getBoundingRectangle().contains(vectorMenu.x, vectorMenu.y)) {
				typeSelectionCamera.unproject(vector);
				// Parcours de tous les sprites
				for (int index = 0; index < typeSprites.size(); index++) {
					// Si le click se trouve dans le rectangle correspondant
					// à un territoire
					if (typeSprites.get(index).getBoundingRectangle().contains(vector.x, vector.y)) {
						typeSprites.get(index).getTexture().getTextureData().prepare();
						if (typeSprites.get(index).getTexture().getTextureData().consumePixmap().getPixel((int) (vector.x - sprites.get(index).getX()), (int) (vector.y - sprites.get(index).getY())) != -256) {
							if(button == Input.Buttons.LEFT){
								selectedType = typeNames.get(index);
								SpriteTerritory currentSprite = ((SpriteTerritory) (typeSprites.get(index)));
								if(!currentSprite.isSelected()){
									currentSprite.changeSelectionState();
									if(ancientTypeSelectedPos == index){
										ancientTypeSelectedPos = -1;
									}
									typeSelection(index);
								}
								else{
									updateTypeSelection(index);					
								}
							}
							return true;
						}
					}
				}
			}
			return false;
		}
		
		
		public void updateSelection(int index){
			// Si ce territoire n'est pas déjà séléctionné
			SpriteTerritory currentSprite = (SpriteTerritory) (sprites.get(index));
			currentSprite.changeSelectionState();
			if (currentSprite.isSelected()) {
				selection(index);
			} else {
				ancientSpriteSelectedPos = -1;
				selectedTerritoryCoordinates = null;

			}
		}
		
		public void selection(int index){
			int x = (int) sprites.get(index).getX();
			int y = (int) sprites.get(index).getY();
			if (ancientSpriteSelectedPos != -1) {
				SpriteTerritory ancientSpriteSelected = (SpriteTerritory) sprites.get(ancientSpriteSelectedPos);
				ancientSpriteSelected.changeSelectionState();
			}
			selectedTerritoryCoordinates = new Coordinates(x, y);
			ancientSpriteSelectedPos = index;
		}
		
		public void updateTypeSelection(int index){
			// Si ce territoire n'est pas déjà séléctionné
			SpriteTerritory currentSprite = (SpriteTerritory) (typeSprites.get(index));
			currentSprite.changeSelectionState();
			if (currentSprite.isSelected()) {
				selection(index);
			} else {
				ancientTypeSelectedPos = -1;
				selectedTypeCoordinates = null;

			}
		}
		public void typeSelection(int index){
			int x = (int) typeSprites.get(index).getX();
			int y = (int) typeSprites.get(index).getY();
			if (ancientTypeSelectedPos != -1) {
				SpriteTerritory ancientSpriteSelected = (SpriteTerritory) typeSprites.get(ancientTypeSelectedPos);
				ancientSpriteSelected.changeSelectionState();
			}
			selectedTypeCoordinates = new Coordinates(x, y);
			ancientTypeSelectedPos = index;
		}

	}
	
	public void save(){
		try {
			GameStockage.getInstance().save(map, players);
		} catch (GameNotSaveException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void updateMenuSprites(){
		menuBatch.begin();
		Texture selectedTexture= new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "64" + File.separator + "ground" + File.separator + "selected.png"));

		for(Sprite s : typeSprites){
			SpriteTerritory st = (SpriteTerritory) s ;
			if (st.isSelected()) {
				menuBatch.draw(selectedTexture, s.getX(), s.getY());
			}
		}
		menuBatch.end();
	}
	
	public class ChangeProdListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y){
			if(changeStockField.isVisible()==true){
				changeStockField.setVisible(false);
				okStock.setVisible(false);
			}
			if(changeProdField.isVisible()==false){
				changeProdField.setVisible(true);
				okProd.setVisible(true);
			}
			else{
				changeProdField.setVisible(false);
				okProd.setVisible(false);
			}
		}
	}
	public class ChangeStockListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y){
			if(changeProdField.isVisible()==true){
				changeProdField.setVisible(false);
				okProd.setVisible(false);
			}
			if(changeStockField.isVisible()==false){
				changeStockField.setVisible(true);
				okStock.setVisible(true);
			}
			else{
				changeStockField.setVisible(false);
				okStock.setVisible(false);
			}
		}
	}
	public class AutoProdListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y){
			for(Coordinates c : territories.keySet()){
				for(Integer resource : territories.get(c).getResources().keySet()){
					if((territories.get(c).isCrossable())){
						int prod = (int) (Math.random() * 35 + 25);
						int stock = (int) (Math.random() * 21 + 80);
						territories.get(c).getResources().get(resource).getProduction().setValue(prod);
						if(!(territories.get(c).isCapital())){
							territories.get(c).getResources().get(resource).getStock().setValue(stock);
						}
					}
				}
			}
		}
	}
	public class AutoProdHelpListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y){
			System.out.println(autoProdHelpText);
		}
	}
	
	public class OkStockListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y){
			changeStockValue = changeStockField.getText();
			if(selectedTerritoryCoordinates!=null){
				for(Integer resource : territories.get(selectedTerritoryCoordinates).getResources().keySet()){
					territories.get(selectedTerritoryCoordinates).getResources().get(resource).getStock().setValue(Integer.parseInt(changeStockValue));
				}
			}
			else{
				System.out.println(Localisation.getInstance().getMessage("Not Territory Selected"));
			}
		}
	}
	
	public class OkProdListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y){
			changeProdValue = changeProdField.getText();
			if(selectedTerritoryCoordinates!=null){
				for(Integer resource : territories.get(selectedTerritoryCoordinates).getResources().keySet()){
					territories.get(selectedTerritoryCoordinates).getResources().get(resource).getProduction().setValue(Integer.parseInt(changeProdValue));
				}
			}
			else{
				System.out.println(Localisation.getInstance().getMessage("Not Territory Selected"));
			}
		}
	}
}
