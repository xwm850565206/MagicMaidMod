package com.xwm.magicmaid.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class KickOutWindow extends GuiScreen
{
    private final String title;
    private final String message;
    private final int punishLevel;

    public KickOutWindow(String title, String message, int punishLevel)
    {
        this.title = title;
        this.message = message;
        this.punishLevel = punishLevel;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        super.initGui();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, 140, "我知错了"));
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawGradientRect(0, 0, this.width, this.height, -12574688, -11530224);
        this.drawCenteredString(this.fontRenderer, this.title, this.width / 2, 90, 16777215);
        this.drawCenteredString(this.fontRenderer, this.message, this.width / 2, 110, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    }


    protected void actionPerformed(GuiButton button){
        if ((punishLevel & 2) == 2)
            throw new RuntimeException("检测到你使用过强mod进攻女仆，崩溃你的客户端");
        if ((punishLevel & 4) == 4){
            Minecraft.getMinecraft().player.setGameType(GameType.ADVENTURE);
        }
    }
}
