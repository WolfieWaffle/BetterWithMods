package betterwithmods.common.entity.ai;

import betterwithmods.common.entity.EntitySpiderWeb;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.MathHelper;

/**
 * Created by primetoxinz on 4/22/17.
 */
public class ShooterSpiderWeb extends EntityAIBase {
    private EntitySpider spider;
    private EntityLivingBase target;
    private int seeTime;
    private int attackTime, maxAttackTime;
    private float radius, maxRadius;

    public ShooterSpiderWeb(EntitySpider spider, int maxAttackTime, float radius) {
        this.spider = spider;
        this.maxAttackTime = maxAttackTime;
        this.maxRadius = this.radius = radius;
        setMutexBits(3);
        this.attackTime = -1;
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase target = spider.getAttackTarget();
        if (target != null) {
            this.target = target;
            double d = spider.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            return !(Math.sqrt(d) < 3 || target.isInWater() || target.isInWeb);
        }
        return false;
    }

    @Override
    public void updateTask() {
        double d = spider.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
        boolean canSee = spider.getEntitySenses().canSee(target);
        if (d <= (maxRadius * maxRadius)) {
            spider.getNavigator().clearPath();
        } else {
            spider.getNavigator().tryMoveToEntityLiving(target, 1.0F);
        }
        spider.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
        if (attackTime-- == 0) {
        	if(canSee) {
		        shootWeb();
	        }
            float f = MathHelper.sqrt(d) / (radius);
            attackTime = MathHelper.floor(f * maxAttackTime);

        } else if (attackTime < 0) {
            float f2 = MathHelper.sqrt(d) / (radius);
            attackTime = MathHelper.floor(f2 * maxAttackTime);
        }
        super.updateTask();
    }

    @Override
    public boolean shouldContinueExecuting() {
        return this.shouldExecute() || !spider.getNavigator().noPath();
    }

    public void resetTask() {
        target = null;
        this.attackTime = -1;
    }

    private void shootWeb() {
	    EntitySpiderWeb web = new EntitySpiderWeb(spider.getEntityWorld(), spider);
	    double d0 = target.posY + (double)target.getEyeHeight() - 1.100000023841858D;
	    double d1 = target.posX - spider.posX;
	    double d2 = d0 - web.posY;
	    double d3 = target.posZ - spider.posZ;
	    float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
	    web.shoot(d1, d2 + (double)f, d3, 1.0F, 2);
	    spider.playSound(SoundEvents.ENTITY_SLIME_SQUISH, 1.0F, 1.0F / (spider.getRNG().nextFloat() * 0.4F + 0.8F));
	    spider.world.spawnEntity(web);
    }
}
