/*
 * Copyright 2019 etrace.io
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

package io.etrace.common.sharding.impl;

import io.etrace.common.sharding.ShardingFactory;
import io.etrace.common.sharding.ShardingStrategy;

public class HashingStrategy implements ShardingStrategy {

    private int size;

    @Override
    public void init(int size) {
        this.size = size;
    }

    @Override
    public int chooseTasks(Object key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(hashAgain(key.hashCode())) % size;
    }

    @Override
    public String name() {
        return ShardingFactory.ShardingType.HASH.name();
    }

    private int hashAgain(int h) {
        h += (h << 15) ^ 0xffffcd7d;
        h ^= (h >>> 10);
        h += (h << 3);
        h ^= (h >>> 6);
        h += (h << 2) + (h << 14);
        return h ^ (h >>> 16);
    }
}
