package com.fabbe50.fabsbnb.world.item.enchantments;

import com.fabbe50.fabsbnb.ModConfig;
import com.fabbe50.fabsbnb.util.Utilities;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public record CapturingEnchant(ResourceKey<Enchantment> enchantmentKey, TagKey<Item> supportedTools, TagKey<Item> primaryTools) implements IEnchantment {
    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public Enchantment.Cost getMinCost() {
        return Enchantment.dynamicCost(15, 9);
    }

    @Override
    public Enchantment.Cost getMaxCost() {
        return Enchantment.dynamicCost(65, 9);
    }

    @Override
    public int getAnvilCost() {
        return 4;
    }

    @Override
    public EquipmentSlotGroup getEquipmentSlotGroup() {
        return EquipmentSlotGroup.HAND;
    }

    @Override
    public ResourceKey<Enchantment> getResourceKey() {
        return enchantmentKey;
    }

    @Override
    public TagKey<Item> getSupportedItems() {
        return supportedTools;
    }

    @Override
    public TagKey<Item> getPrimaryItems() {
        return primaryTools;
    }

    @Override
    public boolean handleEvent(Object... objects) {
        return handleEvent((LivingEntity) objects[0], (ItemStack) objects[1]);
    }

    private boolean handleEvent(LivingEntity attackedEntity, ItemStack weapon) {
        Level level = attackedEntity.level();
        Holder<Enchantment> enchantmentHolder = Utilities.getHolder(level, enchantmentKey);
        if (weapon.isEmpty()) {
            return false;
        }
        int enchantmentLevel = weapon.getEnchantments().getLevel(enchantmentHolder);
        if (!(enchantmentLevel > 0)) {
            return false;
        }
        SpawnEggItem spawnEggItem = SpawnEggItem.byId(attackedEntity.getType());
        if (spawnEggItem != null) {
            ItemStack spawnEggStack = new ItemStack(spawnEggItem);
            if (attackedEntity.getRandom().nextInt(ModConfig.oneInNChanceToDropSpawnEgg.getValue() / enchantmentLevel) == 0) {
                attackedEntity.spawnAtLocation((ServerLevel) level, spawnEggStack);
                return true;
            }
        }
        return false;
    }
}
