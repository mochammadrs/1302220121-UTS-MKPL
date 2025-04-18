package lib;

import java.time.LocalDate;
// import java.time.Month; //Tidak lagi dibutuhkan secara langsung untuk field tanggal masuk
import java.util.LinkedList;
import java.util.List;

public class Employee {

	private String employeeId;
	private String firstName;
	private String lastName;
	private String idNumber;
	private String address;

	// Refactoring(PrimitiveObsession): Mengganti tanggal primitif dengan LocalDate
	private LocalDate joinDate;
	// private int yearJoined; // Dihapus
	// private int monthJoined; // Dihapus
	// private int dayJoined; // Dihapus

	private int monthWorkingInYear; // Disimpan sebagai field hasil perhitungan keperluan pajak

	private boolean isForeigner;
	private boolean gender; // true = Laki-laki, false = Perempuan

	private int monthlySalary;
	private int otherMonthlyIncome;
	private int annualDeductible;

	// Refactoring(DataClumps): Menggunakan objek Spouse
	private Spouse spouse; // Bisa null jika tidak punya pasangan
	// private String spouseName; // Dihapus
	// private String spouseIdNumber; // Dihapus

	// Refactoring: Menggunakan List<child>
	private List<Child> children; // Bisa kosong jika tidak punya anak
	// private List<String> childNames; //Dihapus
	// private List<String> childIdNumbers; // Dihapus

	// Refactoring: Konstruktor sekarang menerima LocalDate
	public Employee(String employeeId, String firstName, String lastName, String idNumber, String address,
			LocalDate joinDate, boolean isForeigner, boolean gender) {
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.idNumber = idNumber;
		this.address = address;
		// Atur field LocalDate
		this.joinDate = joinDate;
		this.isForeigner = isForeigner;
		this.gender = gender;

		// Inisialisasi list children
		children = new LinkedList<Child>();
		// spouse dibiarkan null secara default
	}

	/**
	 * Fungsi untuk menentukan gaji bulanan pegawai berdasarkan grade kepegawaiannya
	 * (grade 1: 3.000.000 per bulan, grade 2: 5.000.000 per bulan, grade 3:
	 * 7.000.000 per bulan)
	 * Jika pegawai adalah warga negara asing gaji bulanan diperbesar sebanyak 50%
	 */

	public void setMonthlySalary(int grade) {
		int baseSalary;
		switch (grade) {
			case 1:
				baseSalary = 3000000;
				break;
			case 2:
				baseSalary = 5000000;
				break;
			case 3:
				baseSalary = 7000000;
				break;
			default:
				// Pertimbangkan melempar IllegalArgumentException untuk grade yang tidak valid
				baseSalary = 0; // Atau tangani error dengan cara yang sesuai
				break;
		}

		// PERBAIKAN BUG: Terapkan pengali 1.5x dengan benar ke gaji pokok berdasarkan
		// grade
		if (isForeigner) {
			monthlySalary = (int) (baseSalary * 1.5);
		} else {
			monthlySalary = baseSalary;
		}
	}

	public void setAnnualDeductible(int deductible) {
		this.annualDeductible = deductible;
	}

	public void setAdditionalIncome(int income) {
		this.otherMonthlyIncome = income;
	}

	// --- Method Tanggungan (Refactored) ---

    /**
     * Sets the spouse information for the employee.
     * Creates a new Spouse object internally.
     * @param spouseName Spouse's full name.
     * @param spouseIdNumber Spouse's ID number.
     */
    public void setSpouse(String spouseName, String spouseIdNumber) {
        // Buat objek Spouse dari parameter
        this.spouse = new Spouse(spouseName, spouseIdNumber);
    }

	/**
     * Adds a child to the employee's list of children.
     * Creates a new Child object internally.
     * @param childName Child's full name.
     * @param childIdNumber Child's ID number.
     */
    public void addChild(String childName, String childIdNumber) {
        // Buat objek Child dari parameter dan tambahkan ke list
        children.add(new Child(childName, childIdNumber));
    }

	// Getter untuk field joinDate hasil refactoring
	public LocalDate getJoinDate() {
		return joinDate;
	}

	public Spouse getSpouse(){
		return spouse;
	}

	public List<Child> getChildren() {
		return children;
	}

	public int getAnnualIncomeTax() {
		LocalDate currenDate = LocalDate.now();

		// Hitung bulan bekerja di tahun berjalan menggunakan field joinDate
		calculateMonthWorkedInYear(currenDate);

		// Peningkatan ketahanan: Periksa apakah spouseIdNumber null atau kosong
		boolean hasSpouse = this.spouse != null && this.spouse.idNumber() != null && !this.spouse.idNumber().isEmpty();

		// Delegasikan perhitungan pajak ke TaxFunction
		// Kirim 'true' ke parameter isMarried jika TIDAK ada ID pasangan (sesuai logika
		// awal, dimana isMarried = spouseIdNumber.equals(""))
		// Nama parameter di taxFunction tampaknya 'isMarried'
		return TaxFunction.calculateTax(monthlySalary, otherMonthlyIncome, monthWorkingInYear, annualDeductible,
				!hasSpouse, children.size()// menggunakan ukuran list child
		);
	}

	/**
	 * Method pembantu untuk menghitung jumlah bulan penuh bekerja dalam tahun pajak
	 * berjalan.
	 * Catatan: Logika asli (bulanSekarang - bulanMasuk) mungkin perlu ditinjau
	 * berdasarkan peraturan pajak spesifik
	 * (misalnya, apakah bulan masuk dihitung penuh? Apakah dihitung pro-rate?).
	 * Refactoring ini mempertahankan metode perhitungan asli tetapi menggunakan
	 * localDate.
	 * 
	 * @param currentDate Tanggal saat ini, digunakan untuk menentukan tahun dan
	 *                    bulan saat ini.
	 */

	private void calculateMonthWorkedInYear(LocalDate currentDate) {
		// Menghitung berapa lama pegawai bekerja dalam setahun ini, jika pegawai sudah
		// bekerja dari tahun sebelumnya maka otomatis dianggap 12 bulan.
		if (currentDate.getYear() == joinDate.getYear()) {
			// Gunakan MonthValue dari LocalDate
			monthWorkingInYear = currentDate.getMonthValue() - joinDate.getMonthValue();
			// Pastikan bulan bekerja tidak negatif (misalnya jika logika berubah atau
			// tanggal masuk ada di masa depan)
			if (monthWorkingInYear < 0) {
				monthWorkingInYear = 0;
			}
			// Pertimbangkan apakah bulan masuk itu sendiri harus dihitung sebagai 1 bulan?
			// Jika ya: monthWorkingInYear = currentDate.getMonthValue() -
			// joinDate.getMonthValue() + 1;
		} else {
			monthWorkingInYear = 12; // Bekerja penuh tahun berjalan atau bergabung di tahun sebelumnya
		}
	}
}