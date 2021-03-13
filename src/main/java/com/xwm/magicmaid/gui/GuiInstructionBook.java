package com.xwm.magicmaid.gui;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.xwm.magicmaid.enumstorage.EnumInstructElement;
import com.xwm.magicmaid.gui.magiccircle.ContainerMagicCircle;
import com.xwm.magicmaid.gui.magiccircle.GuiMagicCircle;
import com.xwm.magicmaid.object.tileentity.Formula;
import com.xwm.magicmaid.object.tileentity.Result;
import com.xwm.magicmaid.registry.MagicFormulaRegistry;
import com.xwm.magicmaid.registry.MagicModelRegistry;
import com.xwm.magicmaid.util.Reference;
import com.xwm.magicmaid.util.instruction.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.vecmath.Vector2d;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiInstructionBook extends GuiScreen
{
    private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation("minecraft:textures/gui/book.png");
    private final int bookImageWidth = 384;
    private final int bookImageHeight = 216;
    private int bookTotalPages = 1;
    private int currPage;
    private Map<String, Integer> catalogIndexMap; // 储存标题对应的页码
    private List<String> catalogList;  // 储存标题
    private List<List<InstructElement>> bookPages;
    private List<InstructElement> cachedComponents;
    private GuiInstructionBook.NextPageButton buttonNextPage;
    private GuiInstructionBook.NextPageButton buttonPreviousPage;

    public GuiInstructionBook() {
        this.bookPages = new ArrayList<>();
        this.catalogIndexMap = new HashMap<>();
        this.catalogList = new ArrayList<>();

    }

    public void initGui()
    {
        this.buttonList.clear();
        this.bookPages.clear();
        this.catalogIndexMap.clear();
        this.catalogList.clear();
        int i = (this.width - this.bookImageWidth) / 2 + bookImageWidth / 2 - 12;
        this.buttonNextPage = this.addButton(new GuiInstructionBook.NextPageButton(0, i + 50, bookImageHeight - 20, true));
        this.buttonPreviousPage = this.addButton(new GuiInstructionBook.NextPageButton(1, i, bookImageHeight - 20, false));
        initContent();
        for (int t = 0; t < catalogList.size(); t++)
            this.addButton(new GuiButton(2+t, 5, 20 + t * 20, 40, 20, catalogList.get(t)));
        bookTotalPages = bookPages.size();

        this.updateScreen();
    }

    private void initContent()
    {
        IResource iresource = null;
        String filename = "texts/instructions.json";
        Gson gson = new Gson();
        try {
            iresource = mc.getResourceManager().getResource(new ResourceLocation(Reference.MODID, filename));
            InputStream inputstream = iresource.getInputStream();
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream, StandardCharsets.UTF_8));
            StringBuilder s = new StringBuilder();
            String s1;
            while ((s1 = bufferedreader.readLine()) != null) {
                s.append(s1);
                s.append('\n');
             }

            //从json中读入
            catalogIndexMap.put("公告", 0);
            catalogList.add("公告");
            JsonArray jsonArray = gson.fromJson(s.toString(), JsonArray.class);
            for (int i = 0; i < jsonArray.size(); i++)
            {
                JsonArray jsonArray1 = jsonArray.get(i).getAsJsonArray();
                List<InstructElement> instructElements = new ArrayList<>();
                for (int j = 0; j < jsonArray1.size(); j++)
                {
                    RawInstructElement rawInstructElement = gson.fromJson(jsonArray1.get(j), RawInstructElement.class);
                    InstructElement element = InstructElement.create(rawInstructElement, this.bookImageWidth - 60, this.fontRenderer);
                    instructElements.add(element);

                    // 第一次出现的title就添加到目录
                    if (element instanceof TitleElement)
                    {
                        if (!catalogIndexMap.containsKey(element.getNameCached().getFormattedText())){
                            catalogIndexMap.put(element.getNameCached().getFormattedText(), bookPages.size());
                            catalogList.add(element.getNameCached().getFormattedText());
                        }
                    }
                }
                bookPages.add(instructElements);
            }
            // 读取formula 和 recipe
            catalogIndexMap.put("石板公式", bookPages.size());
            catalogList.add("石板公式");
            HashMap<ResourceLocation, Formula> formulaMap = MagicFormulaRegistry.getFormulaMap();
            List<InstructElement> instructElements = new ArrayList<InstructElement>(){{add(new TitleElement(EnumInstructElement.TITLE, new TextComponentString("石板公式")));}};
            int i = 0;
            for (Map.Entry<ResourceLocation, Formula> entry : formulaMap.entrySet()) {
                List<ItemStack> allItems = new ArrayList<ItemStack>(){{
                    addAll(entry.getValue().getAllItems());
                    Result result = MagicFormulaRegistry.getResult(entry.getValue().getKeyItem());
                    List<ItemStack> itemstacks = result.getResult(entry.getValue().getKeyItem());
                    addAll(itemstacks);
                }};

                FormulaElement formulaElement = new FormulaElement(EnumInstructElement.FORMULA, new TextComponentString(""), allItems);
                instructElements.add(formulaElement);
                i++;
                if (i == 4) { //一页4个
                    bookPages.add(instructElements);
                    instructElements = new ArrayList<InstructElement>(){{add(new TitleElement(EnumInstructElement.TITLE, new TextComponentString("石板公式")));}};
                    i = 0;
                }
            }
            if (i > 0)
            {
                bookPages.add(instructElements);
            }

        } catch (IOException e) {
            bookPages.add(Lists.newArrayList(new InstructElement(EnumInstructElement.CONTENT, new TextComponentString("读取说明书出错"))));
            e.printStackTrace();
        } catch (Exception e) {
            bookPages.add(Lists.newArrayList(new InstructElement(EnumInstructElement.CONTENT, new TextComponentString("一个未知错误导致读取说明书失败"))));
            e.printStackTrace();
        }
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
            else {
                this.currPage = catalogIndexMap.get(catalogList.get(button.id - 2));
            }
        }
    }


    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        int i = 20 + (this.width - this.bookImageWidth) / 2;
        int j = 2;

        //画背景
        GlStateManager.pushMatrix();
        GlStateManager.color(0F, 0F, 0F, 0.4F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.drawTexturedModalRect(i, j, 0, 0, this.bookImageWidth, this.bookImageHeight);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();

        //画页码
        String s4 = I18n.format("book.pageIndicator", this.currPage + 1, this.bookTotalPages);
        int j1 = this.fontRenderer.getStringWidth(s4);
        this.fontRenderer.drawString(s4, i - j1 + 192, 18, 0xFFFFFF);

        //画内容
        if (this.bookPages != null && this.currPage >= 0 && this.currPage < this.bookPages.size())
        {
            this.cachedComponents = this.bookPages.get(this.currPage);
        }
        try {
            drawPage(cachedComponents, i + 10, j);
        } catch (Exception e) {
            ;
        }


        //画按钮
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private void drawPage(List<InstructElement> instructElements, int x, int y)
    {
        int offset = y + 10;
        for (int i = 0; i < instructElements.size(); i++)
        {
            InstructElement instructElement = instructElements.get(i);
            if (i + 1 < instructElements.size() && (instructElement.type == EnumInstructElement.FORMULA || instructElement.type == EnumInstructElement.RECIPE))
            {
                i++;
                InstructElement instructElement1 = instructElements.get(i);
                drawInstructionEntry(instructElement, x, offset);
                offset = drawInstructionEntry(instructElement1, x + (int)(220 * 0.8 + 10), offset) + 3;

            }
            else
                offset = drawInstructionEntry(instructElement, x, offset) + 3;
        }
    }

    private int drawInstructionEntry(InstructElement instructElement, int x, int y)
    {
        EnumInstructElement enumInstructElement = instructElement.type;
//        RenderHelper.enableGUIStandardItemLighting();
//        RenderHelper.enableStandardItemLighting();
        switch (enumInstructElement)
        {
            case NONE:
                break;
            case CONTENT:
                ContentElement contentElement = (ContentElement) instructElement;
                this.fontRenderer.drawString("[" + contentElement.nameCached.getUnformattedText() + "]", x, y + this.fontRenderer.FONT_HEIGHT, 0xFFFFFF);
                for (int l1 = 0; l1 < contentElement.description.size(); ++l1)
                {
                    ITextComponent itextcomponent2 = contentElement.description.get(l1);
                    this.fontRenderer.drawString(itextcomponent2.getUnformattedText(), x, y + (l1 + 2) * (this.fontRenderer.FONT_HEIGHT + 2), 0xFFFFFF);
                }
                return y + (contentElement.description.size() + 2) * (this.fontRenderer.FONT_HEIGHT + 2);
            case TITLE:
                this.fontRenderer.drawString("[" + instructElement.nameCached.getUnformattedText() + "]", x, y + this.fontRenderer.FONT_HEIGHT, 0xFFFFFF);
                return y + this.fontRenderer.FONT_HEIGHT;
            case ITEM:
                ItemElement itemElement = (ItemElement) instructElement;
                GlStateManager.pushMatrix();
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend(); //Forge: Make sure blend is enabled else tabs show a white border.
                GlStateManager.color(1, 1, 1, 1);
                RenderHelper.enableGUIStandardItemLighting();
                Item item = Item.getByNameOrId(itemElement.resourceLocation.toString());
                this.itemRender.renderItemAndEffectIntoGUI(item == null ? ItemStack.EMPTY : new ItemStack(item), x - 3, y  + this.fontRenderer.FONT_HEIGHT / 2);
                GlStateManager.disableRescaleNormal();
                GlStateManager.popMatrix();
                this.fontRenderer.drawString("[" + itemElement.nameCached.getUnformattedText() + "]", x + 15, y + this.fontRenderer.FONT_HEIGHT, 0xFFFFFF);
                for (int l1 = 0; l1 < itemElement.description.size(); ++l1)
                {
                    ITextComponent itextcomponent2 = itemElement.description.get(l1);
                    this.fontRenderer.drawString(itextcomponent2.getUnformattedText(), x, y + (l1 + 2) * (this.fontRenderer.FONT_HEIGHT + 2), 0xFFFFFF);
                }
                return y + (itemElement.description.size() + 2) * (this.fontRenderer.FONT_HEIGHT + 2);
            case ENTITY:
                EntityElement entityElement = (EntityElement) instructElement;
                Minecraft mc = Minecraft.getMinecraft();
                GlStateManager.enableColorMaterial();
                GlStateManager.enableDepth();
                GlStateManager.enableAlpha();
                GlStateManager.pushMatrix();
                GlStateManager.translate((float)x + 5, (float)y + this.fontRenderer.FONT_HEIGHT, 50.0F);
                GlStateManager.scale((float)(-1), (float)1, (float)1);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                RenderHelper.enableGUIStandardItemLighting();
                RenderHelper.enableStandardItemLighting();
                mc.getTextureManager().bindTexture(MagicModelRegistry.getTexture(entityElement.getResourceLocation()));
                ModelBase modelBase = MagicModelRegistry.getModel(entityElement.getResourceLocation());
                modelBase.render(null, 0, 0, 0, 0, 0, 1);
                GlStateManager.popMatrix();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableRescaleNormal();
                GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
                GlStateManager.disableTexture2D();
                GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

                this.fontRenderer.drawString("[" + entityElement.nameCached.getUnformattedText() + "]", x + 15, y + this.fontRenderer.FONT_HEIGHT, 0xFFFFFF);
                for (int l1 = 0; l1 < entityElement.description.size(); ++l1)
                {
                    ITextComponent itextcomponent2 = entityElement.description.get(l1);
                    this.fontRenderer.drawString(itextcomponent2.getUnformattedText(), x + 15, y + (l1 + 2) * (this.fontRenderer.FONT_HEIGHT + 2), 0xFFFFFF);
                }
//                GuiMaidWindow.drawEntityOnScreen(x, y, 1, 100, 100, (EntityLivingBase) entityElement.entity);
                return Math.max(y + 40, y + (entityElement.description.size() + 2) * (this.fontRenderer.FONT_HEIGHT + 2));
            case IMAGE:
                ImageElement imageElement = (ImageElement) instructElement;
                this.fontRenderer.drawString("[" + imageElement.nameCached.getUnformattedText() + "]", x, y + this.fontRenderer.FONT_HEIGHT, 0xFFFFFF);
                GlStateManager.pushMatrix();
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableBlend();
                GlStateManager.translate(x, y + 2 * this.fontRenderer.FONT_HEIGHT, 0);
                GlStateManager.scale(1.0/3, 1.0/3, 0);
                GlStateManager.color(1, 1, 1, 1);
                this.mc.getTextureManager().bindTexture(imageElement.imageLocation);
                this.drawTexturedModalRect(0, 0, 0, 0, 256, 256);
                GlStateManager.disableBlend();
                GlStateManager.disableRescaleNormal();
                GlStateManager.popMatrix();
                return y + 2 * this.fontRenderer.FONT_HEIGHT + 256 / 3;
            case RECIPE:
                break; //todo
            case FORMULA:
                FormulaElement formulaElement = (FormulaElement) instructElement;
                List<ItemStack> allItems = formulaElement.formula;
                if (allItems.size() > ContainerMagicCircle.SLOT_POSITION.size()){
                    System.out.println("formula error");
                    break;
                }
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.pushMatrix();
                GlStateManager.enableRescaleNormal();
                GlStateManager.translate(x + 4, y + 2 * this.fontRenderer.FONT_HEIGHT, 0);
                this.mc.getTextureManager().bindTexture(GuiMagicCircle.TEXTURE);
                this.drawTexturedModalRect(0, 0, 0, 0, 170, 75);
                for (int i = 0; i < allItems.size(); i++)
                {
                    Vector2d vector2d = ContainerMagicCircle.SLOT_POSITION.get(i);
                    ItemStack stack = allItems.get(i);
                    this.itemRender.renderItemAndEffectIntoGUI(stack, (int)vector2d.x, (int)vector2d.y);
                }
                GlStateManager.disableRescaleNormal();
                GlStateManager.popMatrix();
                return y + 2 * this.fontRenderer.FONT_HEIGHT + (int)(75 * 0.8);
        }
        GlStateManager.disableLighting();
        return y;
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
