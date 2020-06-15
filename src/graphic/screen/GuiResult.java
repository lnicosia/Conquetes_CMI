package graphic.screen;

import java.io.File;

import org.lwjgl.openal.AL;

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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;

import data.localisation.Localisation;

/**
 * The end of game screen
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class GuiResult implements Screen {

	private OrthographicCamera camera;
	private Stage stage;
	private Table buttonsTable;
	private Texture font,light;
	private SpriteBatch batch1;
	private BitmapFont buttons, title, credits,subtitle;
	private TextButton playAgain,closeGame;
	private Skin skin;
	private Screen ancientScreen;
	
	public GuiResult(Screen ancientScreen){
		super();
		this.ancientScreen = ancientScreen;
	}

	@Override
	public void show() {
		
		camera = new OrthographicCamera(GuiScreen.standartWidthDefiniton, GuiScreen.standartHeightDefiniton);
		camera.setToOrtho(false, GuiScreen.standartWidthDefiniton, GuiScreen.standartHeightDefiniton);
		camera.update();
		
		title = new BitmapFont(Gdx.files.internal("resources" + File.separator + "font" + File.separator + "big.fnt"), Gdx.files.internal("resources" + File.separator + "font" + File.separator + "big.png"), false);
		credits = new BitmapFont(Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.fnt"), Gdx.files.internal("resources" + File.separator + "font" + File.separator + "version.png"), false);
		subtitle = new BitmapFont(Gdx.files.internal("resources" + File.separator + "font" + File.separator + "subtitle.fnt"), Gdx.files.internal("resources" + File.separator + "font" + File.separator + "subtitle.png"), false);

		stage = new Stage();
		buttonsTable = new Table();
		//languageTable = new Table();
		skin = new Skin(Gdx.files.internal("resources/menu.json"), new TextureAtlas(Gdx.files.internal("resources/images/buttons/menu.pack")));
		
		batch1 = new SpriteBatch();
		playAgain = new TextButton(Localisation.getInstance().getMessage("Yes"), skin);
		closeGame = new TextButton(Localisation.getInstance().getMessage("No"), skin);


		closeGame.addListener(new closeGameListener());
		playAgain.addListener(new playAgainListener());

		
		font = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "fontmenu.png"));
		light = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "light.png"));
		
		buttonsTable.add(playAgain).padTop(400).padBottom(70).row();
		buttonsTable.add(closeGame).padTop(30).padBottom(70).row();
		
		
		
		buttonsTable.setFillParent(true);
		//languageTable.setFillParent(true);
		
		stage.addActor(buttonsTable);

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch1.setProjectionMatrix(camera.combined);
		stage.getBatch().setProjectionMatrix(camera.combined);

		batch1.begin();
		batch1.draw(font, 0, 0);
		batch1.draw(light,(float)Math.random() * GuiScreen.standartWidthDefiniton+ 20,(float)Math.random() * GuiScreen.standartHeightDefiniton+ 5);
		title.draw(batch1, "Age of Empires", 470, 880);
		subtitle.draw(batch1, "CMI Civilization", 570, 770);
		subtitle.draw(batch1, Localisation.getInstance().getMessage("End Of Game"), 580, 630);
		subtitle.draw(batch1, Localisation.getInstance().getMessage("Replay"), 380, 500);
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
	
	public class closeGameListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			AL.destroy();
			Gdx.app.exit();
			System.exit(0);
		}
	}
	
	public class playAgainListener extends ClickListener {
			
		@Override
		public void clicked(InputEvent event, float x, float y){
			 GuiScreen.getInstance().setScreen(new GuiMenu());
		}
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