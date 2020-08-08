package com.xwm.magicmaid.entity.mob.maid;

import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeapon;
import com.xwm.magicmaid.entity.mob.weapon.EntityMaidWeaponPandorasBox;
import com.xwm.magicmaid.enumstorage.EnumAttackType;
import com.xwm.magicmaid.enumstorage.EnumEquipment;
import com.xwm.magicmaid.object.item.ItemWeapon;
import net.minecraft.world.World;

public class EntityMagicMaidSelina extends EntityMagicMaid
{
    public EntityMagicMaidSelina(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit(){
        super.entityInit();
    }

    @Override
    public void initEntityAI() {
        super.initEntityAI();
    }

    @Override
    public int getAttackDamage(EnumAttackType type){
        return 1;
    }

    @Override
    public void onUpdate() {

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
                pandorasBox.setPosition(posX, posY + height, posZ);
                world.spawnEntity(pandorasBox);
                this.setWeaponID(pandorasBox.getUniqueID());
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


}
