package com.lela.motivator.junit;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;

/**
 * Used as an alternative implementation of the Junit 4 Parameterized runner
 */
public class ParameterizedWithName extends Suite {
    /**
     * Annotation for a method which provides parameters to be injected into the
     * test class constructor by <code>Parameterized</code>
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Parameters {
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public static @interface Name {
    }

    private class TestClassRunnerForParameters extends
            BlockJUnit4ClassRunner {
        private final int fParameterSetNumber;

        private final List<Object[]> fParameterList;
        private Object testInstance;

        TestClassRunnerForParameters(Class<?> type,
                                     List<Object[]> parameterList, int i) throws InitializationError {
            super(type);
            fParameterList= parameterList;
            fParameterSetNumber= i;

            try {
            Object[] computeParams = null;
            try {
                computeParams = fParameterList.get(fParameterSetNumber);
            } catch (ClassCastException e) {
                throw new Exception(String.format(
                        "%s.%s() must return a Collection of arrays.",
                        getTestClass().getName(), getParametersMethod(getTestClass()).getName()));
            }
            
            testInstance = getTestClass().getOnlyConstructor().newInstance(computeParams);
            } catch (Exception e) {
                throw new InitializationError(e);
            }
        }

        @Override
        public Object createTest() throws Exception {
            return testInstance;
        }

        private Object[] computeParams() throws Exception {
            try {
                return fParameterList.get(fParameterSetNumber);
            } catch (ClassCastException e) {
                throw new Exception(String.format(
                        "%s.%s() must return a Collection of arrays.",
                        getTestClass().getName(), getParametersMethod(
                        getTestClass()).getName()));
            }
        }

        @Override
        protected String getName() {

            String name = null;
            try {
                Method m = getNameMethod();
                if (m != null)
                    name = (String) m.invoke(testInstance);
            } catch (Exception e) {
            }

            return String.format("[%s]", name != null ? name : fParameterSetNumber);
        }

        @Override
        protected String testName(final FrameworkMethod method) {

            return String.format("%s[%s]", method.getName(), fParameterSetNumber);
        }

        @Override
        protected void validateConstructor(List<Throwable> errors) {
            validateOnlyOneConstructor(errors);
        }

        @Override
        protected Statement classBlock(RunNotifier notifier) {
            return childrenInvoker(notifier);
        }

        private Method getNameMethod() throws Exception {
            for (Method each : this.getTestClass().getJavaClass().getMethods()) {
                if (Modifier.isPublic((each.getModifiers()))) {
                    Annotation[] annotations = each.getAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType() == Name.class) {
                            if (each.getReturnType().equals(String.class))
                                return each;
                            else
                                throw new Exception("Name annotated method doesn't return an object of type String.");
                        }
                    }
                }
            }
            return null;
        }
    }

    private final ArrayList<Runner> runners= new ArrayList<Runner>();

    /**
     * Only called reflectively. Do not use programmatically.
     */
    public ParameterizedWithName(Class<?> klass) throws Throwable {
        super(klass, Collections.<Runner>emptyList());
        List<Object[]> parametersList= getParametersList(getTestClass());
        for (int i= 0; i < parametersList.size(); i++)
            runners.add(new TestClassRunnerForParameters(getTestClass().getJavaClass(),
                    parametersList, i));
    }

    @Override
    protected List<Runner> getChildren() {
        return runners;
    }

    @SuppressWarnings("unchecked")
    private List<Object[]> getParametersList(TestClass klass)
            throws Throwable {
        return (List<Object[]>) getParametersMethod(klass).invokeExplosively(
                null);
    }

    private FrameworkMethod getParametersMethod(TestClass testClass)
            throws Exception {
        List<FrameworkMethod> methods= testClass
                .getAnnotatedMethods(Parameters.class);
        for (FrameworkMethod each : methods) {
            int modifiers= each.getMethod().getModifiers();
            if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
                return each;
        }

        throw new Exception("No public static parameters method on class "
                + testClass.getName());
    }
}
