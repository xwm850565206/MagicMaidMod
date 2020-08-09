package com.xwm.magicmaid.entity.mob.maid;

import com.xwm.magicmaid.entity.ai.EntityAIMaidAttackMelee;
import com.xwm.magicmaid.entity.ai.selina.EntityAIPandora;
import com.xwm.magicmaid.entity.ai.selina.EntityAIWhisper;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponWhisper;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.enumstorage.EnumMode;
import com.xwm.magicmaid.enumstorage.EnumSelineState;
import com.xwm.magicmaid.object.item.ItemWeapon;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EntityMagicMaidSelina extends EntityMagicMaid
{
    public EntityMagicMaidSelina(World worldIn) {
        super(worldIn);
        weaponStandbyPos = new BlockPos(1, 0.5, 0);
    }

    @Override
    protected void entityInit(){
        super.entityInit();
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();

        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, true));


//        this.tasks.addTask(2, new EntityAIMaidAttackMelee(this, 1.3D, false));
        this.tasks.addTask(3, new EntityAIPandora(this));
        this.tasks.addTask(3, new EntityAIWhisper(this));


    }

    @Override
    public int getAttackDamage(EnumAttackType type){

        return 1;
    }

    @Override
    public void onUpdate() {

        if (!world.isRemote)
        {
            EnumSelineState state = EnumSelineState.valueOf(this.getState());
            EnumMode mode = EnumMode.valueOf(this.getMode());
            if (mode == EnumMode.SITTING && state != EnumSelineState.SITTING)
                this.setState(EnumSelineState.toInt(EnumSelineState.SITTING));
            else if (mode == EnumMode.SERVE && state != EnumSelineState.SERVE)
                this.setState(EnumSelineState.toInt(EnumSelineState.SERVE));
            else if (mode== EnumMode.BOSS && (state == EnumSelineState.SITTING || state == EnumSelineState.SERVE))
                this.setState(EnumSelineState.toInt(EnumSelineState.STANDARD));
        }

        super.onUpdate();
    }

    public void getWeapon(ItemWeapon weapon){
        if (this.world.isRemote)
            return;

        EnumEquipment equipment = weapon.enumEquipment;
        switch (equipment){
            case PANDORA:
                EntityMaidWeaponPandorasBox pandorasBox = new EntityMaidWeaponPandorasBox(world);
                pandorasBox.setMaid(this);
                pandorasBox.setPosition(posX, posY + height + 1, posZ);
                world.spawnEntity(pandorasBox);
                this.setWeaponID(pandorasBox.getUniqueID());
            case WHISPER:
                EntityMaidWeaponWhisper whisper = new EntityMaidWeaponWhisper(world);
                whisper.setMaid(this);
                whisper.setPosition(posX, posY + height + 1, posZ);
                world.spawnEntity(whisper);
                this.setWeaponID(whisper.getUniqueID());
        }
        this.setWeaponType(EnumEquipment.toInt(equipment));

    }

    public void loseWeapon(ItemWeapon weapon){
        try {
            EntityMaidWeapon.getWeaponFromUUID(world, this.getWeaponID()).setDead();
        }catch (Exception e){
            ;
        }
        this.setWeaponType(EnumEquipment.toInt(EnumEquipment.NONE));
        this.setWeaponID(null);
    }


    public void switchMode(){
        this.setMode((this.getMode() + 1) % 3);
    }

}
