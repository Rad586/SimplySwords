package net.sweenus.simplyswords.item.custom;


import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.sweenus.simplyswords.config.SimplySwordsConfig;

import java.util.List;

public class SpeedSwordItem extends SwordItem {
    public SpeedSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        int shitchance = SimplySwordsConfig.getIntValue("speed_chance");
        int sduration = SimplySwordsConfig.getIntValue("speed_duration");

        if (attacker.getRandom().nextInt(100) <= shitchance) {
            attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, sduration, 1), attacker);
        }

        return super.postHit(stack, target, attacker);

    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {

        tooltip.add(Text.literal(""));
        tooltip.add(Text.translatable("item.simplyswords.speedsworditem.tooltip1").formatted(Formatting.AQUA, Formatting.BOLD));
        tooltip.add(Text.translatable("item.simplyswords.speedsworditem.tooltip2"));
        tooltip.add(Text.translatable("item.simplyswords.speedsworditem.tooltip3"));
    }

}
