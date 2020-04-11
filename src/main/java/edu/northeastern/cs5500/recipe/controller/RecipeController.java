package edu.northeastern.cs5500.recipe.controller;

import edu.northeastern.cs5500.recipe.model.Recipe;
import edu.northeastern.cs5500.recipe.repository.GenericRepository;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

@Singleton
@Slf4j
public class RecipeController {
    private final GenericRepository<Recipe> recipes;

    @Inject
    RecipeController(GenericRepository<Recipe> recipeRepository) {
        recipes = recipeRepository;

        log.info("RecipeController > construct");

        if (recipes.count() > 0) {
            return;
    }

        log.info("RecipeController > construct > adding default recipes");

        final Recipe defaultRecipe1 = new Recipe();
        defaultRecipe1.setTitle("Hot dog");

        final Recipe defaultRecipe2 = new Recipe();
        defaultRecipe2.setTitle("A steak");
        defaultRecipe2.setDescription("Not a hot dog");

        try {
            addRecipe(defaultRecipe1);
            addRecipe(defaultRecipe2);
        } catch (Exception e) {
            log.error("RecipeController > construct > adding default recipes > failure?");
            e.printStackTrace();
        }
    }

    @Nullable
    public Recipe getRecipe(@Nonnull ObjectId uuid) {
        log.debug("RecipeController > getRecipe({})", uuid);
        return recipes.get(uuid);
    }

    @Nonnull
    public Collection<Recipe> getRecipes() {
        log.debug("RecipeController > getRecipes()");
        return recipes.getAll();
    }

    @Nonnull
    public Recipe addRecipe(@Nonnull Recipe recipe) throws Exception {
        log.debug("RecipeController > addRecipe(...)");
        if (!recipe.isValid()) {
            // TODO: replace with a real invalid object exception
            // probably not one exception per object type though...
            throw new Exception("InvalidRecipeException");
        }

        ObjectId id = recipe.getId();

        if (id != null && recipes.get(id) != null) {
            // TODO: replace with a real duplicate key exception
            throw new Exception("DuplicateKeyException");
        }

        return recipes.add(recipe);
    }

    public void updateRecipe(@Nonnull Recipe recipe) throws Exception {
        log.debug("RecipeController > updateRecipe(...)");
        recipes.update(recipe);
        }

    public void deleteRecipe(@Nonnull ObjectId id) throws Exception {
        log.debug("RecipeController > deleteRecipe(...)");
        recipes.delete(id);
    }
}
