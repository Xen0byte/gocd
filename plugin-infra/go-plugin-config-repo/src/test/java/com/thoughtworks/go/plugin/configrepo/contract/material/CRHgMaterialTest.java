/*
 * Copyright Thoughtworks, Inc.
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
 */
package com.thoughtworks.go.plugin.configrepo.contract.material;

import com.google.gson.JsonObject;
import com.thoughtworks.go.plugin.configrepo.contract.AbstractCRTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CRHgMaterialTest extends AbstractCRTest<CRHgMaterial> {

    private final CRHgMaterial simpleHg;
    private final CRHgMaterial customHg;
    private final CRHgMaterial invalidHgNoUrl;
    private final CRHgMaterial invalidHgWhitelistAndIgnores;
    private final CRHgMaterial invalidPasswordAndEncryptedPasswordSet;

    public CRHgMaterialTest() {
        simpleHg = new CRHgMaterial();
        simpleHg.setUrl("myHgRepo");

        customHg = new CRHgMaterial("hgMaterial1", "dir1", false, false, null, List.of("externals", "tools"), "repos/myhg", "feature");

        invalidHgNoUrl = new CRHgMaterial();
        invalidHgWhitelistAndIgnores = new CRHgMaterial("hgMaterial1", "dir1", false, false, null, List.of("externals", "tools"), "repos/myhg", "feature");
        invalidHgWhitelistAndIgnores.setIncludesNoCheck("src", "tests");

        invalidPasswordAndEncryptedPasswordSet = new CRHgMaterial("hgMaterial1", "dir1", false, false, null, List.of("externals", "tools"), "repos/myhg", "feature");
        invalidPasswordAndEncryptedPasswordSet.setPassword("pa$sw0rd");
        invalidPasswordAndEncryptedPasswordSet.setEncryptedPassword("26t=$j64");
    }

    @Override
    public void addGoodExamples(Map<String, CRHgMaterial> examples) {
        examples.put("simpleHg", simpleHg);
        examples.put("customHg", customHg);
    }

    @Override
    public void addBadExamples(Map<String, CRHgMaterial> examples) {
        examples.put("invalidHgNoUrl", invalidHgNoUrl);
        examples.put("invalidHgWhitelistAndIgnores", invalidHgWhitelistAndIgnores);
        examples.put("invalidPasswordAndEncryptedPasswordSet", invalidPasswordAndEncryptedPasswordSet);
    }

    @Test
    public void shouldAppendTypeFieldWhenSerializingMaterials() {
        JsonObject jsonObject = (JsonObject) gson.toJsonTree(customHg);
        assertThat(jsonObject.get("type").getAsString()).isEqualTo(CRHgMaterial.TYPE_NAME);
    }

    @Test
    public void shouldHandlePolymorphismWhenDeserializing() {
        CRMaterial value = customHg;
        String json = gson.toJson(value);

        CRHgMaterial deserializedValue = (CRHgMaterial) gson.fromJson(json, CRMaterial.class);
        assertThat(deserializedValue).isEqualTo(value);
    }
}
