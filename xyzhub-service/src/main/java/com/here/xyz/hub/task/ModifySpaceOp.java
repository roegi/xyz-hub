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

import com.here.xyz.hub.connectors.models.Space;
import com.here.xyz.hub.util.diff.Difference;
import com.here.xyz.hub.util.diff.Patcher;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import java.util.List;
import java.util.Map;

public class ModifySpaceOp extends ModifyOp<JsonObject, Space, Space> {

  public ModifySpaceOp(List<JsonObject> inputStates, IfNotExists ifNotExists, IfExists ifExists,
      boolean isTransactional) {
    super(inputStates, ifNotExists, ifExists, isTransactional);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public Space patch(Space headState, JsonObject inputState) throws ModifyOpError {
    Map headClone = Json.mapper.convertValue(headState, Map.class);
    Map input = inputState.getMap();
    final Difference difference = Patcher.calculateDifferenceOfPartialUpdate(headClone, input, null, true);
    Patcher.patch(headClone, difference);
    return Json.mapper.convertValue(headClone, Space.class);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Space merge(Space headState, Space editedState, JsonObject inputState) throws ModifyOpError {
    Map headClone = Json.mapper.convertValue(headState, Map.class);
    Map editedClone = Json.mapper.convertValue(editedState, Map.class);
    Map input = inputState.getMap();

    final Difference diffInput = Patcher.getDifference(editedClone, input);
    final Difference diffHead = Patcher.getDifference(editedClone, headClone);
    try {
      final Difference mergedDiff = Patcher.mergeDifferences(diffInput, diffHead);
      Patcher.patch(headClone, mergedDiff);
      return Json.mapper.convertValue(headClone, Space.class);
    } catch (Exception e) {
      throw new ModifyOpError(e.getMessage());
    }
  }

  @Override
  public Space replace(Space headState, JsonObject inputState) throws ModifyOpError {
    return inputState.mapTo(Space.class);
  }

  @Override
  public Space create(JsonObject input) throws ModifyOpError {
    return input.mapTo(Space.class);
  }

  @Override
  public Space transform(Space sourceState) throws ModifyOpError {
    return Json.decodeValue(Json.encode(sourceState), Space.class);
  }

  @Override
  public boolean equalStates(Space state1, Space state2) {
    if (state1 == null && state2 == null) {
      return true;
    }
    if (state1 == null || state2 == null) {
      return false;
    }

    Difference diff = Patcher.getDifference(Json.mapper.convertValue(state1, Map.class), Json.mapper.convertValue(state2, Map.class));
    return diff == null;
  }
}
