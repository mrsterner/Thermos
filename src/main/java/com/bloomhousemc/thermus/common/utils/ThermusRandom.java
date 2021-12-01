package com.bloomhousemc.thermus.common.utils;

import net.minecraft.world.gen.random.AbstractRandom;
import net.minecraft.world.gen.random.RandomDeriver;

public class ThermusRandom implements AbstractRandom {
    @Override
    public AbstractRandom derive() {
        return null;
    }

    @Override
    public RandomDeriver createRandomDeriver() {
        return null;
    }

    @Override
    public void setSeed(long seed) {

    }

    @Override
    public int nextInt() {
        return 0;
    }

    @Override
    public int nextInt(int bound) {
        return 0;
    }

    @Override
    public long nextLong() {
        return 0;
    }

    @Override
    public boolean nextBoolean() {
        return false;
    }

    @Override
    public float nextFloat() {
        return 0;
    }

    @Override
    public double nextDouble() {
        return 0;
    }

    @Override
    public double nextGaussian() {
        return 0;
    }
}
