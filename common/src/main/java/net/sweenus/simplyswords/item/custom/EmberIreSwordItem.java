package net.sweenus.simplyswords.item.custom;


import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.sweenus.simplyswords.config.SimplySwordsConfig;
import net.sweenus.simplyswords.registry.SoundRegistry;
import net.sweenus.simplyswords.util.HelperMethods;

import java.util.List;

public class EmberIreSwordItem extends SwordItem {
    public EmberIreSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    private static int stepMod = 0;
    private static DefaultParticleType particleWalk = ParticleTypes.FALLING_LAVA;
    private static DefaultParticleType particleSprint = ParticleTypes.FALLING_LAVA;
    private static DefaultParticleType particlePassive = ParticleTypes.SMOKE;

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.world.isClient()) {
            ServerWorld world = (ServerWorld) attacker.world;
            int fhitchance = (int) SimplySwordsConfig.getFloatValue("ember_ire_chance");
            int fduration = (int) SimplySwordsConfig.getFloatValue("ember_ire_duration");
            HelperMethods.playHitSounds(attacker, target);


            if (attacker.getRandom().nextInt(100) <= fhitchance) {
                attacker.setOnFireFor(fduration / 20);
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, fduration, 0), attacker);
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, fduration, 1), attacker);
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, fduration, 0), attacker);
                world.playSoundFromEntity(null, attacker, SoundRegistry.MAGIC_SWORD_SPELL_01.get(), SoundCategory.PLAYERS, 0.5f, 2f);
                particlePassive = ParticleTypes.LAVA;
                particleWalk = ParticleTypes.CAMPFIRE_COSY_SMOKE;
                particleSprint = ParticleTypes.CAMPFIRE_COSY_SMOKE;
            }
        }

        return super.postHit(stack, target, attacker);

    }

    @Override
    public Text getName(ItemStack stack) {
        return Text.translatable(this.getTranslationKey(stack)).formatted(Formatting.GOLD, Formatting.BOLD, Formatting.UNDERLINE);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        if (!user.world.isClient()) {
            if (user.hasStatusEffect(StatusEffects.STRENGTH) && user.hasStatusEffect(StatusEffects.HASTE) && user.hasStatusEffect(StatusEffects.SPEED)) {

                ServerWorld sWorld = (ServerWorld)user.world;
                BlockPos position = (user.getBlockPos());
                Vec3d rotation = user.getRotationVec(1f);
                Vec3d newPos = user.getPos().add(rotation);

                FireballEntity fireball = new FireballEntity(EntityType.FIREBALL, world);
                fireball.updatePosition(newPos.getX(), (user.getY()) +1.5, newPos.getZ());
                fireball.setOwner(user);
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 4), user);
                user.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 60, 2), user);
                sWorld.spawnEntity(fireball);
                fireball.setVelocity(rotation);
                user.removeStatusEffect(StatusEffects.STRENGTH);
                user.removeStatusEffect(StatusEffects.SPEED);
                user.removeStatusEffect(StatusEffects.HASTE);
                world.playSound(null, position, SoundRegistry.ELEMENTAL_BOW_FIRE_SHOOT_IMPACT_03.get(), SoundCategory.PLAYERS, 0.3f, 2f);
                user.extinguish();


            }
        }
        return super.use(world,user,hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {

        if ((entity instanceof PlayerEntity player)) {
            if (!player.hasStatusEffect(StatusEffects.STRENGTH) && !player.isOnFire()) {
                particlePassive = ParticleTypes.SMOKE;
                particleWalk = ParticleTypes.FALLING_LAVA;
                particleSprint = ParticleTypes.FALLING_LAVA;
            }
        }


        if (stepMod > 0)
            stepMod--;
        if (stepMod <= 0)
            stepMod = 7;
        HelperMethods.createFootfalls(entity, stack, world, stepMod, particleWalk, particleSprint, particlePassive, true);

        super.inventoryTick(stack, world, entity, slot, selected);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {

        //1.19.x

        tooltip.add(Text.literal(""));
        tooltip.add(Text.translatable("item.simplyswords.emberiresworditem.tooltip1").formatted(Formatting.GOLD, Formatting.BOLD));
        tooltip.add(Text.translatable("item.simplyswords.emberiresworditem.tooltip2"));
        tooltip.add(Text.translatable("item.simplyswords.emberiresworditem.tooltip3"));
        tooltip.add(Text.literal(""));
        tooltip.add(Text.translatable("item.simplyswords.onrightclick").formatted(Formatting.BOLD, Formatting.GREEN));
        tooltip.add(Text.translatable("item.simplyswords.emberiresworditem.tooltip4"));
        tooltip.add(Text.translatable("item.simplyswords.emberiresworditem.tooltip5"));
        /*

        //1.18.2
        tooltip.add(new LiteralText(""));
        tooltip.add(new TranslatableText("item.simplyswords.emberiresworditem.tooltip1").formatted(Formatting.GOLD, Formatting.BOLD));
        tooltip.add(new TranslatableText("item.simplyswords.emberiresworditem.tooltip2"));
        tooltip.add(new TranslatableText("item.simplyswords.emberiresworditem.tooltip3"));
        tooltip.add(new LiteralText(""));
        tooltip.add(new TranslatableText("item.simplyswords.onrightclick").formatted(Formatting.BOLD, Formatting.GREEN));
        tooltip.add(new TranslatableText("item.simplyswords.emberiresworditem.tooltip4"));
        tooltip.add(new TranslatableText("item.simplyswords.emberiresworditem.tooltip5"));

         */
    }

}
