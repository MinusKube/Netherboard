package fr.minuskube.netherboard.text;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("rawtypes")
public abstract class AbstractTextAnimation implements TextAnimation {

    protected int interval;

    protected Map<Property, Object> defaultProperties;
    protected Map<Property, Object> properties;

    protected AbstractTextAnimation(int defaultInterval, ImmutableMap<Property, Object> defaultProperties) {
        this.interval = defaultInterval;
        this.defaultProperties = defaultProperties;
        this.properties = new HashMap<>();
    }

    @Override
    public int getInterval() { return interval; }

    @Override
    public void setInterval(int interval) { this.interval = interval; }

    @SuppressWarnings("unchecked")
    @Override
    public <E> E getProperty(Property<?, E> property) {
        if(properties.containsKey(property)) {
            return (E) properties.get(property);
        }

        if(defaultProperties.containsKey(property)) {
            return (E) defaultProperties.get(property);
        }

        throw new IllegalArgumentException("The property " + property.getClass().getSimpleName()
                + " is not valid for the class " + getClass().getSimpleName() + ".");
    }

    @Override
    public <E> void setProperty(Property<?, E> property, E value) {
        if(!defaultProperties.containsKey(property)) {
            throw new IllegalArgumentException("The property " + property.getClass().getSimpleName()
                    + " is not valid for the class " + getClass().getSimpleName() + ".");
        }

        properties.put(property, value);
    }

}
