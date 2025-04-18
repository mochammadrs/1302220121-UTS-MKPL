package lib;

public class TaxFunction {

	/**
	 * Fungsi untuk menghitung jumlah pajak penghasilan pegawai yang harus
	 * dibayarkan setahun.
	 * 
	 * Pajak dihitung sebagai 5% dari penghasilan bersih tahunan (gaji dan pemasukan
	 * bulanan lainnya dikalikan jumlah bulan bekerja dikurangi pemotongan)
	 * dikurangi penghasilan tidak kena pajak.
	 * 
	 * Jika pegawai belum menikah dan belum punya anak maka penghasilan tidak kena
	 * pajaknya adalah Rp 54.000.000.
	 * Jika pegawai sudah menikah maka penghasilan tidak kena pajaknya ditambah
	 * sebesar Rp 4.500.000.
	 * Jika pegawai sudah memiliki anak maka penghasilan tidak kena pajaknya
	 * ditambah sebesar Rp 4.500.000 per anak sampai anak ketiga.
	 * 
	 */

	private static final int PTKP_SINGLE_BASE = 54000000;
	private static final int PTKP_MARRIAGE_BONUS = 4500000;
	private static final int PTKP_CHILD_BONUS_PER_CHILD = 4500000;
	private static final int PTKP_CHILD_MAX = 3;
	private static final double TAX_RATE = 0.05;
	private static final int MAX_MONTHS_PER_YEAR = 12;

	// Fungsi untuk menghitung jumlah pajak penghasilan pegawai yang harus
	// dibayarkan setahun.
	public static int calculateTax(int monthlySalary, int otherMonthlyIncome, int numberOfMonthWorking, int deductible,
			boolean isMarried, int numberOfChildren) {
		// 1. Validasi Input
		if (numberOfMonthWorking > MAX_MONTHS_PER_YEAR) {
			System.err.println("More than " + MAX_MONTHS_PER_YEAR + " month working per year");
		}

		// 2. Hitung PTKP (Penghasilan Tidak Kena Pajak)
		int nonTaxableIncome = calculateNonTaxableIncome(isMarried, numberOfChildren);

		// 3. Hitung Penghasilan (Gunakan long untuk mencegah overflow)
		long annualGrossIncome = (long) (monthlySalary + otherMonthlyIncome) * numberOfMonthWorking;

		// 4. Hitung Penghasilan Kena Pajak
		long taxableIncome = annualGrossIncome - deductible - nonTaxableIncome;

		// 5. Hitung Pajak (Hanya jika Penghasilan Kena Pajak > 0)
		int tax = 0;
		if (taxableIncome > 0) {
			tax = (int) Math.round(taxableIncome * TAX_RATE);
		}

		// 6. Pastikan pajak tidak negatif
		if (tax < 0) {
			return 0;
		} else {
			return tax;
		}
	}

	/**
	 * Helper method untuk menghitung Penghasilan Tidak Kena Pajak (PTKP) tahunan.
	 *
	 * @param isMarried        Status menikah pegawai.
	 * @param numberOfChildren Jumlah anak pegawai.
	 * @return Jumlah PTKP tahunan.
	 */
	private static int calculateNonTaxableIncome(boolean isMarried, int numberOfChildren) {
		int nonTaxableIncome = PTKP_SINGLE_BASE;

		if (isMarried) {
			nonTaxableIncome += PTKP_MARRIAGE_BONUS;
		}

		// Batasi jumlah anak yang dihitung maksimal 3
		int eligibleChildren = Math.min(numberOfChildren, PTKP_CHILD_MAX);
		nonTaxableIncome += (long) eligibleChildren * PTKP_CHILD_BONUS_PER_CHILD;

		return nonTaxableIncome;
	}
}
