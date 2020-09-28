package io.etrace.stream.aggregator.plugin;

import com.espertech.esper.client.hook.AggregationFunctionFactory;
import com.espertech.esper.epl.agg.aggregator.AggregationMethod;
import com.espertech.esper.epl.agg.service.common.AggregationValidationContext;
import io.etrace.stream.aggregator.annotation.AggregatorFunction;

import java.util.Map;

@AggregatorFunction(name = "hist")
public class HistogramAggregatorFactory implements AggregationFunctionFactory {
    @Override
    public void setFunctionName(String functionName) {

    }

    @Override
    public void validate(AggregationValidationContext validationContext) {

    }

    @Override
    public AggregationMethod newAggregator() {
        return new HistogramAggregator();
    }

    @Override
    public Class getValueType() {
        return Map.class;
    }
}
