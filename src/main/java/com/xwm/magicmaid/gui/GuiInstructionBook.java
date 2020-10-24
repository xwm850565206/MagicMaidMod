package com.xwm.magicmaid.gui;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;
import com.xwm.magicmaid.util.Reference;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GuiInstructionBook extends GuiScreen
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation("minecraft:textures/gui/book.png");
    private final int bookImageWidth = 192;
    private final int bookImageHeight = 192;
    private int bookTotalPages = 1;
    private int currPage;
    private List<String> bookPages;
    private String bookTitle = "";
    private List<ITextComponent> cachedComponents;
    private int cachedPage = -1;
    private GuiInstructionBook.NextPageButton buttonNextPage;
    private GuiInstructionBook.NextPageButton buttonPreviousPage;

    public GuiInstructionBook() {
        bookPages = new ArrayList<>();
    }

    public void initGui()
    {
        this.buttonList.clear();
        int i = (this.width - 192) / 2;
        int j = 2;
        this.buttonNextPage = this.addButton(new GuiInstructionBook.NextPageButton(0, i + 120, 156, true));
        this.buttonPreviousPage = this.addButton(new GuiInstructionBook.NextPageButton(1, i + 38, 156, false));

        IResource iresource = null;
        String filename = "texts/instructions.txt";
        try{
            iresource = mc.getResourceManager().getResource(new ResourceLocation(Reference.MODID, filename));
            InputStream inputstream = iresource.getInputStream();
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
            StringBuilder s = new StringBuilder();
            String s1;
            while ((s1 = bufferedreader.readLine()) != null)
            {
                if (s1.equals("###")) {
                    bookPages.add(s.toString());
                    s = new StringBuilder();
                }
                else{
                    s.append(s1);
                    s.append('\n');
                }
            }
            if (s.length() > 0)
                bookPages.add(s.toString());

        } catch (IOException e) {
            bookPages.add("...读取说明书失败");
        }

        bookTotalPages = bookPages.size();
    }

    /**
     * Called when the screen is unloaded. Used to disable keyboard repeat events
     */
    public void onGuiClosed()
    {
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
                if (this.currPage < this.bookTotalPages - 1)
                {
                    ++this.currPage;
                }
            }
            else if (button.id == 1)
            {
                if (this.currPage > 0)
                {
                    --this.currPage;
                }
            }
        }
    }


    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
        int i = (this.width - 192) / 2;
        int j = 2;
        this.drawTexturedModalRect(i, 2, 0, 0, 192, 192);

        String s4 = I18n.format("book.pageIndicator", this.currPage + 1, this.bookTotalPages);
        String s5 = "";

        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.size())
        {
            s5 = this.bookPages.get(this.currPage);
        }
        if (this.cachedPage != this.currPage) {

            ITextComponent itextcomponent = new TextComponentString(s5);
            this.cachedComponents = GuiUtilRenderComponents.splitText(itextcomponent, 116, this.fontRenderer, true, true);
            this.cachedPage = this.currPage;
        }

        int j1 = this.fontRenderer.getStringWidth(s4);
        this.fontRenderer.drawString(s4, i - j1 + 192 - 44, 18, 0);

        int k1 = Math.min(128 / this.fontRenderer.FONT_HEIGHT, this.cachedComponents.size());
        for (int l1 = 0; l1 < k1; ++l1)
        {
            ITextComponent itextcomponent2 = this.cachedComponents.get(l1);
            this.fontRenderer.drawString(itextcomponent2.getUnformattedText(), i + 36, 34 + l1 * this.fontRenderer.FONT_HEIGHT, 0);
        }


        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    /**
     * Called when the mouse is clicked. Args : mouseX, mouseY, clickedButton
     */
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }


    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton
    {
        private final boolean isForward;

        public NextPageButton(int buttonId, int x, int y, boolean isForwardIn)
        {
            super(buttonId, x, y, 23, 13, "");
            this.isForward = isForwardIn;
        }

        /**
         * Draws this button to the screen.
         */
        public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks)
        {

            boolean flag = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(GuiInstructionBook.BOOK_GUI_TEXTURES);
            int i = 0;
            int j = 192;

            if (flag)
            {
                i += 23;
            }

            if (!this.isForward)
            {
                j += 13;
            }

            this.drawTexturedModalRect(this.x, this.y, i, j, 23, 13);
        }
    }
}
