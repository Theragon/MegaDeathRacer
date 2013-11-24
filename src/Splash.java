import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created with IntelliJ IDEA.
 * User: logan
 * Date: 11/23/13
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class Splash implements Screen
{
	private SpriteBatch spriteBatch;
	private Texture splsh;
	private Game myGame;

	public Splash(Game g)
	{
		myGame = g;
	}

	@Override
	public void render(float v) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		spriteBatch.begin();
		spriteBatch.draw(splsh, 0, 0);
		spriteBatch.end();
/*
		if(Gdx.input.justTouched())
			myGame.setScreen(new First3D_Core());
*/
	}

	@Override
	public void resize(int i, int i2) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void show()
	{
		spriteBatch = new SpriteBatch();
		splsh = new Texture(Gdx.files.internal("splash.gif"));
	}

	@Override
	public void hide() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void pause() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void resume() {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	@Override
	public void dispose() {
		//To change body of implemented methods use File | Settings | File Templates.
	}
}
