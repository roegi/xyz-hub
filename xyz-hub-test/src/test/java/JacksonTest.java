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

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JacksonTest {

  @JsonTypeName(value = "Person")
  static class Person {
    Person() {}

    Person(Integer id, String name, Person... contacts) {
      this.id = id;
      this.name = name;
      this.contacts = Arrays.asList(contacts);
    }

    public Integer id;
    public String name;
    public List<Person> contacts;
  }

  static class PersonDeserializer extends StdDeserializer<JacksonTest.Person> {

    public PersonDeserializer() {
      super(JacksonTest.Person.class);
    }

    @Override
    public JacksonTest.Person deserialize(JsonParser jp, DeserializationContext deserializationContext)
        throws IOException, JsonProcessingException {

      Person result = null;

      if (jp.getCurrentToken() == JsonToken.START_OBJECT) {

        result = new Person();

        while (jp.getCurrentToken() != JsonToken.END_OBJECT) {
          switch (jp.nextFieldName()) {
            case "id": {
              result.id = jp.nextIntValue(0);
              break;
            }
            case "name": {
              result.name = jp.nextTextValue();
              break;
            }
            case "contacts": {
              if (jp.nextToken() != JsonToken.START_ARRAY) {
                throw new JsonParseException(jp, "Expected start array in field: " + jp.currentName());
              }

              result.contacts = new ArrayList<>();

              while (jp.nextToken() != JsonToken.END_ARRAY) {
                result.contacts.add(deserialize(jp, deserializationContext));
              }

              jp.nextToken();
              break;
            }
            default: {
              jp.nextToken();
              break;
            }
          }
        }
      }

      return result;
    }
  }

  public static void main(String... args) throws Exception {

    long time = System.currentTimeMillis();

    Person[] ppp = new Person[10000000];
    for (int i=0;i<ppp.length;) {
      ppp[i] = new Person(++i, "lucas_" + i);
    }

    time = time("after 10M objects", time);

    Person me = new Person(
        0,
        "lucas",
        ppp
    );

    time = time("after Person", time);

    String json = serialize(me);

    time = time("after serialize", time);

    ObjectMapper mapper = new ObjectMapper();
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Person.class, new PersonDeserializer());

    mapper.registerModule(module);

    Person p = mapper.readValue(json, Person.class);

    time = time("after PersonDeserialize", time);

    String resultingJson = serialize(p);

    time = time("after serialize from PersonDeserialize", time);

    System.out.println("resulting json size: " + resultingJson.length());
    //System.out.println(resultingJson);

    p = new ObjectMapper().readValue(json, Person.class);

    time = time("after ObjectMapper", time);

    resultingJson = serialize(p);

    time = time("after serialize from ObjectMapper", time);

    System.out.println("resulting json size: " + resultingJson.length());
  }

  private static long time(String prefix, long time) {
    long currTime = System.currentTimeMillis();
    System.out.println(prefix + ": " + (currTime - time));
    return currTime;
  }

  private static String serialize(Object p) throws IOException {
    StringWriter w = new StringWriter();
    ObjectCodec c = new ObjectMapper();

    com.fasterxml.jackson.core.JsonFactory f = new JsonFactory();
    JsonGenerator g = f.setCodec(c).createGenerator(w);

    g.writeObject(p);

    return w.toString();
  }
}
