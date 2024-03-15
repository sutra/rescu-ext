package org.oxerr.rescu.ext.singleton;

import java.io.IOException;

import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;

@Path("/{version}")
public interface SellerListingResource {

	@DELETE
	@Path("/externalsellerlistings/{externalId}")
	void deleteListingByExternalListingId(@PathParam("externalId") String externalId) throws IOException;

}
