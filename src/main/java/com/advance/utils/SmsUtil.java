package com.advance.utils;

import static com.twilio.rest.api.v2010.account.Message.creator;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class SmsUtil {
	
	public static final String FROM_NUMBER = "<Your own number from Twilio>";
	public static final String SID_KEY = "<Your own key>";
	public static final String TOKEN_KEY = "<Your own key>";

	public static void sendSms(String to, String messageBody) {
		Twilio.init(SID_KEY, TOKEN_KEY);
		Message message = creator(new PhoneNumber("+" + to), new PhoneNumber(FROM_NUMBER), messageBody).create();
		System.out.println(message);
	}
}
