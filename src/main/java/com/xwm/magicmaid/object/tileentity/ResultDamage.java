package com.xwm.magicmaid.object.tileentity;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ResultDamage extends Result
{

    protected ResultDamage(List<ItemStack> stacks, MetadataTypes type, int metadata) {
        super(stacks, type, metadata);
    }

    public static ResultDamage create(List<ItemStack> stacks, MetadataTypes type, int metadata)
    {
        return new ResultDamage(stacks, type, metadata);
    }

    public static ResultDamage create(List<ItemStack> stacks, MetadataTypes type)
    {
        return new ResultDamage(stacks, type,0);
    }

    public static ResultDamage create(List<ItemStack> stacks, int metadata)
    {
        return new ResultDamage(stacks, MetadataTypes.INCREASE, metadata);
    }

    @Override
    public List<ItemStack> getResult(ItemStack keyItem)
    {
        List<ItemStack> stacks1 = Lists.newArrayList(stacks);
        ItemStack stack1 = keyItem.copy();
        stack1.setCount(1);
        switch (type) {
            case SET:
                stack1.setItemDamage(metadata); break;
            case DECREASE:
                stack1.setItemDamage(keyItem.getItemDamage() - metadata); break;
            case INCREASE:
                stack1.setItemDamage(keyItem.getItemDamage() + metadata); break;
        }
        stack1.setItemDamage(keyItem.getItemDamage() + metadata);
        stacks1.add(stack1);
        return stacks1;
    }
}
