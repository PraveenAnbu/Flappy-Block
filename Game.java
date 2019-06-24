

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Game implements Runnable {
Display display;
int height;
int width;
String title = "Flappy Block";
Thread thread;
private boolean running = false;
private BufferStrategy bs;
private Graphics g;
Block block;
private KeyManager keyManager;
String score = "0";
int fps;

int barWidth = 50;
int distanceBetweenBars = 65;
int distanceBetweenSets = 105;
int barMidpoint;
ArrayList<TopObstacle> topBars;
ArrayList<BottomObstacle> bottomBars ;
int barCounter=0;
int barVel=-1;
int checkScore = 0;
int temp;
int upblockVel=-2;
int downblockVel = 2;

public Game(int height, int width, int fps) {
	this.height = height;
	this.width = width;
	keyManager = new KeyManager();
	this.fps = fps;
	
}


private void init() {
	display = new Display(this.height, this.width,title);
	display.getFrame().addKeyListener(keyManager);
	block = new Block(12,12, Color.BLACK, this.width/5, this.height/2, 1);
	topBars = new ArrayList<TopObstacle>();
	bottomBars = new ArrayList<BottomObstacle>();
	topBars.add(new TopObstacle(this.width - barWidth, 0, barWidth, (this.height/2) - (distanceBetweenBars/2), Color.BLUE));
	bottomBars.add(new BottomObstacle(this.width - barWidth, (this.height/2) +(distanceBetweenBars/2), barWidth, this.height, Color.BLUE));
	temp = this.height/2;
}

private void update() {
blockMovement();
keyManager();
getNewBar();
barMovement();
checkScore();
}

private void render() {
	bs = display.getCanvas().getBufferStrategy();
	if(bs == null) {
		display.getCanvas().createBufferStrategy(3);
		return;
	}
	g = bs.getDrawGraphics();
	// clear screen
	g.clearRect(0, 0, this.width, this.height);
	//start draw
	g.setColor(block.color);
	g.fillRect(block.xCord, block.yCord, block.width, block.height);
	for(int i = 0; i<topBars.size(); i++) {
		
		g.setColor(topBars.get(i).color);
		g.fillRect(topBars.get(i).xCord, topBars.get(i).yCord, topBars.get(i).width, topBars.get(i).height);
		g.setColor(bottomBars.get(i).color);
		g.fillRect(bottomBars.get(i).xCord, bottomBars.get(i).yCord, bottomBars.get(i).width, bottomBars.get(i).height);
	}
	g.setColor(Color.BLACK);
	g.setFont(new Font("Arial", 1, 30));
	g.drawString(score, this.width/2, this.height/8);
	//stop draw
	bs.show();
	g.dispose();
}

public void run() {
		init();
		
		
		double timePerTick = 1000000000/this.fps;
		double delta = 0;
		long now;
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		
		while(running) {
			now = System.nanoTime();
			delta += ((now - lastTime));
			timer += (now - lastTime);
			lastTime = now;
			
			if(delta>=timePerTick) {
			update();
			render();
			ticks++;
			delta -= timePerTick;
			}
			
			if(timer >= 1000000000) {
				System.out.println("Ticks and frames:" + ticks);
				ticks = 0;
				timer = 0;
			}
		}
		stop();
}

public KeyManager getKeyManager() {
	return keyManager;
}

public synchronized void start() {
	if(running) {
		return;
	}
	running = true;
	thread = new Thread(this);
	thread.start();
}
public synchronized void stop() {
	if(!running) {
		return;
	}
	running = false;
	try {
		thread.join();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

private void keyManager() {
	keyManager.tick();
	if(keyManager.sb) {
		block.yVel = upblockVel;
	}
	else {
		block.yVel = downblockVel;
	}
}

private void blockMovement() {
	block.yCord += block.yVel;
	if(block.yCord <= 0) {
		upblockVel = 0;
	}
	if((block.yCord + block.height) >= this.height) {
		downblockVel = 0;
	}
	if(block.yCord > 0) {
		upblockVel = -2;
	}
	if((block.yCord + block.height) < this.height) {
		downblockVel = 2;
	}
}

private void randomVars() {
	barMidpoint = (int) ((Math.random() * ((this.height - distanceBetweenBars+1)))+(distanceBetweenBars/2));	
	if((Math.abs(barMidpoint - temp) <= ((this.height*1)/3))) {
		temp = barMidpoint;
		}
	else {
		randomVars();
	}
	
}

private void barMovement() {
	for(int i = 0; i<topBars.size(); i++) {
		
		topBars.get(i).xCord += barVel;
		bottomBars.get(i).xCord += barVel;
	}
}

private void getNewBar() {
	if(topBars.get(barCounter).xCord + topBars.get(barCounter).width == this.width - distanceBetweenSets-barWidth) {
		randomVars();
		topBars.add(new TopObstacle(this.width - barWidth, 0, barWidth, barMidpoint - (distanceBetweenBars/2), Color.BLUE));
		bottomBars.add(new BottomObstacle(this.width - barWidth, barMidpoint + (distanceBetweenBars/2), barWidth, this.height, Color.BLUE));
		barCounter++;
		
	}
}
private void checkScore() {
	if(((block.xCord+block.width)>= topBars.get(checkScore).xCord) && block.xCord<= (topBars.get(checkScore).xCord + topBars.get(checkScore).width)) {
		if(((block.yCord>=topBars.get(checkScore).height)&& (block.yCord + block.height)<=bottomBars.get(checkScore).yCord)){
			if(block.xCord>= (topBars.get(checkScore).xCord + topBars.get(checkScore).width)) {
				checkScore++;
				score = checkScore + "";
			}
		
	}
		else {
			JOptionPane.showMessageDialog(null, "Game Over");
			JOptionPane.showMessageDialog(null, "Score: " + score);
		int option = JOptionPane.showConfirmDialog(null, "Play Again?", "Flappy Block", JOptionPane.YES_NO_OPTION);
			if(option == 0) {
				score = 0 + "";
				checkScore = 0;
				barCounter=0;
				block = new Block(12,12, Color.BLACK, this.width/5, this.height/2, 1);
				topBars = new ArrayList<TopObstacle>();
				bottomBars = new ArrayList<BottomObstacle>();
				topBars.add(new TopObstacle(this.width - barWidth, 0, barWidth, (this.height/2) - (distanceBetweenBars/2), Color.BLUE));
				bottomBars.add(new BottomObstacle(this.width - barWidth, (this.height/2) +(distanceBetweenBars/2), barWidth, this.height, Color.BLUE));
				this.start();
			}
			else {
				System.exit(1);
			}
		}
}

}


}


