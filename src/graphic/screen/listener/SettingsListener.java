package graphic.screen.listener;

import graphic.screen.GuiScreen;
import graphic.screen.GuiSettings;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Listener for Settings button
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class SettingsListener extends ClickListener {
	
	private Screen ancientScreen;
	
	public SettingsListener(Screen ancientScreen) {
		this.ancientScreen = ancientScreen;
	}
	
	@Override
	public void clicked(InputEvent event, float x, float y){
		GuiScreen.getInstance().setScreen(new GuiSettings(ancientScreen));
	}
}