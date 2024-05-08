
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;


public class AdobePDFMakerGUI extends JFrame {

     public AdobePDFMakerGUI() {
          CourierTypewriterGUI typewriter = new CourierTypewriterGUI();

          Container cp = getContentPane();
          cp.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

          JTextField textfield = new JTextField("", 80);
          cp.add(textfield);

          JLabel lengthLabel = new JLabel("length=");
          cp.add(lengthLabel);

          JLabel YYLabel = new JLabel("YY=");
          cp.add(YYLabel);

          ((AbstractDocument)textfield.getDocument()).setDocumentFilter(new ExampleExpandingDocumentFilter(textfield,lengthLabel));


          textfield.addActionListener(new ActionListener() {

              @Override
              public void actionPerformed(ActionEvent ev) {
                   System.out.println("textfield actionPerformed");
                   String myline = textfield.getText();

                   int XX = 0;
                   int YY = typewriter.GetLine(myline);
                   if (YY != -1) {
                        YYLabel.setText(String.format("YY=%d",YY));
                        textfield.setText("");
                   }
                   else
                        System.exit(0);
              }

          });


          setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          setTitle("GuiTest");
          setLocationRelativeTo(null); //center window on screen
          setSize(900,150);
          setVisible(true);
      }

      public static void main(String[] args) {
          //Run the GUI codes on Event-Dispatching thread for tread saety
          SwingUtilities.invokeLater(new Runnable() {
              @Override
              public void run() {
                  new AdobePDFMakerGUI();
              }
          });
      }//main


      public class ExampleExpandingDocumentFilter extends DocumentFilter {

           private JTextField targettextfield;
           private JLabel     targetlabel;

           public ExampleExpandingDocumentFilter(JTextField textfield, JLabel mylabel) {
                super();
                targettextfield = textfield;
                targetlabel = mylabel;
           }

           @Override
           public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
                super.remove(fb, offset, length);
                System.out.println("remove");
                ChangeLabel();
           }


           @Override
           public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
                 super.insertString(fb, offset, text, attr);
                 System.out.println("insertString = " + text);
                 ChangeLabel();
           }

           @Override
           public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {

                 super.replace(fb, offset, length, text, attrs);
                 System.out.println("replace = " + text);
                 ChangeLabel();
           }

           private void ChangeLabel() {
                 String tempstr = String.format("XX=%d",targettextfield.getText().length());
                 targetlabel.setText(tempstr);
           }

      }//class ExampleExpandingDocumentFilter
}//class AdobePDFMakerGUI

