import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;

public class ListPDFObjects {

    private ArrayList<PDFObject> mylist;
    private int                  count;
    private int                  byte_location;
    private int                  xrefByteLocation;

    public ListPDFObjects()
    {
        mylist = new ArrayList<PDFObject>();
        count=0;
        byte_location=0;
    }

    public PDFObject add(Boolean is_stream, String the_str)
    {
        count++;
        PDFObject myobj = new PDFObject(count, is_stream, the_str);
        mylist.add(myobj);
        return myobj;
    }

    public PDFObject addStreamObjectAndPrestream(String prestreamstr)
    {
        count++;
        PDFObject myobj = new PDFObject(count, prestreamstr);
        mylist.add(myobj);
        return myobj;
    }

    private Boolean WriteNewLineCharacter(FileWriter mywriter)
    {
       try {
            mywriter.write("\n");
            byte_location += 1;
       }
       catch (IOException ex) {
             System.out.println("WriteNewLineCharacter: IOException caught");
             ex.printStackTrace();
             return false;
       }
       return true;
    }

    private Boolean Write(FileWriter mywriter, String str)
    {
        try {
             mywriter.write(str);
             byte_location += str.length();
        }
        catch (IOException ex) {
             System.out.println("Write: IOException caught");
             ex.printStackTrace();
             return false;
        }
        return true;
    }
  
    public Boolean WriteToFile()
    {
        try {
             FileWriter mywriter = new FileWriter("output.pdf");
             Write(mywriter, "%PDF-2.0");
             WriteNewLineCharacter(mywriter);
             WriteObjects(mywriter);
             WriteCrossReferenceTable(mywriter);
             WriteTrailer(mywriter);
             mywriter.close();
        }
        catch (IOException ex) {
             System.out.println("WriteToFile: IOException caught");
             ex.printStackTrace();
             return false;
        }
        return true;
    }


    private void WriteTrailer(FileWriter mywriter)
    {
        String tempstr;
        Write(mywriter, "trailer <</Size ");
        tempstr = String.format("%d", count + 1);
        Write(mywriter, tempstr);
        Write(mywriter, "/Root 1 0 R>>");
        WriteNewLineCharacter(mywriter);
        Write(mywriter, "startxref");
        WriteNewLineCharacter(mywriter);

        tempstr = String.format("%d", xrefByteLocation);
        Write(mywriter, tempstr);

        WriteNewLineCharacter(mywriter);
        Write(mywriter,"%%EOF");
    }

    private void WriteCrossReferenceTable(FileWriter mywriter)
    {
        String tempstr;
        xrefByteLocation = byte_location;
        Write(mywriter, "xref");
        WriteNewLineCharacter(mywriter);
        tempstr = String.format("0 %d", count + 1);
        Write(mywriter, tempstr);
        WriteNewLineCharacter(mywriter);
        Write(mywriter, "0000000000 65535 f");
        WriteNewLineCharacter(mywriter);
             
        for (int ii=0; ii < mylist.size(); ii++)
        {
             PDFObject myobj = mylist.get(ii);
             int byteloc = myobj.GetByteLocation();

             tempstr = String.format("%010d", byteloc);

             Write(mywriter, tempstr);
             Write(mywriter, " 00000 n");
             WriteNewLineCharacter(mywriter);
        }
    }

    private void WriteObjects(FileWriter mywriter)
    {
        String tempstr;
        for (int ii=0; ii < mylist.size(); ii++)
        {
             PDFObject myobj = mylist.get(ii);
             myobj.StoreByteLocation(byte_location);
             if (myobj.GetStreamOrNot()) 
             {
                  tempstr = String.format("%d",
                            myobj.GetObjectNumber());
                  Write(mywriter, tempstr);
                  Write(mywriter, " 0 obj");
                  WriteNewLineCharacter(mywriter);

                  if (myobj.GetPrestream().length() == 0)
                  {    //no prestream
                       Write(mywriter, "<</Length ");
                       tempstr = String.format("%d",
                                 myobj.GetStr().length()+1);
                       Write(mywriter, tempstr);
                       Write(mywriter, ">>");
                       WriteNewLineCharacter(mywriter);
                       Write(mywriter, "stream");
                       WriteNewLineCharacter(mywriter);
                       Write(mywriter, myobj.GetStr());
                       WriteNewLineCharacter(mywriter);
                       Write(mywriter, "endstream");
                       WriteNewLineCharacter(mywriter);
                       Write(mywriter, "endobj");
                       WriteNewLineCharacter(mywriter);
                  }
                  else 
                  {    //prestream exists
                       Write(mywriter, myobj.GetPrestream());
                       Write(mywriter, "/Length ");
                       tempstr = String.format("%d",
                                 myobj.GetStr().length()+1);
                       Write(mywriter, tempstr);
                       Write(mywriter, ">>");
                       WriteNewLineCharacter(mywriter);
                       Write(mywriter, "stream");
                       WriteNewLineCharacter(mywriter);
                       Write(mywriter, myobj.GetStr());
                       WriteNewLineCharacter(mywriter);
                       Write(mywriter, "endstream");
                       WriteNewLineCharacter(mywriter);
                       Write(mywriter, "endobj");
                       WriteNewLineCharacter(mywriter);
                  }
             }
             else //not stream object
             {
                  tempstr = String.format("%d",
                            myobj.GetObjectNumber());
                  Write(mywriter, tempstr);
                  Write(mywriter, " 0 obj");
                  WriteNewLineCharacter(mywriter);
                  Write(mywriter, myobj.GetStr());
                  WriteNewLineCharacter(mywriter);
                  Write(mywriter, "endobj");
                  WriteNewLineCharacter(mywriter);
             }
        }//for
    }//WriteObjects
}//class ListPDFObjects


