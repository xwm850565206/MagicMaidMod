package com.xwm.magicmaid.util.handlers;

import com.xwm.magicmaid.entity.maid.EntityMagicMaid;
import com.xwm.magicmaid.gui.ContainMaidWindow;
import com.xwm.magicmaid.gui.GuiMaidWindow;
import com.xwm.magicmaid.util.Reference;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;
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
            List<EntityMagicMaid> entities = world.getEntitiesWithinAABB(EntityMagicMaid.class, new AxisAlignedBB(x, y, z, x, y, z).grow(0.5));
            if (entities.size() < 1)
                return null;

            EntityMagicMaid maid = entities.get(0);
            return new ContainMaidWindow(player.inventory, maid);
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
            return new GuiMaidWindow((Container) getServerGuiElement(ID, player, world, x, y, z), player.inventory);
        }
        return null;
    }
}
