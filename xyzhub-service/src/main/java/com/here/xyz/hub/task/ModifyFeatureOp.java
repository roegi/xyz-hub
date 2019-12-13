/*
 * Copyright (C) 2017-2019 HERE Europe B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * License-Filename: LICENSE
 */

package com.here.xyz.hub.task;

import com.google.common.base.Objects;
import com.here.xyz.XyzSerializable;
import com.here.xyz.hub.util.diff.Difference;
import com.here.xyz.hub.util.diff.Patcher;
import com.here.xyz.models.geojson.implementation.Feature;
import java.util.List;
import java.util.Map;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class ModifyFeatureOp extends ModifyOp<Feature, Feature, Feature> {

  public ModifyFeatureOp(List<Feature> inputStates, IfNotExists ifNotExists, IfExists ifExists, boolean isTransactional) {
    super(inputStates, ifNotExists, ifExists, isTransactional);
  }

  @Override
  public Feature patch(Feature headState, Feature inputState) {
    final Map<String, Object> headStateMap = headState.asMap();

    final Difference diff = Patcher.calculateDifferenceOfPartialUpdate(headStateMap, inputState.asMap(), null, true);
    Patcher.patch(headStateMap, diff);
    return XyzSerializable.fromMap(headStateMap, Feature.class);
  }

  @Override
  public Feature merge(Feature headState, Feature editedState, Feature inputState) throws ModifyOpError {
    if (equalStates(editedState, headState)) {
      return replace(headState, inputState);
    }
    final Map<String, Object> editedStateMap = editedState.asMap();
    final Difference diffInput = Patcher.getDifference(editedStateMap, inputState.asMap());
    final Difference diffHead = Patcher.getDifference(editedStateMap, headState.asMap());
    try {
      final Difference mergedDiff = Patcher.mergeDifferences(diffInput, diffHead);
      Patcher.patch(editedStateMap, mergedDiff);
      return XyzSerializable.fromMap(editedStateMap, Feature.class);
    } catch (Exception e) {
      throw new ModifyOpError(e.getMessage());
    }
  }

  @Override
  public Feature replace(Feature headState, Feature inputState) {
    return inputState.copy();
  }

  @Override
  public Feature create(Feature inputState) {
    return inputState.copy();
  }

  @Override
  public Feature transform(Feature sourceState) {
    return sourceState;
  }

  @Override
  public boolean equalStates(Feature a, Feature b) {
    // TODO: Check the UUID
    return Objects.equal(a, b);
  }
}
