package org.springframework.contrib.gae.search.metadata.impl;

import org.springframework.contrib.gae.search.SearchIndex;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

public class MethodNameCalculatorTest {
    private Function<Method, String> analyzer = new MethodNameCalculator();

    @Test
    public void apply_willHandleStraightforwardMethod() throws Exception {
        assertThat(analyzer.apply(Methods.class.getDeclaredMethod("straightForwardMethod")))
                .isEqualTo("straightForwardMethod");
    }

    @Test
    public void apply_willHandleNamedMethod() throws Exception {
        assertThat(analyzer.apply(Methods.class.getDeclaredMethod("namedMethod"))
        ).isEqualTo("anotherName");
    }

    @Test
    public void apply_willHandleUnannotatedMethod() throws Exception {
        assertThat(analyzer.apply(Methods.class.getDeclaredMethod("unannotatedMethod")))
                .isEqualTo("unannotatedMethod");
    }

    @Test
    public void apply_willHandleGetWithJavaBeanNaming() throws Exception {
        assertThat(analyzer.apply(Methods.class.getDeclaredMethod("getWithJavaBeanNaming")))
                .isEqualTo("withJavaBeanNaming");
    }

    @Test
    public void apply_willHandleIsWithJavaBeanNaming() throws Exception {
        assertThat(analyzer.apply(Methods.class.getDeclaredMethod("isWithJavaBeanNaming")))
                .isEqualTo("withJavaBeanNaming");
    }

    @Test
    public void apply_willHandleMethodWithUnderscore() throws Exception {
        assertThat(analyzer.apply(Methods.class.getDeclaredMethod("method_withAnUnderscore")))
                .isEqualTo("method_withAnUnderscore");
    }

    private interface Methods {
        @SearchIndex
        void straightForwardMethod();

        @SearchIndex("anotherName")
        void namedMethod();

        void unannotatedMethod();

        @SearchIndex
        void getWithJavaBeanNaming();

        @SearchIndex
        void isWithJavaBeanNaming();

        @SearchIndex
        void method_withAnUnderscore();
    }
}