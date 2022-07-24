package java.task2;

public abstract class Factorial {
    protected int factorial(int n) {
       /* if (n < 0) throw new RuntimeException("negative number"); //рекурсия не работает с дочерним методом (додумать)
        else if (n == 1 || n == 0) return 1;
        return n * factorial(n - 1);*/
        if (n < 0) throw new RuntimeException("negative number");
        int result = 1;
        for (int i = 1; i <= n; i++)
            result = result * i;
        return result;
    }
}

class ChildFactorial extends Factorial {
    public static void main(String[] args) {
        System.out.println(new ChildFactorial().factorial(2));
    }

    @Override
    protected int factorial(int n) {
        return super.factorial(10);
    }
}

class TwoFactorial {
    public static void main(String[] args) {
        System.out.println(factorial(14));
    }
    public static int factorial(int n) {
        if (n < 0) throw new RuntimeException("negative number");
        else if (n == 1 || n == 0) return 1;
        return n * factorial(n - 1);
    }
}