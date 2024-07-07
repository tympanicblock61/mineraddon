package com.tympanic.mineraddon;

import com.tympanic.mineraddon.commands.CommandExample;
import com.tympanic.mineraddon.hud.HudExample;
import com.tympanic.mineraddon.modules.ModuleExample;
import com.mojang.logging.LogUtils;
import com.tympanic.mineraddon.pathing.Pathing;
import com.tympanic.mineraddon.pathing.VoyagerPathing;
import com.tympanic.mineraddon.pathing.custom.A_STAR_Node;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import baritone.api.BaritoneAPI;
import meteordevelopment.voyager.Voyager;

public class AddonTemplate extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("Example");
    public static final HudGroup HUD_GROUP = new HudGroup("Example");
    boolean hasBaritone = FabricLoader.getInstance().isModLoaded("baritone");
    boolean hasVoyager = FabricLoader.getInstance().isModLoaded("voyager");
    public static Pathing pathing = VoyagerPathing.INSTANCE;

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Addon Template");

        // Modules
        Modules.get().add(new ModuleExample());

        // Commands
        Commands.add(new CommandExample());

        // HUD
        Hud.get().register(HudExample.INFO);

        if (hasVoyager) {
            System.out.println(new Voyager());
        }
        if (hasBaritone) {
            System.out.println(BaritoneAPI.getProvider());
        }
        if (!hasVoyager && !hasBaritone) {
            System.out.println("using fallback pathing algos.");
        }
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.tympanic.mineraddon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("tympanicblock61", "mineraddon");
    }
}
