package com.FuFu.CabbageJellyPack.mixin;

import net.minecraft.client.resources.language.I18n;
import net.minecraftforge.client.gui.ModListScreen;
import net.minecraftforge.forgespi.language.IModInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

// 让模组列表界面(Mods 菜单)里的显示名和描述随游戏语言变化。
// Forge 原本直接读 mods.toml 里的静态字符串,这里在 updateCache() 构建信息时,
// 用语言文件里的 fml.menu.mods.info.displayname.<modid> / .description.<modid> 覆盖,
// 若当前语言没有对应翻译,则回退到 mods.toml 原文。
@Mixin(ModListScreen.class)
public class ModListScreenMixin {

    @Redirect(
            method = "updateCache",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/forgespi/language/IModInfo;getDisplayName()Ljava/lang/String;",
                    ordinal = 0
            ),
            remap = false
    )
    private String localizeDisplayName(IModInfo mod) {
        String key = "fml.menu.mods.info.displayname." + mod.getModId();
        return I18n.exists(key) ? I18n.get(key) : mod.getDisplayName();
    }

    @Redirect(
            method = "updateCache",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraftforge/forgespi/language/IModInfo;getDescription()Ljava/lang/String;",
                    ordinal = 0
            ),
            remap = false
    )
    private String localizeDescription(IModInfo mod) {
        String key = "fml.menu.mods.info.description." + mod.getModId();
        return I18n.exists(key) ? I18n.get(key) : mod.getDescription();
    }
}
