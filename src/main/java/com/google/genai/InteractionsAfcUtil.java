/*
 * Copyright 2025 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.genai;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Arrays.stream;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.genai.types.interactions.CreateInteractionConfig;
import com.google.genai.types.interactions.Interaction;
import com.google.genai.types.interactions.content.FunctionCallContent;
import com.google.genai.types.interactions.content.FunctionResultContent;
import com.google.genai.types.interactions.content.Content;
import com.google.genai.types.interactions.tools.FunctionTool;
import com.google.genai.types.interactions.tools.Tool;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for Automatic Function Calling (AFC) in the Interactions API.
 *
 * <p>This class provides methods to check if AFC is enabled, build function maps, and execute
 * function calls.
 */
final class InteractionsAfcUtil {
  static final int DEFAULT_MAX_REMOTE_CALLS_AFC = 10;

  private InteractionsAfcUtil() {}

  /**
   * Check if the config has any FunctionTool with a callable method.
   *
   * @param config The CreateInteractionConfig to check.
   * @return true if there is at least one FunctionTool with a method set.
   */
  static boolean hasCallableTool(CreateInteractionConfig config) {
    if (config == null || !config.tools().isPresent()) {
      return false;
    }
    for (Tool tool : config.tools().get()) {
      if (tool instanceof FunctionTool) {
        FunctionTool ft = (FunctionTool) tool;
        if (ft.method().isPresent()) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Build a map of function name to Method for invocation.
   *
   * @param config The CreateInteractionConfig containing tools.
   * @return An ImmutableMap mapping function names to their Methods.
   */
  static ImmutableMap<String, Method> getFunctionMap(CreateInteractionConfig config) {
    ImmutableMap.Builder<String, Method> builder = ImmutableMap.builder();
    if (config != null && config.tools().isPresent()) {
      for (Tool tool : config.tools().get()) {
        if (tool instanceof FunctionTool) {
          FunctionTool ft = (FunctionTool) tool;
          if (ft.method().isPresent() && ft.name().isPresent()) {
            builder.put(ft.name().get(), ft.method().get());
          }
        }
      }
    }
    return builder.buildOrThrow();
  }

  /**
   * Extract all FunctionCallContent objects from an Interaction response.
   *
   * @param response The Interaction response.
   * @return A list of FunctionCallContent objects, or an empty list if none.
   */
  static ImmutableList<FunctionCallContent> extractFunctionCalls(Interaction response) {
    if (response == null || !response.outputs().isPresent()) {
      return ImmutableList.of();
    }
    return response.outputs().get().stream()
        .filter(content -> content instanceof FunctionCallContent)
        .map(content -> (FunctionCallContent) content)
        .collect(toImmutableList());
  }

  /**
   * Execute a function call and return the result content.
   *
   * @param call The FunctionCallContent describing the function to call.
   * @param functionMap A map from function names to Methods.
   * @return A FunctionResultContent with the result, or null if the function is not found.
   */
  static FunctionResultContent executeFunctionCall(
      FunctionCallContent call, ImmutableMap<String, Method> functionMap) {
    String funcName = call.name();
    Method method = functionMap.get(funcName);
    if (method == null) {
      return null;
    }

    Map<String, Object> args = call.arguments();
    String callId = call.id();

    try {
      Object result = invokeFunctionMethod(method, args);
      if (result == null) {
        return FunctionResultContent.of(callId, funcName, ImmutableMap.of("result", ""));
      }
      return FunctionResultContent.of(callId, funcName, ImmutableMap.of("result", result));
    } catch (Exception e) {
      return FunctionResultContent.of(callId, funcName, ImmutableMap.of("error", e.toString()));
    }
  }

  /**
   * Execute multiple function calls and return the result contents.
   *
   * @param calls The list of FunctionCallContent to execute.
   * @param functionMap A map from function names to Methods.
   * @return A list of FunctionResultContent with the results.
   */
  static ImmutableList<FunctionResultContent> executeFunctionCalls(
      List<FunctionCallContent> calls, ImmutableMap<String, Method> functionMap) {
    ImmutableList.Builder<FunctionResultContent> results = ImmutableList.builder();
    for (FunctionCallContent call : calls) {
      FunctionResultContent result = executeFunctionCall(call, functionMap);
      if (result != null) {
        results.add(result);
      }
    }
    return results.build();
  }

  /**
   * Invoke a method with the given arguments.
   *
   * <p>This is similar to AfcUtil.getFunctionResponse() - it handles type conversion for common
   * types.
   */
  private static Object invokeFunctionMethod(Method method, Map<String, Object> argsFromModel)
      throws Exception {
    List<Object> argsListFromModel = new java.util.ArrayList<>();
    ImmutableList<String> methodParameterNames =
        stream(method.getParameters()).map(Parameter::getName).collect(toImmutableList());

    for (String parameterName : methodParameterNames) {
      if (!argsFromModel.containsKey(parameterName)) {
        throw new IllegalArgumentException(
            "The parameter \""
                + parameterName
                + "\" was not found in the function call from model. Args in function call: "
                + argsFromModel);
      }
      Object argValueFromModel = argsFromModel.get(parameterName);
      String className = argValueFromModel.getClass().getName();

      if (className.equals("java.lang.String")) {
        argsListFromModel.add(argValueFromModel);
      } else if (className.equals("java.lang.Integer")) {
        argsListFromModel.add(Integer.parseInt(argValueFromModel.toString()));
      } else if (className.equals("java.lang.Double")) {
        argsListFromModel.add(Double.parseDouble(argValueFromModel.toString()));
      } else if (className.equals("java.lang.Float")) {
        argsListFromModel.add(Float.parseFloat(argValueFromModel.toString()));
      } else if (className.equals("java.lang.Boolean")) {
        argsListFromModel.add(Boolean.parseBoolean(argValueFromModel.toString()));
      } else if (argValueFromModel instanceof List) {
        argsListFromModel.add(argValueFromModel);
      } else {
        throw new IllegalArgumentException(
            "The value type of the parameter \""
                + parameterName
                + "\" is not supported. Supported types are String, Integer, Double, Float,"
                + " Boolean, and List.");
      }
    }

    return method.invoke(null, argsListFromModel.toArray());
  }

  /**
   * Convert a list of Content to include in input.
   *
   * @param contents The contents to convert.
   * @return The list of Content.
   */
  static ImmutableList<Content> toInputContents(
      List<? extends Content> contents) {
    return ImmutableList.copyOf(contents);
  }
}
