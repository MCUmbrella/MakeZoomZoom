package xyz.ajp.makezoomzoom;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import org.apache.commons.io.IOUtils;
import org.joor.Reflect;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.transformer.MixinTransformer;
import org.spongepowered.asm.mixin.transformer.Proxy;
import xyz.ajp.makezoomzoom.test.ProxyMethodTest;

import java.io.IOException;
import java.net.MalformedURLException;

@Mod(modid="makezoomzoom", name="Make Zoom Zoom", version="0.0.2")
public class MakeZoomZoom {
    @Mod.EventHandler
    public static void preInit(FMLConstructionEvent event) {
        ProxyMethodTest.doNothing();
        System.out.println("MakeZoomZoom loading");
        try {
            IOUtils.toByteArray(Loader.instance().getModClassLoader().getResourceAsStream("pl/asie/charset/lib/material/ItemMaterialHeuristics.class"));
        } catch (IOException|NullPointerException e) {
            for (ModContainer mod : Loader.instance().getModList()) {
                try {
                    Loader.instance().getModClassLoader().addFile(mod.getSource());
                } catch (MalformedURLException etTu) {
                    throw new RuntimeException(etTu);
                }
            }
        }
        Mixins.addConfiguration("mixins.mzz.charsetv1.json");
        Proxy mixinProxy = (Proxy) Launch.classLoader.getTransformers().stream().filter(transformer -> transformer instanceof Proxy).findFirst().get();
        MixinTransformer transformer = Reflect.on(mixinProxy).field("transformer").get();
        Reflect.on(transformer).call("selectConfigs", MixinEnvironment.getCurrentEnvironment());
        Reflect.on(transformer).call("prepareConfigs", MixinEnvironment.getCurrentEnvironment());
//        Proxy mixinProxy = (Proxy) Launch.classLoader.getTransformers().stream().filter(transformer -> transformer instanceof Proxy).findFirst().get();
//        try {
//            Field transformerField = Proxy.class.getDeclaredField("transformer");
//            transformerField.setAccessible(true);
//            MixinTransformer transformer = (MixinTransformer) transformerField.get(mixinProxy);
//
//            Method selectConfigsMethod = MixinTransformer.class.getDeclaredMethod("selectConfigs", MixinEnvironment.class);
//            selectConfigsMethod.setAccessible(true);
//            selectConfigsMethod.invoke(transformer, MixinEnvironment.getCurrentEnvironment());
//
//            Method prepareConfigsMethod = MixinTransformer.class.getDeclaredMethod("prepareConfigs", MixinEnvironment.class);
//            prepareConfigsMethod.setAccessible(true);
//            prepareConfigsMethod.invoke(transformer, MixinEnvironment.getCurrentEnvironment());
//        } catch (ReflectiveOperationException e) {
//            throw new RuntimeException(e);
//        }
    }
}
