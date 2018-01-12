package de.uni.ki.p3.MCL;

import javafx.scene.canvas.GraphicsContext;

public class Particle {
	private float orientation, X, Y;

	public Particle(float x, float y, float orientation) {
		this.orientation = orientation;
		X = x;
		Y = y;
	}

	public void display(GraphicsContext gc) {
		// Draw Orientation as well
		gc.fillOval(X, Y, 3, 3);
	}

	public float getX() {
		return X;
	}

	public void setX(float x) {
		X = x;
	}

	public float getY() {
		return Y;
	}

	public void setY(float y) {
		Y = y;
	}

	public float getOrientation() {
		return orientation;
	}

	public void setOrientation(float orientation) {
		this.orientation = orientation;
	}
}