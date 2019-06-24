
import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;
public class Display {
		Canvas canvas;
		JFrame frame;
		int height;
		int width;
		String title;
	public Display(int height, int width, String title) {
		this.height = height;
		this.width = width;
		this.title = title;
		makeDisplay();
	}
	
	void makeDisplay() {
		frame = new JFrame(this.title);
		frame.setSize(this.width, this.height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(this.width, this.height));
		canvas.setMinimumSize(new Dimension(this.width, this.height));
		canvas.setMaximumSize(new Dimension(this.width, this.height));
		canvas.setFocusable(false);
		frame.add(canvas);
		frame.pack();
	}
	
	public Canvas getCanvas() {
		// TODO Auto-generated method stub
		return canvas;
	}
	
	public JFrame getFrame() {
		return frame;
	}
}
