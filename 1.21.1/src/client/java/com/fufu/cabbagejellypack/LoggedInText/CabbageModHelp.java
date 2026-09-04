package com.fufu.cabbagejellypack.LoggedInText;

import com.fufu.cabbagejellypack.Screen.SettingsIO;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.minecraft.text.Text;
import static com.fufu.cabbagejellypack.Screen.StaticData.InventoryAlpha;
import static com.fufu.cabbagejellypack.Screen.StaticData.GrayBgAlpha;

public class CabbageModHelp {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess) -> registerCommands(dispatcher)
        );
    }

    private static void registerCommands(CommandDispatcher<FabricClientCommandSource> dispatcher) {

        // /CabbageMod help
        dispatcher.register(
                ClientCommandManager.literal("cabbagemod")
                        .then(ClientCommandManager.argument("help", StringArgumentType.word())
                                .executes(context -> {
                                    context.getSource().sendFeedback(
                                            Text.translatable("command.cabbagemod.help")
                                    );
                                    return 1;
                                })
                        )
        );

        // /CabbageMod InventoryAlpha <value>
        dispatcher.register(
                ClientCommandManager.literal("cabbagemod")
                        .then(ClientCommandManager.literal("inventoryalpha")
                                .then(ClientCommandManager.argument(
                                                        "value",
                                                        FloatArgumentType.floatArg(0.0f, 1.0f)
                                                )
                                                .executes(context -> {
                                                    float value = FloatArgumentType.getFloat(context, "value");
                                                    InventoryAlpha = value;
                                                    SettingsIO.saveCabbageData();
                                                    context.getSource().sendFeedback(
                                                            Text.translatable(
                                                                    "command.cabbagemod.inventoryalpha.set",
                                                                    value
                                                            )
                                                    );
                                                    return 1;
                                                })
                                )
                        )
        );

        // /CabbageMod GrayBgAlpha  <value>
        dispatcher.register(
                ClientCommandManager.literal("cabbagemod")
                        .then(ClientCommandManager.literal("graybgalpha")
                                .then(ClientCommandManager.argument(
                                                        "value",
                                                        FloatArgumentType.floatArg(0.0f, 1.0f)
                                                )
                                                .executes(context -> {
                                                    float value = FloatArgumentType.getFloat(context, "value");
                                                    GrayBgAlpha = value;
                                                    SettingsIO.saveCabbageData();
                                                    context.getSource().sendFeedback(
                                                            Text.translatable(
                                                                    "command.cabbagemod.graybgalpha.set",
                                                                    value
                                                            )
                                                    );
                                                    return 1;
                                                })
                                )
                        )
        );
    }
}