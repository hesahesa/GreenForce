package tekmob.anonim2;

public class ModelMasalah extends ModelMasalahMini {
	protected String gambar;//base64 dari image
	protected String lat;
	protected String lng;
	protected String detail;
	protected int voteVal;
	
	public ModelMasalah(String id, String nama, String lokasi, String gambar, String lat, String lng, String detail, int voteVal, String pelapor){
		this.id = id;
		this.nama = nama;
		this.lokasi = lokasi;
		this.gambar = gambar;
		this.lat = lat;
		this.lng = lng;
		this.detail = detail;
		this.voteVal = voteVal;
		this.pelapor = pelapor;
	}

	/**
	 * @return the gambar
	 */
	public String getGambar() {
		return gambar;
	}

	/**
	 * @param gambar
	 *            the gambar to set
	 */
	public void setGambar(String gambar) {
		this.gambar = gambar;
	}

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
}
