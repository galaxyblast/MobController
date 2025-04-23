package net.galaxyblast.mobcontroller;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = MobController.MODID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.IntValue MAX_MONSTERS_PER_CHUNK = BUILDER
            .comment("Maximum number of monsters that can spawn per player.")
            .defineInRange("maxMonstersPerPlayer", 70, 0, 100);

    private static final ModConfigSpec.IntValue MAX_ANIMALS_PER_CHUNK = BUILDER
            .comment("Maximum number of animals that can spawn per player.")
            .defineInRange("maxAnimalsPerPlayer", 40, 0, 100);

    private static final ModConfigSpec.IntValue TOTAL_MAX_MONSTERS = BUILDER
            .comment("Maximum total monsters that can spawn across the entire server.")
            .defineInRange("maxMonstersTotal", 1000, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue TOTAL_MAX_ANIMALS = BUILDER
            .comment("Maximum total animals that can spawn across the entire server.")
            .defineInRange("maxAnimalsTotal", 600, 0, Integer.MAX_VALUE);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static int maxMonstersPerChunk;
    public static int maxAnimalsPerChunk;
    public static int maxMonstersTotal;
    public static int maxAnimalsTotal;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        maxMonstersPerChunk = MAX_MONSTERS_PER_CHUNK.get();
        maxAnimalsPerChunk = MAX_ANIMALS_PER_CHUNK.get();
        maxMonstersTotal = TOTAL_MAX_MONSTERS.get();
        maxAnimalsTotal = TOTAL_MAX_ANIMALS.get();
    }
}
