package com.xwm.magicmaid.object.tileentity;

import com.google.common.collect.Lists;
import com.xwm.magicmaid.init.ItemInit;
import com.xwm.magicmaid.object.block.BlockMagicCircle;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.particle.ParticleSpawner;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TileEntityMagicCircle extends TileEntity implements IInventory, ITickable
{

    private static HashMap<Item, List<Item>> formula = new HashMap<Item, List<Item>>() {{
        put(ItemInit.itemLostKey, Lists.newArrayList(ItemInit.itemJustice, ItemInit.itemEvil));
    }};

    private static HashMap<Item, Integer> cookTimeMap = new HashMap<Item, Integer>() {{
        put(ItemInit.itemLostKey, 200);
    }};

    private NonNullList<ItemStack> inventory = NonNullList.withSize(8, ItemStack.EMPTY);
    private String customName;

    private int cookTime = 0;
    private int totalCookTime = -1;
    private Random random = new Random();

    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.magic_circle";
    }

    @Override
    public boolean hasCustomName()
    {
        return this.customName != null && !this.customName.isEmpty();
    }

    public void setCustomName(String customName)
    {
        this.customName = customName;
    }

    @Override
    public ITextComponent getDisplayName()
    {
        return this.hasCustomName() ? new TextComponentString(this.getName()) : new TextComponentTranslation(this.getName());
    }

    @Override
    public int getSizeInventory()
    {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty()
    {
        for (ItemStack stack : this.inventory) if (!stack.isEmpty())
            return false;
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index)
    {
        return (ItemStack)this.inventory.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        markDirty();
        return ItemStackHelper.getAndSplit(this.inventory, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        if (index == 4) {
            this.cookTime = 0;
            this.totalCookTime = -1;
            this.markDirty();
        }

        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        if (!stack.isEmpty()) {
            this.inventory.set(index, stack);
            if (index == 4){ // 烧炼物
                this.cookTime = 0;
                this.totalCookTime = -1;
                this.markDirty();
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.inventory = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.inventory);
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");

        if (compound.hasKey("CustomName", 8))
            this.setCustomName(compound.getString("CustomName"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        compound.setInteger("CookTime", this.cookTime);
        compound.setInteger("CookTimeTotal", this.totalCookTime);
        ItemStackHelper.saveAllItems(compound, this.inventory);

        if (this.hasCustomName()) compound.setString("CustomName", this.customName);
        return compound;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     *
     * @param player
     */
    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.add(0.5, 0, 0.5)) <= 64;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        //todo 魔法阵的声音
    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     *
     * @param index
     * @param stack
     */
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 5 || index == 6 || index == 7) return false;
        return true;
    }

    @Override
    public int getField(int id) {
        switch (id){
            case 0:
                return this.cookTime;
            case 1:
                return this.totalCookTime;
            default:
                    return 0;
        }
    }

    @Override
    public void setField(int id, int value) {
        switch (id)
        {
            case 0:
                this.cookTime = value;
            case 1:
                this.totalCookTime = value;
        }
    }

    @Override
    public int getFieldCount() {
        return 2;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    public boolean isCooking()
    {
        return this.totalCookTime > 0;
    }

    private int tick = 0; // 控制粒子效果

    //todo 每次打开gui进度条都会从0开始
    public void update()
    {
        boolean flag = checkFormula(this.inventory.subList(0, 4), this.inventory.get(4));
        boolean flag1 = world.getBlockState(pos).getProperties().getOrDefault(BlockMagicCircle.OPEN, false).equals(true);
        boolean flag2 = hasSlotForCook();

        if (!this.isCooking() && flag && flag2) {
            cookBegin();
        }
        else if (this.isCooking() && flag2) {
            if (!flag1)
                BlockMagicCircle.setState(true, world, pos);

            this.cookTime++;
            if (this.cookTime >= this.totalCookTime) {
                cookFinish();
            }

        }
        else if (!this.isCooking() && flag1){
            BlockMagicCircle.setState(false, world, pos);
        }

        if (this.isCooking() && this.world.isRemote) {
            tick++;
            double d0 = pos.getX() + 0.5 + random.nextDouble() - 0.5;
            double d1 = pos.getY() + 0.5;
            double d2 = pos.getZ() + 0.5 + random.nextDouble() - 0.5;
            if (tick == 4) {
                ParticleSpawner.spawnParticle(EnumCustomParticles.STAR, d0, d1, d2, 0, 0, 0);
                tick = 0;
            }
        }
        markDirty();
    }

    public int getCookTime(ItemStack input)
    {
        return cookTimeMap.getOrDefault(input.getItem(), -1);
    }

    public float getProgress()
    {
        if (totalCookTime <= 0)
            return 0;
        return cookTime * 1.0f / totalCookTime;
    }

    public void cookBegin() {

        for (ItemStack stack : inventory.subList(0, 4)) {
            stack.shrink(1);
        }

        this.totalCookTime = getCookTime(inventory.get(4)); //todo 要随着不同的配方修改
        this.markDirty();
    }

    public void cookFinish()
    {
        this.cookTime = 0;
        this.totalCookTime = -1;

        List<Item> items = formula.get(this.inventory.get(4).getItem());
        if (items != null) {
            for (int i = 0; i < 3 && i < items.size(); i++) {
                this.inventory.set(5 + i, new ItemStack(items.get(i)));
            }
            this.inventory.get(4).shrink(1);
        }

        this.markDirty();
//        System.out.println("cook finish");
    }

    @SideOnly(Side.CLIENT)
    public static boolean isCooking(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }

    public boolean hasSlotForCook() {
        for (int i = 5; i < 8; i++) if (!this.inventory.get(i).isEmpty())
            return false;
        return true;
    }

    public static boolean checkFormula(List<ItemStack> raw, ItemStack input) {

        int count = 0;
        for (ItemStack stack : raw) {
            if (!stack.isEmpty())
                count++;
        }
        return count == 4 && !input.isEmpty();
    }
}
