/*
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

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


public class AuthenticationUtils {

	public static String createSignature(AwsAccount account, String data) {
	    try {
	        // Create a Mac instance and initialize it with the AWS secret key
	        SecretKeySpec secretKeySpec = new SecretKeySpec(account.getSecretAccessKey().getBytes(), AuthenticationUtils.HMAC_SHA1);
	        Mac mac = Mac.getInstance(AuthenticationUtils.HMAC_SHA1);
	        mac.init(secretKeySpec);
	        // Compute the HMAC and return the base64 encoded result
	        byte[] rawSignature = mac.doFinal(data.getBytes());
	        return new String(Base64.encodeBase64(rawSignature));
	    } catch (NoSuchAlgorithmException e) {
	        throw new RuntimeException("Cannot get an instance of Mac " + AuthenticationUtils.HMAC_SHA1);
	    } catch (InvalidKeyException e) {
	        throw new RuntimeException("Invalid secret key: " + account.getSecretAccessKey());
	    }
	}

	public final static String HMAC_SHA1 = "HmacSHA1";
}
