package de.uni.ki.p3.MCL;

import java.util.List;

public class MCL {

    private int particleAmount;
    private int bestProbability;

    public MCL(final int particleAmount) {
        this.particleAmount = particleAmount;
    }

    public List<MCLParticle> generateParticles() {
        for (int i = 0; i < particleAmount; i++) {
            int heading = (int) (Math.random() * 360);
//            MCLParticle particle = new MCLParticle();

        }
        return null;
    }

    public void resample(final List<MCLParticle> particles) {

    }
}