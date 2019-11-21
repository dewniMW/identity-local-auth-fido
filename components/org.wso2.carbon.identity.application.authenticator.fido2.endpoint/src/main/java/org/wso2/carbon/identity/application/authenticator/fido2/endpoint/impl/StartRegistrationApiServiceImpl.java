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

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.identity.application.authenticator.fido2.core.WebAuthnService;
import org.wso2.carbon.identity.application.authenticator.fido2.dto.RegistrationRequest;
import org.wso2.carbon.identity.application.authenticator.fido2.endpoint.StartRegistrationApiService;
import org.wso2.carbon.identity.application.authenticator.fido2.endpoint.common.Constants;
import org.wso2.carbon.identity.application.authenticator.fido2.exception.FIDO2AuthenticatorException;
import org.wso2.carbon.identity.application.authenticator.fido2.util.Either;
import org.wso2.carbon.identity.application.authenticator.fido2.util.FIDOUtil;

import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.application.authenticator.fido2.endpoint.common.Util.getErrorDTO;

public class StartRegistrationApiServiceImpl extends StartRegistrationApiService {

    private static final Log LOG = LogFactory.getLog(StartRegistrationApiServiceImpl.class);

    @Override
    public Response startRegistrationGet(String appId) {

        if (StringUtils.isBlank(appId)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Fetching device metadata failed as the app Id is not existing in the request.");
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(getErrorDTO
                    (Constants.ErrorMessages.ERROR_CODE_START_REGISTRATION_EMPTY_APP_ID)).build();
        }

        try {
            WebAuthnService webAuthnService = new WebAuthnService();
            Either<String, RegistrationRequest> result = webAuthnService.startRegistration(appId);
            if (result.isRight()) {
                return Response.ok().entity(FIDOUtil.writeJson(result.right().get())).build();
            } else {
                return Response.serverError().entity(getErrorDTO(Constants.ErrorMessages.ERROR_CODE_START_REGISTRATION,
                        appId)).build();
            }
        } catch (FIDO2AuthenticatorException | JsonProcessingException e) {
            return Response.serverError().entity(getErrorDTO(Constants.ErrorMessages.ERROR_CODE_START_REGISTRATION,
                    appId)).build();
        }

    }
}
