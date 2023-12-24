package data;

import exceptions.dataExceptions.BadFormatException;

/**
 * Essential data classes
 */
final public class Password {
    private final String password;

    public Password(String password) throws BadFormatException {
        if (password == null) throw new NullPointerException("Null password");

        if (!correctFormat(password)) throw new BadFormatException("Incorrect password");

        this.password = password;
    }

    private boolean correctFormat(String password) {
        boolean lowerCase = false, upperCase = false, number = false;
        //Password must contain at lest 8 characters
        if (password.length() < 8) return false;
        //Password must contain at least one upper case character one lower case character and one number
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i))) lowerCase = true;
            if (Character.isUpperCase(password.charAt(i))) upperCase = true;
            if (Character.isDigit(password.charAt(i))) number = true;
        }

        return lowerCase && upperCase && number;
    }

    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return this.password.equals(password.password);
    }

    @Override
    public int hashCode() {
        return this.password.hashCode();
    }

    @Override
    public String toString() {
        return "Password{" + "Password ='" + this.password + '\'' + '}';
    }
}
