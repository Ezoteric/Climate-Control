package climateControl;

import climateControl.api.BiomePackageRegistry;
import climateControl.api.BiomeSettings;
import climateControl.api.CCDimensionSettings;
import climateControl.api.ClimateControlSettings;
import climateControl.api.DimensionalSettingsRegistry;
import climateControl.customGenLayer.GenLayerRiverMixWrapper;
import climateControl.utils.ConfigManager;
import climateControl.utils.Zeno410Logger;
import java.util.logging.Logger;



import climateControl.utils.Named;
import climateControl.utils.PropertyManager;
import climateControl.utils.TaggedConfigManager;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.World;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.terraingen.WorldTypeEvent;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.*;
import cpw.mods.fml.common.event.*;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

@Mod(modid = "climatecontrol", name = "Climate Control", version = "0.4",acceptableRemoteVersions = "*")

public class ClimateControl {
    public static Logger logger = new Zeno410Logger("ClimateControl").logger();
    public static boolean testing = true;

    private Configuration config;
    //private OldClimateControlSettings settings = new OldClimateControlSettings();
    //private OldClimateControlSettings defaultSettings = new OldClimateControlSettings();
    //private OverworldDataStorage storage;

    private ClimateControlSettings newSettings;
    private ConfigManager<ClimateControlSettings> configManager;
    private TaggedConfigManager addonConfigManager;
    private CCDimensionSettings dimensionSettings;

    private HashMap<Integer,WorldServer> servedWorlds = new HashMap<Integer,WorldServer>();
    private GenLayerRiverMixWrapper riverLayerWrapper = new GenLayerRiverMixWrapper(0L);
    //private GenLayer[] vanillaGenerators;
    //private GenLayer[] modifiedGenerators;

    private File configDirectory;
    private File suggestedConfigFile;
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        addonConfigManager = new TaggedConfigManager("climatecontrol.cfg","ClimateControl");
        BiomePackageRegistry.instance = new BiomePackageRegistry(
                event.getSuggestedConfigurationFile().getParentFile(),addonConfigManager);

        DimensionalSettingsRegistry.instance= new DimensionalSettingsRegistry();
        
        newSettings = new ClimateControlSettings();
        suggestedConfigFile = event.getSuggestedConfigurationFile();
        config = new Configuration(suggestedConfigFile);
        config.load();
        //if (this.rescueOldCCMode) defaultSettings.set(config);
        //this.settings = defaultSettings.clone();

        newSettings.readFrom(config);
        
        // settings need to be reset so the mod-specific configs go into the CC file.
        // can't be done first because we don't have a set of biomeSettings yet.

        configDirectory = event.getSuggestedConfigurationFile().getParentFile();
        newSettings.setDefaults(configDirectory);
        logger.info(configDirectory.getAbsolutePath());
        this.dimensionSettings = new CCDimensionSettings();

        configManager = new ConfigManager<ClimateControlSettings>(
                config,newSettings,event.getSuggestedConfigurationFile());
        config.save();

    }

    private ClimateControlSettings freshSettings() {
        ClimateControlSettings result = new ClimateControlSettings();
        Configuration workingConfig = new Configuration(suggestedConfigFile);
        workingConfig.load();
        result.readFrom(workingConfig);
        result.setDefaults(configDirectory);
        return result;
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.TERRAIN_GEN_BUS.register(this);
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) throws Exception {
        newSettings.setDefaults(configDirectory);
        newSettings.copyTo(config);
        config.save();
        logger.info("biome setting count: "+BiomePackageRegistry.instance.biomeSettings().size());
        for (Named<BiomeSettings> addonSetting: BiomePackageRegistry.instance.biomeSettings()) {
            this.addonConfigManager.initializeConfig(addonSetting, configDirectory);
        }
        addonConfigManager.initializeConfig(dimensionSettings.named(), configDirectory);
        //try {
            //dimensionManager = new DimensionManager(newSettings,MinecraftServer.getServer());
        //} catch (Exception e) {}
    }

    private DimensionManager dimensionManager;
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void onWorldLoad(WorldEvent.Load event) {
        DimensionalSettingsRegistry.instance.onWorldLoad(event);
        if (dimensionManager == null) {
            if (MinecraftServer.getServer()!=null)
                dimensionManager = new DimensionManager(newSettings,dimensionSettings,MinecraftServer.getServer());
        }
        if (dimensionManager != null) {
            dimensionManager.onWorldLoad(event);
        }
    }

    @SubscribeEvent
    public void unloadWorld(WorldEvent.Unload event) {
        DimensionalSettingsRegistry.instance.unloadWorld(event);
        if (event.world instanceof WorldServer) {
            servedWorlds.remove(event.world.provider.dimensionId);
        }
    }
    @EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        newSettings.setDefaults(configDirectory);
        newSettings.copyTo(config);
        DimensionalSettingsRegistry.instance.serverStarted(event);
        File worldSaveDirectory = null;
        String worldName = MinecraftServer.getServer().getFolderName();
        if (MinecraftServer.getServer().isSinglePlayer()) {
            File saveDirectory = MinecraftServer.getServer().getFile("saves");
            worldSaveDirectory = new File(saveDirectory,worldName);
        } else {
            PropertyManager settings = new PropertyManager(MinecraftServer.getServer().getFile("server.properties"));
            worldName = settings.getProperty("level-name", worldName);
            worldSaveDirectory = MinecraftServer.getServer().getFile(worldName);
        }
        File worldConfigDirectory = new File(worldSaveDirectory,TaggedConfigManager.worldSpecificConfigFileName);
        addonConfigManager.updateConfig(dimensionSettings.named(), configDirectory, worldConfigDirectory);
    }

    @EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        this.riverLayerWrapper =new GenLayerRiverMixWrapper(0L);
        this.configManager.clearWorldFile();
        for (Named<BiomeSettings> addonSetting: BiomePackageRegistry.instance.biomeSettings()) {
            this.addonConfigManager.initializeConfig(addonSetting, configDirectory);
        }
        addonConfigManager.initializeConfig(dimensionSettings.named(), configDirectory);
        DimensionalSettingsRegistry.instance.serverStopped(event);
        dimensionManager = null;
        //this.riverLayerWrapper.clearRedirection();
    }

    @SubscribeEvent
    public void onWorldSave(WorldEvent.Save event){
        World world = event.world;
        if (world.isRemote) return;
        int dimension = world.provider.dimensionId;
        if (dimension != 0) return;
        //storage.onWorldSave(event,this.settings);


    }
    @EventHandler
    public void onServerStarted(FMLServerStartedEvent event) {
    }

    @EventHandler
    public void onServerStarting(FMLServerStartingEvent event) {


        logger.info("starting server");
        File directory = event.getServer().worldServerForDimension(0).getChunkSaveLocation();
        directory = new File(directory,"worldSpecificConfig");
        directory.mkdir();
            logger.info("Managing configs");
            //addonConfigManager.updateConfig(this.dimensionSettings.named(), configDirectory, directory);
            /*
            configManager.setWorldFile(worldConfigDirectory.getParentFile());
            logger.info(worldConfigDirectory.getParentFile().getName());
            this.newSettings.setDefaults(configDirectory);


            configManager.saveWorldSpecific();
            for (Named<BiomeSettings> addonSetting: newSettings.registeredBiomeSettings()) {
               this.addonConfigManager.updateConfig(addonSetting, configDirectory, worldConfigDirectory);
            }*/
        if (dimensionManager == null) {
            if (event.getServer()!=null)
                dimensionManager = new DimensionManager(newSettings,dimensionSettings,event.getServer());
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onBiomeGenInit(WorldTypeEvent.InitBiomeGens event) {
        if (dimensionManager == null) {
            if (MinecraftServer.getServer()!= null)
            dimensionManager = new DimensionManager(newSettings,dimensionSettings,MinecraftServer.getServer());
        }
        if (dimensionManager != null) {
            dimensionManager.onBiomeGenInit(event);
        }
    }

    public void logBiomes(){
        BiomeGenBase [] biomes = BiomeGenBase.getBiomeGenArray();
        for (BiomeGenBase biome: biomes ) {
            if (biome == null) continue;
            logger.info(biome.biomeName+" "+biome.biomeID+ " temp " +biome.getTempCategory().toString() +
                   " " + biome.getFloatTemperature(0, 64, 0)+ " rain "  + biome.getFloatRainfall());
        }
    }

}
