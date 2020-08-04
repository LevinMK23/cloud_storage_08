package IO;

public class Test {
    public static void main(String[] args) {
        System.out.println(CurrencyEnum.RUB);
        System.out.println(CurrencyEnum.values());
        System.out.println(CurrencyEnum.RUB == CurrencyEnum.getByOrder(0));
        System.out.println(CurrencyEnum.getByName("LOL"));
        System.out.println(CurrencyEnum.getByOrder(12));
    }
}
