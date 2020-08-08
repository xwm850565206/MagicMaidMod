package com.xwm.magicmaid.entity.mob.maid;

import com.xwm.magicmaid.entity.ai.rett.EntityAIDemonKillerAttack;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumRettState;
import com.xwm.magicmaid.object.item.ItemWeapon;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityMagicMaidRett extends EntityMagicMaid
{
    private static final DataParameter<Integer> PERFORMTICK = EntityDataManager.<Integer>createKey(EntityMagicMaidRett.class, DataSerializers.VARINT);

    public EntityMagicMaidRett(World worldIn) {
        super(worldIn);

    }

    @Override
    protected void entityInit(){
        super.entityInit();
        this.dataManager.register(PERFORMTICK, 0);
    }

    @Override
    public void initEntityAI(){
        super.initEntityAI();
        this.targetTasks.addTask(3, new EntityAIDemonKillerAttack(this));
    }

    @Override
    public int getAttackDamage(EnumAttackType type){
        return 50;
    }

    public void onUpdate()
    {
        if (!world.isRemote){
            EnumRettState state = EnumRettState.valueOf(this.getState());
            EnumMode mode = EnumMode.valueOf(this.getMode());
            EnumEquipment equipment = EnumEquipment.valueOf(this.getWeaponType());
            if (equipment == EnumEquipment.DEMONKILLINGSWORD
                    && mode == EnumMode.FIGHT && !isAttackState()){
                this.setState(EnumRettState.toInt(EnumRettState.DEMON_KILLER_STANDARD)); // todo 有两把武器不能这么判断
            }
            else if (equipment == EnumEquipment.NONE)
            {
                if (mode == EnumMode.SITTING && state != EnumRettState.SITTING)
                    this.setState(EnumRettState.toInt(EnumRettState.SITTING));
                else if (mode == EnumMode.SERVE && state != EnumRettState.SERVE)
                    this.setState(EnumRettState.toInt(EnumRettState.SERVE));
            }
        }
        super.onUpdate();
    }

    public void getWeapon(ItemWeapon weapon){
        this.setWeaponType(EnumEquipment.toInt(weapon.enumEquipment));
    }

    public void loseWeapon(ItemWeapon weapon){
        this.setWeaponType(EnumEquipment.toInt(EnumEquipment.NONE));
    }

    public void switchMode(){
        this.setMode((this.getMode() + 1) % 3);
    }

    private boolean isAttackState(){
        EnumRettState state = EnumRettState.valueOf(this.getState());
        return state == EnumRettState.DEMON_KILLER_ATTACK1
                || state == EnumRettState.DEMON_KILLER_ATTACK2
                || state == EnumRettState.DEMON_KILLER_ATTACK3
                || state == EnumRettState.DEMON_KILLER_ATTACK4;
    }

    public int getPerformTick(){
        return this.dataManager.get(PERFORMTICK);
    }

    public void setPerformtick(int performtick){
        this.dataManager.set(PERFORMTICK, performtick);
    }

    public void debug(){
        System.out.println("state: " + EnumRettState.valueOf(this.getState())
                + " mode: " + EnumMode.valueOf(this.getMode())
                + " owner: " + this.hasOwner() + " Equipment: " + EnumEquipment.valueOf(this.getWeaponType()));
    }
}
