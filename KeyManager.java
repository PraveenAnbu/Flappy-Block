

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener{
	private boolean[] keys;
	public boolean sb;
	
	
	public KeyManager() {
		keys = new boolean[256];
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}
	public void tick() {
		sb = keys[KeyEvent.VK_SPACE];
	}
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		System.out.println("Pressed");
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		
	}

}
