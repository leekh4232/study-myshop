package kr.hossam.myshop.helpers;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class UtilHelper {
    public String getRandomString(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("길이는 1자리 이상이어야 합니다.");
        }

        final String DATA = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            // 0부터 DATA.length() - 1까지의 랜덤한 정수를 생성한다.
            int rnd = random.nextInt(DATA.length());

            // 생성된 랜덤한 정수를 인덱스로 사용하여 DATA에서 문자를 가져온다.
            // String.charAt() 메서드는 해당 인덱스의 문자를 반환한다
            // 따라서, sb에 랜덤하게 선택된 문자를 추가한다.
            sb.append( DATA.charAt(rnd));
        }

        // 최종적으로 생성된 문자열을 반환한다.
        // StringBuilder는 문자열을 효율적으로 조작할 수 있는 클래스이다.
        // toString() 메서드를 호출하여 StringBuilder 객체를 String으로 변환한다
        // 이 메서드는 StringBuilder에 저장된 모든 문자를 하나의 문자열로 결합한다.
        return sb.toString();
    }

    private enum MaskingType {
        Name,
        Phone,
        Email,
        Address,
        BankName,
        AccountNumber,
        Year,
        Month
    }

    private String maskAs(String value, MaskingType maskingType ) {
        if (value == null  || value.trim().length() == 0) {
            return value;
        }

        String maskingValue = value;
        switch (maskingType) {
            case Name:
                if (value.length() == 2)
                    maskingValue = "*"+value.substring(1);
                else if (value.length() == 3)
                    maskingValue = value.substring(0,1)+"*"+value.substring(2);
                else if (value.length() >= 4)
                    maskingValue = value.substring(0,1)+"**"+value.substring(3);
                break;
            case Phone:
                if (value.length() >= 8 && value.length() <= 10)
                    maskingValue = (value.length() == 8) ? value.substring(0,4)+"****" : value.substring(0,4)+"****"+value.substring(8);
                else if (value.length() >= 11)
                    maskingValue = value.substring(0,5)+"****"+value.substring(9);
                // what about length 1 to 7
                break;
            case Email:
                if (!value.substring(0).equals("@") && value.contains("@")){
                    String emailValue = value.split("@")[0];
                    if (emailValue.length() == 1 || emailValue.length() == 2)
                        maskingValue = (emailValue.length() == 1) ? value.replaceAll("(?<=.{0}).(?=.*@)", "*") : "*"+ emailValue.substring(1,2)+"@"+value.split("@")[1];
                    else if (emailValue.length() == 3 || emailValue.length() == 4)
                        maskingValue = (emailValue.length() == 3) ? value.replaceAll("(?<=.{1}).(?=.*@)", "*") : emailValue.substring(0,1)+ "**" + emailValue.substring(3)+"@"+ value.split("@")[1];
                    else if (emailValue.length() > 4)
                        maskingValue = emailValue.substring(0,1)+ "***" + emailValue.substring(4)+"@"+ value.split("@")[1];
                }
                break;
            case Address:
                StringBuilder address = new StringBuilder();
                for (int i =0; i < value.length(); i++) {
                    char item = value.charAt(i);
                    address.append(Character.isWhitespace(item) ? item : '*');
                }
                maskingValue = address.toString();
                break;
            case BankName:
                if(value.length() > 2) {
                    maskingValue =  "**" + value.substring(2);
                }else {
                    maskingValue = "**";
                }
                break;
            case AccountNumber:
                StringBuilder accountNumber = new StringBuilder();
                int length = maskingValue.length();
                if(length > 6) {
                    accountNumber.append(value.substring(0,6)); //what if a/c no length is less than 7
                }
                for (int i = 0; i <= length - 7; i++) {
                    accountNumber.append("*");
                }
                maskingValue = accountNumber.toString();
                break;
            case Year:
                StringBuilder year = new StringBuilder();
                for (int i =0; i < value.length(); i++) {
                    char item = value.charAt(i);
                    year.append(Character.isWhitespace(item) ? item : '*');
                }
                maskingValue = year.toString();
                break;
            case Month:
                StringBuilder month = new StringBuilder();
                for (int i =0; i < value.length(); i++) {
                    char item = value.charAt(i);
                    month.append(Character.isWhitespace(item) ? item : '*');
                }
                maskingValue = month.toString();
                break;
        }
        return maskingValue;
    }

    public String maskAsName(String value) {
        return maskAs(value, MaskingType.Name);
    }

    public String maskAsPhone(String value) {
        return maskAs(value, MaskingType.Phone);
    }

    public String maskAsEmail(String value) {
        return maskAs(value, MaskingType.Email);
    }

    public String maskAsAddress(String value) {
        return maskAs(value, MaskingType.Address);
    }

    public String maskAsBankName(String value) {
        return maskAs(value, MaskingType.BankName);
    }

    public String maskAsAccountNumber(String value) {
        return maskAs(value, MaskingType.AccountNumber);
    }

    public String maskAsYear(String value) {
        return maskAs(value, MaskingType.Year);
    }

    public String maskAsMonth(String value) {
        return maskAs(value, MaskingType.Month);
    }
}