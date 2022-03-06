package com.clientui.clientui.exceptions;

import feign.Response;
import feign.codec.ErrorDecoder;

public class CustomErrorDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultErrorDecoder = new Default();

    @Override
    public Exception decode(String invoqueur, Response response) {
        if(response.status() == 400) {
            return new ProductBadRequestException(
                    "Requête incorrecte"
            );
        }
        if(response.status() == 404) {
            return new ProductBadRequestException(
                    "Ressource non trouvée"
            );
        }
        return defaultErrorDecoder.decode(invoqueur, response);
    }
}
