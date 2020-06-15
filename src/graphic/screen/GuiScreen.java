package graphic.screen;

import java.io.File;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

/**
 * Control which screen are use
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class GuiScreen extends Game {

	private static GuiScreen instance;
	private boolean musicState;
	private Music music;
	/**
	 * Width of screen
	 */
	public static float standartWidthDefiniton = 1600;
	
	/**
	 * Height of screen
	 */
	public static float standartHeightDefiniton = 900;
	
	public GuiScreen(){
		super();
		music = null;
	}

	public static GuiScreen getInstance() {
		return instance;
	}

	@Override
	public void create() {
		instance = this;
		setScreen(new GuiMenu());
	}

	@Override
	public void render() {
		super.render();
	}
	
	public void setMusic(Music music){
		this.music = music;
		musicState=true;
		music.setLooping(true);
	}
	
	public boolean getMusicState(){
		return musicState;
	}
	
	public void changeMusicState(){
		musicState = !musicState;
	}
	
	public Music getMusic(){
		return music;
	}
}
