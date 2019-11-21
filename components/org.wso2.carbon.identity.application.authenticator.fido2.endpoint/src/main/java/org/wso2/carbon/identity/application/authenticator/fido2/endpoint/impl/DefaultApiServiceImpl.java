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
import org.wso2.carbon.identity.application.authenticator.fido2.endpoint.DefaultApiService;
import org.wso2.carbon.identity.application.authenticator.fido2.endpoint.common.Constants;
import org.wso2.carbon.identity.application.authenticator.fido2.util.FIDOUtil;
import org.wso2.carbon.identity.core.util.IdentityCoreConstants;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.MessageFormat;
import javax.ws.rs.core.Response;

import static org.wso2.carbon.identity.application.authenticator.fido2.endpoint.common.Util.getErrorDTO;

public class DefaultApiServiceImpl extends DefaultApiService {

    private static final Log LOG = LogFactory.getLog(DefaultApiServiceImpl.class);

    @Override
    public Response rootGet(String username) {

        if (StringUtils.isBlank(username)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Fetching device metadata failed as the username is not existing in the request.");
            }
            return Response.status(Response.Status.BAD_REQUEST).entity(getErrorDTO
                    (Constants.ErrorMessages.ERROR_CODE_FETCH_CREDENTIALS_EMPTY_USERNAME)).build();
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug(MessageFormat.format("Fetching device metadata for the username: {0}", username));
        }
        try {
            WebAuthnService service = new WebAuthnService();
            if (username.contains(Constants.EQUAL_OPERATOR)) {
                username = URLDecoder.decode(username.split(Constants.EQUAL_OPERATOR)[1], IdentityCoreConstants.UTF_8);
            }
            return Response.ok().entity(FIDOUtil.writeJson(service.getDeviceMetaData(username))).build();
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            return Response.serverError().entity(getErrorDTO(Constants.ErrorMessages
                    .ERROR_CODE_FETCH_CREDENTIALS)).build();
        }
    }

}
