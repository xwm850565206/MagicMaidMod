package com.xwm.magicmaid.entity.ai.strawberry;

import com.xwm.magicmaid.entity.maid.EntityMagicMaid;
import net.minecraft.entity.ai.EntityAIBase;

//todo
public class EntityAIRepantence extends EntityAIBase
{
    private EntityMagicMaid maid;

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute() {
        return false;
    }
}
