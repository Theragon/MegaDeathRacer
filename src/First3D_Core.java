import java.nio.FloatBuffer;

import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;


public class First3D_Core implements ApplicationListener, InputProcessor
{
	GL11 gl11;
	//Camera cam;
	private boolean ligthBulbState = true;
	private boolean wiggleLights = false;
	private float wiggleValue = 0f;
	private float count = 0;

	LightCycle lightCycle1;
	LightCycle lightCycle2;
	final int WORLDSIZE = 20;   // 20x20 grid
	float startPositionX1 = -2.0f;
	float startPositionZ1 = 1.0f;

	final int FORWARD = Input.Keys.W;
	final int BACK = Input.Keys.S;
	final int RIGHT1 = Input.Keys.D;
	final int LEFT1 = Input.Keys.A;
	final int RIGHT2 = Input.Keys.RIGHT;
	final int LEFT2 = Input.Keys.LEFT;

	final int NORTH = 0;
	final int EAST = 1;
	final int SOUTH = 2;
	final int WEST = 3;

	private boolean TURNRIGHT = false;

	@Override
	public void create()
	{
		Gdx.input.setInputProcessor(this);
		
		Gdx.gl11.glEnable(GL11.GL_LIGHTING);
		
		Gdx.gl11.glEnable(GL11.GL_LIGHT1);
		Gdx.gl11.glEnable(GL11.GL_DEPTH_TEST);
		
		Gdx.gl11.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		Gdx.gl11.glMatrixMode(GL11.GL_PROJECTION);
		Gdx.gl11.glLoadIdentity();
		Gdx.glu.gluPerspective(Gdx.gl11, 90, 1.333333f, 1.0f, 30.0f);

		Gdx.gl11.glEnableClientState(GL11.GL_VERTEX_ARRAY);

		FloatBuffer vertexBuffer = BufferUtils.newFloatBuffer(72);
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

		Gdx.gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, vertexBuffer);
		//cam = new Camera(new Point3D(0, 3.5f, 0), new Point3D(19.0f, 0.0f, 1.0f), new Vector3D(0.0f, 1.0f, 0.0f));

		gl11 = Gdx.gl11;
		lightCycle1 = new LightCycle(1.0f, 2.0f, 1.0f, NORTH);
		lightCycle2 = new LightCycle(18.0f, 2.0f, 18.0f, SOUTH);
//		drawFloor(20);
//		lightCycle1.draw(startPositionX1, startPositionZ1);
//		lightCycle1.draw();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
/*
	private void movePlayer(int direction)
	{

		//cam.slide(0.0f, 0.0f, -10.0f * deltaTime);
		if(direction == FORWARD)
		{
			if(lightCycle1.direction == NORTH)
			{
//				position1.x = cam.eye.x+5;
//				position1.z = cam.eye.z;
			}
			else if(lightCycle1.direction == EAST)
			{
			}
		}
		if(direction == BACK)
		{
//			position1.x = cam.eye.x+3;
//			position1.z = cam.eye.z;
		}
		if(direction == LEFT)
		{
			if(lightCycle1.direction == NORTH)
				lightCycle1.direction = WEST;
			else
				lightCycle1.direction -= 1;
//			cam.eye.x = position1.x;
//			cam.eye.z = position1.z+3;
//			cam.yaw(-90.0f);
		}
		if(direction == RIGHT)
		{
			if(lightCycle1.direction == WEST)
				lightCycle1.direction = NORTH;
			else
				lightCycle1.direction += 1;
//			cam.eye.x = position1.x;
//			cam.eye.z = position1.z-3;
//			cam.yaw(90.0f);
		}
	}
*/
	private void update() {
		
		if(this.wiggleLights)
		{
			count += 0.03;
			this.wiggleValue = (float) Math.sin(count) * 10;
		}
		
		if(this.ligthBulbState)
			Gdx.gl11.glEnable(GL11.GL_LIGHT0);
		else
			Gdx.gl11.glDisable(GL11.GL_LIGHT0);
		
		float deltaTime = Gdx.graphics.getDeltaTime();

		switch (lightCycle1.direction)
		{
			case NORTH:
				lightCycle1.position.x += deltaTime*1;
				break;
			case EAST:
				lightCycle1.position.z += deltaTime*1;
				break;
			case SOUTH:
				lightCycle1.position.x -= deltaTime*1;
				break;
			case WEST:
				lightCycle1.position.z -= deltaTime*1;
				break;
		}
		switch (lightCycle2.direction)
		{
			case NORTH:
				lightCycle2.position.x += deltaTime*1;
				break;
			case EAST:
				lightCycle2.position.z += deltaTime*1;
				break;
			case SOUTH:
				lightCycle2.position.x -= deltaTime*1;
				break;
			case WEST:
				lightCycle2.position.z -= deltaTime*1;
				break;
		}

//		if(Gdx.input.isKeyPressed(Input.Keys.UP))
//			cam.pitch(-90.0f * deltaTime);
		
//		if(Gdx.input.isKeyPressed(Input.Keys.DOWN))
//			cam.pitch(90.0f * deltaTime);
		
//		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
//			cam.yaw(-90.0f * deltaTime);
		
//		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
//			cam.yaw(90.0f * deltaTime);
		
/*		if(Gdx.input.isKeyPressed(Input.Keys.W))
		{
			movePlayer(FORWARD);
//			cam.slide(0.0f, 0.0f, -10.0f * deltaTime);
		//	playerPositionX1 = cam.eye.x+2.0f;
		//	playerPositionZ1 = cam.eye.z;
		}
*/
/*
		if(Gdx.input.isKeyPressed(Input.Keys.S))
		{
			movePlayer(BACK);
//			cam.slide(0.0f, 0.0f, 10.0f * deltaTime);
			//playerPositionX1 = cam.eye.x+2.0f;
			//playerPositionZ1 = cam.eye.z;
		}
*/
/*
		if(Gdx.input.isKeyPressed(Input.Keys.A))
		{
			//cam.slide(-10.0f * deltaTime, 0.0f, 0.0f);
			movePlayer(LEFT);
		}

		if(Gdx.input.isKeyPressed(Input.Keys.D))
		{
			//cam.slide(10.0f * deltaTime, 0.0f, 0.0f);
			movePlayer(RIGHT);
		}
*/
		if(Gdx.input.isKeyPressed(Input.Keys.R))
//			cam.slide(0.0f, 10.0f * deltaTime, 0.0f);
		
		if(Gdx.input.isKeyPressed(Input.Keys.F)){}
//			cam.slide(0.0f, -10.0f * deltaTime, 0.0f);
	}
	
	private void drawBox() {
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
	}
	
	private void drawFloor() {
		for(float fx = 0.0f; fx < WORLDSIZE; fx += 1.0) {
			for(float fz = 0.0f; fz < WORLDSIZE; fz += 1.0) {
				Gdx.gl11.glPushMatrix();
				Gdx.gl11.glTranslatef(fx, 1.0f, fz);
				Gdx.gl11.glScalef(1.0f, 1.0f, 1.0f);
				drawBox();
				Gdx.gl11.glPopMatrix();
			}
		}
	}
	
	private void display()
	{
		Gdx.gl11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);

	//Lights
		// Configure light 0
		float[] lightDiffuse = {1.0f, 1.0f, 1.0f, 1.0f};
		Gdx.gl11.glLightfv(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, lightDiffuse, 0);

		float[] lightPosition = {this.wiggleValue, 10.0f, 15.0f, 1.0f};
		Gdx.gl11.glLightfv(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPosition, 0);

		// Configure light 1
		float[] lightDiffuse1 = {0.5f, 0.5f, 0.5f, 1.0f};
		Gdx.gl11.glLightfv(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, lightDiffuse1, 0);

		float[] lightPosition1 = {-5.0f, -10.0f, -15.0f, 1.0f};
		Gdx.gl11.glLightfv(GL11.GL_LIGHT1, GL11.GL_POSITION, lightPosition1, 0);

		// Set material on the cube.
		float[] materialDiffuse = {0.2f, .3f, 0.6f, 1.0f};
		Gdx.gl11.glMaterialfv(GL11.GL_FRONT, GL11.GL_DIFFUSE, materialDiffuse, 0);

	//Draw scene 1
		Gdx.gl11.glViewport(0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
		Gdx.gl11.glMatrixMode(GL11.GL_MODELVIEW);
		Gdx.gl11.glLoadIdentity();
		switch (lightCycle1.direction)
		{
			case NORTH:
				Gdx.glu.gluLookAt(Gdx.gl11, lightCycle1.position.x-2.5f, 3.0f, lightCycle1.position.z, lightCycle1.position.x+2, 0.0f, lightCycle1.position.z, 0.0f, 1.0f, 0.0f);
				break;
			case EAST:
				Gdx.glu.gluLookAt(Gdx.gl11, lightCycle1.position.x, 3.0f, lightCycle1.position.z-2.5f, lightCycle1.position.x, 0.0f, lightCycle1.position.z+2, 0.0f, 1.0f, 0.0f);
				break;
			case SOUTH:
				Gdx.glu.gluLookAt(Gdx.gl11, lightCycle1.position.x+2.5f, 3.0f, lightCycle1.position.z, lightCycle1.position.x-2, 0.0f, lightCycle1.position.z, 0.0f, 1.0f, 0.0f);
				break;
			case WEST:
				Gdx.glu.gluLookAt(Gdx.gl11, lightCycle1.position.x, 3.0f, lightCycle1.position.z+2.5f, lightCycle1.position.x, 0.0f, lightCycle1.position.z-2, 0.0f, 1.0f, 0.0f);
				break;
			default:
				Gdx.glu.gluLookAt(Gdx.gl11, lightCycle1.position.x, 3.0f, lightCycle1.position.z-2.5f, lightCycle1.position.x, 0.0f, lightCycle1.position.z, 0.0f, 1.0f, 0.0f);
				break;
		}
		// Draw floor!
		drawFloor();
		lightCycle1.draw();
		lightCycle2.draw();

	//Draw scene 2
		Gdx.gl11.glViewport(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight());
		Gdx.gl11.glMatrixMode(GL11.GL_MODELVIEW);
		Gdx.gl11.glLoadIdentity();
		switch (lightCycle2.direction)
		{
			case NORTH:
				Gdx.glu.gluLookAt(Gdx.gl11, lightCycle2.position.x-2.5f, 3.0f, lightCycle2.position.z, lightCycle2.position.x+2, 0.0f, lightCycle2.position.z, 0.0f, 1.0f, 0.0f);
				break;
			case EAST:
				Gdx.glu.gluLookAt(Gdx.gl11, lightCycle2.position.x, 3.0f, lightCycle2.position.z-2.5f, lightCycle2.position.x, 0.0f, lightCycle2.position.z+2, 0.0f, 1.0f, 0.0f);
				break;
			case SOUTH:
				Gdx.glu.gluLookAt(Gdx.gl11, lightCycle2.position.x+2.5f, 3.0f, lightCycle2.position.z, lightCycle2.position.x-2, 0.0f, lightCycle2.position.z, 0.0f, 1.0f, 0.0f);
				break;
			case WEST:
				Gdx.glu.gluLookAt(Gdx.gl11, lightCycle2.position.x, 3.0f, lightCycle2.position.z+2.5f, lightCycle2.position.x, 0.0f, lightCycle2.position.z-2, 0.0f, 1.0f, 0.0f);
				break;
			default:
				Gdx.glu.gluLookAt(Gdx.gl11, lightCycle2.position.x, 3.0f, lightCycle2.position.z-2.5f, lightCycle2.position.x, 0.0f, lightCycle2.position.z, 0.0f, 1.0f, 0.0f);
				break;
		}
//		Gdx.glu.gluLookAt(Gdx.gl11, lightCycle2.position.x+2.5f, 3.0f, lightCycle2.position.z, lightCycle2.position.x-2, 0.0f, lightCycle2.position.z, 0.0f, 1.0f, 0.0f);

		drawFloor();
		lightCycle1.draw();
		lightCycle2.draw();
	}

	@Override
	public void render()
	{
		update();
		display();
	}

	@Override
	public void resize(int arg0, int arg1) {
	}

	@Override
	public void resume() {
	}

	@Override
	public boolean keyDown(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0)
	{
		switch (arg0)
		{
			case Input.Keys.D:
				System.out.println("P1 TURN RIGHT");
				lightCycle1.movePlayer(RIGHT1);
				break;
			case Input.Keys.A:
				System.out.println("P1 TURN LEFT");
				lightCycle1.movePlayer(LEFT1);
				break;
			case Input.Keys.RIGHT:
				System.out.println("P2 TURN RIGHT");
				lightCycle2.movePlayer(RIGHT2);
				break;
			case Input.Keys.LEFT:
				System.out.println("P2 TURN LEFT");
				lightCycle2.movePlayer(LEFT2);
				break;
			case Input.Keys.L:
				this.ligthBulbState = this.ligthBulbState ? false:true;
				break;
			case Input.Keys.O:
				this.wiggleLights = this.wiggleLights ? false:true;
				break;
			default:
				System.out.println("default case");
				break;
		}
		/*if(arg0 == Input.Keys.L){
			this.ligthBulbState = this.ligthBulbState ? false:true;
		}
		if(arg0 == Input.Keys.O){
			this.wiggleLights = this.wiggleLights ? false:true;
		}*/
		return false;
	}

	@Override
	public boolean mouseMoved(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		return false;
	}
}
