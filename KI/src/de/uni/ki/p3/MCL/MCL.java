package de.uni.ki.p3.MCL;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;

public class MCL {

    private int particleAmount;
    private int bestProbability;

    public MCL(final int particleAmount) {
        this.particleAmount = particleAmount;
    }

    public List<MCLParticle> generateParticles(GraphicsContext gc) {
        float initialWeight = 0.1f;

        List<MCLParticle> particles = new ArrayList<>();
        for (int i = 0; i < particleAmount; i++) {
            float heading = (int) (Math.random() * 360);
            MCLParticle particle = new MCLParticle(0, 0, heading, initialWeight, gc);
            particles.add(particle);
        }

        return particles;
    }

    public void resample(final List<MCLParticle> particles) {

    }
}