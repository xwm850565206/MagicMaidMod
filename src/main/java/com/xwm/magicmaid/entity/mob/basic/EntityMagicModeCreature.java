package com.xwm.magicmaid.entity.mob.basic;

import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityMultiModeCreature;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.object.item.equipment.EquipmentAttribute;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityMagicModeCreature extends AbstractEntityMagicCreature implements IEntityMultiModeCreature
{
    private static final DataParameter<Integer> MODE = EntityDataManager.<Integer>createKey(EntityMagicModeCreature.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> STATE = EntityDataManager.<Integer>createKey(EntityMagicModeCreature.class, DataSerializers.VARINT);

    public EntityMagicModeCreature(World worldIn) {
        super(worldIn);
    }

    public boolean isEnemy(EntityLivingBase entityLivingBase)
    {
        boolean flag = super.isEnemy(entityLivingBase);
        if (!flag)
            return false;
        else
        {
            if (EnumMode.valueOf(this.getMode()) != EnumMode.BOSS && EnumMode.valueOf(this.getMode()) != EnumMode.FIGHT)
                return false;
            return true;
        }
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(MODE, EnumMode.toInt(EnumMode.SITTING));
        this.dataManager.register(STATE, 0); //0-standard

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("mode", this.getMode());
        compound.setInteger("state", this.getState());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setMode(compound.getInteger("mode"));
        this.setState(compound.getInteger("state"));
    }

    @Override
    public int getAttackDamage(EquipmentAttribute type) {
        return 1;
    }

    @Override
    public int getAttackColdTime(EquipmentAttribute type) {
        return 20;
    }

    /**
     * 设置模式
     *
     * @param mode
     */
    @Override
    public void setMode(int mode) {
        this.dataManager.set(MODE, mode);
    }

    /**
     * 得到当前模式
     *
     * @return
     */
    @Override
    public int getMode() {
        return this.dataManager.get(MODE);
    }

    /**
     * 转换模式
     */
    @Override
    public void switchMode() {
        this.setMode((this.getMode() + 1) % 3); //默认3个模式
    }

    /**
     * 得到当前状态
     *
     * @return
     */
    @Override
    public int getState() {
        return this.dataManager.get(STATE);
    }

    /**
     * 设置当前状态
     *
     * @param state
     */
    @Override
    public void setState(int state) {
        this.dataManager.set(STATE, state);
    }
}
