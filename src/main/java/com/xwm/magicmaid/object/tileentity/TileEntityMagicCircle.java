package com.xwm.magicmaid.object.tileentity;

import com.google.common.collect.Lists;
import com.xwm.magicmaid.object.block.BlockMagicCircle;
import com.xwm.magicmaid.particle.EnumCustomParticles;
import com.xwm.magicmaid.particle.ParticleSpawner;
import com.xwm.magicmaid.registry.MagicFormulaRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class TileEntityMagicCircle extends TileEntity implements IItemHandlerModifiable, ITickable
{
    private ItemStackHandler inventory = new ItemStackHandler(8);
//    private NonNullList<ItemStack> inventory = NonNullList.withSize(8, ItemStack.EMPTY);
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
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        this.insertItem(slot, stack, world.isRemote);
    }

    @Override
    public int getSlots() {
        return 8;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return this.inventory.getStackInSlot(slot);
    }


    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return this.inventory.insertItem(slot, stack, simulate);
    }


    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return this.inventory.extractItem(slot, amount, simulate);
    }

    /**
     * Retrieves the maximum stack size allowed to exist in the given slot.
     *
     * @param slot Slot to query.
     * @return The maximum stack size allowed in the slot.
     */
    @Override
    public int getSlotLimit(int slot) {
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
     *
     * @param slot  Slot to query for validity
     * @param stack Stack to test with for validity
     * @return true if the slot can insert the ItemStack, not considering the current state of the inventory.
     * false if the slot can never insert the ItemStack in any situation.
     */
    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return this.inventory.isItemValid(slot, stack);
    }


    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        if (!compound.hasKey("ReadInventory") || compound.getBoolean("ReadInventory")) {
            this.inventory = new ItemStackHandler(8);
            this.inventory.deserializeNBT(compound.getCompoundTag("inventory"));
        }
        this.cookTime = compound.getInteger("CookTime");
        this.totalCookTime = compound.getInteger("CookTimeTotal");

        if (compound.hasKey("CustomName", 8))
            this.setCustomName(compound.getString("CustomName"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        if (!compound.hasKey("ReadInventory") || compound.getBoolean("ReadInventory")) {
            compound.setTag("inventory", this.inventory.serializeNBT());
        }
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
                inventory.getStackInSlot(0),
                inventory.getStackInSlot(1),
                inventory.getStackInSlot(2),
                inventory.getStackInSlot(3)), this.inventory.getStackInSlot(4));
        boolean flag1 = world.getBlockState(pos).getProperties().getOrDefault(BlockMagicCircle.OPEN, false).equals(true);
        boolean flag2 = hasSlotForCook();
        boolean flag3 = false; // should markDirty

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
        this.totalCookTime = getCookTime(this.inventory.getStackInSlot(4));
    }

    public void cookFinish()
    {
        List<ItemStack> itemstacks = MagicFormulaRegistry.getResult(this.inventory.getStackInSlot(4));
        if (itemstacks != null) {
            for (int i = 0; i < 3 && i < itemstacks.size(); i++) {
                this.inventory.insertItem(5 + i, itemstacks.get(i).copy(), world.isRemote);
            }
            for (int i = 0; i < 5; i++)
                this.inventory.extractItem(i, 1, world.isRemote);
        }

        this.cookTime = 0;
        this.totalCookTime = -1;
    }

    @SideOnly(Side.CLIENT)
    public static boolean isCooking(IInventory inventory)
    {
        return inventory.getField(1) >= 0;
    }

    public boolean hasSlotForCook() {
        for (int i = 5; i < 8; i++) if (!this.inventory.getStackInSlot(i).isEmpty())
            return false;
        return true;
    }

    public static boolean checkFormula(List<ItemStack> raw, ItemStack input) {

        Formula formula = Formula.create(input, raw);
        Formula formula1 = MagicFormulaRegistry.getFormula(input);
        if (formula1 == Formula.EMPTY)
            return false;
        else return formula.equals(formula1);
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
    public NBTTagCompound getUpdateTag()
    {
        NBTTagCompound compound = super.getUpdateTag();
        compound = writeToNBT(compound);
        return compound;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag)
    {
        this.readFromNBT(tag);
    }


    @Override
    public SPacketUpdateTileEntity getUpdatePacket(){
        NBTTagCompound nbtTag = new NBTTagCompound();
        nbtTag = this.writeToNBT(nbtTag);
        return new SPacketUpdateTileEntity(getPos(), -1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt){
        NBTTagCompound tag = pkt.getNbtCompound();
        this.readFromNBT(tag);
    }
}
