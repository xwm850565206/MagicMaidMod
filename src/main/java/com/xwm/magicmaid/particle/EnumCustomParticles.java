package com.xwm.magicmaid.particle;

import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;


public enum EnumCustomParticles
{
    SOUL("soul", 0, false),
    CROSS("cross", 1, false),
    CONVICTION("conviction", 2, false),
    STAR("star", 3, false),
    PANDORA("pandora", 4, false),
    WHISPER("whisper", 5, false);

    private final String particleName;
    private final int particleID;
    private final boolean shouldIgnoreRange;
    private final int argumentCount;
    private static final Map<Integer, EnumCustomParticles> PARTICLES = Maps.<Integer, EnumCustomParticles>newHashMap();
    private static final Map<String, EnumCustomParticles> BY_NAME = Maps.<String, EnumCustomParticles>newHashMap();

    private EnumCustomParticles(String particleNameIn, int particleIDIn, boolean shouldIgnoreRangeIn, int argumentCountIn)
    {
        this.particleName = particleNameIn;
        this.particleID = particleIDIn;
        this.shouldIgnoreRange = shouldIgnoreRangeIn;
        this.argumentCount = argumentCountIn;
    }


    private EnumCustomParticles(String particleNameIn, int particleIDIn, boolean shouldIgnoreRangeIn)
    {
        this(particleNameIn, particleIDIn, shouldIgnoreRangeIn, 0);
    }

    public static Set<String> getParticleNames()
    {
        return BY_NAME.keySet();
    }

    public String getParticleName()
    {
        return this.particleName;
    }

    public int getParticleID()
    {
        return this.particleID;
    }

    public int getArgumentCount()
    {
        return this.argumentCount;
    }

    public boolean getShouldIgnoreRange()
    {
        return this.shouldIgnoreRange;
    }

    /**
     * Gets the relative EnumParticleTypes by id.
     */
    @Nullable
    public static EnumCustomParticles getParticleFromId(int particleId)
    {
        return PARTICLES.get(Integer.valueOf(particleId));
    }

    @Nullable
    public static EnumCustomParticles getByName(String nameIn)
    {
        return BY_NAME.get(nameIn);
    }

    static
    {
        for (EnumCustomParticles enumparticletypes : values())
        {
            PARTICLES.put(Integer.valueOf(enumparticletypes.getParticleID()), enumparticletypes);
            BY_NAME.put(enumparticletypes.getParticleName(), enumparticletypes);
        }
    }
}
