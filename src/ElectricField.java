import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ElectricField extends JPanel {
	private static final long serialVersionUID = -271293722209467033L;

	public static JFrame f = new JFrame();
	public static ElectricField EF;
	public static LinkedList<Particle> particles = new LinkedList<>();

	public static boolean fieldPause = false, endGeneration = false;
	public static int zoom = 1;

	public static final double COULOMBS_CONSTANT = 1000;

	public ElectricField() {
		for (int i = 0; i < 3; i++) {
			particles.add(new Particle());
		}
		Timer dynamics = new Timer();
		dynamics.schedule(new TimerTask() {
			@Override
			public void run() {
				if (!fieldPause) {
					for (int i = 0; i < particles.size(); i++)
						particles.get(i).advanceOneFrame();
					detectCollisions();
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					for (int i = 0; i < particles.size(); i++) {
						for (int j = 0; j < particles.get(i).getElectricFieldParticles().size(); j++) {
							if (particles.get(i).getElectricFieldParticles().size() > 0) {
								// particles.get(i).electricFieldParticles.clear();
							}
						}
					}
				} else if (!endGeneration) {
					for (int i = 0; i < particles.size(); i++) {
						// particles.get(i).generateElectricField();
					}
					endGeneration = true;
				}
				repaint();
			}
		}, 0, 1);
	}

	public void detectCollisions() {
		for (int i = 0; i < particles.size(); i++) {
			Particle a = particles.get(i);
			for (int j = 0; j < particles.size(); j++) {
				Particle b = particles.get(j);
				if (!a.equals(b)) {
					int radSum = (int) ((a.radius / 2) + (b.radius / 2));
					int dist = (int) (Math.sqrt(Math.pow((a.x) - (b.x), 2) + Math.pow((a.y) - (b.y), 2)));
					if (radSum > dist && a.charge / b.charge < 0) {
						a.addClusterMember(b);
						b.addClusterMember(a);
						double cx = ((a.mass * a.vx) + (b.mass * b.vx)) / (a.mass + b.mass);
						double cy = ((a.mass * a.vy) + (b.mass * b.vy)) / (a.mass + b.mass);
						a.vx = cx;
						a.vy = cy;
						b.vx = cx;
						b.vy = cy;
					} else {
						if (a.clusterMembers.contains(b))
							a.clusterMembers.remove(b);
						if (b.clusterMembers.contains(a))
							b.clusterMembers.remove(a);
					}
				}
			}
			for (int j = particles.get(i).electricFieldParticles.size() - 1; j >= 0; j--) {
				for (int c = 0; c < particles.size(); c++) {
					int radSum = (int) ((particles.get(c).radius / 2));
					int dist = (int) (Math.sqrt(Math
							.pow((particles.get(i).electricFieldParticles.get(j).x
									+ (particles.get(i).getElectricFieldParticles().get(j).radius / 2))
									- (particles.get(c).x + (particles.get(c).radius / 2)), 2)
							+ Math.pow((particles.get(i).electricFieldParticles.get(j).y
									+ (particles.get(i).getElectricFieldParticles().get(j).radius / 2))
									- (particles.get(c).y + (particles.get(c).radius / 2)), 2)));
					if (radSum > dist && !particles.get(c).electricFieldParticles
							.contains(particles.get(i).electricFieldParticles.get(j))) {
						particles.get(i).electricFieldParticles.remove(j);
						break;
					}
				}
			}
		}
	}

	public void paint(Graphics g) {
		if (!fieldPause) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 2000, 2000);
			for (int i = 0; i < particles.size(); i++) {
				Particle p = particles.get(i);
				g.setColor(p.color);
				g.fillOval((int) (p.x - (p.radius / 2)), (int) (p.y - (p.radius / 2)), (int) (p.radius),
						(int) (p.radius));
			}
			int quality = 3;
			for (int i = 0; i < 1920; i += quality) {
				for (int j = 0; j < 1080; j += quality) {
					double rgb_val = 255 / (1 + Math.pow(Math.E, -(1 * getPotentialDifference(i, j))));
					rgb_val = rgb_val - (rgb_val % 15);
					g.setColor(new Color((int) (255 - rgb_val), 0, (int) (rgb_val)));
					g.fillRect((int) (i), (int) (j), quality, quality);
				}
			}
		} else {
			g.setColor(Color.WHITE);
			for (int i = 0; i < particles.size(); i++) {
				for (int j = 0; j < particles.get(i).getElectricFieldParticles().size(); j++) {
					if ((int) particles.get(i).getElectricFieldParticles().get(j).px != 0)
						g.drawLine((int) particles.get(i).getElectricFieldParticles().get(j).x,
								(int) particles.get(i).getElectricFieldParticles().get(j).y,
								(int) particles.get(i).getElectricFieldParticles().get(j).px,
								(int) particles.get(i).getElectricFieldParticles().get(j).py);
					particles.get(i).getElectricFieldParticles().get(j).advanceOneFrame();
					detectCollisions();
				}
			}
		}
	}

	public static void printScreen() {
		try {
			Robot robot = new Robot();
			String format = "jpg";
			String fileName = "screenshot_em." + format;

			Rectangle screenRect = new Rectangle(1920, 1050);
			BufferedImage screenFullImage = robot.createScreenCapture(screenRect);
			ImageIO.write(screenFullImage, format, new File(fileName));

		} catch (Exception ex) {
			System.err.println(ex);
		}
	}

	public double getPotentialDifference(int x, int y) {
		double sum = 0.0;
		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			sum += (p.charge) / Math.sqrt(Math.pow(p.x - x, 2) + Math.pow(p.y - y, 2));
		}
		return sum;
	}

	public static void main(String[] args) {
		ElectricField EF = new ElectricField();
		f.add(EF);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		f.setVisible(true);
		f.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int keyCode = e.getKeyCode();
				switch (keyCode) {
				case KeyEvent.VK_P:
					fieldPause = !fieldPause;
					break;
				case KeyEvent.VK_UP:
					zoom++;
					break;
				case KeyEvent.VK_DOWN:
					zoom--;
					break;
				case KeyEvent.VK_E:
					endGeneration = false;
					break;
				case KeyEvent.VK_M:
					printScreen();
					break;
				}
			}
		});
	}
}
