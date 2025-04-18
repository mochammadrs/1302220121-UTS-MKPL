package lib;

public class Compensation {

    // Konstanta Gaji & Pengali 
    private static final int GRADE_1_SALARY = 3000000;
    private static final int GRADE_2_SALARY = 5000000;
    private static final int GRADE_3_SALARY = 7000000;
    private static final double FOREIGNER_SALARY_MULTIPLIER = 1.5;

    private int monthlySalary;
    private int otherMonthlyIncome;
    private int annualDeductible;

    // Konstruktor default (atau bisa terima nilai awal jika perlu)
    public Compensation() {
        // Gaji awal bisa 0 atau nilai default lain
        this.monthlySalary = 0;
        this.otherMonthlyIncome = 0;
        this.annualDeductible = 0;
    }

    public void calculateAndSetMonthlySalary(int grade, boolean isForeigner) {
        int baseSalary;
        switch (grade) {
            case 1:
                baseSalary = GRADE_1_SALARY;
                break;
            case 2:
                baseSalary = GRADE_2_SALARY;
                break;
            case 3:
                baseSalary = GRADE_3_SALARY;
                break;
            default:
                baseSalary = 0;
                break;
        }

        if (isForeigner) {
            this.monthlySalary = (int) (baseSalary * FOREIGNER_SALARY_MULTIPLIER);
        } else {
            this.monthlySalary = baseSalary;
        }
    }

    // --- SETTER & GETTER ---
    public void setAnnualDeductible(int deductible) {
        this.annualDeductible = deductible;
    }

    public void setOtherMonthlyIncome(int income) {
        this.otherMonthlyIncome = income;
    }

    public int getMonthlySalary() {
        return monthlySalary;
    }

    public int getOtherMonthlyIncome() {
        return otherMonthlyIncome;
    }

    public int getAnnualDeductible() {
        return annualDeductible;
    }
}