import java.awt.Color;
import java.util.LinkedList;

public class Particle {
	public LinkedList<Particle> clusterMembers;
	public double mass, charge, radius;
	public double px, py;
	public double x, y;
	public double vx, vy;
	public double ax, ay;
	public Color color;

	public LinkedList<Particle> electricFieldParticles;

	public static final double CHARGE_CONSTANT = 50;

	public Particle() {
		mass = 3;
		radius = Math.cbrt((300000 * mass) / (4 * Math.PI));
		if (Math.random() < .5)
			charge = -(2) * CHARGE_CONSTANT;
		else
			charge = (2) * CHARGE_CONSTANT;
		color = new Color((int) ((-charge * (255 / (CHARGE_CONSTANT * 4))) + (127.5)), 0,
				(int) ((charge * (255 / (CHARGE_CONSTANT * 4))) + (127.5)));
		x = Math.random() * 1920;
		y = Math.random() * 1080;
		clusterMembers = new LinkedList<>();
		electricFieldParticles = new LinkedList<>();
	}

	public void addClusterMember(Particle p) {
		clusterMembers.add(p);
	}

	public void generateElectricField() {
		double cx = (x), cy = (y);
		Particle k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx + radius / 2;
		k.y = cy;
		electricFieldParticles.add(k);
		k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx;
		k.y = cy + radius / 2;
		electricFieldParticles.add(k);
		k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx - radius / 2;
		k.y = cy;
		electricFieldParticles.add(k);
		k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx;
		k.y = cy - radius / 2;
		electricFieldParticles.add(k);
		k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx + Math.sqrt(Math.pow(radius / 2, 2) / 2);
		k.y = cy + Math.sqrt(Math.pow(radius / 2, 2) / 2);
		electricFieldParticles.add(k);
		k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx - Math.sqrt(Math.pow(radius / 2, 2) / 2);
		k.y = cy - Math.sqrt(Math.pow(radius / 2, 2) / 2);
		electricFieldParticles.add(k);
		k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx - Math.sqrt(Math.pow(radius / 2, 2) / 2);
		k.y = cy + Math.sqrt(Math.pow(radius / 2, 2) / 2);
		electricFieldParticles.add(k);
		k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx + Math.sqrt(Math.pow(radius / 2, 2) / 2);
		k.y = cy - Math.sqrt(Math.pow(radius / 2, 2) / 2);
		electricFieldParticles.add(k);

		k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx + (radius / 2) * Math.sin((180 / Math.PI) * 22.5);
		k.y = cy + (radius / 2) * Math.cos((180 / Math.PI) * 22.5);
		electricFieldParticles.add(k);
		k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx - ((radius / 2) * Math.sin((180 / Math.PI) * 22.5));
		k.y = cy - ((radius / 2) * Math.cos((180 / Math.PI) * 22.5));
		electricFieldParticles.add(k);
		k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx + (radius / 2) * Math.sin((180 / Math.PI) * 22.5);
		k.y = cy - ((radius / 2) * Math.cos((180 / Math.PI) * 22.5));
		electricFieldParticles.add(k);
		k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx - ((radius / 2) * Math.sin((180 / Math.PI) * 22.5));
		k.y = cy + (radius / 2) * Math.cos((180 / Math.PI) * 22.5);
		electricFieldParticles.add(k);
		k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx + (radius / 2) * Math.cos((180 / Math.PI) * 22.5);
		k.y = cy + (radius / 2) * Math.sin((180 / Math.PI) * 22.5);
		electricFieldParticles.add(k);
		k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx - ((radius / 2) * Math.cos((180 / Math.PI) * 22.5));
		k.y = cy - ((radius / 2) * Math.sin((180 / Math.PI) * 22.5));
		electricFieldParticles.add(k);
		k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx + (radius / 2) * Math.cos((180 / Math.PI) * 22.5);
		k.y = cy - ((radius / 2) * Math.sin((180 / Math.PI) * 22.5));
		electricFieldParticles.add(k);
		k = new Particle();
		k.charge = this.charge;
		k.mass = 100;
		k.x = cx - ((radius / 2) * Math.cos((180 / Math.PI) * 22.5));
		k.y = cy + (radius / 2) * Math.sin((180 / Math.PI) * 22.5);
		electricFieldParticles.add(k);
	}

	public LinkedList<Particle> getElectricFieldParticles() {
		return electricFieldParticles;
	}

	public void advanceOneFrame() {
		// Calculate Superposition Forces
		double lastForceCurr = 0;
		double forcex = 0, forcey = 0;
		LinkedList<Particle> globalParticles = ElectricField.particles;
		for (int i = globalParticles.size() - 1; i >= 0 && i < globalParticles.size(); i--) {
			Particle other = globalParticles.get(i);
			if (other != null && !other.equals(this) && !clusterMembers.contains(other)) {
				double forceCurr = 0.0;
				if (ElectricField.particles.contains(other)
						&& ElectricField.particles.get(ElectricField.particles.indexOf(other)).electricFieldParticles
								.contains(this))
					forceCurr = ElectricField.COULOMBS_CONSTANT * (Math.abs(charge * (other.charge / 10)))
							/ ((Math.pow((other.x + (other.radius / 2)) - (x + (radius / 2)), 2)
									+ Math.pow((other.y + (other.radius / 2)) - (y + (radius / 2)), 2)) * 30);
				else
					forceCurr = ElectricField.COULOMBS_CONSTANT * (Math.abs(charge * (other.charge * 1)))
							/ ((Math.pow((other.x + (other.radius / 2)) - (x + (radius / 2)), 2)
									+ Math.pow((other.y + (other.radius / 2)) - (y + (radius / 2)), 2)) * 30);
				lastForceCurr += forceCurr;
				double theta = 0.0;
				double sign = 1;
				double dist = (Math.sqrt(Math.pow((other.x + (other.radius / 2)) - (x + (radius / 2)), 2)
						+ Math.pow((other.y + (other.radius / 2)) - (y + (radius / 2)), 2)));
				theta = Math.atan(Math.abs((other.y + (other.radius / 2)) - (y + (radius / 2)))
						/ Math.abs(((other.x + (other.radius / 2)) - (x + (radius / 2)))));
				if ((charge > 0 && other.charge > 0) || (charge < 0 && other.charge < 0))
					sign = -1;
				if (other.y + (other.radius / 2) > y + (radius / 2)
						&& other.x + (other.radius / 2) > x + (radius / 2)) {
					forcex += Math.cos(theta) * sign * forceCurr;
					forcey += Math.sin(theta) * sign * forceCurr;
				} else if (other.y + (other.radius / 2) < y + (radius / 2)
						&& other.x + (other.radius / 2) < x + (radius / 2)) {
					forcex += -Math.cos(theta) * sign * forceCurr;
					forcey += -Math.sin(theta) * sign * forceCurr;
				} else if (other.y + (other.radius / 2) < y + (radius / 2)
						&& other.x + (other.radius / 2) > x + (radius / 2)) {
					forcex += Math.cos(theta) * sign * forceCurr;
					forcey += -Math.sin(theta) * sign * forceCurr;
				} else if (other.y + (other.radius / 2) > y + (radius / 2)
						&& other.x + (other.radius / 2) < x + (radius / 2)) {
					forcex += -Math.cos(theta) * sign * forceCurr;
					forcey += Math.sin(theta) * sign * forceCurr;
				} else if (other.y + (other.radius / 2) < y + (radius / 2)
						&& other.x + (other.radius / 2) == x + (radius / 2)) {
					forcey += -forceCurr * sign;
				} else if (other.y + (other.radius / 2) > y + (radius / 2)
						&& other.x + (other.radius / 2) == x + (radius / 2)) {
					forcey += forceCurr * sign;
				} else if (other.y + (other.radius / 2) == y + (radius / 2)
						&& other.x + (other.radius / 2) < x + (radius / 2)) {
					forcex += -forceCurr * sign;
				} else if (other.y + (other.radius / 2) == y + (radius / 2)
						&& other.x + (other.radius / 2) > x + (radius / 2)) {
					forcex += forceCurr * sign;
				}
			}
		}
		ax = forcex / (mass * 10000);
		ay = forcey / (mass * 10000);
		vx += ax;
		vy += ay;
		if (ElectricField.particles.contains(this)) {
			x += vx / 1;
			y += vy / 1;
		} else {
			px = x;
			py = y;
			x += vx / 10;
			y += vy / 10;
		}
	}
}
