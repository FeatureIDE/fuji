package org.prevayler.demos.demo2.business.transactions;
import java.util.Date;
import org.prevayler.demos.demo2.business.*;
import de.ovgu.cide.jakutil.*;
public class Withdrawal extends AccountTransaction {
  private static final long serialVersionUID=-4227641889302816129L;
  protected long _amount;
  private Withdrawal(){
  }
  public Withdrawal(  Account account,  long amount){
    super(account);
    _amount=amount;
  }
  public void executeAndQuery(  Account account,  Date timestamp) throws Account.InvalidAmount {
    account.withdraw(_amount,timestamp);
  }
}
