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

package io.etrace.common.message.metric.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import io.etrace.common.message.metric.MetricInTraceApi;
import io.etrace.common.message.metric.MetricManager;
import io.etrace.common.message.metric.field.MetricKey;
import io.etrace.common.util.MessageHelper;
import io.etrace.common.util.SimpleArrayMap;

import java.io.IOException;
import java.util.Map;

public abstract class AbstractMetric<M> implements MetricInTraceApi<M> {

    protected MetricManager manager;
    protected long timestamp;
    protected boolean completed;
    protected Map<String, String> tags;
    private MetricKey key;
    private MetricKey tagKey;
    private String topic;
    private String name;

    protected AbstractMetric() {
    }

    public AbstractMetric(MetricManager manager, String name) {
        this.manager = manager;
        this.name = MessageHelper.truncate(name, 512);
        key = new MetricKey();
        key.add(getMetricType().toIdentifier());
        timestamp = System.currentTimeMillis();
    }

    protected void build(AbstractMetric metric) {
        this.key = metric.key;
        this.tagKey = metric.tagKey;
        this.topic = metric.topic;
        this.name = metric.name;
        this.timestamp = metric.timestamp;
        this.tags = metric.tags;
    }

    @Override
    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    protected void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public MetricKey getKey() {
        return key;
    }

    @Override
    public MetricKey getTagKey() {
        return tagKey;
    }

    public String getTopic() {
        return topic == null ? "" : topic;
    }

    public M setTopic(String topic) {
        if (completed) {
            return (M)this;
        }
        this.topic = topic;
        return (M)this;
    }

    @Override
    public M addTag(String key, String value) {
        if (completed) {
            return (M)this;
        }
        try {
            if (manager != null) {
                int tagCount = this.manager.getConfigManager().getMetricConfig().getTagCount();
                if (tags == null) {
                    tags = new SimpleArrayMap<>(tagCount);
                }
                tags.put(key, value);
            } else {
                if (tags == null) {
                    tags = new SimpleArrayMap<>(8);
                }
                tags.put(key, value);
            }
        } catch (Exception ignore) {
        }
        if (this.tagKey == null) {
            this.tagKey = new MetricKey();
        }
        this.tagKey.add(key);
        this.tagKey.add(value);
        return (M)this;
    }

    @Override
    public void write(JsonGenerator generator) throws IOException {
        generator.writeString(getMetricType().toIdentifier());
        generator.writeString(name);
        generator.writeNumber(timestamp);
        if (tags == null || tags.isEmpty()) {
            generator.writeNull();
        } else {
            generator.writeStartObject();
            for (Map.Entry<String, String> tag : tags.entrySet()) {
                String key = tag.getKey();
                String value = tag.getValue();
                // move truncate behavior out from client thread
                if (manager != null) {
                    key = MessageHelper.truncate(key, 64);
                    value = MessageHelper.truncate(value,
                        this.manager.getConfigManager().getMetricConfig().getTagSize());
                }
                generator.writeStringField(key, value);
            }
            generator.writeEndObject();
        }
    }

    protected boolean tryCompleted() {
        if (completed) {
            return false;
        }
        completed = true;
        //        setTimestamp(System.currentTimeMillis());
        //the time to aggregator metric, default 1s
        int aggregatorTime = 0;

        if (manager != null && manager.getConfigManager() != null
            && manager.getConfigManager().getMetricConfig() != null) {
            aggregatorTime = manager.getConfigManager().getMetricConfig().getAggregatorTime();
        }
        if (aggregatorTime < 1000) {
            aggregatorTime = 1000;
        }
        key.add(String.valueOf(timestamp / aggregatorTime));
        key.add(topic);
        return true;
    }
}
