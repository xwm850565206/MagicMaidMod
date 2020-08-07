package com.xwm.magicmaid.entity.mob.maid;

import com.xwm.magicmaid.entity.ai.martha.EntityAIConviction;
import com.xwm.magicmaid.entity.ai.martha.EntityAIRepantence;
import com.xwm.magicmaid.entity.ai.martha.EntityAIMarthaServe;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.object.item.ItemWeapon;
import net.minecraft.world.World;

public class EntityMagicMaidMartha extends EntityMagicMaid
{
    //state: 0-草莓标准站立 1-草莓服侍站立 2-草莓待命站立 3-草莓攻击

    public EntityMagicMaidMartha(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit(){
        super.entityInit();

    }

    public void initEntityAI(){
        super.initEntityAI();
        this.targetTasks.addTask(3, new EntityAIMarthaServe(this));
        this.targetTasks.addTask(3, new EntityAIRepantence(this));
        this.targetTasks.addTask(3, new EntityAIConviction(this));
    }

    @Override
    public int getAttackDamage(EnumAttackType type){
        return 50;
    }

    @Override
    public void onUpdate()
    {
        if (!world.isRemote){
            EnumMode mode = EnumMode.valueOf(this.getMode());
            if (mode == EnumMode.SERVE && this.getState() != 1)
                this.setState(1);
            else if (mode == EnumMode.SITTING && this.getState() != 2)
                this.setState(2);
        }

        super.onUpdate();
    }

    public void getWeapon(ItemWeapon weapon){

        if (this.world.isRemote)
            return;

        createWeapon(EnumEquipment.toInt(weapon.enumEquipment),
                EnumEquipment.toEntityMaidWeapon(weapon.enumEquipment, world));

        //todo 得到武器后要进行一系列的操作来维护
    }

    public void loseWeapon(ItemWeapon weapon){

        if (this.world.isRemote)
            return;

        try {
            EntityMaidWeapon weapon1 = EntityMaidWeapon.getWeaponFromUUID(world, getWeaponID());
            weapon1.setDead();
            setWeaponID(null);
            setWeaponType(0);
        } catch (Exception e){
            ; //可能武器被其他模组杀死了
        }
        //todo 失去武器也要修改一系列数据来维护
    }


    public void createWeapon(int weaponType, EntityMaidWeapon weapon)
    {
        if (weapon == null)
            return;
        weapon.setMaid(this);
        weapon.setPosition(this.posX, this.posY, this.posZ);
        this.setWeaponID(weapon.getUniqueID());
        this.setWeaponType(weaponType);
        this.world.spawnEntity(weapon);
    }

    public void switchMode(){
        this.setMode((this.getMode() + 1) % 3);
    }
}
