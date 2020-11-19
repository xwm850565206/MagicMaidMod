package com.xwm.magicmaid.object.tileentity;

import com.xwm.magicmaid.object.block.BlockChurchPortalFrame;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityChurchPortal extends TileEntity
{
    private EnumFacing facing;

    @SideOnly(Side.CLIENT)
    public boolean shouldRenderFace(EnumFacing p_184313_1_)
    {
        if(facing == null)
        {
            BlockPattern.PatternHelper blockPattern$PatternHelper = BlockChurchPortalFrame.getOrCreatePortalShape().match(world, pos);
            if (blockPattern$PatternHelper == null)
                facing = EnumFacing.NORTH;
            else
                facing = blockPattern$PatternHelper.getForwards();
        }
        return facing == p_184313_1_ || facing.getOpposite() == p_184313_1_;
    }
}
