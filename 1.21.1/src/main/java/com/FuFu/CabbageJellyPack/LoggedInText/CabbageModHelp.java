package com.FuFu.CabbageJellyPack.LoggedInText;

import com.FuFu.CabbageJellyPack.CabbageJellyPack;
import com.FuFu.CabbageJellyPack.Screen.SettingsIO;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterClientCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.FuFu.CabbageJellyPack.Screen.StaticData.InventoryAlpha;
import static com.FuFu.CabbageJellyPack.Screen.StaticData.GrayBgAlpha;

@Mod.EventBusSubscriber(modid = CabbageJellyPack.MODID, value = Dist.CLIENT)
public class CabbageModHelp {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterClientCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("cabbagemod")
                .requires(source -> source.hasPermission(0))
                .then(Commands.argument("help", StringArgumentType.word())
                        .executes(context -> {
                            context.getSource().sendSuccess(
                                    () -> Component.translatable("command.cabbagemod.help"),
                                    false
                            );
                            return 1;
                        })));

        dispatcher.register(
                Commands.literal("cabbagemod")
                        .then(Commands.literal("inventoryalpha")
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0.0f, 1.0f))
                                        .executes(InventoryContext -> {
                                            float value = FloatArgumentType.getFloat(InventoryContext, "value");
                                            InventoryAlpha = value;
                                            SettingsIO.saveCabbageData();
                                            InventoryContext.getSource().sendSuccess(() ->
                                                            Component.translatable("command.cabbagemod.inventoryalpha.set", value),
                                                    true);
                                            return 1;
                                        })
                                )
                        )
        );

        dispatcher.register(
                Commands.literal("cabbagemod")
                        .then(Commands.literal("graybgalpha")
                                .then(Commands.argument("value", FloatArgumentType.floatArg(0.0f, 1.0f))
                                        .executes(SettingScreenContext -> {
                                            float value = FloatArgumentType.getFloat(SettingScreenContext, "value");
                                            GrayBgAlpha = value;
                                            SettingsIO.saveCabbageData();
                                            SettingScreenContext.getSource().sendSuccess(() ->
                                                            Component.translatable("command.cabbagemod.graybgalpha.set", value),
                                                    true);
                                            return 1;
                                        })
                                )
                        )
        );
    }
}