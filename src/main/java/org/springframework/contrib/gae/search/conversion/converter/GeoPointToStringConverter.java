package org.springframework.contrib.gae.search.conversion.converter;

import com.google.appengine.api.search.GeoPoint;
import org.springframework.core.convert.converter.Converter;

/**
 * Convert {@link GeoPoint} values to {@link String} values.
 */
public class GeoPointToStringConverter implements Converter<GeoPoint, String> {
    @Override
    public String convert(GeoPoint source) {
        return String.format("geopoint(%s, %s)", source.getLatitude(), source.getLongitude());
    }
}
