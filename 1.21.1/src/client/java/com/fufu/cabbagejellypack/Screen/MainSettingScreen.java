package com.fufu.cabbagejellypack.Screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.EditBoxWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import static com.fufu.cabbagejellypack.Screen.StaticData.*;

public class MainSettingScreen extends Screen {
    private ButtonWidget intelligentButton;
    private ButtonWidget papeButton;

    private AlphaSlider InventoryBgAlphaSlider;
    private BackgroundAlphaSlider GrayBgAlphaSlider;
    private EditBoxWidget InventoryBgAlphaInputBox;
    private EditBoxWidget GrayBgAlphaInputBox;

    Text title = Text.translatable("cabbagejellypack.settings.screen");

    private static final Identifier Mouse_Arrow =
            Identifier.of("cabbagejellypack", "textures/hud/mouse/mouse_arrow.png");
    private static final Identifier fufuMouse =
            Identifier.of("cabbagejellypack", "textures/hud/mouse/fufu_mouse00.png");
    private static final Identifier Title =
            Identifier.of("cabbagejellypack", "textures/hud/title/logo.png");

    private static final Identifier lock = Identifier.of(
            "minecraft", "textures/gui/sprites/widget/locked_button.png");

    private static final Identifier panelBackGround = Identifier.of(
            "minecraft", "textures/gui/inworld_menu_list_background.png");

    // 当前播放帧
    private int currentFrame = 0;

    // 是否正在播放
    private boolean playingClickAnimation = false;

    // 每帧间隔（tick）
    private static final int FRAME_DELAY = 1; // 1 = 每tick一帧（快）
    private int frameTimer = 0;

    // 动画总帧数
    private static final int TOTAL_FRAMES = 10;
    private static final Identifier[] CLICK_FRAMES = new Identifier[TOTAL_FRAMES];

    private boolean mouseHolding = false;

    private static final int HALF_FRAME = TOTAL_FRAMES / 2;

    private boolean waitingRelease = false;
    private boolean releaseSoundPlayed = false;

    static {
        for (int i = 0; i < TOTAL_FRAMES; i++) {
            String index = String.format("%02d", i);
            CLICK_FRAMES[i] = Identifier.of(
                    "cabbagejellypack",
                    "textures/hud/mouse/fufu_mouse" + index + ".png"
            );
        }
    }
    private Text getShowHudButton() {
        return Text.translatable(showHudButton ?
                "cabbagejellypack.settings.showhudbutton.open" :
                "cabbagejellypack.settings.showhudbutton.close");
    }

    private Text getHudText() {
        return Text.translatable(showHudText ?
                "cabbagejellypack.settings.position&rt&transparent.open" :
                "cabbagejellypack.settings.position&rt&transparent.close");
    }

    private Text getBgModeText() {
        return switch (bgMode) {
            case NONE -> Text.translatable("cabbagejellypack.settings.nobackground");
            case BLUR -> Text.translatable("cabbagejellypack.settings.blurbackground");
            case GRAY -> Text.translatable("cabbagejellypack.settings.graybackground");
        };
    }

    private Text getCabbageEasterText() {
        return Text.translatable(CabbageEaster ?
                "cabbagejellypack.configsettings.cabbageeaster.open" :
                "cabbagejellypack.configsettings.cabbageeaster.close");
    }

    private Text getIntelligent() {
        return Text.translatable(openIntelligent ?
                "cabbagejellypack.settings.intelligent.open" :
                "cabbagejellypack.settings.intelligent.close");
    }

    private Text getPapeText() {
        return showAlphaControls
                ? Text.literal("↷")
                : Text.literal("↶");
    }

    private void hideCursor() {
        long window = MinecraftClient.getInstance().getWindow().getHandle();
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
    }

    private void showCursor() {
        long window = MinecraftClient.getInstance().getWindow().getHandle();
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    }

    public MainSettingScreen() {
        super(Text.translatable("cabbagejellypack.settings.screen"));
    }

    @Override
    protected void init() {
        super.init(); // 重要：调用父类初始化

        if (CabbageEaster) {
            hideCursor();
        }

        TextRenderer font = this.textRenderer;
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // 库存背景透明度输入框
        this.InventoryBgAlphaInputBox = new EditBoxWidget(
                font,
                centerX + 50, centerY - 50,
                30, 18,
                Text.empty(),      // placeholder - 占位符文本
                Text.empty()       // message - 用于旁白的消息（可空）
        );

        this.InventoryBgAlphaInputBox.setTooltip(
                net.minecraft.client.gui.tooltip.Tooltip.of(
                Text.translatable("cabbagejellypack.settings.inventory_alpha.tooltip")
                )
        );
        // 设置初始值
        this.InventoryBgAlphaInputBox.setText(String.valueOf(InventoryAlpha));

        // 设置文本变化监听器
        this.InventoryBgAlphaInputBox.setChangeListener(text -> {
            try {
                float value = Float.parseFloat(text);
                if (value >= 0.0F && value <= 1.0F) {
                    InventoryAlpha = value;
                    // 同步到滑块
                    if (this.InventoryBgAlphaSlider != null) {
                        this.InventoryBgAlphaSlider.syncTo(InventoryAlpha);
                        SettingsIO.saveCabbageData();
                    }
                }
            } catch (NumberFormatException ignored) {
                // 非数字输入，忽略
            }
        });
        InventoryBgAlphaInputBox.visible = showAlphaControls;
        this.addDrawableChild(this.InventoryBgAlphaInputBox);


        // 设置界面背景透明度输入框
        this.GrayBgAlphaInputBox = new EditBoxWidget(
                font,
                centerX + 50, centerY - 20,
                30, 18,
                Text.empty(),
                Text.empty()
        );

        this.GrayBgAlphaInputBox.setTooltip(
                net.minecraft.client.gui.tooltip.Tooltip.of(
                        Text.translatable("cabbagejellypack.settings.bg_alpha.tooltip")
                )
        );
        this.GrayBgAlphaInputBox.setText(String.valueOf(GrayBgAlpha));

        this.GrayBgAlphaInputBox.setChangeListener(text -> {
            try {
                float value = Float.parseFloat(text);
                if (value >= 0.0F && value <= 1.0F) {
                    GrayBgAlpha = value;
                    if (this.GrayBgAlphaSlider != null) {
                        this.GrayBgAlphaSlider.syncTo(GrayBgAlpha);
                        SettingsIO.saveCabbageData();
                    }
                }
            } catch (NumberFormatException ignored) {
                // 非法输入忽略
            }
        });
        this.addDrawableChild(this.GrayBgAlphaInputBox);

        // 库存透明度滑块
        this.InventoryBgAlphaSlider = new AlphaSlider(
                centerX - 80, centerY - 50,
                120, 18,
                InventoryAlpha
        );
        InventoryBgAlphaSlider.visible = showAlphaControls;
        this.addDrawableChild(this.InventoryBgAlphaSlider);

        // 设置界面背景透明度滑块
        this.GrayBgAlphaSlider = new BackgroundAlphaSlider(
                centerX - 80, centerY - 20,
                120, 18,
                GrayBgAlpha
        );
        this.addDrawableChild(this.GrayBgAlphaSlider);

        this.addDrawableChild(
                ButtonWidget.builder(
                        getShowHudButton(), // 初始显示的文本
                        button -> {
                            showHudButton = !showHudButton; // 切换值
                            button.setMessage(getShowHudButton());          // 更新按钮文本
                            SettingsIO.saveCabbageData();
                        })
                        .dimensions(this.width / 2 - 80, this.height / 2 + 10, 160, 18) // 按钮位置
                        .tooltip(Tooltip.of(
                                Text.translatable(
                                        "cabbagejellypack.settings.showhudbutton.tooltip")
                        ))
                        .build()
        );

        this.addDrawableChild(
                ButtonWidget.builder(
                                getHudText(),
                                button -> {
                                    showHudText = !showHudText;
                                    button.setMessage(getHudText());
                                }
                        )
                        .dimensions(centerX - 80, centerY + 40, 160, 18)
                        .build()
        );

        this.addDrawableChild(
                ButtonWidget.builder(
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
                        .dimensions(this.width / 2 - 80, this.height / 2 + 70, 78, 18)
                        .build()
        );

        this.addDrawableChild(
                ButtonWidget.builder(
                        getCabbageEasterText(), // 初始显示的文本
                        btn -> {
                            CabbageEaster = !CabbageEaster;
                            btn.setMessage(getCabbageEasterText());// 更新按钮文本
                            SettingsIO.saveCabbageData();
                            if (!CabbageEaster){
                                showCursor();
                            }else {
                                hideCursor();
                            }
                        })
                        .dimensions(this.width / 2 + 2, this.height / 2 + 70, 78, 18) // 按钮位置
                        .build()
        );

        // 关闭按钮
        this.addDrawableChild(ButtonWidget.builder(
                        Text.literal("×"),
                        button -> this.close())  // 关闭屏幕
                .position(centerX - 130, centerY - 110)
                .size(12, 12)
                .build()
        );

        papeButton = ButtonWidget.builder(
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
                .position(this.width / 2 + 130, this.height / 2 - 110) // 按钮位置（可根据已有按钮上下调整）
                .size(12, 12)
                .build();
        papeButton.visible = !openIntelligent;
        this.addDrawableChild(papeButton);


        intelligentButton = ButtonWidget.builder(
                        getIntelligent(),
                        btn ->
                        {
                            openIntelligent = !openIntelligent;
                            btn.setMessage(getIntelligent());
                            SettingsIO.saveIntelligentData();
                            papeButton.visible = !openIntelligent;
                        }
                )
                .position(this.width / 2 - 80, this.height / 2 - 50)
                .size(120, 18)
                .tooltip(Tooltip.of(
                        Text.translatable("cabbagejellypack.settings.intelligent.tooltip")
                ))
                .build();
        intelligentButton.visible = !showAlphaControls;
        this.addDrawableChild(intelligentButton);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if (CabbageEaster) {
            Identifier texture;

            if (playingClickAnimation) {
                texture = CLICK_FRAMES[currentFrame];
            } else {
                texture = fufuMouse;
            }

            context.getMatrices().push();
            context.getMatrices().translate(0, 0, 100);
            context.drawTexture(
                    Mouse_Arrow,
                    mouseX,
                    mouseY,
                    0, 0,
                    16, 16,
                    16, 16
            );
            context.drawTexture(
                    texture,
                    mouseX + 6,
                    mouseY + 6,
                    0, 0,
                    16, 16,
                    16, 16
            );
            context.getMatrices().pop();
        }

        int scale = 2;
        int iconSize = 16;
        int iconRealSize = iconSize * scale;
        int spacing = 50;
        int textWidth = textRenderer.getWidth(title);
        // 整体真实宽度
        int totalWidth = iconRealSize + 5 + textWidth;
        // 整体起点
        int startX = (this.width - totalWidth) / 2;
        int centerY = this.height / 2 - 97;

        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.scale(scale, scale, 1.0f);
        context.drawTexture(
                Title,
                startX / scale,
                centerY / scale,
                0 ,
                0,
                0,
                iconSize,
                iconSize,
                iconSize,
                iconSize
        );
        matrices.pop();

        // 画文字
        int textX = startX + iconRealSize + spacing;
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                title,
                textX,
                centerY + (iconRealSize - 8) / 2,
                0xFFFFFF
        );

        if (openIntelligent) {
            matrices.push();
            matrices.scale(0.6F, 0.6F, 1.0F);
            context.drawTexture(
                    lock,
                    (int) (((double) this.width / 2 + 130) / 0.6), (int) (((double) this.height / 2 - 110) / 0.6),
                    0, 0,
                    20, 20,
                    20, 20
            );
            matrices.pop();
        }
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        switch (bgMode) {
            case NONE:
                // 不渲染任何背景
                break;

            case BLUR:
                // 模糊背景
                if (this.client != null) {
                    this.client.gameRenderer.renderBlur(delta);
                }
                if (this.client != null) {
                    this.client.getFramebuffer().beginWrite(false);
                }
                break;

            case GRAY:
                // 灰色背景 + 面板
                // 先渲染暗色背景
                if (this.client != null && this.client.world == null) {
                    this.renderPanoramaBackground(context, delta);
                }
                this.applyBlur(delta);
                this.renderDarkening(context);
                // 然后绘制面板
                renderVanillaPanel(context, this.height / 2 - 58, this.width);
                break;
        }
    }

    private void renderVanillaPanel(DrawContext context, int y, int w) {
        RenderSystem.enableBlend();
        context.setShaderColor(1.0f,1.0f,1.0f, 0.3f);
        context.drawTexture(panelBackGround, 0, y, 0, 0, w, 153, 32, 32);
        context.drawTexture(Screen.HEADER_SEPARATOR_TEXTURE, 0, y - 2, 0, 0, w, 2, 32, 2);
        context.drawTexture(Screen.FOOTER_SEPARATOR_TEXTURE, 0, y + 153, 0, 0, w, 2, 32, 2);
        context.setShaderColor(1.0f,1.0f,1.0f, 1.0f);
        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_J) {
            MinecraftClient.getInstance().setScreen(null);
            SettingsIO.saveCabbageData();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    // 库存透明度滑块
    private class AlphaSlider extends SliderWidget {
        public AlphaSlider(int x, int y, int width, int height, double value) {
            super(x, y, width, height, Text.translatable("cabbagejellypack.settings.inventory_alpha"), value);
            this.updateMessage();
        }
        @Override
        protected void updateMessage() {
            int percent = (int) (this.value * 100);
            this.setMessage(Text.translatable("cabbagejellypack.settings.inventory_alpha", percent));
        }
        @Override
        protected void applyValue() {
            InventoryAlpha = (float) this.value;
            if (InventoryBgAlphaInputBox != null) {
                InventoryBgAlphaInputBox.setText(String.format("%.2f", this.value));
            }
        }
        public void syncTo(float newValue) {
            this.value = newValue;
            this.updateMessage();
        }
    }
    // 设置界面背景透明度滑块
    private class BackgroundAlphaSlider extends SliderWidget {
        private static final double MIN_VALUE = 0.0;
        private static final double MAX_VALUE = 1.0;
        public BackgroundAlphaSlider(int x, int y, int width, int height, double value) {
            super(x, y, width, height, Text.translatable("cabbagejellypack.settings.bg_alpha"),
                    (value - MIN_VALUE) / (MAX_VALUE - MIN_VALUE));
            this.updateMessage();
        }
        @Override
        protected void updateMessage() {
            double currentValue = MIN_VALUE + (this.value * (MAX_VALUE - MIN_VALUE));
            int percent = (int) (currentValue * 100);
            this.setMessage(Text.translatable("cabbagejellypack.settings.bg_alpha", percent));
        }
        @Override
        protected void applyValue() {
            GrayBgAlpha = (float) (MIN_VALUE + (this.value * (MAX_VALUE - MIN_VALUE)));
            if (GrayBgAlphaInputBox != null) {
                GrayBgAlphaInputBox.setText(String.format("%.2f", GrayBgAlpha));
            }
        }
        public void syncTo(float newValue) {
            this.value = (newValue - MIN_VALUE) / (MAX_VALUE - MIN_VALUE);
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
                Identifier rl_return = Identifier.tryParse("cabbagejellypack:fufu_click_return");
                if (rl_return != null) {
                    MinecraftClient.getInstance().getSoundManager().play(
                            PositionedSoundInstance.master(
                                    SoundEvent.of(rl_return),
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

    //---------------位置---------------
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (CabbageEaster && button == 0) {
            Identifier rl = Identifier.tryParse("cabbagejellypack:fufu_click");

            if (rl != null) {
                MinecraftClient.getInstance().getSoundManager().play(
                        PositionedSoundInstance.master(SoundEvent.of(rl), 1.0F)
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
