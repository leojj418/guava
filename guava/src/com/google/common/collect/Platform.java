/*
 * Copyright (C) 2008 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

import static com.google.common.base.Strings.lenientFormat;
import static java.lang.Boolean.parseBoolean;

import com.google.common.annotations.GwtCompatible;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Methods factored out so that they can be emulated differently in GWT.
 *
 * @author Hayward Chan
 */
@GwtCompatible(emulated = true)
final class Platform {
  /** Returns the platform preferred implementation of a map based on a hash table. */
  static <K extends @Nullable Object, V extends @Nullable Object>
      Map<K, V> newHashMapWithExpectedSize(int expectedSize) {
    return Maps.newHashMapWithExpectedSize(expectedSize);
  }

  /**
   * Returns the platform preferred implementation of an insertion ordered map based on a hash
   * table.
   */
  static <K extends @Nullable Object, V extends @Nullable Object>
      Map<K, V> newLinkedHashMapWithExpectedSize(int expectedSize) {
    return Maps.newLinkedHashMapWithExpectedSize(expectedSize);
  }

  /** Returns the platform preferred implementation of a set based on a hash table. */
  static <E extends @Nullable Object> Set<E> newHashSetWithExpectedSize(int expectedSize) {
    return Sets.newHashSetWithExpectedSize(expectedSize);
  }

  /**
   * Returns the platform preferred implementation of an insertion ordered set based on a hash
   * table.
   */
  static <E extends @Nullable Object> Set<E> newLinkedHashSetWithExpectedSize(int expectedSize) {
    return Sets.newLinkedHashSetWithExpectedSize(expectedSize);
  }

  /**
   * Returns the platform preferred map implementation that preserves insertion order when used only
   * for insertions.
   */
  static <K extends @Nullable Object, V extends @Nullable Object>
      Map<K, V> preservesInsertionOrderOnPutsMap() {
    return Maps.newLinkedHashMap();
  }

  /**
   * Returns the platform preferred set implementation that preserves insertion order when used only
   * for insertions.
   */
  static <E extends @Nullable Object> Set<E> preservesInsertionOrderOnAddsSet() {
    return Sets.newLinkedHashSet();
  }

  /**
   * Returns a new array of the given length with the same type as a reference array.
   *
   * @param reference any array of the desired type
   * @param length the length of the new array
   */
  @SuppressWarnings("argument.type.incompatible") // arrays
  static <T> T[] newArray(T[] reference, int length) {
    Class<?> type = reference.getClass().getComponentType();

    // the cast is safe because
    // result.getClass() == reference.getClass().getComponentType()
    @SuppressWarnings("unchecked")
    T[] result = (T[]) Array.newInstance(type, length);
    return result;
  }

  /** Equivalent to Arrays.copyOfRange(source, from, to, arrayOfType.getClass()). */
  @SuppressWarnings("return.type.incompatible") // arrays
  static <T> T[] copy(Object[] source, int from, int to, T[] arrayOfType) {
    return Arrays.copyOfRange(source, from, to, (Class<? extends T[]>) arrayOfType.getClass());
  }

  /**
   * Configures the given map maker to use weak keys, if possible; does nothing otherwise (i.e., in
   * GWT). This is sometimes acceptable, when only server-side code could generate enough volume
   * that reclamation becomes important.
   */
  static MapMaker tryWeakKeys(MapMaker mapMaker) {
    return mapMaker.weakKeys();
  }

  static int reduceIterationsIfGwt(int iterations) {
    return iterations;
  }

  static int reduceExponentIfGwt(int exponent) {
    return exponent;
  }

  private static final String GWT_RPC_PROPERTY_NAME = "guava.gwt.emergency_reenable_rpc";

  static void checkGwtRpcEnabled() {
    if (!parseBoolean(System.getProperty(GWT_RPC_PROPERTY_NAME, "true"))) {
      throw new UnsupportedOperationException(
          lenientFormat(
              "We are removing GWT-RPC support for Guava types. You can temporarily reenable"
                  + " support by setting the system property %s to true. For more about system"
                  + " properties, see %s. For more about Guava's GWT-RPC support, see %s.",
              GWT_RPC_PROPERTY_NAME,
              "https://stackoverflow.com/q/5189914/28465",
              "https://groups.google.com/d/msg/guava-announce/zHZTFg7YF3o/rQNnwdHeEwAJ"));
    }
  }

  private Platform() {}
}
