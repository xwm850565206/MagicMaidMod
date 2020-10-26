package com.xwm.magicmaid.init;

import com.xwm.magicmaid.object.block.BlockBianFlower;
import com.xwm.magicmaid.object.block.BlockMagicCircle;
import com.xwm.magicmaid.object.block.BlockMemoryClock;
import com.xwm.magicmaid.object.block.BlockOreHolyStone;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockInit
{
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block blockHolyStone = new BlockOreHolyStone("oreholystone");

    public static final Block blockMemoryClock = new BlockMemoryClock("memoryclock");

    public static final Block magicCircle = new BlockMagicCircle("magic_circle");

    public static final Block bianFlower = new BlockBianFlower("bianflower");
}
