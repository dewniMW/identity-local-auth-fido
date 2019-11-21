/*
 * Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.identity.application.authenticator.fido2.endpoint.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authenticator.fido2.core.WebAuthnService;
import org.wso2.carbon.identity.application.authenticator.fido2.endpoint.FinishRegistrationApiService;
import org.wso2.carbon.identity.application.authenticator.fido2.endpoint.common.Constants;
import org.wso2.carbon.identity.application.authenticator.fido2.exception.FIDO2AuthenticatorException;
import org.wso2.carbon.identity.application.authenticator.fido2.exception.FIDO2AuthenticatorServerException;

import java.io.IOException;
import java.text.MessageFormat;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.application.authenticator.fido2.endpoint.common.Util.getErrorDTO;

public class FinishRegistrationApiServiceImpl extends FinishRegistrationApiService {

    private static final Log LOG = LogFactory.getLog(FinishRegistrationApiServiceImpl.class);

    @Override
    public Response finishRegistrationPost(String response) {

        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("Received finish registration response: {0}", response));
        }
        try {
            WebAuthnService webAuthnService = new WebAuthnService();
            webAuthnService.finishRegistration(response);
        } catch (FIDO2AuthenticatorServerException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(getErrorDTO(Constants.ErrorMessages
                    .ERROR_CODE_FINISH_REGISTRATION)).build();
        } catch (FIDO2AuthenticatorException | IOException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(getErrorDTO(Constants.ErrorMessages
                    .ERROR_CODE_FINISH_REGISTRATION_BY_USER, response)).build();
        }
        return Response.ok().entity(response).build();
    }

}
