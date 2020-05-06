
public class NitUtils {

	private String nit;

	public NitUtils(String nit) {
		this.nit = nit;
	}

	public boolean valid() {
		return validNotNullAndNotEmpty() && validLength() && validNotLetters() && validDigitVerified();
	}

	public boolean validNotNullAndNotEmpty() {
		return !checkIfNullOrEmpty();
	}

	public boolean validLength() {
		return validNotNullAndNotEmpty() ? (checkContainsDashes() ? this.nit.length() == 17 : this.nit.length() == 14)
				: false;
	}

	public boolean validNotLetters() {
		return validNotNullAndNotEmpty() && this.nit.matches("[0-9\\s\\-]+");
	}

	public boolean validDigitVerified() {

		if (!validNotNullAndNotEmpty() || !validLength() || !validNotLetters()) {
			return false;
		}

		String nitUnmask = this.nit;

		if (checkContainsDashes()) {
			nitUnmask = getNitUnMasked();
		}

		Long subNit = Long.valueOf(nitUnmask.substring(10, 13));
		double suma = 0;
		double valida = -1;
		if (subNit < 100) {
			// rutina vieja
			int n = 1;
			for (int i = 0; i <= 12; i++) {
				int x = Integer.valueOf(nitUnmask.substring((i), i + 1));
				suma = suma + (x * (15 - n));
				n++;
			}

			valida = (suma % 11);

			if (valida == 10) {
				valida = 0;
			}

		} else {
			// rutina nueva
			int n = 1;
			for (int i = 0; i <= 12; i++) {
				double factor = (3 + (6 * Math.floor(Math.abs(((n) + 4) / 6)))) - (n);
				int x = Integer.valueOf(nitUnmask.substring((i), i + 1));
				suma = suma + (x * factor);
				n++;
			}

			double mod = (suma % 11);
			if (mod > 1) {
				valida = 11 - mod;
			} else {
				valida = 0;
			}

		}

		int lastDigit = Integer.valueOf(nitUnmask.substring(13, 14));

		return valida == lastDigit;
	}

	public String getNitMasked() {
		return valid()
				? (getDepartamento() + getMunicipio() + "-" + getBirthDate() + "-" + getCorrelativo() + "-"
						+ getDigitVerified())
				: null;
	}

	public String getNitUnMasked() {
		if (!validNotNullAndNotEmpty() || !validLength() || !validNotLetters()) {
			return null;
		}
		return this.nit.replaceAll("-", "");
	}

	public String getDepartamento() {
		return valid() ? getNitUnMasked().substring(0, 2) : null;
	}

	public String getMunicipio() {
		return valid() ? getNitUnMasked().substring(2, 4) : null;
	}

	public String getBirthDate() {
		return valid() ? getNitUnMasked().substring(4, 10) : null;
	}

	public String getCorrelativo() {
		return valid() ? getNitUnMasked().substring(10, 13) : null;
	}

	public String getDigitVerified() {
		return valid() ? getNitUnMasked().substring(13, 14) : null;
	}

	private boolean checkIfNullOrEmpty() {
		return this.nit == null || this.nit.trim().length() == 0;
	}

	private boolean checkContainsDashes() {
		return this.nit.contains("-");
	}
}
