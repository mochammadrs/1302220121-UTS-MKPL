package lib;

public class Compensation {

    private static final int GRADE_1_SALARY = 3000000;
    private static final int GRADE_2_SALARY = 5000000;
    private static final int GRADE_3_SALARY = 7000000;
    private static final double FOREIGNER_SALARY_MULTIPLIER = 1.5;

    private int monthlySalary;
    private int otherMonthlyIncome;
    private int annualDeductible;

    public Compensation() {
        this.monthlySalary = 0;
        this.otherMonthlyIncome = 0;
        this.annualDeductible = 0;
    }

    // Refactored: Parameter menggunakan Enum EmployeeGrade
    public void calculateAndSetMonthlySalary(EmployeeGrade grade, boolean isForeigner) {
        int baseSalary;

        // Refactored: Switch menggunakan Enum
        switch (grade) {
            case GRADE_1:
                baseSalary = GRADE_1_SALARY;
                break;
            case GRADE_2:
                baseSalary = GRADE_2_SALARY;
                break;
            case GRADE_3:
                baseSalary = GRADE_3_SALARY;
                break;
            default:
                // Dengan enum, idealnya semua case tercakup.
                // Jika grade bisa null, perlu penanganan NullPointerException.
                // Atau jika ada kemungkinan grade tidak valid:
                throw new IllegalArgumentException("Unsupported EmployeeGrade: " + grade);
        }

        if (isForeigner) {
            this.monthlySalary = (int) (baseSalary * FOREIGNER_SALARY_MULTIPLIER);
        } else {
            this.monthlySalary = baseSalary;
        }
    }

    public void setAnnualDeductible(int deductible) { this.annualDeductible = deductible; }
    public void setOtherMonthlyIncome(int income) { this.otherMonthlyIncome = income; }
    public int getMonthlySalary() { return monthlySalary; }
    public int getOtherMonthlyIncome() { return otherMonthlyIncome; }
    public int getAnnualDeductible() { return annualDeductible; }
}