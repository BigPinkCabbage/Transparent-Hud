package com.FuFu.CabbageJellyPack.Screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

import static com.FuFu.CabbageJellyPack.Screen.StaticData.*;

public class MainSettingScreen extends Screen {
    private Button intelligentButton;
    private Button papeButton;

    private AlphaSlider InventoryBgAlphaSlider;
    private BackgroundAlphaSlider GrayBgAlphaSlider;
    private static EditBox InventoryBgAlphaInputBox;
    private static EditBox GrayBgAlphaInputBox;

    Component title = Component.translatable("cabbagejellypack.settings.screen");

    private static final ResourceLocation Mouse_Arrow =
            ResourceLocation.tryBuild("cabbagejellypack", "textures/hud/mouse/mouse_arrow.png");
    private static final ResourceLocation fufuMouse =
            ResourceLocation.tryBuild("cabbagejellypack", "textures/hud/mouse/fufu_mouse00.png");
    private static final ResourceLocation Title =
            ResourceLocation.tryBuild("cabbagejellypack", "textures/hud/title/logo.png");

    private static final ResourceLocation panelBackGround = ResourceLocation.withDefaultNamespace(
            "textures/gui/inworld_menu_list_background.png");

    private static final ResourceLocation lock = ResourceLocation.fromNamespaceAndPath(
            "minecraft", "textures/gui/sprites/widget/locked_button.png");
    // 当前播放帧
    private static int currentFrame = 0;

    // 是否正在播放
    private static boolean playingClickAnimation = false;

    // 每帧间隔（tick）
    private static final int FRAME_DELAY = 1; // 1 = 每tick一帧（快）
    private static int frameTimer = 0;

    // 动画总帧数
    private static final int TOTAL_FRAMES = 10;
    private static final ResourceLocation[] CLICK_FRAMES = new ResourceLocation[TOTAL_FRAMES];

    private static boolean mouseHolding = false;

    private static final int HALF_FRAME = TOTAL_FRAMES / 2;

    private static boolean waitingRelease = false;
    private static boolean releaseSoundPlayed = false;

    static {
        for (int i = 0; i < TOTAL_FRAMES; i++) {
            String index = String.format("%02d", i);
            CLICK_FRAMES[i] = ResourceLocation.fromNamespaceAndPath(
                    "cabbagejellypack",
                    "textures/hud/mouse/fufu_mouse" + index + ".png"
            );
        }
    }

    protected MainSettingScreen() {
        super(Component.translatable("cabbagejellypack.settings.screen"));
    }

    private Component getShowHudButton() {
        return Component.translatable(showHudButton ?
                "cabbagejellypack.settings.showhudbutton.open" :
                "cabbagejellypack.settings.showhudbutton.close");
    }

    private Component getHudText() {
        return Component.translatable(showHudText ?
                "cabbagejellypack.settings.position&rt&transparent.open" :
                "cabbagejellypack.settings.position&rt&transparent.close");
    }

    private Component getBgModeText() {
        return switch (bgMode) {
            case NONE -> Component.translatable("cabbagejellypack.settings.nobackground");
            case BLUR -> Component.translatable("cabbagejellypack.settings.blurbackground");
            case GRAY -> Component.translatable("cabbagejellypack.settings.graybackground");
        };
    }

    private static Component getCabbageEasterText() {
        return Component.translatable(CabbageEaster ?
                "cabbagejellypack.configsettings.cabbageeaster.open" :
                "cabbagejellypack.configsettings.cabbageeaster.close");
    }

    private static Component getAllowSendMessageText() {
        return Component.translatable(AllowSendMessage ?
                "cabbagejellypack.configsettings.allowsendmessage.open" :
                "cabbagejellypack.configsettings.allowsendmessage.close");
    }

    private Component getIntelligent() {
        return Component.translatable(openIntelligent ?
                "cabbagejellypack.settings.intelligent.open" :
                "cabbagejellypack.settings.intelligent.close");
    }

    private Component getPapeText() {
        return showAlphaControls
                ? Component.literal("↷")
                : Component.literal("↶");
    }
    private static void hideCursor() {
        long window = Minecraft.getInstance().getWindow().getWindow();
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
    }

    private static void showCursor() {
        long window = Minecraft.getInstance().getWindow().getWindow();
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    }

    private void renderVanillaPanel(GuiGraphics g, int y, int w) {
        RenderSystem.enableBlend();
        g.setColor(1.0f,1.0f,1.0f, 0.3f);
        g.blit(panelBackGround, 0, y, 0, 0, w, 153, 32, 32);
        g.blit(Screen.HEADER_SEPARATOR, 0, y - 2, 0, 0, w, 2, 32, 2);
        g.blit(Screen.FOOTER_SEPARATOR, 0, y + 153, 0, 0, w, 2, 32, 2);
        g.setColor(1.0f,1.0f,1.0f, 1.0f);
        RenderSystem.disableBlend();
    }

    public static void open() {
        Minecraft.getInstance().setScreen(new MainSettingScreen());
    }

    @Override
    protected void init() {
        super.init();

        if (CabbageEaster) {
            hideCursor();
        }

        Font font = Minecraft.getInstance().font;

        InventoryBgAlphaInputBox = new EditBox(
                font,
                this.width / 2 + 50, this.height / 2 - 50,
                30, 18,
                Component.literal("")

        );
        InventoryBgAlphaInputBox.setValue(String.valueOf(InventoryAlpha));

        InventoryBgAlphaInputBox.setResponder(text -> {
            try {
                float value = Float.parseFloat(text);
                if (value >= 0.0F && value <= 1.0F) {
                    InventoryAlpha = value;
                    if (InventoryBgAlphaInputBox != null) {
                        InventoryBgAlphaSlider.syncTo(InventoryAlpha); // ✅ 设置滑轮值并刷新显示
                        SettingsIO.saveCabbageData();
                    }
                }
            } catch (NumberFormatException ignored) {
                // 非数字输入，忽略
            }
        });
        InventoryBgAlphaInputBox.visible = showAlphaControls;
        this.addRenderableWidget(InventoryBgAlphaInputBox);

        GrayBgAlphaInputBox = new EditBox(
                font,
                this.width / 2 + 50, this.height / 2 - 20,
                30, 18,
                Component.literal("")
        );

        GrayBgAlphaInputBox.setValue(String.valueOf(GrayBgAlpha));

        GrayBgAlphaInputBox.setResponder(text -> {
            try {
                float value = Float.parseFloat(text);
                if (value >= 0.0F && value <= 1.0F) {
                    GrayBgAlpha = value;
                    if (GrayBgAlphaInputBox != null) {
                        GrayBgAlphaSlider.syncTo(GrayBgAlpha);
                        SettingsIO.saveCabbageData();
                    }
                }
            } catch (NumberFormatException ignored) {
                // 非法输入忽略

            }
        });
        this.addRenderableWidget(GrayBgAlphaInputBox);

        this.InventoryBgAlphaSlider = new AlphaSlider(
                this.width / 2 - 80, this.height / 2 - 50,
                120, 18 ,
                InventoryBgAlphaInputBox
        );
        InventoryBgAlphaSlider.visible = showAlphaControls;
        this.addRenderableWidget(this.InventoryBgAlphaSlider);

        this.GrayBgAlphaSlider =new BackgroundAlphaSlider(
                this.width / 2 - 80, this.height / 2 - 20,
                120, 18 ,
                GrayBgAlphaInputBox
        );

        this.addRenderableWidget(this.GrayBgAlphaSlider);

        this.addRenderableWidget(Button.builder(
                        getShowHudButton(), // 初始显示的文本
                        btn -> {
                            showHudButton = !showHudButton; // 切换值
                            btn.setMessage(getShowHudButton());          // 更新按钮文本
                            SettingsIO.saveCabbageData();
                        })
                .pos(this.width / 2 - 80, this.height / 2 + 10) // 按钮位置
                .size(160, 18)
                .tooltip(Tooltip.create(
                        Component.translatable("cabbagejellypack.settings.showhudbutton.tooltip")
                ))
                .build()
        );

        this.addRenderableWidget(Button.builder(
                        getHudText(), // 初始显示的文本
                        btn -> {
                            showHudText = !showHudText; // 切换值
                            btn.setMessage(getHudText());          // 更新按钮文本
                            SettingsIO.saveCabbageData();
                        })
                .pos(this.width / 2 - 80, this.height / 2 + 40) // 按钮位置
                .size(160, 18)
                .build()
        );

        this.addRenderableWidget(Button.builder(
                        getBgModeText(),
                        btn -> {

                            // ⭐ 循环切换
                            bgMode = switch (bgMode) {
                                case NONE -> BackgroundMode.BLUR;
                                case BLUR -> BackgroundMode.GRAY;
                                case GRAY -> BackgroundMode.NONE;
                            };

                            btn.setMessage(getBgModeText());
                            SettingsIO.saveCabbageData();
                        })
                .pos(this.width / 2 - 80, this.height / 2 + 70)
                .size(78, 18)
                .build()
        );

        this.addRenderableWidget(Button.builder(
                        getCabbageEasterText(), // 初始显示的文本
                        btn -> {
                            CabbageEaster = !CabbageEaster;
                            btn.setMessage(getCabbageEasterText());// 更新按钮文本
                            SettingsIO.saveCabbageData();
                            if (!CabbageEaster) {
                                showCursor();
                            }else {
                                hideCursor();
                            }
                        })
                .pos(this.width / 2 + 2, this.height / 2 + 70) // 按钮位置
                .size(78, 18)
                .build()
        );

        this.addRenderableWidget(Button.builder(
                                Component.literal("×"),  // 按钮文本
                                btn -> Minecraft.getInstance().setScreen(null) // 点击关闭菜单
                        )
                        .pos(this.width / 2 - 130, this.height / 2 - 110) // 按钮位置（可根据已有按钮上下调整）
                        .size(12, 12)
                        .build()
        );

        papeButton = Button.builder(
                        getPapeText(),
                        btn ->
                        {
                            showAlphaControls = !showAlphaControls;
                            btn.setMessage(getPapeText());
                            InventoryBgAlphaInputBox.visible = showAlphaControls;
                            InventoryBgAlphaSlider.visible = showAlphaControls;
                            intelligentButton.visible = !showAlphaControls;
                            SettingsIO.saveIntelligentData();
                        }
                )
                .pos(this.width / 2 + 130, this.height / 2 - 110) // 按钮位置（可根据已有按钮上下调整）
                .size(12, 12)
                .build();
        papeButton.visible = !openIntelligent;
        this.addRenderableWidget(papeButton);


        intelligentButton = Button.builder(
                        getIntelligent(),
                        btn ->
                        {
                            openIntelligent = !openIntelligent;
                            btn.setMessage(getIntelligent());
                            SettingsIO.saveIntelligentData();
                            papeButton.visible = !openIntelligent;
                        }
                )
                .pos(this.width / 2 - 80, this.height / 2 - 50)
                .size(120, 18)
                .tooltip(Tooltip.create(
                        Component.translatable("cabbagejellypack.settings.intelligent.tooltip")
                ))
                .build();
        intelligentButton.visible = !showAlphaControls;
        this.addRenderableWidget(intelligentButton);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (CabbageEaster) {
            ResourceLocation texture;
            if (playingClickAnimation) {
                texture = CLICK_FRAMES[currentFrame];
            } else {
                texture = fufuMouse;
            }

            guiGraphics.pose().pushPose(); // 保存当前变换状态
            guiGraphics.pose().translate(0, 0, 100);
            guiGraphics.blit(
                    Mouse_Arrow,
                    mouseX,
                    mouseY,
                    0, 0,
                    16, 16,
                    16, 16
            );
            guiGraphics.blit(
                    texture,
                    mouseX + 6,
                    mouseY + 6,
                    0, 0,
                    16, 16,
                    16, 16
            );
            guiGraphics.pose().popPose();
        }

        int scale = 2;
        int iconSize = 16;
        int iconRealSize = iconSize * scale;
        int spacing = 6;
        int textWidth = this.font.width(title);
        // 整体真实宽度
        int totalWidth = iconRealSize + spacing + textWidth;
        // 整体起点
        int startX = (this.width - totalWidth) / 2;
        int centerY = this.height / 2 - 97;
        // 画图标（带 scale）
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.blit(
                Title,
                startX / scale,
                centerY / scale,
                0, 0,
                iconSize,
                iconSize,
                iconSize,
                iconSize
        );
        guiGraphics.pose().popPose();
        // 画文字
        int textX = startX + iconRealSize + spacing;
        guiGraphics.drawString(
                this.font,
                Component.translatable("cabbagejellypack.settings.screen").getString(),
                textX,
                centerY + (iconRealSize - 8) / 2,
                0xFFFFFF,
                false
        );

        if (openIntelligent) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(0.6F, 0.6F, 1.0F);
            guiGraphics.blit(
                    lock,
                    (int) (((double) this.width / 2 + 130) / 0.6), (int) (((double) this.height / 2 - 110) / 0.6),
                    0, 0,
                    20, 20,
                    20, 20
            );
            guiGraphics.pose().popPose();
        }
    }

    @Override
    public void renderBackground(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        switch (bgMode) {
            case NONE -> {
            }
            case BLUR -> this.renderBlurredBackground(partialTick);

            case GRAY -> {
                this.renderMenuBackground(guiGraphics);
                renderVanillaPanel(guiGraphics, this.height / 2 - 58, this.width);
            }
        }
    }

    private static class AlphaSlider extends AbstractSliderButton {
        public AlphaSlider(int x, int y, int width, int height,EditBox AlphaInputBox) {
            super(x, y, width, height, Component.literal("Inventory Alpha"), InventoryAlpha); // 初始值
            InventoryBgAlphaInputBox = AlphaInputBox;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            this.setMessage(Component.translatable("cabbagejellypack.settings.inventory_alpha"));
        }

        @Override
        protected void applyValue() {
            InventoryAlpha = (float) this.value;
            if (InventoryBgAlphaInputBox != null) {
                InventoryBgAlphaInputBox.setValue(String.format("%.2f", this.value));
                SettingsIO.saveCabbageData();
            }
        }
        // ✅ 新增公开方法供外部调用
        public void syncTo(float newValue) {
            this.value = newValue;         // 修改内部 value
            this.updateMessage();          // 更新显示文本
        }
    }

    private static class BackgroundAlphaSlider extends AbstractSliderButton {
        public BackgroundAlphaSlider(int x, int y, int width, int height, EditBox AlphaInputBox) {
            super(x, y, width, height, Component.literal("GrayBg Alpha"),GrayBgAlpha);
            GrayBgAlphaInputBox = AlphaInputBox;
            this.updateMessage();
        }

        @Override
        protected void updateMessage() {
            this.setMessage(Component.translatable("cabbagejellypack.settings.bg_alpha"));
        }

        @Override
        protected void applyValue() {
            GrayBgAlpha = (float) this.value;
            if (GrayBgAlphaInputBox != null) {
                GrayBgAlphaInputBox.setValue(String.format("%.2f", GrayBgAlpha));
                SettingsIO.saveCabbageData();
            }
        }

        public void syncTo(float newValue) {
            this.value = newValue;
            this.updateMessage();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!playingClickAnimation)
            return;

        frameTimer++;

        if (frameTimer < FRAME_DELAY)
            return;

        frameTimer = 0;

        // ===== 第一阶段 =====
        if (!waitingRelease) {
            // 长按：播放到前半段最后一帧后暂停
            if (mouseHolding && currentFrame >= HALF_FRAME - 1) {
                waitingRelease = true;
                return;
            }
        }
        // ===== 第二阶段 =====
        else {
            // 鼠标还没松开
            if (mouseHolding)
                return;
            if (!releaseSoundPlayed) {
                releaseSoundPlayed = true;
                ResourceLocation rl_return = ResourceLocation.tryParse("cabbagejellypack:fufu_click_return");
                if (rl_return != null) {
                    Minecraft.getInstance().getSoundManager().play(
                            SimpleSoundInstance.forUI(
                                    SoundEvent.createVariableRangeEvent(rl_return),
                                    1.0F
                            )
                    );
                }
            }
        }

        // 前进到下一帧（两分支的公共逻辑）
        currentFrame++;

        if (currentFrame >= TOTAL_FRAMES) {
            playingClickAnimation = false;
            waitingRelease = false;
            currentFrame = 0;
        }
    }

    @Override
    public void removed() {
        super.removed();
        showCursor();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (CabbageEaster && button == 0) {
            ResourceLocation rl = ResourceLocation.tryParse("cabbagejellypack:fufu_click");
            if (rl != null) {
                Minecraft.getInstance().getSoundManager().play(
                        SimpleSoundInstance.forUI(
                                SoundEvent.createVariableRangeEvent(rl),
                                1.0F
                        )
                );
            }
            mouseHolding = true;
            waitingRelease = false;
            playingClickAnimation = true;
            currentFrame = 0;
            frameTimer = 0;

            releaseSoundPlayed = false;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            mouseHolding = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    //----------------------内部类----------------------
    public static class ConfigurationScreen extends Screen {
        private final Screen parent;

        private Button allowSendMessage;
        private Button cabbageEaster;

        public ConfigurationScreen(Screen parent) {
            super(Component.translatable("cabbagejellypack.configsettings.screen"));
            this.parent = parent;
        }

        @Override
        protected void init() {
            if (CabbageEaster) {
                hideCursor();
            }

            int centerX = this.width / 2;
            int centerY = this.height / 2;

            // 允许通知按钮
            allowSendMessage = Button.builder(
                    getAllowSendMessageText(),
                    btn -> {
                        AllowSendMessage = !AllowSendMessage;
                        btn.setMessage(getAllowSendMessageText());
                        SettingsIO.saveConfigData();
                    }
            ).bounds(centerX - 85, centerY - 30, 170, 20).build();
            this.addRenderableWidget(allowSendMessage);

            // 彩蛋按钮
            cabbageEaster = Button.builder(
                    getCabbageEasterText(),
                    btn -> {
                        CabbageEaster = !CabbageEaster;
                        btn.setMessage(getCabbageEasterText());
                        SettingsIO.saveConfigData();
                        if (!CabbageEaster) {
                            showCursor();
                        }else {
                            hideCursor();
                        }
                    }
            ).bounds(centerX - 85, centerY, 170, 20).build();
            this.addRenderableWidget(cabbageEaster);

            // 重置按钮
            this.addRenderableWidget(Button.builder(
                    Component.translatable("cabbagejellypack.configsettings.reset_all"),
                    btn -> {
                        SettingsIO.resetAllParameters();
                        showCursor();
                        allowSendMessage.setMessage(getAllowSendMessageText());
                        cabbageEaster.setMessage(getCabbageEasterText());
                    }
            ).bounds(centerX - 85, centerY + 30, 82, 20).build());

            // 返回按钮
            this.addRenderableWidget(Button.builder(
                    Component.translatable("cabbagejellypack.configsettings.back"),
                    btn -> {
                        if (this.minecraft != null) {
                            SettingsIO.saveConfigData();
                            this.minecraft.setScreen(parent);
                        }
                    }
            ).bounds(centerX + 3, centerY + 30, 82, 20).build());
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
            super.render(graphics, mouseX, mouseY, partialTicks);

            if (CabbageEaster) {
                ResourceLocation texture;
                if (playingClickAnimation) {
                    texture = CLICK_FRAMES[currentFrame];
                } else {
                    texture = fufuMouse;
                }

                graphics.pose().pushPose(); // 保存当前变换状态
                graphics.pose().translate(0, 0, 100);
                if (Mouse_Arrow != null) {
                    graphics.blit(
                            Mouse_Arrow,
                            mouseX,
                            mouseY,
                            0, 0,
                            16, 16,
                            16, 16
                    );
                }
                if (texture != null) {
                    graphics.blit(
                            texture,
                            mouseX + 6,
                            mouseY + 6,
                            0, 0,
                            16, 16,
                            16, 16
                    );
                }
                graphics.pose().popPose();
            }

            int iconSize = 16;
            int spacing = 10;
            int textWidth = this.font.width(title);
            // 整体真实宽度
            int totalWidth = iconSize + spacing + textWidth;
            // 整体起点
            int startX = (this.width - totalWidth) / 2;
            int centerY = this.height / 2 - 65;

            if (Title != null) {
                graphics.blit(
                        Title,
                        startX,
                        centerY,
                        0, 0,
                        iconSize,
                        iconSize,
                        iconSize,
                        iconSize
                );
            }

            // 画文字
            int textX = startX + iconSize + spacing;
            graphics.drawString(
                    this.font,
                    this.title,
                    textX,
                    centerY + (iconSize - 8) / 2,
                    0xFFFFFF,
                    false
            );
        }

        @Override
        public void tick() {
            super.tick();
            if (!playingClickAnimation)
                return;

            frameTimer++;

            if (frameTimer < FRAME_DELAY)
                return;

            frameTimer = 0;

            // ===== 第一阶段 =====
            if (!waitingRelease) {
                // 长按：播放到前半段最后一帧后暂停
                if (mouseHolding && currentFrame >= HALF_FRAME - 1) {
                    waitingRelease = true;
                    return;
                }
            }
            // ===== 第二阶段 =====
            else {
                // 鼠标还没松开
                if (mouseHolding)
                    return;
                if (!releaseSoundPlayed) {
                    releaseSoundPlayed = true;
                    ResourceLocation rl_return = ResourceLocation.tryParse("cabbagejellypack:fufu_click_return");
                    if (rl_return != null) {
                        Minecraft.getInstance().getSoundManager().play(
                                SimpleSoundInstance.forUI(
                                        SoundEvent.createVariableRangeEvent(rl_return),
                                        1.0F
                                )
                        );
                    }
                }
            }

            // 前进到下一帧（两分支的公共逻辑）
            currentFrame++;

            if (currentFrame >= TOTAL_FRAMES) {
                playingClickAnimation = false;
                waitingRelease = false;
                currentFrame = 0;
            }
        }

        @Override
        public void removed() {
            super.removed();
            showCursor();
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (CabbageEaster && button == 0) {
                ResourceLocation rl = ResourceLocation.tryParse("cabbagejellypack:fufu_click");
                if (rl != null) {
                    Minecraft.getInstance().getSoundManager().play(
                            SimpleSoundInstance.forUI(
                                    SoundEvent.createVariableRangeEvent(rl),
                                    1.0F
                            )
                    );
                }
                mouseHolding = true;
                waitingRelease = false;
                playingClickAnimation = true;
                currentFrame = 0;
                frameTimer = 0;

                releaseSoundPlayed = false;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseReleased(double mouseX, double mouseY, int button) {
            if (button == 0) {
                mouseHolding = false;
            }
            return super.mouseReleased(mouseX, mouseY, button);
        }
    }
}
