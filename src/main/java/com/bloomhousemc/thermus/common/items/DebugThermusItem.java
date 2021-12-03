package com.bloomhousemc.thermus.common.items;

import com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlock;
import com.bloomhousemc.thermus.common.blocks.boiler.BoilerBlockEntity;
import com.bloomhousemc.thermus.common.components.ITemperature;
import com.bloomhousemc.thermus.common.registry.ThermusPorperties;
import net.minecraft.block.BlockState;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class DebugThermusItem extends Item {
    public DebugThermusItem(Settings settings) {
        super(settings);
    }


    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(context.getWorld().isClient || context.getPlayer().isSneaking())return ActionResult.PASS;
        BlockPos blockPos = context.getBlockPos();
        BlockState blockState = context.getWorld().getBlockState(blockPos);
        if(blockState.getBlock() instanceof BoilerBlock boilerBlock){
            //context.getWorld().setBlockState(blockPos, blockState.with(LIT, !blockState.get(LIT)));
            BoilerBlockEntity boilerBlockEntity = (BoilerBlockEntity) context.getWorld().getBlockEntity(blockPos);
            System.out.println(boilerBlockEntity.getItems());
        }
        try {
            int i = ITemperature.get(blockState).getTemperature();
            System.out.println("Temperature: "+i);
        }catch(Exception e){
            System.out.println("Did not find temperature in target");
        }
        try {
            int i = ITemperature.get(blockState).getTemperatureModifier();
            System.out.println("TemperatureModifier: "+i);
        }catch(Exception e){
            System.out.println("Did not find modifier in target");
        }
        try {
            int i = ITemperature.get(blockState).getTargetTemperature();
            System.out.println("Target Temperature: "+i);
        }catch(Exception e){
            System.out.println("Did not find targetTemperature in target");
        }

        return ActionResult.PASS;

    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            if (user.isSneaking()) {
                try {
                    int i = ITemperature.get(user).getTemperature();
                    System.out.println("Temperature: " + i);
                } catch (Exception e) {
                    System.out.println("Did not find temperature in you");
                }
                try {
                    int i = ITemperature.get(user).getTemperatureModifier();
                    System.out.println("Temperature Modifier: " + i);
                } catch (Exception e) {
                    System.out.println("Did not find temperatureModifier in you");
                }
                try {
                    int i = ITemperature.get(user).getTargetTemperature();
                    System.out.println("Target Temperature: " + i);
                } catch (Exception e) {
                    System.out.println("Did not find targetTemperature in you");
                }

            }

        }
        return TypedActionResult.pass(user.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(new TranslatableText("Shift-click to use on self"));
    }
}
