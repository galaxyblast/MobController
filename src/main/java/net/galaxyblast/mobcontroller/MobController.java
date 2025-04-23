package net.galaxyblast.mobcontroller;

import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import net.minecraft.server.dedicated.DedicatedServerSettings;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.LocalMobCapCalculator;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(MobController.MODID)
public class MobController
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "mobcontroller";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public MobController(IEventBus modEventBus, ModContainer modContainer)
    {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    @SubscribeEvent
    public void onTrySpawn(MobSpawnEvent.SpawnPlacementCheck event)
    {
        if(event.getLevel().isClientSide()) return;

        //int totalMonsters = Iterables.size(event.getLevel().getLevel().getAllEntities());
        int totalMonsters = 0;
        int totalAnimals = 0;

        for(Entity entity : event.getLevel().getLevel().getAllEntities())
        {
            if(entity instanceof Monster)
                totalMonsters++;
            else if (entity instanceof Animal)
                totalAnimals++;
        }

        int width = 128;
        int height = 256;

        AABB nearbyBox = new AABB(event.getPos().getX() - width, event.getPos().getY() - height, event.getPos().getZ() - width,
                                  event.getPos().getX() + width, event.getPos().getY() + height, event.getPos().getZ() + width);

        if(event.getEntityType().getCategory() == MobCategory.MONSTER)
        {
            if(totalMonsters >= Config.maxMonstersTotal || event.getLevel().getEntitiesOfClass(Monster.class, nearbyBox).size() >= Config.maxMonstersPerChunk)
                event.setResult(MobSpawnEvent.SpawnPlacementCheck.Result.FAIL);
        }
        else
        {
            if(totalAnimals >= Config.maxAnimalsTotal || event.getLevel().getEntitiesOfClass(Animal.class, nearbyBox).size() >= Config.maxAnimalsPerChunk)
                event.setResult(MobSpawnEvent.SpawnPlacementCheck.Result.FAIL);
        }
    }
}
