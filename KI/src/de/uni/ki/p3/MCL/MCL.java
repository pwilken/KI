package de.uni.ki.p3.MCL;

import de.uni.ki.p3.Drawing.MapObject;
import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;

public class MCL {

	private int particleAmount;
	private GraphicsContext gc;
	private int bestProbability;
	private List<Particle> particles = new ArrayList<Particle>();
	private float noiseFactor = 0.4f;
	private MapObject map;
	private float[] weightTable;

	public MCL(final GraphicsContext gc, final MapObject map) {
		this.gc = gc;
		this.map = map;
	}

	public void initializeParticles(double x, double y, double width, double height) {
		this.weightTable = new float[this.particleAmount];
		float initialWeight = 0f;

		for (int i = 0; i < particleAmount; i++) {
			float heading = (int) (Math.random() * 360);

			int xRandom = (int) (Math.random() * width);
			// System.out.println("xRandom: " + xRandom);
			int yRandom = (int) (y + (Math.random() * height));
			// System.out.println("yRandom: " + yRandom);

			Particle particle = new Particle(xRandom, yRandom, heading, initialWeight, gc, map);
			particles.add(particle);
			
			setWeightTable();
		}
	}
	
	private void setWeightTable()
	{
		float weight = 1;
		for(int i = 0; i < particleAmount; i++)
		{
			weightTable[i] = weight / (i+1);
		}
	}

	public void mcl(float robotDistance, float heading, float speed) {
		particles.forEach(particle -> {
			if (particle.measure() <= robotDistance) {
				int index = (int) (robotDistance - particle.measure());
				if(index >= particleAmount) index = particleAmount - 1;
				particle.weight = weightTable[index];
				System.out.println("Particle-Weight: " + particle.weight);
			} else {
				int index = (int)(robotDistance - particle.measure())*-1;
				if(index >= particleAmount) index = particleAmount - 1;
				particle.weight = weightTable[index];
				System.out.println("Particle-Weight: " + particle.weight);
			}

			particle.heading += (Math.random() * 360);
			particle.heading %= 360;

			// particle movement
			float endX = (particle.x + speed);
			float endY = (particle.y + speed);
			Rotate r = new Rotate(particle.heading - 225, particle.x, particle.y);
			Point2D p = r.transform(endX, endY);
			particle.x = (float) p.getX();
			particle.y = (float) p.getY();
		});

		resample();

		for (Particle p : particles)
			p.draw();
	}

	private void resample() {
		int N = particles.size();
		List<Particle> new_particles = new ArrayList<Particle>();

		double incr = 0;
		int index = 0;

		for (int i = 0; i < N; i++) {
			incr += particles.get(i).weight;
		}

		incr = incr / 2.0 / N;
		double beta = incr;

		for (int i = 0; i < N; i++) {
			while (beta > particles.get(index).weight) {
				beta -= particles.get(index).weight;
				index = (index + 1) % N;
			}

			beta += incr;
			try {
				new_particles.add((Particle) particles.get(index).clone());
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		particles = new_particles;
	}

	public List<Particle> getParticles() {
		return particles;
	}

	public void setParticleAmount(final int particleAmount) {
		this.particleAmount = particleAmount;
	}
}