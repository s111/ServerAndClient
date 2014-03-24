package metadata;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.junit.Test;

public class ExifWriterTest {
	@Test
	public void create_exif_writer_expect_method_call_getOutputSet()
			throws ImageReadException, ImageWriteException, IOException {
		TiffImageMetadata exifMock = mock(TiffImageMetadata.class);

		File fileMock = mock(File.class);
		when(fileMock.getName()).thenReturn("image.jpg");

		new ExifWriter(fileMock, exifMock).cleanUp();

		verify(exifMock).getOutputSet();
	}
}
