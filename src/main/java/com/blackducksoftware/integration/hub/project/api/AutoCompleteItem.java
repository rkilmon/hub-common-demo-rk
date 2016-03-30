package com.blackducksoftware.integration.hub.project.api;

public class AutoCompleteItem {

	private final String value;

	private final String uuid;

	public AutoCompleteItem(final String value, final String uuid) {
		this.value = value;
		this.uuid = uuid;
	}

	public String getValue() {
		return value;
	}

	public String getUuid() {
		return uuid;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("AutoCompleteItem [value=");
		builder.append(value);
		builder.append(", uuid=");
		builder.append(uuid);
		builder.append("]");
		return builder.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AutoCompleteItem)) {
			return false;
		}
		final AutoCompleteItem other = (AutoCompleteItem) obj;
		if (uuid == null) {
			if (other.uuid != null) {
				return false;
			}
		} else if (!uuid.equals(other.uuid)) {
			return false;
		}
		if (value == null) {
			if (other.value != null) {
				return false;
			}
		} else if (!value.equals(other.value)) {
			return false;
		}
		return true;
	}

}