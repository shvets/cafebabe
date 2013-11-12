// ArchivedDataSource.java

package org.sf.cafebabe.activation;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;

import javax.activation.DataSource;
import javax.activation.FileTypeMap;

/**
 * This class represents data source from the archive entry.
 *
 * @version 1.0 03/04/2002
 * @author Alexander Shvets
 */
public class ArchivedDataSource implements DataSource {
  private ZipFile zipFile;
  private ZipEntry zipEntry;

  /**
   * Creates new data source for the archive entry
   *
   * @param zipFile  the zip file
   * @param entryName  the entry name
   */
  public ArchivedDataSource(ZipFile zipFile, String entryName) {
    this.zipFile  = zipFile;
    this.zipEntry = zipFile.getEntry(entryName);
  }

  /**
   * Gets the source as an input stream
   *
   * @return the input stream
   * @exception IOException the I/O exception
   */
  public InputStream getInputStream() throws IOException {
    return zipFile.getInputStream(zipEntry);
  }

  /**
   * This source could not be represented as output stream, because
   * saving in archive is not supported.
   */
  public OutputStream getOutputStream() throws IOException {
    throw new IOException("This operation doesn't supported for an archive.");
  }

  /**
   * Gets te content type of the data. If the type cannot be determined,
   * the "application/octet-stream" is used.
   *
   * @return the MIME type of the data in the form of a string.
   */
  public String getContentType() {
    return FileTypeMap.getDefaultFileTypeMap().getContentType(getName());
  }

  /**
   * Gets the name of this source
   *
   * @return the name of this source
   */
  public String getName() {
    return zipFile.getName() + "#" + zipEntry.getName();
  }

}
