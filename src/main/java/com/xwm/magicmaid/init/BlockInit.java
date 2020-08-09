package com.xwm.magicmaid.init;

import com.xwm.magicmaid.object.block.BlockOreHolyStone;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockInit
{
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block blockHolyStone = new BlockOreHolyStone("oreholystone");
}
