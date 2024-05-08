import java.io.FileWriter;
import java.io.IOException;

public class PDFObject {

    private int     ObjNum;
    private Boolean IsStream;
    private String  Str;

    private String  PrestreamStr;

    private int     ByteLocation;

    public PDFObject(int objnum, Boolean is_stream, String the_str)
    {
         ObjNum       = objnum;
         IsStream     = is_stream;
         PrestreamStr = "";
         Str          = the_str;
    }

    public PDFObject(int objnum, String prestreamstr)
    {
         ObjNum       = objnum;
         IsStream     = true;
         PrestreamStr = prestreamstr;
         Str          = "";
    }

    public void AppendStr(String appendstr)
    {
         Str += appendstr;
    }

    public int GetObjectNumber()
    {
         return ObjNum;
    }

    public Boolean GetStreamOrNot()
    {
         return IsStream;
    }

    public String GetPrestream()
    {
         return PrestreamStr;
    }

    public String GetStr()
    {
         return Str;
    }

    public void StoreByteLocation(int byteloc) 
    {
         ByteLocation = byteloc;
    }

    public int GetByteLocation()
    {
         return ByteLocation;
    }

}


