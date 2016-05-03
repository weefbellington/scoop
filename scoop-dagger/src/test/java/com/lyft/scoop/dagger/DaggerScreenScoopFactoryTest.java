package com.lyft.scoop.dagger;

import com.lyft.scoop.Scoop;
import com.lyft.scoop.Screen;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class DaggerScreenScoopFactoryTest {

    @Mock
    DaggerInjector mockDaggerInjector;

    private DaggerScreenScoopFactory daggerScreenScooper;
    private Scoop.Builder scoopBuilder;
    private Scoop scoop;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        daggerScreenScooper = new DaggerScreenScoopFactory();
        scoop = new Scoop.Builder("root").service(DaggerInjector.SERVICE_NAME, mockDaggerInjector).build();
        scoopBuilder = new Scoop.Builder("child");
    }

    @Test
    public void testAddModuleDynamic() {
        when(mockDaggerInjector.extend(any(TestModule.class))).thenReturn(mockDaggerInjector);
        assertNotNull(daggerScreenScooper.addServices(scoopBuilder, new TestScreenDynamicModule(), scoop));
    }

    @Test
    public void testAddModuleStatic() {
        when(mockDaggerInjector.extend(any(TestModule.class))).thenReturn(mockDaggerInjector);
        assertNotNull(daggerScreenScooper.addServices(scoopBuilder, new TestScreenStaticModule(), scoop));
    }

    @Test
    public void testNoModule() {
        assertNotNull(daggerScreenScooper.addServices(scoopBuilder, new TestNoModuleScreen(), scoop));
    }

    @DaggerModule(TestModule.class)
    private static class TestScreenStaticModule extends Screen {

    }

    private static class TestScreenDynamicModule extends Screen implements DaggerScreen {
        @Override
        public Object[] getModules() {
            return new Object[] { new TestModule()} ;
        }
    }

    static class TestNoModuleScreen extends Screen {

    }

    static class TestModule {

    }
}
