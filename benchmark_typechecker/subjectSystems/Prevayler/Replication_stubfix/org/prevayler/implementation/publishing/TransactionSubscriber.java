package org.prevayler.implementation.publishing;
import de.uni_passau.spl.bytecodecomposer.stubs.Stub;
public interface TransactionSubscriber {
    @Stub
    void receive(org.prevayler.implementation.TransactionTimestamp transactionTimestamp);
}