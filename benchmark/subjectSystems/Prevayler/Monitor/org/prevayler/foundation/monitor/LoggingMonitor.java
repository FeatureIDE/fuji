package org.prevayler.foundation.monitor;
import java.io.File;
import de.ovgu.cide.jakutil.*;
/** 
 * Abstract Monitor for Logging implementation monitors to extend.
 */
public abstract class LoggingMonitor implements Monitor {
  public void notify(  Class clazz,  String message){
    if (isInfoEnabled(clazz))     info(clazz,message);
  }
  public void notify(  Class clazz,  String message,  Exception ex){
    error(clazz,message,ex);
  }
  public void notify(  Class clazz,  String message,  File file){
    if (isInfoEnabled(clazz))     info(clazz,message + "\nFile: " + file);
  }
  public void notify(  Class clazz,  String message,  File file,  Exception ex){
    error(clazz,message + "\nFile: " + file,ex);
  }
  protected abstract void info(  Class clazz,  String Message);
  protected abstract void error(  Class clazz,  String message,  Exception ex);
  /** 
 * default returns true. Override as needed.
 */
  protected boolean isInfoEnabled(  Class clazz){
    return true;
  }
}
