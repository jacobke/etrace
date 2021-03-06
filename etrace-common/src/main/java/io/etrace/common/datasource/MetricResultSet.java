/*
 * Copyright 2020 etrace.io
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

package io.etrace.common.datasource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetricResultSet {

    private Object results;
    private String queryType;
    private Object functions;
    private String name;
    private String errorMsg;
    private List<TagFilter> tagFilters;

    public MetricResultSet(Object result, String queryType, Object functions, String name, List<TagFilter> tagFilters) {
        this.results = result;
        this.queryType = queryType;
        this.functions = functions;
        this.name = name;
        this.tagFilters = tagFilters;
    }
}
