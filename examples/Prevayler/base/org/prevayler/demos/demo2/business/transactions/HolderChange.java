package org.prevayler.demos.demo2.business.transactions;
import java.util.Date;
import org.prevayler.demos.demo2.business.Account;
import de.ovgu.cide.jakutil.*;
public class HolderChange extends AccountTransaction {
  private static final long serialVersionUID=-5846094202312934631L;
  private String _newHolder;
  private HolderChange(){
  }
  public HolderChange(  Account account,  String newHolder){
    super(account);
    _newHolder=newHolder;
  }
  public void executeAndQuery(  Account account,  Date ignored) throws Account.InvalidHolder {
    account.holder(_newHolder);
  }
}
