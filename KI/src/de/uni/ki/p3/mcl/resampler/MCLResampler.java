/*
 * Copyright © 2018 Unitechnik Systems GmbH. All Rights Reserved.
 */
package de.uni.ki.p3.mcl.resampler;

import java.util.List;

import de.uni.ki.p3.mcl.*;

public interface MCLResampler
{
	public List<Particle> resample(MCL mcl, List<Particle> particles);
}
