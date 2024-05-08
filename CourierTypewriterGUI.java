import java.io.FileWriter;
import java.io.IOException;



public class CourierTypewriterGUI {

     private int font_size; //10,12,14,20
	 
     private double char_width_fontsize20;
     private double char_width_fontsize14;
     private double char_width_fontsize12;
     private double char_width_fontsize10;
	 
     private int line_char_limit_fontsize10;
     private int line_char_limit_fontsize12;
     private int line_char_limit_fontsize14;
     private int line_char_limit_fontsize20;
	 
     private int line_YY_space_fontsize10;
     private int line_YY_space_fontsize12;
     private int line_YY_space_fontsize14;
     private int line_YY_space_fontsize20;
	 
     private int XX_left_margin;
     private int YY;
	 
     private Boolean is_center_justification;

     private int page_count;
     private int page_content_object;

     PDFObject current_page_object;
     PDFObject pagetreenode;

     ListPDFObjects mylist;

     //Unit is 72 == 1 inch, i do not remember how to change the unit-size
     //So if you want 0.5 inch margins at fontsize12 then
     //left-side-x value is 36
     //top-side-y value is 756
     //10 Courier characters at fontsize12 make 1 inch which means
     //width of 1 Courier-character at fontsize12 == 7.2 units
	 
     //Helvetica is a proportional-width font
     //Courier is a fixed-width font

     public CourierTypewriterGUI()
     {
          page_count = 1;
          page_content_object = 6;

	  is_center_justification    = false;
	  font_size                  = 20;
		  
	  char_width_fontsize20 = 12;
	  char_width_fontsize14 = 8.3835616438356164383561643835616;
          char_width_fontsize12 = 7.2;
	  char_width_fontsize10 = 6;
		  
	  line_char_limit_fontsize20 = 45; //51chars per line,    12 units == character-width
	  line_char_limit_fontsize14 = 65; //73chars per line,    8.3835616438356164383561643835616 units == character-width
          line_char_limit_fontsize12 = 75; //80chars per line,    7.2 units == character-width
	  line_char_limit_fontsize10 = 90;
		  
	  line_YY_space_fontsize10   = 10;
	  line_YY_space_fontsize12   = 12;
	  line_YY_space_fontsize14   = 14;
	  line_YY_space_fontsize20   = 20;
		  
	  XX_left_margin             = 36;
	  //XX_left_margin=0;
	  YY                         = 756;
          

          mylist = new ListPDFObjects();

          //1
          mylist.add(false,
                     "<</Type /Catalog /Pages 4 0 R>>");

          //2
          mylist.add(false,
                     "<</Font <</F1 3 0 R>>>>");

          //3
          //Helvetica is a proportional-width font
          //mylist.add(false,
          //          "<</Type /Font /Subtype /Type1 /BaseFont /Helvetica>>");

          //Courier is a fixed-width font
          mylist.add(false,
                     "<</Type /Font /Subtype /Type1 /BaseFont /Courier>>");
  

          //4 page-tree-node
          //example:
          //<</Type /Pages /Kids [5 0 R] /Count 1>>
          //<</Type /Pages /Kids [5 0 R 7 0 R] /Count 2>>
          //<</Type /Pages /Kids [5 0 R 7 0 R 9 0 R] /Count 3>>

          pagetreenode = mylist.add(false, 
                                              "<</Type /Pages /Kids ");

          //5 first-page
          String tempstr = String.format("<</Type /Page /Parent 4 0 R /Resources 2 0 R /MediaBox [0 0 612 792] /Contents %d 0 R>>", page_content_object);
          mylist.add(false, tempstr);


          //6
          current_page_object = mylist.add(true, "");

          System.out.println("Just start typing anything this acts like an old typewriter, for now only 1 page,");
	  System.out.println("every time you press-ENTER-key it forces a new line and carriage-return,");
	  System.out.println("to stop just make sure on 1 brand new line you press the *-key and then enter,");
	  System.out.println("to change to fontsize20 make sure on 1 brand new line you press the number-1-key and then enter,");
	  System.out.println("to change to fontsize14 make sure on 1 brand new line you press the number-2-key and then enter.");
	  System.out.println("to change to fontsize12 make sure on 1 brand new line you press the number-3-key and then enter.");
	  System.out.println("to change to fontsize10 make sure on 1 brand new line you press the number-4-key and then enter.");
	  System.out.println("by default this types left-justification like a typewriter, but to start typing center-justification press the number-5-key and then enter.");
	  System.out.println("ok please start typing:");
     }



     public int GetLine(String myline)
     {
          int mylen = myline.length();

          if (mylen == 1) {
               char mychar = myline.charAt(0);
               if ((int)mychar == 42) {
                    //ESC-key is 0x27 for ubuntu-linux, does not work on windows nor does ctrl-keys show up on windows line so using * key which is 0x2A
                    CloseOutputFile();
                    return -1;
               }
	       else if ((int)mychar == 49) { //number-1-key == 0x31
	            System.out.println("changing font_size to 20\n");
	            font_size = 20;
	       }
	       else if ((int)mychar == 50) { //number-2-key == 0x32
	            System.out.println("changing font_size to 14\n");
		    font_size = 14;
               }
	       else if ((int)mychar == 51) { //number-3-key == 0x33
	            System.out.println("changing font_size to 12\n");
		    font_size = 12;
	       }
	       else if ((int)mychar == 52) { //number-4-key == 0x34
	            System.out.println("changing font_size to 10\n");
		    font_size = 10;
	       }
	       else if ((int)mychar == 53) { //number-5-key == 0x35
	            System.out.println("changing to center-justification");
		    is_center_justification = true;
               }
               else if ((int)mychar == 54) { //number-6-key == 0x36
                    System.out.println("changing to left-justification");
                    is_center_justification = false;
               }
               else {
                    //System.out.println(String.format("length is 1, int value of mychar is %d\n", (int)mychar));
                    ProcessLine(myline, 1);
	       }
          }
          else
               ProcessLine(myline, 1);

          return YY;
     }//GetLine


     private void CloseOutputFile()
     {
          //<</Type /Pages /Kids [5 0 R 7 0 R 9 0 R] /Count 3>>
          pagetreenode.AppendStr("[");
          int pageobj = 5;
          for (int pp=1; pp <= page_count; pp++)
          {
               if (pp >= 2)
                    pagetreenode.AppendStr(" ");
               pagetreenode.AppendStr(String.format("%d 0 R", pageobj));
               pageobj += 2;
          }
          pagetreenode.AppendStr(String.format("] /Count %d>>", page_count));

          mylist.WriteToFile();
     }


     private void ProcessLine(String myline, int fontnum)
     {
          String addstr;
          String tempstr = myline;
	  int line_char_limit = 666;
	  int line_YY_space = 0;
	  double char_width = 0;
		  
	  if (font_size == 10) {
	       line_char_limit = line_char_limit_fontsize10;
	       line_YY_space = line_YY_space_fontsize10;
	       char_width = char_width_fontsize10;
	  }
	  else if (font_size == 12) {
	       line_char_limit = line_char_limit_fontsize12;
	       line_YY_space = line_YY_space_fontsize12;
	       char_width = char_width_fontsize12;
	  }
	  else if (font_size == 14) {
	       line_char_limit = line_char_limit_fontsize14;
	       line_YY_space = line_YY_space_fontsize14;
	       char_width = char_width_fontsize14;
	  }
	  else if (font_size == 20) {
	       line_char_limit = line_char_limit_fontsize20;
	       line_YY_space = line_YY_space_fontsize20;
	       char_width = char_width_fontsize20;
	  }
		  
          while (tempstr.length() >= line_char_limit)
          {
               String appendstr = tempstr.substring(0, line_char_limit);
               tempstr = tempstr.substring(line_char_limit);

               addstr = String.format("BT /F%d %d Tf %d %d Td (%s)Tj ET\n", fontnum, font_size, XX_left_margin, YY, appendstr);

               current_page_object.AppendStr(addstr);
               AdjustYYAndCheckNewPage(line_YY_space);
          }
		  
	  if (is_center_justification)
	  {
               if (tempstr.length() > 0)
	       {
	            int white_space_character_length = line_char_limit - tempstr.length();
		    double tempdouble = (double)white_space_character_length / 2.0;
		    tempdouble = tempdouble * char_width;
		    int tempXXleftmargin = XX_left_margin + (int)tempdouble;
		    addstr = String.format("BT /F%d %d Tf %d %d Td (%s)Tj ET\n", fontnum, font_size, tempXXleftmargin, YY, tempstr);
	       }
	       else
	            addstr = String.format("BT /F%d %d Tf %d %d Td (%s)Tj ET\n", fontnum, font_size, XX_left_margin, YY, tempstr); 
	  }
	  else
	       addstr = String.format("BT /F%d %d Tf %d %d Td (%s)Tj ET\n", fontnum, font_size, XX_left_margin, YY, tempstr);
		   
          current_page_object.AppendStr(addstr);
          AdjustYYAndCheckNewPage(line_YY_space);
     }//ProcessLine

     private void AdjustYYAndCheckNewPage(int line_YY_space)
     {
          YY -= line_YY_space;
          if (YY <= 36)
          {
               YY = 756;
               page_count++;
               page_content_object += 2;

               String tempstr = String.format("<</Type /Page /Parent 4 0 R /Resources 2 0 R /MediaBox [0 0 612 792] /Contents %d 0 R>>", page_content_object);
               mylist.add(false, tempstr);

               current_page_object = mylist.add(true, "");
          }
     }

}//class


