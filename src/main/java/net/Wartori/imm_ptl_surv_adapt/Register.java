package net.Wartori.imm_ptl_surv_adapt;

import com.qouteall.immersive_portals.portal.custom_portal_gen.CustomPortalGeneration;
import net.Wartori.imm_ptl_surv_adapt.Blocks.PortalBlock;
import net.Wartori.imm_ptl_surv_adapt.Blocks.PortalOre;
import net.Wartori.imm_ptl_surv_adapt.Blocks.UsedPortalBlock;
import net.Wartori.imm_ptl_surv_adapt.Commands.ArgumentTypes.DirectionArgumentType;
import net.Wartori.imm_ptl_surv_adapt.Commands.CreatePortalWithRelativeDestination;
import net.Wartori.imm_ptl_surv_adapt.Commands.ImmersivePortalsSurvivalAdaptationGive;
import net.Wartori.imm_ptl_surv_adapt.Commands.ToPortalWithRelativeCoordinates;
import net.Wartori.imm_ptl_surv_adapt.Guis.ConfigurePortalCompleterGui;
import net.Wartori.imm_ptl_surv_adapt.Items.*;
import net.Wartori.imm_ptl_surv_adapt.Networking.AnalizeC2SPackets;
import net.Wartori.imm_ptl_surv_adapt.PortalGenForm.PortalBlockForm;
import net.Wartori.imm_ptl_surv_adapt.Portals.PortalMirrorWithRelativeCoordinates;
import net.Wartori.imm_ptl_surv_adapt.Portals.PortalWithRelativeCoordinates;
import net.Wartori.imm_ptl_surv_adapt.features.NonEuclideanHouseFeature;
import net.Wartori.imm_ptl_surv_adapt.generators.NonEuclideanHouseGenerator;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.block.Block;

import net.minecraft.command.argument.ArgumentTypes;
import net.minecraft.command.argument.serialize.ConstantArgumentSerializer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.*;
import com.google.common.collect.Lists;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Register {
    public static final Item PORTAL_MODIFICATOR_ITEM = new PortalModificatorItem(new Item.Settings().maxCount(1).maxDamage(100));
    public static final Item PORTAL_MODIFICATOR_DISTANCE_MODIFIER_ITEM = new PortalModificatorDistanceModifier(new Item.Settings().maxCount(1));
    public static final Item PORTAL_MODIFICATOR_ROTATION_MODIFIER_ITEM = new PortalModificatorRotationModifier(new Item.Settings().maxCount(1));
    public static final Item PORTAL_CREATOR_ONE_WAY = new PortalCreatorOneWay(new Item.Settings().group(ItemGroup.MISC).maxCount(1));
    public static final Item PORTAL_INGOT = new PortalIngot(new Item.Settings().group(ItemGroup.MISC));
    public static final Item PORTAL_MODIFICATOR_DELETE_ITEM = new PortalModificatorDelete(new Item.Settings().group(ItemGroup.MISC).maxCount(1));
    public static final Item PORTAL_COMPLETER_ITEM = new PortalCompleter(new Item.Settings().maxCount(1).maxDamage(12));

    public static final Block PORTAL_BLOCK = new PortalBlock();
    public static final Block PORTAL_ORE = new PortalOre();
    public static final Block USED_PORTAL_BLOCK = new UsedPortalBlock();

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

    private static final ConfiguredFeature<?, ?> PORTAL_ORE_OVERWORLD = Feature.ORE
            .configure(new OreFeatureConfig(
                    OreFeatureConfig.Rules.BASE_STONE_OVERWORLD,
                    PORTAL_ORE.getDefaultState(),
                    32)) // vein size
            .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(
                    0,
                    0,
                    64)))
            .spreadHorizontally()
            .applyChance(4);

    protected static void registerItems() {
        Registry.register(Registry.ITEM, Utils.myId("portal_block"), new BlockItem(PORTAL_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        Registry.register(Registry.ITEM, Utils.myId("portal_modificator"), PORTAL_MODIFICATOR_ITEM);
        Registry.register(Registry.ITEM, Utils.myId("portal_modificator_distance_modifier"), PORTAL_MODIFICATOR_DISTANCE_MODIFIER_ITEM);
        Registry.register(Registry.ITEM, Utils.myId("portal_modificator_rotation_modifier"), PORTAL_MODIFICATOR_ROTATION_MODIFIER_ITEM);
        Registry.register(Registry.ITEM, Utils.myId("portal_creator_one_way"), PORTAL_CREATOR_ONE_WAY);
        Registry.register(Registry.ITEM, Utils.myId("portal_ore"), new BlockItem(PORTAL_ORE, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        Registry.register(Registry.ITEM, Utils.myId("portal_ingot"), PORTAL_INGOT);
        Registry.register(Registry.ITEM, Utils.myId("used_portal_block"), new BlockItem(USED_PORTAL_BLOCK, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
        Registry.register(Registry.ITEM, Utils.myId("portal_modificator_delete"), PORTAL_MODIFICATOR_DELETE_ITEM);
        Registry.register(Registry.ITEM, Utils.myId("portal_completer"), PORTAL_COMPLETER_ITEM);

    }

    protected static void registerBlocks() {
        Registry.register(Registry.BLOCK, Utils.myId("portal_block"), PORTAL_BLOCK);
        Registry.register(Registry.BLOCK, Utils.myId("portal_ore"), PORTAL_ORE);
        Registry.register(Registry.BLOCK, Utils.myId("used_portal_block"), USED_PORTAL_BLOCK);
    }

    protected static void registerWorldGen() {
        RegistryKey<ConfiguredFeature<?, ?>> orePortalOverworld = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN,
                Utils.myId("ore_portal_overworld"));
        Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, orePortalOverworld.getValue(), PORTAL_ORE_OVERWORLD);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.UNDERGROUND_ORES, orePortalOverworld);
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
        DefaultedRegistry<EntityType<?>> registry = Registry.ENTITY_TYPE;
        registerEntity(o -> PortalWithRelativeCoordinates.entityType = o,
                () -> PortalWithRelativeCoordinates.entityType,
                "imm_ptl_surv_adapt:portal_with_relative_destination",
                PortalWithRelativeCoordinates::new,
                registry
        );
        registerEntity(o -> PortalMirrorWithRelativeCoordinates.entityType = o,
                () -> PortalMirrorWithRelativeCoordinates.entityType,
                "imm_plt_surv_adapt:mirror_with_relative_destination",
                PortalMirrorWithRelativeCoordinates::new,
                registry);

    }



    protected static void registerStructures() {
        Registry.register(Registry.STRUCTURE_PIECE, Utils.myId("non_eucledian_house_piece"), NON_EUCLIDEAN_HOUSE_PIECE);
        FabricStructureBuilder.create(Utils.myId("non_eucledian_house_structure"), NON_EUCLIDEAN_HOUSE_STRUCTURE)
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)
                .defaultConfig(30, 2, 1213)
                .adjustsSurface()
                .register();

        RegistryKey<ConfiguredStructureFeature<?, ?>> nonEuclideanHouseConfigured = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_WORLDGEN,
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
            Supplier<EntityType<T>> getEntityType,
            String id,
            EntityType.EntityFactory<T> constructor,
            Registry<EntityType<?>> registry
    ) {
        EntityType<T> entityType = FabricEntityTypeBuilder.create(
                SpawnGroup.MISC,
                constructor
        ).dimensions(
                new EntityDimensions(1, 1, true)
        ).fireImmune().trackable(96, 20).build();
        setEntityType.accept(entityType);
        Registry.register(
                Registry.ENTITY_TYPE,
                new Identifier(id),
                entityType
        );
    }

    protected static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(Utils.myId("update_portal_modificator"), (AnalizeC2SPackets::executePacketUpdatePortalModificator));
        ServerPlayNetworking.registerGlobalReceiver(Utils.myId("update_portal_creator"), (AnalizeC2SPackets::executePacketUpdatePortalCreator));
        ServerPlayNetworking.registerGlobalReceiver(Utils.myId("update_portal_completer"), (AnalizeC2SPackets::executePacketUpdatePortalCompleter));

    }

}
