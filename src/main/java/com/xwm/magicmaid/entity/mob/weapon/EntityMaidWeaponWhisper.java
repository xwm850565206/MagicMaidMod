package com.xwm.magicmaid.entity.mob.weapon;

import com.xwm.magicmaid.registry.MagicEquipmentRegistry;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityMaidWeaponWhisper extends EntityMaidWeapon
{
    private static final DataParameter<Boolean> ISATTACK = EntityDataManager.<Boolean>createKey(EntityMaidWeaponWhisper.class, DataSerializers.BOOLEAN);
    public int tick = 0; //动画控制

    public EntityMaidWeaponWhisper(World worldIn) {
        super(worldIn);
        equipmentAttribute = MagicEquipmentRegistry.WHISPER;

    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(ISATTACK, false);

    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        //客户端操作就行
        if (world.isRemote) {
            this.tick++;
            if (this.tick == 360)
                this.tick = 0;

            if (isAttack()) {
                //todo 环绕粒子效果
            }
        }
    }

    public void setAttack(boolean isAttack)
    {
        this.dataManager.set(ISATTACK, isAttack);
    }

    public boolean isAttack(){
        return dataManager.get(ISATTACK);
    }
}
