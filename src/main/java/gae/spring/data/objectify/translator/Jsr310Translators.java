package gae.spring.data.objectify.translator;

import com.googlecode.objectify.impl.translate.Translators;

/**
 * <p>A convenient static method that adds all the JSR-310(jdk-8 date-time API) related converters to your factory's translator list.
 * <p>
 * <p>{@code Jsr310Translators.addTo(ObjectifyService.factory());}
 * <p>
 * <p>All custom translators must be registered <strong>before</strong> entity classes are registered.</p>
 */
public class Jsr310Translators {
    /**
     * Add the default JSR-310 translators to the given translators list.
     *
     * @param translators Objectify translators list.
     */
    public static void addTo(Translators translators) {
        translators.add(new OffsetDateTimeDateTranslatorFactory());
        translators.add(new ZonedDateTimeDateTranslatorFactory());
    }
}
