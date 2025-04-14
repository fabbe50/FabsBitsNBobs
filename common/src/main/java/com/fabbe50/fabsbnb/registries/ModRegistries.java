package com.fabbe50.fabsbnb.registries;

import com.fabbe50.fabsbnb.FabsBnB;
import com.fabbe50.fabsbnb.Utilities;
import com.fabbe50.fabsbnb.world.block.*;
import com.fabbe50.fabsbnb.world.block.entity.BlockBreakerBlockEntity;
import com.fabbe50.fabsbnb.world.block.entity.BlockDetectorBlockEntity;
import com.fabbe50.fabsbnb.world.block.entity.BlockPlacerBlockEntity;
import com.fabbe50.fabsbnb.world.block.entity.XPHolderBlockEntity;
import com.fabbe50.fabsbnb.world.effects.MobEffectExt;
import com.fabbe50.fabsbnb.world.inventory.BlockBreakerMenu;
import com.fabbe50.fabsbnb.world.item.BlockYoinkerItem;
import com.fabbe50.fabsbnb.world.item.BuildingWandItem;
import com.fabbe50.fabsbnb.world.item.WhooshWandItem;
import com.fabbe50.fabsbnb.world.item.WrenchItem;
import com.fabbe50.fabsbnb.world.item.base.ModBlockItem;
import com.fabbe50.fabsbnb.world.item.base.ModItem;
import com.fabbe50.fabsbnb.world.item.enchantments.VeinMinerEnchant;
import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

import java.util.ArrayList;
import java.util.List;

public class ModRegistries {
    // Constants
    public static final int SHORT_DURATION_POTION = 3600;
    public static final int LONG_DURATION_POTION = 9600;
    public static final int STRONG_POTION = 450;

    // Registry Lists
    public static final List<RegistrySupplier<Item>>  ITEM_LIST = new ArrayList<>();
    public static final List<RegistrySupplier<Block>> BLOCK_LIST = new ArrayList<>();

    // Registrars
    private static final Registrar<CreativeModeTab>             TABS                                                    = FabsBnB.MANAGER.get().get(Registries.CREATIVE_MODE_TAB);
    public  static final Registrar<Block>                       BLOCKS                                                  = FabsBnB.MANAGER.get().get(Registries.BLOCK);
    private static final Registrar<BlockEntityType<?>>          BLOCK_ENTITIES                                          = FabsBnB.MANAGER.get().get(Registries.BLOCK_ENTITY_TYPE);
    public  static final Registrar<Item>                        ITEMS                                                   = FabsBnB.MANAGER.get().get(Registries.ITEM);
    private static final Registrar<Enchantment>                 ENCHANTMENTS                                            = FabsBnB.MANAGER.get().get(Registries.ENCHANTMENT);
    private static final Registrar<MobEffect>                   MOB_EFFECTS                                             = FabsBnB.MANAGER.get().get(Registries.MOB_EFFECT);
    private static final Registrar<Potion>                      POTIONS                                                 = FabsBnB.MANAGER.get().get(Registries.POTION);
    private static final Registrar<MenuType<?>>                 MENU_TYPES                                              = FabsBnB.MANAGER.get().get(Registries.MENU);

    // Blocks
    public static final RegistrySupplier<Block> LAVA_SPONGE                                                             = registerBlock(FabsBnB.location("lava_sponge"), () -> new CustomSpongeBlock(FluidTags.LAVA, BlockBehaviour.Properties.of()));
    public static final RegistrySupplier<Block> LAVA_SPONGE_USED                                                        = registerBlock(FabsBnB.location("lava_sponge_used"), () -> new LavaUsedSpongeBlock(BlockBehaviour.Properties.of()));
    public static final RegistrySupplier<Block> PUSHER_BLOCK                                                            = registerBlock(FabsBnB.location("pusher_block"), () -> new PusherBlock(BlockBehaviour.Properties.of()));
    public static final RegistrySupplier<Block> THIN_LIGHT                                                              = registerBlock(FabsBnB.location("thin_light"), () -> new ThinLightBlock(BlockBehaviour.Properties.of().lightLevel(Utilities.litBlockEmission(15))));
    public static final RegistrySupplier<Block> POWERED_THIN_LIGHT                                                      = registerBlock(FabsBnB.location("powered_thin_light"), () -> new PoweredThinLightBlock(BlockBehaviour.Properties.of().lightLevel(value -> 15)));
    public static final RegistrySupplier<Block> BLOCK_PLACER                                                            = registerBlock(FabsBnB.location("block_placer"), () -> new BlockPlacerBlock(BlockBehaviour.Properties.of()));
    public static final RegistrySupplier<Block> BLOCK_BREAKER                                                           = registerBlock(FabsBnB.location("block_breaker"), () -> new BlockBreakerBlock(BlockBehaviour.Properties.of()));
    public static final RegistrySupplier<Block> BLOCK_DETECTOR                                                          = registerBlock(FabsBnB.location("block_detector"), () -> new BlockDetectorBlock(BlockBehaviour.Properties.of()));
    public static final RegistrySupplier<Block> XP_HOLDER                                                               = registerBlock(FabsBnB.location("xp_holder"), () -> new XPHolderBlock(BlockBehaviour.Properties.of()));

    private static RegistrySupplier<Block> registerBlock(ResourceLocation location, Supplier<Block> blockSupplier) {
        RegistrySupplier<Block> block = BLOCKS.register(location, blockSupplier);
        BLOCK_LIST.add(block);
        return block;
    }

    // Block Entities
    public static final RegistrySupplier<BlockEntityType<BlockPlacerBlockEntity>> BLOCK_PLACER_BLOCK_ENTITY             = BLOCK_ENTITIES.register(FabsBnB.location("block_placer_block_entity"), Suppliers.memoize(() -> BlockEntityType.Builder.of(BlockPlacerBlockEntity::new, BLOCK_PLACER.get()).build(null)));
    public static final RegistrySupplier<BlockEntityType<BlockBreakerBlockEntity>> BLOCK_BREAKER_BLOCK_ENTITY           = BLOCK_ENTITIES.register(FabsBnB.location("block_breaker_block_entity"), Suppliers.memoize(() -> BlockEntityType.Builder.of(BlockBreakerBlockEntity::new, BLOCK_BREAKER.get()).build(null)));
    public static final RegistrySupplier<BlockEntityType<BlockDetectorBlockEntity>> BLOCK_DETECTOR_BLOCK_ENTITY         = BLOCK_ENTITIES.register(FabsBnB.location("block_detector_block_entity"), Suppliers.memoize(() -> BlockEntityType.Builder.of(BlockDetectorBlockEntity::new, BLOCK_DETECTOR.get()).build(null)));
    public static final RegistrySupplier<BlockEntityType<XPHolderBlockEntity>> XP_HOLDER_BLOCK_ENTITY                   = BLOCK_ENTITIES.register(FabsBnB.location("xp_holder_block_entity"), Suppliers.memoize(() -> BlockEntityType.Builder.of(XPHolderBlockEntity::new, XP_HOLDER.get()).build(null)));

    // Items
    public static final RegistrySupplier<Item> WOODEN_BUILDING_WAND                                                     = registerItem(FabsBnB.location("wooden_building_wand"), () -> new BuildingWandItem(Tiers.WOOD, new Item.Properties()));
    public static final RegistrySupplier<Item> STONE_BUILDING_WAND                                                      = registerItem(FabsBnB.location("stone_building_wand"), () -> new BuildingWandItem(Tiers.STONE, new Item.Properties()));
    public static final RegistrySupplier<Item> IRON_BUILDING_WAND                                                       = registerItem(FabsBnB.location("iron_building_wand"), () -> new BuildingWandItem(Tiers.IRON, new Item.Properties()));
    public static final RegistrySupplier<Item> GOLD_BUILDING_WAND                                                       = registerItem(FabsBnB.location("gold_building_wand"), () -> new BuildingWandItem(Tiers.GOLD, new Item.Properties()));
    public static final RegistrySupplier<Item> DIAMOND_BUILDING_WAND                                                    = registerItem(FabsBnB.location("diamond_building_wand"), () -> new BuildingWandItem(Tiers.DIAMOND, new Item.Properties()));
    public static final RegistrySupplier<Item> NETHERITE_BUILDING_WAND                                                  = registerItem(FabsBnB.location("netherite_building_wand"), () -> new BuildingWandItem(Tiers.NETHERITE, new Item.Properties()));
    public static final RegistrySupplier<Item> ITEM_BLOCK_YOINKER                                                       = registerItem(FabsBnB.location("block_yoinker"), () -> new BlockYoinkerItem(new Item.Properties()));
    public static final RegistrySupplier<Item> WHOOSH_WAND                                                              = registerItem(FabsBnB.location("whoosh_wand"), () -> new WhooshWandItem(new Item.Properties()));
    public static final RegistrySupplier<Item> CAT_CLAW                                                                 = registerItem(FabsBnB.location("cat_claw"), () -> new ModItem(new Item.Properties()));
    public static final RegistrySupplier<Item> WRENCH                                                                   = registerItem(FabsBnB.location("wrench"), () -> new WrenchItem(new Item.Properties()));
    public static final RegistrySupplier<Item> ITEM_LAVA_SPONGE                                                         = registerItem(FabsBnB.location("lava_sponge"), () -> new ModBlockItem(LAVA_SPONGE.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> ITEM_LAVA_SPONGE_USED                                                    = registerItem(FabsBnB.location("lava_sponge_used"), () -> new ModBlockItem(LAVA_SPONGE_USED.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> ITEM_PUSHER_BLOCK                                                        = registerItem(FabsBnB.location("pusher_block"), () -> new ModBlockItem(PUSHER_BLOCK.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> ITEM_THIN_LIGHT                                                          = registerItem(FabsBnB.location("thin_light"), () -> new ModBlockItem(THIN_LIGHT.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> ITEM_POWERED_THIN_LIGHT                                                  = registerItem(FabsBnB.location("powered_thin_light"), () -> new ModBlockItem(POWERED_THIN_LIGHT.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> ITEM_BLOCK_PLACER                                                        = registerItem(FabsBnB.location("block_placer"), () -> new ModBlockItem(BLOCK_PLACER.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> ITEM_BLOCK_BREAKER                                                       = registerItem(FabsBnB.location("block_breaker"), () -> new ModBlockItem(BLOCK_BREAKER.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> ITEM_BLOCK_DETECTOR                                                      = registerItem(FabsBnB.location("block_detector"), () -> new ModBlockItem(BLOCK_DETECTOR.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> ITEM_XP_HOLDER                                                           = registerItem(FabsBnB.location("xp_holder"), () -> new ModBlockItem(XP_HOLDER.get(), new Item.Properties()));
    public static final RegistrySupplier<Item> FULL_WATER_CAULDRON                                                      = registerItem(FabsBnB.location("water_cauldron"), () -> new BlockItem(Blocks.WATER_CAULDRON, new Item.Properties()), false);

    private static RegistrySupplier<Item> registerItem(ResourceLocation location, Supplier<Item> itemSupplier) {
        return registerItem(location, itemSupplier, true);
    }

    private static RegistrySupplier<Item> registerItem(ResourceLocation location, Supplier<Item> itemSupplier, boolean addToTab) {
        RegistrySupplier<Item> item = ITEMS.register(location, itemSupplier);
        if (addToTab) {
            ITEM_LIST.add(item);
        }
        return item;
    }

    // Enchantments
    public static final RegistrySupplier<Enchantment> VEIN_MINER                                                        = ENCHANTMENTS.register(FabsBnB.location("vein_miner"), () -> new VeinMinerEnchant(Enchantment.Rarity.VERY_RARE, EquipmentSlot.MAINHAND));

    // Mob Effects
    public static final RegistrySupplier<MobEffect> FELINE_AURA                                                         = MOB_EFFECTS.register(FabsBnB.location("feline_aura"), () -> new MobEffectExt(MobEffectCategory.BENEFICIAL, 0x939918));

    // Potions
    public static final RegistrySupplier<Potion> FELINE_AURA_POTION_SHORT                                               = registerPotion("feline_aura_short", FELINE_AURA, SHORT_DURATION_POTION);
    public static final RegistrySupplier<Potion> FELINE_AURA_POTION_LONG                                                = registerPotion("feline_aura_long", FELINE_AURA, LONG_DURATION_POTION);

    private static RegistrySupplier<Potion> registerPotion(String name, RegistrySupplier<MobEffect> effect, int duration) {
        return POTIONS.register(FabsBnB.location(name), () -> new Potion(new MobEffectInstance(effect.get(), duration)));
    }

    // Creative Tabs
    public static final RegistrySupplier<CreativeModeTab> TAB                                                           = TABS.register(FabsBnB.location("tab"), () -> CreativeTabRegistry.create(Component.translatable("category.fabsbnb.tab"), () -> new ItemStack(DIAMOND_BUILDING_WAND.get())));

    // Menu Types
    public static final RegistrySupplier<MenuType<BlockBreakerMenu>> BLOCK_BREAKER_MENU                                 = MENU_TYPES.register(FabsBnB.location("block_breaker_menu"), () -> MenuRegistry.of(BlockBreakerMenu::new));

    // Tags
    public static final TagKey<Item> BUILDING_WANDS                                                                     = TagKey.create(Registries.ITEM, FabsBnB.location("building_wands"));
    public static final TagKey<Block> BLOCK_YOINKER_BLACKLIST                                                           = TagKey.create(Registries.BLOCK, FabsBnB.location("block_yoinker_blacklist"));
    public static final TagKey<Block> SPIDER_NOT_CLIMBABLE                                                              = TagKey.create(Registries.BLOCK, FabsBnB.location("spider_not_climbable"));

    public static void init() {}
}
