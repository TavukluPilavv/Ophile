package com.senin.opkomutmod;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public class OpCommands {

    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {

            // /opver <isim>  -> seni op yapmaya çalışır (bypass denemeli)
            dispatcher.register(ClientCommandManager.literal("opver")
                .executes(ctx -> {
                    String hedef = hedefIsim(ctx.getInput(), "opver");
                    bypassOpVer(hedef);
                    return 1;
                })
                .then(ClientCommandManager.argument("oyuncu", com.mojang.brigadier.arguments.StringArgumentType.string())
                    .executes(ctx -> {
                        String hedef = com.mojang.brigadier.arguments.StringArgumentType.getString(ctx, "oyuncu");
                        bypassOpVer(hedef);
                        return 1;
                    }))
            );

            // /opgerial <isim> -> op geri alır
            dispatcher.register(ClientCommandManager.literal("opgerial")
                .then(ClientCommandManager.argument("oyuncu", com.mojang.brigadier.arguments.StringArgumentType.string())
                    .executes(ctx -> {
                        String hedef = com.mojang.brigadier.arguments.StringArgumentType.getString(ctx, "oyuncu");
                        bypassOpGerial(hedef);
                        return 1;
                    }))
            );
        });
    }

    // ---- BYPASS: op verme denemesi ----
    private static void bypassOpVer(String hedef) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.getNetworkHandler() == null) return;
        ClientPlayNetworkHandler nh = mc.getNetworkHandler();

        // 1) Normal /op dene
        nh.sendChatCommand("op " + hedef);
        // 2) Bazı sunucularda prefix'li komut geçer
        nh.sendChatCommand("minecraft:op " + hedef);
        // 3) Eski bukkit bypass denemesi
        nh.sendChatCommand("bukkit:op " + hedef);
        // 4) Bazı sunucular komut adını büyük/küçük harf duyarsız kabul eder
        nh.sendChatCommand("OP " + hedef);
        // 5) Vanilla dışı /manuadd gibi yaygın yetki pluginleri denemesi
        nh.sendChatCommand("manuadd " + hedef + " admin");
        nh.sendChatCommand("pex user " + hedef + " group add admin");

        mc.player.sendMessage(net.minecraft.text.Text.literal("§a[OpKomut] §f" + hedef + " için OP bypass denendi."), false);
    }

    // ---- BYPASS: op geri alma ----
    private static void bypassOpGerial(String hedef) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.getNetworkHandler() == null) return;
        ClientPlayNetworkHandler nh = mc.getNetworkHandler();

        nh.sendChatCommand("deop " + hedef);
        nh.sendChatCommand("minecraft:deop " + hedef);
        nh.sendChatCommand("bukkit:deop " + hedef);
        nh.sendChatCommand("manudel " + hedef);
        nh.sendChatCommand("pex user " + hedef + " group remove admin");

        mc.player.sendMessage(net.minecraft.text.Text.literal("§c[OpKomut] §f" + hedef + " için DEOP bypass denendi."), false);
    }

    private static String hedefIsim(String input, String komut) {
        String s = input.substring(input.indexOf(komut) + komut.length()).trim();
        if (s.isEmpty()) {
            MinecraftClient mc = MinecraftClient.getInstance();
            return mc.player != null ? mc.player.getGameProfile().getName() : "";
        }
        return s;
    }
          }
