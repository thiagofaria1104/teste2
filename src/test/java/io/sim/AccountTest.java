package io.sim;

import org.junit.Test;
import static org.junit.Assert.*;

public class AccountTest {

    @Test
    public void testDeposit() {
        Account account = new Account("username", "password", 100.0);
        account.deposit(50.0);
        assertEquals(150.0, account.getBalance(), 0.001);
    }

    @Test
    public void testValidWithdrawal() {
        Account account = new Account("username", "password", 100.0);
        assertTrue(account.withdraw(50.0));
        assertEquals(50.0, account.getBalance(), 0.001);
    }

    @Test
    public void testInvalidWithdrawal() {
        Account account = new Account("username", "password", 100.0);
        assertFalse(account.withdraw(-50.0));
        assertFalse(account.withdraw(150.0));
        assertEquals(100.0, account.getBalance(), 0.001);
    }

    @Test
    public void testInitialBalance() {
        Account account = new Account("username", "password", 100.0);
        assertEquals(100.0, account.getBalance(), 0.001);
    }
}
