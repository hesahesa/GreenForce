package tekmob.anonim2;

public class ModelMasalahMini {
	protected String id;
	protected String nama;
	protected String lokasi;
	protected String pelapor;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the nama
	 */
	public String getNama() {
		return nama;
	}

	/**
	 * @param nama
	 *            the nama to set
	 */
	public void setNama(String nama) {
		this.nama = nama;
	}

	/**
	 * @return the lokasi
	 */
	public String getLokasi() {
		return lokasi;
	}

	/**
	 * @param lokasi
	 *            the lokasi to set
	 */
	public void setLokasi(String lokasi) {
		this.lokasi = lokasi;
	}

	/**
	 * @return the pelapor
	 */
	public String getPelapor() {
		return pelapor;
	}

	/**
	 * @param pelapor
	 *            the pelapor to set
	 */
	public void setPelapor(String pelapor) {
		this.pelapor = pelapor;
	}
}