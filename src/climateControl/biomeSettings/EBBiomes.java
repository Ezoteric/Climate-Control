package climateControl.biomeSettings;

import climateControl.utils.Mutable;
import com.google.common.base.Optional;
import enhancedbiomes.EnhancedBiomesMod;
//import enhancedbiomes.api.EBAPI;
import climateControl.utils.Settings;
import java.io.File;
import net.minecraftforge.common.config.Configuration;


public class EBBiomes {

    NativeSettings settings = new NativeSettings();
    EBBiome AlpineMountains;
	EBBiome AlpineMountainsEdge;
	EBBiome AlpineMountainsM;
	EBBiome AlpineTundra;
	EBBiome AspenForest;
	EBBiome AspenHills;
	EBBiome Badlands;
	EBBiome Basin;
	EBBiome BlossomHills;
	EBBiome BlossomWoods;
	EBBiome BorealArchipelago;
	EBBiome BorealForest;
	EBBiome BorealPlateau;
	EBBiome BorealPlateauM;
	EBBiome Carr;
	EBBiome ClayHills;
	EBBiome Clearing;
	EBBiome ColdBorealForest;
	EBBiome ColdCypressForest;
	EBBiome ColdFirForest;
	EBBiome ColdPineForest;
	EBBiome CypressForest;
	EBBiome DesertArchipelago;
	EBBiome EphemeralLake;
	EBBiome EphemeralLakeEdge;
	EBBiome Fens;
	EBBiome FirForest;
	EBBiome FloweryArchipelago;
	EBBiome ForestedArchipelago;
	EBBiome ForestedMountains;
	EBBiome ForestedValley;
	EBBiome FrozenArchipelago;
	EBBiome Glacier;
	EBBiome GrassyArchipelago;
	EBBiome IceSheet;
	EBBiome Kakadu;
	EBBiome Lake;
	EBBiome LowHills;
	EBBiome Mangroves;
	EBBiome Marsh;
	EBBiome Meadow;
	EBBiome MeadowM;
	EBBiome MountainousArchipelago;
	EBBiome Mountains;
	EBBiome MountainsEdge;
	EBBiome OakForest;
	EBBiome Oasis;
	EBBiome PineForest;
	EBBiome PineForestArchipelago;
	EBBiome Plateau;
	EBBiome PolarDesert;
	EBBiome Prairie;
	EBBiome Rainforest;
	EBBiome RainforestValley;
	EBBiome RedDesert;
	EBBiome RockyDesert;
	EBBiome RockyHills;
	EBBiome RoofedShrublands;
	EBBiome Sahara;
	EBBiome SandstoneCanyon;
	EBBiome SandstoneCanyons;
	EBBiome SandstoneRanges;
	EBBiome SandstoneRangesM;
	EBBiome Scree;
	EBBiome Scrub;
	EBBiome Shield;
	EBBiome Shrublands;
	EBBiome SilverPineForest;
	EBBiome SilverPineHills;
	EBBiome SnowyDesert;
	EBBiome SnowyPlateau;
	EBBiome SnowyRanges;
	EBBiome SnowyWastelands;
	EBBiome Steppe;
	EBBiome StoneCanyon;
	EBBiome StoneCanyons;
	EBBiome TropicalArchipelago;
	EBBiome Tundra;
	EBBiome Volcano;
	EBBiome VolcanoM;
	EBBiome Wastelands;
	EBBiome WoodlandField;
	EBBiome WoodlandHills;
	EBBiome WoodlandLake;
	EBBiome WoodlandLakeEdge;
	EBBiome Woodlands;
	EBBiome XericSavannah;
	EBBiome XericShrubland;
    
    public EBBiomes(File configDirectory) {
        try {
        	AlpineMountains = new EBBiomes(EnhancedBiomesMod.ALPINEMOUNTAINS,"alpineMountains");
        	AlpineMountainsEdge;
        	AlpineMountainsM;
        	AlpineTundra;
        	AspenForest;
        	AspenHills;
        	Badlands;
        	Basin;
        	BlossomHills;	
        	BlossomWoods;
        	BorealArchipelago;
        	BorealForest;
        	BorealPlateau;
        	BorealPlateauM;
        	Carr;
        	ClayHills;
        	Clearing;
        	ColdBorealForest;
        	ColdCypressForest;
        	ColdFirForest;
        	ColdPineForest;
        	CypressForest;
        	DesertArchipelago;
        	EphemeralLake;
        	EphemeralLakeEdge;
        	Fens;
        	FirForest;
        	FloweryArchipelago;
        	ForestedArchipelago;
        	ForestedMountains;
        	ForestedValley;
        	FrozenArchipelago;
        	Glacier;
        	GrassyArchipelago;
        	IceSheet;
        	Kakadu;
        	Lake;
        	LowHills;
        	Mangroves;
        	Marsh;
        	Meadow;
        	MeadowM;
        	MountainousArchipelago;
        	Mountains;
        	MountainsEdge;
        	OakForest;
        	Oasis;
        	PineForest;
        	PineForestArchipelago;
        	Plateau;
        	PolarDesert;
        	Prairie;
        	Rainforest;
        	RainforestValley;
 			RedDesert;
 			RockyDesert;
 			RockyHills;
 			RoofedShrublands;
 			Sahara;
 			SandstoneCanyon;
 			SandstoneCanyons;
 			SandstoneRanges;
 			SandstoneRangesM;
 			Scree;
 			Scrub;
 			Shield;
 			Shrublands;
 			SilverPineForest;
 			SilverPineHills;
 			SnowyDesert;
 			SnowyPlateau;
 			SnowyRanges;
 			SnowyWastelands;
 			Steppe;
 			StoneCanyon;
 			StoneCanyons;
 			TropicalArchipelago;
 			Tundra;
 			Volcano;
 			VolcanoM;
 			Wastelands;
 			WoodlandField;
 			WoodlandHills;
 			WoodlandLake;
 			WoodlandLakeEdge;
 			Woodlands;
 			XericSavannah;
 			XericShrubland = new EBBiome(BiomeSettings.XERICSHRUBLAND,"xericShrubland");
 
   /*     alpine = new EBXLBiome(BiomeSettings.ALPINE,"alpine");
        autumnwoods = new EBXLBiome(BiomeSettings.AUTUMNWOODS,"autumnwoods");
        birchforest = new EBXLBiome(BiomeSettings.BIRCHFOREST,"birchforest");
        this.extremejungle = new EBXLBiome(BiomeSettings.EXTREMEJUNGLE,"extremejungle");
       */
        settings.readFrom(new Configuration(new File(configDirectory,"enhancedbiomes.cfg")));
        } catch (java.lang.NoClassDefFoundError e){
            // EBXL isn't installed
        }
    }

    public class EBLBiome {
        private final BiomeSettings source;
        private final String configName;
        private final Mutable<Integer> nativeID;
        private EBLBiome(BiomeSettings source, String configName) {
            this.source = source;
            this.configName = configName;
            nativeID = settings.biomeCategory.intSetting(configName+".id", nativeBiomeID());
        }

        public int nativeBiomeID() {
            if (source.isEnabled()) return source.getID();
            return -1;
        }

        public int biomeID() {
            return nativeID.value();
        }
    }

    private class NativeSettings extends Settings {
        public final Category biomeCategory = new Category("biome");

    }

}
