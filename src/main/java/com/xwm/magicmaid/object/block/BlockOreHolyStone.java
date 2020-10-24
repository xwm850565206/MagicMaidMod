package com.xwm.magicmaid.object.block;

import com.xwm.magicmaid.Main;
import com.xwm.magicmaid.init.BlockInit;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.util.interfaces.IHasModel;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

import static com.xwm.magicmaid.creativetab.CreativeTabMaid.CREATIVE_TAB_MAID;

public class BlockOreHolyStone extends BlockOre implements IHasModel
{
    public BlockOreHolyStone(String name)
    {
        super(MapColor.SILVER);
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(CREATIVE_TAB_MAID);
        setHardness(3.0f);
        setResistance(5.0f);
        setSoundType(SoundType.STONE);

        BlockInit.BLOCKS.add(this);
        ItemInit.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
    }

    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ItemInit.ITEM_HOLY_STONE;
    }

    @Override
    public int getExpDrop(IBlockState state, net.minecraft.world.IBlockAccess world, BlockPos pos, int fortune)
    {
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        return MathHelper.getInt(rand, 5, 10);
    }

    @Override
    public void registerModels() {
        Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
    }
}
