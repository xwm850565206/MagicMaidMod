package com.xwm.magicmaid.util.handlers;

import com.xwm.magicmaid.entity.mob.maid.EntityMagicMaid;
import com.xwm.magicmaid.gui.*;
import com.xwm.magicmaid.gui.magiccircle.ContainerMagicCircle;
import com.xwm.magicmaid.gui.magiccircle.GuiMagicCircle;
import com.xwm.magicmaid.gui.maidwindow.ContainerMaidWindow;
import com.xwm.magicmaid.gui.maidwindow.GuiMaidWindow;
import com.xwm.magicmaid.gui.player.ContainerPlayerMenuMain;
import com.xwm.magicmaid.gui.player.GuiPlayerMenuMain;
import com.xwm.magicmaid.object.tileentity.TileEntityMagicCircle;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GuiHandler implements IGuiHandler
{

    public static GuiHandler maidWindowHandler = new GuiHandler();

    /**
     * Returns a Server side Container to be displayed to the user.
     *
     * @param ID     The Gui ID Number
     * @param player The player viewing the Gui
     * @param world  The current world
     * @param x      X Position
     * @param y      Y Position
     * @param z      Z Position
     * @return A GuiScreen/Container to be displayed to the user, null if none.
     */
    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == Reference.GUI_MAID_WINDOW)
        {
            List<EntityMagicMaid> entities = world.getEntitiesWithinAABB(EntityMagicMaid.class, new AxisAlignedBB(x, y, z, x, y, z).grow(1));
            if (entities.size() < 1)
                return null;
            Collections.sort(entities, new Sorter(player));
            EntityMagicMaid maid = entities.get(0);
            return new ContainerMaidWindow(player.inventory, maid);
        }
        else if (ID == Reference.GUI_MAID_MEMORY) {
            ;
        }
        else if (ID == Reference.GUI_MAGIC_CIRCLE) {
            return new ContainerMagicCircle(player.inventory, (TileEntityMagicCircle) world.getTileEntity(new BlockPos(x, y, z)));
        }
        else if (ID == Reference.GUI_INSTRUCTION_BOOK) {
            ;
        }
        else if (ID == Reference.GUI_PLAYER_MENU_MAIN) {
            return  new ContainerPlayerMenuMain(player.inventory);
        }

        return null;
    }

    /**
     * Returns a Container to be displayed to the user. On the client side, this
     * needs to return a instance of GuiScreen On the server side, this needs to
     * return a instance of Container
     *
     * @param ID     The Gui ID Number
     * @param player The player viewing the Gui
     * @param world  The current world
     * @param x      X Position
     * @param y      Y Position
     * @param z      Z Position
     * @return A GuiScreen/Container to be displayed to the user, null if none.
     */
    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {

        if (ID == Reference.GUI_MAID_WINDOW)
        {
            List<EntityMagicMaid> entities = world.getEntitiesWithinAABB(EntityMagicMaid.class, new AxisAlignedBB(x, y, z, x, y, z).grow(1));
            if (entities.size() < 1)
                return null;

            Collections.sort(entities, new Sorter(player));
            EntityMagicMaid maid = entities.get(0);
            return new GuiMaidWindow(new ContainerMaidWindow(player.inventory, maid), player.inventory, maid);
        }
        else if (ID == Reference.GUI_MAID_MEMORY)
        {
            return new GuiShowMemory(true, x);
        }
        else if (ID == Reference.GUI_MAGIC_CIRCLE)
        {
            return new GuiMagicCircle((Container) getServerGuiElement(ID, player, world, x, y, z), (TileEntityMagicCircle) world.getTileEntity(new BlockPos(x, y, z)));
        }
        else if (ID == Reference.GUI_INSTRUCTION_BOOK) {
            return new GuiInstructionBook();
        }
        else if (ID == Reference.GUI_PLAYER_MENU_MAIN) {
            return new GuiPlayerMenuMain(new ContainerPlayerMenuMain(player.inventory), player);
        }

        return null;
    }

    public static class Sorter implements Comparator<Entity>
    {
        private final Entity entity;

        public Sorter(Entity entityIn)
        {
            this.entity = entityIn;
        }

        public int compare(Entity p_compare_1_, Entity p_compare_2_)
        {
            double d0 = this.entity.getDistanceSq(p_compare_1_);
            double d1 = this.entity.getDistanceSq(p_compare_2_);

            if (d0 < d1)
            {
                return -1;
            }
            else
            {
                return d0 > d1 ? 1 : 0;
            }
        }
    }
}
