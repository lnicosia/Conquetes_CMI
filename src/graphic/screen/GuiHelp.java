package graphic.screen;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import data.localisation.Localisation;

/**
 * Display the tutorial
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class GuiHelp implements Screen {

	private OrthographicCamera camera;
	private Stage stage;
	private Texture font;
	private SpriteBatch batch1;
	private TextButton closeHelp;
	private Skin skin;
	private Screen ancientScreen;
	
	public GuiHelp(Screen ancientScreen){
		super();
		this.ancientScreen = ancientScreen;
	}

	@Override
	public void show() {
		
		camera = new OrthographicCamera(GuiScreen.standartWidthDefiniton, GuiScreen.standartHeightDefiniton);
		camera.setToOrtho(false, GuiScreen.standartWidthDefiniton, GuiScreen.standartHeightDefiniton);
		camera.update();

		stage = new Stage();
		skin = new Skin(Gdx.files.internal("resources/menu.json"), new TextureAtlas(Gdx.files.internal("resources/images/buttons/menu.pack")));
		
		batch1 = new SpriteBatch();
		closeHelp = new TextButton(Localisation.getInstance().getMessage("Close"), skin);

		closeHelp.addListener(new closeHelpListener());
		;
		if(Localisation.getInstance().getLanguage().equals("en")){

		font = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "help.jpg"));
		}
		else if(Localisation.getInstance().getLanguage().equals("fr")){
			font = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "helpFR.jpg"));

		}
		else{
			font = new Texture(Gdx.files.internal("resources" + File.separator + "images" + File.separator + "help.jpg"));

		}
		closeHelp.setPosition(1270, 40);
		stage.addActor(closeHelp);

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
		batch1.draw(font, 0,0);
		if (Gdx.input.isKeyPressed(Keys.ESCAPE)){
			GuiScreen.getInstance().setScreen(ancientScreen);
		}
		batch1.end();

		stage.act();
		stage.draw();
		
	}

	public void dispose() {
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
	public void resume() {
		// TODO Auto-generated method stub

	}
	

	public class closeHelpListener extends ClickListener {

		
		@Override
		public void clicked(InputEvent event, float x, float y){
			GuiScreen.getInstance().setScreen(ancientScreen);
		}
	}


	@Override
	public void resize(int arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}