package ballgame;
import java.awt.*;

import javax.swing.*;

public class BallGame extends JFrame {
	Image ball =Toolkit.getDefaultToolkit().getImage("images/ball.png");
	Image desk =Toolkit.getDefaultToolkit().getImage("images/desk.jpg");
	
	double x=100,
			y=100;
	boolean right =true;
	
	public void paint(Graphics g) {
		System.out.println("paint once");
		g.drawImage(desk, 0, 0, null);
		g.drawImage(ball, (int)x, (int)y, null);
		if(right) {
			x=x+10;
		}else {
			x-=10;
		}
		if(x>856-40-30) {
			right=!right;
		}
		if(x<40) {
			right=!right;
		}
		
	}
	
	void launchFrame() {
		setSize(856,500);
		setLocation(400, 400);
		setVisible(true);
		while(true) {
			repaint();
			try {
				Thread.sleep(40);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BallGame game =new BallGame();
		game.launchFrame();
	}

}
