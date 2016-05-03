package com.lyft.scoop.dagger;

import com.lyft.scoop.Scoop;
import com.lyft.scoop.Screen;
import com.lyft.scoop.ScreenScoopFactory;

public class DaggerScreenScoopFactory extends ScreenScoopFactory {

    @Override
    protected Scoop addServices(Scoop.Builder scoopBuilder, Screen screen, Scoop parentScoop) {
        DaggerInjector parentDagger = DaggerInjector.fromScoop(parentScoop);


        Object[] modules = extractModules(screen);

        if (modules == null) {
            return scoopBuilder.service(DaggerInjector.SERVICE_NAME, parentDagger).build();
        }
        else {
            DaggerInjector screenInjector = parentDagger.extend(modules);
            return scoopBuilder.service(DaggerInjector.SERVICE_NAME, screenInjector).build();
        }
    }

    private Object[] extractModules(Screen screen) {
        return (screen instanceof DaggerScreen) ? extractModulesDynamic((DaggerScreen) screen) : extractModulesStatic(screen);
    }

    private Object[] extractModulesDynamic(DaggerScreen screen) {
        return screen.getModules();
    }

    private Object[] extractModulesStatic(Screen screen) {
        DaggerModule daggerModule = screen.getClass().getAnnotation(DaggerModule.class);
        if (daggerModule == null) return null;
        try {
            final Object module = daggerModule.value().newInstance();
            return new Object[] { module };
        } catch (Throwable e) {
            throw new RuntimeException("Failed to instantiate module for screen: " + screen.getClass().getSimpleName(), e);
        }
    }
}
