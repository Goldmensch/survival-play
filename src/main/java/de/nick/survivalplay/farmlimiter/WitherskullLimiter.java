package de.nick.survivalplay.farmlimiter;

import de.nick.survivalplay.SurvivalPlay;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

public class WitherskullLimiter {

    private final SurvivalPlay main;
    private final ItemStack witherskull;

    public WitherskullLimiter(SurvivalPlay main) {
        this.main = main;
        witherskull = new ItemStack(Material.WITHER_SKELETON_SKULL);
    }


    public void callSpawn(Entity entity, Location location) {
        // check if entity is a Witherskeleton
        if (entity.getType() == EntityType.WITHER_SKELETON) {
            // check if it enabled in config
            if (main.getConfigHandler().witherskullfarmDisable()) {
                // get spawn block and block above
                Material block = location.getBlock().getType();
                Material blockAbove = location.getWorld().getBlockAt(location.getBlockX(), location.getBlockY() + 1, location.getBlockZ()).getType();
                // check if blocktypes are Wither_rose
                if ((block == Material.WITHER_ROSE) || (blockAbove == Material.WITHER_ROSE)) {
                    // set metadata for mobs
                    entity.setMetadata("witherskullfarm", new FixedMetadataValue(main, true));
                }
            }
        }

    }

    public void callDeath(Entity entity, List<ItemStack> drops) {
        // check if entity has Metadata
        if (entity.hasMetadata("witherskullfarm")) {
            // remove witherskull from drops
            drops.remove(witherskull);
        }
    }

}
