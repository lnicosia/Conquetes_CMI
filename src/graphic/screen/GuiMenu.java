package graphic.screen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import network.Client;
import network.Server;

import org.lwjgl.openal.AL;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import data.localisation.Localisation;
import engine.exception.GameNotLoadException;
import engine.exception.GameNotSaveException;
import engine.game.GameInitializator;
import engine.game.GameLoop;
import engine.game.GameStockage;
import graphic.screen.listener.LoadingListener;
import graphic.screen.listener.SettingsListener;
/**
 * The menu screen
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */

public class GuiMenu implements Screen {

	private OrthographicCamera camera;
	private Stage stage;
	private Table table, newGameTable, localTable, onlineTable, localNormalGameTable, localRandomGameTable, scrollTable, editorTable;
	private Texture font,light;
	private SpriteBatch batch1;
	private BitmapFont buttons, title, credits,subtitle;
	
	private TextButton newGame, loadGame, editor, buttonCredits, settings, exit;
	//Menu principal
	
	private TextButton local, online, returnNewGame, returnNewGameBis;
	//Menu nouvelle partie

	private TextButton normalGame, randomGame, editorMap, returnMainPlay;
	//Menu local
	
	//Nouvelle partie locale normale
	private TextButton launchNormalGame, plusPlayerN, lessPlayerN, plusEasy, lessEasy, plusNormal, lessNormal;
	private Label playerShowN, EasyShow, NormalShow, totalShowN;
	private Integer totalCountN, playerCountN, EasyCount, NormalCount;
	
	//Nouvelle partie locale aléatoire
	private TextButton launchRandomGame, plusPlayerR, lessPlayerR, plusIA, lessIA;
	private Label playerShowR, IAShow, totalShowR;
	private Integer totalCountR, playerCountR, IACount;
	
	//Chargement map �diteur
	private ScrollPane scrollPane;
	private ArrayList<TextButton> maps;
	
	//Nouvelle partie online
	private TextButton host, join, plusPlayerO, lessPlayerO;
	private Label joinPortLabel, hostPortLabel, IPLabel, playerShowO;
	private String IP, joinPort, hostPort;
	private TextField joinPortField, hostPortField, IPField;
	private Integer playerCountO;
	
	//Music
	private TextButton musicButton;
	
	
	private Skin skin, uiskin;
	/**
	 * Initialize the menu
	 */
	public GuiMenu(){
		super();
		
		//Save & Load
		try {
			GameStockage.getInstance().setPath( "save" + File.separator);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		if(GuiScreen.getInstance().getMusic() == null){
			GuiScreen.getInstance().setMusic(Gdx.audio.newMusic(Gdx.files.internal("resources" + File.separator +"sounds" + File.separator  + "music.mp3")));
			GuiScreen.getInstance().getMusic().play();
		}
	}

	/**
	 * Called when this screen becomes the current screen for a Game.
	 */
	@Override
	public void show() {
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GuiScreen.standartWidthDefiniton, GuiScreen.standartHeightDefiniton);

		camera.update();
		
		playerCountN = 2;
		EasyCount = 0;
		NormalCount = 0;
		totalCountN = playerCountN+EasyCount+NormalCount;
		
		playerCountO = 2;
		
		playerCountR = 2;
		IACount = 0;
		totalCountR = playerCountR+IACount;
		
		IP = "localhost";
		joinPort ="8080";
		hostPort ="8080";
		
		File mapsFolder= new File("maps");
		if(!mapsFolder.exists()){
			mapsFolder.mkdirs();
		}
		File[] f = 	mapsFolder.listFiles();
		
		maps = new ArrayList<TextButton>();
		
		title = new BitmapFont(Gdx.files.internal("resources" + File.separator + "font" + File.separator + "big.fnt"), Gdx.files.internal("resources" + File.separator + "font" + File.separator + "big.png"), false);
		subtitle = new BitmapFont(Gdx.files.internal("resources" + File.separator + "font" + File.separator + "subtitle.fnt"), Gdx.files.internal("resources" + File.separator + "font" + File.separator + "subtitle.png"), false);

		credits = new BitmapFont(Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.fnt"), Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.png"), false);

		stage = new MenuStage();
		table = new Table();
		newGameTable = new Table();
		localTable = new Table();
		onlineTable = new Table();
		localNormalGameTable = new Table();
		localRandomGameTable = new Table();
		scrollTable = new Table();
		editorTable = new Table();
		
		skin = new Skin(Gdx.files.internal("resources/menu.json"), new TextureAtlas(Gdx.files.internal("resources/images/buttons/menu.pack")));
		uiskin = new Skin(Gdx.files.internal("resources/uiskin.json"));
		
		batch1 = new SpriteBatch();
		
		// Menu principal
		newGame = new TextButton(Localisation.getInstance().getMessage("New Game"), skin);
		loadGame = new TextButton(Localisation.getInstance().getMessage("Load Game"), skin);
		settings = new TextButton(Localisation.getInstance().getMessage("Settings"), skin);
		buttonCredits = new TextButton(Localisation.getInstance().getMessage("Credits"), skin);
		exit = new TextButton(Localisation.getInstance().getMessage("Exit"), skin);
		
		// Menu nouvelle partie
		local = new TextButton(Localisation.getInstance().getMessage("Local"),skin);
		online = new TextButton(Localisation.getInstance().getMessage("Online"), skin);
		returnMainPlay = new TextButton(Localisation.getInstance().getMessage("Return"), skin);	
		
		// Menu local
		normalGame = new TextButton(Localisation.getInstance().getMessage("Normal Game"), skin);
		randomGame = new TextButton(Localisation.getInstance().getMessage("Random Game"), skin);
		editorMap = new TextButton(Localisation.getInstance().getMessage("Editor map"), skin);
		editor = new TextButton(Localisation.getInstance().getMessage("Editor"), skin);
		returnNewGame = new TextButton(Localisation.getInstance().getMessage("Return"), skin);	
		
		// Menu partie normale
		launchNormalGame = new TextButton(Localisation.getInstance().getMessage("Launch"),skin);
		playerShowN = new Label(Localisation.getInstance().getMessage("Players number")+" : "+playerCountN.toString(),skin);
		EasyShow = new Label(Localisation.getInstance().getMessage("Easys number")+" : "+EasyCount.toString(),skin);
		NormalShow = new Label(Localisation.getInstance().getMessage("Normals number")+" : "+NormalCount.toString(),skin);
		totalShowN = new Label("Total : "+totalCountN+"/8".toString(),skin);
		plusPlayerN = new TextButton("+",skin);
		lessPlayerN = new TextButton("-",skin);
		plusEasy = new TextButton("+",skin);
		lessEasy = new TextButton("-",skin);
		plusNormal = new TextButton("+",skin);
		lessNormal = new TextButton("-",skin);
		
		// Menu partie al�atoire
		launchRandomGame = new TextButton(Localisation.getInstance().getMessage("Launch"),skin);
		playerShowR = new Label(Localisation.getInstance().getMessage("Players number")+" : "+playerCountN.toString(),skin);
		IAShow = new Label(Localisation.getInstance().getMessage("AI number")+" : "+NormalCount.toString(),skin);
		totalShowR = new Label("Total : "+totalCountN+"/8".toString(),skin);
		plusPlayerR = new TextButton("+",skin);
		lessPlayerR = new TextButton("-",skin);
		plusIA = new TextButton("+",skin);
		lessIA = new TextButton("-",skin);
		
		// Menu partie online
		host = new TextButton(Localisation.getInstance().getMessage("Host"), skin);
		join = new TextButton(Localisation.getInstance().getMessage("Join"), skin);
		joinPortField = new TextField(joinPort, uiskin);
		hostPortField = new TextField(hostPort, uiskin);
		IPField = new TextField(IP, uiskin);
		joinPortLabel = new Label(Localisation.getInstance().getMessage("Port")+" : " , skin);
		hostPortLabel = new Label(Localisation.getInstance().getMessage("Port")+" : " , skin);
		IPLabel = new Label(Localisation.getInstance().getMessage("IP")+" : " , skin);
		playerShowO = new Label(Localisation.getInstance().getMessage("Players number")+" : "+playerCountO+"/8", skin);
		plusPlayerO = new TextButton("+",skin);
		lessPlayerO = new TextButton("-",skin);
		
		for(int i = 0 ; i<f.length; i++){
			TextButton map = new TextButton(f[i].getName(), skin);
			map.addListener(new LaunchMapListener(f[i].getName()));
			maps.add(map);
		}
		

		
		musicButton = new TextButton("Music : On", skin);
				
		returnNewGameBis = new TextButton(Localisation.getInstance().getMessage("Return"), skin);	

		newGame.addListener(new NewGameListener());
		loadGame.addListener(new LoadingListener(this));
		exit.addListener(new ExitListener());
		settings.addListener(new SettingsListener(this));
		editor.addListener(new EditorListener());
		
		randomGame.addListener(new RandomGameListener());
		normalGame.addListener(new NormalGameListener());
		returnMainPlay.addListener(new ReturnMainListener());
		
		local.addListener(new LocalListener());
		online.addListener(new OnlineListener());
		returnNewGame.addListener(new ReturnNewGameListener());
		returnNewGameBis.addListener(new ReturnNewGameBisListener());
		
		launchNormalGame.addListener(new LaunchNormalGameListener());
		launchRandomGame.addListener(new LaunchRandomGameListener());
		
		plusPlayerN.addListener(new plusPlayerNListener());
		lessPlayerN.addListener(new lessPlayerNListener());
		plusEasy.addListener(new PlusEasyListener());
		lessEasy.addListener(new LessEasyListener());
		plusNormal.addListener(new PlusNormalListener());
		lessNormal.addListener(new LessNormalListener());
		
		plusPlayerR.addListener(new plusPlayerRListener());
		lessPlayerR.addListener(new lessPlayerRListener());
		plusIA.addListener(new PlusIAListener());
		lessIA.addListener(new LessIAListener());
		
		plusPlayerO.addListener( new plusPlayerOListener());
		lessPlayerO.addListener( new lessPlayerOListener());
		
		host.addListener(new HostListener());
		join.addListener(new JoinListener());
		
		editorMap.addListener(new EditorMapListener());
		
		musicButton.addListener(new MusicButtonListener());
		
		font = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "fontmenu.png"));
		light = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "light.png"));
		
		newGameTable.add(local).padTop(200).padBottom(70).row();
		newGameTable.add(online).padBottom(70).row();
		newGameTable.add(returnMainPlay).padBottom(70).row();
		
		table.add(newGame).height(50).padTop(180).padBottom(30).row();
		table.add(loadGame).height(50).padBottom(30).row();
		table.add(editor).height(50).padBottom(30).row();
		table.add(settings).height(50).padBottom(30).row();
		table.add(buttonCredits).height(50).padBottom(30).row();
		table.add(exit).height(50).padBottom(30).row();
		table.add(musicButton).height(50);
		
		localTable.add(normalGame).padTop(200).padBottom(70).row();
		localTable.add(randomGame).padBottom(70).row();
		localTable.add(editorMap).padBottom(70).row();
		localTable.add(returnNewGame).padBottom(70).row();
		
		localNormalGameTable.add(playerShowN);
		localNormalGameTable.add(plusPlayerN).width(60);
		localNormalGameTable.add(lessPlayerN).width(60).row();
		localNormalGameTable.add(EasyShow);
		localNormalGameTable.add(plusEasy).width(60);
		localNormalGameTable.add(lessEasy).width(60).row();
		localNormalGameTable.add(NormalShow);
		localNormalGameTable.add(plusNormal).width(60);
		localNormalGameTable.add(lessNormal).width(60).row();
		localNormalGameTable.add(totalShowN);
		localNormalGameTable.add(launchNormalGame);
		
		localRandomGameTable.add(playerShowR);
		localRandomGameTable.add(plusPlayerR).width(60);
		localRandomGameTable.add(lessPlayerR).width(60).row();
		localRandomGameTable.add(IAShow);
		localRandomGameTable.add(plusIA).width(60);
		localRandomGameTable.add(lessIA).width(60).row();
		localRandomGameTable.add(totalShowR);
		localRandomGameTable.add(launchRandomGame);
		
		onlineTable.add(host);
		onlineTable.add(hostPortLabel);
		onlineTable.add(hostPortField).row();
		onlineTable.add(playerShowO);
		onlineTable.add(plusPlayerO).width(60);
		onlineTable.add(lessPlayerO).width(60).row();
		onlineTable.add(IPLabel).padTop(50);
		onlineTable.add(IPField).padTop(50).row();
		onlineTable.add(join);
		onlineTable.add(joinPortLabel);
		onlineTable.add(joinPortField).row();
		onlineTable.add(returnNewGameBis).padTop(50).colspan(3);
		
		for(int index = 0; index < maps.size() ; index ++){
			scrollTable.add(maps.get(index)).height(50).padTop(20).row();;
		}
		scrollPane= new ScrollPane(scrollTable);
		editorTable.add(scrollPane);
		
		table.setFillParent(true);
		newGameTable.setFillParent(true);
		localTable.setFillParent(true);
		onlineTable.setFillParent(true);
		localNormalGameTable.setFillParent(true);
		localRandomGameTable.setFillParent(true);
		editorTable.setFillParent(true);
		
		stage.addActor(table);

		Gdx.input.setInputProcessor(stage);
	}
	/**
	 * Called when the screen should render itself.
	 */
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch1.setProjectionMatrix(camera.combined);
		stage.getBatch().setProjectionMatrix(camera.combined);

		batch1.begin();
		batch1.draw(font, 0, 0);
		batch1.draw(light,(float)Math.random() * GuiScreen.standartWidthDefiniton+ 20,(float)Math.random() * GuiScreen.standartHeightDefiniton+ 5);
		title.draw(batch1, "Age of Swag", 470, 880);
		subtitle.draw(batch1, "Conquest of CMI", 570, 770);
		credits.draw(batch1, Localisation.getInstance().getMessage("Authors"), 10, 20);
		credits.draw(batch1, "V 1.1", 40, 880);
		
		Pixmap pm = new Pixmap(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "cursor.png"));
		Gdx.input.setCursorImage(pm, 0, 0);
		pm.dispose();
		
		batch1.end();

		stage.act();
		stage.draw();
		
	}
	
	
	/**
	 * Releases all resources of this object.
	 */
	public void dispose() {
		font.dispose();
		// batch1.dispose();
		buttons.dispose();
		title.dispose();
		credits.dispose();
		stage.dispose();

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
	public void resize(int width, int height) {
		
		Array<Actor> list = stage.getActors();
		stage = new MenuStage();
		for(Actor a : list){
			stage.addActor(a);
		}
		Gdx.input.setInputProcessor(stage);
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}
	
	/**
	 * Button listener to make a Random game(number of players, etc).
	 */
	public class RandomGameListener extends ClickListener {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			localTable.remove();
			stage.addActor(localRandomGameTable);
		}
	}
	/**
	 * Button listener to launch a normal game.
	 */
	public class NormalGameListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			localTable.remove();
			stage.addActor(localNormalGameTable);
		}
	}
	
	/**
	 * Button listener to make a Normal game(number of players, etc).
	 */
	public class LaunchNormalGameListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y){
			ArrayList<String> iaNames = new ArrayList<String>();
			for(int i = 0; i < EasyCount; i++){
				iaNames.add("Easy");
			}
			for(int i = 0; i < NormalCount; i++){
				iaNames.add("Normal");
			}
			GameInitializator gi = new GameInitializator("normal",2,totalCountN,iaNames);
			try {
				Server serv = new Server(6788, new GameLoop(gi), 1);
				serv.start();
				Client c = new Client("localhost", 6788, false);
				GuiScreen.getInstance().setScreen(new GuiGame(c));
			} catch (IOException e) {
				System.err.println("Erreur de connexion");
			}
		}
	}
	
	public class LaunchRandomGameListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y){
			ArrayList<String> iaNames = new ArrayList<String>();
			for(int i = 0; i < IACount; i++){
				int IAtype = (int) (Math.random()*2)  ;
				if(IAtype==0){
					iaNames.add("Easy");
				}
				else{
					iaNames.add("Normal");
				}
			}
			GameInitializator gi = new GameInitializator("random",2,totalCountR,iaNames);
			try {
				Server serv = new Server(6788, new GameLoop(gi), 1);
				serv.start();
				Client c = new Client("localhost", 6788, false);
				GuiScreen.getInstance().setScreen(new GuiGame(c));
			} catch (IOException e) {
				System.err.println("Erreur de connexion");
			}
		}
	}
	
	public class MusicButtonListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y){
			if(GuiScreen.getInstance().getMusicState()){
				GuiScreen.getInstance().changeMusicState();
				GuiScreen.getInstance().getMusic().stop();
				musicButton.setText("Music : Off");
			}
			else{
				GuiScreen.getInstance().changeMusicState();
				GuiScreen.getInstance().getMusic().play();
				musicButton.setText("Music : On");
			}
		}
	}
	/**
	 * Button listener to access to editor map.
	 */
	public class EditorListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y){
			GuiScreen.getInstance().setScreen(new GuiEditor());
		}
	}
	
	/**
	 * To make a newgame
	 */
	public class NewGameListener extends ClickListener {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			table.remove();
			stage.addActor(newGameTable);
		}
	}
	
	/**
	 * Add human player.
	 */
	public class plusPlayerNListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if(totalCountN>7){
				System.out.println("Nombre de joueur maximum atteint !");
			}
			else{
				playerCountN++;
			}
			totalCountN=EasyCount+NormalCount+playerCountN;
			refreshButtons();
		}
	}
	
	/**
	 * Remove human player.
	 */
	public class lessPlayerNListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if((totalCountN>2)&&(playerCountN>1)){
				playerCountN--;
				totalCountN=EasyCount+NormalCount+playerCountN;
				refreshButtons();
			}
		}
	}
	
	public class plusPlayerOListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if(playerCountO>7){
				System.out.println("Nombre de joueur maximum atteint !");
			}
			else{
				playerCountO++;
			}
			refreshButtons();
		}
	}
	
	/**
	 * Remove human player.
	 */
	public class lessPlayerOListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if(playerCountO>2){
				playerCountO--;
				refreshButtons();
			}
		}
	}
	
	/**
	 * Add human player.
	 */
	public class plusPlayerRListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if(totalCountR>7){
				System.out.println("Nombre de joueur maximum atteint !");
			}
			else{
				playerCountR++;
			}
			totalCountR=IACount+playerCountR;
			refreshButtons();
		}
	}
	
	/**
	 * Add IA player.
	 */
	public class PlusIAListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if(totalCountR<8){
				IACount++;
				totalCountR=IACount+playerCountR;
				refreshButtons();
			}
		}
	}
	/**
	 * Remove IA player.
	 */
	public class LessIAListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if((totalCountR>2)&&(IACount>0)){	
				IACount--;
				totalCountR=IACount+playerCountR;
				refreshButtons();
			}
		}
	}
	
	/**
	 * Remove human player.
	 */
	public class lessPlayerRListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if((totalCountR>2)&&(playerCountR>1)){
				playerCountR--;
				totalCountR=IACount+playerCountR;
				refreshButtons();
			}
		}
	}
	/**
	 * Add IA player.
	 */
	public class PlusEasyListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if(totalCountN<8){
				EasyCount++;
				totalCountN=EasyCount+NormalCount+playerCountN;
				refreshButtons();
			}
		}
	}
	/**
	 * Remove IA player.
	 */
	public class LessEasyListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if((totalCountN>2)&&(EasyCount>0)){	
				EasyCount--;
				totalCountN=EasyCount+NormalCount+playerCountN;
				refreshButtons();
			}
		}
	}
	
	/**
	 * Add IA player.
	 */
	public class PlusNormalListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if(totalCountN<8){
				NormalCount++;
				totalCountN=EasyCount+NormalCount+playerCountN;
				refreshButtons();
			}
		}
	}
	/**
	 * Remove IA player.
	 */
	public class LessNormalListener extends ClickListener {
		@Override
		public void clicked(InputEvent event, float x, float y) {
			if((totalCountN>2)&&(NormalCount>0)){	
				NormalCount--;
				totalCountN=EasyCount+NormalCount+playerCountN;
				refreshButtons();
			}
		}
	}
	
	
	/**
	 * To Load a game
	 */
	public class LoadGameListener extends ClickListener {

		@Override
		public void clicked(InputEvent event, float x, float y) {
		}
	}
	
	/**
	 * Exit the game
	 */

	public class ExitListener extends ClickListener {

		@Override
		public void clicked(InputEvent event, float x, float y) {
			AL.destroy();
			Gdx.app.exit();
			System.exit(0);
		}
	}
	/**
	 * Return to Menu.
	 */
	public class ReturnMainListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			newGameTable.remove();
			stage.addActor(table);
		}
	}
	/**
	 * Return to New game section.
	 */
	public class ReturnNewGameListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			localTable.remove();
			stage.addActor(newGameTable);
		}
	}
	
	/**
	 * Return to New game bis section.
	 */
	public class ReturnNewGameBisListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			onlineTable.remove();
			stage.addActor(newGameTable);
		}
	}
	
	/**
	 * Listener to make a Local Game.
	 */
	public class LocalListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			newGameTable.remove();
			stage.addActor(localTable);
		}
	}

	/**
	 * Listener to make an online game.
	 */

	public class OnlineListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			newGameTable.remove();
			stage.addActor(onlineTable);
		}
	}
	
	public class HostListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			GameInitializator gi = new GameInitializator("normal", 2, playerCountO);
			try {
				int port = Integer.parseInt(hostPortField.getText());
				Server serv = new Server(port, new GameLoop(gi), playerCountO);
				serv.start();
				Client c = new Client("localhost", port, true);
				GuiScreen.getInstance().setScreen(new GuiGame(c));
			} catch (IOException e) {
				System.err.println("Erreur de connexion");
			} catch (NumberFormatException e){
				System.err.println(e.getMessage());
			}
		}
	}
	
	public class JoinListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			try {
				int port = Integer.parseInt(joinPortField.getText());
				Client c = new Client(IPField.getText(), port, true);
				GuiScreen.getInstance().setScreen(new GuiGame(c));
			} catch (IOException e) {
				System.err.println("Erreur de connexion");
			} catch (NumberFormatException e){
				System.err.println(e.getMessage());
			}
			
		}
	}
	
	public class EditorMapListener extends ClickListener{
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			localTable.remove();
			stage.addActor(editorTable);
		}
	}
	
	public class LaunchMapListener extends ClickListener{
		
		String file;
		public LaunchMapListener(String file){
			this.file=file;
		}
		@Override
		public void clicked(InputEvent event, float x, float y){
			try{
				GameInitializator gi = GameStockage.getInstance().loadMap(file);
				try {
					Server serv = new Server(6788, new GameLoop(gi), 1);
					serv.start();
					Client c = new Client("localhost", 6788, false);
					GuiScreen.getInstance().setScreen(new GuiGame(c));
				} catch (IOException e) {
					System.err.println("Erreur de connexion");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (GameNotLoadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	/**
	 * Display buttons.
	 */
	
	public void refreshButtons(){
		newGame.setText(Localisation.getInstance().getMessage("New Game"));
		loadGame.setText(Localisation.getInstance().getMessage("Load Game"));
		settings.setText(Localisation.getInstance().getMessage("Settings"));
		buttonCredits.setText(Localisation.getInstance().getMessage("Credits"));
		exit.setText(Localisation.getInstance().getMessage("Exit"));
		returnMainPlay.setText(Localisation.getInstance().getMessage("Return"));
		
		playerShowN.setText(Localisation.getInstance().getMessage("Players number")+" : "+playerCountN.toString());
		EasyShow.setText(Localisation.getInstance().getMessage("Easys number")+" : "+EasyCount.toString());
		NormalShow.setText(Localisation.getInstance().getMessage("Normals number")+" : "+NormalCount.toString());
		totalShowN.setText("Total : "+totalCountN+"/8".toString());
		
		playerShowR.setText(Localisation.getInstance().getMessage("Players number")+" : "+playerCountR.toString());
		IAShow.setText(Localisation.getInstance().getMessage("AI number")+" : "+IACount.toString());
		totalShowR.setText("Total : "+totalCountR+"/8".toString());
		playerShowO.setText(Localisation.getInstance().getMessage("Players number")+" : "+playerCountO+"/8".toString());
	}
	
	public class MenuStage extends Stage {

		@Override
		public boolean keyUp(int keycode) {
			if (keycode == Input.Keys.P) {
				GameInitializator gi = new GameInitializator("random", 2, 2);
				try {
					Server serv = new Server(6788, new GameLoop(gi), 1);
					serv.start();
					Client c = new Client("localhost", 6788, false);
					GuiScreen.getInstance().setScreen(new GuiGame(c));
				} catch (IOException e) {
					System.err.println("Erreur de connexion");
				}
			} else if (keycode == Input.Keys.ESCAPE) {
				AL.destroy();
				Gdx.app.exit();
				System.exit(0);
			}
			return true;
		}
	}

}