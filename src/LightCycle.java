/**
 * Created with IntelliJ IDEA.
 * User: logan
 * Date: 11/4/13
 * Time: 6:18 PM
 * To change this template use File | Settings | File Templates.
 */
import java.nio.FloatBuffer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.BufferUtils;

public class LightCycle {

	FloatBuffer vertexBuffer;
	//FloatBuffer texCoordBuffer;
	public int direction;
	final int NORTH = 0;
	final int EAST = 1;
	final int SOUTH = 2;
	final int WEST = 3;
	Point3D position;// = new Point3D(1.0f, 2.0f, 1.0f);

	//Texture tex;

	public LightCycle(/*String textureImage*/ float x, float y, float z, int dir)
	{
		position = new Point3D(x, y, z);
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
		this.direction = dir;

		vertexBuffer = BufferUtils.newFloatBuffer(72);
		vertexBuffer.put(new float[] {-0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f,
				0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f,
				0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f,
				0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,
				0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,
				-0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
				-0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f,
				-0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f,
				-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f,
				0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f,
				-0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f,
				0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f});
		vertexBuffer.rewind();

/*		texCoordBuffer = BufferUtils.newFloatBuffer(48);
		texCoordBuffer.put(new float[] {0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f,
				0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f});
		texCoordBuffer.rewind();
*/
//		tex = new Texture(Gdx.files.internal("assets/textures/" + textureImage));

		//this.direction = NORTH;
	}


	public void draw()
	{
		GL10 gl10 = Gdx.gl10;
//		Gdx.gl11.glShadeModel(GL11.GL_SMOOTH);
		Gdx.gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, vertexBuffer);

//		Gdx.gl11.glEnable(GL11.GL_TEXTURE_2D);
//		Gdx.gl11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

//		tex.bind();  //Gdx.gl11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

//		Gdx.gl11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, texCoordBuffer);
		Gdx.gl11.glPushMatrix();
		Gdx.gl11.glTranslatef(position.x, position.y, position.z);
		if(direction == EAST)
			Gdx.gl11.glRotatef(90, 0, 1, 0);
		if(direction == WEST)
			Gdx.gl11.glRotatef(-90, 0, 1, 0);

//		Gdx.gl11.glLoadIdentity();
//		Gdx.glu.gluLookAt(gl10, position.x-3, position.y+3, position.z-3, position.x, position.y, position.z, 0, 1, 0);
		Gdx.gl11.glScalef(2.0f, 0.5f, 0.5f);

		Gdx.gl11.glNormal3f(0.0f, 0.0f, -1.0f);
		Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		Gdx.gl11.glNormal3f(1.0f, 0.0f, 0.0f);
		Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 4, 4);
		Gdx.gl11.glNormal3f(0.0f, 0.0f, 1.0f);
		Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 8, 4);
		Gdx.gl11.glNormal3f(-1.0f, 0.0f, 0.0f);
		Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 12, 4);
		Gdx.gl11.glNormal3f(0.0f, 1.0f, 0.0f);
		Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 16, 4);
		Gdx.gl11.glNormal3f(0.0f, -1.0f, 0.0f);
		Gdx.gl11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 20, 4);

		Gdx.gl11.glPopMatrix();

//		Gdx.gl11.glDisable(GL11.GL_TEXTURE_2D);
//		Gdx.gl11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
	}

	public void movePlayer(int direction)
	{
		switch (direction)
		{
			case Input.Keys.D:
				if(this.direction == WEST)
					this.direction = NORTH;
				else
					this.direction += 1;
				break;
			case Input.Keys.A:
				if(this.direction == NORTH)
					this.direction = WEST;
				else
					this.direction -= 1;
				break;

			case Input.Keys.LEFT:
				if(this.direction == NORTH)
					this.direction = WEST;
				else
					this.direction -= 1;
				break;
			case Input.Keys.RIGHT:
				if(this.direction == WEST)
					this.direction = NORTH;
				else
					this.direction += 1;
				break;
		}
//		if(direction == LEFT)
/*		if(direction == Input.Keys.LEFT)
		{
			if(this.direction == NORTH)
				this.direction = WEST;
			else
				this.direction -= 1;
		}
//		if(direction == RIGHT)
		if(direction == Input.Keys.RIGHT)
		{
			if(this.direction == WEST)
				this.direction = NORTH;
			else
				this.direction += 1;
		}*/
	}
}