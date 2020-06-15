package graphic.screen;

import graphic.screen.GuiMenu.LaunchMapListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import network.Client;
import network.Server;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import data.localisation.Localisation;
import engine.exception.GameNotLoadException;
import engine.game.GameLoop;
import engine.game.GameStockage;

/**
 * Screen for load a saved game
 * @author @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class GuiLoading implements Screen {

	private OrthographicCamera camera;
	private Stage stage;
	private Table savesTable, table;
	private Texture font, savesTexture, light;
	private SpriteBatch batch1, savesBatch;
	private BitmapFont buttons, title, credits;
	private TextButton returnMain;
	private Skin skin;
	private Screen ancientScreen;
	private ScrollPane scrollPane;
	private ArrayList<TextButton> saves;
	
	public GuiLoading(Screen ancientScreen){
		super();
		this.ancientScreen = ancientScreen;
	}

	@Override
	public void show() {

		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, GuiScreen.standartWidthDefiniton, GuiScreen.standartHeightDefiniton);
		camera.update();
		
		title = new BitmapFont(Gdx.files.internal("resources" + File.separator + "font" + File.separator + "big.fnt"), Gdx.files.internal("resources" + File.separator + "font" + File.separator + "big.png"), false);
		credits = new BitmapFont(Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.fnt"), Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.png"), false);

		stage = new MenuStage();
		savesTable = new Table();
		table = new Table();
		skin = new Skin(Gdx.files.internal("resources/menu.json"), new TextureAtlas(Gdx.files.internal("resources/images/buttons/menu.pack")));

		saves = new ArrayList<TextButton>();
		batch1 = new SpriteBatch();
		savesBatch = new SpriteBatch();
		returnMain = new TextButton(Localisation.getInstance().getMessage("Return"), skin);

		File savesFolder= new File("save");
		File[] f = 	savesFolder.listFiles();

		returnMain.addListener(new ReturnMainListener());
		
		font = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "fontmenu.png"));
		light = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "light.png"));
		
		savesTexture = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "savesBackground.png"));

		for(int i = 0 ; i<f.length; i++){
			TextButton save = new TextButton(f[i].getName(), skin);
			save.addListener(new LaunchSaveListener(f[i].getName()));
			table.add(save).padTop(20).row();
		}
		scrollPane = new ScrollPane(table);
		savesTable.add(scrollPane).row();
		savesTable.add(returnMain).padTop(20).row();
		savesTable.setFillParent(true);
		
		stage.addActor(savesTable);

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch1.setProjectionMatrix(camera.combined);
		savesBatch.setProjectionMatrix(camera.combined);
		stage.getBatch().setProjectionMatrix(camera.combined);

		batch1.begin();
		batch1.draw(font, 0, 0);

		batch1.draw(light,(float)Math.random() * GuiScreen.standartWidthDefiniton+ 20,(float)Math.random() * GuiScreen.standartHeightDefiniton+ 5);
		title.draw(batch1, "Age Of Swag", 400, 810);
		credits.draw(batch1, Localisation.getInstance().getMessage("Authors"), 10, 20);
		credits.draw(batch1, "V 1.1", 40, 880);
		
		batch1.end();

		stage.act();
		stage.draw();
		
	}

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
	
	
	public class ReturnMainListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			GuiScreen.getInstance().setScreen(ancientScreen);
		}
	}
	
	public class LaunchSaveListener extends ClickListener {
		
		private String f;
		public LaunchSaveListener(String f){
			this.f=f;
		}
		@Override
		public void clicked(InputEvent event, float x, float y){
			
			try {
				GameLoop GL = GameStockage.getInstance().load(f);
				GL.resumeLoop();
				Server serv = new Server(6788, GL, 1);
				serv.start();
				Client c = new Client("localhost", 6788, false);
				GuiScreen.getInstance().setScreen(new GuiGame(c));
			} catch (IOException | GameNotLoadException e) {
				System.err.println("Erreur de connexion");
			}

		}
	}
	
	public void refreshButtons(){
		returnMain.setText(Localisation.getInstance().getMessage("Return"));
	}
	
	public class MenuStage extends Stage {

		@Override
		public boolean keyUp(int keycode) {
			if (keycode == Input.Keys.ESCAPE) {
				GuiScreen.getInstance().setScreen(ancientScreen);
			}
			return true;
		}
	}
}