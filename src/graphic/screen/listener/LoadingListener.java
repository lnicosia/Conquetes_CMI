package graphic.screen.listener;

import graphic.screen.GuiLoading;
import graphic.screen.GuiScreen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Listener for loading button
 * @author Arya JEMO, Bastien LEPESANT, Lucas NICOSIA
 *
 */
public class LoadingListener extends ClickListener {
	
	private Screen ancientScreen;
	
	public LoadingListener(Screen ancientScreen) {
		this.ancientScreen = ancientScreen;
	}
	
	@Override
	public void clicked(InputEvent event, float x, float y){
		GuiScreen.getInstance().setScreen(new GuiLoading(ancientScreen));
	}
}