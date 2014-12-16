/*
 * Copyright 2006 Polar Rose <http://www.polarrose.com>
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

package com.polarrose.amazon.aws;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

public class AwsUtils
{
    private AwsUtils()
    {
    }

    public static void validateAwsAccount(AwsAccount account)
    {
        if (account == null) {
            throw new IllegalArgumentException("Parameter 'account' cannot be null");
        }
    }

    //

    /**
     * URL encode a string according to the rules defined by AWS.
     *
     * @param value the unencoded value
     * @return an URL-encoded version of the input
     */

    private static String encode(String value)
    {
        try {
            String encoded = URLEncoder.encode(value, "UTF-8");
            encoded = encoded.replace("+", "%20"); // Lame
            encoded = encoded.replace("*", "%2A"); // Lame
            encoded = encoded.replace("%7E", "~"); // Lame
            return encoded;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private final static String SIGNATURE_METHOD = "HmacSHA1";

	private static String createSignature(AwsAccount account, String data)
    {
	    try {
	        // Create a Mac instance and initialize it with the AWS secret key
	        SecretKeySpec secretKeySpec = new SecretKeySpec(account.getSecretAccessKey().getBytes(), SIGNATURE_METHOD);
	        Mac mac = Mac.getInstance(SIGNATURE_METHOD);
	        mac.init(secretKeySpec);
	        // Compute the HMAC and return the base64 encoded result
	        byte[] rawSignature = mac.doFinal(data.getBytes());
	        return new String(Base64.encodeBase64(rawSignature));
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Cannot get an instance of Mac " + SIGNATURE_METHOD);
	    } catch (InvalidKeyException e) {
	        throw new RuntimeException("Invalid secret key: " + account.getSecretAccessKey());
	    }
	}

    public static void createSignatureV2Parameters(AwsAccount account, String method, String host, String uri, Map<String,String> parameters)
    {
        parameters.put("SignatureVersion", "2");
        parameters.put("SignatureMethod", SIGNATURE_METHOD);

        // Create a sorted list of the parameter names

        List<String> sortedNames = new ArrayList<String>();
        for (String name : parameters.keySet()) {
            sortedNames.add(name);
        }

        Collections.sort(sortedNames);

        // Concatenate the sorted name/value pairs to form the signature data

        StringBuffer signatureData = new StringBuffer();

        signatureData.append(method);
        signatureData.append("\n");

        signatureData.append(host.toLowerCase());
        signatureData.append("\n");

        signatureData.append(uri);
        signatureData.append("\n");

        for (int i = 0; i < sortedNames.size(); i++) {
            if (i != 0) {
                signatureData.append("&");
            }
            signatureData.append(encode(sortedNames.get(i)));
            signatureData.append("=");
            signatureData.append(encode(parameters.get(sortedNames.get(i))));
        }

        // Create the signature parameters

        parameters.put("Signature", createSignature(account, signatureData.toString()));
    }

    //

    private static final SimpleDateFormat timestampDateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    public static String createRequestTimestamp()
    {
        return timestampDateformat.format(new Date());
    }

}
