

import Jakarta.util.*;
import java.io.*;
import java.util.*;

class MainModel {

    public void visit( Visitor v ) {
        
        v.action( this );
    }

}
