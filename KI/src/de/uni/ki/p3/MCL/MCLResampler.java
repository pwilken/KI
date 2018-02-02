/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.MCL;

import java.util.List;

public interface MCLResampler
{
	public List<Particle> resample(MCL mcl, List<Particle> particles);
}
