package de.uni.ki.p3.MCL;

import java.util.ArrayList;

public class MCLParticleSet {
	private ArrayList<MCLParticle> mclParticleSet;

	public ArrayList<MCLParticle> getMclParticleSet() {
		return mclParticleSet;
	}

	public void setMclParticleSet(ArrayList<MCLParticle> mclParticleSet) {
		this.mclParticleSet = mclParticleSet;
	}

	public int getCount() {
		return mclParticleSet.size();
	}

	public MCLParticle getParticle(int index) {
		return mclParticleSet.get(index);
	}
}
