package org.oxerr.rescu.ext.singleton;

import java.io.IOException;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Path("/{version}")
public interface SellerListingResource {

	@DELETE
	@Path("/externalsellerlistings/{externalId}")
	void deleteListingByExternalListingId(@PathParam("externalId") String externalId) throws IOException;

}
