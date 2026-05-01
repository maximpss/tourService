package by.psu.model;

import by.psu.exception.TourServiceValidationException;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Pattern;

public class Client
{
    private UUID clientId;
    private String fullName;
    private String email;
    private String phone;
    private String passportNumber;
    private int loyaltyPoints;

    // Регулярное выражение для email
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    // Конструктор со всеми параметрами (clientId генерируется автоматически)
    public Client(String fullName, String email, String phone, String passportNumber, int loyaltyPoints) {
        // Валидация fullName
        if (fullName == null)
        {
            throw new TourServiceValidationException("fullName=null (не может быть null)");
        }

        String[] nameParts = fullName.trim().split("\\s+");
        if (nameParts.length < 2)
        {
            throw new TourServiceValidationException("fullName=" + fullName + " (должно быть минимум 2 слова)");
        }

        for (String part : nameParts)
        {
            if (part.length() < 2) {
                throw new TourServiceValidationException("fullName=" + fullName + " (каждое слово должно содержать минимум 2 символа)");
            }
        }

        // Валидация email
        if (email == null || !EMAIL_PATTERN.matcher(email).matches())
        {
            throw new TourServiceValidationException("email=" + email + " (не соответствует формату email)");
        }

        // Валидация phone: первый символ +, затем от 10 до 15 чисел
        if (phone == null || !phone.matches("^\\+\\d{10,15}$"))
        {
            throw new TourServiceValidationException("phone=" + phone + " (должен начинаться с + и содержать от 10 до 15 цифр)");
        }

        // Валидация passportNumber: не null, 10 символов
        if (passportNumber == null || passportNumber.length() != 10)
        {
            throw new TourServiceValidationException("passportNumber=" + passportNumber + " (должен быть не null и содержать 10 символов)");
        }

        // Валидация loyaltyPoints: не отрицательно
        if (loyaltyPoints < 0)
        {
            throw new TourServiceValidationException("loyaltyPoints=" + loyaltyPoints + " (не может быть отрицательным)");
        }

        this.clientId = UUID.randomUUID();
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.passportNumber = passportNumber;
        this.loyaltyPoints = loyaltyPoints;
    }

    // Геттеры и сеттеры
    public UUID getClientId()
    {
        return clientId;
    }

    public void setClientId(UUID clientId)
    {
        this.clientId = clientId;
    }

    public String getFullName()
    {
        return fullName;
    }

    public void setFullName(String fullName) {
        // Валидация при установке значения
        if (fullName == null)
        {
            throw new TourServiceValidationException("fullName=null (не может быть null)");
        }

        String[] nameParts = fullName.trim().split("\\s+");
        if (nameParts.length < 2)
        {
            throw new TourServiceValidationException("fullName=" + fullName + " (должно быть минимум 2 слова)");
        }

        for (String part : nameParts)
        {
            if (part.length() < 2) {
                throw new TourServiceValidationException("fullName=" + fullName + " (каждое слово должно содержать минимум 2 символа)");
            }
        }

        this.fullName = fullName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches())
        {
            throw new TourServiceValidationException("email=" + email + " (не соответствует формату email)");
        }
        this.email = email;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        if (phone == null || !phone.matches("^\\+\\d{10,15}$"))
        {
            throw new TourServiceValidationException("phone=" + phone + " (должен начинаться с + и содержать от 10 до 15 цифр)");
        }
        this.phone = phone;
    }

    public String getPassportNumber()
    {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        if (passportNumber == null || passportNumber.length() != 10)
        {
            throw new TourServiceValidationException("passportNumber=" + passportNumber + " (должен быть не null и содержать 10 символов)");
        }
        this.passportNumber = passportNumber;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints)
    {
        if (loyaltyPoints < 0)
        {
            throw new TourServiceValidationException("loyaltyPoints=" + loyaltyPoints + " (не может быть отрицательным)");
        }
        this.loyaltyPoints = loyaltyPoints;
    }

    // Метод для добавления баллов лояльности
    public void addLoyaltyPoints(int points)
    {
        if (points < 0)
        {
            throw new TourServiceValidationException("points=" + points + " (не может быть отрицательным)");
        }
        this.loyaltyPoints += points;
    }

    // Метод для получения скидки в зависимости от баллов

    public BigDecimal getDiscountRate()
    {
        if (loyaltyPoints >= 5000)
        {
            return new BigDecimal("0.2"); //5%
        } else if (loyaltyPoints >=1000)
        {
            return new BigDecimal("0.15");  //10%
        } else if (loyaltyPoints >= 500)
        {
            return new BigDecimal("0.1"); //15%
        }
        else if (loyaltyPoints >= 100)
        {
            return new BigDecimal("0.05");  //20%
        } else
        {
            return BigDecimal.ZERO;            // 0%
        }
    }


    // Метод для получения маскированного номера паспорта
    public String getMaskedPassportNumber()
    {
        if (passportNumber == null || passportNumber.length() < 4)
        {
            return "****";
        }

        int visibleChars = 4;
        int maskedLength = passportNumber.length() - visibleChars;
        StringBuilder masked = new StringBuilder();

        for (int i = 0; i < maskedLength; i++)
        {
            masked.append('*');
        }

        masked.append(passportNumber.substring(maskedLength));
        return masked.toString();
    }

    @Override
    public String toString()
    {
        return "Client{" +
                "clientId=" + clientId +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", passportNumber='" + getMaskedPassportNumber() + '\'' +
                ", loyaltyPoints=" + loyaltyPoints +
                ", discountRate=" + getDiscountRate().multiply(new BigDecimal("100")) + "%" +
                '}';
    }
}