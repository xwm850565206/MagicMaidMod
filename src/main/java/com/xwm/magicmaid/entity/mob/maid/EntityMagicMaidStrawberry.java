package com.xwm.magicmaid.entity.mob.maid;

import com.xwm.magicmaid.entity.ai.strawberry.EntityAIConviction;
import com.xwm.magicmaid.entity.ai.strawberry.EntityAIRepantence;
import net.minecraft.world.World;

public class EntityMagicMaidStrawberry extends EntityMagicMaid
{
    //state: 0-草莓标准站立 1-草莓服侍站立 2-草莓开心站立 3-草莓攻击

    public EntityMagicMaidStrawberry(World worldIn) {
        super(worldIn);
    }


    public void initEntityAI(){
        super.initEntityAI();
//        this.targetTasks.addTask(3, new EntityAIStrawberryServe(this));
        this.targetTasks.addTask(3, new EntityAIRepantence(this));
        this.targetTasks.addTask(3, new EntityAIConviction(this));
    }

    @Override
    public int getAttackDamage(EnumAttackTypes type){
        return 50;
    }
}
