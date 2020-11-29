package com.xwm.magicmaid.object.tileentity;

import com.google.common.collect.Lists;
import com.xwm.magicmaid.object.block.BlockMagicCircle;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.particle.ParticleSpawner;
import com.xwm.magicmaid.registry.MagicFormulaRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class TileEntityMagicCircle extends TileEntity implements IInventory, ITickable
{
//    private ItemStackHandler inventory = new ItemStackHandler(8);
    private NonNullList<ItemStack> inventory = NonNullList.withSize(8, ItemStack.EMPTY);
    private String customName;

    private int cookTime = 0;
    private int totalCookTime = -1;
    private Random random = new Random();

    public String getName()
    {
        return this.hasCustomName() ? this.customName : "container.magic_circle";
    }

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
    public int getSizeInventory() {
        return 8;
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.get(index);
    }


    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.inventory, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.inventory, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index >= 0 && index < this.inventory.size())
        {
            this.inventory.set(index, stack);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }


    /**
     * <p>
     * This function re-implements the vanilla function {@link IInventory#isItemValidForSlot(int, ItemStack)}.
     * It should be used instead of simulated insertions in cases where the contents and state of the inventory are
     * irrelevant, mainly for the purpose of automation and logic (for instance, testing if a minecart can wait
     * to deposit its items into a full inventory, or if the items in the minecart can never be placed into the
     * inventory and should move on).
     * </p>
     * <ul>
     * <li>isItemValid is false when insertion of the item is never valid.</li>
     * <li>When isItemValid is true, no assumptions can be made and insertion must be simulated case-by-case.</li>
     * <li>The actual items in the inventory, its fullness, or any other state are <strong>not</strong> considered by isItemValid.</li>
     * </ul>
     */
    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        this.inventory = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.inventory);
//        if (!compound.hasKey("ReadInventory") || compound.getBoolean("ReadInventory")) {
//            this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
//        }
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");

        if (compound.hasKey("CustomName", 8))
            this.setCustomName(compound.getString("CustomName"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        ItemStackHelper.saveAllItems(compound, this.inventory);

//        if (!compound.hasKey("ReadInventory") || compound.getBoolean("ReadInventory")) {
//            compound.setTag("inventory", this.inventory.serializeNBT());
//        }
        compound.setInteger("CookTime", this.cookTime);
        compound.setInteger("CookTimeTotal", this.totalCookTime);

        if (this.hasCustomName())
            compound.setString("CustomName", this.customName);
        return compound;
    }


    public boolean isCooking()
    {
        return this.totalCookTime > 0;
    }

    private int tick = 0; // 控制粒子效果

    @Override
    public void update()
    {
        boolean flag = checkFormula(Lists.newArrayList(
                inventory.get(0),
                inventory.get(1),
                inventory.get(2),
                inventory.get(3)), this.inventory.get(4));
        boolean flag1 = world.getBlockState(pos).getProperties().getOrDefault(BlockMagicCircle.OPEN, false).equals(true);
        boolean flag2 = hasSlotForCook();
        boolean flag3 = false; // should markDirty

        if (!flag) {
            totalCookTime = -1;
        }

        if (this.isCooking() && flag && flag2) {
            this.cookTime++;
        }

        if (!this.isCooking() && flag && flag2)
        {
            cookBegin();
            flag3 = true;
        }
        else if (flag2 && this.totalCookTime >= 0 && this.cookTime >= this.totalCookTime)
        {
//            if (!world.isRemote)
            cookFinish();
            flag3 = true;
        }

        if (flag1 != this.isCooking()) {
            BlockMagicCircle.setState(this.isCooking(), world, pos);
            flag3 = true;
        }

        if (flag3 && !world.isRemote) {
            markDirty();
        }

        if (flag1 && this.world.isRemote) {
            tick++;
            double d0 = pos.getX() + 0.5 + random.nextDouble() - 0.5;
            double d1 = pos.getY() + 0.5;
            double d2 = pos.getZ() + 0.5 + random.nextDouble() - 0.5;
            if (tick == 4) {
                ParticleSpawner.spawnParticle(EnumCustomParticles.STAR, d0, d1, d2, 0, 0, 0);
                tick = 0;
            }
        }
    }

    public int getCookTime(ItemStack input) {
        return MagicFormulaRegistry.getCookTime(input);
    }

    public float getProgress()
    {
        if (totalCookTime <= 0)
            return 0;
        return cookTime * 1.0f / totalCookTime;
    }

    public void cookBegin() {
        this.cookTime = 0;
        this.totalCookTime = getCookTime(this.inventory.get(4));
    }

    public void cookFinish()
    {
        Result result = MagicFormulaRegistry.getResult(this.inventory.get(4));
        List<ItemStack> itemstacks = result.getResult(this.inventory.get(4));

        if (itemstacks != null) {
            for (int i = 0; i < 3 && i < itemstacks.size(); i++) {
                ItemStack stack = this.getStackInSlot(5 + i);
                if (stack != ItemStack.EMPTY && stack.getItem() == itemstacks.get(i).getItem())
                    stack.grow(itemstacks.get(i).getCount());
                else
                    stack = itemstacks.get(i).copy();
                this.setInventorySlotContents(5 + i, stack);
            }
            for (int i = 0; i < 5; i++)
                this.decrStackSize(i, 1);
        }

        this.cookTime = 0;
        this.totalCookTime = -1;
    }


    public boolean hasSlotForCook() {
        Result result = MagicFormulaRegistry.getResult(this.inventory.get(4));
        List<ItemStack> itemstacks = result.getResult(this.inventory.get(4));

        for (int i = 5; i < 8; i++)
        {
            if (!this.inventory.get(i).isEmpty())
            {
                if ((itemstacks.size() + 5 > i && this.getStackInSlot(i).getItem() == itemstacks.get(i - 5).getItem()))
                    continue;
                else
                    return false;
            }
        }
        return true;
    }

    public static boolean checkFormula(List<ItemStack> raw, ItemStack input) {

        Formula formula = Formula.create(input, raw);
        Formula formula1 = MagicFormulaRegistry.getFormula(input);
        if (formula1 == Formula.EMPTY)
            return false;
        else return formula1.equals(formula);
    }

    @Override
    public void markDirty()
    {
        super.markDirty();
        NBTTagCompound compound = new NBTTagCompound();
        writeToNBT(compound);
        SPacketUpdateTileEntity packet = new SPacketUpdateTileEntity(pos, -1, compound);
        List<EntityPlayer> players = world.playerEntities;
        for (EntityPlayer player : players) {
            if (player instanceof EntityPlayerMP)
                ((EntityPlayerMP) player).connection.sendPacket(packet);
        }
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        this.inventory.clear();
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, 5, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        super.handleUpdateTag(tag);
//        this.readFromNBT(tag);
    }
//

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
        NBTTagCompound tag = pkt.getNbtCompound();
        this.readFromNBT(tag);
    }
}
