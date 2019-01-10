package com.bobmowzie.mowziesmobs.server.potion;

import com.bobmowzie.mowziesmobs.MowziesMobs;
import com.bobmowzie.mowziesmobs.server.item.ItemHandler;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Josh on 1/10/2019.
 */

@GameRegistry.ObjectHolder(MowziesMobs.MODID)
@Mod.EventBusSubscriber(modid = MowziesMobs.MODID)
public class PotionTypeHandler {
    private PotionTypeHandler() {
    }

    public static final PotionType POISON_RESIST = new PotionType("poison_resist", new PotionEffect[]{new PotionEffect(PotionHandler.POISON_RESIST, 3600)}).setRegistryName("poison_resist");
    public static final PotionType LONG_POISON_RESIST = new PotionType("poison_resist", new PotionEffect[]{new PotionEffect(PotionHandler.POISON_RESIST, 9600)}).setRegistryName("long_poison_resist");

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PotionType> event) {
        event.getRegistry().registerAll(
                POISON_RESIST,
                LONG_POISON_RESIST
        );

        PotionHelper.addMix(PotionTypes.AWKWARD, ItemHandler.GLOWING_JELLY, POISON_RESIST);
        PotionHelper.addMix(POISON_RESIST, Items.REDSTONE, LONG_POISON_RESIST);

    }
}
