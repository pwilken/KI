package de.uni.ki.p3.MCL;

import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class MCL {

    private int particleAmount;
    private GraphicsContext gc;
    private int bestProbability;
    private List<Particle> particles = new ArrayList<Particle>();
    private float noiseFactor = 0.4f;


    public MCL(final GraphicsContext gc) {
        this.gc = gc;
    }

    public void initializeParticles(double x, double y, double width, double height) {
        float initialWeight = 0.1f;

        for (int i = 0; i < particleAmount; i++) {
            float heading = (int) (Math.random() * 360);
            
            int xRandom = (int)(Math.random() * width);
            System.out.println("xRandom: " + xRandom);
            int yRandom = (int)(y + (Math.random() * height));
            System.out.println("yRandom: " + yRandom);
            
            Particle particle = new Particle(xRandom, yRandom, heading, initialWeight, gc);
            particles.add(particle);
        }
    }

    public void resample() {

    }

    public List<Particle> getParticles() {
        return particles;
    }

    public void setParticleAmount(final int particleAmount) {
        this.particleAmount = particleAmount;
    }
}