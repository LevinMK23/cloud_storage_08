package IO;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CurrencyEnum {

    private final static CurrencyEnum instance = new CurrencyEnum();

    private CurrencyEnum() {
    }

    private static class Currency {
        String name;
        int ord;
        static int counter = 0;
        static List<Currency> values = new ArrayList<>();
        private Currency(String name) {
            ord = counter++;
            this.name = name;
            values.add(this);
        }

        @Override
        public String toString() {
            return "Currency{" +
                    "name='" + name + '\'' +
                    ", ord=" + ord +
                    '}';
        }
    }

    static {
        Class<?> c = CurrencyEnum.class;
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(Currency.class)) {
                Currency currency = new Currency(field.getName());
                try {
                    field.set(instance, currency);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Currency RUB;
    public static Currency USD;
    public static Currency EUR;

    public static List<Currency> values() {
        return Collections.unmodifiableList(Currency.values);
    }

    public static Currency getByOrder(int order) {
        return values().get(order);
    }

    public static Currency getByName(String name) {
        for (Currency currency : values()){
            if (currency.name.equals(name)) {
                return currency;
            }
        }
        throw new RuntimeException("Currency with name = " + name + " not found!");
    }

}
