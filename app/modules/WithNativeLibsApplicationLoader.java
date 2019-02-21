package modules;

import play.ApplicationLoader;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.libs.NativeLoader;

import javax.inject.Singleton;

@Singleton
public class WithNativeLibsApplicationLoader extends GuiceApplicationLoader {
    public GuiceApplicationBuilder builder(ApplicationLoader.Context context) {
        NativeLoader.load("jniortools");
        return super.builder(context);
    }
}