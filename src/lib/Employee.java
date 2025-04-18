package lib;

import java.time.LocalDate;

public class Employee {

    // --- Data Inti Karyawan ---
    private String employeeId;
    private String firstName;
    private String lastName;
    private String idNumber;
    private String address;
    private LocalDate joinDate;
    private boolean isForeigner;
    private boolean gender; 

    private Compensation compensation;
    private DependentInfo dependentInfo;

    public Employee(String employeeId, String firstName, String lastName, String idNumber, String address,
                    LocalDate joinDate, boolean isForeigner, boolean gender /* Atau Enum Gender */) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
        this.address = address;
        this.joinDate = joinDate;
        this.isForeigner = isForeigner;
        this.gender = gender;

        // Inisialisasi objek komposisi
        this.compensation = new Compensation();
        this.dependentInfo = new DependentInfo();
    }

    /**
     * Menghitung dan mengatur gaji bulanan berdasarkan grade.
     * Mendelegasikan ke objek Compensation.
     * @param grade Tingkat grade karyawan.
     */
    public void setMonthlySalary(int grade) {
        // Perlu meneruskan status isForeigner ke method kalkulasi
        compensation.calculateAndSetMonthlySalary(grade, this.isForeigner);
    }

    public void setAnnualDeductible(int deductible) {
        compensation.setAnnualDeductible(deductible);
    }

    public void setAdditionalIncome(int income) {
        compensation.setOtherMonthlyIncome(income);
    }

    public void setSpouse(String spouseName, String spouseIdNumber) {
        dependentInfo.setSpouse(spouseName, spouseIdNumber);
    }

    public void addChild(String childName, String childIdNumber) {
        dependentInfo.addChild(childName, childIdNumber);
    }

    public Compensation getCompensation() {
        return compensation;
    }

    public DependentInfo getDependentInfo() {
        return dependentInfo;
    }

     public LocalDate getJoinDate() {
        return joinDate;
    }
     public boolean isForeigner() {
         return isForeigner;
     }
     public String getEmployeeId(){
         return employeeId;
     }

    public int getAnnualIncomeTax() {
        LocalDate currentDate = LocalDate.now();

        // Hitung bulan bekerja di tahun berjalan (TIDAK lagi menyimpan di field)
        int monthsWorked = calculateMonthsWorkedInYear(currentDate);

        // Delegasikan perhitungan ke TaxFunction dengan objek Employee ini
        try {
             // TaxFunction perlu disesuaikan untuk mengambil data via getCompensation() dan getDependentInfo()
             return TaxFunction.calculateTax(this, monthsWorked);
        } catch (IllegalArgumentException e) {
             System.err.println("Error calculating tax for employee " + this.employeeId + ": " + e.getMessage());
             return 0;
        }
    }

    // Refactored: Method ini sekarang MENGEMBALIKAN nilai, tidak set field instance
    private int calculateMonthsWorkedInYear(LocalDate currentDate) {
        int months;
        if (currentDate.getYear() == joinDate.getYear()) {
            months = currentDate.getMonthValue() - joinDate.getMonthValue();
            // Pastikan bulan tidak negatif
            months = Math.max(0, months);
            // Pertimbangkan +1 jika bulan bergabung dihitung penuh? Sesuai aturan bisnis.
        } else {
            months = 12;
        }
        return months;
    }
}