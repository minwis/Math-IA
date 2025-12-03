public class MathIA {

    public static double verletLTE(double k, double m, double t, double dt) {
            double sum1 = 0.0;
            for (int j = 2; j <= 50; j++) {
                sum1 += Math.pow(-1, j) *
                        Math.pow(k * t * t, j) /
                        (Math.pow(m, j) * factorial(2 * j));
            }

            double sum2 = 0.0;
            for (int j = 3; j <= 50; j++) {
                sum2 += Math.pow(-1, j) *
                        Math.pow(k * (t - dt) * (t - dt), j) /
                        (Math.pow(m, j) * factorial(2 * j));
            }

            // inner sum inside the power
            double inner = 0.0;
            for (int j = 0; j <= 50; j++) {
                inner += Math.pow(-1, j) *
                        Math.pow(k * t * t, j) /
                        (Math.pow(m, j) * factorial(2 * j));
            }

            double sum3 = 0.0;
            for (int i = 2; i <= 50; i++) {
                sum3 += 2 * Math.pow(2 * inner, 2 * i) *
                        Math.pow(dt, 2 * i) /
                        factorial(2 * i);
            }

            return (2 + (k * dt) / m) * sum1 - 2 * sum2 + 2 * sum3;

    }

    public static double rk4LTE(double k, double m, double t) {
        double sum1 = 0.0;
        for (int n = 3; n <= 50; n++) {
            sum1 += Math.pow(-1, n) *
                    Math.pow(k * t * t, n) /
                    (Math.pow(m, n) * factorial(2 * n));
        }

        double sum2 = 0.0;
        double sqrtkm = Math.sqrt(k / m);

        for (int n = 2; n <= 50; n++) {
            sum2 += Math.pow(-1, n) *
                    Math.pow(k * t * t, n) /
                    (Math.pow(m, n) * factorial(2 * n + 1)) *
                    Math.pow(sqrtkm * t, 2 * n + 1);
        }

        return 16 * sum1 - 8 * sqrtkm * sum2;
    }

    public static double factorial(int n) {
        double result = 1.0;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public static void main(String[] args) {
        double verletGTE = 0.0;
        double rk4GTE = 0.0;
        double t = 0;
        double m = 4;
        double k = 100;
        double dt = 0.2;
        double increment = 0.1;
        int n = 0;
        while( n < 1000 ) {
            t = 0;
            verletGTE = 0;
            rk4GTE = 0;
            while ( verletGTE == rk4GTE ) {

                verletGTE += verletLTE(k, m, t, dt);
                rk4GTE += 2 * 4 * rk4LTE(k, m, t);
                t += dt;
            }
            System.out.println("dt:" + dt + " Is RK4's GTE greater than Verlet's GTE? " + (verletGTE < rk4GTE));
            /*
            if ( verletGTE < rk4GTE ) {
                System.out.println("dt when rk4GTE becomes bigger than verletGTE: " + dt + "t:" + t);
                break;
            }*/
            dt += increment;
            n++;
        }


    }
}