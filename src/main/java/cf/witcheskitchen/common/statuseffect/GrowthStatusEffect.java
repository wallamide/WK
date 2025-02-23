package cf.witcheskitchen.common.statuseffect;

import cf.witcheskitchen.common.registry.WKStatusEffects;
import net.minecraft.block.BlockState;
import net.minecraft.block.Fertilizable;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class GrowthStatusEffect extends StatusEffect {
    public GrowthStatusEffect(StatusEffectCategory type, int color) {
        super(type, color);
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity.hasStatusEffect(WKStatusEffects.COOLDOWN)) {
            return;
        }
        int radius = amplifier + 1;
        World world = entity.world;
        BlockPos initialPosition = entity.getBlockPos();
        for (BlockPos position : BlockPos.iterate(initialPosition.add(-radius, -radius, -radius), initialPosition.add(radius, radius, radius))) {
            BlockState blockState = entity.world.getBlockState(position);
            {
                if (blockState.getBlock() instanceof Fertilizable fertilizable) {
                    if (fertilizable.isFertilizable(world, position, entity.world.getBlockState(position), false)) {
                        if (fertilizable.canGrow(world, world.random, position, entity.world.getBlockState(position))) {
                            BoneMealItem.useOnFertilizable(new ItemStack(Items.BONE_MEAL), entity.world, position);
                            BoneMealItem.useOnGround(new ItemStack(Items.BONE_MEAL), entity.world, position, null);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entity.addStatusEffect(new StatusEffectInstance(WKStatusEffects.COOLDOWN, 6000, 0));
    }
}
