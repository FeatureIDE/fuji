package Everything;import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;




public class SMTPTransmitter {

    private List destinations;
    private List cc;
    private List bcc;
    private List attachments;
    private String subject;
    private String body;
    private String account;
    private Properties props;


 	public SMTPTransmitter(){
 		System.out.println("mail transmitted");
 	}

    public SMTPTransmitter(String accountName){
		this.account = accountName;
		Account acc = new Account(accountName);
		props = acc.getProperties();
    }

    public void addDestination(String address){
	if(destinations==null)
	    destinations = new LinkedList();

	destinations.add(address);
    }

    public void addCC(String address){
	if(cc==null)
	    cc = new LinkedList();

	cc.add(address);
    }

    public void addBCC(String address){
	if(bcc==null)
	    bcc = new LinkedList();

	bcc.add(address);
    }

    public void addAttachment(String path){
	if(attachments==null)
	    attachments = new LinkedList();

	attachments.add(path);
    }

    public void setSubject(String sub){
	subject= sub;
    }

    public void setMessage(String message){
	body = message;
    }

    public void send(){
	try{
	    Properties propss = new Properties();
	    propss.setProperty("mail.smtp.host", ((SMTPTransmitter) this).props.getProperty("smtpServer"));

	    Session session = Session.getInstance(propss, null);
	    Message msg = new MimeMessage(session);

	    msg.setFrom(new InternetAddress(((SMTPTransmitter) this).props.getProperty("userSend")));
	    msg.setSubject(subject);
		//normal recipients
	    InternetAddress[] dest = new InternetAddress[destinations.size()];
	    for(int i=0; i<dest.length; i++)
			dest[i] = new InternetAddress((String)destinations.get(i));

	    msg.setRecipients(Message.RecipientType.TO,dest);

	    //CC recipients
	    if (cc != null){
		InternetAddress[] ccs = new InternetAddress[cc.size()];
		for(int i=0; i<ccs.length; i++)
		    ccs[i] = new InternetAddress((String)cc.get(i));
			msg.setRecipients(Message.RecipientType.CC,ccs);
	    }

	    //BCC recipients
	    if (bcc != null){
		InternetAddress[] bccs = new InternetAddress[bcc.size()];
		for(int i=0; i<bccs.length; i++)
		    bccs[i] = new InternetAddress((String)bcc.get(i));
			msg.setRecipients(Message.RecipientType.BCC,bccs);
	    }

	    //attachments
	    if (attachments != null && attachments.size()!=0){
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setText(body);

			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			for(int i=0; i<attachments.size(); i++){
			    String path = (String)attachments.get(i);
			    DataHandler dh = new DataHandler(new FileDataSource(path));
		            MimeBodyPart attachBodyPart = new MimeBodyPart();
		            attachBodyPart.setDataHandler(dh);
		            attachBodyPart.setFileName(getFileName(path));
		            multipart.addBodyPart(attachBodyPart);
			}
			msg.setContent(multipart);
	    }
	    else{
			msg.setText(body);
	    }

	
	    msg.setSentDate(new Date());
	    Transport.send(msg);

	} catch (Exception ex){System.err.println("Unable to send mail ... ");
			      System.out.println(ex.toString());
	}

	try{((SMTPTransmitter) this).finalize();}
	catch(Throwable e){};
    }

    private static String getFileName(String path){
	String name = "";
	int posOfLastSlash = 0;

	for(int i=(path.length()-1);i>=0;i--){
	    char c = path.charAt(i);
	    if(c=='/' || c=='\\'){
		posOfLastSlash = i;
		i=-1;
	    }
	}

	for(int i=posOfLastSlash+1;i<path.length();i++)
	   name = name + path.charAt(i);

	return name;
    }

    public static void main(String[] args){
		SMTPTransmitter t = new SMTPTransmitter("###");
		t.addDestination("###");
		t.setSubject("###");
		t.setMessage("###");
		//t.addAttachment("###");
		t.send();
    }
}