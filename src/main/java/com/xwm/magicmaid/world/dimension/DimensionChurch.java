package com.xwm.magicmaid.world.dimension;

import com.xwm.magicmaid.event.EventLoader;
import com.xwm.magicmaid.init.BiomeInit;
import com.xwm.magicmaid.init.DimensionInit;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.network.InfoLogginPacket;
import com.xwm.magicmaid.network.NetworkLoader;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWritableBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.end.DragonFightManager;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class DimensionChurch extends WorldProvider
{
    private MagicCreatureFightManager fightManager;

    public void init()
    {
        this.biomeProvider = new BiomeProviderSingle(BiomeInit.RUINS);
        NBTTagCompound nbttagcompound = this.world.getWorldInfo().getDimensionData(this.world.provider.getDimension());
        this.fightManager = this.world instanceof WorldServer ? new MagicMaidFightManager((WorldServer)this.world, nbttagcompound.getCompoundTag("MaidFight")) : null;
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
        int i = 10518688;
        float f = MathHelper.cos(p_76562_1_ * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        float f1 = 0.627451F;
        float f2 = 0.5019608F;
        float f3 = 0.627451F;
        f1 = f1 * (f * 0.0F + 0.15F);
        f2 = f2 * (f * 0.0F + 0.15F);
        f3 = f3 * (f * 0.0F + 0.15F);
        return new Vec3d((double)f1, (double)f2, (double)f3);
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
        return false;
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

    /**
     * Called when a Player is added to the provider's world.
     */
    public void onPlayerAdded(EntityPlayerMP player)
    {
//        player.sendMessage(new TextComponentString(
//                TextFormatting.YELLOW + "注意！boss比较强大，击杀难度较大，但是目前依然有很多方法击杀！\n\n" +
//                        TextFormatting.YELLOW  + "鉴于难度对新手不友好，本模组可以与苦力怕工作室的" +
//                        TextFormatting.RED + "【HSC】上古神器-石头利用" +
//                        TextFormatting.YELLOW + "联动！\n\n" +
//                        TextFormatting.YELLOW +"使用其中的终极武器" +
//                        TextFormatting.RED + "至密金刚剑" +
//                        TextFormatting.YELLOW + "可以轻松击杀boss，有需要的可以在网易组件中心找到"));

        if (!world.isRemote) {
            if (player != null) {
                if (player.getHeldItemOffhand().isEmpty())
                    player.setHeldItem(EnumHand.OFF_HAND, new ItemStack(ItemInit.ITEME_INSTRUCCTION_BOOK));
                else {
                    EntityItem entityItem = new EntityItem(player.getEntityWorld(), player.posX, player.posY, player.posZ, new ItemStack(ItemInit.ITEME_INSTRUCCTION_BOOK));
                    world.spawnEntity(entityItem);
                }
            }
        }

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

    public MagicCreatureFightManager getFightManager() {
        return this.fightManager;
    }

    public void setFightManager(MagicCreatureFightManager fightManager) {
        this.fightManager = fightManager;
    }
}
