package graphic.screen;

import java.io.File;

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
 * Settings screen
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class GuiSettings implements Screen {

	private OrthographicCamera camera;
	private Stage stage;
	private Table settingsTable, languageTable;
	private Texture font,light;
	private SpriteBatch batch1;
	private BitmapFont buttons, title, credits,subtitle;
	private TextButton language, fr, en, returnMainSettings, returnSettings;
	private Skin skin;
	private Screen ancientScreen;
	
	public GuiSettings(Screen ancientScreen){
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
		settingsTable = new Table();
		languageTable = new Table();
		skin = new Skin(Gdx.files.internal("resources/menu.json"), new TextureAtlas(Gdx.files.internal("resources/images/buttons/menu.pack")));
		
		batch1 = new SpriteBatch();
		language = new TextButton(Localisation.getInstance().getMessage("Language"), skin);
		fr = new TextButton("Fr", skin);
		en = new TextButton("En", skin);
		returnMainSettings = new TextButton(Localisation.getInstance().getMessage("Return"), skin);
		returnSettings = new TextButton(Localisation.getInstance().getMessage("Return"), skin);


		returnMainSettings.addListener(new ReturnMainListener());
		language.addListener(new LanguageListener());
		returnSettings.addListener(new ReturnSettingsListener());
		en.addListener(new EnListener());
		fr.addListener(new FrListener());
		
		font = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "fontmenu.png"));
		light = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "light.png"));

		settingsTable.add(returnMainSettings).padTop(200).padBottom(70).row();
		settingsTable.add(language).padBottom(70).row();
		
		languageTable.add(returnSettings).padTop(200).padBottom(70).row();
		languageTable.add(en).padBottom(70).row();
		languageTable.add(fr).padBottom(70).row();

		
		settingsTable.setFillParent(true);
		languageTable.setFillParent(true);
		
		stage.addActor(settingsTable);

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
		credits.draw(batch1, Localisation.getInstance().getMessage("Authors"), 10, 20);
		credits.draw(batch1, "V 1.1", 40, 880);
		
		batch1.end();

		stage.act();
		stage.draw();
		
	}

	public void dispose() {
		font.dispose();
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
	
	public class LanguageListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			settingsTable.remove();
			stage.addActor(languageTable);
		}
	}
	
	public class ReturnMainListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			GuiScreen.getInstance().setScreen(ancientScreen);
		}
	}
	
	public class ReturnSettingsListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			languageTable.remove();
			stage.addActor(settingsTable);
		}
	}
	
	public class EnListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			Localisation.getInstance().setLocalisation("en");
			refreshButtons();
		}
	}
	
	public class FrListener extends ClickListener {
		
		@Override
		public void clicked(InputEvent event, float x, float y){
			Localisation.getInstance().setLocalisation("fr");
			refreshButtons();
		}
	}
	
	public void refreshButtons(){
		language.setText(Localisation.getInstance().getMessage("Language"));
		fr.setText("Fr");
		en.setText("En");
		returnMainSettings.setText(Localisation.getInstance().getMessage("Return"));
		returnSettings.setText(Localisation.getInstance().getMessage("Return"));
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