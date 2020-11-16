package com.xwm.magicmaid.init;

import com.xwm.magicmaid.object.block.*;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BlockInit
{
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final Block BLOCK_HOLY_STONE = new BlockOreHolyStone("oreholystone");

    public static final Block BLOCK_MEMORY_CLOCK = new BlockMemoryClock("memoryclock");

    public static final Block MAGIC_CIRCLE = new BlockMagicCircle("magic_circle");

    public static final Block BLOCK_BIAN_FLOWER = new BlockBianFlower("bianflower");

    public static final Block CHURCH_PORTAL_FRAME = new BlockChurchPortalFrame("church_portal_frame");

    public static final Block CHURCH_PORTAL = new BlockChurchPortal("church_portal");
}
