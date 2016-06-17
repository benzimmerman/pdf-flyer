/*
 * Copyright 2016 Ben Zimmerman and distributed under v3 of the LGPL.
 */
package kmctb;

/**
 * @author zeroblue76@gmail.com
 */
public enum FlyerType {

	FULL_SHEET(8.25f, 10.75f), HALF_SHEET(8.25f, 5.25f), QUARTER_SHEET(4f, 5.25f);

	final float width;
	final float height;

	private FlyerType(final float w, final float h) {
		this.width = w;
		this.height = h;
	}

	public float getWidth() {
		return this.width;
	}

	public float getHeight() {
		return this.height;
	}

	@Override
	public String toString() {
		return String.format("%s [%.2fx%.2f]", super.name(), this.width, this.height);
	}
}
