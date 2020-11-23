package com.xwm.magicmaid.object.tileentity;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.List;

public class ResultNBTTagCompound extends Result
{
    protected String key;

    protected ResultNBTTagCompound(List<ItemStack> stacks, MetadataTypes type, int metadata, String key) {
        super(stacks, type, metadata);
        this.key = key;
    }

    public static ResultNBTTagCompound create(List<ItemStack> stacks, MetadataTypes type, int metadata, String key)
    {
        return new ResultNBTTagCompound(stacks, type, metadata, key);
    }

    public static ResultNBTTagCompound create(List<ItemStack> stacks, MetadataTypes type, String key)
    {
        return new ResultNBTTagCompound(stacks, type, 0, key);
    }

    public static ResultNBTTagCompound create(List<ItemStack> stacks, int metadata, String key)
    {
        return new ResultNBTTagCompound(stacks, MetadataTypes.INCREASE, metadata, key);
    }

    @Override
    public List<ItemStack> getResult(ItemStack keyItem)
    {
        List<ItemStack> stacks1 = Lists.newArrayList(stacks);
        ItemStack stack1 = keyItem.copy();
        NBTTagCompound compound = stack1.getTagCompound();
        if (compound == null)
            compound = new NBTTagCompound();
        int metadata = compound.hasKey(key) ? compound.getInteger(key) : 0;
        switch (type) {
            case SET:
                compound.setInteger(key, this.metadata); break;
            case DECREASE:
                compound.setInteger(key, metadata - this.metadata); break;
            case INCREASE:
                compound.setInteger(key, metadata + this.metadata);  break;
        }
        stack1.setCount(1);
        stack1.setTagCompound(compound);
        stacks1.add(stack1);
        return stacks1;
    }
}
