/*
 * Copyright 2013-2018 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with
 * the License. A copy of the License is located at
 * 
 * http://aws.amazon.com/apache2.0
 * 
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package com.amazonaws.services.pinpoint.model.transform;

import javax.annotation.Generated;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.pinpoint.model.*;

import com.amazonaws.protocol.*;
import com.amazonaws.annotation.SdkInternalApi;

/**
 * WriteApplicationSettingsRequestMarshaller
 */
@Generated("com.amazonaws:aws-java-sdk-code-generator")
@SdkInternalApi
public class WriteApplicationSettingsRequestMarshaller {

    private static final MarshallingInfo<StructuredPojo> CAMPAIGNHOOK_BINDING = MarshallingInfo.builder(MarshallingType.STRUCTURED)
            .marshallLocation(MarshallLocation.PAYLOAD).marshallLocationName("CampaignHook").build();
    private static final MarshallingInfo<StructuredPojo> LIMITS_BINDING = MarshallingInfo.builder(MarshallingType.STRUCTURED)
            .marshallLocation(MarshallLocation.PAYLOAD).marshallLocationName("Limits").build();
    private static final MarshallingInfo<StructuredPojo> QUIETTIME_BINDING = MarshallingInfo.builder(MarshallingType.STRUCTURED)
            .marshallLocation(MarshallLocation.PAYLOAD).marshallLocationName("QuietTime").build();

    private static final WriteApplicationSettingsRequestMarshaller instance = new WriteApplicationSettingsRequestMarshaller();

    public static WriteApplicationSettingsRequestMarshaller getInstance() {
        return instance;
    }

    /**
     * Marshall the given parameter object.
     */
    public void marshall(WriteApplicationSettingsRequest writeApplicationSettingsRequest, ProtocolMarshaller protocolMarshaller) {

        if (writeApplicationSettingsRequest == null) {
            throw new SdkClientException("Invalid argument passed to marshall(...)");
        }

        try {
            protocolMarshaller.marshall(writeApplicationSettingsRequest.getCampaignHook(), CAMPAIGNHOOK_BINDING);
            protocolMarshaller.marshall(writeApplicationSettingsRequest.getLimits(), LIMITS_BINDING);
            protocolMarshaller.marshall(writeApplicationSettingsRequest.getQuietTime(), QUIETTIME_BINDING);
        } catch (Exception e) {
            throw new SdkClientException("Unable to marshall request to JSON: " + e.getMessage(), e);
        }
    }

}
