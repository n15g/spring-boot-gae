package contrib.springframework.data.gcp.objectify.translator;

import com.googlecode.objectify.impl.translate.Translators;

/**
 * A convenient static method that adds all the JSR-310(jdk-8 date-time API) related converters to your factory's translator list.
 * <p>
 * {@code Jsr310Translators.addTo(ObjectifyService.factory());}
 * <p>
 * All custom translators must be registered <strong>before</strong> entity classes are registered.
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
        translators.add(new LocalDateDateTranslatorFactory());
    }
}
