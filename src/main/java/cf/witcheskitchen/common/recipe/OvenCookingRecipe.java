package cf.witcheskitchen.common.recipe;

import cf.witcheskitchen.common.registry.WKRecipeTypes;
import cf.witcheskitchen.common.util.RecipeUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

public class OvenCookingRecipe implements Recipe<Inventory> {

    private final Identifier id;
    private final Ingredient input;
    private final DefaultedList<ItemStack> outputs;
    private final float xp;

    public OvenCookingRecipe(Identifier id, Ingredient input, DefaultedList<ItemStack> outputs, float xp) {
        this.id = id;
        this.input = input;
        this.outputs = outputs;
        this.xp = xp;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return false;
    }

    @Override
    public ItemStack craft(Inventory inventory) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean fits(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    public Ingredient getInput() {
        return input;
    }

    public DefaultedList<ItemStack> getOutputs() {
        return outputs;
    }

    public float getXp() {
        return xp;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return WKRecipeTypes.WITCHES_OVEN_COOKING_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return WKRecipeTypes.WITCHES_OVEN_COOKING_RECIPE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<OvenCookingRecipe> {

        @Override
        public OvenCookingRecipe read(Identifier id, JsonObject json) {
            final Ingredient input = Ingredient.fromJson(JsonHelper.getObject(json, "ingredient"));
            var array = JsonHelper.getArray(json, "results");
            final DefaultedList<ItemStack> outputs = RecipeUtil.deserializeStacks(array);
            if (outputs.isEmpty()) {
                throw new JsonParseException("No output for Witches' Oven recipe");
            } else if (outputs.size() > 2) {
                throw new JsonParseException("Too many outputs for Witches' Oven recipe");
            }
            final float xp = JsonHelper.getFloat(json, "experience");
            return new OvenCookingRecipe(id, input, outputs, xp);
        }

        @Override
        public OvenCookingRecipe read(Identifier id, PacketByteBuf buf) {
            final Ingredient input = Ingredient.fromPacket(buf);
            final DefaultedList<ItemStack> outputs = DefaultedList.ofSize(buf.readVarInt(), ItemStack.EMPTY);
            for (int i = 0; i < outputs.size(); i++) {
                outputs.set(i, buf.readItemStack());
            }
            final float xp = buf.readFloat();
            return new OvenCookingRecipe(id, input, outputs, xp);
        }

        @Override
        public void write(PacketByteBuf buf, OvenCookingRecipe recipe) {
            recipe.getInput().write(buf);
            buf.writeVarInt(recipe.outputs.size());
            for (var stack : recipe.outputs) {
                buf.writeItemStack(stack);
            }
            buf.writeFloat(recipe.getXp());
        }
    }
}
