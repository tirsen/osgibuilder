package com.thoughtworks.osgibuilder;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class CompositeBundleLocatorTest {
    Mockery context = new JUnit4Mockery();

    public class HardwiredBundleResolver implements BundleResolver {
        private Bundle bundle;

        public HardwiredBundleResolver(Bundle bundle) {
            this.bundle = bundle;
        }

        public Bundle byName(String name) {
            return bundle;
        }

        public Bundle byPackage(String name) {
            return bundle;
        }
    }

    @Test
    public void picksFirstNonNullBundle() {
        CompositeBundleResolver compositeLocator = new CompositeBundleResolver();
        compositeLocator.addBundleLocator(new HardwiredBundleResolver(null));
        compositeLocator.addBundleLocator(new HardwiredBundleResolver(new Bundle("bundle1")));
        compositeLocator.addBundleLocator(new HardwiredBundleResolver(new Bundle("bundle2")));

        assertThat(compositeLocator.byName(null).getName(), equalTo("bundle1"));
        assertThat(compositeLocator.byPackage(null).getName(), equalTo("bundle1"));
    }
}
