package com.xwm.magicmaid.world.dimension;

import com.xwm.magicmaid.init.BiomeInit;
import com.xwm.magicmaid.init.DimensionInit;
import com.xwm.magicmaid.manager.IMagicBossManager;
import com.xwm.magicmaid.manager.IMagicBossManagerImpl;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class DimensionChurch extends WorldProvider
{
    private IMagicBossManager fightManager;

    public void init()
    {
//        this.biomeProvider = new BiomeProviderSingle(Biomes.DESERT);
        this.biomeProvider = new BiomeProviderSingle(BiomeInit.RUINS);
        this.hasSkyLight = true;
        NBTTagCompound nbttagcompound = this.world.getWorldInfo().getDimensionData(this.world.provider.getDimension());
        this.fightManager = this.world instanceof WorldServer ? new IMagicBossManagerImpl((WorldServer)this.world, nbttagcompound.getCompoundTag("MaidFight")) : null;
    }

    public BlockPos getSpawnCoordinate()
    {
        return new BlockPos(0, 55, 0);
    }


    public int getAverageGroundLevel()
    {
        return 50;
    }


    public boolean canCoordinateBeSpawn(int x, int z)
    {
        return this.world.getGroundAboveSeaLevel(new BlockPos(x, 0, z)).getMaterial().blocksMovement();
    }


    public float calculateCelestialAngle(long worldTime, float partialTicks)
    {
        return super.calculateCelestialAngle(worldTime, partialTicks);
    }

    /**
     * Returns array with sunrise/sunset colors
     */
    @Nullable
    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks)
    {
        float[] colorsSunriseSunset =  super.calcSunriseSunsetColors(celestialAngle, partialTicks);
        return colorsSunriseSunset;
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getSkyColor(net.minecraft.entity.Entity cameraEntity, float partialTicks)
    {
        return new Vec3d(1, 0, 0);
    }

    @SideOnly(Side.CLIENT)
    public boolean isSkyColored()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_)
    {
        return super.getFogColor(p_76562_1_, p_76562_2_);
    }

    @Override
    public DimensionType getDimensionType() {
        return DimensionInit.CHURCH;
    }

    @Override
    public IChunkGenerator createChunkGenerator()
    {
        return new ChunkGeneratorChurch(world, true, this.world.getSeed(), getSpawnCoordinate());
    }

    @Override
    public boolean canRespawnHere()
    {
        return false;
    }

    @Override
    public boolean isSurfaceWorld()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public float getCloudHeight()
    {
        return 8.0F;
    }

    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z)
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1)
    {
        return 1;
    }

    public boolean shouldClientCheckLighting(){
        return true;
    }

    @SideOnly(Side.CLIENT)
    public float getSunBrightness(float par1)
    {
        return world.getSunBrightnessBody(par1);
    }


    /**
     * Called when a Player is added to the provider's world.
     */
    public void onPlayerAdded(EntityPlayerMP player)
    {
        if (fightManager != null) {
            fightManager.addPlayer(player);
        }
    }

    @Override
    public void onPlayerRemoved(net.minecraft.entity.player.EntityPlayerMP player)
    {
        if (this.fightManager != null)
        {
            this.fightManager.removePlayer(player);
        }
    }

    public void onWorldUpdateEntities()
    {
        if (this.fightManager != null && !world.isRemote)
        {
            this.fightManager.tick();
        }

        if (this.getWorldTime() == 1 && this.world.loadedEntityList.size() > 50) {
            for (Entity entity : this.world.loadedEntityList)
            {
                if (entity instanceof EntityVillager)
                {
                    entity.setFire(20);
                }
            }
        }

        super.onWorldUpdateEntities();
    }

    public void onWorldSave()
    {
        if (fightManager != null) {
            NBTTagCompound nbttagcompound = new NBTTagCompound();

            nbttagcompound.setTag("MaidFight", this.fightManager.getCompound());

            this.world.getWorldInfo().setDimensionData(this.world.provider.getDimension(), nbttagcompound);
        }
        super.onWorldSave();
    }

    public IMagicBossManager getFightManager() {
        return this.fightManager;
    }

    public void setFightManager(IMagicBossManager fightManager) {
        this.fightManager = fightManager;
    }
}
