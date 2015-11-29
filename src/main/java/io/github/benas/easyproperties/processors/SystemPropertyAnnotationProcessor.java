/*
 *   The MIT License
 *
 *    Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *    Permission is hereby granted, free of charge, to any person obtaining a copy
 *    of this software and associated documentation files (the "Software"), to deal
 *    in the Software without restriction, including without limitation the rights
 *    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *    copies of the Software, and to permit persons to whom the Software is
 *    furnished to do so, subject to the following conditions:
 *
 *    The above copyright notice and this permission notice shall be included in
 *    all copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *    THE SOFTWARE.
 */

package io.github.benas.easyproperties.processors;

import io.github.benas.easyproperties.annotations.SystemProperty;
import io.github.benas.easyproperties.api.AnnotationProcessingException;
import io.github.benas.easyproperties.api.AnnotationProcessor;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An annotation processor that loads properties from system properties.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class SystemPropertyAnnotationProcessor extends AbstractAnnotationProcessor implements AnnotationProcessor<SystemProperty> {

    /**
     * Logger instance.
     */
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public void processAnnotation(final SystemProperty systemProperty, final Field field, final Object object) throws AnnotationProcessingException {

        String key = systemProperty.value().trim();

        //check key value
        if (key.isEmpty()) {
            throw new AnnotationProcessingException(missingAttributeValue("value", "@SystemProperty", field, object));
        }

        //check system property
        String value = System.getProperty(key);
        if (value == null) {
            logger.log(Level.WARNING, "System property " + key + " on field " + field.getName() +
                    " of type " + object.getClass() + " not found in system properties: "
                    + System.getProperties());

            //Use default value if specified
            String defaultValue = systemProperty.defaultValue();
            if (defaultValue != null && !defaultValue.isEmpty()) {
                value = defaultValue.trim();
            } else {
                throw new AnnotationProcessingException(missingAttributeValue("defaultValue", "@SystemProperty", field, object));
            }
        }

        processAnnotation(object, field, key, value);

    }
}
