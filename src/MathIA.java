public class MathIA {
    static double k = 10;
    static double m = 0.1;

    static double t = 0;
    static double dt = (double) 1/60;

    static double xn = 10.0;
    static double vn = 0.0;

    static double machine_epsilon = Math.pow(2,-52);
    static int term1max = 0;
    static int term2max = 0;

    static double u;
    static double lte;

    private static void RungeKutta() {
        double k1v = vn;
        double k1a = (-k / m) * xn;

        double k2v = vn + k1a * (dt / 2.0);
        double k2a = (-k / m) * (xn + k1v * (dt / 2.0));

        double k3v = vn + k2a * (dt / 2.0);
        double k3a = (-k / m) * (xn + k2v * (dt / 2.0));

        double k4v = vn + k3a * dt;
        double k4a = (-k / m) * (xn + k3v * dt);

        xn = xn + (dt / 6.0) * (k1v + 2*k2v + 2*k3v + k4v);
        vn = vn + (dt / 6.0) * (k1a + 2*k2a + 2*k3a + k4a);
    }

    private static double calculateRoundingError() {
        return t * machine_epsilon * xn / dt;
    }

    public static double calculateLTE() {
        double sum1 = 0;
        double sum2 = 0;
        double sqrtKM = Math.sqrt(k / m);

        for (int j = 3; j <= term1max; j++) {
            double term1 = (xn * Math.pow(-1, j) / factorial(2 * j))
                    * Math.pow(sqrtKM, 2 * j)
                    * Math.pow(dt, 2 * j);
            sum1 += term1;
        }
        for ( int j = 2; j < term2max; j++ ) {
            double term2 = (vn * Math.pow(-1, j) / factorial(2 * j + 1))
                    * Math.pow(sqrtKM, 2 * j)
                    * Math.pow(dt, 2 * (j+1));
            sum2 += term2;
        }

        return t * (sum1 + sum2);
    }

    private static double factorial(int n) {
        double res = 1;
        for (int i = 2; i <= n; i++) res *= i;
        return res;
    }

    private static int calculateTerm1MaxJ() {
        int j = 3;
        double KM = k / m;
        double term1;

        do {

            term1 = (xn * Math.pow(-1, j) / factorial(2 * j))
                    * Math.pow(KM, j)
                    * Math.pow(dt, 2 * j - 1);
            j++;
        }
        while ( Math.abs(term1) > machine_epsilon && j < 10000 );

        return j;
    }

    private static int calculateTerm2MaxJ() {
        int j = 2;
        double KM = k / m;
        double term2 = 0;

        do {
            term2 = (vn * Math.pow(-1, j) / factorial(2 * j + 1))
                    * Math.pow(KM, j)
                    * Math.pow(dt, 2 * j);
            j++;
        }
        while ( Math.abs(term2) > machine_epsilon && j < 10000 );

        return j;
    }

    private static void findTime() {
        do {
            t += dt;
            term1max = calculateTerm1MaxJ();
            term2max = calculateTerm2MaxJ();
            RungeKutta();
            u = calculateRoundingError();
            lte = calculateLTE();
        } while ( lte < u );
    }

    public static void main(String[] args) {
        //changing dt
        while( dt <= 0.1 ) {
            findTime();
            System.out.println(dt);
            dt += 0.015625;
        }
        //"dt: " + dt

        //changing k
        /*while( k <= 1000 ) {
            findTime();
            System.out.println("k: " + k + ", t: " + t);
            k += 10;
        }*/

        //changing dt
        /*while( m <= 360 ) {
            findTime();
            System.out.println("m: " + m + ", t: " + t);
            m += 0.5;
        }*/
    }


}