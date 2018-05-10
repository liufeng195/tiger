// Copyright 2016 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
////////////////////////////////////////////////////////////////////////////////
package sample;

import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import javax.inject.Named;

public class PseudoApplication {
  @Inject
  Baz baz;

  @Inject
  Tank tank;

  @Inject
  @Named("Russia")
  Set<Tank> russiaTanks;

  @Inject
  Set<String> strings;

  @Inject
  Map<String, Planet> planets;

  @Override
  public String toString() {
    return "PseudoApplication[baz: "
        + baz
        + " tank: "
        + tank
        + " Russia tansk: "
        + russiaTanks
        + " strings: "
        + strings
        + " planets: "
        + planets
        + "]";
  }
}
