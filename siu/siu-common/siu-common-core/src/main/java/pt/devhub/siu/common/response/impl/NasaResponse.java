package pt.devhub.siu.common.response.impl;

import lombok.Getter;
import pt.devhub.siu.common.response.IResponse;

/**
 * Object that contains the data that represents the NASA APOD API response.
 */
public class NasaResponse implements IResponse {

	/**
	 * The serial version unique identifier.
	 */
	private static final long serialVersionUID = 6265999069522152794L;

	/**
	 * NASA's picture of the day URL.
	 */
	@Getter
	private final String podUrl;

	/**
	 * NASA's picture of the day title.
	 */
	@Getter
	private final String title;

	/**
	 * NASA's picture of the day mediaType.
	 */
	@Getter
	private final String mediaType;

	/**
	 * NASA's picture of the day explanation.
	 */
	@Getter
	private final String explanation;

	/**
	 * Constructor for this class.
	 *
	 * @param builder
	 *            the builder class
	 */
	private NasaResponse(final NasaResponseBuilder builder) {
		this.podUrl = builder.podUrl;
		this.title = builder.title;
		this.mediaType = builder.mediaType;
		this.explanation = builder.explanation;
	}

	/**
	 * Builder for this class.
	 */
	public static final class NasaResponseBuilder {

		/**
		 * NASA's picture of the day URL.
		 */
		private String podUrl;
		/**
		 * NASA's picture of the day title.
		 */
		private String title;
		/**
		 * NASA's picture of the day media type.
		 */
		private String mediaType;
		/**
		 * NASA's picture of the day explanation.
		 */
		private String explanation;

		/**
		 * Default builder constructor.
		 *
		 * @param podUrl
		 *            NASA's picture of the day URL
		 */
		public NasaResponseBuilder(final String podUrl) {
			this.podUrl = podUrl;
		}

		/**
		 * Sets NASA's picture of the day title.
		 *
		 * @param title
		 *            NASA's picture of the day title
		 * @return this builder
		 */
		public NasaResponseBuilder setTitle(String title) {
			this.title = title;

			return this;
		}

		/**
		 * Sets NASA's picture of the day mediaType.
		 *
		 * @param mediaType
		 *            NASA's picture of the day mediaType
		 * @return this builder
		 */
		public NasaResponseBuilder setMediaType(String mediaType) {
			this.mediaType = mediaType;

			return this;
		}

		/**
		 * Sets NASA's picture of the day explanation.
		 *
		 * @param explanation
		 *            NASA's picture of the day explanation.
		 * @return this builder
		 */
		public NasaResponseBuilder setExplanation(String explanation) {
			this.explanation = explanation;

			return this;
		}

		/**
		 * Creates a new instance of the NasaResponse class in accordance with
		 * the builder pattern.
		 *
		 * @return a new instance of the NasaResponse class
		 */
		public NasaResponse build() {
			return new NasaResponse(this);
		}
	}
}
