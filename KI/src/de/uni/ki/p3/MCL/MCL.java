package de.uni.ki.p3.MCL;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

public class MCL {

    private int particleAmount;
    private GraphicsContext gc;
    private int bestProbability;
    private List<Particle> particles;

    public MCL(final int particleAmount, final GraphicsContext gc) {
        this.particleAmount = particleAmount;
        this.gc = gc;
    }

    public void initializeParticles() {
        float initialWeight = 0.1f;

        for (int i = 0; i < particleAmount; i++) {
            float heading = (int) (Math.random() * 360);
            Particle particle = new Particle(0, 0, heading, initialWeight, gc);
            particles.add(particle);
        }
    }

    public void resample() {

    }

    public List<Particle> getParticles() {
        return particles;
    }
}