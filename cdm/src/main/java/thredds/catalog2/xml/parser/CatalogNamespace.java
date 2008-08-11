package thredds.catalog2.xml.parser;

import thredds.util.HttpUriResolver;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * _more_
 *
 * @author edavis
 * @since 4.0
 */
public enum CatalogNamespace
{
  CATALOG_1_0( "http://www.unidata.ucar.edu/namespaces/thredds/InvCatalog/v1.0",
               "/resources/thredds/schemas/InvCatalog.1.0.2.xsd",
               "http://www.unidata.ucar.edu/schemas/thredds/InvCatalog.1.0.2.xsd"),
  CATALOG_0_6( "http://www.unidata.ucar.edu/thredds",
               "/resources/thredds/schemas/InvCatalog.0.6.xsd",
               "http://www.unidata.ucar.edu/schemas/thredds/InvCatalog.0.6.xsd"),
  XLINK( "http://www.w3.org/1999/xlink",
         "/resources/thredds/schemas/xlink.xsd",
         // "/resources/schemas/xlink/1.0.0/xlinks.xsd", // OGC version
         "");

  private String namespaceUri;
  private String resourceName;
  private URI resourceUri;

  CatalogNamespace( String namespaceUri, String resourceName, String resourceUri )
  {
    this.namespaceUri = namespaceUri;
    this.resourceName = resourceName;
    try
    {
      this.resourceUri = new URI( resourceUri);
    }
    catch ( URISyntaxException e )
    {
      throw new IllegalArgumentException( "Badly formed resource URI [" + resourceUri + "].", e);
    }
  }

  public String getNamespaceUri()
  {
    return this.namespaceUri;
  }

  public String getResourceName()
  {
    return this.resourceName;
  }

  public URI getResourceUri()
  {
    return this.resourceUri;
  }

  public static CatalogNamespace getNamespace( String namespaceUri )
  {
    if ( namespaceUri == null ) return null;
    for ( CatalogNamespace curNs : CatalogNamespace.values() )
    {
      if ( curNs.namespaceUri.equals( namespaceUri ) )
        return curNs;
    }
    return null;
  }

  public InputStream resolveNamespace()
          throws IOException
  {
    InputStream inStream = null;
    if ( this.getResourceName() != null )
      inStream = this.getClass().getClassLoader().getResourceAsStream( this.getResourceName() );
    if ( inStream == null && this.getResourceUri() != null )
      inStream = HttpUriResolver.newDefaultUriResolver().getResponseBodyAsInputStream( this.getResourceUri() );

    return inStream;
  }
}
