package com.fabbe50.fabsbnb.registries;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.ModConfig;
import com.fabbe50.fabsbnb.data.OwnTippedArrowRecipe;
import com.fabbe50.fabsbnb.util.LangUtils;
import com.fabbe50.fabsbnb.world.block.*;
import com.fabbe50.fabsbnb.world.block.base.ExtTransparentBlock;
import com.fabbe50.fabsbnb.world.block.entity.BlockBreakerBlockEntity;
import com.fabbe50.fabsbnb.world.block.entity.BlockDetectorBlockEntity;
import com.fabbe50.fabsbnb.world.block.entity.BlockPlacerBlockEntity;
import com.fabbe50.fabsbnb.world.block.entity.XPHolderBlockEntity;
import com.fabbe50.fabsbnb.world.effects.MobEffectExt;
import com.fabbe50.fabsbnb.world.inventory.BlockBreakerMenu;
import com.fabbe50.fabsbnb.world.item.*;
import com.fabbe50.fabsbnb.world.item.base.ModBlockItem;
import com.fabbe50.fabsbnb.world.item.base.ModItem;
import com.fabbe50.fabsbnb.world.item.enchantments.*;
import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ColorRGBA;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class ModRegistries {
    // Constants
    public static final int SHORT_DURATION_POTION = 3600;
    public static final int LONG_DURATION_POTION = 9600;
    public static final int STRONG_POTION = 450;

    // Registry Lists
    public static final List<RegistrySupplier<Item>> ITEM_LIST = new ArrayList<>();
    public static final List<RegistrySupplier<Item>> CREATIVE_ITEM_LIST = new ArrayList<>();
    public static final List<RegistrySupplier<Item>> NORMAL_ITEM_LIST = new ArrayList<>();
    public static final List<RegistrySupplier<Item>> BLOCK_ITEM_LIST = new ArrayList<>();
    public static final List<RegistrySupplier<Block>> BLOCK_LIST = new ArrayList<>();
    public static final List<RegistrySupplier<Block>> NORMAL_BLOCK_LIST = new ArrayList<>();
    public static final List<RegistrySupplier<Potion>> POTION_LIST = new ArrayList<>();
    public static final List<IEnchantment> ENCHANTMENT_LIST = new ArrayList<>();

    // Registrars
    private static final Registrar<CreativeModeTab>             TABS                                                    = FabsBnB.MANAGER.get().get(Registries.CREATIVE_MODE_TAB);
    public  static final Registrar<Block>                       BLOCKS                                                  = FabsBnB.MANAGER.get().get(Registries.BLOCK);
    private static final Registrar<BlockEntityType<?>>          BLOCK_ENTITIES                                          = FabsBnB.MANAGER.get().get(Registries.BLOCK_ENTITY_TYPE);
    public  static final Registrar<Item>                        ITEMS                                                   = FabsBnB.MANAGER.get().get(Registries.ITEM);
    private static final Registrar<MobEffect>                   MOB_EFFECTS                                             = FabsBnB.MANAGER.get().get(Registries.MOB_EFFECT);
    private static final Registrar<Potion>                      POTIONS                                                 = FabsBnB.MANAGER.get().get(Registries.POTION);
    private static final Registrar<ConsumeEffect.Type<?>>       CONSUME_EFFECT_TYPES                                    = FabsBnB.MANAGER.get().get(Registries.CONSUME_EFFECT_TYPE);
    private static final Registrar<MenuType<?>>                 MENU_TYPES                                              = FabsBnB.MANAGER.get().get(Registries.MENU);
    private static final Registrar<RecipeSerializer<?>>         RECIPE_SERIALIZERS                                      = FabsBnB.MANAGER.get().get(Registries.RECIPE_SERIALIZER);

    // Blocks
    public static final RegistrySupplier<Block> LAVA_SPONGE                                                             = registerBlock("lava_sponge", properties -> new CustomSpongeBlock(FluidTags.LAVA, properties));
    public static final RegistrySupplier<Block> LAVA_SPONGE_USED                                                        = registerBlock("lava_sponge_used", LavaUsedSpongeBlock::new);
    public static final RegistrySupplier<Block> PUSHER_BLOCK                                                            = registerBlock("pusher_block", PusherBlock::new, true);
    public static final RegistrySupplier<Block> THIN_LIGHT                                                              = registerBlock("thin_light", properties -> new ThinLightBlock(properties.lightLevel(litBlockEmission(15))), true);
    public static final RegistrySupplier<Block> POWERED_THIN_LIGHT                                                      = registerBlock("powered_thin_light", properties -> new PoweredThinLightBlock(properties.lightLevel(value -> 15)), true);
    public static final RegistrySupplier<Block> BLOCK_PLACER                                                            = registerBlock("block_placer", BlockPlacerBlock::new, true);
    public static final RegistrySupplier<Block> BLOCK_BREAKER                                                           = registerBlock("block_breaker", BlockBreakerBlock::new, true);
    public static final RegistrySupplier<Block> BLOCK_DETECTOR                                                          = registerBlock("block_detector", BlockDetectorBlock::new, true);
    public static final RegistrySupplier<Block> XP_HOLDER                                                               = registerBlock("xp_holder", XPHolderBlock::new, true);
    public static final RegistrySupplier<Block> SLIME_SAND                                                              = registerBlock("slime_sand", properties -> new ColoredFallingBlock(new ColorRGBA(0x556945), properties.strength(0.3F, 1200.0F).sound(SoundType.SLIME_BLOCK).instabreak().isValidSpawn(ModRegistries::never).friction(0.8F)));
    public static final RegistrySupplier<Block> STRUCTURAL_GOOP                                                         = registerBlock("structural_goop", properties -> new ExtTransparentBlock(4, properties.strength(0.0F, 3600000.0F).sound(SoundType.SLIME_BLOCK).forceSolidOn().noOcclusion().noCollission().isValidSpawn(ModRegistries::never).isRedstoneConductor(ModRegistries::never).isSuffocating(ModRegistries::never).isViewBlocking(ModRegistries::never)));
    public static final RegistrySupplier<Block> STRUCTURAL_GLASS                                                        = registerBlock("structural_glass", properties -> new ExtTransparentBlock(2, properties.strength(0.3F, 3600000.0F).sound(SoundType.SLIME_BLOCK).noOcclusion().isValidSpawn(ModRegistries::never).isRedstoneConductor(ModRegistries::never).isSuffocating(ModRegistries::never).isViewBlocking(ModRegistries::never)));

    private static RegistrySupplier<Block> registerBlock(String name, Function<BlockBehaviour.Properties, Block> function) {
        return registerBlock(name, function, BlockBehaviour.Properties.of());
    }

    private static RegistrySupplier<Block> registerBlock(String name, Function<BlockBehaviour.Properties, Block> function, boolean customModel) {
        return registerBlock(name, function, true, customModel);
    }

    private static RegistrySupplier<Block> registerBlock(String name, Function<BlockBehaviour.Properties, Block> function, boolean makeKnown, boolean customModel) {
        return registerBlock(name, function, BlockBehaviour.Properties.of(), makeKnown, customModel);
    }

    private static RegistrySupplier<Block> registerBlock(String name, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties) {
        return registerBlock(name, function, properties, false);
    }

    private static RegistrySupplier<Block> registerBlock(String name, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties, boolean customModel) {
        return registerBlock(name, function, properties, true, customModel);
    }

    private static RegistrySupplier<Block> registerBlock(String name, Function<BlockBehaviour.Properties, Block> function, BlockBehaviour.Properties properties, boolean makeKnown, boolean customModel) {
        RegistrySupplier<Block> block = BLOCKS.register(FabsBnB.location(name), () -> function.apply(properties.setId(FabsBnB.key(Registries.BLOCK, name))));
        if (makeKnown) {
            BLOCK_LIST.add(block);
        }
        if (!customModel) {
            NORMAL_BLOCK_LIST.add(block);
        }
        return block;
    }

    private static ToIntFunction<BlockState> litBlockEmission(int i) {
        return (blockState) -> (Boolean)blockState.getValue(BlockStateProperties.LIT) ? i : 0;
    }

    private static Boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return false;
    }

    // Block Entities
    public static final RegistrySupplier<BlockEntityType<BlockPlacerBlockEntity>> BLOCK_PLACER_BLOCK_ENTITY             = BLOCK_ENTITIES.register(FabsBnB.location("block_placer_block_entity"), Suppliers.memoize(() -> new BlockEntityType<>(BlockPlacerBlockEntity::new, Set.of(BLOCK_PLACER.get()))));
    public static final RegistrySupplier<BlockEntityType<BlockBreakerBlockEntity>> BLOCK_BREAKER_BLOCK_ENTITY           = BLOCK_ENTITIES.register(FabsBnB.location("block_breaker_block_entity"), Suppliers.memoize(() -> new BlockEntityType<>(BlockBreakerBlockEntity::new, Set.of(BLOCK_BREAKER.get()))));
    public static final RegistrySupplier<BlockEntityType<BlockDetectorBlockEntity>> BLOCK_DETECTOR_BLOCK_ENTITY         = BLOCK_ENTITIES.register(FabsBnB.location("block_detector_block_entity"), Suppliers.memoize(() -> new BlockEntityType<>(BlockDetectorBlockEntity::new, Set.of(BLOCK_DETECTOR.get()))));
    public static final RegistrySupplier<BlockEntityType<XPHolderBlockEntity>> XP_HOLDER_BLOCK_ENTITY                   = BLOCK_ENTITIES.register(FabsBnB.location("xp_holder_block_entity"), Suppliers.memoize(() -> new BlockEntityType<>(XPHolderBlockEntity::new, Set.of(XP_HOLDER.get()))));

    // Items
    public static final RegistrySupplier<Item> WOODEN_BUILDING_WAND                                                     = registerItem("wooden_building_wand", properties -> new BuildingWandItem(ToolMaterial.WOOD, properties));
    public static final RegistrySupplier<Item> STONE_BUILDING_WAND                                                      = registerItem("stone_building_wand", properties -> new BuildingWandItem(ToolMaterial.STONE, properties));
    public static final RegistrySupplier<Item> IRON_BUILDING_WAND                                                       = registerItem("iron_building_wand", properties -> new BuildingWandItem(ToolMaterial.IRON, properties));
    public static final RegistrySupplier<Item> GOLD_BUILDING_WAND                                                       = registerItem("gold_building_wand", properties -> new BuildingWandItem(ToolMaterial.GOLD, properties));
    public static final RegistrySupplier<Item> DIAMOND_BUILDING_WAND                                                    = registerItem("diamond_building_wand", properties -> new BuildingWandItem(ToolMaterial.DIAMOND, properties));
    public static final RegistrySupplier<Item> NETHERITE_BUILDING_WAND                                                  = registerItem("netherite_building_wand", properties -> new BuildingWandItem(ToolMaterial.NETHERITE, properties));
    public static final RegistrySupplier<Item> ITEM_BLOCK_YOINKER                                                       = registerItem("block_yoinker", BlockYoinkerItem::new);
    public static final RegistrySupplier<Item> WHOOSH_WAND                                                              = registerItem("whoosh_wand", WhooshWandItem::new);
    public static final RegistrySupplier<Item> CAT_CLAW                                                                 = registerItem("cat_claw", ModItem::new);
    public static final RegistrySupplier<Item> WRENCH                                                                   = registerItem("wrench", WrenchItem::new);
    public static final RegistrySupplier<Item> MILK_BOTTLE                                                              = registerItem("milk_bottle", MilkBottleItem::new);
    public static final RegistrySupplier<Item> CHOCOLATE_MILK_BOTTLE                                                    = registerItem("chocolate_milk_bottle", ChocolateMilkBottleItem::new);
    public static final RegistrySupplier<Item> CHOCOLATE_NECKLACE                                                       = registerItem("chocolate_necklace", ChocolateNecklaceItem::new);
    public static final RegistrySupplier<Item> WAND_OF_HOLDING                                                          = registerItem("holding_wand", HolderWandItem::new);
    public static final RegistrySupplier<Item> ITEM_LAVA_SPONGE                                                         = registerItemBlock("lava_sponge", LAVA_SPONGE);
    public static final RegistrySupplier<Item> ITEM_LAVA_SPONGE_USED                                                    = registerItemBlock("lava_sponge_used", LAVA_SPONGE_USED);
    public static final RegistrySupplier<Item> ITEM_PUSHER_BLOCK                                                        = registerItemBlock("pusher_block", PUSHER_BLOCK);
    public static final RegistrySupplier<Item> ITEM_THIN_LIGHT                                                          = registerItemBlock("thin_light", THIN_LIGHT);
    public static final RegistrySupplier<Item> ITEM_POWERED_THIN_LIGHT                                                  = registerItemBlock("powered_thin_light", POWERED_THIN_LIGHT);
    public static final RegistrySupplier<Item> ITEM_BLOCK_PLACER                                                        = registerItemBlock("block_placer", BLOCK_PLACER);
    public static final RegistrySupplier<Item> ITEM_BLOCK_BREAKER                                                       = registerItemBlock("block_breaker", BLOCK_BREAKER);
    public static final RegistrySupplier<Item> ITEM_BLOCK_DETECTOR                                                      = registerItemBlock("block_detector", BLOCK_DETECTOR);
    public static final RegistrySupplier<Item> ITEM_SLIME_SAND                                                          = registerItemBlock("slime_sand", SLIME_SAND);
    public static final RegistrySupplier<Item> ITEM_STRUCTURAL_GOOP                                                     = registerItemBlock("structural_goop", STRUCTURAL_GOOP);
    public static final RegistrySupplier<Item> ITEM_STRUCTURAL_GLASS                                                    = registerItemBlock("structural_glass", STRUCTURAL_GLASS);
    public static final RegistrySupplier<Item> ITEM_XP_HOLDER                                                           = registerItemBlock("xp_holder", XP_HOLDER);
    public static final RegistrySupplier<Item> FULL_WATER_CAULDRON                                                      = registerItemBlock("water_cauldron", Blocks.WATER_CAULDRON, false);
    public static final RegistrySupplier<Item> EXT_ENCHANTED_BOOK                                                       = registerItemWithCustomModel("enchanted_book", properties -> new Item(properties.stacksTo(1).rarity(Rarity.UNCOMMON).component(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY).component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)), false, false);
    public static final RegistrySupplier<Item> OWN_POTION_ITEM                                                          = registerItemWithCustomModel("potion", properties -> new PotionItem(properties.stacksTo(16)), false, false);
    public static final RegistrySupplier<Item> OWN_SPLASH_POTION_ITEM                                                   = registerItemWithCustomModel("splash_potion", properties -> new SplashPotionItem(properties.stacksTo(16)), false, false);
    public static final RegistrySupplier<Item> OWN_LINGERING_POTION_ITEM                                                = registerItemWithCustomModel("lingering_potion", properties -> new LingeringPotionItem(properties.stacksTo(16)), false, false);
    public static final RegistrySupplier<Item> OWN_TIPPED_ARROW_ITEM                                                    = registerItemWithCustomModel("tipped_arrow", TippedArrowItem::new, false, false);

    private static RegistrySupplier<Item> registerItem(String name, Function<Item.Properties, Item> function) {
        return registerItem(name, function, true, true);
    }

    private static RegistrySupplier<Item> registerItemBlock(String name, RegistrySupplier<Block> block) {
        return registerItemBlock(name, block, true, true);
    }

    private static RegistrySupplier<Item> registerItemBlock(String name, RegistrySupplier<Block> block, boolean makeKnown) {
        return registerItemBlock(name, block, makeKnown, makeKnown);
    }

    private static RegistrySupplier<Item> registerItemBlock(String name, Block block, boolean makeKnown) {
        return registerItemBlock(name, block, makeKnown, makeKnown);
    }

    private static RegistrySupplier<Item> registerItemBlock(String name, RegistrySupplier<Block> block, boolean makeKnown, boolean addToTab) {
        RegistrySupplier<Item> item = registerItem(name, properties -> new ModBlockItem(block.get(), properties), makeKnown, addToTab);
        if (makeKnown) {
            BLOCK_ITEM_LIST.add(item);
        }
        return item;
    }

    private static RegistrySupplier<Item> registerItemBlock(String name, Block block, boolean makeKnown, boolean addToTab) {
        RegistrySupplier<Item> item = registerItem(name, properties -> new ModBlockItem(block, properties), makeKnown, addToTab);
        if (makeKnown) {
            BLOCK_ITEM_LIST.add(item);
        }
        return item;
    }

    private static RegistrySupplier<Item> registerItem(String name, Function<Item.Properties, Item> function, boolean makeKnown) {
        return registerItem(name, function, makeKnown, makeKnown);
    }

    private static RegistrySupplier<Item> registerItem(String name, Function<Item.Properties, Item> function, boolean makeKnown, boolean addToTab) {
        return registerItem(name, function, new Item.Properties(), makeKnown, addToTab);
    }

    private static RegistrySupplier<Item> registerItemWithCustomModel(String name, Function<Item.Properties, Item> function, boolean makeKnown, boolean addToTab) {
        return registerItemWithCustomModel(name, function, new Item.Properties(), makeKnown, addToTab);
    }

    private static RegistrySupplier<Item> registerItem(String name, Function<Item.Properties, Item> function, Item.Properties properties, boolean makeKnown, boolean addToTab) {
        return registerItem(name, function, properties, makeKnown, addToTab, false);
    }

    private static RegistrySupplier<Item> registerItemWithCustomModel(String name, Function<Item.Properties, Item> function, Item.Properties properties, boolean makeKnown, boolean addToTab) {
        return registerItem(name, function, properties, makeKnown, addToTab, true);
    }

    private static RegistrySupplier<Item> registerItem(String name, Function<Item.Properties, Item> function, Item.Properties properties, boolean makeKnown, boolean addToTab, boolean customModel) {
        RegistrySupplier<Item> item = ITEMS.register(FabsBnB.location(name), () -> function.apply(properties.setId(FabsBnB.key(Registries.ITEM, name))));
        if (makeKnown) {
            ITEM_LIST.add(item);
        }
        if (addToTab) {
            CREATIVE_ITEM_LIST.add(item);
        }
        if (!customModel) {
            NORMAL_ITEM_LIST.add(item);
        }
        return item;
    }

    // Mob Effects
    public static final RegistrySupplier<MobEffect> FELINE_AURA                                                         = MOB_EFFECTS.register(FabsBnB.location("feline_aura"), () -> new MobEffectExt(MobEffectCategory.BENEFICIAL, 0x939918));

    // Wrapper to fix RegistrySupplier not being able to save effects. Thanks to fzzyhamstrs on GitHub.
    public static Holder<MobEffect> getMobEffectReference(RegistrySupplier<MobEffect> input) {
        return MOB_EFFECTS.getHolder(input.getId());
    }

    // Potions
    public static final RegistrySupplier<Potion> FELINE_AURA_POTION_SHORT                                               = registerPotion("feline_aura_short", FELINE_AURA, SHORT_DURATION_POTION);
    public static final RegistrySupplier<Potion> FELINE_AURA_POTION_LONG                                                = registerPotion("feline_aura_long", FELINE_AURA, LONG_DURATION_POTION);
    public static final RegistrySupplier<Potion> SCUTE_POTION_SHORT                                                     = registerPotion("scute_potion_short", new MobEffectInstance(MobEffects.RESISTANCE, SHORT_DURATION_POTION, 5), new MobEffectInstance(MobEffects.STRENGTH, SHORT_DURATION_POTION, 2));
    public static final RegistrySupplier<Potion> SCUTE_POTION_LONG                                                      = registerPotion("scute_potion_long", new MobEffectInstance(MobEffects.RESISTANCE, LONG_DURATION_POTION, 5), new MobEffectInstance(MobEffects.STRENGTH, LONG_DURATION_POTION, 2));

    private static RegistrySupplier<Potion> registerPotion(String name, RegistrySupplier<MobEffect> effect, int duration) {
        RegistrySupplier<Potion> potion = POTIONS.register(FabsBnB.location(name), () -> new Potion(name, new MobEffectInstance(getMobEffectReference(effect), duration)));
        POTION_LIST.add(potion);
        return potion;
    }

    private static RegistrySupplier<Potion> registerPotion(String name, MobEffectInstance... mobEffectInstances) {
        RegistrySupplier<Potion> potion = POTIONS.register(FabsBnB.location(name), () -> new Potion(name, mobEffectInstances));
        POTION_LIST.add(potion);
        return potion;
    }

    // Wrapper to fix RegistrySupplier not being able to save potions. Thanks to fzzyhamstrs on GitHub.
    public static Holder<Potion> getPotionReference(RegistrySupplier<Potion> input) {
        return POTIONS.getHolder(input.getId());
    }

    public static Holder<Potion> getPotionReference(Potion input) {
        return POTIONS.getHolder(POTIONS.getId(input));
    }

    // Consume Effects
    public static final RegistrySupplier<ConsumeEffect.Type<DrinkItem.HandleDrink>> HANDLE_DRINK                        = CONSUME_EFFECT_TYPES.register(FabsBnB.location("handle_drink"), () -> new ConsumeEffect.Type<>(DrinkItem.HandleDrink.CODEC, DrinkItem.HandleDrink.STREAM_CODEC));


    // Creative Tabs
    public static final RegistrySupplier<CreativeModeTab> TAB                                                           = TABS.register(FabsBnB.location("tab"), () -> CreativeTabRegistry.create(LangUtils.MOD_NAME_C, () -> new ItemStack(DIAMOND_BUILDING_WAND.get())));

    // Menu Types
    public static final RegistrySupplier<MenuType<BlockBreakerMenu>> BLOCK_BREAKER_MENU                                 = MENU_TYPES.register(FabsBnB.location("block_breaker_menu"), () -> MenuRegistry.ofExtended(BlockBreakerMenu::new));

    // Recipes Serializers
    public static final RegistrySupplier<RecipeSerializer<OwnTippedArrowRecipe>> OWN_TIPPED_ARROW_RECIPE_SERIALIZER     = RECIPE_SERIALIZERS.register(FabsBnB.location("own_tipped_arrow_recipe"), () -> new CustomRecipe.Serializer<>(OwnTippedArrowRecipe::new));

    // Tags
    public static final TagKey<Item> BUILDING_WANDS                                                                     = TagKey.create(Registries.ITEM, FabsBnB.location("building_wands"));
    public static final TagKey<Item> IMMUNE_TO_CACTUS_DAMAGE                                                            = TagKey.create(Registries.ITEM, FabsBnB.location("immune_to_cactus"));
    public static final TagKey<Item> NETHERITE_ITEMS                                                                    = TagKey.create(Registries.ITEM, FabsBnB.location("c", "netherite_items"));
    public static final TagKey<Item> DIGGING_TOOLS                                                                      = TagKey.create(Registries.ITEM, FabsBnB.location("digging_tools"));
    public static final TagKey<Item> VAULT_UNLOCKERS                                                                    = TagKey.create(Registries.ITEM, FabsBnB.location("vault_unlockers"));
    public static final TagKey<Item> WAND_OF_HOLDING_ACCEPTS                                                            = TagKey.create(Registries.ITEM, FabsBnB.location("holding_wand_accepts"));
    public static final TagKey<Block> BLOCK_YOINKER_BLACKLIST                                                           = TagKey.create(Registries.BLOCK, FabsBnB.location("block_yoinker_blacklist"));
    public static final TagKey<Block> SPIDER_NOT_CLIMBABLE                                                              = TagKey.create(Registries.BLOCK, FabsBnB.location("spider_not_climbable"));
    public static final TagKey<Block> ORE_MINER_WHITELIST                                                               = TagKey.create(Registries.BLOCK, FabsBnB.location("ore_miner_whitelist"));
    public static final TagKey<Block> TREE_CHOPPER_WHITELIST                                                            = TagKey.create(Registries.BLOCK, FabsBnB.location("tree_chopper_whitelist"));
    public static final TagKey<Block> TREE_CHOPPER_ATTACHMENTS                                                          = TagKey.create(Registries.BLOCK, FabsBnB.location("tree_chopper_attachments"));
    public static final TagKey<Block> LEAF_BREAKER_WHITELIST                                                            = TagKey.create(Registries.BLOCK, FabsBnB.location("leaf_breaker_whitelist"));
    public static final TagKey<Block> SCYTHE_ABLE                                                                       = TagKey.create(Registries.BLOCK, FabsBnB.location("scythe-able"));

    // Enchantments
    public static final ResourceKey<Enchantment> ORE_MINER;
    public static final ResourceKey<Enchantment> TREE_CHOPPER;
    public static final ResourceKey<Enchantment> LEAF_BREAKER;
    public static final ResourceKey<Enchantment> CAPTURING;
    public static final ResourceKey<Enchantment> HARVESTING;
    public static final ResourceKey<Enchantment> TILLING;
    public static final ResourceKey<Enchantment> SCYTHE;

    public static final VeinMinerEnchant ORE_MINER_ENCHANT;
    public static final VeinMinerEnchant TREE_CHOPPER_ENCHANT;
    public static final VeinMinerEnchant LEAF_BREAKER_ENCHANT;
    public static final CapturingEnchant CAPTURING_ENCHANT;
    public static final HarvestingEnchant HARVESTING_ENCHANT;
    public static final TillingEnchant TILLING_ENCHANT;
    public static final ScytheEnchantment SCYTHE_ENCHANT;

    static {
        if (ModConfig.oreMinerEnabled.getValue()) {
            ORE_MINER = ResourceKey.create(Registries.ENCHANTMENT, FabsBnB.location("ore_miner"));
            ORE_MINER_ENCHANT = registerEnchantment(new VeinMinerEnchant(ORE_MINER, ORE_MINER_WHITELIST, DIGGING_TOOLS, ItemTags.PICKAXES, ModConfig.oreMinerMiningLimit.getValue(), ModConfig.oreMinerScanRange.getValue()));
        } else {
            ORE_MINER = null;
            ORE_MINER_ENCHANT = null;
        }

        if (ModConfig.treeChopperEnabled.getValue()) {
            TREE_CHOPPER = ResourceKey.create(Registries.ENCHANTMENT, FabsBnB.location("tree_chopper"));
            TREE_CHOPPER_ENCHANT = registerEnchantment(new VeinMinerEnchant(TREE_CHOPPER, TREE_CHOPPER_WHITELIST, ItemTags.AXES, ItemTags.AXES, ModConfig.treeChopperMiningLimit.getValue(), ModConfig.treeChopperScanRange.getValue(), null, true));
        } else {
            TREE_CHOPPER = null;
            TREE_CHOPPER_ENCHANT = null;
        }

        if (ModConfig.leafBreakerEnabled.getValue()) {
            LEAF_BREAKER = ResourceKey.create(Registries.ENCHANTMENT, FabsBnB.location("leaf_breaker"));
            LEAF_BREAKER_ENCHANT = registerEnchantment(new VeinMinerEnchant(LEAF_BREAKER, LEAF_BREAKER_WHITELIST, ItemTags.AXES, ItemTags.AXES, ModConfig.leafBreakerMiningLimit.getValue(), ModConfig.leafBreakerScanRange.getValue(), null, true));
        } else {
            LEAF_BREAKER = null;
            LEAF_BREAKER_ENCHANT = null;
        }

        if (ModConfig.capturingEnabled.getValue()) {
            CAPTURING = ResourceKey.create(Registries.ENCHANTMENT, FabsBnB.location("capturing"));
            CAPTURING_ENCHANT = registerEnchantment(new CapturingEnchant(CAPTURING, ItemTags.SHARP_WEAPON_ENCHANTABLE, ItemTags.SWORDS));
        } else {
            CAPTURING = null;
            CAPTURING_ENCHANT = null;
        }

        if (ModConfig.harvestingEnabled.getValue()) {
            HARVESTING = ResourceKey.create(Registries.ENCHANTMENT, FabsBnB.location("harvesting"));
            HARVESTING_ENCHANT = registerEnchantment(new HarvestingEnchant(HARVESTING, ItemTags.HOES, ItemTags.HOES));
        } else {
            HARVESTING = null;
            HARVESTING_ENCHANT = null;
        }

        if (ModConfig.harvestingEnabled.getValue()) {
            TILLING = ResourceKey.create(Registries.ENCHANTMENT, FabsBnB.location("tilling"));
            TILLING_ENCHANT = registerEnchantment(new TillingEnchant(TILLING, ItemTags.HOES, ItemTags.HOES));
        } else {
            TILLING = null;
            TILLING_ENCHANT = null;
        }

        if (ModConfig.scytheEnabled.getValue()) {
            SCYTHE = ResourceKey.create(Registries.ENCHANTMENT, FabsBnB.location("scythe"));
            SCYTHE_ENCHANT = registerEnchantment(new ScytheEnchantment(SCYTHE, ItemTags.HOES, ItemTags.HOES));
        } else {
            SCYTHE = null;
            SCYTHE_ENCHANT = null;
        }
    }

    private static <T extends IEnchantment> T registerEnchantment(T enchantment) {
        ENCHANTMENT_LIST.add(enchantment);
        return enchantment;
    }


    public static void init() {
        FabsBnB.log("Setting up registry...");
        if (Platform.isFabric()) {
            registerCompostables();
        }
    }

    public static void registerCompostables() {
        FabsBnB.log("Registering compostables...");
        addCompost(0.5f, Items.ROTTEN_FLESH);
        addCompost(0.3f, Items.BAMBOO);
        addCompost(1f, Items.POISONOUS_POTATO);
        addCompost(0.3f, Items.SPIDER_EYE);
        addCompost(0.3f, Items.CHORUS_FRUIT);
        addCompost(0.3f, Items.CHORUS_FLOWER);
        addCompost(0.4f, Items.ROTTEN_FLESH);
    }

    private static void addCompost(float chance, Item item) {
        FabsBnB.debug("Adding compostable: " + item.arch$registryName() + " with chance: " + chance);
        ComposterBlock.COMPOSTABLES.put(item, chance);
    }
}
