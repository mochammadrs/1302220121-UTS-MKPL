package lib;

public class TaxFunction {

    private static final int PTKP_SINGLE_BASE = 54000000;
    private static final int PTKP_MARRIAGE_BONUS = 4500000;
    private static final int PTKP_CHILD_BONUS_PER_CHILD = 4500000;
    private static final int PTKP_CHILD_MAX = 3;
    private static final double TAX_RATE = 0.05;
    private static final int MAX_MONTHS_PER_YEAR = 12;


    public static int calculateTax(Employee employee, int numberOfMonthWorking) {

        if (numberOfMonthWorking > MAX_MONTHS_PER_YEAR) {
            throw new IllegalArgumentException("Number of working months (" + numberOfMonthWorking + ") cannot exceed " + MAX_MONTHS_PER_YEAR);
        }
        if (employee == null) {
             throw new IllegalArgumentException("Employee object cannot be null");
        }
        // Pastikan objek komposisi tidak null 
        if (employee.getCompensation() == null || employee.getDependentInfo() == null) {
             throw new IllegalStateException("Employee object is not fully initialized (compensation or dependentInfo is null)");
        }


        // 2. Ambil data dari objek komposisi Employee
        Compensation comp = employee.getCompensation();
        DependentInfo depInfo = employee.getDependentInfo();

        int monthlySalary = comp.getMonthlySalary();
        int otherMonthlyIncome = comp.getOtherMonthlyIncome();
        int deductible = comp.getAnnualDeductible();
        boolean isMarried = depInfo.getSpouse() != null;
        int numberOfChildren = depInfo.getChildren().size();

        // 3. Hitung PTKP 
        int nonTaxableIncome = calculateNonTaxableIncome(isMarried, numberOfChildren);

        // 4. Hitung Penghasilan/ Tahunan 
        long annualGrossIncome = (long) (monthlySalary + otherMonthlyIncome) * numberOfMonthWorking;

        // 5. Hitung Penghasilan Kena Pajak
        long taxableIncome = annualGrossIncome - deductible - nonTaxableIncome;

        // 6. Hitung Pajak
        int tax = 0;
        if (taxableIncome > 0) {
            tax = (int) Math.round(taxableIncome * TAX_RATE);
        }

        // 7. Kembalikan hasil
        return tax;
    }

    private static int calculateNonTaxableIncome(boolean isMarried, int numberOfChildren) {
        int nonTaxableIncome = PTKP_SINGLE_BASE;
        if (isMarried) {
            nonTaxableIncome += PTKP_MARRIAGE_BONUS;
        }
        int eligibleChildren = Math.min(numberOfChildren, PTKP_CHILD_MAX);
        nonTaxableIncome += (long) eligibleChildren * PTKP_CHILD_BONUS_PER_CHILD;
        return nonTaxableIncome;
    }
}