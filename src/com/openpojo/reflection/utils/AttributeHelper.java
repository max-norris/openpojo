/**
 * Copyright (C) 2010 Osman Shoukry
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.openpojo.reflection.utils;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import com.openpojo.reflection.exception.ReflectionException;

/**
 * This Class is responsible for normalizing field names to attribute names.
 * The reason for this is some companies have policies that require all
 * member variables start with "m" or "its" or any of the hungarian notation prefixes.
 *
 * @author oshoukry
 */
public class AttributeHelper {

    private static final List<String> fieldPrefixes = new LinkedList<String>();

    /**
     * If your fields are prefixed with some pre-defined string register them here.
     * You can register more than one prefix.
     *
     * @param prefix
     *            The prefix to register.
     */
    public synchronized static void registerFieldPrefix(final String prefix) {
        // don't allow null, empty or duplicate prefixes.
        if (prefix != null && prefix.length() > 0 && !fieldPrefixes.contains(prefix)) {
            fieldPrefixes.add(prefix);
        }
    }

    /**
     * If a prefix is no longer needed unregister it here.
     *
     * @param prefix
     *            The prefix to un-register.
     */
    public synchronized static void unregisterFieldPrefix(final String prefix) {
        fieldPrefixes.remove(prefix);
    }

    /**
     * This method removes all registered values.
     */
    public synchronized static void clearRegistery() {
        fieldPrefixes.clear();
    }

    /**
     * This method returns the attribute name given a field name.
     * The field name will get stripped of prefixes
     *
     * @param field
     * @return
     */
    public synchronized static String getAttributeName(final Field field) {
        String normalizedFieldName = field.getName();
        normalizedFieldName = stripPrefix(normalizedFieldName);
        return formattedFieldName(normalizedFieldName);
    }

    private static String stripPrefix(final String fieldName) {
        for (String prefix : fieldPrefixes) {
            if (fieldName.equals(prefix)) {
                throw ReflectionException.getInstance(String.format(
                        "Field name =[%s] matches registered prefix=[%s], if stripped, empty string will result",
                        fieldName, prefix));
            }
            if (fieldName.startsWith(prefix)) {
                return fieldName.substring(prefix.length(), fieldName.length());
            }
        }
        return fieldName;
    }

    /**
     * Properly CamelCased the field name as expected "first Letter is uppercase, rest is unchanged".
     *
     * @param field
     *            The field to proper case.
     * @return
     *         Formatted field name.
     */
    private static String formattedFieldName(final String fieldName) {
        return fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1, fieldName.length());
    }
}