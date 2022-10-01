package net.sweenus.simplyswords.forge;

import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sweenus.simplyswords.SimplySwords;
import net.sweenus.simplyswords.forge.compat.GobberCompat;

@Mod(SimplySwords.MOD_ID)
public class SimplySwordsForge {
    public SimplySwordsForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(SimplySwords.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        //if (ModList.get().isLoaded("gobber2"))
            //GobberCompat.registerModItems();

        SimplySwords.init();

        if (ModList.get().isLoaded("gobber2"))
            GobberCompat.GOBBER_ITEM.register(FMLJavaModLoadingContext.get().getModEventBus());

    }
}