package fr.minuskube.netherboard.api.text;

import java.util.HashMap;
import java.util.Map;

public interface TextAnimation {

    class Property<T extends TextAnimation, E> {}

    int getInterval();
    void setInterval(int interval);

    <E> E getProperty(Property<?, E> property);
    <E> void setProperty(Property<?, E> property, E value);

    String getCurrentText();
    void update();

    static <T extends TextAnimation> Builder<T> builder(Class<T> clazz) {
        return new Builder<>(clazz);
    }

    class Builder<T extends TextAnimation> {

        private Class<T> clazz;
        private Map<TextAnimation.Property, Object> properties = new HashMap<>();
        private int interval = 0;

        public Builder(Class<T> clazz) {
            this.clazz = clazz;
        }

        public Builder<T> interval(int interval) {
            this.interval = interval;
            return this;
        }

        public <E> Builder<T> property(TextAnimation.Property<T, E> property, E value) {
            properties.put(property, value);
            return this;
        }

        @SuppressWarnings("unchecked")
        public T build() {
            try {
                T t = clazz.newInstance();
                t.setInterval(interval);

                for(Map.Entry<TextAnimation.Property, Object> entry : properties.entrySet())
                    t.setProperty(entry.getKey(), entry.getValue());

                return t;
            } catch(InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException("The given TextAnimation class could not be instantiated.", e);
            }
        }

    }

}