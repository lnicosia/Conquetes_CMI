package graphic.screen;

import graphic.map.ActualiseMap;
import graphic.map.Coordinates;
import graphic.map.GenerateGraphicMap;
import graphic.map.SpriteTerritory;
import graphic.screen.listener.SettingsListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import network.Client;

import org.lwjgl.openal.AL;
import org.reflections.Reflections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import data.localisation.Localisation;
import data.player.Player;
import data.territory.Territory;
import data.territory.building.ResourceIncrease;
import data.territory.resource.Resource;
import data.troop.Troop;
import data.troop.soldier.Soldier;
import engine.game.action.AcceptAllianceAction;
import engine.game.action.AskAllianceAction;
import engine.game.action.CreateBuildingAction;
import engine.game.action.CreateTroopAction;
import engine.game.action.EndOfTurnAction;
import engine.game.action.MoveTroopAction;
import engine.game.action.RejectAllianceAction;
import engine.game.player.EntityEngine;
import engine.game.player.PlayerEngine;
import data.territory.building.Building;

/**
 * Main graphic class. Control display and evenements in a game
 * 
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */

public class GuiGame implements Screen, GuiStream {

	private InputGame stage;
	private Skin skinDialog;
	private OrthographicCamera camera, cameraMenu;
	private Texture font;
	private Sprite lateralBar, bottomBar;
	private ArrayList<Texture> ribbons;
	private Texture bTerritory, bProd, bStock;
	private TextButton buttonPass, settings, alliance, help, barracks, factory,
			ramparts;
	private ArrayList<Button> troopButtons;
	private BitmapFont buttons;
	private Texture soldierTexture;

	private SpriteBatch batchMap, batchFont, batchText, batchTextNumber;
	private Client client;
	private GenerateGraphicMap ggm;
	private BitmapFont territoryStats, numberOfTroops, nameBuilding;
	private ArrayList<Sprite> sprites;
	private int ancientSpriteSelectedPos;
	private HashMap<Coordinates, Territory> territories;
	private Coordinates selectedTerritoryCoordinates;

	private Integer selectedTroop;
	private Territory selectedTroopNativeTerritory;
	private String errorMessage;
	private boolean openDialog;

	private Dialog allianceDialog, askAllianceDialog;

	/**
	 * 
	 * @param client
	 *            Client which have communication with the game server
	 */
	public GuiGame(Client client) {
		super();
		this.client = client;

		// Redirection des messages d'erreur
		try {
			System.setOut(new RedirectOutputStream(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Gdx.graphics.setContinuousRendering(false);
	}

	/**
	 * Initialize the screen information
	 * 
	 */
	public void show() {

		skinDialog = new Skin(Gdx.files.internal("resources/uiskin.json"));

		allianceDialog = new Dialog("Alliance", skinDialog, "dialog") {
			protected void result(Object object) {
				System.out.println("Chosen: " + object);
			}
		};

		Dialog dialog = new Dialog("lollll", skinDialog, "dialog");
		dialog.setSize(400, 500);
		dialog.setPosition(Gdx.graphics.getWidth() / 2 - 200, Gdx.graphics.getHeight() / 2 - 300);
		dialog.setPosition(20, 20);

		allianceDialog.hide();

		askAllianceDialog = new Dialog(Localisation.getInstance().getMessage("Asked Alliance"), skinDialog, "dialog") {
			protected void result(Object object) {
				System.out.println("Chosen: " + object);
			}
		};

		askAllianceDialog.hide();

		while (client.getMap() == null) {

		}

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		errorMessage = "";

		selectedTerritoryCoordinates = null;
		ancientSpriteSelectedPos = -1;

		selectedTroop = null;
		selectedTroopNativeTerritory = null;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.update();

		cameraMenu = new OrthographicCamera();
		cameraMenu.setToOrtho(false, w, h);
		cameraMenu.update();

		territoryStats = new BitmapFont(Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.fnt"), Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.png"), false);
		nameBuilding = new BitmapFont(Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.fnt"), Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.png"), false);

		soldierTexture = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "troops" + File.separator + "Barbarian" + ".png"));

		numberOfTroops = new BitmapFont(Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.fnt"), Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.png"), false);

		batchMap = new SpriteBatch();
		batchFont = new SpriteBatch();
		batchText = new SpriteBatch();
		batchTextNumber = new SpriteBatch();

		ggm = new GenerateGraphicMap(client.getMap(), batchMap);

		sprites = ggm.getSprites();
		territories = ggm.getTerritoriesByCoordinates();
		font = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "font.jpg"));
		lateralBar = new Sprite(new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "lateralbar.png")));
		bottomBar = new Sprite(new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "bottombar.png")));

		ribbons = new ArrayList<Texture>();
		for (int i = 0; i < 8; i++) {
			ribbons.add(new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "leather" + File.separator + "P" + (i + 1) + ".png")));
		}

		bTerritory = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "buttons" + File.separator + "bterritory.png"));
		bStock = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "buttons" + File.separator + "bstock.png"));
		bProd = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "buttons" + File.separator + "bprod.png"));

		camera.translate(00, -1000);
		camera.zoom = 3;
		buttons();
		Gdx.input.setInputProcessor(stage);

		Pixmap pm = new Pixmap(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "cursor.png"));
		Gdx.input.setCursorImage(pm, 0, 0);
		pm.dispose();
	}

	/**
	 * Render all buttons of the graphical interface
	 */

	private void buttons() {

		TextButtonStyle buttonStyleTour, buttonsSettingsStyle,
				buttonAllianceStyle, buttonHelpStyle, buttonBarrackStyle,
				buttonFactoryStyle, buttonRampartsStyle;
		stage = new InputGame();

		Skin skin = new Skin(Gdx.files.internal("resources/menu.json"), new TextureAtlas(Gdx.files.internal("resources/images/buttons/menu.pack")));
		buttons = new BitmapFont(Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.fnt"), Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.png"), false);

		buttonStyleTour = new TextButtonStyle();
		buttonStyleTour.font = buttons;
		buttonStyleTour.up = skin.getDrawable("red0");
		buttonStyleTour.down = skin.getDrawable("red1");
		buttonStyleTour.pressedOffsetY = -4;

		buttonHelpStyle = new TextButtonStyle();
		buttonHelpStyle.font = buttons;
		buttonHelpStyle.up = skin.getDrawable("green2");
		buttonHelpStyle.down = skin.getDrawable("green1");

		buttonsSettingsStyle = new TextButtonStyle();
		buttonsSettingsStyle.font = buttons;
		buttonsSettingsStyle.up = skin.getDrawable("settings");

		buttonAllianceStyle = new TextButtonStyle();
		buttonAllianceStyle.font = buttons;
		buttonAllianceStyle.up = skin.getDrawable("blue4");
		buttonAllianceStyle.down = skin.getDrawable("blue3");
		buttonAllianceStyle.pressedOffsetY = -4;

		buttonBarrackStyle = new TextButtonStyle();
		buttonBarrackStyle.font = buttons;
		buttonBarrackStyle.up = skin.getDrawable("barracks");
		buttonBarrackStyle.down = skin.getDrawable("barracks1");
		buttonBarrackStyle.pressedOffsetY = -20;

		buttonFactoryStyle = new TextButtonStyle();
		buttonFactoryStyle.font = buttons;
		buttonFactoryStyle.up = skin.getDrawable("mine");
		buttonFactoryStyle.down = skin.getDrawable("mine1");
		buttonFactoryStyle.pressedOffsetY = -20;

		buttonRampartsStyle = new TextButtonStyle();
		buttonRampartsStyle.font = buttons;
		buttonRampartsStyle.up = skin.getDrawable("defense");
		buttonRampartsStyle.down = skin.getDrawable("defense1");
		buttonRampartsStyle.pressedOffsetY = -20;

		settings = new TextButton(Localisation.getInstance().getMessage("Settings"), buttonsSettingsStyle);
		buttonPass = new TextButton(Localisation.getInstance().getMessage("Next Turn"), buttonStyleTour);
		alliance = new TextButton(Localisation.getInstance().getMessage("Alliance"), buttonAllianceStyle);
		help = new TextButton(Localisation.getInstance().getMessage("Help"), buttonHelpStyle);
		barracks = new TextButton("", buttonBarrackStyle);
		factory = new TextButton("", buttonFactoryStyle);
		ramparts = new TextButton("", buttonRampartsStyle);

		buttonPass.addListener(new PassListener());
		settings.addListener(new SettingsListener(this));
		alliance.addListener(new AllianceListener());
		help.addListener(new HelpListener(this));

		factory.addListener(new CreateBuildingListener("Factory"));
		barracks.addListener(new CreateBuildingListener("Barracks"));
		ramparts.addListener(new CreateBuildingListener("Rampart"));

		// buttonPass.setFillParent(true);

		Reflections soldiersReflexion = new Reflections("data.troop.soldier");
		Set<Class<? extends Soldier>> soldiersClasses = soldiersReflexion.getSubTypesOf(Soldier.class);

		troopButtons = new ArrayList<Button>();
		ArrayList<String> troopsName = new ArrayList<String>();
		troopsName.add("Barbarian");
		troopsName.add("Bowman");
		troopsName.add("Spearman");
		troopsName.add("Swordman");
		troopsName.add("Wizard");

		Iterator<String> it = troopsName.iterator();

		for (String s : troopsName) {
			TextButtonStyle tbs = new TextButtonStyle();
			Button tb;
			tbs.font = buttons;
			tbs.up = skin.getDrawable(s + "Button");
			tbs.down = skin.getDrawable(s + "Button");
			tbs.pressedOffsetY = -4;
			tb = new Button(null, tbs);
			tb.addListener(new CreateTroopListener(s));
			tb.setVisible(false);
			if (troopButtons.isEmpty()) {
				tb.setX(lateralBar.getWidth() + 320 + 60);
				tb.setY(bottomBar.getHeight() - 100);
			} else {
				tb.setX(troopButtons.get(troopButtons.size() - 1).getX() + troopButtons.get(troopButtons.size() - 1).getWidth() + 60);
				tb.setY(troopButtons.get(troopButtons.size() - 1).getY());
			}
			troopButtons.add(tb);
			stage.addActor(tb);
		}

		settings.setX(10);
		settings.setY(10);
		help.setX(10);
		help.setY(buttonPass.getY() + buttonPass.getHeight() + 10);
		alliance.setX(10);
		alliance.setY(help.getY() + help.getHeight() + 50);
		buttonPass.setX(10);
		buttonPass.setY(alliance.getY() + alliance.getHeight() + 10);

		factory.setX(1475);
		factory.setY(50);
		barracks.setX(1345);
		barracks.setY(50);
		ramparts.setX(1215);
		ramparts.setY(50);

		stage.addActor(factory);
		stage.addActor(barracks);
		stage.addActor(ramparts);
		stage.addActor(help);
		stage.addActor(buttonPass);
		stage.addActor(settings);
		stage.addActor(alliance);
	}

	/**
	 * Called when the screen should render itself.
	 */

	@Override
	public void render(float delta) {
		if (!openDialog) {
			actionOnTheMap();
		}
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batchFont.begin();
		batchFont.draw(font, 0, 0);
		batchFont.end();

		camera.update();
		cameraMenu.update();
		batchMap.setProjectionMatrix(camera.combined);
		batchFont.setProjectionMatrix(cameraMenu.combined);

		stage.getViewport().update((int) Gdx.graphics.getWidth(), (int) Gdx.graphics.getHeight());

		ggm.generate(selectedTroopNativeTerritory, selectedTroop);

		writeText(batchText);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		stage.getViewport().update((int) w, (int) h);
		stage.act();
		stage.draw();
		checkEndOfGame();
		if (selectedTerritoryCoordinates != null) {
			batchTextNumber.begin();
			numberOfTroops.draw(batchTextNumber, "10", 590, 133);
			numberOfTroops.draw(batchTextNumber, "25", 695, 133);
			numberOfTroops.draw(batchTextNumber, "25", 800, 133);
			numberOfTroops.draw(batchTextNumber, "25", 905, 133);
			numberOfTroops.draw(batchTextNumber, "30", 1015, 133);
			batchTextNumber.end();
		}
	}

	/**
	 * Write informations of players, territory, production
	 */

	private void writeText(SpriteBatch batchText) {

		batchText.begin();
		batchText.draw(lateralBar, 0, 0);
		batchText.draw(bottomBar, lateralBar.getWidth(), 0);
		if (selectedTerritoryCoordinates != null) {
			int x = selectedTerritoryCoordinates.getX() / 256;
			int y = selectedTerritoryCoordinates.getY() / 192;
			batchText.draw(bTerritory, 260, 100);
			territoryStats.setColor(118, 118, 118, 1);
			territoryStats.draw(batchText, Localisation.getInstance().getMessage("Selected Territory") + " : [ " + Integer.toString(x) + "." + Integer.toString(-y) + " ]  " + territories.get(selectedTerritoryCoordinates).getId(), lateralBar.getWidth() + 30, bottomBar.getHeight() - 70);
			Territory currentTerritory = territories.get(selectedTerritoryCoordinates);
			int bonusProduction = 0;

			for (Troop i : territories.get(selectedTerritoryCoordinates).getTroops()) {
				// System.out.println("lol");
				System.out.println(i.toString());
			}

			if (currentTerritory.getBuilding() instanceof ResourceIncrease) {
				ResourceIncrease currentBuilding = (ResourceIncrease) currentTerritory.getBuilding();
				for (Integer i : currentTerritory.getResources().keySet()) {
					if (currentTerritory.getOwner() != null) {
						bonusProduction = currentBuilding.getResourceIncrease(i);
					}
				}
			}
			int currentTerritoryProduction = currentTerritory.getResources().get(Resource.WOOD).getProduction().getValue();
			batchText.draw(bProd, 260, 52);
			batchText.draw(bStock, 420, 52);
			territoryStats.setColor(0, 0, 0, 1);
			territoryStats.draw(batchText, Localisation.getInstance().getMessage("Production") + " : " + Integer.toString(currentTerritoryProduction + (currentTerritoryProduction * bonusProduction / 100)) + "     " + currentTerritory.getResources().get(Resource.WOOD).getStock().toString(), lateralBar.getWidth() + 30, bottomBar.getHeight() - 120);
			if (currentTerritory.getOwner() != null) {
				batchText.draw(ribbons.get(currentTerritory.getOwner().getNumber()), 220, 148);
				territoryStats.setColor(1, 1, 1, 1);
				territoryStats.draw(batchText, currentTerritory.getOwner().getName(), 260, 190);
			}

		} else {

		}
		territoryStats.setColor(Color.CYAN);
		territoryStats.draw(batchText, errorMessage, lateralBar.getWidth() + 10, territoryStats.getCapHeight() + 10);
		territoryStats.draw(batchText, Localisation.getInstance().getMessage("Your Resources") + " : ", 20, Gdx.graphics.getHeight() - 150);
		territoryStats.draw(batchText, client.getCurrentPlayer().getDataEntity().getStock().get(Resource.WOOD).toString(), 20, Gdx.graphics.getHeight() - 190);
		territoryStats.draw(batchText, "Tour " + client.getTurn(), 20, Gdx.graphics.getHeight() - 50);

		territoryStats.setColor(Color.WHITE);
		territoryStats.draw(batchText, "Players :", 20, Gdx.graphics.getHeight() - 250);

		for (EntityEngine playerName : client.getPlayers()) {
			if (playerName.equals(client.getCurrentPlayer())) {
				territoryStats.setColor(Color.YELLOW);
				territoryStats.draw(batchText, playerName.getDataEntity().getName(), 20, Gdx.graphics.getHeight() - 290 - (client.getPlayers().indexOf(playerName) * 40));
			} else {
				territoryStats.setColor(Color.DARK_GRAY);
				territoryStats.draw(batchText, playerName.getDataEntity().getName(), 20, Gdx.graphics.getHeight() - 290 - (client.getPlayers().indexOf(playerName) * 40));
			}
		}

		nameBuilding.draw(batchText, Localisation.getInstance().getMessage("Barracks"), 1355, 40);
		nameBuilding.draw(batchText, Localisation.getInstance().getMessage("Factory"), 1490, 40);
		nameBuilding.draw(batchText, Localisation.getInstance().getMessage("Ramparts"), 1218, 40);

		batchText.end();
	}

	/**
	 * Check if there is any key pressed and the position of cursor.
	 */

	private void actionOnTheMap() {

		boolean change = false;
		// To move the map
		if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) {
			camera.translate(16, 0); // Modify the first value to
										// reduce/accelerate the speed
			change = true;
		}
		if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) {
			camera.translate(-16, 0);
			change = true;
		}
		if (Gdx.input.isKeyPressed(Keys.DPAD_DOWN)) {
			camera.translate(0, -16);
			change = true;
		}
		if (Gdx.input.isKeyPressed(Keys.DPAD_UP)) {
			camera.translate(0, 16);
			change = true;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A) && camera.zoom < 5) {
			camera.zoom += 0.02;
			change = true;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.Q) && camera.zoom > 0.8) {
			camera.zoom -= 0.02;
			change = true;
		}

		if (Gdx.input.getY() > (Gdx.graphics.getHeight() - 40) && Gdx.input.getY() < (Gdx.graphics.getHeight() - 10)) {
			camera.translate(0, -16);
			change = true;
		}

		if (Gdx.input.getY() < 40 && Gdx.input.getY() > 10) {
			camera.translate(0, 16);
			change = true;
		}

		if (Gdx.input.getX() < 40 && Gdx.input.getX() > 10) {
			camera.translate(-16, 0);
			change = true;
		}
		if (Gdx.input.getX() > (Gdx.graphics.getWidth() - 40) && Gdx.input.getX() < (Gdx.graphics.getWidth() - 10)) {
			camera.translate(16, 0);
			change = true;
		}

		// System.err.println(client.getCurrentPlayer().getDataEntity().getAskAlliance());

		if (change) {
			Gdx.graphics.requestRendering();
			change = false;
		}

		if (!client.getCurrentPlayer().getDataEntity().getAskAlliance().isEmpty()) {
			for (Player target : client.getCurrentPlayer().getDataEntity().getAskAlliance()) {
				// System.err.println(target);

				openDialog = true;
				Label label = new Label(Localisation.getInstance().getMessage("Ask Alliance"), skinDialog);
				label.setWrap(true);
				label.setFontScale(.8f);
				label.setAlignment(Align.center);

				askAllianceDialog.getContentTable().add(label).width(300).row();
				askAllianceDialog.getButtonTable().padTop(50);

				TextButton yesButton = new TextButton(Localisation.getInstance().getMessage("Yes"), skinDialog);
				yesButton.addListener(new AcceptAllianceListener(target));
				askAllianceDialog.getContentTable().add(yesButton).padTop(50).row();

				TextButton noButton = new TextButton(Localisation.getInstance().getMessage("No"), skinDialog);
				noButton.addListener(new RejectAllianceListener(target));
				askAllianceDialog.getContentTable().add(noButton).padTop(50).row();

				askAllianceDialog.key(Keys.ESCAPE, false);
				askAllianceDialog.invalidateHierarchy();
				askAllianceDialog.invalidate();
				askAllianceDialog.layout();
				askAllianceDialog.show(stage);
			}
		}
	}

	/**
	 * Check if the game it's over, and call a result screen
	 */
	public void checkEndOfGame() {
		Screen endScreen = null;

		if (client.getEndOfGame()) {
			GuiScreen.getInstance().setScreen(new GuiResult(endScreen));
			Gdx.graphics.requestRendering();
		}
	}

	/**
	 * Allow the user to create a troop
	 * 
	 * @param troop
	 *            the name of the troops
	 */

	private void createTroop(String troop) {
		if (selectedTerritoryCoordinates != null) {
			if (client.getCurrentPlayer().getClass().equals(PlayerEngine.class)) {
				client.makeAction(new CreateTroopAction(territories.get(selectedTerritoryCoordinates).getId(), troop));
			} else {
				System.out.println(Localisation.getInstance().getMessage("Not Territory Selected"));
			}
		} else {
			System.out.println(Localisation.getInstance().getMessage("Not Your Turn"));
		}
	}

	/**
	 * Allow the user to create a building
	 * 
	 * @param building
	 *            the name of the building
	 */

	private void createBuilding(String building) {
		if (selectedTerritoryCoordinates != null) {
			if (client.getCurrentPlayer().getClass().equals(PlayerEngine.class)) {
				client.makeAction(new CreateBuildingAction(territories.get(selectedTerritoryCoordinates).getId(), building));
			} else {
				System.out.println(Localisation.getInstance().getMessage("Not Territory Selected"));
			}
		} else {
			System.out.println(Localisation.getInstance().getMessage("Not Your Turn"));
		}
	}

	/**
	 * To pass the turn
	 */

	private void pass() {
		client.makeAction(new EndOfTurnAction());
		if (client.isLastPlayerOnClient()) {
			ActualiseMap am = new ActualiseMap(ggm, client);
			am.start();
		}
		if (ancientSpriteSelectedPos != -1) {
			((SpriteTerritory) (sprites.get(ancientSpriteSelectedPos))).changeSelectionState();
			ancientSpriteSelectedPos = -1;
		}
		selectedTerritoryCoordinates = null;
		for (Button tb : troopButtons) {
			tb.setVisible(false);
		}
		selectedTroopNativeTerritory = null;
		errorMessage = "";
	}

	private void save() {
		try {
			client.save();
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Quit the game
	 */
	private void quit() {
		AL.destroy();
		Gdx.app.exit();
		System.exit(0);
	}

	@Override
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
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

	/**
	 * Check if there is any key pressed and the position of cursor.
	 */

	public class InputGame extends Stage {

		@Override
		public boolean keyUp(int keycode) {
			if (!openDialog) {
				if (keycode == Input.Keys.ESCAPE) {
					quit();
					return true;
				}
			}
			if (client.getCurrentPlayer().getClass().equals(PlayerEngine.class)) {
				if (keycode == Input.Keys.SPACE) {
					pass();
				} else if (keycode == Input.Keys.C) {
					createTroop("Barbarian");
				} else if (keycode == Input.Keys.M) {
					if ((selectedTerritoryCoordinates != null) && (selectedTroop != null)) {
						client.makeAction(new MoveTroopAction(selectedTroopNativeTerritory.getId(), selectedTroop, territories.get(selectedTerritoryCoordinates).getId()));
						selectedTroopNativeTerritory = null;
						selectedTroop = null;
					}
				} else if (keycode == Input.Keys.S) {
					save();
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
			} else {
				System.out.println(Localisation.getInstance().getMessage("Not Your Turn"));
			}
			return true;
		}

		/**
		 * Zoom/Dezoom the map.
		 */

		@Override
		public boolean scrolled(int amount) {
			if (amount > 0 && camera.zoom < 5) {
				camera.zoom += 0.20;
			} else if (amount < 0 && camera.zoom > 0.8) {
				camera.zoom -= 0.20;
			}
			return true;
		}

		/**
		 * Detect if a player clicked on a territory
		 */

		@Override
		public boolean touchDown(int screenX, int screenY, int pointer, int button) {
			super.touchDown(screenX, screenY, pointer, button);
			// Traduction des coordonnées de la souris en coordonnées de la
			// camera
			Vector3 vector = new Vector3(screenX, screenY, 0);
			Vector3 vectorMenu = new Vector3(screenX, screenY, 0);
			cameraMenu.unproject(vectorMenu);
			if ((!lateralBar.getBoundingRectangle().contains(vectorMenu.x, vectorMenu.y)) && (!bottomBar.getBoundingRectangle().contains(vectorMenu.x, vectorMenu.y))) {
				camera.unproject(vector);
				// Parcour de tous les sprites
				if (!openDialog)
					for (int index = 0; index < sprites.size(); index++) {
						// Si le click se trouve dans le rectangle correspondant
						// à un territoire
						if (sprites.get(index).getBoundingRectangle().contains(vector.x, vector.y)) {
							sprites.get(index).getTexture().getTextureData().prepare();
							if (sprites.get(index).getTexture().getTextureData().consumePixmap().getPixel((int) (vector.x - sprites.get(index).getX()), (int) (vector.y - sprites.get(index).getY())) != -256) {
								if (button == Input.Buttons.LEFT) {
									ArrayList<Troop> currentTroops = territories.get(new Coordinates((int) (sprites.get(index).getX()), (int) (sprites.get(index).getY()))).getTroops();
									if (!currentTroops.isEmpty()) {
										boolean clickOnSoldier = false;

										// On regarde si on a clické sur un
										// soldat
										for (int i = 0; i < currentTroops.size(); i++) {
											float posX = (float) (sprites.get(index).getX() + sprites.get(index).getWidth() / 2 + (Math.cos((i * 2 * Math.PI) / currentTroops.size()) * (sprites.get(index).getWidth() / 3)) - (soldierTexture.getWidth() / 2));
											float posY = (float) (sprites.get(index).getY() + sprites.get(index).getHeight() / 2 + (Math.sin((i * 2 * Math.PI) / currentTroops.size()) * (sprites.get(index).getWidth() / 3)) - (soldierTexture.getHeight() / 2));
											if ((vector.x > posX) && (vector.x < (posX + soldierTexture.getWidth())) && (vector.y > posY) && (vector.y < (posY + soldierTexture.getHeight()))) {
												clickOnSoldier = true;
												SpriteTerritory currentSprite = ((SpriteTerritory) (sprites.get(index)));
												if (!currentSprite.isSelected()) {
													currentSprite.changeSelectionState();
												}
												if (ancientSpriteSelectedPos == index) {
													ancientSpriteSelectedPos = -1;
												}
												selection(index);
												if (selectedTroopNativeTerritory != null) {
													if ((selectedTroopNativeTerritory != territories.get(selectedTerritoryCoordinates)) || (selectedTroop != i)) {
														selectedTroopNativeTerritory = territories.get(selectedTerritoryCoordinates);
														selectedTroop = i;
													} else {
														selectedTroopNativeTerritory = null;
														selectedTroop = -1;
													}
												} else {
													selectedTroopNativeTerritory = territories.get(selectedTerritoryCoordinates);
													selectedTroop = i;
												}
												break;
											}
										}
										if (!clickOnSoldier) {
											updateSelection(index);
										}
									} else {
										updateSelection(index);
									}
								}
								if (client.getCurrentPlayer().getClass().equals(PlayerEngine.class)) {
									if (button == Input.Buttons.RIGHT) {
										if ((selectedTroop != null)) {
											if (!((SpriteTerritory) (sprites.get(index))).isSelected()) {
												updateSelection(index);
											}
											client.makeAction(new MoveTroopAction(selectedTroopNativeTerritory.getId(), selectedTroop, territories.get(selectedTerritoryCoordinates).getId()));
											selectedTroopNativeTerritory = null;
											selectedTroop = null;
										}
									}
								} else {
									System.out.println(Localisation.getInstance().getMessage("Not Your Turn"));
								}
								Gdx.graphics.requestRendering();
								return true;
							}
						}
					}
			}

			return false;

		}

		private void updateSelection(int index) {
			// Si ce territoire n'est pas déjà séléctionné
			SpriteTerritory currentSprite = (SpriteTerritory) (sprites.get(index));
			currentSprite.changeSelectionState();
			if (currentSprite.isSelected()) {
				selection(index);
			} else {
				ancientSpriteSelectedPos = -1;
				selectedTerritoryCoordinates = null;
				for (Button tb : troopButtons) {
					tb.setVisible(false);
				}
			}
		}

		private void selection(int index) {
			int x = (int) sprites.get(index).getX();
			int y = (int) sprites.get(index).getY();
			if (ancientSpriteSelectedPos != -1) {
				SpriteTerritory ancientSpriteSelected = (SpriteTerritory) sprites.get(ancientSpriteSelectedPos);
				ancientSpriteSelected.changeSelectionState();
			}
			selectedTerritoryCoordinates = new Coordinates(x, y);
			ancientSpriteSelectedPos = index;
			for (Button tb : troopButtons) {
				tb.setVisible(true);
			}
		}

	}

	/**
	 * Listener of Alliance button, who pop a dialog.
	 */

	public class AllianceListener extends ClickListener {

		@Override
		public void clicked(InputEvent event, float x, float y) {

			// Dialog allianceDialog = new Dialog("Alliance", skin, "dialog") {
			// protected void result(Object object) {
			// System.out.println("Chosen: " + object);
			// }
			// }.text(Localisation.getInstance().getMessage("Who Alliance"))
			// .button("Yes", true).button("No", false)
			// .key(Keys.ENTER, true).key(Keys.ESCAPE, false).show(stage);
			// }
			allianceDialog = new Dialog("Alliance", skinDialog, "dialog") {
				protected void result(Object object) {
					System.out.println("Chosen: " + object);
				}
			};
			openDialog = true;
			Label label = new Label(Localisation.getInstance().getMessage("Who Alliance"), skinDialog);
			label.setWrap(true);
			label.setFontScale(.8f);
			label.setAlignment(Align.center);

			allianceDialog.getContentTable().add(label).width(300).row();
			allianceDialog.getButtonTable().padTop(50);

			for (EntityEngine playerName : client.getPlayers()) {
				if (!playerName.equals(client.getCurrentPlayer())) {
					TextButton playerButton = new TextButton(playerName.getDataEntity().getName(), skinDialog);
					playerButton.addListener(new AskAllianceListener(playerName.getDataEntity()));
					allianceDialog.getContentTable().add(playerButton).row();

				}
			}

			TextButton closeButton = new TextButton(Localisation.getInstance().getMessage("Close"), skinDialog);
			closeButton.addListener(new CloseDialogListener());
			allianceDialog.getContentTable().add(closeButton).padTop(50).row();
			allianceDialog.key(Keys.ESCAPE, false);
			allianceDialog.invalidateHierarchy();
			allianceDialog.invalidate();
			allianceDialog.layout();
			allianceDialog.show(stage);
		}

	}

	/**
	 * Listener of Alliance button, who close the dialog.
	 */
	public class CloseDialogListener extends ClickListener {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			allianceDialog.hide();
			openDialog = false;
		}
	}

	/**
	 * Listener of Alliance button, who ask an alliance.
	 */
	public class AskAllianceListener extends ClickListener {

		private Player target;

		public AskAllianceListener(Player target) {
			this.target = target;
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			client.makeAction(new AskAllianceAction(client.getPlayersData().indexOf(target)));
			System.out.println(Localisation.getInstance().getMessage("Asked Alliance"));
			allianceDialog.hide();
			openDialog = false;
		}
	}

	/**
	 * Listener of Alliance, to accept an alliance.
	 */
	public class AcceptAllianceListener extends ClickListener {

		private Player target;

		public AcceptAllianceListener(Player target) {
			this.target = target;
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			client.getCurrentPlayer().getDataEntity().getAskAlliance().remove(target);
			client.makeAction(new AcceptAllianceAction(client.getPlayersData().indexOf(target)));
			askAllianceDialog.hide();
			openDialog = false;
		}
	}

	/**
	 * Listener of Alliance, to reject an alliance.
	 */

	public class RejectAllianceListener extends ClickListener {

		private Player target;

		public RejectAllianceListener(Player target) {
			this.target = target;
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			client.getCurrentPlayer().getDataEntity().getAskAlliance().remove(target);
			client.makeAction(new RejectAllianceAction(client.getPlayersData().indexOf(target)));
			askAllianceDialog.hide();
			openDialog = false;
		}
	}

	/**
	 * Listener of Pass button.
	 */
	public class PassListener extends ClickListener {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			pass();
		}
	}

	/**
	 * Listener of CreateTroop button
	 */
	public class CreateTroopListener extends ClickListener {

		private String troop;

		public CreateTroopListener(String troop) {
			super();
			this.troop = troop;
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			createTroop(troop);
		}
	}

	/**
	 * Listener of tutorial button
	 */
	public class HelpListener extends ClickListener {

		private Screen ancientScreen;

		public HelpListener(Screen ancientScreen) {
			this.ancientScreen = ancientScreen;
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			GuiScreen.getInstance().setScreen(new GuiHelp(ancientScreen));
		}
	}

	/**
	 * Listener to build a Barracks/Factory/Ramparts
	 */
	public class CreateBuildingListener extends ClickListener {

		private String building;

		public CreateBuildingListener(String building) {
			super();
			this.building = building;
		}

		@Override
		public void clicked(InputEvent event, float x, float y) {
			createBuilding(building);
		}
	}

}
