package application.utils;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;

/**
 * Provide general purpose methods for handling OpenCV-JavaFX data conversion.
 * Moreover, expose some "low level" methods for matching few JavaFX behavior.
 *
 * @author <a href="mailto:luigi.derussis@polito.it">Luigi De Russis</a>
 * @author <a href="http://max-z.de">Maximilian Zuleger</a>
 * @version 1.0 (2016-09-17)
 * @since 1.0
 * 
 */
public final class Utils
{
	
	/**
	 * Generic method for putting element running on a non-JavaFX thread on the
	 * JavaFX thread, to properly update the UI
	 * 
	 * @param property
	 *            a {@link ObjectProperty}
	 * @param value
	 *            the value to set for the given {@link ObjectProperty}
	 */
	public static <T> void onFXThread(final ObjectProperty<T> property, final T value)
	{
		Platform.runLater(() -> {
			property.set(value);
		});
	}
}
