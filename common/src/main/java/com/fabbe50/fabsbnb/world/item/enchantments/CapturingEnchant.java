package com.fabbe50.fabsbnb.world.item.enchantments;

import com.fabbe50.fabsbnb.ModConfig;
import com.fabbe50.fabsbnb.util.Utilities;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class CapturingEnchant implements IEnchantment {
    private final ResourceKey<Enchantment> enchantmentKey;
    private final TagKey<Item> supportedTools;
    private final TagKey<Item> primaryTools;

    public CapturingEnchant(ResourceKey<Enchantment> enchantmentKey, TagKey<Item> supportedTools, TagKey<Item> primaryTools) {
        this.enchantmentKey = enchantmentKey;
        this.supportedTools = supportedTools;
        this.primaryTools = primaryTools;
    }

    @Override
    public Enchantment.EnchantmentDefinition getEnchantmentDefinition(HolderGetter<Item> itemHolder) {
        return Enchantment.definition(itemHolder.getOrThrow(supportedTools), itemHolder.getOrThrow(primaryTools), 1, 3, Enchantment.dynamicCost(15, 9), Enchantment.dynamicCost(65, 9), 4, EquipmentSlotGroup.HAND);
    }

    public void handleEvent(LivingEntity attackedEntity, ItemStack weapon) {
        Level level = attackedEntity.level();
        Holder<Enchantment> enchantmentHolder = Utilities.getHolder(level, enchantmentKey);
        if (weapon.isEmpty()) {
            return;
        }
        int enchantmentLevel = weapon.getEnchantments().getLevel(enchantmentHolder);
        if (!(enchantmentLevel > 0)) {
            return;
        }
        SpawnEggItem spawnEggItem = SpawnEggItem.byId(attackedEntity.getType());
        if (spawnEggItem != null) {
            ItemStack spawnEggStack = new ItemStack(spawnEggItem);
            if (attackedEntity.getRandom().nextInt(ModConfig.oneInNChanceToDropSpawnEgg.getValue() / enchantmentLevel) == 0) {
                attackedEntity.spawnAtLocation(spawnEggStack);
            }
        }
    }
}
