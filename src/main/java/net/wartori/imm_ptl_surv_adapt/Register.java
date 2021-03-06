package net.wartori.imm_ptl_surv_adapt;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.wartori.imm_ptl_surv_adapt.Blocks.*;
import net.wartori.imm_ptl_surv_adapt.Commands.ArgumentTypes.DirectionArgumentType;
import net.wartori.imm_ptl_surv_adapt.Commands.CreatePortalWithRelativeDestination;
import net.wartori.imm_ptl_surv_adapt.Commands.ImmersivePortalsSurvivalAdaptationGive;
import net.wartori.imm_ptl_surv_adapt.Commands.ToPortalWithRelativeCoordinates;
import net.wartori.imm_ptl_surv_adapt.Items.*;
import net.wartori.imm_ptl_surv_adapt.Networking.AnalyzeC2SPackets;
import net.wartori.imm_ptl_surv_adapt.PortalGenForm.PortalBlockForm;
import net.wartori.imm_ptl_surv_adapt.Portals.PortalMirrorWithRelativeCoordinates;
import net.wartori.imm_ptl_surv_adapt.Portals.PortalWithRelativeCoordinates;
import net.wartori.imm_ptl_surv_adapt.features.NonEuclideanHouseFeature;
import net.wartori.imm_ptl_surv_adapt.generators.NonEuclideanHouseGenerator;
import net.wartori.imm_ptl_surv_adapt.status_effects.PortalByPass;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.block.Block;

import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.Potion;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import com.google.common.collect.Lists;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import qouteall.imm_ptl.core.portal.custom_portal_gen.CustomPortalGeneration;


import java.util.ArrayList;
import java.util.function.Consumer;

public class Register {

    public static final Item PORTAL_MODIFICATOR_ITEM = new PortalModificatorItem(new Item.Settings().maxCount(1).maxDamage(100).group(ItemGroup.TOOLS));
    public static final Item PORTAL_MODIFICATOR_DISTANCE_MODIFIER_ITEM = new PortalModificatorDistanceModifier(new Item.Settings().maxCount(1).group(ItemGroup.TOOLS));
    public static final Item PORTAL_MODIFICATOR_ROTATION_MODIFIER_ITEM = new PortalModificatorRotationModifier(new Item.Settings().maxCount(1).group(ItemGroup.TOOLS));
    public static final Item PORTAL_CREATOR_ONE_WAY = new PortalCreatorOneWay(new Item.Settings().maxCount(1).group(ItemGroup.TOOLS));
    public static final Item PORTAL_INGOT = new PortalIngot(new Item.Settings().group(ItemGroup.MATERIALS));
    public static final Item PORTAL_INGOT_RAW = new PortalIngotRaw(new Item.Settings().group(ItemGroup.MATERIALS));
    public static final Item PORTAL_MODIFICATOR_DELETE_ITEM = new PortalModificatorDelete(new Item.Settings().group(ItemGroup.TOOLS).maxCount(1));
    public static final Item PORTAL_COMPLETER_ITEM = new PortalCompleter(new Item.Settings().maxCount(1).maxDamage(12).group(ItemGroup.TOOLS));
    public static final Item PORTAL_WRAPPING_ZONE =  new PortalWrappingZone(new Item.Settings().maxCount(1).group(ItemGroup.TOOLS));
    public static final Item PORTAL_CLAIMER_ITEM = new PortalClaimer(new Item.Settings().maxCount(1).group(ItemGroup.TOOLS));
    public static final Item PORTAL_DISCLAIMER_ITEM = new PortalDisclaimer(new Item.Settings().maxCount(1).group(ItemGroup.TOOLS));

    public static final Block PORTAL_BLOCK = new PortalBlock();
    public static final Block PORTAL_ORE = new PortalOre();
    public static final Block PORTAL_ORE_DEEPSLATE = new PortalOreDeepslate();
    public static final Block PORTAL_BLOCK_RAW = new PortalBlockRaw();
    public static final Block USED_PORTAL_BLOCK = new UsedPortalBlock();
    public static final Block WRAPPING_ZONE_START = new WrappingZoneStart();
    public static final Block WRAPPING_ZONE_END = new WrappingZoneEnd();


    public static final PortalBlockForm portalBlockForm = new PortalBlockForm();

    public static final StructurePieceType NON_EUCLIDEAN_HOUSE_PIECE = NonEuclideanHouseGenerator.NonEuclideanHousePiece::new;
    public static final StructureFeature<DefaultFeatureConfig> NON_EUCLIDEAN_HOUSE_STRUCTURE = new NonEuclideanHouseFeature(DefaultFeatureConfig.CODEC);
    public static final ConfiguredStructureFeature<DefaultFeatureConfig, ? extends StructureFeature<DefaultFeatureConfig>> NON_EUCLIDEAN_HOUSE_CONFIGURED = NON_EUCLIDEAN_HOUSE_STRUCTURE.configure(DefaultFeatureConfig.DEFAULT);

    public static final CustomPortalGeneration portalBlock = new CustomPortalGeneration(
            Lists.newArrayList(CustomPortalGeneration.anyDimension),
            CustomPortalGeneration.theSameDimension,
            1,
            1,
            false,
            portalBlockForm,
            null,
            new ArrayList<>(),
            new ArrayList<>()
    );

    // These three must be done in static, not in registerWorldGen
    public static final ImmutableList<OreFeatureConfig.Target> PORTAL_ORE_TARGETS =
            ImmutableList.of(OreFeatureConfig.createTarget(OreFeatureConfig.Rules.STONE_ORE_REPLACEABLES, PORTAL_ORE.getDefaultState()),
                    OreFeatureConfig.createTarget(OreFeatureConfig.Rules.DEEPSLATE_ORE_REPLACEABLES, PORTAL_ORE_DEEPSLATE.getDefaultState()));

    public static final OreFeatureConfig PORTAL_ORE_CONFIG = new OreFeatureConfig(PORTAL_ORE_TARGETS, 32);

    public static final ConfiguredFeature<?, ?> PORTAL_ORE_OVERWORLD = Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, Utils.myId("portal_ore_overworld"),
            ((Feature.ORE.configure(PORTAL_ORE_CONFIG).triangleRange(YOffset.aboveBottom(0), YOffset.fixed(64))).spreadHorizontally()).applyChance(3));

    public static final StatusEffect PORTAL_BY_PASS = new PortalByPass();

    protected static void registerItems() {
        Registry.register(Registry.ITEM, Utils.myId("portal_block"), new BlockItem(PORTAL_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)) {
            @Override
            public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
                if (this.isIn(group)  || RegisterItemGroups.IMMERSIVE_PORTALS_SURVIVAL_ADAPTATION_GROUP == group) {
                    stacks.add(new ItemStack(this));
                }
            }
        });
        Registry.register(Registry.ITEM, Utils.myId("portal_block_raw"), new BlockItem(PORTAL_BLOCK_RAW, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)) {
            @Override
            public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
                if (this.isIn(group)  || RegisterItemGroups.IMMERSIVE_PORTALS_SURVIVAL_ADAPTATION_GROUP == group) {
                    stacks.add(new ItemStack(this));
                }
            }
        });
        Registry.register(Registry.ITEM, Utils.myId("portal_modificator"), PORTAL_MODIFICATOR_ITEM);
        Registry.register(Registry.ITEM, Utils.myId("portal_modificator_distance_modifier"), PORTAL_MODIFICATOR_DISTANCE_MODIFIER_ITEM);
        Registry.register(Registry.ITEM, Utils.myId("portal_modificator_rotation_modifier"), PORTAL_MODIFICATOR_ROTATION_MODIFIER_ITEM);
        Registry.register(Registry.ITEM, Utils.myId("portal_creator_one_way"), PORTAL_CREATOR_ONE_WAY);
        Registry.register(Registry.ITEM, Utils.myId("portal_ore"), new BlockItem(PORTAL_ORE, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)) {
            @Override
            public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
                if (this.isIn(group) || RegisterItemGroups.IMMERSIVE_PORTALS_SURVIVAL_ADAPTATION_GROUP == group) {
                    stacks.add(new ItemStack(this));
                }
            }
        });
        Registry.register(Registry.ITEM, Utils.myId("portal_ore_deepslate"), new BlockItem(PORTAL_ORE_DEEPSLATE, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)) {
            @Override
            public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
                if (this.isIn(group) || RegisterItemGroups.IMMERSIVE_PORTALS_SURVIVAL_ADAPTATION_GROUP == group) {
                    stacks.add(new ItemStack(this));
                }
            }
        });
        Registry.register(Registry.ITEM, Utils.myId("portal_ingot"), PORTAL_INGOT);
        Registry.register(Registry.ITEM, Utils.myId("portal_ingot_raw"), PORTAL_INGOT_RAW);
        Registry.register(Registry.ITEM, Utils.myId("used_portal_block"), new BlockItem(USED_PORTAL_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)) {
            @Override
            public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
                if (this.isIn(group) || RegisterItemGroups.IMMERSIVE_PORTALS_SURVIVAL_ADAPTATION_GROUP == group) {
                    stacks.add(new ItemStack(this));
                }
            }
        });
        Registry.register(Registry.ITEM, Utils.myId("portal_modificator_delete"), PORTAL_MODIFICATOR_DELETE_ITEM);
        Registry.register(Registry.ITEM, Utils.myId("portal_completer"), PORTAL_COMPLETER_ITEM);
        Registry.register(Registry.ITEM, Utils.myId("wrapping_zone_start"), new BlockItem(WRAPPING_ZONE_START, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)) {
            @Override
            public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
                if (this.isIn(group) || RegisterItemGroups.IMMERSIVE_PORTALS_SURVIVAL_ADAPTATION_GROUP == group) {
                    stacks.add(new ItemStack(this));
                }
            }
        });
        Registry.register(Registry.ITEM, Utils.myId("wrapping_zone_end"), new BlockItem(WRAPPING_ZONE_END, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)) {
            @Override
            public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
                if (this.isIn(group)  || RegisterItemGroups.IMMERSIVE_PORTALS_SURVIVAL_ADAPTATION_GROUP == group) {
                    stacks.add(new ItemStack(this));
                }
            }
        });
        Registry.register(Registry.ITEM, Utils.myId("portal_wrapping_zone"), PORTAL_WRAPPING_ZONE);
        Registry.register(Registry.ITEM, Utils.myId("portal_claimer"), PORTAL_CLAIMER_ITEM);
        Registry.register(Registry.ITEM, Utils.myId("portal_disclaimer"), PORTAL_DISCLAIMER_ITEM);
    }

    protected static void registerBlocks() {
        Registry.register(Registry.BLOCK, Utils.myId("portal_block"), PORTAL_BLOCK);
        Registry.register(Registry.BLOCK, Utils.myId("portal_ore"), PORTAL_ORE);
        Registry.register(Registry.BLOCK, Utils.myId("portal_ore_deepslate"), PORTAL_ORE_DEEPSLATE);
        Registry.register(Registry.BLOCK, Utils.myId("portal_block_raw"), PORTAL_BLOCK_RAW);
        Registry.register(Registry.BLOCK, Utils.myId("used_portal_block"), USED_PORTAL_BLOCK);
        Registry.register(Registry.BLOCK, Utils.myId("wrapping_zone_start"), WRAPPING_ZONE_START);
        Registry.register(Registry.BLOCK, Utils.myId("wrapping_zone_end"), WRAPPING_ZONE_END);
    }

    protected static void registerCommands() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            ImmersivePortalsSurvivalAdaptationGive.register(dispatcher);
            CreatePortalWithRelativeDestination.register(dispatcher);
            ToPortalWithRelativeCoordinates.register(dispatcher);
        }));
    }

    protected static void registerArgumentTypes() {
        ArgumentTypes.register("imm_ptl_surv_adapt:direction", DirectionArgumentType.class, new ConstantArgumentSerializer(DirectionArgumentType::direction));
    }

    protected static void registerEntity() {
        registerEntity(o -> PortalWithRelativeCoordinates.entityType = o,
                "imm_ptl_surv_adapt:portal_with_relative_destination",
                PortalWithRelativeCoordinates::new
        );
        registerEntity(o -> PortalMirrorWithRelativeCoordinates.entityType = o,
                "imm_plt_surv_adapt:mirror_with_relative_destination",
                PortalMirrorWithRelativeCoordinates::new);

    }


    @SuppressWarnings("deprecation")
    protected static void registerStructures() {
        Registry.register(Registry.STRUCTURE_PIECE, Utils.myId("non_eucledian_house_piece"), NON_EUCLIDEAN_HOUSE_PIECE);
        FabricStructureBuilder.create(Utils.myId("non_eucledian_house_structure"), NON_EUCLIDEAN_HOUSE_STRUCTURE)
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)
                .defaultConfig(30, 2, 1213)
                .adjustsSurface()
                .register();

        RegistryKey<ConfiguredStructureFeature<?, ?>> nonEuclideanHouseConfigured = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY,
                Utils.myId("non_eucledian_house_structure"));
        BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, nonEuclideanHouseConfigured.getValue(), NON_EUCLIDEAN_HOUSE_CONFIGURED);
        BiomeModifications.addStructure(BiomeSelectors.categories(Biome.Category.PLAINS,
                Biome.Category.MESA,
                Biome.Category.FOREST,
                Biome.Category.SAVANNA,
                Biome.Category.TAIGA),
                nonEuclideanHouseConfigured);
    }

    /*
    Copy of com.qouteall.hiding_in_the_bushes.MyRegistry.registerEntity.
     */
    private static <T extends Entity> void registerEntity(
            Consumer<EntityType<T>> setEntityType,
            String id,
            EntityType.EntityFactory<T> constructor
    ) {
        EntityType<T> entityType = FabricEntityTypeBuilder.create(
                SpawnGroup.MISC,
                constructor
        ).dimensions(
                new EntityDimensions(1, 1, true)
        ).fireImmune()
                .trackRangeBlocks(96)
                .trackedUpdateRate(20)
                .forceTrackedVelocityUpdates(true)
                .build();
        setEntityType.accept(entityType);
        Registry.register(
                Registry.ENTITY_TYPE,
                new Identifier(id),
                entityType
        );
    }

    protected static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(Utils.myId("update_portal_modificator"), (AnalyzeC2SPackets::executePacketUpdatePortalModificator));
        ServerPlayNetworking.registerGlobalReceiver(Utils.myId("update_portal_creator"), (AnalyzeC2SPackets::executePacketUpdatePortalCreator));
        ServerPlayNetworking.registerGlobalReceiver(Utils.myId("update_portal_completer"), (AnalyzeC2SPackets::executePacketUpdatePortalCompleter));

    }

    protected static void registerStatusEffects() {
        Registry.register(Registry.STATUS_EFFECT, Utils.myId("portal_bypass"), PORTAL_BY_PASS);
        Registry.register(Registry.POTION, Utils.myId("portal_bypass"), new Potion("Portal bypass", new StatusEffectInstance(PORTAL_BY_PASS, 20*60*3)));
        Registry.register(Registry.POTION, Utils.myId("portal_bypass_long"), new Potion("Portal bypass", new StatusEffectInstance(PORTAL_BY_PASS, 20*60*3*2)));
    }
}
