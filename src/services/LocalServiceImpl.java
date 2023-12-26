package services;

import data.Password;
import exceptions.dataExceptions.BadFormatException;
import exceptions.serviceExceptions.InvalidAccountException;

import java.util.HashMap;
import java.util.Map;

public class LocalServiceImpl implements LocalService {
    Map<String, Password> db = new HashMap<>();

    public LocalServiceImpl() throws BadFormatException {
        Password password = new Password("Password1");
        db.put("user", password);
    }

    @Override
    public void verifyAccount(String login, Password pssw) throws InvalidAccountException {
        Password password = db.get(login);
        //user does not exists or password is incorrect
        if (password == null || !pssw.equals(password)) throw new InvalidAccountException("Account is not valid");
    }
}
