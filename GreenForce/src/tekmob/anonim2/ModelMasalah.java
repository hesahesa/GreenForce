package tekmob.anonim2;

public class ModelMasalah {
	private String id;
	private String nama;
	private String lokasi;
	private String pelapor;
	private String foto_full;//base64 dari image
	private String foto_small;//base64 dari image
	private String lat;
	private String lng;
	private String detail;
	private int voteVal;
	
	/**
	 * @return the lat
	 */
	public String getLat() {
		return lat;
	}

	/**
	 * @param lat
	 *            the lat to set
	 */
	public void setLat(String lat) {
		this.lat = lat;
	}

	/**
	 * @return the lng
	 */
	public String getLng() {
		return lng;
	}

	/**
	 * @param lng
	 *            the lng to set
	 */
	public void setLng(String lng) {
		this.lng = lng;
	}

	/**
	 * @return the detail
	 */
	public String getDetail() {
		return detail;
	}

	/**
	 * @param detail
	 *            the detail to set
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	/**
	 * @return the voteVal
	 */
	public int getVoteVal() {
		return voteVal;
	}

	/**
	 * @param voteVal
	 *            the voteVal to set
	 */
	public void setVoteVal(int voteVal) {
		this.voteVal = voteVal;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
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
	 * @param nama the nama to set
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
	 * @param lokasi the lokasi to set
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
	 * @param pelapor the pelapor to set
	 */
	public void setPelapor(String pelapor) {
		this.pelapor = pelapor;
	}

	/**
	 * @return the foto_full
	 */
	public String getFoto_full() {
		return foto_full;
	}

	/**
	 * @param foto_full the foto_full to set
	 */
	public void setFoto_full(String foto_full) {
		this.foto_full = foto_full;
	}

	/**
	 * @return the foto_small
	 */
	public String getFoto_small() {
		return foto_small;
	}

	/**
	 * @param foto_small the foto_small to set
	 */
	public void setFoto_small(String foto_small) {
		this.foto_small = foto_small;
	}
}
