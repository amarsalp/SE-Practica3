package services;

import data.Password;
import exceptions.dataExceptions.BadFormatException;
import exceptions.serviceExceptions.InvalidAccountException;

import java.util.HashMap;
import java.util.Map;

public class LocalServiceImpl implements LocalService {
    private Map<String, Password> db = new HashMap<>();

    public LocalServiceImpl() throws BadFormatException {
        db.put("user", new Password("Password1"));
    }

    @Override
    public void verifyAccount(String login, Password pssw) throws InvalidAccountException {
        Password password = db.get(login);
        //user does not exist or password is incorrect
        if (!pssw.equals(password)) throw new InvalidAccountException("Account is not valid");
    }
}
