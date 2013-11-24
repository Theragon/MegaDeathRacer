import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;

/**
 * Created with IntelliJ IDEA.
 * User: logan
 * Date: 11/23/13
 * Time: 4:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class MegaDeathRacerGame extends Game implements ApplicationListener
{
	@Override
	public void create()
	{
		this.setScreen(new Splash(this));
	}
}
