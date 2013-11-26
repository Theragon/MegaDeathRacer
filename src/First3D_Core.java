import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL11;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

	LightCycle cycle1;
	LightCycle cycle2;
	static final int WORLDSIZE = 40;               // 20x20 grid
	static final byte SPEED = 5;
	private byte state = 1;

//	static final int FORWARD = Input.Keys.W;
//	static final int BACK = Input.Keys.S;
	static final int RIGHT1 = Input.Keys.D;
	static final int LEFT1 = Input.Keys.A;
	static final int RIGHT2 = Input.Keys.RIGHT;
	static final int LEFT2 = Input.Keys.LEFT;

	static final byte NORTH = 0;
	static final byte EAST = 1;
	static final byte SOUTH = 2;
	static final byte WEST = 3;

	static final byte PAUSE = 0;
	static final byte RUNNING = 1;


    ArrayList<Trail> walls = new ArrayList<Trail>();

	ArrayList<Trail> trails1 = new ArrayList<Trail>();
	int trailCount1 = -1;

	ArrayList<Trail> trails2 = new ArrayList<Trail>();
	int trailCount2 = -1;

	FloatBuffer vertexBuffer;

	Music music;

	private SpriteBatch spriteBatch;
	private BitmapFont font;

	private FPSLogger fpsLogger;

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

		Gdx.gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, vertexBuffer);

        Trail westWall = new Trail(WORLDSIZE/2, 5.0f, 0.0f, WEST);
        Trail eastWall = new Trail(WORLDSIZE/2, 5.0f, WORLDSIZE, EAST);
        Trail northWall = new Trail(WORLDSIZE, 5.0f, WORLDSIZE/2, NORTH);
        Trail southWall = new Trail(0.0f, 5.0f, WORLDSIZE/2, SOUTH);

        walls.add(westWall);
        walls.add(eastWall);
        walls.add(northWall);
        walls.add(southWall);

		gl11 = Gdx.gl11;
		cycle1 = new LightCycle(1.0f, 2.0f, 1.0f, NORTH);
		cycle1.startNorth = true;
		cycle2 = new LightCycle(38.0f, 2.0f, 38.0f, SOUTH);
		cycle2.startSouth = true;

		music = Gdx.audio.newMusic(Gdx.files.internal("assets/music/EndOfLine.mp3"));
		music.setLooping(true);
		music.play();

		fpsLogger = new FPSLogger();
		spriteBatch = new SpriteBatch();
		font = new BitmapFont();
	}

	@Override
	public void dispose()
	{
		music.dispose();
		System.out.println("DISPOSE");
		// TODO Auto-generated method stub
	}

	@Override
	public void pause()
	{
		// TODO Auto-generated method stub
	}

	private void updatePause()
	{
		//TODO: draw pause screen
	}

	private void updateRunning()
	{
		if(this.wiggleLights)
		{
			count += 0.03;
			this.wiggleValue = (float) Math.sin(count) * 10;
		}

		if(this.ligthBulbState) Gdx.gl11.glEnable(GL11.GL_LIGHT0);
		else Gdx.gl11.glDisable(GL11.GL_LIGHT0);

		float deltaTime = Gdx.graphics.getDeltaTime();

		switch(cycle1.direction)                            // check what direction cycle 1 is facing
		{
			case NORTH:
				if(cycle1.startNorth)
				{
					trails1.add(new Trail(cycle1.pos.x, cycle1.pos.y, cycle1.pos.z, NORTH));
					trailCount1++;
					trails1.get(trailCount1).startx = cycle1.pos.x;
					trails1.get(trailCount1).startz = cycle1.pos.z;
					cycle1.startNorth = false;
                    if(trailCount1 > 0)
                    {
                        trails1.get(trailCount1-1).endx = trails1.get(trailCount1).startx;
                        trails1.get(trailCount1-1).endz = trails1.get(trailCount1).startz;
                    }
				}
				else
				{
					updateTrail(trails1.get(trailCount1), cycle1);
				}
				cycle1.pos.x += deltaTime*SPEED;            // move forwards on the x-axis
				break;
			case EAST:
				if(cycle1.startEast)
				{
					trails1.add(new Trail(cycle1.pos.x, cycle1.pos.y, cycle1.pos.z, EAST));
					trailCount1++;
					trails1.get(trailCount1).startx = cycle1.pos.x;
					trails1.get(trailCount1).startz = cycle1.pos.z;
					cycle1.startEast = false;
                    if(trailCount1 > 0)
                    {
                        trails1.get(trailCount1-1).endx = trails1.get(trailCount1).startx;
                        trails1.get(trailCount1-1).endz = trails1.get(trailCount1).startz;
                    }
				}
				else
				{
					updateTrail(trails1.get(trailCount1), cycle1);
				}
				cycle1.pos.z += deltaTime*SPEED;            // move forwards on the z-axis
				break;
			case SOUTH:
				if(cycle1.startSouth)
				{
					trails1.add(new Trail(cycle1.pos.x, cycle1.pos.y, cycle1.pos.z, SOUTH));
					trailCount1++;
					trails1.get(trailCount1).startx = cycle1.pos.x;
					trails1.get(trailCount1).startz = cycle1.pos.z;
					cycle1.startSouth = false;
					if(trailCount1 > 0)
					{
						trails1.get(trailCount1-1).endx = trails1.get(trailCount1).startx;
						trails1.get(trailCount1-1).endz = trails1.get(trailCount1).startz;
					}
				}
				else
				{
					updateTrail(trails1.get(trailCount1), cycle1);
				}
				cycle1.pos.x -= deltaTime*SPEED;            // move backwards on the x-axis
				break;
			case WEST:
				if(cycle1.startWest)
				{
					trails1.add(new Trail(cycle1.pos.x, cycle1.pos.y, cycle1.pos.z, WEST));
					trailCount1++;
					trails1.get(trailCount1).startx = cycle1.pos.x;
					trails1.get(trailCount1).startz = cycle1.pos.z;
					cycle1.startWest = false;
					if(trailCount1 > 0)
					{
						trails1.get(trailCount1-1).endx = trails1.get(trailCount1).startx;
						trails1.get(trailCount1-1).endz = trails1.get(trailCount1).startz;
					}
				}
				else
				{
					updateTrail(trails1.get(trailCount1), cycle1);
				}
				cycle1.pos.z -= deltaTime*SPEED;            // move backwards on the z-axis
				break;
		}
		cycle1.updatePosition();

		switch(cycle2.direction)                            // check what direction cycle 2 is facing
		{
			case NORTH:
				if(cycle2.startNorth)
				{
					trails2.add(new Trail(cycle2.pos.x, cycle2.pos.y, cycle2.pos.z, NORTH));
					trailCount2++;
					trails2.get(trailCount2).startx = cycle2.pos.x;
					trails2.get(trailCount2).startz = cycle2.pos.z;
					cycle2.startNorth = false;
					if(trailCount2 > 0)
					{
						trails2.get(trailCount2-1).endx = trails2.get(trailCount2).startx;
						trails2.get(trailCount2-1).endz = trails2.get(trailCount2).startz;
					}
				}
				else
				{
					updateTrail(trails2.get(trailCount2), cycle2);
				}
				cycle2.pos.x += deltaTime*SPEED;            // move forwards on the x-axis
				break;
			case EAST:
				if(cycle2.startEast)
				{
					trails2.add(new Trail(cycle2.pos.x, cycle2.pos.y, cycle2.pos.z, EAST));
					trailCount2++;
					trails2.get(trailCount2).startx = cycle2.pos.x;
					trails2.get(trailCount2).startz = cycle2.pos.z;
					cycle2.startEast = false;
					if(trailCount2 > 0)
					{
						trails2.get(trailCount2-1).endx = trails2.get(trailCount2).startx;
						trails2.get(trailCount2-1).endz = trails2.get(trailCount2).startz;
					}
				}
				else
				{
					updateTrail(trails2.get(trailCount2), cycle2);
				}
				cycle2.pos.z += deltaTime*SPEED;            // move forwards on the z-axis
				break;
			case SOUTH:
				if(cycle2.startSouth)
				{
					trails2.add(new Trail(cycle2.pos.x, cycle2.pos.y, cycle2.pos.z, SOUTH));
					trailCount2++;
					trails2.get(trailCount2).startx = cycle2.pos.x;
					trails2.get(trailCount2).startz = cycle2.pos.z;
					cycle2.startSouth = false;
					if(trailCount2 > 0)
					{
						trails2.get(trailCount2-1).endx = trails2.get(trailCount2).startx;
						trails2.get(trailCount2-1).endz = trails2.get(trailCount2).startz;
					}
				}
				else
				{
					updateTrail(trails2.get(trailCount2), cycle2);
				}
				cycle2.pos.x -= deltaTime*SPEED;            // move backwards on the x-axis
				break;
			case WEST:
				if(cycle2.startWest)
				{
					trails2.add(new Trail(cycle2.pos.x, cycle2.pos.y, cycle2.pos.z, WEST));
					trailCount2++;
					trails2.get(trailCount2).startx = cycle2.pos.x;
					trails2.get(trailCount2).startz = cycle2.pos.z;
					cycle2.startWest = false;
					if(trailCount2 > 0)
					{
						trails2.get(trailCount2-1).endx = trails2.get(trailCount2).startx;
						trails2.get(trailCount2-1).endz = trails2.get(trailCount2).startz;
					}
				}
				else
				{
					updateTrail(trails2.get(trailCount2), cycle2);
				}
				cycle2.pos.z -= deltaTime*SPEED;            // move backwards on the z-axis
				break;
		}
		cycle2.updatePosition();

		collisionDetection(cycle1);
		collisionDetection(cycle2);
	}

	public void getPosition()
	{
		System.out.println("Cycle1: " + (Math.ceil(cycle1.pos.x)+1) + "," + Math.ceil(cycle1.pos.z));
		System.out.println("Cycle2: " + (Math.ceil(cycle2.pos.x)-1) + "," + Math.ceil(cycle2.pos.z));
	}

	public void collisionDetection(LightCycle cycle)
	{
		switch(cycle.direction)
		{
			case NORTH:
				for(Trail trail : trails1)
				{
					if(trail.startz < cycle.pos.z && trail.endz > cycle.pos.z)
					{
						if(Math.ceil(cycle.pos.x)+1 == Math.round(trail.x))
						{
							System.out.println("TRAIL1 COLLISION NORTH");
							state = PAUSE;
						}
					}
					if(trail.endz < cycle.pos.z && trail.startz > cycle.pos.z)
					{
						if(Math.ceil(cycle.pos.x)+1 == Math.round(trail.x))
						{
							System.out.println("TRAIL1 COLLISION NORTH");
						}
					}
				}
				for(Trail trail : trails2)
				{
					if(trail.startz < cycle.pos.z && trail.endz > cycle.pos.z)
					{
						if(Math.ceil(cycle.pos.x)+1 == Math.round(trail.x))
						{
							System.out.println("TRAIL2 COLLISION NORTH");
							state = PAUSE;
						}
					}
					if(trail.endz < cycle.pos.z && trail.startz > cycle.pos.z)
					{
						if(Math.ceil(cycle.pos.x)+1 == Math.round(trail.x))
						{
							System.out.println("TRAIL2 COLLISION NORTH");
							state = PAUSE;
						}
					}
				}
				break;
			case EAST:
				for(Trail trail : trails1)
				{
					if(trail.startx < cycle.pos.x && trail.endx > cycle.pos.x)
					{
						if(Math.ceil(cycle.pos.z)+1 == Math.round(trail.z))
						{
							System.out.println("TRAIL1 COLLISION EAST");
							state = PAUSE;
						}
					}
					if(trail.endx < cycle.pos.x && trail.startx > cycle.pos.x)
					{
						if(Math.ceil(cycle.pos.z)+1 == Math.round(trail.z))
						{
							System.out.println("TRAIL1 COLLISION EAST");
							state = PAUSE;
						}
					}
				}
				for(Trail trail : trails2)
				{
					if(trail.startx < cycle.pos.x && trail.endx > cycle.pos.x)
					{
						if(Math.ceil(cycle.pos.z)+1 == Math.round(trail.z))
						{
							System.out.println("TRAIL2 COLLISION EAST");
							state = PAUSE;
						}
					}
					if(trail.endx < cycle.pos.x && trail.startx > cycle.pos.x)
					{
						if(Math.ceil(cycle.pos.z)+1 == Math.round(trail.z))
						{
							System.out.println("TRAIL2 COLLISION EAST");
							state = PAUSE;
						}
					}
				}
				break;

			case SOUTH:
				for(Trail trail : trails1)
				{
					if(trail.startz < cycle.pos.z && trail.endz > cycle.pos.z)
					{
						if(Math.ceil(cycle.pos.x)-1 == Math.round(trail.x))
						{
							System.out.println("TRAIL1 COLLISION SOUTH");
							state = PAUSE;
						}
					}
				}
				for(Trail trail : trails2)
				{
					if(trail.startz < cycle.pos.z && trail.endz > cycle.pos.z)
					{
						if(Math.ceil(cycle.pos.x)-1 == Math.round(trail.x))
						{
							System.out.println("TRAIL2 COLLISION SOUTH");
							state = PAUSE;
						}
					}
					if(trail.endz < cycle.pos.z && trail.startz > cycle.pos.z)
					{
						if(Math.ceil(cycle.pos.x)-1 == Math.round(trail.x))
						{
							System.out.println("TRAIL2 COLLISION SOUTH");
							state = PAUSE;
						}
					}
				}
				break;

			case WEST:
				for(Trail trail : trails1)
				{
					if(trail.startx < cycle.pos.x && trail.endx > cycle.pos.x)
					{
						if(Math.ceil(cycle.pos.z)-1 == Math.round(trail.z))
						{
							System.out.println("TRAIL1 COLLISION WEST");
							state = PAUSE;
						}
					}
					if(trail.endx < cycle.pos.x && trail.startx > cycle.pos.x)
					{
						if(Math.ceil(cycle.pos.z)-1 == Math.round(trail.z))
						{
							System.out.println("TRAIL1 COLLISION WEST");
							state = PAUSE;
						}
					}
				}
				for(Trail trail : trails2)
				{
					if(trail.startx < cycle.pos.x && trail.endx > cycle.pos.x)
					{
						if(Math.ceil(cycle.pos.z)-1 == Math.round(trail.z))
						{
							System.out.println("TRAIL2 COLLISION WEST");
							state = PAUSE;
						}
					}
					if(trail.endx < cycle.pos.x && trail.startx > cycle.pos.x)
					{
						if(Math.ceil(cycle.pos.z)-1 == Math.round(trail.z))
						{
							System.out.println("TRAIL2 COLLISION WEST");
							state = PAUSE;
						}
					}
				}
				break;
		}

		if(Math.ceil(cycle1.pos.x)+1 == Math.ceil(cycle2.pos.x)-1)        // Same X position
		{
			if(Math.ceil(cycle2.pos.z) == Math.ceil(cycle1.pos.z))    // Same Z position
			{
//				if(Math.floor(cycle1.front) == Math.floor(cycle2.front))    // Head on collision
				if(cycle1.direction == NORTH && cycle2.direction == SOUTH)
				{
					System.out.println("COLLISION");
					state = PAUSE;
				}
			}
		}
	}

	private void update()
	{
		switch(state)
		{
			case RUNNING:
				updateRunning();
				break;
			case PAUSE:
				updatePause();
				break;
			default:
				break;
		}
	}
	
	private void drawBox()
	{
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

    private void drawWalls()
    {

        for(Trail wall : walls)
        {
            switch(wall.direction)
            {
                case NORTH:
                    Gdx.gl11.glPushMatrix();
                    Gdx.gl11.glTranslatef(wall.x-0.5f, 2.0f, wall.z);
                    Gdx.gl11.glScalef(0.5f, 5.0f, wall.x);
                    drawBox();
                    Gdx.gl11.glPopMatrix();
                    break;
                case SOUTH:
                    Gdx.gl11.glPushMatrix();
                    Gdx.gl11.glTranslatef(wall.x - 0.5f, 2.0f, wall.z);
                    Gdx.gl11.glScalef(0.5f, 5.0f, wall.z*2);
                    drawBox();
                    Gdx.gl11.glPopMatrix();
                    break;
                case WEST:
                    Gdx.gl11.glPushMatrix();
                    Gdx.gl11.glTranslatef(wall.x, 2.0f, wall.z - 0.5f);
                    Gdx.gl11.glScalef(wall.x*2, 5.0f, 0.5f);
                    drawBox();
                    Gdx.gl11.glPopMatrix();
                    break;
                case EAST:
                    Gdx.gl11.glPushMatrix();
                    Gdx.gl11.glTranslatef(wall.x, 2.0f, wall.z);
                    Gdx.gl11.glScalef(wall.x*2, 5.0f, 0.5f);
                    drawBox();
                    Gdx.gl11.glPopMatrix();
                    break;

            }
        }
        /*Gdx.gl11.glPushMatrix();
        Gdx.gl11.glTranslatef(((trail.startx+trail.endx)/2), 2.0f, trail.z);
        Gdx.gl11.glScalef(trail.endx-trail.startx, 1.0f, 0.1f);
        //Gdx.gl11.glRotatef(angle, 0.0f, 1.0f, 0.0f);
        drawBox();
        Gdx.gl11.glPopMatrix();
        */
    }
	
	private void drawFloor()
	{
		for(float fx = 0.0f; fx < WORLDSIZE; fx += 1.0)
		{
			for(float fz = 0.0f; fz < WORLDSIZE; fz += 1.0)
			{
				Gdx.gl11.glPushMatrix();
				Gdx.gl11.glTranslatef(fx, 1.0f, fz);
				Gdx.gl11.glScalef(0.97f, 1.0f, 0.97f);
				drawBox();
				Gdx.gl11.glPopMatrix();
			}
		}
	}
	
	private void display()
	{
		Gdx.gl11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
/*
		if(state == PAUSE)
		{
			String beginMsg = "Press spacebar to Pong!";
			this.spriteBatch.begin();
			font.setColor(1f, 1f, 1f, 1f);
			font.draw(spriteBatch,beginMsg,230, 300);
			this.spriteBatch.end();
		}
		Gdx.gl11.glVertexPointer(3, GL11.GL_FLOAT, 0, vertexBuffer);
*/
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
//		Gdx.gl11.glViewport(0, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight));                                // Vertical split
		Gdx.gl11.glViewport(0, Gdx.graphics.getHeight() / 2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);    // Horizontal split
		Gdx.gl11.glMatrixMode(GL11.GL_MODELVIEW);
		Gdx.gl11.glLoadIdentity();
		switch (cycle1.direction)
		{
			case NORTH:
				Gdx.glu.gluLookAt(Gdx.gl11, cycle1.pos.x-2.5f, 3.0f, cycle1.pos.z, cycle1.pos.x+2, 0.0f, cycle1.pos.z, 0.0f, 1.0f, 0.0f);
				break;
			case EAST:
				Gdx.glu.gluLookAt(Gdx.gl11, cycle1.pos.x, 3.0f, cycle1.pos.z-2.5f, cycle1.pos.x, 0.0f, cycle1.pos.z+2, 0.0f, 1.0f, 0.0f);
				break;
			case SOUTH:
				Gdx.glu.gluLookAt(Gdx.gl11, cycle1.pos.x+2.5f, 3.0f, cycle1.pos.z, cycle1.pos.x-2, 0.0f, cycle1.pos.z, 0.0f, 1.0f, 0.0f);
				break;
			case WEST:
				Gdx.glu.gluLookAt(Gdx.gl11, cycle1.pos.x, 3.0f, cycle1.pos.z+2.5f, cycle1.pos.x, 0.0f, cycle1.pos.z-2, 0.0f, 1.0f, 0.0f);
				break;
			default:
				Gdx.glu.gluLookAt(Gdx.gl11, cycle1.pos.x, 3.0f, cycle1.pos.z-2.5f, cycle1.pos.x, 0.0f, cycle1.pos.z, 0.0f, 1.0f, 0.0f);
				break;
		}
        //Draw walls
        drawWalls();
	    // Draw floor!
        drawFloor();
        // Set material on player 1.
        float[] materialDiffuse2 = {1.0f,0.0f,0.0f,1.0f};
        Gdx.gl11.glMaterialfv(GL11.GL_FRONT, GL11.GL_DIFFUSE, materialDiffuse2, 0);
        cycle1.draw();
        drawTrail(trails1);
        // Set material on player 2.
        float[] materialDiffuse3 = {1.0f,1.0f,1.0f,1.0f};
        Gdx.gl11.glMaterialfv(GL11.GL_FRONT, GL11.GL_DIFFUSE, materialDiffuse3, 0);
        cycle2.draw();
        drawTrail(trails2);

	//Lights
		// Configure light 0
		float[] lightDiffuse2 = {1.0f, 1.0f, 1.0f, 1.0f};
		Gdx.gl11.glLightfv(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, lightDiffuse2, 0);

		float[] lightPosition2 = {this.wiggleValue, 10.0f, 15.0f, 1.0f};
		Gdx.gl11.glLightfv(GL11.GL_LIGHT0, GL11.GL_POSITION, lightPosition2, 0);

		// Configure light 1
		float[] lightDiffuse12 = {0.5f, 0.5f, 0.5f, 1.0f};
		Gdx.gl11.glLightfv(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, lightDiffuse12, 0);

		float[] lightPosition12 = {-5.0f, -10.0f, -15.0f, 1.0f};
		Gdx.gl11.glLightfv(GL11.GL_LIGHT1, GL11.GL_POSITION, lightPosition12, 0);



	//Draw scene 2
//		Gdx.gl11.glViewport(Gdx.graphics.getWidth() / 2, 0, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight()); // Vertical
		Gdx.gl11.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);                           // Horizontal
		Gdx.gl11.glMatrixMode(GL11.GL_MODELVIEW);
		Gdx.gl11.glLoadIdentity();
		switch (cycle2.direction)
		{
			case NORTH:
				Gdx.glu.gluLookAt(Gdx.gl11, cycle2.pos.x-2.5f, 3.0f, cycle2.pos.z, cycle2.pos.x+2, 0.0f, cycle2.pos.z, 0.0f, 1.0f, 0.0f);
				break;
			case EAST:
				Gdx.glu.gluLookAt(Gdx.gl11, cycle2.pos.x, 3.0f, cycle2.pos.z-2.5f, cycle2.pos.x, 0.0f, cycle2.pos.z+2, 0.0f, 1.0f, 0.0f);
				break;
			case SOUTH:
				Gdx.glu.gluLookAt(Gdx.gl11, cycle2.pos.x+2.5f, 3.0f, cycle2.pos.z, cycle2.pos.x-2, 0.0f, cycle2.pos.z, 0.0f, 1.0f, 0.0f);
				break;
			case WEST:
				Gdx.glu.gluLookAt(Gdx.gl11, cycle2.pos.x, 3.0f, cycle2.pos.z+2.5f, cycle2.pos.x, 0.0f, cycle2.pos.z-2, 0.0f, 1.0f, 0.0f);
				break;
			default:
				Gdx.glu.gluLookAt(Gdx.gl11, cycle2.pos.x, 3.0f, cycle2.pos.z-2.5f, cycle2.pos.x, 0.0f, cycle2.pos.z, 0.0f, 1.0f, 0.0f);
				break;
		}

        Gdx.gl11.glMaterialfv(GL11.GL_FRONT, GL11.GL_DIFFUSE, materialDiffuse, 0);
		drawFloor();
        // Set material on player 1.
        Gdx.gl11.glMaterialfv(GL11.GL_FRONT, GL11.GL_DIFFUSE, materialDiffuse2, 0);
		cycle1.draw();
        drawTrail(trails1);
        // Set material on player 2.
        Gdx.gl11.glMaterialfv(GL11.GL_FRONT, GL11.GL_DIFFUSE, materialDiffuse3, 0);
		cycle2.draw();
		drawTrail(trails2);
	}

	private void updateTrail(Trail trail, LightCycle cycle)
	{
		switch (trail.direction)
		{
			case NORTH:
				trail.end = cycle.pos.x;
				trail.endx = cycle.pos.x;
				trail.endz = cycle.pos.z;
				break;

			case EAST:
				trail.end = cycle.pos.z;
				trail.endx = cycle.pos.x;
				trail.endz = cycle.pos.z;
				break;

			case SOUTH:
				trail.end = cycle.pos.x;
				trail.endx = cycle.pos.x;
				trail.endz = cycle.pos.z;
				break;

			case WEST:
				trail.end = cycle.pos.z;
				trail.endx = cycle.pos.x;
				trail.endz = cycle.pos.z;
				break;
		}
	}

	private void drawTrail(List<Trail> trails)
	{
		for(Trail trail : trails)
		{
			switch (trail.direction)
			{
				case NORTH:
					Gdx.gl11.glPushMatrix();
					Gdx.gl11.glTranslatef(((trail.startx+trail.endx)/2), 2.0f, trail.z);
					Gdx.gl11.glScalef(trail.endx-trail.startx, 1.0f, 0.1f);
					//Gdx.gl11.glRotatef(angle, 0.0f, 1.0f, 0.0f);
					drawBox();
					Gdx.gl11.glPopMatrix();
					break;

				case EAST:
					Gdx.gl11.glPushMatrix();
					Gdx.gl11.glTranslatef(trail.x, 2.0f, ((trail.startz+trail.endz)/2));
					Gdx.gl11.glScalef(0.1f, 1.0f, trail.endz-trail.startz);
					//Gdx.gl11.glRotatef(angle, 0.0f, 1.0f, 0.0f);
					drawBox();
					Gdx.gl11.glPopMatrix();
					break;

				case SOUTH:
					Gdx.gl11.glPushMatrix();
					Gdx.gl11.glTranslatef(((trail.startx+trail.endx)/2), 2.0f, trail.z);
					Gdx.gl11.glScalef(trail.endx-trail.startx, 1.0f, 0.1f);
					//Gdx.gl11.glRotatef(angle, 0.0f, 1.0f, 0.0f);
					drawBox();
					Gdx.gl11.glPopMatrix();
					break;

				case WEST:
					Gdx.gl11.glPushMatrix();
					Gdx.gl11.glTranslatef(trail.x, 2.0f, ((trail.startz+trail.endz)/2));
					Gdx.gl11.glScalef(0.1f, 1.0f, trail.endz-trail.startz);
					//Gdx.gl11.glRotatef(angle, 0.0f, 1.0f, 0.0f);
					drawBox();
					Gdx.gl11.glPopMatrix();
					break;
			}
		}
	}

	@Override
	public void render()
	{
		update();
		display();
		fpsLogger.log();
	}

	@Override
	public void resize(int arg0, int arg1)
	{}

	@Override
	public void resume()
	{}

	@Override
	public boolean keyDown(int arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char arg0)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int arg0)
	{
		switch (arg0)                       // Check what key was released
		{
			case RIGHT1:                    // Player 1 right turn
				cycle1.movePlayer(RIGHT1);
				break;
			case LEFT1:                     // Player 1 left turn
				cycle1.movePlayer(LEFT1);
				break;
			case RIGHT2:                    // Player 2 right turn
				cycle2.movePlayer(RIGHT2);
				break;
			case LEFT2:                     // Player 2 left turn
				cycle2.movePlayer(LEFT2);
				break;
			case Input.Keys.P:              // Pause game
				if(state == PAUSE) state = RUNNING;
				else if(state == RUNNING) state = PAUSE;
				break;
			case Input.Keys.SPACE:
				getPosition();
				break;
			case Input.Keys.L:
				this.ligthBulbState = this.ligthBulbState ? false:true;
				break;
			case Input.Keys.O:
				this.wiggleLights = this.wiggleLights ? false:true;
				break;
			default:
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
