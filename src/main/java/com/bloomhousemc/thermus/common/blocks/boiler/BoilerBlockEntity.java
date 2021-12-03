package com.bloomhousemc.thermus.common.blocks.boiler;

import com.bloomhousemc.thermus.Thermus;
import com.bloomhousemc.thermus.common.registry.ThermusObjects;
import com.bloomhousemc.thermus.common.registry.ThermusProperties;
import com.bloomhousemc.thermus.common.registry.ThermusTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;


public class BoilerBlockEntity extends BlockEntity implements IAnimatable, ImplementedInventory, SidedInventory {
    protected final DefaultedList<ItemStack> items = DefaultedList.ofSize(2, ItemStack.EMPTY);
    private final AnimationFactory factory = new AnimationFactory(this);
    private boolean loaded = false;
    public int active = 0;
    private int timer = 0;
    private int fuelTime = Thermus.config.boilerFuelBurnTimeMinutes *60*20;

    public BoilerBlockEntity(BlockPos pos, BlockState state) {
        super(ThermusObjects.BOILER_BLOCK_ENTITY, pos, state);
    }

    public static void decrementBoiler(World world, BoilerBlockEntity blockEntity){
        if((blockEntity.getStack(0).getCount() > 0 && blockEntity.getStack(0).isOf(Items.COAL)) && blockEntity.getStack(0).getCount() > 0 && blockEntity.getStack(1).isOf(Items.CHARCOAL)){
            int rand = MathHelper.nextInt(world.random, 0,1);
            blockEntity.getStack(rand).decrement(1);
        }else if (blockEntity.getStack(0).getCount() > 0 && blockEntity.getStack(0).isOf(Items.COAL)){
            blockEntity.getStack(0).decrement(1);
        }else if (blockEntity.getStack(1).getCount() > 0 && blockEntity.getStack(1).isOf(Items.CHARCOAL)){
            blockEntity.getStack(1).decrement(1);
        }
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = super.toInitialChunkDataNbt();
        writeNbt(nbt);
        return nbt;
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    public void sync() {
        if (world != null && !world.isClient) {
            world.updateListeners(pos, getCachedState(), getCachedState(), Block.NOTIFY_LISTENERS);
        }
    }

    public static void tick(World world, BlockPos pos, BlockState state, BoilerBlockEntity blockEntity) {
        if (world != null) {
            if (!blockEntity.loaded) {
                if (!world.isClient && state.get(ThermusProperties.LIT)) {
                    blockEntity.markDirty();
                    blockEntity.sync();
                }
                blockEntity.loaded = true;
            }
            if (state.get(ThermusProperties.LIT)) {
                blockEntity.timer++;
                if(world.isClient){
                    if (world.random.nextFloat() <= 0.075f) {
                        world.playSound(null, pos, SoundEvents.BLOCK_FURNACE_FIRE_CRACKLE, SoundCategory.BLOCKS, 1 / 3f, 1);
                    }
                }else if(blockEntity.timer < 0){
                    blockEntity.active++;
                    if(blockEntity.active > blockEntity.fuelTime){
                        blockEntity.active=0;
                        decrementBoiler(world, blockEntity);
                        markDirty(world,pos,state);
                        blockEntity.sync();
                    }
                }else{
                    world.setBlockState(pos, state.with(ThermusProperties.LIT, false), 3);
                    blockEntity.markDirty();
                    blockEntity.sync();
                    blockEntity.reset(true);
                    updateCoal(state,world,pos);
                }
            }
        }
    }

    private void reset(boolean clear) {
        if (world != null) {
            if (clear) {
                items.clear();
            }
        }
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.timer = nbt.getInt("Timer");
        this.active = nbt.getInt("Active");
        Inventories.readNbt(nbt, items);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("Timer", this.timer);
        nbt.putInt("Active", this.active);
        Inventories.writeNbt(nbt, items);
        super.writeNbt(nbt);
    }

    private <E extends BlockEntity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.boiler.idle", true));
        return PlayState.CONTINUE;
    }

    private <E extends BlockEntity & IAnimatable> PlayState coal(AnimationEvent<E> event) {
        if(event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).getBlock() instanceof BoilerBlock){
            int coal = event.getAnimatable().getWorld().getBlockState(event.getAnimatable().getPos()).get(ThermusProperties.COAL);
            String animation = switch (coal) {
                case 1 -> "coal1";
                case 2 -> "coal2";
                case 3 -> "coal3";
                case 4 -> "coal4";
                default -> "nocoal";
            };
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.boiler."+animation, true));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
        animationData.addAnimationController(new AnimationController<>(this, "coal", 0, this::coal));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return items;
    }

    @Override
    public int[] getAvailableSlots(Direction side) {
        int[] result = new int[getItems().size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }

    @Override
    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return dir == Direction.UP;
    }

    @Override
    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return false;
    }

    public static void updateCoal(BlockState state, World world, BlockPos pos){
        if(world.getBlockEntity(pos) instanceof BoilerBlockEntity boilerBlockEntity){
            int count = boilerBlockEntity.getStack(0).getCount() + boilerBlockEntity.getStack(1).getCount();
            world.setBlockState(pos, state.with(ThermusProperties.COAL, count == 0 ? 0 : count < 16 ? 1 : count < 32 ? 2 : count < 48 ? 3 : 4));
        }
    }

    public void onUse(World world, BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {

            ItemStack itemStack = player.getStackInHand(hand);
            if(state.get(ThermusProperties.COIL) == 0 && ThermusTags.COIL.contains(itemStack.getItem())){
                world.setBlockState(pos, getCachedState().with(ThermusProperties.COIL, itemStack.isOf(ThermusObjects.COPPER_COIL) ? 1 : itemStack.isOf(ThermusObjects.GOLD_COIL) ? 2 : itemStack.isOf(ThermusObjects.IRON_COIL) ? 3 : 4));
            }
            if(world.getBlockEntity(pos) instanceof BoilerBlockEntity boilerBlockEntity && itemStack.isOf(Items.FLINT_AND_STEEL) && (boilerBlockEntity.getStack(0).getCount()+boilerBlockEntity.getStack(1).getCount()) > 0){
                world.setBlockState(pos, getCachedState().with(ThermusProperties.LIT, true));
            }
            if(world.getBlockEntity(pos) instanceof BoilerBlockEntity boilerBlockEntity && (itemStack.isOf(Items.COAL) || itemStack.isOf(Items.CHARCOAL))){
                if((boilerBlockEntity.getStack(0).getCount() + boilerBlockEntity.getStack(1).getCount())<64){
                    if(boilerBlockEntity.getStack(itemStack.isOf(Items.COAL) ? 0 : 1).isOf(Items.AIR)){
                        boilerBlockEntity.setStack(itemStack.isOf(Items.COAL) ? 0 : 1, itemStack.isOf(Items.COAL) ? new ItemStack(Items.COAL) : new ItemStack(Items.CHARCOAL));
                        player.getStackInHand(hand).decrement(1);
                        this.timer = this.timer - fuelTime;
                        sync();
                        updateCoal(getCachedState(),world,pos);
                        markDirty();
                        return;
                    }
                    if(player.isSneaking()){
                        while((boilerBlockEntity.getStack(0).getCount() + boilerBlockEntity.getStack(1).getCount())<64 && player.getStackInHand(hand).getCount()>0){
                            if(boilerBlockEntity.getStack(itemStack.isOf(Items.COAL) ? 0 : 1).isOf(Items.AIR)){
                                boilerBlockEntity.setStack(itemStack.isOf(Items.COAL) ? 0 : 1, itemStack.isOf(Items.COAL) ? new ItemStack(Items.COAL) : new ItemStack(Items.CHARCOAL));
                                player.getStackInHand(hand).decrement(1);
                                this.timer = this.timer - fuelTime;
                                sync();
                            }
                            boilerBlockEntity.getStack(itemStack.isOf(Items.COAL) ? 0 : 1).increment(1);
                            player.getStackInHand(hand).decrement(1);
                            this.timer = this.timer - fuelTime;
                            sync();
                        }
                    }else{
                        boilerBlockEntity.getStack(itemStack.isOf(Items.COAL) ? 0 : 1).increment(1);
                        player.getStackInHand(hand).decrement(1);
                        this.timer = this.timer - fuelTime;
                        sync();
                    }
                }
                updateCoal(getCachedState(),world,pos);
            }
        markDirty();
    }
}
