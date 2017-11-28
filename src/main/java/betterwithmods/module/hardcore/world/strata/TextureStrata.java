package betterwithmods.module.hardcore.world.strata;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.util.math.BlockPos;
import team.chisel.ctm.api.texture.ITextureContext;
import team.chisel.ctm.api.util.TextureInfo;
import team.chisel.ctm.client.texture.ctx.TextureContextPosition;
import team.chisel.ctm.client.texture.render.AbstractTexture;
import team.chisel.ctm.client.util.Quad;

import javax.annotation.Nullable;
import java.util.List;

public class TextureStrata extends AbstractTexture<TextureTypeStrata> {
    public TextureStrata(TextureTypeStrata type, TextureInfo info) {
        super(type, info);
    }

    @Override
    public List<BakedQuad> transformQuad(BakedQuad quad, @Nullable ITextureContext context, int quadGoal) {
        if (context instanceof TextureContextPosition) {
            TextureContextPosition c = (TextureContextPosition) context;
            BlockPos pos = c.getPosition();
            int strata = HCStrata.getStratification(pos.getY(), 50);
//            if(y >= (topY-10))
//                return 0;
//            if(y >= (topY-30))
//                return 1;
//            return 2;
            Quad q = makeQuad(quad,context);
            System.out.println(sprites.length);
            return Lists.newArrayList( q.transformUVs(sprites[strata]).rebake());
        }
        return Lists.newArrayList(quad);
    }
}
