package com.xwm.magicmaid.world.dimension;

import com.google.common.base.Predicate;
import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.init.BlockInit;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.BlockPortal;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class ChurchTeleporter extends Teleporter
{
    public static BlockPos aimPos = new BlockPos(100, 60, 0);

    public int dimension;
    private final WorldServer worldServerInstance;
    private final Random random;
    private double prevY, prevX, prevZ;

    public ChurchTeleporter(WorldServer worldIn, int dimension, double x, double y, double z)
    {

        super(worldIn);
        this.worldServerInstance = worldIn;
        this.random = new Random(worldIn.getSeed());
        this.dimension = dimension;
        this.prevX = x;
        this.prevY = y;
        this.prevZ = z;

    }

    public void placeInPortal(Entity entityIn, float rotationYaw)
    {
        entityIn.setLocationAndAngles(aimPos.getX(), aimPos.getY(), aimPos.getZ(), entityIn.rotationYaw, entityIn.rotationPitch);
    }

    public boolean placeInExistingPortal(Entity entityIn, float rotationYaw)
    {
        return false;
    }

    public boolean makePortal(Entity entityIn)
    {
//        entityIn.setLocationAndAngles(aimPos.getX(), aimPos.getY(), aimPos.getZ(), entityIn.rotationYaw, entityIn.rotationPitch);

        return true;
    }
}