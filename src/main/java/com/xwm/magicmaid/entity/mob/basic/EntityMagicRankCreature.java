package com.xwm.magicmaid.entity.mob.basic;

import com.xwm.magicmaid.entity.mob.basic.interfaces.IEntityRankCreature;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityMagicRankCreature extends EntityMagicModeCreature implements IEntityRankCreature
{
    protected static int max_rank = 2; //默认只有3阶 0,1,2
    private static final DataParameter<Integer> LEVEL = EntityDataManager.<Integer>createKey(EntityMagicRankCreature.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> EXP = EntityDataManager.<Integer>createKey(EntityMagicRankCreature.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> RANK = EntityDataManager.<Integer>createKey(EntityMagicRankCreature.class, DataSerializers.VARINT);


    public EntityMagicRankCreature(World worldIn) {
        super(worldIn);
    }


    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(LEVEL, 0);
        this.dataManager.register(EXP, 0);
        this.dataManager.register(RANK, 0);

    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setInteger("level", this.getLevel());
        compound.setInteger("exp", this.getExp());
        compound.setInteger("rank", this.getRank());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.setLevel(compound.getInteger("level"));
        this.setExp(compound.getInteger("exp"));
        this.setRank(compound.getInteger("rank"));
    }


    /**
     * 设置经验值
     *
     * @param exp
     */
    @Override
    public void setExp(int exp) {
        this.dataManager.set(EXP, exp);
    }

    /**
     * 得到经验值
     *
     * @return
     */
    @Override
    public int getExp() {
        return this.dataManager.get(EXP);
    }

    /**
     * todo 增加经验，目前等级系统和阶系统有冲突，还要修改
     */
    @Override
    public void plusExp() {
        int i = getExp() + 1;
        if (i <= 100) {
            this.dataManager.set(EXP, i);
        }
//        else {
//            this.dataManager.set(EXP, 0);
//            this.dataManager.set(LEVEL, getLevel() + 1);
//        }
    }

    /**
     * 设置等级
     *
     * @param level
     */
    @Override
    public void setLevel(int level) {
        this.dataManager.set(LEVEL, level);
    }

    /**
     * 得到等级
     *
     * @return
     */
    @Override
    public int getLevel() {
        return this.dataManager.get(LEVEL);
    }

    /**
     * 设置品阶
     *
     * @param rank
     */
    @Override
    public void setRank(int rank) {
        this.dataManager.set(RANK, rank);
    }

    /**
     * 得到品阶
     *
     * @return
     */
    @Override
    public int getRank() {
        return this.dataManager.get(RANK);
    }

    /**
     * 升阶
     */
    @Override
    public void plusRank() {
        int i = getRank();
        if (i < max_rank)
            this.dataManager.set(RANK, i+1);
    }


    /**
     * 是否达到最大阶
     * @return
     */
    public boolean maxRank() {
        return getRank() >= max_rank;
    }
}
