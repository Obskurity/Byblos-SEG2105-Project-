package com.example.bybloslogin.ui.login;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class CompleteProfileActivityTest {

    @Test
    public void testValidStreetNumber() {
        String streetNumber = "100";
        boolean result1 = CompleteProfileActivity.isValidStreetNumber(streetNumber);
        assertEquals(result1, true);
    }

    @Test
    public void testInvalidStreetNumber() {
        String streetNumber = "100.1";
        boolean result1 = CompleteProfileActivity.isValidStreetNumber(streetNumber);
        assertEquals(result1, false);
    }

    @Test
    public void testValidCity() {
        String city = "Ottawa";
        boolean result1 = CompleteProfileActivity.isValidCity(city);
        assertEquals(result1, true);
    }

    @Test
    public void testInvalidCity() {
        String streetNumber = "123";
        boolean result1 = CompleteProfileActivity.isValidCity(streetNumber);
        assertEquals(result1, false);
    }

    @Test
    public void testValidPostal() {
        String postalCode = "H0H 0H0";
        boolean result1 = CompleteProfileActivity.isValidPostal(postalCode);
        assertEquals(result1, true);
    }

    @Test
    public void testInvalidPostal() {
        String postalCode = "123456";
        boolean result1 = CompleteProfileActivity.isValidPostal(postalCode);
        assertEquals(result1, false);
    }

    @Test
    public void testValidPostal2() {
        String postalCode = "12345";
        boolean result1 = CompleteProfileActivity.isValidPostal(postalCode);
        assertEquals(result1, true);
    }

    @Test
    public void testInvalidPostal2() {
        String postalCode = "1A1 A1A";
        boolean result1 = CompleteProfileActivity.isValidPostal(postalCode);
        assertEquals(result1, false);
    }

    @Test
    public void testValidPhoneNumber() {
        String test = "9059059905";
        boolean result = CompleteProfileActivity.isValidPhoneNumber(test);
        assertEquals(result, true);
    }

    @Test
    public void testInvalidPhoneNumber() {
        String test = "123";
        boolean result = CompleteProfileActivity.isValidPhoneNumber(test);
        assertEquals(result, false);
    }

}